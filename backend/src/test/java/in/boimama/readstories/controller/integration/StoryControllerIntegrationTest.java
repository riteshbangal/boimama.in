package in.boimama.readstories.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.boimama.readstories.dto.StoryRequest;
import in.boimama.readstories.dto.StoryResponse;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoryControllerIntegrationTest {

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
    final static StoryRequest storyRequest = new StoryRequest();
    private static String testStoryId;

    @BeforeAll
    static void setUp() {
        System.out.println("Starting..");

        // Test data
        storyRequest.setTitle("Integration Test");
        storyRequest.setDescription("Sample integration test data for story-description");
        storyRequest.setContent("Sample integration test data for story-content");
        storyRequest.setStoryImage(null);
        storyRequest.setCategory("IntegrationTest");
        storyRequest.setPublishedDate(LocalDate.parse("2024-01-18"));
        storyRequest.setAuthorIds(Arrays.asList("672d35f6-0642-47cb-98d0-09cc51ae5e2c"));
        storyRequest.setAuthorNames(Arrays.asList("আই লেখক"));
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Finished!");
    }

    @Test
    @DisplayName("Health check")
    void testHealthCheck() throws Exception {
        System.out.println("testHealthCheck");
        mockMvc.perform(get("/story/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all stories")
    void testGetAllStories() throws Exception {
        System.out.println("testGetAllStories");

        final ResultActions response = mockMvc.perform(get("/story/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print());;
    }

    @Test
    @Order(1)
    @DisplayName("Add new story")
    void testAddStory() throws Exception {
        System.out.println("testAddStory");
        MockMultipartFile storyImageFile = new MockMultipartFile(
                "storyImage", // Assuming the name of the file parameter is "storyImage"
                "filename.jpg", // Original filename
                MediaType.TEXT_PLAIN_VALUE, // Content type
                "File content".getBytes() // File content as bytes
        );

        final ResultActions testResponse = mockMvc.perform(
                MockMvcRequestBuilders.multipart("/story/add")
                        .file(storyImageFile)
                        .param("title", storyRequest.getTitle())
                        .param("description", storyRequest.getDescription())
                        .param("content", storyRequest.getContent())
                        .param("category", storyRequest.getCategory())
                        .param("contactDetails", storyRequest.getPublishedDate().toString())
                        .param("authorIds", storyRequest.getAuthorIds().get(0))
                        .param("authorNames", storyRequest.getAuthorNames().get(0))
        );

        testResponse.andExpect(status().isCreated())
                .andDo(print());

        // Extract the "id" field from the response
        String storyResponse = testResponse.andReturn().getResponse().getContentAsString();
        testStoryId = objectMapper.readValue(storyResponse, StoryResponse.class).getId().toString();

        testResponse.andExpect(jsonPath("$.id", is(testStoryId)));
        testResponse.andExpect(jsonPath("$.title", is(storyRequest.getTitle())));
    }

    @Test
    @Order(2)
    @DisplayName("Update existing story")
    void testUpdateStory() throws Exception {
        System.out.println("testUpdateStory");
        MockMultipartFile storyImageFile = new MockMultipartFile(
                "storyImage", // Assuming the name of the file parameter is "storyImage"
                "filename.jpg", // Original filename
                MediaType.TEXT_PLAIN_VALUE, // Content type
                "File content".getBytes() // File content as bytes
        );

        final ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.multipart("/story/{id}/update", testStoryId)
                        .file(storyImageFile)
                        .param("title", storyRequest.getTitle())
                        .param("description", "Updated, " + storyRequest.getDescription())
                        .param("content", storyRequest.getContent())
                        .param("category", storyRequest.getCategory())
                        .param("contactDetails", storyRequest.getPublishedDate().toString())
                        .param("authorIds", storyRequest.getAuthorIds().get(0))
                        .param("authorNames", storyRequest.getAuthorNames().get(0))
                        .with(request -> {
                            request.setMethod(HttpMethod.PUT.toString()); // For update
                            return request;
                        })
        );

        response.andExpect(status().isOk())
                .andDo(print());;
        response.andExpect(jsonPath("$.id", is(testStoryId)));
        response.andExpect(jsonPath("$.title", is(storyRequest.getTitle())));
        response.andExpect(jsonPath("$.description", is("Updated, " + storyRequest.getDescription())));
    }

    @Test
    @Order(3)
    @DisplayName("Get story by Id")
    void testGetStoryById() throws Exception {
        System.out.println("testGetStoryById");

        final ResultActions response = mockMvc.perform(get("/story/{id}", testStoryId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print());
        response.andExpect(jsonPath("$.id", is(testStoryId)));
        response.andExpect(jsonPath("$.title", is(storyRequest.getTitle())));
    }

    @Test
    @Order(3)
    @DisplayName("Search story by keyword")
    void testSearchStoryByKeyword() throws Exception {
        System.out.println("testSearchStoryByKeyword");

        final ResultActions storySearchResponse = mockMvc.perform(get("/story/search?searchText={searchText}", "IntegrationTest")
                .contentType(MediaType.APPLICATION_JSON));

        storySearchResponse.andExpect(status().isOk())
                .andDo(print());

        // Extract stories from the response
        final List<LinkedHashMap> searchedStories = objectMapper
                .readValue(storySearchResponse.andReturn().getResponse().getContentAsString(), List.class);
        assertFalse(searchedStories.isEmpty());

        final LinkedHashMap storyAttributes = searchedStories.get(0);
        assertEquals(testStoryId, storyAttributes.get("id"));
        assertEquals(storyRequest.getTitle(), storyAttributes.get("title"));
    }

    @Test
    @Order(3)
    @DisplayName("Search story by category")
    void testSearchStoryByCategory() throws Exception {
        System.out.println("testSearchStoryByCategory");

        final ResultActions storySearchResponse = mockMvc.perform(get("/story/search?searchText={searchText}&categorySearch={categorySearch}",
                storyRequest.getCategory(), true)
                .contentType(MediaType.APPLICATION_JSON));

        storySearchResponse.andExpect(status().isOk())
                .andDo(print());

        // Extract stories from the response
        final List<LinkedHashMap> searchedStories = objectMapper
                .readValue(storySearchResponse.andReturn().getResponse().getContentAsString(), List.class);
        assertFalse(searchedStories.isEmpty());

        final LinkedHashMap storyAttributes = searchedStories.get(0);
        assertEquals(testStoryId, storyAttributes.get("id"));
        assertEquals(storyRequest.getTitle(), storyAttributes.get("title"));
    }

    @Test
    @Order(3)
    @DisplayName("Search story by tag")
    void testSearchStoryByTag() throws Exception {
        System.out.println("testSearchStoryByTag");

        final ResultActions storySearchResponse = mockMvc.perform(get("/story/search?searchText={searchText}&categorySearch={categorySearch}",
                "IntegrationTest", false)
                .contentType(MediaType.APPLICATION_JSON));

        storySearchResponse.andExpect(status().isOk())
                .andDo(print());

        // Extract stories from the response
        final List<LinkedHashMap> searchedStories = objectMapper
                .readValue(storySearchResponse.andReturn().getResponse().getContentAsString(), List.class);
        assertFalse(searchedStories.isEmpty());

        final LinkedHashMap storyAttributes = searchedStories.get(0);
        assertEquals(testStoryId, storyAttributes.get("id"));
        assertEquals(storyRequest.getTitle(), storyAttributes.get("title"));
    }

    @Test
    @Order(3)
    @DisplayName("Get story's image")
    void testGetStoryImage() throws Exception {
        System.out.println("testGetStoryImage");

        final ResultActions response = mockMvc.perform(get("/story/{id}/image", testStoryId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andDo(print());
        // TODO: Check response image
    }

    @Test
    @Order(4)
    @DisplayName("Delete existing story")
    void testDeleteStory() throws Exception {
        System.out.println("testDeleteStory");
        final ResultActions response = mockMvc
                .perform(delete("/story/{id}", testStoryId));

        response.andExpect(status().isAccepted())
                .andDo(print());;
        response.andExpect(jsonPath("$.id", is(testStoryId)));
    }
}