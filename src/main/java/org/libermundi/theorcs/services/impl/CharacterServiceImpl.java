package org.libermundi.theorcs.services.impl;

import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.QChronicle;
import org.libermundi.theorcs.repositories.CharacterRepository;
import org.libermundi.theorcs.services.CharacterService;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.SecurityService;
import org.libermundi.theorcs.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CharacterServiceImpl extends AbstractServiceImpl<Character> implements CharacterService {

    private final UserService userService;

    private final SecurityService securityService;

    private final ChronicleService chronicleService;


    public CharacterServiceImpl(CharacterRepository characterRepository, UserService userService,
                                SecurityService securityService, ChronicleService chronicleService) {
        setRepository(characterRepository,Character.class);
        this.userService = userService;
        this.securityService = securityService;
        this.chronicleService = chronicleService;
    }

    @Override
    public List<Chronicle> findChronicleByPlayer(User player) {
        return ((CharacterRepository)getRepository()).findChronicleByPlayer(player);
    }

    @Override
    public Character createBasicCharacter(User player, Chronicle chronicle, String characterName) {
        Character character = createNew();
            character.setName(characterName);
            character.setChronicle(chronicle);
            character.setPlayer(player);

        return character;
    }

    @Override
    public Character createNew() {
        return new Character();
    }

    @Override
    public void initData() {
        if(log.isDebugEnabled()){
            log.debug("Initializing Character Data");
        }

        //Predicate query = QChronicle.chronicle

        //Chronicle chronicle = chronicleService.

        Character c0 = createNew();
        Character c1 = createNew();
        Character c2 = createNew();
        Character c3 = createNew();


        User admin = userService.findByUsername("admin");
        User user1 = userService.findByUsername("user1");
        User user2 = userService.findByUsername("user2");


    }
}
