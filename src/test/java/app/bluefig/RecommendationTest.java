package app.bluefig;

import app.bluefig.controller.RecommendationController;
import app.bluefig.entity.RecommendationJpa;
import app.bluefig.mapper.MapStructMapper;
import app.bluefig.model.Recommendation;
import app.bluefig.service.NotificationServiceImpl;
import app.bluefig.service.PatientHierarchyServiceImpl;
import app.bluefig.service.RecommendationServiceImpl;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MapStructMapper.class)
@WebMvcTest(RecommendationController.class)
public class RecommendationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecommendationServiceImpl service;
    @MockBean
    private NotificationServiceImpl notificationService;
    @MockBean
    private PatientHierarchyServiceImpl patientHierarchyService;
    @MockBean
    private MapStructMapper mapper;
    private final LocalDateTime localDateTime = LocalDateTime.of(2022, 7, 7, 7, 7, 7, 7);

    @Test
    void addRecommendationTest() throws Exception {
        String body = """
                {
                    "patientId": "1",
                    "doctorId": "2",
                    "recommendation": "eat well"
                }""";


        mockMvc.perform(post("/recommendation").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    @Test
    void getRecommendationByPatientTest() throws Exception {
        List<RecommendationJpa> recommendationJpaList = List.of(getRecommendationJpa());
        Mockito.when(this.service.findRecommendationJpaByPatient("1")).thenReturn(recommendationJpaList);
        Mockito.when(this.mapper.RecommendationJpasToRecommendations(recommendationJpaList))
                .thenReturn(List.of(getRecommendation()));

        mockMvc.perform(get("/recommendation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    private RecommendationJpa getRecommendationJpa() {
        RecommendationJpa recommendationJpa = new RecommendationJpa();
        recommendationJpa.setRecommendation("eat well");
        recommendationJpa.setId("1");
        recommendationJpa.setDatetime(localDateTime);
        recommendationJpa.setDoctorId("2");
        recommendationJpa.setPatientId("1");

        return recommendationJpa;
    }

    private Recommendation getRecommendation() {
        Recommendation recommendation = new Recommendation();
        recommendation.setRecommendation("eat well");
        recommendation.setId("1");
        recommendation.setDatetime(localDateTime);
        recommendation.setDoctorId("2");
        recommendation.setPatientId("1");

        return recommendation;
    }
}
