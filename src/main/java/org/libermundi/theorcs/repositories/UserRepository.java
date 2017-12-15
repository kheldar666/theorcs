package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,QuerydslPredicateExecutor<User> {

	Optional<User> findByUsername(String username);

	Optional<User>  findByNickName(String nickname);

	Optional<User>  findByUid(String uid);

	Optional<User>  findByEmail(String email);

}
