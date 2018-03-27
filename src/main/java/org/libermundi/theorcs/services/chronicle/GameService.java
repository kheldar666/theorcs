package org.libermundi.theorcs.services.chronicle;

import org.libermundi.theorcs.domain.jpa.chronicle.Game;
import org.libermundi.theorcs.services.base.BaseService;

public interface GameService extends BaseService<Game> {
    Game findByName(String name);
}
