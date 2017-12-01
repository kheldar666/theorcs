package org.libermundi.theorcs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.libermundi.theorcs.domain.jpa.security.Authority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class SecurityUtils {
	private SecurityUtils(){}
	
	public static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
	
    public static final List<GrantedAuthority> NO_AUTHORITIES = Collections.emptyList();
    
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12); //make sure to have the same value in security-context.xml
    /*
     * Set of characters that is valid. Must be printable, memorable, and "won't
     * break HTML" (i.e., not ' <', '>', '&', '=', ...). or break shell commands
     * (i.e., not ' <', '>', '$', '!', ...). I, L and O are good to leave out,
     * as are numeric zero and one.
     */
    private static char[] _goodChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
        'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
        'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '@', };    

    private static java.util.Random _random = new java.util.Random();
    
    /**
     * Returns true if the current user has the specified authority.
     *
     * @param authority the authority to test for (e.g. "ROLE_A").
     * @return true if a GrantedAuthority object with the same string representation as the supplied authority
     * name exists in the current user's list of authorities. False otherwise, or if the user in not authenticated.
     */
    public static boolean userHasAuthority(GrantedAuthority authority) {
        List<GrantedAuthority> authorities = getUserAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.getAuthority().equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Returns true if the current user has the specified authority.
     *
     * @param authority the authority to test for (e.g. "ROLE_A").
     * @return true if a GrantedAuthority object with the same string representation as the supplied authority
     * name exists in the current user's list of authorities. False otherwise, or if the user in not authenticated.
     */
    public static boolean userHasAuthority(String authority) {
        return userHasAuthority(new Authority(authority));
    }

    /**
     * Returns the authorities of the current user.
     *
     * @return an array containing the current user's authorities (or an empty array if not authenticated), never null.
     */
    private static List<GrantedAuthority> getUserAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getAuthorities() == null) {
            return NO_AUTHORITIES;
        }
        
		List<GrantedAuthority> lAuth = new ArrayList<>(0);
		Collection<? extends GrantedAuthority> arrAuth = auth.getAuthorities();
		for (Iterator<? extends GrantedAuthority> iterator = arrAuth.iterator(); iterator.hasNext();) {
			GrantedAuthority grantedAuthority = iterator.next();
			lAuth.add(grantedAuthority);
		}
		
		return lAuth;
    }


    /**
     * Creates a array of GrantedAuthority objects from a comma-separated string
     * representation (e.g. "ROLE_A, ROLE_B, ROLE_C").
     *
     * @param authorityString the comma-separated string
     * @return the authorities created by tokenizing the string
     */
    public static List<GrantedAuthority> commaSeparatedStringToAuthorityList(String authorityString) {
        return createAuthorityList(StringUtils.tokenizeToStringArray(authorityString, ","));
    }

    /**
     * Converts an array of GrantedAuthority objects to a Set.
     * @return a Set of the Strings obtained from each call to GrantedAuthority.getAuthority()
     */
    public static Set<String> authorityListToSet(List<GrantedAuthority> authorities) {
        Set<String> set = new HashSet<>(authorities.size());

        for (GrantedAuthority authority: authorities) {
            set.add(authority.getAuthority());
        }

        return set;
    }

    public static List<GrantedAuthority> createAuthorityList(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);

        for (int i=0; i < roles.length; i++) {
            authorities.add(new SimpleGrantedAuthority(roles[i]));
        }

        return authorities;
    }
    
	public static boolean isInRole(List<GrantedAuthority> roles) {
		for (Iterator<GrantedAuthority> iterator = roles.iterator(); iterator.hasNext();) {
			GrantedAuthority role = iterator.next();
			if(SecurityUtils.userHasAuthority(role)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;	
	}
	
	public static String generatePassword(int lenght) {
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < lenght; i++) {
	      sb.append(_goodChar[_random.nextInt(_goodChar.length)]);
	    }
	    return sb.toString();
	}

	public static boolean isValid(String s) {
		if(logger.isDebugEnabled()) {
			logger.debug("Checking if Password is BCrypt Hashed");
			logger.debug("\t s.startsWith(\"$2a$12$\") =" + s.startsWith("$2a$12$"));
			logger.debug("\t s.length() = " + s.length());
		}
	    return s.startsWith("$2a$12$") && s.length() == 60; //Typical start/length for a BCrypt hashed password with Log 12 
	}
	
	public static String encodePassword(String password) {
        String encoded = passwordEncoder.encode(password);
		if(logger.isDebugEnabled()) {
			logger.debug("Encoded Password using BCrypt : " + password + " >>> " + encoded);
		}
		return encoded;
	}
	
	public static boolean isPasswordMatch(String presentedPass, String storedPass) {
        return passwordEncoder.matches(presentedPass,storedPass);
	}

}
