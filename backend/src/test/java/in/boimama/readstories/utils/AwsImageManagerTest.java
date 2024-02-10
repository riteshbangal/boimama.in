package in.boimama.readstories.utils;

import in.boimama.readstories.exception.ApplicationServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AwsImageManagerTest {

    @Mock
    private S3Client s3Client;

    private AwsImageManager awsImageManager;

    // Set passAwsCredentials to true/false using reflection
    Field passAwsCredentialsField;

    // Test data
    String imageDirectory = "images";
    String imageName = "test-image.jpg";
    MockMultipartFile imageFile = new MockMultipartFile("image",
            "test-image.jpg",
            "image/jpeg",
            "test image content".getBytes());

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        awsImageManager = new AwsImageManager(s3Client);

        // Set passAwsCredentials to true/false using reflection
        passAwsCredentialsField = AwsImageManager.class.getDeclaredField("passAwsCredentials");
        passAwsCredentialsField.setAccessible(true);
        passAwsCredentialsField.set(awsImageManager, false); // Default: false
    }

    @Test
    void testUploadImageWithValidFile() throws ApplicationServerException, IllegalAccessException {
        // Mocking
        passAwsCredentialsField.set(awsImageManager, true);
        when(s3Client.putObject((PutObjectRequest) any(), (Path) any())).thenReturn(null);

        // Test
        String result = awsImageManager.uploadImage(imageDirectory, imageName, imageFile);

        // Assertions
        assertNotNull(result);
        assertTrue(result.startsWith(imageDirectory + "/"));

        // Verify interactions with the mocked S3Client
        verify(s3Client, times(1)).putObject((PutObjectRequest) any(), (Path) any());
    }

    @DisplayName("Test getImage() with AwsCredentials and valid image name")
    @Test
    void testGetImageWithValidImageName() throws IllegalAccessException {

        // Create a local file to store the downloaded image
        final File tempImageFile = new File("temp_story_image.jpg");

        // Mocking
        passAwsCredentialsField.set(awsImageManager, true);
        when(s3Client.getObject((GetObjectRequest) any(), (Path) any())).thenReturn(null);

        // Test data
        String imageName = "test-image.jpg";

        // Test
        File result = awsImageManager.getImage(imageName);

        // Assertions
        assertNotNull(result);
        // assertTrue(result.exists());

        // Verify interactions with the mocked S3Client
        verify(s3Client, times(1)).getObject((GetObjectRequest) any(), (Path) any());
    }

    @DisplayName("Test getImage() without AwsCredentials")
    @Test
    void testGetImageWithoutAwsCredentials() {

        // Mocking
        when(s3Client.getObject((GetObjectRequest) any(), (Path) any())).thenReturn(null);

        // Test data
        String imageName = "test-image.jpg";

        // Test
        File result = awsImageManager.getImage(imageName);

        // Assertions
        assertNull(result);
    }

    @DisplayName("Test uploadImage() without AwsCredentials")
    @Test
    void testUploadImageWithoutAwsCredentials() {

        // Mocking
        when(s3Client.putObject((PutObjectRequest) any(), (Path) any())).thenReturn(null);

        // Test
        String actualResult = awsImageManager.uploadImage(imageDirectory, imageName, imageFile);

        // Assertions
        assertNotNull(actualResult);
        assertTrue(actualResult.contains("Skipping image upload into S3 Bucket"));
    }

    // Additional tests for edge cases, error handling, etc., can be added as needed.
}

