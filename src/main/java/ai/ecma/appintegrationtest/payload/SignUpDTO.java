package ai.ecma.appintegrationtest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpDTO {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    public SignUpDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
