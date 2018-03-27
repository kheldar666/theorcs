package org.libermundi.theorcs.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.services.chronicle.*;
import org.libermundi.theorcs.services.messaging.MessagingService;
import org.libermundi.theorcs.services.scene.PostService;
import org.libermundi.theorcs.services.scene.SceneService;
import org.libermundi.theorcs.services.security.AuthorityService;
import org.libermundi.theorcs.services.security.SecurityService;
import org.libermundi.theorcs.services.security.UserService;
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
	private final NewsService newsService;
	private final SceneService sceneService;
	private final PostService postService;
	private final MessagingService messagingService;


	public ApplicationLoader(AuthorityService authorityService, UserService userService, GameService gameService,
							 ChronicleService chronicleService, GameSystemService gameSystemService,
							 SecurityService securityService, CharacterService characterService,
							 NewsService newsService, SceneService sceneService, PostService postService,
							 MessagingService messagingService) {
		this.authorityService = authorityService;
		this.userService = userService;
		this.gameService = gameService;
		this.chronicleService = chronicleService;
		this.gameSystemService = gameSystemService;
		this.securityService = securityService;
		this.characterService = characterService;
		this.newsService = newsService;
		this.sceneService = sceneService;
		this.postService = postService;
		this.messagingService = messagingService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
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
		newsService.initData();
		sceneService.initData();
		postService.initData();

		securityService.restoreUser();

	}

}
