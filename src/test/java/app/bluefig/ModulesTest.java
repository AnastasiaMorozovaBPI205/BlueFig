package app.bluefig;

import app.bluefig.controller.ModulesController;
import app.bluefig.controller.UserController;
import app.bluefig.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MapStructMapper.class)
@WebMvcTest(ModulesController.class)
public class ModulesTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ModuleServiceImpl moduleService;
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

    }

    @Test
    void addModuleTest() throws Exception {
    }

    @Test
    void deleteQuestionaryTest() throws Exception {
    }

    @Test
    void changeQuestionaryFrequencyTest() throws Exception {
    }

    @Test
    void findModulesByPatientDoctorIdsTest() throws Exception {
    }

    @Test
    void findGastroParametersTest() throws Exception {
    }

    @Test
    void addModuleFillInTest() throws Exception {
    }

    @Test
    void findModuleFillInsByPatientDoctorIdsTest() throws Exception {
    }
}
