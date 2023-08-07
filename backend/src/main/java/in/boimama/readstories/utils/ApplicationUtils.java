package in.boimama.readstories.utils;

import in.boimama.readstories.exception.ApplicationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static in.boimama.readstories.utils.ApplicationConstants.AVERAGE_READING_SPEED;

/**
 * Utility methods for the application
 */
public class ApplicationUtils {

    // Method to estimate story length in minutes
    public static int estimateStoryLengthInMinutes(String storyText) {
        if (storyText == null || storyText.isEmpty()) {
            return 0;
        }
        final String[] words = storyText.trim().split("\\s+");
        final int totalWords = words.length;

        return (totalWords < AVERAGE_READING_SPEED) ? 1 : (totalWords / AVERAGE_READING_SPEED);
    }

    public static byte[] getImageBytes(MultipartFile pStoryImageFile) throws ApplicationException {
        try {
            return (pStoryImageFile != null) ? pStoryImageFile.getBytes() : new byte[0];
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    // Prevent instantiation as this is a utility class.
    private ApplicationUtils() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }
}
