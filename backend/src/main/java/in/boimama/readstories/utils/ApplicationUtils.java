package in.boimama.readstories.utils;

import in.boimama.readstories.exception.ApplicationServerException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import static in.boimama.readstories.utils.ApplicationConstants.AVERAGE_READING_SPEED;
import static in.boimama.readstories.utils.ApplicationConstants.IMAGE_FILE_EXTENSIONS;

/**
 * Utility methods for the application
 */
public class ApplicationUtils {

    public static boolean isEmpty(Object pObject) {
        return pObject == null;
    }

    public static boolean isEmpty(Collection<?> pCollectionObject) {
        return pCollectionObject == null || pCollectionObject.isEmpty();
    }

    // Method to estimate story length in minutes
    public static int estimateStoryLengthInMinutes(String storyText) {
        if (storyText == null || storyText.isEmpty()) {
            return 0;
        }
        final String[] words = storyText.trim().split("\\s+");
        final int totalWords = words.length;

        return (totalWords < AVERAGE_READING_SPEED) ? 1 : (totalWords / AVERAGE_READING_SPEED);
    }

    public static byte[] getFileBytes(File pFile) throws ApplicationServerException {
        try {
            final Path filePath = pFile.toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new ApplicationServerException(e);
        }
    }

    public static byte[] getFileBytes(MultipartFile pMultipartFile) throws ApplicationServerException {
        try {
            return (pMultipartFile != null) ? pMultipartFile.getBytes() : new byte[0];
        } catch (IOException e) {
            throw new ApplicationServerException(e);
        }
    }

    public static String getFileExtension(String fileName) {
        String fileExtension = "";
        if (fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return fileExtension.toLowerCase();
    }

    public static boolean hasImageExtension(String fileExtension) {
        return Arrays.asList(IMAGE_FILE_EXTENSIONS).contains(fileExtension);
    }

    // Prevent instantiation as this is a utility class.
    private ApplicationUtils() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }
}
