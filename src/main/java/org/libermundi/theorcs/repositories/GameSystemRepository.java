package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.chronicle.Game;
import org.libermundi.theorcs.domain.jpa.chronicle.GameSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSystemRepository extends JpaRepository<GameSystem, Long> {
    Optional<GameSystem> findByName(String name);
}
