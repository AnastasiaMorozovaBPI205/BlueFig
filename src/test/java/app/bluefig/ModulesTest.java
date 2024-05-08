package app.bluefig;

import app.bluefig.controller.ModulesController;
import app.bluefig.dto.ModuleWithParametersDTO;
import app.bluefig.entity.*;
import app.bluefig.mapper.MapStructMapper;
import app.bluefig.model.GastroLabel;
import app.bluefig.model.Parameter;
import app.bluefig.model.Questionary;
import app.bluefig.service.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MapStructMapper.class)
@WebMvcTest(ModulesController.class)
public class ModulesTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FormulaServiceImpl formulaService;
    @MockBean
    private NotificationServiceImpl notificationService;
    @MockBean
    private DoctorParameterService doctorParameterService;
    @MockBean
    private DoctorParameterLabelServiceImpl doctorParameterLabelService;
    @MockBean
    private DoctorParameterFillInServiceImpl doctorParameterFillInService;
    @MockBean
    private PatientHierarchyServiceImpl patientHierarchyService;
    @MockBean
    private ProductGroupServiceImpl productGroupService;
    @MockBean
    private ModuleServiceImpl moduleService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private ProductServiceImpl productService;
    @MockBean
    private ParameterServiceImpl parameterService;
    @MockBean
    private QuestionaryServiceImpl questionaryService;
    @MockBean
    private ModuleFillInServiceImpl moduleFillInService;
    @MockBean
    private QuestionaryAnswerServiceImpl questionaryAnswerService;
    @MockBean
    private GastroLabelServiceImpl gastroLabelService;
    @MockBean
    private MapStructMapper mapper;

    @Test
    void getParametersTest() throws Exception {
        Mockito.when(this.parameterService.findParametersJpa()).thenReturn(getParamsJpa());
        Mockito.when(this.moduleService.findModulesJpa()).thenReturn(getModuleJpas());
        Mockito.when(this.mapper.ParameterJpaToParameter(getParamJpa())).thenReturn(getParam());

        mockMvc.perform(get("/parameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void addModuleTest() throws Exception {
        String body = """
                {
                    "moduleId": "1",
                    "frequency": "7",
                    "patientId": "1",
                    "doctorId": "2",
                    "doctorParametersFillIn": [
                          {
                              "parameterId": "11",
                              "value": "Автоматическая обработка"
                          },
                          {
                              "parameterId": "22",
                              "value": "Стандарт"
                          }
                      ]
                }""";

        mockMvc.perform(post("/questionary").contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void deleteQuestionaryTest() throws Exception {
        mockMvc.perform(delete("/questionary/11")).andExpect(status().isOk());
    }

    @Test
    void changeQuestionaryFrequencyTest() throws Exception {
        QuestionaryJpa questionaryJpa = getQuestionaryJpas().get(0);
        Mockito.when(this.questionaryService.findQuestionaryById("11")).thenReturn(questionaryJpa);

        mockMvc.perform(put("/questionary/11/7")).andExpect(status().isOk());
    }

    @Test
    void findModulesByPatientDoctorIdsTest() throws Exception {
        List<QuestionaryJpa> questionaryJpas = getQuestionaryJpas();
        Mockito.when(this.questionaryService.findQuestionaryJpaByPatientDoctorIds("2", "1"))
                .thenReturn(questionaryJpas);
        Mockito.when(this.mapper.ModuleJpasToModules(questionaryJpas)).thenReturn(getQuestionaries());
        Mockito.when(this.parameterService.findParametersJpa()).thenReturn(getParamsJpa());
        Mockito.when(this.moduleService.findModulesJpa()).thenReturn(getModuleJpas());
        Mockito.when(this.mapper.ParameterJpaToParameter(getParamJpa())).thenReturn(getParam());

        mockMvc.perform(get("/module/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void findGastroParametersTest() throws Exception {
        List<GastroLabelJpa> gastroLabelJpas = getGastroLabelJpas();
        Mockito.when(this.gastroLabelService.findGastroLabelByParameter("1"))
                .thenReturn(gastroLabelJpas);
        Mockito.when(this.mapper.GastroLabelJpasToGastroLabels(gastroLabelJpas)).thenReturn(getGastroLabel());

        mockMvc.perform(get("/module/gastroLabel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void addModuleFillInTest() throws Exception {
        String body = """
                {
                   "questionaryId": "11",
                   "datetime": "2024-03-19T18:04:49",
                   "fillIn":
                   [
                       {
                           "value": 50,
                           "answerIdJpa": {
                               "fillIn" : "",
                               "parameterId": "11"
                           }
                       }
                   ]
                }
                """;
        QuestionaryJpa questionaryJpa = getQuestionaryJpas().get(0);
        Mockito.when(this.questionaryService.findQuestionaryById("11")).thenReturn(questionaryJpa);

        List<DoctorParameterFillInJpa> doctorParameterFillInJpas = getDoctorParameters();
        Mockito.when(this.doctorParameterFillInService.findDoctorParameterFillInJpas("11"))
                .thenReturn(doctorParameterFillInJpas);

        PatientHierarchyJpa hierarchyJpa = getPatientHierarchy();
        Mockito.when(this.patientHierarchyService.findPatientHierarchyJpaById("1"))
                .thenReturn(hierarchyJpa);

        mockMvc.perform(post("/moduleFillIn").contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void findModuleFillInsByPatientDoctorIdsTest() throws Exception {
        List<QuestionaryJpa> questionaryJpas = getQuestionaryJpas();
        Mockito.when(this.questionaryService.findQuestionaryJpaByPatientDoctorIds("2", "1"))
                .thenReturn(questionaryJpas);
        Mockito.when(this.mapper.ModuleJpasToModules(questionaryJpas)).thenReturn(getQuestionaries());

        Mockito.when(this.parameterService.findParametersJpa()).thenReturn(getParamsJpa());
        Mockito.when(this.moduleService.findModulesJpa()).thenReturn(getModuleJpas());
        Mockito.when(this.mapper.ParameterJpaToParameter(getParamJpa())).thenReturn(getParam());

        Mockito.when(this.moduleFillInService.findModulesFillInJpaByPatientDoctorIds("2", "1"))
                .thenReturn(getModuleFillInJpa());
        Mockito.when(this.questionaryAnswerService.findFieldAnswers("111"))
                .thenReturn(getQuestionaryAnswerJpa());

        mockMvc.perform(get("/moduleFillIn/1/2")).andExpect(status().isOk());
    }

    @Test
    void getStatisticsTest() throws Exception {
        Mockito.when(this.moduleFillInService.findModulesFillInJpaByPatientIdModuleId("2", "1"))
                .thenReturn(getModuleFillInJpa());
        Mockito.when(this.questionaryAnswerService.findFieldAnswers("111")).thenReturn(getQuestionaryAnswerJpa());
        Mockito.when(this.parameterService.findParameterJpaById("11")).thenReturn(getParamsJpa().get(0));

        mockMvc.perform(get("/statistics/2/1")).andExpect(status().isOk());
    }

    private List<ModuleWithParametersDTO> getModulesWithParams() {
        ModuleWithParametersDTO module = new ModuleWithParametersDTO();
        module.setId("1");
        module.setName("Антропометрия");
        module.setFrequency(7);
        module.setParameterList(List.of(getParam()));
        return List.of(module);
    }

    private List<DoctorParameterFillInJpa> getDoctorParameters() {
        DoctorParameterFillInIdJpa doctorParameterFillInIdJpa = new DoctorParameterFillInIdJpa();
        doctorParameterFillInIdJpa.setParameterId("111");
        doctorParameterFillInIdJpa.setQuestionaryId("11");

        DoctorParameterFillInJpa doctorParameterFillInJpa = new DoctorParameterFillInJpa();
        doctorParameterFillInJpa.setId(doctorParameterFillInIdJpa);
        doctorParameterFillInJpa.setValue("Автоматическая обработка");

        return List.of(doctorParameterFillInJpa);
    }

    private PatientHierarchyJpa getPatientHierarchy() {
        PatientHierarchyJpa patientHierarchyJpa = new PatientHierarchyJpa();
        patientHierarchyJpa.setPatientId("1");
        patientHierarchyJpa.setNumber(1);

        return patientHierarchyJpa;
    }

    private List<ModuleJpa> getModuleJpas() {
        ModuleJpa module = new ModuleJpa();
        module.setId("1");
        module.setName("Антропометрия");

        return List.of(module);
    }

    private List<ParameterJpa> getParamsJpa() {
        return List.of(getParamJpa());
    }

    private ParameterJpa getParamJpa() {
        ParameterJpa parameterJpa = new ParameterJpa();
        parameterJpa.setModuleId("1");
        parameterJpa.setId("11");
        parameterJpa.setName("Вес");

        return parameterJpa;
    }

    private Parameter getParam() {
        Parameter parameter = new Parameter();
        parameter.setModuleId("1");
        parameter.setId("11");
        parameter.setName("Вес");

        return parameter;
    }

    private List<QuestionaryJpa> getQuestionaryJpas() {
        QuestionaryJpa questionaryJpa = new QuestionaryJpa();
        questionaryJpa.setId("12");
        questionaryJpa.setModuleId("1");
        questionaryJpa.setDoctorId("2");
        questionaryJpa.setPatientId("1");

        return List.of(questionaryJpa);
    }

    private List<Questionary> getQuestionaries() {
        Questionary questionary = new Questionary();
        questionary.setId("12");
        questionary.setModuleId("1");
        questionary.setDoctorId("2");
        questionary.setPatientId("1");

        return List.of(questionary);
    }

    private List<GastroLabelJpa> getGastroLabelJpas() {
        GastroLabelJpa gastroLabel = new GastroLabelJpa();
        gastroLabel.setId("1");
        gastroLabel.setName("да");
        gastroLabel.setIsRedFlag("1");

        return List.of(gastroLabel);
    }


    private List<GastroLabel> getGastroLabel() {
        GastroLabel gastroLabel = new GastroLabel();
        gastroLabel.setId("1");
        gastroLabel.setName("да");
        gastroLabel.setIsRedFlag("1");

        return List.of(gastroLabel);
    }

    private List<ModuleFillInJpa> getModuleFillInJpa() {
        ModuleFillInJpa moduleFillInJpa = new ModuleFillInJpa();
        moduleFillInJpa.setId("111");
        moduleFillInJpa.setQuestionaryId("12");
        moduleFillInJpa.setDatetime(LocalDateTime.now());

        return List.of(moduleFillInJpa);
    }

    private List<QuestionaryAnswerJpa> getQuestionaryAnswerJpa() {
        QuestionaryAnswerJpa questionaryAnswerJpa = new QuestionaryAnswerJpa();
        QuestionaryAnswerIdJpa questionaryAnswerIdJpa = new QuestionaryAnswerIdJpa();

        questionaryAnswerIdJpa.setParameterId("11");
        questionaryAnswerIdJpa.setFillIn("111");

        questionaryAnswerJpa.setAnswerIdJpa(questionaryAnswerIdJpa);
        questionaryAnswerJpa.setValue("50");

        return List.of(questionaryAnswerJpa);
    }
}
