package api.media.kg.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
@JsonInclude(JsonInclude.Include.NON_NULL)  // null маанилерди көрсөтпөө үчүн
public class SimpleResponse {
    private HttpStatus status;
    private String message;
    private String data;


    public SimpleResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public SimpleResponse(HttpStatus status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
