package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.security.Authority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends BaseRepository<Authority, Long> {
	
	Optional<Authority> findByAuthority(String authority);

}
