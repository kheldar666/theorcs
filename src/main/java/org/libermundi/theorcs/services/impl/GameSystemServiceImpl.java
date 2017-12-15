package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Game;
import org.libermundi.theorcs.domain.jpa.chronicle.GameSystem;
import org.libermundi.theorcs.repositories.GameRepository;
import org.libermundi.theorcs.repositories.GameSystemRepository;
import org.libermundi.theorcs.services.GameService;
import org.libermundi.theorcs.services.GameSystemService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class GameSystemServiceImpl extends AbstractServiceImpl<GameSystem> implements GameSystemService {

    public GameSystemServiceImpl(GameSystemRepository gameSystemRepository) {
        setRepository(gameSystemRepository);
    }

    @Override
    public GameSystem findByName(String name) {
        Optional<GameSystem> optional = ((GameSystemRepository)this.repository).findByName(name);
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

        this.repository.save(d10);

        GameSystem d100 = createNew();

        d10.setName("Chaosium");

        this.repository.save(d100);
    }
}
