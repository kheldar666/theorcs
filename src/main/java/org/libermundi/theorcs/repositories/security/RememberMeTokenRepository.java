package org.libermundi.theorcs.repositories.security;

import org.libermundi.theorcs.domain.jpa.security.RememberMeToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RememberMeTokenRepository extends CrudRepository<RememberMeToken, Long> {
	Optional<RememberMeToken> findBySeries(String seriesId);
	
	Iterable<RememberMeToken> findAllByUsername(String username);

}
