package org.libermundi.theorcs.domain.jpa.listeners;

import org.libermundi.theorcs.domain.jpa.base.CreatedOrUpdatedBy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class CreatedOrUpdatedByListener {
	public static final String SYSTEM="__System__";

	@PrePersist
	public void setCreatedBy(CreatedOrUpdatedBy c) {
		String username = getCurrentUsername();
		c.setCreatedBy(username);
		c.setUpdatedBy(username);
	}

	@PreUpdate
	public void setUpdatedBy(CreatedOrUpdatedBy c) {
		String username = getCurrentUsername();
		c.setUpdatedBy(username);
	}

	/**
	 * Try to get the current logged in user. If he cannot get logged in user
	 * in any context, he will return <b>System</b> as default
	 *
	 * @return the login name of logged in user.
	 * */

	private static String getCurrentUsername() {

		UserDetails userDetails = getUserDetails(SecurityContextHolder.getContext());

		if (userDetails == null || userDetails.getUsername().isEmpty()) {
			return SYSTEM; // return System as default.
		}

		return userDetails.getUsername();
	}

	private static UserDetails getUserDetails(SecurityContext context) {
		try {
			Object principle = context.getAuthentication().getPrincipal();
			if (principle != null && principle instanceof UserDetails) {
				return (UserDetails) principle;
			}
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}
}
