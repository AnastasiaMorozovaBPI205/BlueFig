package app.bluefig.controller;

import app.bluefig.mapper.MapStructMapper;
import app.bluefig.dto.ModuleWithParametersDTO;
import app.bluefig.entity.*;
import app.bluefig.model.*;
import app.bluefig.model.Questionary;
import app.bluefig.service.*;
import org.apache.commons.lang3.ObjectUtils;
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

    private final String MIXTURE_MASS = "75312e69-f9ee-11ee-88dc-00f5f80cf8ae";
    private final String FORMULA_NAME = "c67574a1-f8ec-11ee-88dc-00f5f80cf8ae";
    private final String PRODUCT_MASS = "7f1862d8-fe90-11ee-88dc-00f5f80cf8ae";
    private final String PRODUCT_NAME = "89e20095-fd15-11ee-88dc-00f5f80cf8ae";
    private final String WEIGHT = "7eb1c37f-cc4a-11ee-8c0c-00f5f80cf8ae";
    private final String HEIGHT = "80a45253-cc4a-11ee-8c0c-00f5f80cf8ae";
    private final String PERCENTAGE_DIFFERENCE = "31f92255-fa55-11ee-88dc-00f5f80cf8ae";
    private final String CONVERSION_COEFFICIENT = "f480a9de-fb3f-11ee-88dc-00f5f80cf8ae";

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

    /**
     * Получение модулей и их параметров.
     * @return медицинские модули с параметрами
     */
    @GetMapping(path="/parameters", produces = "application/json;charset=UTF-8")
    public List<ModuleWithParametersDTO> getParameters() {
        return getAllModules();
    }

    /**
     * Получение параметров для выбора доктором в модуль пациенту.
     * @param moduleId id модуля
     * @return параметры для доктора
     */
    @GetMapping(path="/doctorParameters/{moduleId}", produces = "application/json;charset=UTF-8")
    public List<DoctorParameter> getDoctorParameters(@PathVariable String moduleId) {
        if (moduleId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.DoctorParametersJpaToDoctorParameters(doctorParameterService.
                findDoctorParameterJpasByModuleId(moduleId));
    }

    /**
     * Получение лейблов параметров для доктора.
     * @param parameterId id параметра
     * @return лейблы параметра
     */
    @GetMapping(path="/doctorParametersLabels/{parameterId}", produces = "application/json;charset=UTF-8")
    public List<DoctorParameterLabel> getDoctorParameterLabels(@PathVariable String parameterId) {
        if (parameterId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.DoctorParameterLabelJpasToDoctorParameterLabels(doctorParameterLabelService.findDoctorParameterJpas(parameterId));
    }

    /**
     * Добавление анкеты пациенту.
     * @param data данные анкеты
     */
    @PostMapping(path="/questionary")
    public void addQuestionary(@RequestBody HashMap<String, Object> data) {
        String moduleId = data.get("moduleId").toString();
        String patientId = data.get("patientId").toString();
        String doctorId = data.get("doctorId").toString();

        if (ObjectUtils.anyNull(patientId, doctorId, data.get("frequency").toString(), moduleId) ||
                !data.get("frequency").toString().matches("\\d+")) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        int frequency = Integer.parseInt(data.get("frequency").toString());

        UUID questionaryId = UUID.randomUUID();

        questionaryService.addQuestionary(questionaryId.toString(), doctorId, patientId, moduleId, frequency, LocalDateTime.now());

        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) data.get("doctorParametersFillIn");
        for (LinkedHashMap<String, Object> hm : list) {
            String value = hm.get("value").toString() == null ? "" : hm.get("value").toString();
            String parameterId = hm.get("parameterId").toString() == null ? "" : hm.get("parameterId").toString();

            doctorParameterFillInService.addDoctorParameterFillIn(String.valueOf(questionaryId),
                    parameterId, value);
        }

        notificationService.addNotification(patientId, "Вам добавлен новый модуль к заполнению!", LocalDateTime.now());

        System.out.println("Анкета для заполнения пациентом успешно добавлена.");
    }

    /**
     * Удаление модуля у пациента.
     * @param questionaryId id анкеты
     */
    @DeleteMapping("/questionary/{questionaryId}")
    public void deleteQuestionary(@PathVariable String questionaryId) {
        if (questionaryId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        questionaryService.setQuestionaryInactive(questionaryId);

        System.out.println("Анкета для заполнения пациентом удалена успешно.");
    }

    /**
     * Изменение частоты заполнения модуля.
     * @param questionaryId id анкеты
     * @param frequency частота
     */
    @PutMapping("/questionary/{questionaryId}/{frequency}")
    public void changeQuestionaryFrequency(@PathVariable String questionaryId, @PathVariable String frequency) {
        if (questionaryId == null || frequency == null || !frequency.matches("\\d+")) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        questionaryService.updateQuestionaryFrequency(questionaryId, Integer.parseInt(frequency));
        QuestionaryJpa questionaryJpa = questionaryService.findQuestionaryById(questionaryId);
        notificationService.addNotification(questionaryJpa.getPatientId(),
                "У модуля изменилась частота заполнения!", LocalDateTime.now());

        System.out.println("Частота заполнения модуля успешно изменена.");
    }

    /**
     * Получение всех модулей пациента.
     * @param patientId id пациента
     * @param doctorId id врача
     * @return список модулей пациента
     */
    @GetMapping(path="/module/{patientId}/{doctorId}", produces = "application/json;charset=UTF-8")
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

    /**
     * Получение заполненных параметров доктором для модуля.
     * @param questionaryId id анкеты
     * @return список параметров
     */
    @GetMapping(path="/moduleParameters/{questionaryId}", produces = "application/json;charset=UTF-8")
    public List<DoctorParameterFillIn> findModulesParametersByPatientDoctorIds(@PathVariable String questionaryId) {
        if (questionaryId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<DoctorParameterFillInJpa> list = doctorParameterFillInService.
                findDoctorParameterFillInJpas(questionaryId);
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

    /**
     * Поиск всех модулей пациента.
     * @param patientId id пациента
     * @return список модулей пациента
     */
    @GetMapping(path="/module/{patientId}", produces = "application/json;charset=UTF-8")
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
                    ModuleWithParametersDTO newModule = new ModuleWithParametersDTO(module);
                    newModule.setFrequency(questionary.getFrequency());
                    newModule.setQuestionaryId(questionary.getId());

                    modulesForPatient.add(newModule);
                }
            }
        }

        return modulesForPatient;
    }

    /**
     * Поиск всех лейблов гастро-параметров.
     * @param parameterId id параметра
     * @return список лейблов
     */
    @GetMapping(path="/module/gastroLabel/{parameterId}", produces = "application/json;charset=UTF-8")
    public List<GastroLabel> findGastroParameters(@PathVariable String parameterId) {
        if (parameterId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.GastroLabelJpasToGastroLabels(gastroLabelService.findGastroLabelByParameter(parameterId));
    }

    /**
     * Добавление заполненного модуля пациентом.
     * @param data заполненный модуль
     */
    @PostMapping(path = "/moduleFillIn", produces = "application/json;charset=UTF-8")
    public void addModuleFillIn(@RequestBody HashMap<String, Object> data) {
        if (ObjectUtils.anyNull(data.get("fillIn"), data.get("questionaryId"), data.get("datetime"))) {
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

        boolean isRedFillIn = false;
        try {
            isRedFillIn = changeNumberInHierarchy(answerJpas, questionaryId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        moduleFillInService.addModuleFillIn(fillInId.toString(), questionaryId, dateTime, isRedFillIn);

        for (QuestionaryAnswerJpa answerJpa : answerJpas) {
            questionaryAnswerService.addFieldAnswer(answerJpa.getValue(), String.valueOf(fillInId), answerJpa.getAnswerIdJpa().getParameterId());
        }

        System.out.println("Заполнение модуля пациентом успешно добавлено.");
    }

    /**
     * Получение всех заполненных модулей пациента.
     * @param patientId id пациента
     * @param doctorId id врача
     * @return список заполненных модулей пациента
     */
    @GetMapping(path="/moduleFillIn/{patientId}/{doctorId}", produces = "application/json;charset=UTF-8")
    public List<ModuleWithParametersDTO> findModuleFillInsByPatientDoctorIds(@PathVariable String patientId,
                                                                              @PathVariable String doctorId) {
        if (patientId == null || doctorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<Questionary> questionaries = mapper.ModuleJpasToModules(
                questionaryService.findQuestionaryJpaByPatientDoctorIdsAll(doctorId, patientId));
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
                    module.setParameterList(moduleWithParametersDTO.getParameterList()
                            .stream()
                            .map(Parameter::new)
                            .toList());
                    module.setRed(moduleFillInJpa.isRed());
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

    /**
     * Получение всех заполненных модулей пациента.
     * @param patientId id пациента
     * @return список заполненных модулей пациента
     */
    @GetMapping(path="/moduleFillIn/{patientId}", produces = "application/json;charset=UTF-8")
    public List<ModuleWithParametersDTO> findModuleFillInsByPatientId(@PathVariable String patientId) {
        if (patientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        List<Questionary> questionaries = mapper.ModuleJpasToModules(
                questionaryService.findQuestionaryJpaByPatientIdAll(patientId));
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
                    module.setRed(moduleFillInJpa.isRed());

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

    /**
     * Получение статистики в виде координат графика.
     * @param moduleId id модуля
     * @param patientId id пациента
     * @return статистика
     */
    @GetMapping(path="/statistics/{moduleId}/{patientId}", produces = "application/json;charset=UTF-8")
    public HashMap<String, LinkedHashMap<LocalDateTime, Object>> getStatistics(@PathVariable String moduleId,
                                                                                @PathVariable String patientId) {
        if (patientId == null || moduleId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }
        HashMap<String, LinkedHashMap<LocalDateTime, Object>> graphs = new HashMap<>();

        List<ModuleFillInJpa> fillIns = moduleFillInService.findModulesFillInJpaByPatientIdModuleId(moduleId, patientId);
        for (ModuleFillInJpa fillIn : fillIns) {
            List<QuestionaryAnswerJpa> questionaryAnswerJpas = questionaryAnswerService.findFieldAnswers(fillIn.getId());
            for (QuestionaryAnswerJpa answer : questionaryAnswerJpas) {
                ParameterJpa parameterJpa = parameterService.findParameterJpaById(answer.getAnswerIdJpa().getParameterId());
                if (parameterJpa == null) {
                    continue;
                }

                if (!graphs.containsKey(parameterJpa.getName())) {
                    graphs.put(parameterJpa.getName(), new LinkedHashMap<>());
                }

                graphs.get(parameterJpa.getName()).put(fillIn.getDatetime(), answer.getValue());
            }
        }

        return graphs;
    }

    /**
     * Получение всех смесей для кормления.
     * @return список смесей
     */
    @GetMapping(path="/formula", produces = "application/json;charset=UTF-8")
    public List<String> getFormulaNames() {
        return formulaService.findFormulaNames();
    }

    /**
     * Получение всех групп продуктов.
     * @return список групп продуктов
     */
    @GetMapping(path="/productGroup", produces = "application/json;charset=UTF-8")
    public List<ProductGroup> getProductGroups() {
        return mapper.ProductGroupJpasToProductGroups(productGroupService.findProductGroups());
    }

    /**
     * Получение продуктов конкретной группы.
     * @param productGroupId id группы продуктов
     * @return список продуктов
     */
    @GetMapping(path="/products/{productGroupId}", produces = "application/json;charset=UTF-8")
    public List<Product> getProducts(@PathVariable String productGroupId) {
        if (productGroupId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.ProductJpasToProducts(productService.findProductsInGroup(productGroupId));
    }

    /**
     * Получение всех продуктов с данными о них.
     * @return список продуктов
     */
    @GetMapping(path="/products", produces = "application/json;charset=UTF-8")
    public List<Product> getSortedProducts() {
        return mapper.ProductJpasToProducts(productService.findSortedProducts());
    }

    /**
     * Получение списка пациентов, ждущих ответа врача и отсортированных по приоритету.
     * @param doctorId id врача
     * @return список пациентов
     */
    @GetMapping(path="/patientsHierarchy/{doctorId}", produces = "application/json;charset=UTF-8")
    public List<User> getPatientsHierarchy(@PathVariable String doctorId) {
        if (doctorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.UserJpasToUsers(userService.findSortedPatientHierarchyJpas(doctorId));
    }

    private boolean changeNumberInHierarchy(List<QuestionaryAnswerJpa> answers, String questionaryId) {
        int number = 0;

        QuestionaryJpa questionary = questionaryService.findQuestionaryById(questionaryId);
        String patientId = questionary.getPatientId();
        String moduleId = questionary.getModuleId();

        UserJpa patient = userService.findUserJpaById(patientId);

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
            notificationService.addNotification(questionary.getDoctorId(),
                    "Пациент " + patient.getLastname() + " " + patient.getFirstname() + " ждет вашей обратной связи!",
                    LocalDateTime.now());

            return true;
        }

        return false;
    }

    private int getNumberOfRedFlags(String moduleId, List<QuestionaryAnswerJpa> answers,
                                    List<DoctorParameterFillInJpa> doctorParameters,
                                    String patientId, String questionaryId) {
        return switch (moduleId) {
            case ANTHROPOMETRY -> getNumberOfRedFlagsAnthropometry(answers, doctorParameters, patientId, questionaryId);
            case DIET -> getNumberOfRedFlagsDiet(answers, doctorParameters, patientId);
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
                                        List<DoctorParameterFillInJpa> doctorParameters,
                                        String patientId) {
        List<String> productMasses = new ArrayList<>();
        List<String> productNames = new ArrayList<>();

        for (QuestionaryAnswerJpa answerJpa : answers) {
            if (answerJpa.getAnswerIdJpa().getParameterId().equals(PRODUCT_MASS)) {
                productMasses = Arrays.stream(answerJpa.getValue()
                                .replace("[", "").replace("]", "")
                                .replace(" ", "")
                                .split(",")).toList();
            } else if (answerJpa.getAnswerIdJpa().getParameterId().equals(PRODUCT_NAME)) {
                productNames = Arrays.stream(answerJpa.getValue()
                                .replace("[", "").replace("]", "")
                                .split(",")).toList();
            }
        }

        int productMass = 0;
        int calories = 0;
        for (String mass : productMasses) {
            productMass += Integer.parseInt(mass);
        }

        for (String name : productNames) {
            calories += productService.findProductEnergyByName(name.trim());
        }

        int factEnergy = productMass * calories / 100;

        return countRedFlagsConsumption(factEnergy, doctorParameters, patientId);
    }

    private int getNumberOfRedFlagsFormulas(List<QuestionaryAnswerJpa> answers,
                                            List<DoctorParameterFillInJpa> doctorParameters,
                                            String patientId) {
        int mixtureMass = 0;
        int calories = 0;
        for (QuestionaryAnswerJpa answer : answers) {
            if (answer.getAnswerIdJpa().getParameterId().equals(MIXTURE_MASS)) {
                mixtureMass = Integer.parseInt(answer.getValue());
            } else if (answer.getAnswerIdJpa().getParameterId().equals(FORMULA_NAME)) {
                calories = Integer.parseInt(formulaService.findFormulaByName(answer.getValue()
                        .replace("[", "").replace("]", "").trim()));
            }
        }

        int factEnergy = mixtureMass * calories / 100;

        return countRedFlagsConsumption(factEnergy, doctorParameters, patientId);
    }

    private int countRedFlagsConsumption(int factEnergy,
                                         List<DoctorParameterFillInJpa> doctorParameters,
                                         String patientId) {
        UserJpa patient = userService.findUserJpaById(patientId);
        int sex = patient.getSex().equals("женский") ? 1 : 0;
        int age = Period.between(patient.getBirthday(), LocalDate.now()).getYears();

        List<QuestionaryAnswerJpa> lastAnthropometryAnswer = getLastAnthropometryAnswer(patientId);
        if (lastAnthropometryAnswer == null || lastAnthropometryAnswer.isEmpty()) {
            return 0;
        }

        int weight = 0;
        int height = 0;
        for (QuestionaryAnswerJpa answer : lastAnthropometryAnswer) {
            if (answer.getAnswerIdJpa().getParameterId().equals(WEIGHT)) {
                weight = Integer.parseInt(answer.getValue());
            } else if (answer.getAnswerIdJpa().getParameterId().equals(HEIGHT)) {
                height = Integer.parseInt(answer.getValue());
            }
        }

        double conversionCoefficient = 1;
        for (DoctorParameterFillInJpa parameter : doctorParameters) {
            if (parameter.getId().getParameterId().equals(CONVERSION_COEFFICIENT)) {
                conversionCoefficient = Integer.parseInt(parameter.getValue());
                break;
            }
        }
        double countedEnergy = getCountedEnergyShofield(age, sex, weight, height) * conversionCoefficient;

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

        if (previousAnswers.isEmpty()) {
            return new ArrayList<>();
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
        final String BM_ADMIXTURE_DOCTOR = "799f9b82-fb3e-11ee-88dc-00f5f80cf8ae";
        final String BM_TYPE_DOCTOR = "326c8381-fa55-11ee-88dc-00f5f80cf8ae";

        for (QuestionaryAnswerJpa answer : answers) {
            answer.setValue(answer.getValue().replace("[", "").replace("]", "").trim());
            switch (answer.getAnswerIdJpa().getParameterId()) {
                case VOMIT -> {
                    if (Integer.parseInt(answer.getValue()) >= 1) {
                        numberOfRedFlags += 1;
                    }
                }
                case NAUSEA -> {
                    if (answer.getValue().equals("препятствует приему пищи")) {
                        numberOfRedFlags += 1;
                    }
                }
                case APPETIT -> {
                    if (answer.getValue().equals("отсутствует")) {
                        numberOfRedFlags += 1;
                    }
                }
                case SKIN_RASH -> {
                    if (answer.getValue().equals("да")) {
                        numberOfRedFlags += 1;
                    }
                }
                case BM_TYPE -> {
                    for (DoctorParameterFillInJpa parameter : doctorParameters) {
                        if (parameter.getId().getParameterId().equals(BM_TYPE_DOCTOR)) {
                            List<String> redFlagsBM = List.of(parameter.getValue()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(","));

                            for (String redFlag : redFlagsBM) {
                                if (answer.getValue().trim().equals(redFlag)) {
                                    numberOfRedFlags += 1;
                                }
                            }
                        }
                    }
                }
                case BM_ADMIXTURE -> {
                    for (DoctorParameterFillInJpa parameter : doctorParameters) {
                        if (parameter.getId().getParameterId().equals(BM_ADMIXTURE_DOCTOR)) {
                            List<String> redFlagsBMAdmixture = List.of(parameter.getValue()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(","));

                            List<String> answerAdmixtures = List.of(answer.getValue()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(","));
                            for (String redFlag : redFlagsBMAdmixture) {
                                for (String answerAdmixture : answerAdmixtures) {
                                    if (redFlag.trim().equals(answerAdmixture.trim())) {
                                        numberOfRedFlags += 1;
                                    }
                                }
                            }
                        }
                    }
                }
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
