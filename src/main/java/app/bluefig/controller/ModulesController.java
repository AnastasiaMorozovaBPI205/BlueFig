package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.dto.ModuleWithParametersDTO;
import app.bluefig.entity.*;
import app.bluefig.model.*;
import app.bluefig.model.Questionary;
import app.bluefig.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*")
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
    private QuestionaryAnswerServiceImpl questionaryAnswerService;

    @Autowired
    private GastroLabelServiceImpl gastroLabelService;

    @Autowired
    private ModuleServiceImpl moduleService;

    @Autowired
    NotificationServiceImpl notificationService;

    @GetMapping("/parameters")
    public List<ModuleWithParametersDTO> getParameters() {
        return getAllModules();
    }

    @PostMapping("/questionary")
    public void addQuestionary(@RequestBody HashMap<String, Object> data) {
        String moduleId = data.get("moduleId").toString();
        int frequency = Integer.parseInt(data.get("frequency").toString());
        String patientId = data.get("patientId").toString();
        String doctorId = data.get("doctorId").toString();

        if (patientId == null || doctorId == null || data.get("frequency").toString() == null || moduleId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        UUID questionaryId = UUID.randomUUID();

        questionaryService.addQuestionary(questionaryId.toString(), doctorId, patientId, moduleId, frequency);

        notificationService.addNotification(patientId, "Вам добавлен новый модуль к заполнению!", LocalDateTime.now());
        System.out.println("questionary added successfully");
    }

    @DeleteMapping("/questionary/{questionaryId}")
    public void deleteQuestionary(@PathVariable String questionaryId) {
        if (questionaryId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        questionaryService.deleteQuestionaryById(questionaryId);
    }

    @PutMapping("/questionary/{questionaryId}/{frequency}")
    public void changeQuestionaryFrequency(@PathVariable String questionaryId, @PathVariable String frequency) {
        if (questionaryId == null || frequency == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        questionaryService.updateQuestionaryFrequency(questionaryId, Integer.parseInt(frequency));
        QuestionaryJpa questionaryJpa = questionaryService.findQuestionaryById(questionaryId);
        notificationService.addNotification(questionaryJpa.getPatientId(),
                "У модуля изменилась частота заполнения!", LocalDateTime.now());

    }

    @GetMapping("/module/{patientId}/{doctorId}")
    public List<ModuleWithParametersDTO> findModulesByPatientDoctorIds(@PathVariable String patientId,
                                                                       @PathVariable String doctorId) {
        if (patientId == null || doctorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<Questionary> questionaries = mapper.ModuleJpasToModules(questionaryService
                .findQuestionaryJpaByPatientDoctorIds(doctorId, patientId));
        List<ModuleWithParametersDTO> modules = getAllModules();

        List<ModuleWithParametersDTO> modulesForPatient = new ArrayList<>();
        for (Questionary questionary : questionaries) {
            for (ModuleWithParametersDTO module : modules) {
                if (questionary.getModuleId().equals(module.getId())) {
                    module.setFrequency(questionary.getFrequency());
                    modulesForPatient.add(module);
                }
            }
        }

        return modulesForPatient;
    }

    @GetMapping("/module/{patientId}")
    public List<ModuleWithParametersDTO> findModulesByPatientId(@PathVariable String patientId) {
        if (patientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<Questionary> questionaries = mapper.ModuleJpasToModules(questionaryService
                .findQuestionaryJpaByPatientId(patientId));
        List<ModuleWithParametersDTO> modules = getAllModules();

        List<ModuleWithParametersDTO> modulesForPatient = new ArrayList<>();
        for (Questionary questionary : questionaries) {
            for (ModuleWithParametersDTO module : modules) {
                if (questionary.getModuleId().equals(module.getId())) {
                    module.setFrequency(questionary.getFrequency());
                    modulesForPatient.add(module);
                }
            }
        }

        return modulesForPatient;
    }

    @GetMapping("/module/gastroLabel/{parameterId}")
    public List<GastroLabel> findGastroParameters(@PathVariable String parameterId) {
        if (parameterId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.GastroLabelJpasToGastroLabels(gastroLabelService.findGastroLabelByParameter(parameterId));
    }

    @PostMapping("/moduleFillIn")
    public void addModuleFillIn(@RequestBody HashMap<String, Object> data) {
        if (data.get("fillIn") == null || data.get("questionaryId") == null || data.get("datetime") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) data.get("fillIn");
        List<QuestionaryAnswerJpa> answerJpas = new ArrayList<>();

        for (LinkedHashMap<String, Object> hm : list) {
            QuestionaryAnswerJpa questionaryAnswerJpa = new QuestionaryAnswerJpa();
            String value = hm.get("value").toString() == null ? "" : hm.get("value").toString();
            questionaryAnswerJpa.setValue(value);

            QuestionaryAnswerIdJpa questionaryAnswerIdJpa = new QuestionaryAnswerIdJpa();
            LinkedHashMap<String, Object> map = hm.get("answerIdJpa") == null ? new LinkedHashMap<>()
                    : (LinkedHashMap<String, Object>) hm.get("answerIdJpa");
            String parameterId = map.get("parameterId").toString() == null ? "" : map.get("parameterId").toString();
            questionaryAnswerIdJpa.setParameterId(parameterId);
            questionaryAnswerJpa.setAnswerIdJpa(questionaryAnswerIdJpa);

            answerJpas.add(questionaryAnswerJpa);
        }

        String questionaryId = data.get("questionaryId").toString();
        LocalDateTime dateTime = LocalDateTime.parse(data.get("datetime").toString());
        UUID fillInId = UUID.randomUUID();

        moduleFillInService.addModuleFillIn(fillInId.toString(), questionaryId, dateTime);

        for (QuestionaryAnswerJpa answerJpa : answerJpas) {
            questionaryAnswerService.addFieldAnswer(answerJpa.getValue(), String.valueOf(fillInId), answerJpa.getAnswerIdJpa().getParameterId());
        }
    }

    @GetMapping("/moduleFillIn/{patientId}/{doctorId}")
    public List<ModuleWithParametersDTO>  findModuleFillInsByPatientDoctorIds(@PathVariable String patientId,
                                                                              @PathVariable String doctorId) {
        if (patientId == null || doctorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<Questionary> questionaries = mapper.ModuleJpasToModules(questionaryService.findQuestionaryJpaByPatientDoctorIds(doctorId, patientId));
        List<ModuleWithParametersDTO> modules = getAllModules();

        List<ModuleWithParametersDTO> patientsModules = new ArrayList<>();
        for (Questionary questionary : questionaries) {
            for (ModuleWithParametersDTO module : modules) {
                if (questionary.getModuleId().equals(module.getId())) {
                    module.setFrequency(questionary.getFrequency());
                    patientsModules.add(module);
                    break;
                }
            }
        }

        List<ModuleWithParametersDTO> patientsFillIns = new ArrayList<>();
        List<ModuleFillInJpa> fillIns = moduleFillInService.findModulesFillInJpaByPatientDoctorIds(doctorId, patientId);

        for (ModuleFillInJpa moduleFillInJpa : fillIns) {
            ModuleWithParametersDTO module = new ModuleWithParametersDTO();

            for (ModuleWithParametersDTO moduleWithParametersDTO : patientsModules) {
                if (moduleWithParametersDTO.getId().equals(getModuleIdFromQuestionary(questionaries, moduleFillInJpa.getQuestionaryId()))) {
                    module.setId(moduleWithParametersDTO.getId());
                    module.setName(moduleWithParametersDTO.getName());
                    module.setFrequency(moduleWithParametersDTO.getFrequency());
                    module.setParameterList(moduleWithParametersDTO.getParameterList());
                    break;
                }
            }

            module.setDateTime(moduleFillInJpa.getDatetime());

            List<QuestionaryAnswerJpa> questionaryAnswerJpas = questionaryAnswerService.findFieldAnswers(moduleFillInJpa.getId());
            for (Parameter parameter : module.getParameterList()) {
                for (QuestionaryAnswerJpa questionaryAnswerJpa : questionaryAnswerJpas) {
                    if (parameter.getId().equals(questionaryAnswerJpa.getAnswerIdJpa().getParameterId())) {
                        parameter.setValue(questionaryAnswerJpa.getValue());
                        break;
                    }
                }
            }

            patientsFillIns.add(module);
        }

        return patientsFillIns;
    }

    @GetMapping("/statistics/{moduleId}/{patientId}")
    public HashMap<String, HashMap<LocalDateTime, Object>> getStatistics(@PathVariable String moduleId,
                                                                         @PathVariable String patientId) {
        HashMap<String, HashMap<LocalDateTime, Object>> graphs = new HashMap<>();

        List<ModuleFillInJpa> fillIns = moduleFillInService.findModulesFillInJpaByPatientIdModuleId(moduleId, patientId);
        for (ModuleFillInJpa fillIn : fillIns) {
            List<QuestionaryAnswerJpa> questionaryAnswerJpas = questionaryAnswerService.findFieldAnswers(fillIn.getId());
            for (QuestionaryAnswerJpa answer : questionaryAnswerJpas) {
                ParameterJpa parameterJpa = parameterService.findParameterJpaById(answer.getAnswerIdJpa().getParameterId());
                if (parameterJpa == null) {
                    continue;
                }

                if (!graphs.containsKey(parameterJpa.getName())) {
                    graphs.put(parameterJpa.getName(), new HashMap<>());
                }

                graphs.get(parameterJpa.getName()).put(fillIn.getDatetime(), answer.getValue());
            }
        }

        return graphs;
    }

    private String getModuleIdFromQuestionary(List<Questionary> questionaries, String questionaryId) {
        for (Questionary questionary : questionaries) {
            if (questionary.getId().equals(questionaryId)) {
                return questionary.getModuleId();
            }
        }

        return "";
    }

    private List<ModuleWithParametersDTO> getAllModules() {
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

            moduleWithParametersDTOS.add(moduleWithParametersDTO);
        }

        return moduleWithParametersDTOS;
    }
}
