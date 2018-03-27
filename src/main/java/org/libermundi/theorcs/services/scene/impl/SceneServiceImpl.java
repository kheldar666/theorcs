package org.libermundi.theorcs.services.scene.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.repositories.scene.SceneRepository;
import org.libermundi.theorcs.services.base.impl.AbstractServiceImpl;
import org.libermundi.theorcs.services.chronicle.CharacterService;
import org.libermundi.theorcs.services.chronicle.ChronicleService;
import org.libermundi.theorcs.services.scene.SceneService;
import org.libermundi.theorcs.services.utils.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Implementation of SceneService
 *
 */
@Slf4j
@Service("SceneService")
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class SceneServiceImpl extends AbstractServiceImpl<Scene> implements SceneService {
	private final String dateFormat;

	private final ChronicleService chronicleService;

	private final PictureService pictureService;

	private final CharacterService characterService;

	public SceneServiceImpl(SceneRepository sceneRepository, ChronicleService chronicleService,
							CharacterService characterService,
							@Value("${theorcs.general.dateformat}") String dateFormat,
							PictureService pictureService) {
		setRepository(sceneRepository, Scene.class);
		this.chronicleService = chronicleService;
		this.dateFormat = dateFormat;
		this.pictureService = pictureService;
		this.characterService = characterService;
	}

	@Override
	public List<Scene> getAllScenes(Chronicle chronicle) {
		List<Scene> sceneList = ((SceneRepository)getRepository()).findAllByChronicle(chronicle);
		return sceneList;
	}

	@Override
	public Scene createNew() {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Scene scene = new Scene();
		scene.setDate(sdf.format(new Date()));
		return scene;
	}

	@Override
	public void initData() {
		Chronicle chronicle = chronicleService.findByTitle("Terror on the Orient Express");
		Character character = characterService.findById(1L);

		Scene s1 = createNew();
		s1.setChronicle(chronicle);
		s1.setLocked(Boolean.FALSE);
		s1.setAuthor(character);
		s1.setTitle("Scène de la nouvelle nuit.");

		save(s1);

		Character character2 = characterService.findById(4L);

		Scene s2 = createNew();
		s2.setChronicle(chronicle);
		s2.setLocked(Boolean.FALSE);
		s2.setAuthor(character2);
		s2.setTitle("Le soir au Louvre.");

		save(s2);
	}

}
