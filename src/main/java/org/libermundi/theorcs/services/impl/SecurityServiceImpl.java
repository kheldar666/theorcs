package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.security.Authority;
import org.libermundi.theorcs.services.AuthorityService;
import org.libermundi.theorcs.services.SecurityService;
import org.libermundi.theorcs.services.UserService;
import org.libermundi.theorcs.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Martin Papy
 *
 */

@Service
@Transactional
@Slf4j
public class SecurityServiceImpl implements SecurityService {

	private final UserService userService;

	private final RememberMeServices rememberMeServices;

	private final MutableAclService aclServices;

	private final AclPermissionEvaluator aclPermissionEvaluator;

	private final AuthorityService authorityService;

	@Autowired
	public SecurityServiceImpl(AuthorityService authorityService, UserService userService,
							   RememberMeServices rememberMeServices, MutableAclService aclService,
							   AclPermissionEvaluator permissionEvaluator) {
		this.authorityService=authorityService;
		this.userService = userService;
		this.rememberMeServices = rememberMeServices;
		this.aclServices = aclService;
		this.aclPermissionEvaluator = permissionEvaluator;
	}

	/* (non-Javadoc)
	 * @see org.libermundi.security.services.SecurityManager#getCurrentUserDetails()
	 */
	@Override
	public UserDetails getCurrentUserDetails() {
		if (!(getCurrentAuthentication().getPrincipal() instanceof UserDetails)) {
			return ANONYMOUS_USERDETAILS;
		}
		return (UserDetails)getCurrentAuthentication().getPrincipal();
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() {
		UserDetails ud = getCurrentUserDetails();
		if(ud instanceof User) {
			User user = (User)ud;
			if(user.getId() != null) {
				return user;
			} else {
				return userService.findByUsername(user.getUsername())
;			}
		}
		return null;
	}		
	
	/* (non-Javadoc)
	 * @see org.libermundi.security.services.SecurityManager#getCurrentUsername()
	 */
	@Override
	public String getCurrentUsername() {
		if(isLoggedIn()) {
			return getCurrentUserDetails().getUsername();
		}
		return ANONYMOUS_USERDETAILS.getUsername();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		if(log.isInfoEnabled()) {
			log.info("loadUserByUsername({})", username);
		}
		User user = userService.findByUsername(username.toLowerCase());
		if (user == null) {
			throw new UsernameNotFoundException("There is no User with login/username : " + username);
		}
		
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#isLoggedIn()
	 */
	@Override
	public boolean isLoggedIn() {
		Authentication auth = getCurrentAuthentication();

		if(auth == null) {
			return false;
		} else if(auth.isAuthenticated()
				&& (auth.getPrincipal() instanceof UserDetails)) {
			return true; 
		} else {
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#authenticate(org.libermundi.theorcs.security.model.User)
	 */
	@Override
	public Authentication authenticate(User user) {
		Authentication auth = new PreAuthenticatedAuthenticationToken(user, user.getUsername(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		return auth;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#authenticate(org.libermundi.theorcs.security.model.User, boolean, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Authentication authenticate(User user, boolean rememberMe, HttpServletRequest request, HttpServletResponse response ) {
		Authentication auth = authenticate(user);
		if(rememberMe) {
			rememberMeServices.loginSuccess(request, response, auth);
			if(log.isDebugEnabled()) {
				log.debug("Setting RememberMe Token");
			}
		}
		return auth;
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#switchUser(org.springframework.security.core.userdetails.UserDetails)
	 */
	@Override
	public void switchUser(UserDetails switchDetails) {
		Authentication oldAuth = getCurrentAuthentication();

		log.warn("Switching User Authentication : from [ " + getCurrentUsername() + " ] to [ " + switchDetails.getUsername() +" ]");
		
		GrantedAuthority switchedAuth = new SwitchUserGrantedAuthority(ROLE_PREVIOUS_USER, oldAuth);
		ArrayList<GrantedAuthority> newAuthorities = new ArrayList<>(switchDetails.getAuthorities());
		newAuthorities.add(switchedAuth);
		
		Authentication auth2Switch = buildAuthentication(switchDetails, newAuthorities);
				
		SecurityContextHolder.getContext().setAuthentication(auth2Switch);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#restoreUser()
	 */
	@Override
	public void restoreUser() {
		Authentication switchedAuth = getCurrentAuthentication();
		
		GrantedAuthority[] switchedGA = switchedAuth.getAuthorities().toArray(new GrantedAuthority[switchedAuth.getAuthorities().size()]);
		for (int i = 0; i < switchedGA.length; i++) {
			GrantedAuthority ga = switchedGA[i];
			if(ga instanceof SwitchUserGrantedAuthority) {
				Authentication origAuth = ((SwitchUserGrantedAuthority)ga).getSource();
				log.warn("Restoring User Authentication : from [ " + getCurrentUsername() + " ] to [ " + ((UserDetails)origAuth.getPrincipal()).getUsername() +" ]");
				SecurityContextHolder.getContext().setAuthentication(origAuth);
				return;
			}
		}
	
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#getCurrentAuthentication()
	 */
	@Override
	public Authentication getCurrentAuthentication() {
		Authentication authz = SecurityContextHolder.getContext().getAuthentication();
		if(authz == null){
			authz = new AnonymousAuthenticationToken(UUID.randomUUID().toString(), ANONYMOUS_USERDETAILS, ANONYMOUS_USERDETAILS.getAuthorities());
		}
		return authz;
	}

	// ~ ACL Management ------------------------------------------------------------------------------------
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#setAcl(java.lang.Object, java.lang.Object, org.springframework.security.acls.model.Permission, boolean)
	 */
	@Override
	public void setAcl(Object owner, Object to, Permission permission, boolean granting) {
		ObjectIdentity oi = new ObjectIdentityImpl(to);
		Sid sid;
		if(owner instanceof User) {
			sid = new PrincipalSid(((User)owner).getUsername());
		} else {
			sid = new GrantedAuthoritySid(((Authority)owner).getAuthority());
		}

		MutableAcl acl;
		try {
			acl = (MutableAcl)aclServices.readAclById(oi);
		} catch (NotFoundException e) {
			acl = aclServices.createAcl(oi);
		}
		
		acl.insertAce(acl.getEntries().size(), permission, sid, granting);
		aclServices.updateAcl(acl);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#grantAdminAcl(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void grantAdminAcl(Object owner, Object to) {
		this.setAcl(owner, to, BasePermission.ADMINISTRATION, Boolean.TRUE);
		grantReadWriteAcl(owner,to);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#grantReadWriteAcl(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void grantReadWriteAcl(Object owner, Object to) {
		this.setAcl(owner, to, BasePermission.WRITE, Boolean.TRUE);
		grantReadAcl(owner, to);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#grantReadAcl(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void grantReadAcl(Object owner, Object to) {
		this.setAcl(owner, to, BasePermission.READ, Boolean.TRUE);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#hasPermission(java.lang.Object, org.springframework.security.acls.model.Permission[])
	 */
	@Override
	public boolean hasPermission(Object obj, Permission... permission) {
		return aclPermissionEvaluator.hasPermission(getCurrentAuthentication(), obj, permission);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#grantRole(org.libermundi.theorcs.security.model.User, java.lang.String)
	 */
	@Override
	public void grantRole(User user, String role) {
		Authority newAuthority = authorityService.findByAuthority(role);
		Set<Authority> roles = user.getAuthorities();
		if(!roles.contains(newAuthority)){
			roles.add(newAuthority);
			userService.save(user);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#removeRole(org.libermundi.theorcs.security.model.User, java.lang.String)
	 */
	@Override
	public void removeRole(User user, String role) {
		Authority oldRole = authorityService.findByAuthority(role);
		Set<Authority> roles = user.getAuthorities();
		if(roles.contains(oldRole)){
			roles.remove(oldRole);
			userService.save(user);
		}
	}

	// ~ ----------------------------------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#getSystemUserDetails()
	 */
	@Override
	public UserDetails getSystemUserDetails() {
		return SYSTEM_USERDETAILS;
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#getAnonymousUserDetails()
	 */
	@Override
	public UserDetails getAnonymousUserDetails() {
		return ANONYMOUS_USERDETAILS;
	}
	
	private static final UserDetails ANONYMOUS_USERDETAILS=new UserDetails() {
		private static final long serialVersionUID = 4018460955903946952L;
		private final List<GrantedAuthority> _roles = initRoles();

		@Override
		public boolean isEnabled() {
			return Boolean.TRUE;
		}
		
		private List<GrantedAuthority> initRoles() {
			List<GrantedAuthority> roles = new ArrayList<>(1);
			roles.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_ANONYMOUS));
			return roles;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return Boolean.TRUE;
		}
		
		@Override
		public boolean isAccountNonLocked() {
			return Boolean.TRUE;
		}
		
		@Override
		public boolean isAccountNonExpired() {
			return Boolean.TRUE;
		}
		
		@Override
		public String getUsername() {
			return SecurityConstants.USERNAME_ANONYMOUS;
		}
		
		@Override
		public String getPassword() {
			return null;
		}
		
		@Override
		public Collection<GrantedAuthority> getAuthorities() {
			return _roles;
		}
		
	};			
		
	private static final UserDetails SYSTEM_USERDETAILS=new UserDetails() {
		private static final long serialVersionUID = -6974753744857317615L;
		private final List<GrantedAuthority> _roles = initRoles();

		@Override
		public boolean isEnabled() {
			return Boolean.TRUE;
		}
		
		private List<GrantedAuthority> initRoles() {
			List<GrantedAuthority> roles = new ArrayList<>(1);
			roles.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_SYSTEM));
			return roles;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return Boolean.TRUE;
		}
		
		@Override
		public boolean isAccountNonLocked() {
			return Boolean.TRUE;
		}
		
		@Override
		public boolean isAccountNonExpired() {
			return Boolean.TRUE;
		}
		
		@Override
		public String getUsername() {
			return SecurityConstants.USERNAME_SYSTEM;
		}
		
		@Override
		public String getPassword() {
			return null;
		}
		
		@Override
		public Collection<GrantedAuthority> getAuthorities() {
			return _roles;
		}
	};		

	private static Authentication buildAuthentication(UserDetails userdetails, Collection<GrantedAuthority> newAuthorites) {
		return new PreAuthenticatedAuthenticationToken(userdetails, userdetails.getUsername(), newAuthorites);
	}
	
	private static final String ROLE_PREVIOUS_USER = "ROLE_PREVIOUS_USER";

}
