package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends UndeletableRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User>  findByNickName(String nickname);

	Optional<User>  findByUid(String uid);

	Optional<User>  findByEmail(String email);

}
