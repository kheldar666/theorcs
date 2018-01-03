package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Game;
import org.libermundi.theorcs.domain.jpa.chronicle.GameSystem;
import org.libermundi.theorcs.repositories.GameRepository;
import org.libermundi.theorcs.repositories.GameSystemRepository;
import org.libermundi.theorcs.services.GameService;
import org.libermundi.theorcs.services.GameSystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service("GameSystemService")
@Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)
public class GameSystemServiceImpl extends AbstractServiceImpl<GameSystem> implements GameSystemService {

    public GameSystemServiceImpl(GameSystemRepository gameSystemRepository) {
        setRepository(gameSystemRepository, GameSystem.class);
    }

    @Override
    public GameSystem findByName(String name) {
        Optional<GameSystem> optional = ((GameSystemRepository)getRepository()).findByName(name);
        if(optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException("GameSystem with name "+ name +" was not found.");
        }

    }

    @Override
    public GameSystem createNew() {
        return new GameSystem();
    }

    @Override
    public void initData() {
        if(log.isDebugEnabled()){
            log.debug("Initializing Game System Data");
        }

        GameSystem d10 = createNew();

        d10.setName("White Wolf (D10)");

        getRepository().save(d10);

        GameSystem d100 = createNew();

        d100.setName("Chaosium");

        getRepository().save(d100);
    }
}
