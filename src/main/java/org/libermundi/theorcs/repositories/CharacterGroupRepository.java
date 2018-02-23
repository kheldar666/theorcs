package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.CharacterGroup;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterGroupRepository extends UndeletableRepository<CharacterGroup, Long> {
    @Query("select distinct cg from CharacterGroup cg where cg.chronicle = ?1 and (?2 member of cg.leaders or ?2 member of cg.members)")
    List<CharacterGroup> findAllByCharacter(Chronicle chronicle, Character character);
}
