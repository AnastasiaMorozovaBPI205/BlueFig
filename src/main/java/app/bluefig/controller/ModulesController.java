package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.entity.ParameterJpa;
import app.bluefig.entity.RecommendationJpa;
import app.bluefig.model.Parameter;
import app.bluefig.model.Recommendation;
import app.bluefig.service.ParameterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ModulesController {
    @Autowired
    private MapStructMapper mapper;

    @Autowired
    private ParameterServiceImpl parameterService;

    @GetMapping("/parameters")
    public List<Parameter> getParameters() {
        List<ParameterJpa> parametersJpas = parameterService.findParametersJpa();
        return mapper.ParameterJpasToParameters(parametersJpas);
    }
}
