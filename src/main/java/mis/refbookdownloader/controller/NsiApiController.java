package mis.refbookdownloader.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import mis.refbookdownloader.model.*;
import mis.refbookdownloader.model.rootPassport.RootPassport;
import mis.refbookdownloader.service.DataMapper;
import mis.refbookdownloader.service.NsiSwagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Controller
@Transactional
public class NsiApiController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataMapper dataMapper;

    @PostMapping("/ref")
    public String getNsiData(@RequestParam(value = "refbookName") String refbookName,
                             @RequestParam(value = "version") String version,
                             Model model) throws JsonProcessingException {
        RootPassport rootPassport = NsiSwagger.getRefbookPassport(refbookName, version);

        int page = 0; //start page in request
        List<List<Object>> listOfJsonRecords = NsiSwagger.getListOfRecords(refbookName, version, page);
        FullRefbook frb = dataMapper.getMappedData(rootPassport, listOfJsonRecords);

        entityManager.persist(frb.getRefbook());
        entityManager.persist(frb.getExternalRefbook());
//        for() {
        entityManager.persist(frb.getRefbookVersion());
//        }
        return "redirect:/ref";
    }
}