package in.boimama.readstories.utils;

import in.boimama.readstories.data.model.Story;
import in.boimama.readstories.dto.StoryResponse;
import in.boimama.readstories.exception.ApplicationException;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperHelper {

    public <S, D> StoryResponse mapStory(Story storyDAO, Class<D> storyDTO) throws ApplicationException { // TODO: Fix the Generic type
        final StoryResponse storyResponse = new StoryResponse();
        storyResponse.setId(storyDAO.getStoryId());
        storyResponse.setTitle(storyDAO.getTitle());
        storyResponse.setCategory(storyDAO.getCategory());
        storyResponse.setDescription(storyDAO.getDescription());
        storyResponse.setContent(storyDAO.getContent());
        storyResponse.setPublishedDate(storyDAO.getPublishedDate());
        storyResponse.setAuthorIds(storyDAO.getAuthorIds());
        storyResponse.setAuthorNames(storyDAO.getAuthorNames());
        storyResponse.setRating(storyDAO.getRating());
        storyResponse.setLengthInMins(storyDAO.getLengthInMins());
        storyResponse.setImagePath(storyDAO.getImagePath());

        return storyResponse;
    }
}
