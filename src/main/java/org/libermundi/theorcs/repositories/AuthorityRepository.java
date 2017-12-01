package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
	Authority findByAuthority(String authority);

}
