package org.libermundi.theorcs.repositories.chronicle;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChronicleRepository extends UndeletableRepository<Chronicle, Long> {
    Optional<Chronicle> findByTitle(String title);

}
