package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.CharacterGroup;
import org.libermundi.theorcs.domain.jpa.chronicle.CharacterGroupType;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.repositories.CharacterGroupRepository;
import org.libermundi.theorcs.repositories.SceneRepository;
import org.libermundi.theorcs.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of CharacterGroupService
 *
 */
@Slf4j
@Service("CharacterGroupService")
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class CharacterGroupServiceImpl extends AbstractServiceImpl<CharacterGroup> implements CharacterGroupService {
	private final String dateFormat;
	private final ChronicleService chronicleService;
	private final CharacterService characterService;

	public CharacterGroupServiceImpl(CharacterGroupRepository characterGroupRepository, ChronicleService chronicleService,
									 CharacterService characterService,
									 @Value("${theorcs.general.dateformat}") String dateFormat) {
		setRepository(characterGroupRepository, CharacterGroup.class);
		this.chronicleService = chronicleService;
		this.dateFormat = dateFormat;
		this.characterService = characterService;
	}

	@Override
	public List<CharacterGroup> getAllCharacterGroup(Chronicle chronicle, Character character) {
		List<CharacterGroup> characterGroupList = ((CharacterGroupRepository)getRepository()).findAllByCharacter(chronicle, character);
		return characterGroupList;
	}

	@Override
	public CharacterGroup createNew() {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		CharacterGroup characterGroup = new CharacterGroup();
		characterGroup.setDate(sdf.format(new Date()));
		return characterGroup;
	}

	@Override
	public void initData() {
		Chronicle chronicle = chronicleService.findByTitle("Terror on the Orient Express");
		Character character = characterService.findById(2L);

		CharacterGroup c1 = createNew();
		c1.setChronicle(chronicle);
		c1.setName("La chapelle St Vincent de Paul dans les égouts");
		c1.setReason("Petite chapelle édifiée le 27/09/2017 par Tony. Elle est la propriété exclusive de Siranda.");
		c1.setDescription("Il semble y avoir un petit passage qui débouche sur un couloir. D'ailleurs, il semblerait que cette partie ne date pas de la même époque que le reste du lieu, a priori plus récent. Le couloir mène à une petite chapelle très sobre.\n" +
				"<br/><br/>On y trouve un Autel, sur lequel est dressée une nappe blanche brodée. Un calice y est posé. L'Autel est surplombé par un crucifix. Il y a également un banc.");
		c1.setType(CharacterGroupType.PLACE);
		c1.setOpen(Boolean.FALSE);
		c1.setPublicVisibility(Boolean.FALSE);

		Set<Character> setC = new HashSet<>();
		setC.add(character);

		c1.setLeaders(setC);

		save(c1);
	}
}
