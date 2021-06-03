package ai.ecma.appintegrationtest.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInDTO {
    @NotBlank(message = "Iltimos username bo'sh bo'lmasin")
    private String username;

    @NotBlank(message = "Iltimos parol bo'sh bo'lmasin")
    private String password;

    private String token;


    public SignInDTO(String token) {
        this.token = token;
    }

    public SignInDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
