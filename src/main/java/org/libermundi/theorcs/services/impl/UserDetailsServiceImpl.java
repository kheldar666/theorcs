package org.libermundi.theorcs.services.impl;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserService userService;

    @Autowired
	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		if(logger.isInfoEnabled()) {
			logger.info("loadUserByUsername({})", username);
		}
		User user = userService.findByUsername(username.toLowerCase());
		if (user == null) {
			throw new UsernameNotFoundException("There is no User with login/username : " + username);
		} else {
			if(logger.isInfoEnabled()) {
				logger.info("Found User : {})", user);
			}
		}
		
		return user;
	}
}