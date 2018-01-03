package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.security.Authority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChronicleRepository extends BaseRepository<Chronicle, Long> {
    Optional<Chronicle> findByTitle(String title);

}
