package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.CharacterGroup;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;

import java.util.List;

public interface CharacterGroupService extends BaseService<CharacterGroup> {
    List<CharacterGroup> getAllCharacterGroup(Chronicle chronicle, Character character);
}
