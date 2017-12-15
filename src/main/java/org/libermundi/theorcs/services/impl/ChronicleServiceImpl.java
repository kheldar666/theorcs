package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.*;
import org.libermundi.theorcs.repositories.ChronicleRepository;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.GameService;
import org.libermundi.theorcs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChronicleServiceImpl extends AbstractServiceImpl<Chronicle> implements ChronicleService {

    private final UserService userService;

    private final GameService gameService;

    @Autowired
    public ChronicleServiceImpl(ChronicleRepository chronicleRepository, UserService userService, GameService gameService) {
        setRepository(chronicleRepository);
        this.userService = userService;
        this.gameService = gameService;
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

        Chronicle chronicle1 = createNew();
        chronicle1.setTitle("Default Chronicle");
        chronicle1.setSubTitle("A demo chronicle2 for TheORCS");
        chronicle1.setAdmin(userService.findByUsername("admin"));
        chronicle1.setGame(gameService.findByName("Vampire"));
        chronicle1.setGenre(ChronicleGenre.CONTEMPORARY);
        chronicle1.setOpenForInscription(Boolean.TRUE);
        chronicle1.setPace(ChroniclePace.AVERAGE);
        chronicle1.setStyle(ChronicleStyle.AMBIANCE);
        chronicle1.setType(ChronicleType.STANDARD);

        repository.save(chronicle1);

        Chronicle chronicle2 = createNew();
        chronicle2.setTitle("A Second Chronicle");
        chronicle2.setSubTitle("A second demo chronicle for TheORCS");
        chronicle2.setAdmin(userService.findByUsername("admin"));
        chronicle2.setGame(gameService.findByName("Call of Cthulhu"));
        chronicle2.setGenre(ChronicleGenre.CONTEMPORARY);
        chronicle2.setOpenForInscription(Boolean.TRUE);
        chronicle2.setPace(ChroniclePace.AVERAGE);
        chronicle2.setStyle(ChronicleStyle.AMBIANCE);
        chronicle2.setType(ChronicleType.STANDARD);

        repository.save(chronicle2);

    }
}
