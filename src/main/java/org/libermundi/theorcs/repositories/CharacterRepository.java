package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long>, QuerydslPredicateExecutor<Character> {
    List<Chronicle> findChronicleByPlayer(User player);
}
