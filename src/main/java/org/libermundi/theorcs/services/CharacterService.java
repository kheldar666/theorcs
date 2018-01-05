package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;

import java.util.List;

public interface CharacterService extends BaseService<Character> {

    Character createBasicCharacter(User player, Chronicle chronicle, String characterName);

    List<Chronicle> findChronicleByPlayer(User player);

    Character getDefaultCharacter(User player, Chronicle chronicle);
}
