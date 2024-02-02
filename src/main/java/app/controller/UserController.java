package app.controller;

import app.data.PhoneEntity;
import app.data.UserEntity;
import app.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    private String passwordRegex;

    @Autowired
    public UserController(UserRepository userRepository, @Value("${password.regex}") String passwordRegex) {
        this.userRepository = userRepository;
        this.passwordRegex = passwordRegex;
    }
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto saveUser(@RequestBody @Valid UserRequest userRequestDto) {
        var optUserEntityFound = userRepository.findByEmail(userRequestDto.getEmail());

        if(userRequestDto.getPassword() == null || !userRequestDto.getPassword().matches(passwordRegex)){
            throw new RuntimeException("Password no valido");
        }

        if (optUserEntityFound.isPresent()) {
            throw new RuntimeException("Correo ya registrado");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDto.getName());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setPassword(userRequestDto.getPassword());

        List<PhoneEntity> phoneEntities = userRequestDto.getPhones().stream()
                .map(phoneDto -> {
                    PhoneEntity phoneEntity = PhoneEntity.builder()
                            .number(phoneDto.getNumber())
                            .citycode(phoneDto.getCitycode())
                            .countrycode(phoneDto.getCountrycode())
                            .build();
                    return phoneEntity;
                })
                .collect(Collectors.toList());

        userEntity.setPhones(phoneEntities);
        userEntity.setToken(UUID.randomUUID().toString());
        var savedUserEntity = userRepository.save(userEntity);
        return savedUserEntity.toDtoUser();
    }
}
