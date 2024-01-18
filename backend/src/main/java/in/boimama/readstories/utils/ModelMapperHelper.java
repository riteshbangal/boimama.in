package in.boimama.readstories.utils;

import in.boimama.readstories.data.model.Author;
import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.dto.AuthorResponse;
import in.boimama.readstories.dto.StoryResponse;
import in.boimama.readstories.exception.ApplicationServerException;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperHelper {

    public <S, D> AuthorResponse mapAuthor(Author authorDAO, Class<D> authorDTO) throws ApplicationServerException { // TODO: Fix the Generic type
        final AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(authorDAO.getAuthorId());
        authorResponse.setName(authorDAO.getAuthorName());
        authorResponse.setUsername(authorDAO.getUsername());
        authorResponse.setBiography(authorDAO.getBiography());
        authorResponse.setNumberOfFollowers(authorDAO.getNumberOfFollowers());
        authorResponse.setJoiningDate(authorDAO.getJoiningDate());
        authorResponse.setContactDetails(authorDAO.getContactDetails());
        authorResponse.setPublishedWorks(authorDAO.getPublishedWorks());
        authorResponse.setImagePath(authorDAO.getImagePath());
        authorResponse.setImage(authorDAO.getImage());

        return authorResponse;
    }

    public <S, D> StoryResponse mapStory(Story storyDAO, Class<D> storyDTO) throws ApplicationServerException { // TODO: Fix the Generic type
        final StoryResponse storyResponse = new StoryResponse();
        storyResponse.setId(storyDAO.getStoryId());
        storyResponse.setTitle(storyDAO.getStoryTitle());
        storyResponse.setCategory(storyDAO.getCategory());
        storyResponse.setDescription(storyDAO.getDescription());
        storyResponse.setContent(storyDAO.getContent());
        storyResponse.setPublishedDate(storyDAO.getPublishedDate());
        storyResponse.setAuthorIds(storyDAO.getAuthorIds());
        storyResponse.setAuthorNames(storyDAO.getAuthorNames());
        storyResponse.setRating(storyDAO.getRating());
        storyResponse.setLengthInMins(storyDAO.getLengthInMins());
        storyResponse.setImagePath(storyDAO.getImagePath());
        storyResponse.setImage(storyDAO.getImage());

        return storyResponse;
    }
}
