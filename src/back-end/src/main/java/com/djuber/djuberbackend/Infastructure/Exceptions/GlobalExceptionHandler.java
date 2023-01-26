package com.djuber.djuberbackend.Infastructure.Exceptions;

import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.UnexpectedTypeException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(),HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistsException.class})
    public ResponseEntity<ErrorObject> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RideNotDoneException.class})
    public ResponseEntity<ErrorObject> handleRideNotDoneException(RideNotDoneException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RideReviewTimePassedException.class})
    public ResponseEntity<ErrorObject> handleRideReviewTimePassedException(RideReviewTimePassedException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CannotUpdateCanceledRideException.class})
    public ResponseEntity<ErrorObject> handleCannotUpdateCanceledRideException(CannotUpdateCanceledRideException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DriverReachedLimitOfActiveHoursException.class})
    public ResponseEntity<ErrorObject> handleDriverReachedLimitOfActiveHoursException(DriverReachedLimitOfActiveHoursException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidReservationStartException.class})
    public ResponseEntity<ErrorObject> handleInvalidReservationStartException(InvalidReservationStartException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorObject> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ChatNotFoundException.class})
    public ResponseEntity<ErrorObject> handleChatNotFoundException(ChatNotFoundException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AdminCreatingChatException.class})
    public ResponseEntity<ErrorObject> handleAdminCreatingChatException(AdminCreatingChatException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RequestAlreadyExistsException.class})
    public ResponseEntity<ErrorObject> handleRequestAlreadyExistsException(RequestAlreadyExistsException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CarWithLicensePlateAlreadyExistsException.class})
    public ResponseEntity<ErrorObject> handleCarWithLicensePlateAlreadyExistsException(CarWithLicensePlateAlreadyExistsException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<ErrorObject> handleLockedException(LockedException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorObject> handleBadCredentialsException(BadCredentialsException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<ErrorObject> handleDisabledException(DisabledException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject("Client account is not verified.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnsupportedSocialProviderExcetpion.class})
    public ResponseEntity<ErrorObject> handleUnsupportedSocialProviderException(UnsupportedSocialProviderExcetpion ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnsupportedCarTypeException.class})
    public ResponseEntity<ErrorObject> handleUnsupportedCarTypeException(UnsupportedCarTypeException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DifferentSocialSigningProvidersException.class})
    public ResponseEntity<ErrorObject> handleDifferentSocialSigningProvidersException(DifferentSocialSigningProvidersException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ErrorObject> handleInvalidTokenException(InvalidTokenException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorObject> handleAuthenticationCredentialsNotFoundException(AuthenticationException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.GATEWAY_TIMEOUT), HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<ErrorObject> handleITokenExpiredException(TokenExpiredException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.EXPECTATION_FAILED), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorObject> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.EXPECTATION_FAILED), HttpStatus.EXPECTATION_FAILED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorObject>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<ErrorObject> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(getErrorObject(error.getDefaultMessage(), HttpStatus.BAD_REQUEST));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UnexpectedTypeException.class})
    public ResponseEntity<ErrorObject> handleUnexpectedTypeException(
            UnexpectedTypeException ex) {

        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.GATEWAY_TIMEOUT), HttpStatus.GATEWAY_TIMEOUT);
    }

    private static ErrorObject getErrorObject(String message, HttpStatus status){
        ErrorObject error= new ErrorObject();
        error.setStatusCode(status.value());
        error.setMessage(message);
        error.setTimeStamp(new Date());
        return error;
    }

}
