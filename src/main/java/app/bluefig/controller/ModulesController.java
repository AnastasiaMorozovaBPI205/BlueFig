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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
public class ModulesController {
    private final String ANTHROPOMETRY = "a76d9dc1-de4f-11ee-8c0c-00f5f80cf8ae";
    private final String DIET = "a8ff43df-de4f-11ee-8c0c-00f5f80cf8ae";
    private final String FORMULAS = "aa35f36a-de4f-11ee-8c0c-00f5f80cf8ae";
    private final String GASTRO_SYMPTOMS = "ab96ac6d-de4f-11ee-8c0c-00f5f80cf8ae";

    @Autowired
    private MapStructMapper mapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ParameterServiceImpl parameterService;

    @Autowired
    private FormulaServiceImpl formulaService;

    @Autowired
    private ProductGroupServiceImpl productGroupService;

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

    @Autowired
    DoctorParameterService doctorParameterService;

    @Autowired
    DoctorParameterLabelServiceImpl doctorParameterLabelService;

    @Autowired
    DoctorParameterFillInServiceImpl doctorParameterFillInService;

    @Autowired
    PatientHierarchyServiceImpl patientHierarchyService;

    @GetMapping("/parameters")
    public List<ModuleWithParametersDTO> getParameters() {
        return getAllModules();
    }

    @GetMapping("/doctorParameters/{moduleId}")
    public List<DoctorParameter> getDoctorParameters(@PathVariable String moduleId) {
        return mapper.DoctorParametersJpaToDoctorParameters(doctorParameterService.findDoctorParameterJpasByModuleId(moduleId));
    }

