package app.bluefig.service;

import app.bluefig.repository.FormulaJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormulaServiceImpl implements FormulaService {
    @Autowired
    FormulaJpaRepository formulaJpaRepository;

    @Override
    public List<String> findFormulaNames() {
        return formulaJpaRepository.findFormulaNames();
    }
}
