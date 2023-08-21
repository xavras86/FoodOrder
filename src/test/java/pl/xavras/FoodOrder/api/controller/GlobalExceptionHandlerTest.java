package pl.xavras.FoodOrder.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import pl.xavras.FoodOrder.domain.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private FieldError fieldError;


    @Test
    public void testHandleException() {
        Exception ex = new Exception("Test exception");
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ModelAndView modelAndView = exceptionHandler.handleException(ex);

        assertEquals("error", modelAndView.getViewName());
        assertEquals("Other exception occurred: [Test exception]", modelAndView.getModel().get("errorMessage"));
    }

    @Test
    public void testDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation");
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ModelAndView modelAndView = exceptionHandler.handleException(ex);

        assertEquals("error", modelAndView.getViewName());
        assertEquals("The provided value is already taken. Please choose a different value.  [Data integrity violation]", modelAndView.getModel().get("errorMessage"));
    }


    @Test
    public void testNotFoundException() {
        NotFoundException ex = new NotFoundException("Resource not found");
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ModelAndView modelAndView = exceptionHandler.handleNoResourceFound(ex);

        assertEquals("error", modelAndView.getViewName());
        assertEquals("Could not find a resource: [Resource not found]", modelAndView.getModel().get("errorMessage"));
    }

}