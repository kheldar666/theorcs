package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Game;
import org.libermundi.theorcs.repositories.GameRepository;
import org.libermundi.theorcs.services.GameService;
import org.libermundi.theorcs.services.GameSystemService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class GameServiceImpl extends AbstractServiceImpl<Game> implements GameService {
    private final GameSystemService gameSystemService;

    public GameServiceImpl(GameRepository gameRepository, GameSystemService gameSystemService) {
        setRepository(gameRepository);
        this.gameSystemService = gameSystemService;
    }

    @Override
    public Game findByName(String name) {
        Optional<Game> optional = ((GameRepository)this.repository).findByName(name);
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

        repository.save(vampire);

        Game adc = createNew();
        adc.setName("Call of Cthulhu");
        adc.setGameSystem(gameSystemService.findByName("Chaosium"));

        repository.save(adc);
    }
}
