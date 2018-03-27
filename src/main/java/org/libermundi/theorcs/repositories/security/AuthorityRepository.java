package org.libermundi.theorcs.repositories.security;

import org.libermundi.theorcs.domain.jpa.security.Authority;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends UndeletableRepository<Authority, Long> {
	
	Optional<Authority> findByAuthority(String authority);

}
