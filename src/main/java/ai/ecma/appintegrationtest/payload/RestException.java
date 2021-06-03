package ai.ecma.appintegrationtest.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestException extends RuntimeException {

    private String userMsg;
    private HttpStatus status;

    public RestException(String userMsg, HttpStatus status) {
        super(userMsg);
        this.userMsg = userMsg;
        this.status = status;
    }

}
