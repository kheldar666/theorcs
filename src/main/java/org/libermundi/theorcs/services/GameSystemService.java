package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.chronicle.GameSystem;

public interface GameSystemService extends BaseService<GameSystem> {
    GameSystem findByName(String name);
}
