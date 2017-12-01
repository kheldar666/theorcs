package org.libermundi.theorcs.controllers.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFound(Exception e) {
        log.error("Handling 'EntityNotFoundException'");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/404");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        modelAndView.addObject("customException",e);
        return modelAndView;
    }

}