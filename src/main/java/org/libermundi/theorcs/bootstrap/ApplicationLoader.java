package org.libermundi.theorcs.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.services.AuthorityService;
import org.libermundi.theorcs.services.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationLoader implements ApplicationListener<ContextRefreshedEvent> {

	private AuthorityService authorityService;
	
	private UserService userService;
	
	public ApplicationLoader(AuthorityService authorityService, UserService userService) {
		this.authorityService = authorityService;
		this.userService = userService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if(userService.count() == 0) {
			if(log.isDebugEnabled()){
				log.debug("Initializing Data for First launch of TheORCS");
			}

			initData();
		}
	}

	private void initData() {
		userService.initData();
		authorityService.initData();
	}

}
