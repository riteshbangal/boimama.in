package in.boimama.readstories.utils;

import in.boimama.readstories.exception.ApplicationServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;

import static in.boimama.readstories.utils.ApplicationConstants.DEFAULT_IMAGE_FILE_EXTENSION;
import static in.boimama.readstories.utils.ApplicationUtils.getFileExtension;
import static in.boimama.readstories.utils.ApplicationUtils.hasImageExtension;

@Component
public class AwsImageManager {

    private static final Logger logger = LoggerFactory.getLogger(AwsImageManager.class);


    @Value("${app.passAwsCredentials}")
    private boolean passAwsCredentials;

    @Value("${aws.data-bucket}")
    private String bucketName;

    private final S3Client s3Client;

    public AwsImageManager(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImage(String imageDirectory, String imageName, MultipartFile imageFile) throws ApplicationServerException {
        if (!passAwsCredentials) {
            logger.info("Skipping image uploaded into S3 Bucket!");
            return "Skipping image uploaded into S3 Bucket!";
        }

        File localFile;
        try {
            localFile = File.createTempFile("temp", null);
            imageFile.transferTo(localFile);
        } catch (IOException exception) {
            logger.error("Unable to create image file for S3 Upload!", exception);
            throw new ApplicationServerException(exception);
        }

        uploadImage(imageDirectory, imageName, localFile);
        localFile.delete();  // Delete the local file after upload

        logger.debug("Image uploaded in S3 Bucket successfully.");
        return imageDirectory + "/" + imageName;
    }

    public void uploadImage(String imageDirectory, String imageName, File imageFile) {
        if (!passAwsCredentials) {
            logger.info("Skipping image uploaded into S3 Bucket!");
            return;
        }

        imageName = hasImageExtension(getFileExtension(imageName)) ?
                imageName : imageName + "." + DEFAULT_IMAGE_FILE_EXTENSION;
        final PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(imageDirectory + "/" + imageName)
                .build();

        s3Client.putObject(request, imageFile.toPath());
    }

    public File getImage(String imageName) {
        if (!passAwsCredentials) {
            logger.info("Skipping image uploaded into S3 Bucket!");
            return null;
        }

        imageName = hasImageExtension(getFileExtension(imageName)) ?
                imageName : imageName + "." + DEFAULT_IMAGE_FILE_EXTENSION;
        final GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(imageName)
                .build();

        // Create a local file to store the downloaded image
        final File tempImageFile = new File("temp_story_image.jpg");
        if (tempImageFile.exists()) {
            tempImageFile.delete(); // This will resolve UnixException due to duplicate file name!
        }

        try {
            s3Client.getObject(request, tempImageFile.toPath());
        } catch (S3Exception exception) {
            logger.error("Unable to find image file from S3 Bucket!", exception);
            return null;
        }
        return tempImageFile;
    }
}