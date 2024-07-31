package com.blogger.blogger.exceptions;

import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.enums.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse> handleConstraintViolationException(
            ConstraintViolationException exception) {
        GenericResponse genericResponse = new GenericResponse("02", "An Unknown Error", HttpStatus.BAD_REQUEST);
        log.error("Exception: ", exception);
        return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentExceptionNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            String code, WebRequest webRequest
    ) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        GenericResponse response = new GenericResponse();
        response.setCode(code);
        response.setMessage("Validation error");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        response.setData(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppRequestException.class)
    public ResponseEntity<GenericResponse> handleAppRequestException(AppRequestException ex) {
        GenericResponse genericResponse = new GenericResponse("02", ex.getMessage(), HttpStatus.BAD_REQUEST);
        log.error("Exception: ", ex);
        return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        GenericResponse genericResponse = new GenericResponse("02", ex.getMessage(), HttpStatus.NOT_FOUND);
        log.error("Exception: ", ex);
        return new ResponseEntity<>(genericResponse, HttpStatus.NOT_FOUND);
    }
}



//@Slf4j
//@RestControllerAdvice
//public class CustomExceptionAdvice extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<GenericResponse> handleConstraintViolationException (
//            ConstraintViolationException exception){
//            GenericResponse genericResponse = new GenericResponse("02",
//                    "An Unknown Error", HttpStatus.BAD_REQUEST);
//            log.error("Exception: " , exception);
//        return new ResponseEntity<>(genericResponse, genericResponse.getHttpStatus());
//    }
//
//    protected ResponseEntity<Object> handleMethodArgumentExceptionNotValid (
//            MethodArgumentNotValidException ex, HttpHeaders headers,
//            String code, WebRequest webRequest
//    ){
//        List<String> errors = new ArrayList<>();
//        ex.getBindingResult().getAllErrors().forEach((error)-> {
//            String errorMessage = error.getDefaultMessage();
//            errors.add(errorMessage);
//        });
//
//        GenericResponse response = new GenericResponse();
//        response.setCode(code);
//        response.setMessage("Validation error");
//        response.setHttpStatus(HttpStatus.BAD_REQUEST);
//        response.setData(errors);
//        return new ResponseEntity<>(response, response.getHttpStatus());
//    }
//
//    @ExceptionHandler(AppRequestException.class)
//    public ResponseEntity<GenericResponse> handleAppException (AppRequestException ex){
//        GenericResponse genericResponse = new GenericResponse("02", ex.getMessage(), HttpStatus.BAD_REQUEST);
//        log.error("Exception: " , ex);
//        return new ResponseEntity<>(genericResponse, genericResponse.getHttpStatus());
//    }
//
//
//    @ExceptionHandler(AppRequestException.class)
//    public ResponseEntity<GenericResponse> handleIllegalArgumentException (IllegalArgumentException ex){
//        GenericResponse genericResponse = new GenericResponse("02", ex.getMessage(), HttpStatus.BAD_REQUEST);
//        log.error("Exception: " , ex);
//        return new ResponseEntity<>(genericResponse, genericResponse.getHttpStatus());
//    }
//
//    @ExceptionHandler(AppRequestException.class)
//    public ResponseEntity<GenericResponse> handleNullPointerException (NullPointerException ex){
//        GenericResponse genericResponse = new GenericResponse("02", ex.getMessage(), HttpStatus.BAD_REQUEST);
//        log.error("Exception: " , ex);
//        return new ResponseEntity<>(genericResponse, genericResponse.getHttpStatus());
//    }
//
//}
