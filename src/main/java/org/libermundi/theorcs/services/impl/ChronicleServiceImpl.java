package org.libermundi.theorcs.services.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.chronicle.*;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.repositories.ChronicleRepository;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.GameService;
import org.libermundi.theorcs.services.SecurityService;
import org.libermundi.theorcs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChronicleServiceImpl extends AbstractServiceImpl<Chronicle> implements ChronicleService {

    private final UserService userService;

    private final GameService gameService;

    private final SecurityService securityService;

    @Autowired
    public ChronicleServiceImpl(ChronicleRepository chronicleRepository, UserService userService, GameService gameService, SecurityService securityService) {
        setRepository(chronicleRepository,Chronicle.class);
        this.userService = userService;
        this.gameService = gameService;
        this.securityService = securityService;
    }

    @Override
    public Chronicle findByName() {
        return null;
    }

    @Override
    public Chronicle createNew() {
        return new Chronicle();
    }

    @Override
    public void initData() {
        if(log.isDebugEnabled()){
            log.debug("Initializing Chronicle Data");
        }

        User admin = userService.findByUsername("admin");

        Chronicle chronicle1 = createNew();
        chronicle1.setTitle("Terror on the Orient Express");
        chronicle1.setSubTitle("A demo Chronicle for TheORCS");
        chronicle1.setAdmin(admin);
        chronicle1.setGame(gameService.findByName("Vampire"));
        chronicle1.setGenre(ChronicleGenre.CONTEMPORARY);
        chronicle1.setOpenForInscription(Boolean.TRUE);
        chronicle1.setPace(ChroniclePace.AVERAGE);
        chronicle1.setStyle(ChronicleStyle.AMBIANCE);
        chronicle1.setType(ChronicleType.STANDARD);

        getRepository().save(chronicle1);

        securityService.grantAdminAcl(admin, chronicle1);
        securityService.setAcl(admin, chronicle1, BasePermission.ADMINISTRATION, Boolean.TRUE);

    }
}
