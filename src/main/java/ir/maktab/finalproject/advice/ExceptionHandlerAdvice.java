package ir.maktab.finalproject.advice;

import ir.maktab.finalproject.controller.dto.Error;
import ir.maktab.finalproject.controller.dto.ResponseTemplate;
import ir.maktab.finalproject.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(value = {AssistanceNotFoundException.class , SubAssistanceNotFoundException.class ,
            CustomerNotFoundException.class , SpecialistNotFoundException.class , UserNotFoundException.class ,
            FileNotFoundException.class , OfferNotFoundException.class , RequestNotFoundException.class })
    public ResponseEntity<ResponseTemplate> notFound(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(404)
                .message("resource not found")
                .errors(Arrays.asList(Error.builder().code(404).message("not found.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = IllegalParameterException.class)
    public ResponseEntity<ResponseTemplate> illegalParameter(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("parameters are illegal.")
                .errors(Arrays.asList(Error.builder().code(400).message("parameters are illegal.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = InvalidBeginRequestException.class)
    public ResponseEntity<ResponseTemplate> invalidBeginRequest(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("cannot begin request.")
                .errors(Arrays.asList(Error.builder().code(400).message("cannot begin request.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = InvalidFinishRequestException.class  )
    public ResponseEntity<ResponseTemplate> invalidFinishRequest(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("cannot finish request.")
                .errors(Arrays.asList(Error.builder().code(400).message("cannot finish request.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = InvalidFileFormatException.class  )
    public ResponseEntity<ResponseTemplate> invalidFile(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(406)
                .message("file format is not supported.")
                .errors(Arrays.asList(Error.builder().code(406).message("not supported.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE ).body(response);
    }

    @ExceptionHandler(value = InvalidOfferRegisterException.class  )
    public ResponseEntity<ResponseTemplate> invalidOfferRegister(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("offer has already been selected.")
                .errors(Arrays.asList(Error.builder().code(400).message("offer has already been selected.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = InvalidOfferSelectionException.class  )
    public ResponseEntity<ResponseTemplate> invalidOfferSelection(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(401)
                .message("cannot select offer for this request.")
                .errors(Arrays.asList(Error.builder().code(401).message("not authorized.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }

    @ExceptionHandler(value = InvalidRequestEvaluationException.class  )
    public ResponseEntity<ResponseTemplate> invalidEvaluation(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("cannot evaluate this request yet.")
                .errors(Arrays.asList(Error.builder().code(400).message("request is not done.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = InvalidRequestStatusException.class  )
    public ResponseEntity<ResponseTemplate> invalidStatus(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("request is not done yet.")
                .errors(Arrays.asList(Error.builder().code(400).message("request is not done.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = InvalidSpecialistOfferException.class  )
    public ResponseEntity<ResponseTemplate> invalidSpecialist(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(401)
                .message("cannot add offers for this request.")
                .errors(Arrays.asList(Error.builder().code(401).message("don't have the assistance.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }

    @ExceptionHandler(value = LowerThanBasePriceException.class  )
    public ResponseEntity<ResponseTemplate> lowerThanBasePrice(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("price is lower than the base price.")
                .errors(Arrays.asList(Error.builder().code(400).message("price is low.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = RequestSettlementException.class  )
    public ResponseEntity<ResponseTemplate> notEnoughCredit(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("you don't have enough credit.")
                .code(400)
                .errors(Arrays.asList(Error.builder().code(400).message("credit is low.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(response);
    }

    @ExceptionHandler(value = UnauthenticatedException.class  )
    public ResponseEntity<ResponseTemplate> unauthenticated(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .message("you are not authenticated.")
                .code(401)
                .errors(Arrays.asList(Error.builder().code(401).message("unauthenticated.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }

    @ExceptionHandler(value = UnauthorizedCustomerException.class  )
    public ResponseEntity<ResponseTemplate> unauthorizedCustomer(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(401)
                .message("you are not authorized.")
                .errors(Arrays.asList(Error.builder().code(401).message("unauthorized.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }

    @ExceptionHandler(value = UnauthorizedSpecialistException.class  )
    public ResponseEntity<ResponseTemplate> unauthorizedSpecialist(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(401)
                .message("you are not authorized.")
                .errors(Arrays.asList(Error.builder().code(401).message("unauthorized.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }

    @ExceptionHandler(value = InvalidCaptchaException.class  )
    public ResponseEntity<ResponseTemplate<Object>> notHuman(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("captcha is invalid.")
                .errors(Arrays.asList(Error.builder().code(400).message("invalid captcha.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }

    @ExceptionHandler(value = InvalidVerificationMailException.class  )
    public ResponseEntity<ResponseTemplate<Object>> invalidMail(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("mail verification failed.")
                .errors(Arrays.asList(Error.builder().code(400).message("invalid mail.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }



    @ExceptionHandler(value = InvalidSpecialistStatusException.class  )
    public ResponseEntity<ResponseTemplate<Object>> invalidSpecialistStatus(){
        ResponseTemplate<Object> response = ResponseTemplate.builder()
                .code(400)
                .message("specialist status is not waiting approval.")
                .errors(Arrays.asList(Error.builder().code(400).message("invalid specialist status.").build()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED ).body(response);
    }
}
