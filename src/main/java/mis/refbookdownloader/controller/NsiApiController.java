package mis.refbookdownloader.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import mis.refbookdownloader.model.*;
import mis.refbookdownloader.model.rootPassport.RootPassport;
import mis.refbookdownloader.service.DataAssist;
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
    @Autowired
    private DataAssist dataAssist;

    @PostMapping("/ref")
    public String getNsiData(@RequestParam(value = "refbookName") String refbookName,
                             @RequestParam(value = "version") String version,
                             Model model) throws JsonProcessingException {
        RootPassport rootPassport = NsiSwagger.getRefbookPassport(refbookName, version);

        int page = 0; //start page in request
        List<List<Object>> listOfJsonRecords = NsiSwagger.getListOfRecords(refbookName, version, page);

        Refbook refbook = new Refbook();

        if (dataAssist.checkCurrRefbookInDB(rootPassport).getId() != null) {
            refbook = dataAssist.checkCurrRefbookInDB(rootPassport);
        } else {
            refbook = dataMapper.getRefbook(rootPassport);
            entityManager.persist(refbook);
        }

        ExternalRefbook externalRefbook = new ExternalRefbook();
        if (dataAssist.checkCurrExternalRefbookInDB(rootPassport).getId() != null) {
            externalRefbook = dataAssist.checkCurrExternalRefbookInDB(rootPassport);
            externalRefbook.setRefbook(refbook);
        } else {
            externalRefbook = dataMapper.getExternalRefbook(rootPassport);
            externalRefbook.setId(refbook.getId());
            externalRefbook.setRefbook(refbook);
            entityManager.persist(externalRefbook);
        }

        RefbookVersion refbookVersion = new RefbookVersion();
        if (dataAssist.checkCurrRefbookVersionInDB(rootPassport).getId() != null) {
            refbookVersion = dataAssist.checkCurrRefbookVersionInDB(rootPassport);
            refbookVersion.setRefbook(refbook);
        } else {
            refbookVersion = dataMapper.getRefbookVersion(rootPassport);
            refbookVersion.setRefbook(refbook);
            entityManager.persist(refbookVersion);
        }

        List<RefbookColumn> refbookColumns = dataMapper.getRefbookColumns(rootPassport, refbookVersion);
        for (RefbookColumn refCols : refbookColumns) {
            entityManager.persist(refCols);
        }

//        for(List<Object> listOfJsonRecords:listOfJsonRecords) {
            RecordsAndRecordColumns recordsAndRecordColumns = dataMapper.getRecordsAndRecordColumns(listOfJsonRecords, refbookVersion, refbookColumns);
            for (RecordColumn recCols : recordsAndRecordColumns.getRecordColumns()) {
                entityManager.persist(recCols);
            }
            for (Record recs : recordsAndRecordColumns.getRecords()) {
                entityManager.persist(recs);
            }
//        }

        return "redirect:/ref";
    }
}