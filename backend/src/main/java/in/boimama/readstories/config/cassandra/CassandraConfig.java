package in.boimama.readstories.config.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class CassandraConfig {

    private static final Logger logger = LoggerFactory.getLogger(CassandraConfig.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${spring.data.cassandra.configFile}")
    private String cassandraConfigFile;

    @Value("${spring.data.cassandra.keyspaceName}")
    private String keyspaceName;

    @Primary
    public @Bean CqlSession session() throws NoSuchAlgorithmException, IOException {

        logger.debug("Building CQL Session for keyspace/cassandra database.");
        return CqlSession.builder()
                .withConfigLoader(DriverConfigLoader.fromFile(getDriverConfig()))
                .withSslContext(SSLContext.getDefault())
                .withKeyspace(keyspaceName)
                .build();
    }

    private File getDriverConfig() {

        final Resource cassandraConfigResource = resourceLoader
                .getResource("classpath:" + cassandraConfigFile);
        try {
            if (cassandraConfigResource.exists()) {
                logger.info("Resource exists!");
                return cassandraConfigResource.getFile();
                /**
                 * TODO: This is giving java.io.FileNotFoundException with docker container:
                 * class path resource [cassandra/config/keyspaces-application.conf] cannot be resolved to absolute file path
                 * because it does not reside in the file system: jar:file:/app/boimama-app.jar!/BOOT-INF/classes!/cassandra/config/keyspaces-application.conf
                 *
                 * Fix this!
                 */
            }
        } catch (IOException exception) {
            logger.error("Unable to load keyspace configuration file", exception);
        }

        logger.debug("Could not find the cassandra configuration file inside classpath! " +
                "Looking for system directory.");
        return new File("/app" + cassandraConfigFile); // TODO: Temporary workaround for the above exception!
    }
}