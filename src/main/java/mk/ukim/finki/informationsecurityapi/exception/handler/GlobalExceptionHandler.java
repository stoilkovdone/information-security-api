package mk.ukim.finki.informationsecurityapi.exception.handler;

import mk.ukim.finki.informationsecurityapi.api.dto.ErrorDTO;
import mk.ukim.finki.informationsecurityapi.exception.BadCredentialsException;
import mk.ukim.finki.informationsecurityapi.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(ResourceNotFoundException ex) {
        return new ErrorDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadCredentialsException(BadCredentialsException ex) {
        return new ErrorDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
    }

}
