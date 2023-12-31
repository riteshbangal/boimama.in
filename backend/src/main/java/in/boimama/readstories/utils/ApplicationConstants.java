package in.boimama.readstories.utils;

public class ApplicationConstants {

    // Private constructor to prevent external instantiation
    private ApplicationConstants() {
    }

    // Define constant fields here
    public static final String UNCATEGORIZED_TYPE = "Uncategorized";

    // List of common image file extensions
    public static final String[] IMAGE_FILE_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp"};

    public static final String DEFAULT_IMAGE_FILE_EXTENSION = "jpg";

    public static final int AVERAGE_READING_SPEED = 100; // Default average reading speed in words per minute.


    // Singleton instance holder
    private static final ApplicationConstants instance = new ApplicationConstants();

    // Public static method to access the singleton instance
    public static ApplicationConstants getInstance() {
        return instance;
    }
}
