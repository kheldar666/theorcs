package org.libermundi.theorcs.services.chronicle.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Game;
import org.libermundi.theorcs.repositories.chronicle.GameRepository;
import org.libermundi.theorcs.services.chronicle.GameService;
import org.libermundi.theorcs.services.chronicle.GameSystemService;
import org.libermundi.theorcs.services.base.impl.AbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service("GameService")
@Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)
public class GameServiceImpl extends AbstractServiceImpl<Game> implements GameService {
    private final GameSystemService gameSystemService;

    public GameServiceImpl(GameRepository gameRepository, GameSystemService gameSystemService) {
        setRepository(gameRepository, Game.class);
        this.gameSystemService = gameSystemService;
    }

    @Override
    public Game findByName(String name) {
        Optional<Game> optional = ((GameRepository)getRepository()).findByName(name);
        if(optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException("Game with name "+ name +" was not found.");
        }

    }

    @Override
    public Game createNew() {
        return new Game();
    }

    @Override
    public void initData() {
        if(log.isDebugEnabled()){
            log.debug("Initializing Game Data");
        }

        Game vampire = createNew();

        vampire.setName("Vampire");
        vampire.setGameSystem(gameSystemService.findByName("White Wolf (D10)"));

        getRepository().save(vampire);

        Game adc = createNew();
        adc.setName("Call of Cthulhu");
        adc.setGameSystem(gameSystemService.findByName("Chaosium"));

        getRepository().save(adc);
    }
}
