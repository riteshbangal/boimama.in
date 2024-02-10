package in.boimama.readstories.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.boimama.readstories.dto.ContactUsRequest;
import in.boimama.readstories.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static in.boimama.readstories.utils.ApplicationConstants.FAILURE_MESSAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService; // Mock the UserService bean

    @InjectMocks
    private UserController userController;

    ContactUsRequest request = new ContactUsRequest();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Test data
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("9912123456");
        request.setMessage("Test message");
    }

    @Test
    void testHealthEndpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/user/health"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

        @Test
    void testContactUserSuccess() throws Exception {
        when(userService.contactUser(any())).thenReturn("Success");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"response\": \"Success\"}"));
    }

    @Test
    void testContactUserFailure() throws Exception {
        when(userService.contactUser(any())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().json("{\"response\": \"" + FAILURE_MESSAGE + "\"}"));
    }

    /**
     * This class provides a custom security configuration specifically for testing purposes.
     * The permitAll() method is used to allow access to the specified endpoints.
     */
    @Configuration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf().disable()
                    .authorizeHttpRequests()
                    .requestMatchers("/author/**", "/story/**", "/user/**").permitAll()
                    .and()
                    .build();
        }
    }
}
