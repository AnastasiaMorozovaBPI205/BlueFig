package app.bluefig;

import app.bluefig.entity.RecommendationJpa;
import app.bluefig.entity.UserJpa;
import app.bluefig.model.Recommendation;
import app.bluefig.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {
    User UserJpaToUser(UserJpa userJpa);
    List<User> UserJpasToUsers(List<UserJpa> userJpas);

    Recommendation RecommendationJpaToRecommendation(RecommendationJpa recommendationJpa);
    List<Recommendation> RecommendationJpasToRecommendations(List<RecommendationJpa> recommendationJpa);
}
