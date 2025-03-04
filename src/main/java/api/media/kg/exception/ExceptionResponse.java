package api.media.kg.exception;

import org.springframework.http.HttpStatus;


public class ExceptionResponse {
    private HttpStatus httpStatus;
    private String className;
    private String message;


    public ExceptionResponse(HttpStatus httpStatus, String className, String message) {
        this.httpStatus = httpStatus;
        this.className = className;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
