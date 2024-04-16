package app.bluefig.service;

import app.bluefig.entity.FormulaJpa;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormulaService {
    List<String> findFormulaNames();
    String findFormulaByName(@Param("name") String name);

}
