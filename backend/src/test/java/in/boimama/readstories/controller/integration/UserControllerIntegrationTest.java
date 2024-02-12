package in.boimama.readstories.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.boimama.readstories.dto.ContactUsRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final static ContactUsRequest request = new ContactUsRequest();

    @BeforeAll
    static void setUp() {
        System.out.println("Starting..");

        // Test data
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("9912123456");
        request.setMessage("Test message");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Finished!");
    }

    @Test
    void testHealthCheck() throws Exception {
        System.out.println("testHealthCheck");
        mockMvc.perform(get("/user/health"))
                .andExpect(status().isOk());
    }

    @Test
    void testContactUserSuccess() throws Exception {
        // Use correct user-input!
        request.setEmail("john.doe@example.com");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"response\": \"success\"}"));
    }

    @Test
    void testContactUserFailure() throws Exception {
        // Use wrong user-input!
        request.setEmail(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}