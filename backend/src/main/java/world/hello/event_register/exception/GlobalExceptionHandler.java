package world.hello.event_register.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import world.hello.event_register.domain.dto.ErrorResponseDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponseDto<String> handleInvalidFileException(InvalidFileException ex, WebRequest req) {

        return ErrorResponseDto.<String>builder()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .origin(req.getDescription(true))
                .details("Error occurred due to: " + ex.getClass().getSimpleName() + ". " +
                        "Message: " + ex.getMessage() + ". Please check the file and try again.")
                .build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto<String>> handleResponseStatusException(ResponseStatusException ex, WebRequest req) {
        return ResponseEntity.status(ex.getStatus().value()).body(ErrorResponseDto.<String>builder()
                .statusCode(ex.getStatus().value())
                .message(ex.getMessage())
                .origin(req.getDescription(true))
                .details("Error occurred during request handling. Status: " + ex.getStatus() +
                        ". Message: " + ex.getMessage() +
                        ". This might be due to an issue with the request parameters or server error.")
                .build());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest req) {
        Map<String, String> errMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errMap.put(err.getField(), err.getDefaultMessage()));

        return ErrorResponseDto.<Map<String, String>>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation Failed")
                .origin(req.getDescription(true))
                .details(errMap)
                .build();
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto<String> handleInvalidPathParams(MethodArgumentTypeMismatchException ex, WebRequest req) {
        return ErrorResponseDto.<String>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .origin(req.getDescription(true))
                .details("Argument type mismatch: " + ex.getName()
                        + " expected type: "
                        + ex.getRequiredType())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto<String> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return ErrorResponseDto.<String>builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .origin(request.getDescription(true))
                .details("Resource not found: " + ex.getMessage())
                .build();
    }

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto<String> handleGenericException(GenericException ex, WebRequest request) {
        return ErrorResponseDto.<String>builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .origin(request.getDescription(true))
                .details("A generic error occurred: " + ex.getMessage() + ". Please check the server logs.")
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto<String> handleException(Exception ex, WebRequest request) {
        return ErrorResponseDto.<String>builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred: " + ex.getMessage())
                .origin(request.getDescription(true))
                .details("Unexpected exception: " + ex.getClass().getSimpleName() + ". Please check the server logs.")
                .build();
    }

}
