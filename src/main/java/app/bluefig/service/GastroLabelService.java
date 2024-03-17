package app.bluefig.service;

import app.bluefig.entity.GastroLabelJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GastroLabelService {
    List<GastroLabelJpa> findGastroLabelByParameter(String parameterId);
}
