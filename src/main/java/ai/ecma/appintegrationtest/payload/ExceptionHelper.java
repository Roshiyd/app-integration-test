package ai.ecma.appintegrationtest.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHelper {
    /**
     * HUQUQI YO'Q YO'LGA MUROJAAT QILGANDA
     *
     * @param ex
     * @return
     */
//    @ExceptionHandler(value = {ForbiddenException.class})
//    public void handleInvalidInputException(ForbiddenException ex,HttpServletResponse response) {
////        HttpServletResponse response=new HttpServletResponseWrapper();
//        try {
//            response.sendError(403,ex.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()),HttpStatus.FORBIDDEN);
//    }
//
//    /**
//     * TIZIMGA AUTORIZATSIYADAN O'TMAGAN HOLATDA MUROJAAT QILGANDA
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(value = {UnauthorizedException.class})
//    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
//        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
//    }
//
//    /**
//     * SERVERGA BOG'LIQ HAR QANDAY XATO BO'LGANDA
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(value = {Exception.class})
//    public ResponseEntity<?> handleException(Exception ex) {
//        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }


    /**
     * REQUEST VALIDATSIYADAN O'TA OLMAGAN HOLATDA
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * FOYDALANUVCHI TOMONIDAN XATO SODIR BO'LGANDA
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {RestException.class})
    public ResponseEntity<?> handleException(RestException ex) {
        return new ResponseEntity<>(new ApiResult<>(ex.getMessage(), ex.getStatus().value()), ex.getStatus());
    }
}
