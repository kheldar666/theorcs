package org.libermundi.theorcs.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.services.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationLoader implements ApplicationListener<ContextRefreshedEvent> {

	private final AuthorityService authorityService;
	
	private final UserService userService;

	private final GameService gameService;

	private final ChronicleService chronicleService;

	private final GameSystemService gameSystemService;

	private final SecurityService securityService;

	private final CharacterService characterService;

	public ApplicationLoader(AuthorityService authorityService, UserService userService, GameService gameService,
							 ChronicleService chronicleService, GameSystemService gameSystemService, SecurityService securityService,
							CharacterService characterService ) {
		this.authorityService = authorityService;
		this.userService = userService;
		this.gameService = gameService;
		this.chronicleService = chronicleService;
		this.gameSystemService = gameSystemService;
		this.securityService = securityService;
		this.characterService = characterService;
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
		securityService.switchUser(securityService.getSystemUserDetails());

		userService.initData();
		authorityService.initData();
		gameSystemService.initData();
		gameService.initData();
		chronicleService.initData();
		characterService.initData();

		securityService.restoreUser();
	}
}
