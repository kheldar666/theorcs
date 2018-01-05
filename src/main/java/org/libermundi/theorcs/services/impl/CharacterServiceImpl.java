package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.repositories.CharacterRepository;
import org.libermundi.theorcs.services.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("CharacterService")
@Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)
public class CharacterServiceImpl extends AbstractServiceImpl<Character> implements CharacterService {

    private final UserService userService;

    private final SecurityService securityService;

    private final ChronicleService chronicleService;

    private final PictureService pictureService;

    public CharacterServiceImpl(CharacterRepository characterRepository, UserService userService,
                                SecurityService securityService, ChronicleService chronicleService,
                                PictureService pictureService) {
        setRepository(characterRepository,Character.class);
        this.userService = userService;
        this.securityService = securityService;
        this.chronicleService = chronicleService;
        this.pictureService = pictureService;
    }

    @Override
    public List<Chronicle> findChronicleByPlayer(User player) {
        User p = userService.findById(player.getId());
        return ((CharacterRepository)getRepository()).findChronicleByPlayer(p);
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
    public Character getDefaultCharacter(User player, Chronicle chronicle) {
        return ((CharacterRepository)getRepository()).getDefaultCharacter(player,chronicle);
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

        Chronicle chronicle = chronicleService.getLast();

        User admin = userService.findByUsername("admin");
        User user1 = userService.findByUsername("user1");
        User user2 = userService.findByUsername("user2");


        Character c0 = createBasicCharacter(admin,chronicle,"*MJ");

        Picture p0 = pictureService.getPicture(new ClassPathResource("static/images/demo/perso0.jpg"));
        c0.setAvatar(p0);
        c0.setDefaultCharacter(Boolean.TRUE);

        save(c0);

        Character c1 = createBasicCharacter(user1,chronicle,"Harvey Walters");

        Picture p1 = pictureService.getPicture(new ClassPathResource("static/images/demo/perso1.jpg"));
        c1.setAvatar(p1);
        c1.setDefaultCharacter(Boolean.TRUE);

        save(c1);

        Character c2 = createBasicCharacter(user1,chronicle,"John Rambo");
        Picture p2 = pictureService.getPicture(new ClassPathResource("static/images/demo/perso2.jpg"));

        c2.setAvatar(p2);
        c2.setDefaultCharacter(Boolean.FALSE);

        save(c2);

        Character c3 = createBasicCharacter(user2,chronicle,"Bob le Ponce");

        Picture p3 = pictureService.getPicture(new ClassPathResource("static/images/demo/perso3.jpg"));
        c3.setAvatar(p3);
        c3.setDefaultCharacter(Boolean.TRUE);

        save(c3);

        securityService.grantReadAcl(user1,chronicle);
        securityService.grantReadAcl(user2,chronicle);

    }
}
