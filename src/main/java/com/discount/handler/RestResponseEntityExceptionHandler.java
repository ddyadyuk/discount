package com.discount.handler;

import com.discount.error.ErrorResponse;
import com.discount.exception.ClientNotFoundException;
import com.discount.exception.IncorrectWithdrawalAmountException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Objects;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice(basePackages = "com.discount")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ClientNotFoundException.class,
            IncorrectWithdrawalAmountException.class,
            IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequestExceptions(RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), Instant.now());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY,
                                  "Validation error. Check 'errors' field for details.",
                                  Instant.now());

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errorResponse.addValidationError(error.getObjectName(), error.getDefaultMessage());
        }

        return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String error = ex.getParameterName() + " parameter is missing";

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.BAD_REQUEST, error, Instant.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), Instant.now());

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorResponse.addValidationError(violation.getRootBeanClass().getName(),
                                             violation.getPropertyPath() + ": " + violation.getMessage());
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");

        Objects.requireNonNull(ex.getSupportedHttpMethods())
                .forEach(t -> builder.append(t).append(" "));

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, builder.toString(), Instant.now());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED.value()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {

        log.error("Unknown error occurred", exception);

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected exception was thrown",
                                  Instant.now());

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
