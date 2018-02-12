package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.chronicle.Game;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends UndeletableRepository<Game, Long> {
    Optional<Game> findByName(String name);


}
