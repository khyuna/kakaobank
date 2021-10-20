package kakaobank_api.kakaobank_api.advice;

import org.springframework.http.HttpStatus;

//예외처리 
//미완성..
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private String message;

    public RestException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
}
