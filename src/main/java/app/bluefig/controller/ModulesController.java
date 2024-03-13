package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.entity.FieldAnswerJpa;
import app.bluefig.entity.ModuleFieldJpa;
import app.bluefig.entity.ParameterJpa;
import app.bluefig.model.*;
import app.bluefig.model.Module;
import app.bluefig.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    private ModuleFieldServiceImpl moduleFieldService;

    @Autowired
    private ModuleServiceImpl moduleService;

    @Autowired
    private ModuleFillInServiceImpl moduleFillInService;

    @Autowired
    private FieldAnswerService fieldAnswerService;

    @GetMapping("/parameters")
    public List<Parameter> getParameters() {
        List<ParameterJpa> parametersJpas = parameterService.findParametersJpa();
        return mapper.ParameterJpasToParameters(parametersJpas);
    }

    @PostMapping("/module")
    public void addModule(@RequestBody HashMap<String, Object> data) {
        List<ModuleFieldJpa> module = (List<ModuleFieldJpa>) data.get("questionary");
        String patientId = data.get("patientId").toString();
        String doctorId = data.get("doctorId").toString();
        UUID questionaryId = UUID.randomUUID();

        moduleService.addModule(questionaryId.toString(), doctorId, patientId);

        for (ModuleFieldJpa moduleFieldJpa : module) {
            moduleFieldService.addModuleField(String.valueOf(questionaryId), moduleFieldJpa.getOrderNumber(),
                    moduleFieldJpa.getFrequency(), moduleFieldJpa.getParameterId());
        }
    }

    @GetMapping("/module/{patient_id}/{doctor_id}")
    public HashMap<String, List<ModuleField>> findModulesByPatientDoctorIds(@PathVariable String patientId, @PathVariable String doctorId) {
        List<Module> modules = mapper.ModuleJpasToModules(moduleService.findModulesJpaByPatientDoctorIds(doctorId, patientId));
        HashMap<String, List<ModuleField>> moduleFields = new HashMap<>();

        for (Module module : modules) {
            List<ModuleFieldJpa> fieldJpas = moduleFieldService.findModuleFieldsBy(module.getId().toString());
            List<ModuleField> fields = mapper.ModuleFieldJpasToModuleFields(fieldJpas);
            moduleFields.put(module.getId().toString(), fields);
        }

        return moduleFields;
    }

    @PostMapping("moduleFillIn")
    public void addModuleFillIn(@RequestBody HashMap<String, Object> data) {
        List<FieldAnswerJpa> fieldAnswers = (List<FieldAnswerJpa>) data.get("fillIn");
        String questionaryId = data.get("questionaryId").toString();
        LocalDateTime dateTime = (LocalDateTime) data.get("datetime");
        UUID fillInId = UUID.randomUUID();

        moduleFillInService.addModuleFillIn(fillInId.toString(), questionaryId, dateTime);

        for (FieldAnswerJpa fieldAnswer : fieldAnswers) {
            fieldAnswerService.addFieldAnswer(fieldAnswer.getValue(), fieldAnswer.getFieldAnswerIdJpa().getFillIn(), fieldAnswer.getFieldAnswerIdJpa().getFieldId());
        }
    }

    @GetMapping("/moduleFillIn/{patient_id}/{doctor_id}")
    public HashMap<String, List<FieldAnswer>> findModuleFillInsByPatientDoctorIds(@PathVariable String patientId,
                                                                                  @PathVariable String doctorId) {
        List<ModuleFillIn> modules = mapper.ModuleFillInJpasToModuleFillIns(moduleFillInService.findModulesFillInJpaByPatientDoctorIds(doctorId, patientId));
        HashMap<String, List<FieldAnswer>> moduleFieldAnswers = new HashMap<>();

        for (ModuleFillIn fillIn : modules) {
            List<FieldAnswerJpa> fieldAnswerJpas = fieldAnswerService.findFieldAnswers(fillIn.getId().toString());
            List<FieldAnswer> fieldsAnswers = mapper.FieldAnswerJpasToFieldAnswers(fieldAnswerJpas);
            moduleFieldAnswers.put(fillIn.getId().toString(), fieldsAnswers);
        }

        return moduleFieldAnswers;
    }
}
