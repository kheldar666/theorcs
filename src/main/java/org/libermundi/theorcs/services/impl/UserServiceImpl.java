package org.libermundi.theorcs.services.impl;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.repositories.UserRepository;
import org.libermundi.theorcs.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of UserManager
 *
 */
@Service
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		super();
		setRepository(userRepository);
	}
	
	/* (non-Javadoc)
	 * @see org.libermundi.theorcs.main.services.UserManager#getUser()
	 */
	@Override
	public User getUser() {
		User user = new User();
		user.setUid(UUID.randomUUID().toString());
		user.setEnabled(Boolean.FALSE);
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.UserService#findByUsername(java.lang.String)
	 */
	@Override
	public User findByUsername(String username) {
		return ((UserRepository)getRepository()).findByUsername(username);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.UserService#findByNickname(java.lang.String)
	 */
	@Override
	public User findByNickName(String nickname) {
		return ((UserRepository)getRepository()).findByNickName(nickname);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.UserService#findByUID(java.lang.String)
	 */
	@Override
	public User findByUID(String uid) {
		return ((UserRepository)getRepository()).findByUid(uid);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.UserService#isUsernameAvailable(java.lang.String)
	 */
	@Override
	public boolean isUsernameAvailable(String username) {
		if(findByUsername(username) != null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.UserService#isNickNameAvailable(java.lang.String)
	 */
	@Override
	public boolean isNickNameAvailable(String nickname) {
		if(findByNickName(nickname) != null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.UserManager#isEmailAvailable(java.lang.String)
	 */
	@Override
	public boolean isEmailAvailable(String email) {
		if(findByEmail(email) != null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/* (non-Javadoc)
	 * @see org.libermundi.theorcs.security.services.UserManager#getUniqueNickName(java.lang.String)
	 */
	@Override
	public String getUniqueNickName(String nickname) {
		String testNickName = nickname;
		int i = 1;
		while(!isNickNameAvailable(testNickName)){
			testNickName = nickname + "_" + i;
			i++;
		}
		return testNickName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.UserService#findByEmail(java.lang.String)
	 */
	@Override
	public User findByEmail(String email) {
		return ((UserRepository)getRepository()).findByEmail(email);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.Service#createNew()
	 */
	@Override
	public User createNew() {
		User user = new User();
		user.setUid(UUID.randomUUID().toString());
		user.setEnabled(Boolean.FALSE);
		return user;
	}

	@Override
	public void initData() {
		if(logger.isDebugEnabled()){
			logger.debug("Initializing User DB");
		}
		User rootUser = createNew();
			rootUser.setUsername("root");
			rootUser.setPassword("root");
			rootUser.setNickName("Super Admin");
			rootUser.setFirstName("Super Administrator");
			rootUser.setLastName("");
			rootUser.setEmail("root@localhost");
			rootUser.setDeleted(Boolean.FALSE);
			rootUser.setEnabled(Boolean.TRUE);
			rootUser.setAccountNonLocked(Boolean.TRUE);
			rootUser.setAccountNonExpired(Boolean.TRUE);
			rootUser.setCredentialsNonExpired(Boolean.TRUE);
			save(rootUser);				
		
		User adminUser = createNew();
			adminUser.setUsername("admin");
			adminUser.setPassword("admin");
			adminUser.setNickName("Admin");
			adminUser.setFirstName("Administrator");
			adminUser.setLastName("");
			adminUser.setEmail("admin@localhost");
			adminUser.setDeleted(Boolean.FALSE);
			adminUser.setEnabled(Boolean.TRUE);
			adminUser.setAccountNonLocked(Boolean.TRUE);
			adminUser.setAccountNonExpired(Boolean.TRUE);
			adminUser.setCredentialsNonExpired(Boolean.TRUE);
			save(adminUser);
		
		User stdUser1 = createNew();
			stdUser1.setUsername("user1");
			stdUser1.setPassword("password");
			stdUser1.setNickName("User 1");
			stdUser1.setFirstName("John");
			stdUser1.setLastName("Doe");
			stdUser1.setEmail("user1@localhost");
			stdUser1.setDeleted(Boolean.FALSE);
			stdUser1.setEnabled(Boolean.TRUE);
			stdUser1.setAccountNonLocked(Boolean.TRUE);
			stdUser1.setAccountNonExpired(Boolean.TRUE);
			stdUser1.setCredentialsNonExpired(Boolean.TRUE);
			save(stdUser1);
		
		User stdUser2 = createNew();
			stdUser2.setUsername("user2");
			stdUser2.setPassword("password");
			stdUser2.setNickName("User 2");
			stdUser2.setFirstName("John");
			stdUser2.setLastName("Smith");
			stdUser2.setEmail("user2@localhost");
			stdUser2.setDeleted(Boolean.FALSE);
			stdUser2.setEnabled(Boolean.TRUE);
			stdUser2.setAccountNonLocked(Boolean.TRUE);
			stdUser2.setAccountNonExpired(Boolean.TRUE);
			stdUser2.setCredentialsNonExpired(Boolean.TRUE);
			save(stdUser2);	
	}
	
}
