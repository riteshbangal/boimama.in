package in.boimama.readstories.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String region;


    public AwsCredentials credentials() {
        /**
         * AWS access key ID and secret access key of a service account,
         * which have access to the bucket and its contents.
         */
        return AwsBasicCredentials.create(
                "aws_access_key_id",
                "aws_secret_access_key"
        );
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                //.credentialsProvider(StaticCredentialsProvider.create(credentials()))
                .credentialsProvider(getCredentialsProvider())
                .region(Region.of(region))
                .build();
    }

    private AwsCredentialsProvider getCredentialsProvider() {
        /**
         * Access key must be specified either via environment variable (AWS_ACCESS_KEY_ID) or system property (aws.accessKeyId).
         */
        // return EnvironmentVariableCredentialsProvider.create();

        /**
         * This provider automatically load credentials from various sources,
         * including environment variables and system properties.
         */
        return DefaultCredentialsProvider.create();
    }
}