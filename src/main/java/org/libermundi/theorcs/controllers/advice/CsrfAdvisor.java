package org.libermundi.theorcs.controllers.advice;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CsrfAdvisor {
	private static final Logger logger = LoggerFactory.getLogger(CsrfAdvisor.class);
	
	@Value("${spring.profiles.active}")
	private String env;
	
	@ModelAttribute
    public void addCsrf(Model model) {
		
		if (!Strings.isNullOrEmpty(env) && env.equals("dev")) {
			logger.warn("***********************************");
			logger.warn("Application is in Dev Mode. Adding a fake CSRF Token !");
			CsrfToken token = new DefaultCsrfToken("X-XSRF-TOKEN", "CSRF-Disabled", "CSRF-Disabled");
			model.addAttribute("_csrf", token);
			logger.warn("***********************************");
		}
		
    }

}