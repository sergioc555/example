package app.controller;

import app.data.UserEntity;
import app.data.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController =
                new UserController(userRepository, "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    @Test
    void saveUser_ValidUser_SaveSuccessfully() throws IOException {

        UserRequest userRequest = UserRequest.builder()
                .email("abc@gmail.com")
                .name("abc@gmail.com")
                .password("abc@gmail.com")
                .phones(List.of(PhoneRequest.builder().build()))
                .build();

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(UserEntity
                .builder()
                .email("abc@gmail.com")
                .build());

        UserDto savedUserDto = userController.saveUser(userRequest);
        assertNotNull(savedUserDto);
    }

    @Test
    void saveUser_InvalidPassword_ThrowException() {

        UserRequest userRequest = UserRequest.builder()
                .email("abc@gmail.com")
                .name("abc@gmail.com")
                .build();

        userRequest.setPassword("invalid_password");

        assertThrows(RuntimeException.class, () -> userController.saveUser(userRequest));
    }

    @Test
    void saveUser_DuplicateEmail_ThrowException() {
        UserRequest userRequest = UserRequest.builder()
                .email("abc@gmail.com")
                .name("abc@gmail.com")
                .password("abc@gmail.com")
                .build();

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(
                UserEntity
                        .builder()
                        .email("abc@gmail.com")
                        .build()
                ));

        assertThrows(RuntimeException.class, () -> userController.saveUser(userRequest));
    }
}
