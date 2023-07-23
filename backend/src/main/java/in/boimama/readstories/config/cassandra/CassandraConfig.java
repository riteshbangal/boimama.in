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

    private static final Logger LOG = LoggerFactory.getLogger(CassandraConfig.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${spring.data.cassandra.configFile}")
    private String cassandraConfigFile;

    @Value("${spring.data.cassandra.keyspaceName}")
    private String keyspaceName;

    @Primary
    public @Bean CqlSession session() throws NoSuchAlgorithmException, IOException {

        LOG.debug("Building CQL Session for keyspace/cassandra database.");
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
                LOG.info("Resource exists!");
                return cassandraConfigResource.getFile();
            }
        } catch (IOException exception) {
            LOG.error("Unable to load keyspace configuration file");
        }
        return new File(System.getProperty("user.dir")
                + "/src/main/resources"
                + cassandraConfigFile);
    }
}