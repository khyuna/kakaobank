package kakaobank_api.kakaobank_api.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> handler(RestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("message", e.getMessage());

        return new ResponseEntity<>(resBody, e.getStatus());
    }

}
