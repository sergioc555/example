package app.controller;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

    private String name;

    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El formato del correo electrónico no es válido")
    private String email;
    private String password;
    private List<PhoneRequest> phones;
}
