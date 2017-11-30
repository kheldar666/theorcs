package org.libermundi.theorcs.services.impl;

import com.google.common.collect.Lists;
import org.libermundi.theorcs.domain.jpa.Authority;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.repositories.AuthorityRepository;
import org.libermundi.theorcs.services.AuthorityService;
import org.libermundi.theorcs.services.UserService;
import org.libermundi.theorcs.utils.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Implementation of AuthorityService
 *
 */
@Service
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class AuthorityServiceImpl extends AbstractServiceImpl<Authority> implements AuthorityService {
	private static final Logger logger = LoggerFactory.getLogger(AuthorityService.class);
	
	private UserService userService;
	
	@Autowired
	public AuthorityServiceImpl(AuthorityRepository authorityRepository, UserService userService) {
		super();
		setRepository(authorityRepository);
		this.userService = userService;
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
		if(logger.isDebugEnabled()){
			logger.debug("Initializing Authority Data");
		}
		
		Authority a1 = createNew(); a1.setAuthority(SecurityConstants.ROLE_ROOT);
		Authority a2 = createNew(); a2.setAuthority(SecurityConstants.ROLE_ADMIN);
		Authority a3 = createNew(); a3.setAuthority(SecurityConstants.ROLE_USER);
		Authority a4 = createNew(); a4.setAuthority(SecurityConstants.ROLE_ANONYMOUS);
		
		List<Authority> auths = Lists.newArrayList(a1, a2, a3, a4);
		
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
