package org.libermundi.theorcs.repositories.chronicle;

import org.libermundi.theorcs.domain.jpa.chronicle.GameSystem;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSystemRepository extends UndeletableRepository<GameSystem, Long> {
    Optional<GameSystem> findByName(String name);

}
