package in.boimama.readstories.service;

import in.boimama.readstories.config.cassandra.CassandraConfig;
import in.boimama.readstories.data.AuthorRepository;
import in.boimama.readstories.dto.ContactUsRequest;
import in.boimama.readstories.exception.ApplicationServerException;
import in.boimama.readstories.utils.AwsImageManager;
import in.boimama.readstories.utils.ModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static in.boimama.readstories.utils.ApplicationConstants.SUCCESS_MESSAGE;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired(required = true)
    private CassandraConfig cassandraConfig;

    @Autowired(required = true)
    private AuthorRepository authorRepository;

    @Autowired(required = false)
    private AwsImageManager awsImageManager;

    @Autowired(required = true)
    private ModelMapperHelper modelMapperHelper;

    public String contactUser(final ContactUsRequest pRequest) throws ApplicationServerException {

        logger.debug("{} is requesting to contact him/her on email: {} and phone number: {}; and sent a message: {}",
                pRequest.getName(), pRequest.getEmail(), pRequest.getPhone(), pRequest.getMessage());
        // TODO: Implement this!
        return SUCCESS_MESSAGE;
    }
}
