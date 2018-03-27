package org.libermundi.theorcs.services.chronicle;

import org.libermundi.theorcs.domain.jpa.chronicle.GameSystem;
import org.libermundi.theorcs.services.base.BaseService;

public interface GameSystemService extends BaseService<GameSystem> {
    GameSystem findByName(String name);
}
