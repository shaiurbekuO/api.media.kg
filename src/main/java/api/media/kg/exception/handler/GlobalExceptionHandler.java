package api.media.kg.exception.handler;

import api.media.kg.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = new LinkedList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException notFoundException){
        return new  ExceptionResponse(HttpStatus.NOT_FOUND,
                notFoundException.getClass().getSimpleName(),
                notFoundException.getMessage());
    }
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyExistException(AlreadyExistException alreadyExistException){
        return new ExceptionResponse(HttpStatus.CONFLICT,
                alreadyExistException.getClass().getSimpleName(),
                alreadyExistException.getMessage());

    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handle(BadRequestException badRequestException){
        return new ExceptionResponse(HttpStatus.BAD_REQUEST,
                badRequestException.getClass().getSimpleName(),
                badRequestException.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse accessDeniedException(AccessDeniedException accessDeniedException){
        return new ExceptionResponse(HttpStatus.BAD_REQUEST,
                accessDeniedException.getClass().getSimpleName(),
                accessDeniedException.getMessage());

    }
}
