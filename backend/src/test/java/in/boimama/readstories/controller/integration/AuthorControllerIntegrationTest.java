package in.boimama.readstories.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.boimama.readstories.dto.AuthorRequest;
import in.boimama.readstories.dto.AuthorResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Important Note: Static variables are shared among all instances of a class.
     * There is only one copy of a static variable, regardless of how many instances of the class are created.
     *
     * In case of non-static, each test method will get the variable/object from the instances associated with those test methods.
     */
    final static AuthorRequest authorRequest = new AuthorRequest();
    private static String testAuthorId;

    @BeforeAll
    static void setUp() {
        System.out.println("Starting..");

        // Test data
        authorRequest.setName("Integration Test");
        authorRequest.setUsername("integration_test");
        authorRequest.setAuthorImage(null);
        authorRequest.setBiography("Sample integration test data for biography!");
        authorRequest.setNumberOfFollowers(10);
        authorRequest.setJoiningDate(LocalDate.parse("2024-01-18"));
        authorRequest.setContactDetails("integration_test@boimama.in");
        authorRequest.setPublishedWorks(Arrays.asList("Sample integration test data for published works."));
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Finished!");
    }

    @Test
    @DisplayName("Health check")
    void testHealthCheck() throws Exception {
        System.out.println("testHealthCheck");
        mockMvc.perform(get("/author/health"))
                .andExpect(status().isOk());
    }

    @Container // TODO: Implement test-container: https://java.testcontainers.org/modules/databases/cassandra/
    private static final CassandraContainer<?> cassandraContainer = new CassandraContainer<>("cassandra:latest")
            .withExposedPorts(9042); // TODO: Not working! Fix this.

    @DynamicPropertySource // TODO: Not working! Fix.
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        // registry.add("spring.data.cassandra.configFile", "/cassandra/config/cassandra-test.conf"::toString);

        // Set dynamic properties for connecting to Cassandra
        registry.add("spring.data.cassandra.contact-points", cassandraContainer::getContactPoint);
        registry.add("spring.data.cassandra.port", "9042"::toString);
        registry.add("spring.data.cassandra.keyspace", "boimama"::toString);
        registry.add("spring.data.cassandra.username", cassandraContainer::getUsername);
        registry.add("spring.data.cassandra.password", cassandraContainer::getPassword);
    }

    @Test
    @DisplayName("Test Containers")
    public void testContainers() {
        // You can use the container IP and port to connect to Cassandra
        String containerIpAddress = cassandraContainer.getContainerIpAddress();
        int containerPort = cassandraContainer.getMappedPort(9042);

        // Example: Check if Cassandra is reachable
        assertTrue(cassandraContainer.isRunning());
        // assertEquals("127.0.0.1:" + containerPort, containerIpAddress + ":" + containerPort); // Failing!
        assertEquals("localhost:" + containerPort, containerIpAddress + ":" + containerPort);
    }

    @Test
    @DisplayName("Get all authors")
    void testGetAllAuthors() throws Exception {
        System.out.println("testGetAllAuthors");

        final ResultActions response = mockMvc.perform(get("/author/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print());;
    }

    @Test
    @Order(3)
    @DisplayName("Get author by Id")
    void testGetAuthorById() throws Exception {
        System.out.println("testGetAuthorById");

        final ResultActions response = mockMvc.perform(get("/author/{id}", testAuthorId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print());
        response.andExpect(jsonPath("$.id", is(testAuthorId)));
        response.andExpect(jsonPath("$.username", is(authorRequest.getUsername())));
    }

    @Test
    @Order(3)
    @DisplayName("Get author's image")
    void testGetAuthorImage() throws Exception {
        System.out.println("testGetAuthorImage");

        final ResultActions response = mockMvc.perform(get("/author/{id}/image", testAuthorId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andDo(print());
        // TODO: Check response image
    }

    @Test
    @Order(1)
    @DisplayName("Add new author")
    void testAddAuthor() throws Exception {
        System.out.println("testAddAuthor");
        MockMultipartFile authorImageFile = new MockMultipartFile(
                "authorImage", // Assuming the name of the file parameter is "authorImage"
                "filename.jpg", // Original filename
                MediaType.TEXT_PLAIN_VALUE, // Content type
                "File content".getBytes() // File content as bytes
        );

        final ResultActions testResponse = mockMvc.perform(
                MockMvcRequestBuilders.multipart("/author/add")
                        .file(authorImageFile)
                        .param("name", authorRequest.getName())
                        .param("username", authorRequest.getUsername())
                        .param("biography", authorRequest.getBiography())
                        .param("numberOfFollowers", String.valueOf(authorRequest.getNumberOfFollowers()))
                        .param("contactDetails", authorRequest.getContactDetails())
                        .param("publishedWorks", String.valueOf(authorRequest.getPublishedWorks()))
        );

        testResponse.andExpect(status().isCreated())
                .andDo(print());

        // Extract the "id" field from the response
        String authorResponse = testResponse.andReturn().getResponse().getContentAsString();
        testAuthorId = objectMapper.readValue(authorResponse, AuthorResponse.class).getId().toString();

        testResponse.andExpect(jsonPath("$.id", is(testAuthorId)));
        testResponse.andExpect(jsonPath("$.username", is(authorRequest.getUsername())));
    }

    @Test
    @Order(2)
    @DisplayName("Update existing author")
    void testUpdateAuthor() throws Exception {
        System.out.println("testUpdateAuthor");
        MockMultipartFile authorImageFile = new MockMultipartFile(
                "authorImage", // Assuming the name of the file parameter is "authorImage"
                "filename.jpg", // Original filename
                MediaType.TEXT_PLAIN_VALUE, // Content type
                "File content".getBytes() // File content as bytes
        );

        final ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.multipart("/author/{id}/update", testAuthorId)
                        .file(authorImageFile)
                        .param("name", authorRequest.getName())
                        .param("username", authorRequest.getUsername())
                        .param("biography", "Updated, " + authorRequest.getBiography())
                        .param("numberOfFollowers", String.valueOf(authorRequest.getNumberOfFollowers() + 1))
                        .param("contactDetails", authorRequest.getContactDetails())
                        .param("publishedWorks", String.valueOf(authorRequest.getPublishedWorks()))
                        .with(request -> {
                            request.setMethod(HttpMethod.PUT.toString()); // For update
                            return request;
                        })
        );

        response.andExpect(status().isOk())
                .andDo(print());;
        response.andExpect(jsonPath("$.id", is(testAuthorId)));
        response.andExpect(jsonPath("$.username", is(authorRequest.getUsername())));
        response.andExpect(jsonPath("$.biography", is("Updated, " + authorRequest.getBiography())));
    }

    @Test
    @Order(4)
    @DisplayName("Delete existing author")
    void testDeleteAuthor() throws Exception {
        System.out.println("testDeleteAuthor");
        final ResultActions response = mockMvc
                .perform(delete("/author/{id}", testAuthorId));

        response.andExpect(status().isAccepted())
                .andDo(print());;
        response.andExpect(jsonPath("$.id", is(testAuthorId)));
    }
}