package org.libermundi.theorcs.services.chronicle;

import org.libermundi.theorcs.domain.jpa.security.User;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.base.BaseService;

import java.util.List;

public interface CharacterService extends BaseService<Character> {

    Character createBasicCharacter(User player, Chronicle chronicle, String characterName);

    List<Chronicle> findChronicleByPlayer(User player);

    Character getDefaultCharacter(User player, Chronicle chronicle);

    List<Character> getAllCharacters(User player, Chronicle chronicle);

    List<Character> getAllContacts(User player, Chronicle chronicle);
}
