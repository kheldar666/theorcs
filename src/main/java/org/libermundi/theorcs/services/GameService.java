package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.chronicle.Game;

public interface GameService extends BaseService<Game> {
    Game findByName(String name);
}
