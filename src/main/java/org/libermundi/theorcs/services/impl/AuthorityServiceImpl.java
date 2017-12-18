package org.libermundi.theorcs.services.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.security.Authority;
import org.libermundi.theorcs.repositories.AuthorityRepository;
import org.libermundi.theorcs.services.AuthorityService;
import org.libermundi.theorcs.services.UserService;
import org.libermundi.theorcs.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of AuthorityService
 *
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class AuthorityServiceImpl extends AbstractServiceImpl<Authority> implements AuthorityService {

	private final UserService userService;

	private final RoleHierarchy roleHierarchy;
	
	@Autowired
	public AuthorityServiceImpl(AuthorityRepository authorityRepository, UserService userService,
								RoleHierarchy roleHierarchy) {
		setRepository(authorityRepository,Authority.class);
		this.userService = userService;
		this.roleHierarchy = roleHierarchy;
	}

	@Override
	public Authority findByAuthority(String authority) {
		Optional<Authority> result = ((AuthorityRepository)getRepository()).findByAuthority(authority);
		if(result.isPresent()) {
			return result.get();
		} else {
			throw new EntityNotFoundException("Authority [" + authority + "] not found.");
		}
	}

	/* (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#hasReachableAuthority(java.util.Collection, org.springframework.security.core.GrantedAuthority)
	 */
	@Override
	public boolean hasReachableAuthority(
			Collection<? extends GrantedAuthority> authorities, GrantedAuthority authority) {
		return roleHierarchy.getReachableGrantedAuthorities(authorities).contains(authority);
	}

	/* (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.SecurityManager#hasReachableAuthority(org.libermundi.theorcs.security.model.User, org.springframework.security.core.GrantedAuthority)
	 */
	@Override
	public boolean hasReachableAuthority(User user, GrantedAuthority authority) {
		return hasReachableAuthority(user.getAuthorities(),authority);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#createNew()
	 */
	@Override
	public Authority createNew() {
		Authority auth = new Authority();

		return auth;
	}

	@Override
	public void initData() {
		if(log.isDebugEnabled()){
			log.debug("Initializing Authority Data");
		}

		Authority a0 = createNew(); a0.setAuthority(SecurityConstants.ROLE_SYSTEM);
		Authority a1 = createNew(); a1.setAuthority(SecurityConstants.ROLE_ROOT);
		Authority a2 = createNew(); a2.setAuthority(SecurityConstants.ROLE_ADMIN);
		Authority a3 = createNew(); a3.setAuthority(SecurityConstants.ROLE_USER);
		Authority a4 = createNew(); a4.setAuthority(SecurityConstants.ROLE_ANONYMOUS);
		
		List<Authority> auths = Lists.newArrayList(a0, a1, a2, a3, a4);
		
		saveAll(auths);
		
		User root = userService.findByUsername("root");
		Set<Authority> r1 = root.getAuthorities();
			r1.add(a1);
			userService.save(root);
		
		User admin = userService.findByUsername("admin");
		Set<Authority> r2 = admin.getAuthorities();
			r2.add(a2);
			userService.save(admin);

		User user1 = userService.findByUsername("user1");
		Set<Authority> r3 = user1.getAuthorities();
			r3.add(a3);
			userService.save(user1);

		User user2 = userService.findByUsername("user2");
		Set<Authority> r4 = user2.getAuthorities();
			r4.add(a3);
			userService.save(user2);
	}
}
