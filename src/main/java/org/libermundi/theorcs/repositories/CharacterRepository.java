package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.security.Authority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends BaseRepository<Character, Long> {

    @Query("select distinct c.chronicle from Character c where c.player = ?1 order by c.chronicle.title")
    List<Chronicle> findChronicleByPlayer(User player);

    @Query("select c from Character c where c.player = ?1 and c.chronicle = ?2 and c.defaultCharacter = true")
    Character findDefaultCharacter(User player, Chronicle chronicle);

    @Query("select c from Character c where c.player = ?1 and c.chronicle = ?2")
    List<Character> findAllCharactersByAndSort(User player, Chronicle chronicle, Sort sort);

    @Query("select c from Character c where not c.player = ?1 and c.chronicle = ?2")
    List<Character> findAllContactsByAndSort(User player, Chronicle chronicle, Sort sort);
}
