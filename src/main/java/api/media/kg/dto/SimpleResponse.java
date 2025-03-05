package api.media.kg.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class SimpleResponse {
    private HttpStatus status;
    private String message;

    public SimpleResponse(HttpStatus status, String message) {
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

    public void setMessage(String message) {
        this.message = message;
    }
}
