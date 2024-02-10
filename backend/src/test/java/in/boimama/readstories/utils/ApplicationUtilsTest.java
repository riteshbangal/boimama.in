package in.boimama.readstories.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationUtilsTest {

    @BeforeEach
    void setUp() {
        // Code to set up common resources or perform actions before each test

    }

    @AfterEach
    void tearDown() {
        // Code to clean up resources or perform actions after each test

    }

    @Test
    void testIsEmptyForObject() {
        assertTrue(ApplicationUtils.isEmpty(null));
        assertFalse(ApplicationUtils.isEmpty("someString"));
    }

    @Test
    void testIsEmptyForCollection() {
        assertTrue(ApplicationUtils.isEmpty(null));
        assertTrue(ApplicationUtils.isEmpty(Collections.emptyList()));
        assertFalse(ApplicationUtils.isEmpty(Arrays.asList("item1", "item2")));
    }

    @Test
    void testEstimateStoryLengthInMinutes() {
        assertEquals(0, ApplicationUtils.estimateStoryLengthInMinutes(null));
        assertEquals(0, ApplicationUtils.estimateStoryLengthInMinutes(""));
        assertEquals(1, ApplicationUtils.estimateStoryLengthInMinutes("Short story text"));
        assertEquals(1, ApplicationUtils.estimateStoryLengthInMinutes("This is a longer story text with more words"));
    }

    @Test
    void testGetFileBytesFromFile() throws IOException {
        File testFile = new File("test.txt");
        byte[] fileBytes = "Test content".getBytes();
        Files.write(testFile.toPath(), fileBytes);

        assertArrayEquals(fileBytes, ApplicationUtils.getFileBytes(testFile));

        assertTrue(testFile.delete());
    }

    @Test
    void testGetFileBytesFromMultipartFile() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());

        assertArrayEquals("Test content".getBytes(), ApplicationUtils.getFileBytes(multipartFile));
    }

    @Test
    void testGetFileExtension() {
        assertEquals("txt", ApplicationUtils.getFileExtension("test.txt"));
        assertEquals("pdf", ApplicationUtils.getFileExtension("document.pdf"));
        assertEquals("", ApplicationUtils.getFileExtension("fileWithoutExtension"));
    }

    @Test
    void testHasImageExtension() {
        assertTrue(ApplicationUtils.hasImageExtension("jpg"));
        assertTrue(ApplicationUtils.hasImageExtension("png"));
        assertFalse(ApplicationUtils.hasImageExtension("txt"));
        assertFalse(ApplicationUtils.hasImageExtension(""));
    }
}