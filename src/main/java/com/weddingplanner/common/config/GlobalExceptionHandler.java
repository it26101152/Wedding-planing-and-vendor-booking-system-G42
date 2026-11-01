package com.weddingplanner.common.config;

import com.weddingplanner.common.exception.DataFileException;
import com.weddingplanner.common.exception.DuplicateResourceException;
import com.weddingplanner.common.exception.ResourceNotFoundException;
import com.weddingplanner.common.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        if ("Please login first".equals(message)) {
            // A restarted application does not retain the old browser session.
            return new ModelAndView("redirect:/login?expired=1");
        }
        if ("Admin only".equals(message)) {
            return new ModelAndView("redirect:/login?denied=1");
        }
        return error(message == null ? "Please check the information you entered." : message);
    }

    @ExceptionHandler({ResourceNotFoundException.class, DuplicateResourceException.class,
            ValidationException.class, DataFileException.class})
    public ModelAndView handleKnown(RuntimeException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ModelAndView handleBadRequest(Exception ex) {
        return error("A required field is missing or has an invalid value. Please check the form and try again.");
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnexpected(Exception ex) {
        return error("Something went wrong. Please try again or contact the administrator.");
    }

    private ModelAndView error(String message) {
        ModelAndView view = new ModelAndView("error");
        view.addObject("message", message);
        return view;
    }
}
