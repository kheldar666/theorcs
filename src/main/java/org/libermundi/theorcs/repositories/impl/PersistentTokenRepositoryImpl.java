package org.libermundi.theorcs.repositories.impl;

import org.libermundi.theorcs.domain.jpa.security.RememberMeToken;
import org.libermundi.theorcs.repositories.security.RememberMeTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;
import java.util.Optional;

public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {
	private static final Logger logger = LoggerFactory.getLogger(PersistentTokenRepositoryImpl.class);
	private final RememberMeTokenRepository rememberMeTokenRepository;
	
	public PersistentTokenRepositoryImpl(RememberMeTokenRepository rememberMeTokenRepository) {
		this.rememberMeTokenRepository = rememberMeTokenRepository;
	}

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		RememberMeToken tokenToSave = transform(token);
		if(logger.isDebugEnabled()) {
			logger.debug("Saving RememberMeToken : " + tokenToSave);
		}
		rememberMeTokenRepository.save(tokenToSave);

	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		Optional<RememberMeToken> result = rememberMeTokenRepository.findBySeries(series);
		RememberMeToken token;
		if(result.isPresent()){
			token = result.get();
		} else {
			token = new RememberMeToken();
			token.setSeries(series);
		}

		token.setDate(lastUsed);
		token.setTokenValue(tokenValue);

		if(logger.isDebugEnabled()) {
			logger.debug("Updating RememberMeToken : " + token);
		}

		rememberMeTokenRepository.save(token);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		Optional<RememberMeToken> result = rememberMeTokenRepository.findBySeries(seriesId);
		if(!result.isPresent()){
			return null;
		}
		return transform(result.get());
	}

	@Override
	public void removeUserTokens(String username) {
		Iterable<RememberMeToken> listToDelete = rememberMeTokenRepository.findAllByUsername(username);
		if(logger.isDebugEnabled()) {
			logger.debug("Deleting RememberMeToken : " + listToDelete);
		}
		rememberMeTokenRepository.deleteAll(listToDelete);
	}

	private static PersistentRememberMeToken transform(RememberMeToken token){
		if(token != null) {
			return new PersistentRememberMeToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
		}
		return null;
	}

	private static RememberMeToken transform(PersistentRememberMeToken token){
		return new RememberMeToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
	}
	
}
