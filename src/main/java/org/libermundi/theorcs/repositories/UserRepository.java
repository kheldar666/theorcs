package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByNickName(String nickname);

	User findByUid(String uid);

	User findByEmail(String email);

}
