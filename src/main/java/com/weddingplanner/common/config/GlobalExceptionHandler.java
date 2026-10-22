package com.weddingplanner.common.config;

import com.weddingplanner.common.exception.DataFileException;
import com.weddingplanner.common.exception.DuplicateResourceException;
import com.weddingplanner.common.exception.ResourceNotFoundException;
import com.weddingplanner.common.exception.ValidationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/** Sends all shared exceptions to /WEB-INF/views/error.jsp with a readable message. */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, DuplicateResourceException.class,
            ValidationException.class, DataFileException.class})
    public ModelAndView handleKnown(RuntimeException ex) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", ex.getMessage());
        return mv;
    }
}
