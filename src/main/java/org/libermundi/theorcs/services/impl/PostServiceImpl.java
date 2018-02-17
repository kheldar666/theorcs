package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.scene.Post;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.repositories.PostRepository;
import org.libermundi.theorcs.repositories.SceneRepository;
import org.libermundi.theorcs.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Implementation of PostService
 *
 */
@Slf4j
@Service("PostService")
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class PostServiceImpl extends AbstractServiceImpl<Post> implements PostService {
	private final String dateFormat;

	private final ChronicleService chronicleService;
	private final CharacterService characterService;
	private final SceneService sceneService;

	public PostServiceImpl(PostRepository postRepository, ChronicleService chronicleService,
						   CharacterService characterService,
						   @Value("${theorcs.general.dateformat}") String dateFormat,
						   SceneService sceneService) {
		setRepository(postRepository, Post.class);
		this.chronicleService = chronicleService;
		this.dateFormat = dateFormat;
		this.sceneService = sceneService;
		this.characterService = characterService;
	}

	@Override
	public List<Post> getAllPosts(Scene scene) {
		List<Post> postList = ((PostRepository)getRepository()).findAllByScene(scene);
		return postList;
	}

	@Override
	public Post createNew() {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Post post = new Post();
		post.setDate(sdf.format(new Date()));
		return post;
	}

	@Override
	public void initData() {
		Character character = characterService.findById(1L);
		Scene scene = sceneService.findById(1L);

		Post p1 = createNew();
		p1.setContent(StringFormatService.LOREM_IPSUM);
		p1.setPerso(character);
		p1.setScene(scene);

		save(p1);

		Character character2 = characterService.findById(4L);

		Post p2 = createNew();
		p2.setContent(StringFormatService.LOREM_IPSUM);
		p2.setPerso(character2);
		p2.setScene(scene);

		save(p2);

		Post p3 = createNew();
		p3.setContent(StringFormatService.LOREM_IPSUM);
		p3.setPerso(character);
		p3.setScene(scene);

		save(p3);

		Post p4 = createNew();
		p4.setContent(StringFormatService.LOREM_IPSUM);
		p4.setPerso(character2);
		p4.setScene(scene);

		save(p4);
	}
}
