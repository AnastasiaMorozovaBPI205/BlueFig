package app.bluefig;

import app.bluefig.entity.*;
import app.bluefig.model.*;
import app.bluefig.model.Module;
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

    Module ModuleJpaToModule(ModuleJpa moduleJpa);
    List<Module> ModuleJpasToModules(List<ModuleJpa> moduleJpa);

    ModuleField ModuleFieldJpaToModuleField(ModuleFieldJpa moduleFieldJpa);
    List<ModuleField> ModuleFieldJpasToModuleFields(List<ModuleFieldJpa> moduleFieldJpa);

    ModuleFillIn ModuleFillInJpaToModuleFillIn(ModuleFillInJpa moduleFillInJpa);
    List<ModuleFillIn> ModuleFillInJpasToModuleFillIns(List<ModuleFillInJpa> moduleFillInJpa);

    FieldAnswer FieldAnswerJpaToFieldAnswer(FieldAnswerJpa FieldAnswerJpa);
    List<FieldAnswer> FieldAnswerJpasToFieldAnswers(List<FieldAnswerJpa> FieldAnswerJpa);
}
