package app.bluefig;

import app.bluefig.entity.*;
import app.bluefig.model.*;
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

    Parameter ParameterJpaToParameter(ParameterJpa parameterJpa);
    List<Parameter> ParameterJpasToParameters(List<ParameterJpa> parameterJpa);

    Questionary ModuleJpaToModule(QuestionaryJpa moduleJpa);
    List<Questionary> ModuleJpasToModules(List<QuestionaryJpa> moduleJpa);

    ModuleFillIn ModuleFillInJpaToModuleFillIn(ModuleFillInJpa moduleFillInJpa);
    List<ModuleFillIn> ModuleFillInJpasToModuleFillIns(List<ModuleFillInJpa> moduleFillInJpa);

    GastroLabel GastroLabelJpaToGastroLabel (GastroLabelJpa gastroLabelJpa);
    List<GastroLabel> GastroLabelJpasToGastroLabels (List<GastroLabelJpa> gastroLabelJpa);

    Notification NotificationJpaToNotification(NotificationJpa NotificationJpa);
    List<Notification> NotificationJpasToNotifications(List<NotificationJpa> NotificationJpas);
}
