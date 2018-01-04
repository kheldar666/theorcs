package org.libermundi.theorcs.controllers.advice;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class CsrfAdvice {
	@Value("${spring.profiles.active}")
	private String env;

	private boolean informed = Boolean.FALSE;

	@ModelAttribute
    public void addCsrf(Model model) {
		
		if (!Strings.isNullOrEmpty(env) && env.equals("dev") && !informed) {
			log.warn("************************************************************************");
			log.warn("* Application is in Dev Mode. Adding a fake CSRF Token to all Requests *");
			log.warn("************************************************************************");
			informed = Boolean.TRUE;
		}

		if (!Strings.isNullOrEmpty(env) && env.equals("dev")) {
			CsrfToken token = new DefaultCsrfToken("X-XSRF-TOKEN", "CSRF-Disabled", "CSRF-Disabled");
			model.addAttribute("_csrf", token);
		}
    }

}