    @GetMapping("/doctorParametersLabels/{parameterId}")
    public List<DoctorParameterLabel> getDoctorParameterLabels(@PathVariable String parameterId) {
        return mapper.DoctorParameterLabelJpasToDoctorParameterLabels(doctorParameterLabelService.findDoctorParameterJpas(parameterId));
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

        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) data.get("doctorParametersFillIn");
        for (LinkedHashMap<String, Object> hm : list) {
            String value = hm.get("value").toString() == null ? "" : hm.get("value").toString();
            String parameterId = hm.get("parameterId").toString() == null ? "" : hm.get("parameterId").toString();

            doctorParameterFillInService.addDoctorParameterFillIn(String.valueOf(questionaryId),
                    parameterId, value);
        }

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
                    module.setQuestionaryId(questionary.getId());
                    modulesForPatient.add(module);
                }
            }
        }

        return modulesForPatient;
    }

    @GetMapping("/moduleParameters/{questionaryId}")
    public List<DoctorParameterFillIn> findModulesParametersByPatientDoctorIds(@PathVariable String questionaryId) {
        if (questionaryId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<DoctorParameterFillInJpa> list = doctorParameterFillInService.findDoctorParameterFillInJpas(questionaryId);
        List<DoctorParameterFillIn> listMapped = new ArrayList<>();

        for (DoctorParameterFillInJpa fillInJpa : list) {
            DoctorParameterFillIn fillIn = new DoctorParameterFillIn();
            fillIn.setParameterId(fillInJpa.getId().getParameterId());
            fillIn.setQuestionaryId(fillInJpa.getId().getQuestionaryId());
            fillIn.setValue(fillInJpa.getValue());

            listMapped.add(fillIn);
        }

        return listMapped;
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
                    module.setQuestionaryId(questionary.getId());

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

        changeNumberInHierarchy(answerJpas, questionaryId);

        moduleFillInService.addModuleFillIn(fillInId.toString(), questionaryId, dateTime);

        for (QuestionaryAnswerJpa answerJpa : answerJpas) {
            questionaryAnswerService.addFieldAnswer(answerJpa.getValue(), String.valueOf(fillInId), answerJpa.getAnswerIdJpa().getParameterId());
        }
    }

    @GetMapping("/moduleFillIn/{patientId}/{doctorId}")
    public List<ModuleWithParametersDTO> findModuleFillInsByPatientDoctorIds(@PathVariable String patientId,
                                                                              @PathVariable String doctorId) {
        if (patientId == null || doctorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<Questionary> questionaries = mapper.ModuleJpasToModules(
                questionaryService.findQuestionaryJpaByPatientDoctorIds(doctorId, patientId));
        List<ModuleWithParametersDTO> modules = getAllModules();

        List<ModuleWithParametersDTO> patientsModules = new ArrayList<>();
        for (Questionary questionary : questionaries) {
            for (ModuleWithParametersDTO module : modules) {
                if (questionary.getModuleId().equals(module.getId())) {
                    module.setFrequency(questionary.getFrequency());
                    module.setQuestionaryId(questionary.getId());

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

    @GetMapping("/moduleFillIn/{patientId}")
    public List<ModuleWithParametersDTO> findModuleFillInsByPatientId(@PathVariable String patientId) {
        if (patientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<Questionary> questionaries = mapper.ModuleJpasToModules(
                questionaryService.findQuestionaryJpaByPatientId(patientId));
        List<ModuleWithParametersDTO> modules = getAllModules();

        List<ModuleWithParametersDTO> patientsModules = new ArrayList<>();
        for (Questionary questionary : questionaries) {
            for (ModuleWithParametersDTO module : modules) {
                if (questionary.getModuleId().equals(module.getId())) {
                    module.setFrequency(questionary.getFrequency());
                    module.setQuestionaryId(questionary.getId());

                    patientsModules.add(module);
                    break;
                }
            }
        }

        List<ModuleWithParametersDTO> patientsFillIns = new ArrayList<>();
        List<ModuleFillInJpa> fillIns = moduleFillInService.findModulesFillInJpaByPatientId(patientId);

        for (ModuleFillInJpa moduleFillInJpa : fillIns) {
            ModuleWithParametersDTO module = new ModuleWithParametersDTO();

            for (ModuleWithParametersDTO moduleWithParametersDTO : patientsModules) {
                if (moduleWithParametersDTO.getId().equals(getModuleIdFromQuestionary(questionaries, moduleFillInJpa.getQuestionaryId()))) {
                    module.setId(moduleWithParametersDTO.getId());
                    module.setName(moduleWithParametersDTO.getName());
                    module.setFrequency(moduleWithParametersDTO.getFrequency());
                    module.setParameterList(moduleWithParametersDTO.getParameterList()
                            .stream()
                            .map(Parameter::new)
                            .toList());
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

    @GetMapping("/formula")
    public List<String> getFormulaNames() {
        return formulaService.findFormulaNames();
    }

    @GetMapping("/productGroup")
    public List<ProductGroup> getProductGroups() {
        return mapper.ProductGroupJpasToProductGroups(productGroupService.findProductGroups());
    }

    @GetMapping("/products/{productGroupId}")
    public List<Product> getProducts(@PathVariable String productGroupId) {
        return mapper.ProductJpasToProducts(productService.findProductsInGroup(productGroupId));
    }

    @GetMapping("/patientsHierarchy")
    public List<User> getPatientsHierarchy() {
        return mapper.UserJpasToUsers(patientHierarchyService.findSortedPatientHierarchyJpas());
    }

    private void changeNumberInHierarchy(List<QuestionaryAnswerJpa> answers, String questionaryId) {
        int number = 0;

        QuestionaryJpa questionary = questionaryService.findQuestionaryById(questionaryId);
        String patientId = questionary.getPatientId();
        String moduleId = questionary.getModuleId();

        boolean isFeedbackTypeAutomatic = true;
        List<DoctorParameterFillInJpa> doctorParameterFillInJpas = doctorParameterFillInService
                .findDoctorParameterFillInJpas(questionaryId);
        for (DoctorParameterFillInJpa doctorParameterFillInJpa : doctorParameterFillInJpas) {
            if (doctorParameterFillInJpa.getValue().equals("Ручная обработка")) {
                isFeedbackTypeAutomatic = false;
                break;
            }
        }

        PatientHierarchyJpa hierarchyJpa = patientHierarchyService.findPatientHierarchyJpaById(patientId);
        if (hierarchyJpa != null) {
            number += 1 + hierarchyJpa.getNumber(); // добавляем 1 за ожидание ответа и добавляем прошлый номер в иерархии
        } else {
            if (!isFeedbackTypeAutomatic) {
                number = 1;
            }
        }

        number += getNumberOfRedFlags(moduleId, answers, doctorParameterFillInJpas, patientId, questionaryId);

        if (number > 0) {
            if (hierarchyJpa == null) {
                patientHierarchyService.addPatientToHierarchy(patientId, number);
            } else {
                patientHierarchyService.changePatientNumber(patientId, number);
            }
        }
    }

    private int getNumberOfRedFlags(String moduleId, List<QuestionaryAnswerJpa> answers,
                                    List<DoctorParameterFillInJpa> doctorParameters,
                                    String patientId, String questionaryId) {
        return switch (moduleId) {
            case ANTHROPOMETRY -> getNumberOfRedFlagsAnthropometry(answers, doctorParameters, patientId, questionaryId);
            case DIET -> getNumberOfRedFlagsDiet(answers, doctorParameters);
            case FORMULAS -> getNumberOfRedFlagsFormulas(answers, doctorParameters, patientId);
            case GASTRO_SYMPTOMS -> getNumberOfRedFlagsGastroSymptoms(answers, doctorParameters);
            default -> 0;
        };
    }

    private int getNumberOfRedFlagsAnthropometry(List<QuestionaryAnswerJpa> answers,
                                                 List<DoctorParameterFillInJpa> doctorParameters, String patientId,
                                                 String questionaryId) {
        UserJpa patient = userService.findUserJpaById(patientId);
        int age = Period.between(patient.getBirthday(), LocalDate.now()).getYears();

        // предыдущие ответы, отсортированные от самого позднего до самого раннего
        List<ModuleFillInJpa> previousFillIns = moduleFillInService
                .findModulesFillInJpaByPatientIdQuestionaryId(patientId, questionaryId);
        List<List<QuestionaryAnswerJpa>> previousAnswers = new ArrayList<>();
        for (ModuleFillInJpa moduleFillIn : previousFillIns) {
            previousAnswers.add(questionaryAnswerService.findFieldAnswers(moduleFillIn.getId()));
        }

        final String WEIGHT = "7eb1c37f-cc4a-11ee-8c0c-00f5f80cf8ae";

        int currentWeight = 0;
        for (QuestionaryAnswerJpa answerJpa : answers) {
            if (answerJpa.getAnswerIdJpa().getParameterId().equals(WEIGHT)) {
                currentWeight = Integer.parseInt(answerJpa.getValue());
            }
        }

        if (previousFillIns.isEmpty()) {
            return 0;
        }

        label:
        for (DoctorParameterFillInJpa doctorParameter : doctorParameters) {
            switch (doctorParameter.getValue()) {
                case "Стандарт":
                    if (age < 1) {
                        boolean weightDecreaseTimeIsWeek = true;
                        for (int i = 0; i < previousAnswers.size(); ++i) {
                            for (QuestionaryAnswerJpa answer : previousAnswers.get(i)) {
                                if (answer.getAnswerIdJpa().getParameterId().equals(WEIGHT)
                                        && Period.between(previousFillIns.get(i).getDatetime().toLocalDate(), LocalDate.now()).getDays() < 7
                                        && currentWeight >= Integer.parseInt(answer.getValue())) {
                                    weightDecreaseTimeIsWeek = false;
                                    break;
                                }
                                currentWeight = Integer.parseInt(answer.getValue());
                            }
                        }

                        if (weightDecreaseTimeIsWeek) {
                            return 1;
                        }
                    } else {
                        for (int i = 0; i < previousAnswers.size(); ++i) {
                            for (QuestionaryAnswerJpa answer : previousAnswers.get(i)) {
                                if (answer.getAnswerIdJpa().getParameterId().equals(WEIGHT)
                                        && Period.between(previousFillIns.get(i).getDatetime().toLocalDate(), LocalDate.now()).getMonths() >= 1
                                        && currentWeight <= Integer.parseInt(answer.getValue())) {
                                    return 1;
                                }
                            }
                        }
                    }
                    break label;
                case "Снижение веса":
                    for (QuestionaryAnswerJpa answer : previousAnswers.get(0)) {
                        if (answer.getAnswerIdJpa().getParameterId().equals(WEIGHT)
                                && currentWeight > Integer.parseInt(answer.getValue()) * 1.05) {
                            return 1;
                        }
                    }
                    break label;
                case "Терапия нутритивного дефицита":
                    for (int i = 0; i < previousAnswers.size(); ++i) {
                        for (QuestionaryAnswerJpa answer : previousAnswers.get(i)) {
                            if (answer.getAnswerIdJpa().getParameterId().equals(WEIGHT)
                                    && Period.between(previousFillIns.get(i).getDatetime().toLocalDate(), LocalDate.now()).getDays() >= 7
                                    && currentWeight <= Integer.parseInt(answer.getValue())) {
                                return 1;
                            }
                        }
                    }

                    for (QuestionaryAnswerJpa answer : previousAnswers.get(0)) {
                        if (answer.getAnswerIdJpa().getParameterId().equals(WEIGHT)
                                && currentWeight < Integer.parseInt(answer.getValue()) * 0.95) {
                            return 1;
                        } else if (age < 1 && currentWeight < Integer.parseInt(answer.getValue())) {
                            return 1;
                        }
                    }
                    break label;
            }
        }

        return 0;
    }

    private int getNumberOfRedFlagsDiet(List<QuestionaryAnswerJpa> answers,
                                        List<DoctorParameterFillInJpa> doctorParameters) {
        int numberOfRedFlags = 0;

        return numberOfRedFlags;
    }

    private int getNumberOfRedFlagsFormulas(List<QuestionaryAnswerJpa> answers,
                                            List<DoctorParameterFillInJpa> doctorParameters,
                                            String patientId) {
        final String MIXTURE_MASS = "75312e69-f9ee-11ee-88dc-00f5f80cf8ae";
        final String FORMULA_NAME = "c67574a1-f8ec-11ee-88dc-00f5f80cf8ae";
        final String WEIGHT = "7eb1c37f-cc4a-11ee-8c0c-00f5f80cf8ae";
        final String HEIGHT = "80a45253-cc4a-11ee-8c0c-00f5f80cf8ae";
        final String PERCENTAGE_DIFFERENCE = "31f92255-fa55-11ee-88dc-00f5f80cf8ae";

        int mixtureMass = 0;
        int calories = 0;
        for (QuestionaryAnswerJpa answer : answers) {
            if (answer.getAnswerIdJpa().getParameterId().equals(MIXTURE_MASS)) {
                mixtureMass = Integer.parseInt(answer.getValue());
            } else if (answer.getAnswerIdJpa().getParameterId().equals(FORMULA_NAME)) {
                calories = Integer.parseInt(formulaService.findFormulaByName(answer.getValue()));
            }
        }

        int factEnergy = mixtureMass * calories / 100;

        UserJpa patient = userService.findUserJpaById(patientId);
        int sex = patient.getSex().equals("женский") ? 1 : 0;
        int age = Period.between(patient.getBirthday(), LocalDate.now()).getYears();

        List<QuestionaryAnswerJpa> lastAnthropometryAnswer = getLastAnthropometryAnswer(patientId);
        int weight = 0;
        int height = 0;
        for (QuestionaryAnswerJpa answer : lastAnthropometryAnswer) {
            if (answer.getAnswerIdJpa().getParameterId().equals(WEIGHT)) {
                weight = Integer.parseInt(answer.getValue());
            } else if (answer.getAnswerIdJpa().getParameterId().equals(HEIGHT)) {
                height = Integer.parseInt(answer.getValue());
            }
        }

        double countedEnergy = getCountedEnergyShofield(age, sex, weight, height);

        if (factEnergy == 0 || countedEnergy == 0) {
            return 0;
        }

        double percentageDifference = (countedEnergy - factEnergy) / factEnergy * 100;
        if (percentageDifference < 0) {
            percentageDifference *= -1;
        }

        int percentageMaxDifference = 0;
        for (DoctorParameterFillInJpa parameter : doctorParameters) {
            if (parameter.getId().getParameterId().equals(PERCENTAGE_DIFFERENCE)) {
                percentageMaxDifference = Integer.parseInt(parameter.getValue());
                break;
            }
        }

        if (percentageDifference > percentageMaxDifference) {
            return 1;
        }

        return 0;
    }

    private double getCountedEnergyShofield(int age, int sex, int weight, int height) {
        if (age <= 3 && sex == 0) {
            return 16.25*weight + 1023.2*height - 413.5;
        } else if (age > 3 && age <= 10 && sex == 0) {
            return 16.97*weight + 161.8*height + 371.2;
        } else if (age > 10 && sex == 0) {
            return 8.365*weight + 465*height + 200;
        } else if (age <= 3) {
            return  0.167*weight + 1517.4*height - 617.6;
        } else if (age <= 10) {
            return  19.6*weight + 130.3*height + 414.9;
        }

        return 16.25*weight + 137.2*height + 515.5;
    }

    private List<QuestionaryAnswerJpa> getLastAnthropometryAnswer(String patientId) {
        String questionaryId = questionaryService.findQuestionaryByPatientIdModuleId(patientId, ANTHROPOMETRY);

        // предыдущие ответы, отсортированные от самого позднего до самого раннего
        List<ModuleFillInJpa> previousFillIns = moduleFillInService
                .findModulesFillInJpaByPatientIdQuestionaryId(patientId, questionaryId);
        List<List<QuestionaryAnswerJpa>> previousAnswers = new ArrayList<>();
        for (ModuleFillInJpa moduleFillIn : previousFillIns) {
            previousAnswers.add(questionaryAnswerService.findFieldAnswers(moduleFillIn.getId()));
        }

        return previousAnswers.get(0);
    }

    private int getNumberOfRedFlagsGastroSymptoms(List<QuestionaryAnswerJpa> answers,
                                                  List<DoctorParameterFillInJpa> doctorParameters) {
        int numberOfRedFlags = 0;
        final String VOMIT = "8632ea18-cc4a-11ee-8c0c-00f5f80cf8ae";
        final String NAUSEA = "850657cb-cc4a-11ee-8c0c-00f5f80cf8ae";
        final String APPETIT = "88a297ac-cc4a-11ee-8c0c-00f5f80cf8ae";
        final String SKIN_RASH = "89e4f98c-cc4a-11ee-8c0c-00f5f80cf8ae";
        final String BM_TYPE = "97cf3aa0-f2b6-11ee-88dc-00f5f80cf8ae";
        final String BM_ADMIXTURE = "837e3f4f-cc4a-11ee-8c0c-00f5f80cf8ae";

        for (QuestionaryAnswerJpa answer : answers) {
            answer.setValue(answer.getValue().replace("[", "").replace("]", ""));
            if (answer.getAnswerIdJpa().getParameterId().equals(VOMIT)) {
                if (Integer.parseInt(answer.getValue()) >= 1) {
                    numberOfRedFlags += 1;
                }
            } else if (answer.getAnswerIdJpa().getParameterId().equals(NAUSEA)) {
                if (answer.getValue().equals("препятствует приему пищи")) {
                    numberOfRedFlags += 1;
                }
            } else if (answer.getAnswerIdJpa().getParameterId().equals(APPETIT)) {
                if (answer.getValue().equals("отсутствует")) {
                    numberOfRedFlags += 1;
                }
            } else if (answer.getAnswerIdJpa().getParameterId().equals(SKIN_RASH)) {
                if (answer.getValue().equals("да")) {
                    numberOfRedFlags += 1;
                }
            } else if (answer.getAnswerIdJpa().getParameterId().equals(BM_TYPE)) {

            } else if (answer.getAnswerIdJpa().getParameterId().equals(BM_ADMIXTURE)) {

            }
        }

        return numberOfRedFlags;
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
