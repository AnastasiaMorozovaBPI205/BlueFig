package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.dto.ModuleWithParametersDTO;
import app.bluefig.entity.ModuleJpa;
import app.bluefig.entity.QuestionaryAnswerJpa;
import app.bluefig.entity.ParameterJpa;
import app.bluefig.model.*;
import app.bluefig.model.Questionary;
import app.bluefig.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class ModulesController {
    @Autowired
    private MapStructMapper mapper;

    @Autowired
    private ParameterServiceImpl parameterService;

    @Autowired
    private QuestionaryServiceImpl questionaryService;

    @Autowired
    private ModuleFillInServiceImpl moduleFillInService;

    @Autowired
    private QuestionaryAnswerService questionaryAnswerService;

    @Autowired
    private ModuleServiceImpl moduleService;

    @GetMapping("/parameters")
    public List<ModuleWithParametersDTO> getParameters() {
        List<ParameterJpa> parametersJpas = parameterService.findParametersJpa();
        List<ModuleJpa> moduleJpas = moduleService.findModulesJpa();
        List<ModuleWithParametersDTO> moduleWithParametersDTOS = new ArrayList<>();

        for (ModuleJpa moduleJpa : moduleJpas) {
            ModuleWithParametersDTO moduleWithParametersDTO = new ModuleWithParametersDTO();
            moduleWithParametersDTO.setId(moduleJpa.getId());
            moduleWithParametersDTO.setName(moduleJpa.getName());

            List<Parameter> parameterList = new ArrayList<>();
            for (ParameterJpa parameterJpa : parametersJpas) {
                if (parameterJpa.getModuleId().equals(moduleJpa.getId())) {
                    parameterList.add(mapper.ParameterJpaToParameter(parameterJpa));
                }
            }
            moduleWithParametersDTO.setParameterList(parameterList);
        }

        return moduleWithParametersDTOS;
    }

    @PostMapping("/module")
    public void addModule(@RequestBody HashMap<String, Object> data) {
        List<ModuleFieldJpa> module = (List<ModuleFieldJpa>) data.get("questionary");
        String patientId = data.get("patientId").toString();
        String doctorId = data.get("doctorId").toString();
        UUID questionaryId = UUID.randomUUID();

        questionaryService.addQuestionary(questionaryId.toString(), doctorId, patientId);

        for (ModuleFieldJpa moduleFieldJpa : module) {
            moduleFieldService.addModuleField(String.valueOf(questionaryId), moduleFieldJpa.getOrderNumber(),
                    moduleFieldJpa.getFrequency(), moduleFieldJpa.getParameterId());
        }
    }

    @GetMapping("/module/{patient_id}/{doctor_id}")
    public HashMap<String, List<ModuleField>> findModulesByPatientDoctorIds(@PathVariable String patientId, @PathVariable String doctorId) {
        List<Questionary> questionaries = mapper.ModuleJpasToModules(questionaryService.findQuestionaryJpaByPatientDoctorIds(doctorId, patientId));
        HashMap<String, List<ModuleField>> moduleFields = new HashMap<>();

        for (Questionary questionary : questionaries) {
            List<ModuleFieldJpa> fieldJpas = moduleFieldService.findModuleFieldsBy(questionary.getId().toString());
            List<ModuleField> fields = mapper.ModuleFieldJpasToModuleFields(fieldJpas);
            moduleFields.put(questionary.getId().toString(), fields);
        }

        return moduleFields;
    }

    @PostMapping("moduleFillIn")
    public void addModuleFillIn(@RequestBody HashMap<String, Object> data) {
        List<QuestionaryAnswerJpa> fieldAnswers = (List<QuestionaryAnswerJpa>) data.get("fillIn");
        String questionaryId = data.get("questionaryId").toString();
        LocalDateTime dateTime = (LocalDateTime) data.get("datetime");
        UUID fillInId = UUID.randomUUID();

        moduleFillInService.addModuleFillIn(fillInId.toString(), questionaryId, dateTime);

        for (QuestionaryAnswerJpa fieldAnswer : fieldAnswers) {
            questionaryAnswerService.addFieldAnswer(fieldAnswer.getValue(), fieldAnswer.getFieldAnswerIdJpa().getFillIn(), fieldAnswer.getFieldAnswerIdJpa().getParameterId());
        }
    }

    @GetMapping("/moduleFillIn/{patient_id}/{doctor_id}")
    public HashMap<String, List<QuestionaryAnswer>> findModuleFillInsByPatientDoctorIds(@PathVariable String patientId,
                                                                                        @PathVariable String doctorId) {
        List<ModuleFillIn> modules = mapper.ModuleFillInJpasToModuleFillIns(moduleFillInService.findModulesFillInJpaByPatientDoctorIds(doctorId, patientId));
        HashMap<String, List<QuestionaryAnswer>> moduleFieldAnswers = new HashMap<>();

        for (ModuleFillIn fillIn : modules) {
            List<QuestionaryAnswerJpa> questionaryAnswerJpas = questionaryAnswerService.findFieldAnswers(fillIn.getId().toString());
            List<QuestionaryAnswer> fieldsAnswers = mapper.FieldAnswerJpasToFieldAnswers(questionaryAnswerJpas);
            moduleFieldAnswers.put(fillIn.getId().toString(), fieldsAnswers);
        }

        return moduleFieldAnswers;
    }
}
