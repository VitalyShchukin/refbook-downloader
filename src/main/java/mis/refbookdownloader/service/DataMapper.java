package mis.refbookdownloader.service;

import mis.refbookdownloader.model.*;
import mis.refbookdownloader.model.rootPassport.RootField;
import mis.refbookdownloader.model.rootPassport.RootPassport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataMapper {

    @Autowired
    private DataAssist dataAssist;

    public FullRefbook getMappedData(RootPassport rootPassport, List<List<Object>> listOfJsonRecords) {
//        DataAssist dataAssist = new DataAssist();
        FullRefbook fullRB = new FullRefbook();

        Refbook refbook = new Refbook();
        ExternalRefbook externalRefbook = new ExternalRefbook();
        RefbookVersion refbookVersion = new RefbookVersion();

        String rootOid = dataAssist.findRootOid(rootPassport.getOid());
        String codeOid = dataAssist.findCodeOid(rootPassport.getOid());
        Refbook refbookDB = dataAssist.checkRefbookInBase(rootOid, codeOid);

        if (refbookDB == null) {
            refbook.setFullName(rootPassport.getFullName());
            refbook.setShortName(rootPassport.getShortName());
            refbook.setObjectId(dataAssist.findCodeOid(rootPassport.getOid()));
            refbook.setSourceId(dataAssist.getRefSrcId(rootOid));
            refbook.setRefbookVersion(refbookVersion);
        }

        //into mdm_record_column
        List<Record> records = new ArrayList<>();
        List<RecordColumn> recordColumns = new ArrayList<>();

        //into mdm_refbook_column
        List<RefbookColumn> refbookColumns = new ArrayList<>();
        for (RootField i : rootPassport.getFields()) {
            RefbookColumn col = new RefbookColumn();
            col.setName(i.getField());
            col.setRefbookVersion(refbookVersion);
            col.setRecordColumns(recordColumns);
            refbookColumns.add(col);
        }

        for (List<Object> i : listOfJsonRecords) {
            Record rec = new Record();
            rec.setRefbookVersion(refbookVersion);
            rec.setRecordColumns(recordColumns);
            records.add(rec);
            for (int j = 0; j < i.size(); j++) {
                RecordColumn recCol = new RecordColumn();
                Map<Object, Object> mapObj = (Map<Object, Object>) i.get(j);
                String recColumn = (String) mapObj.get("column");
                String recValue = (String) mapObj.get("value");
                recCol.setValue(recValue);
                recCol.setRefbookColumn(findRefbookColumn(refbookColumns, recColumn));
                recCol.setRecord(rec);

                recordColumns.add(recCol);
            }
        }

        //into mdm_refbook_version
        refbookVersion.setRefbook(refbook);
        String dateInString = rootPassport.getPublishDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDate date = LocalDate.parse(dateInString, formatter);
        refbookVersion.setDate(date);
        refbookVersion.setVersion(rootPassport.getVersion());
        refbookVersion.setRefbookColumns(refbookColumns);

        //into mdm_external_refbook
        externalRefbook.setRefbook(refbook);
        externalRefbook.setCode(rootPassport.getOid());

        fullRB.setRecords(records);
        fullRB.setExternalRefbook(externalRefbook);
        fullRB.setRecordColumns(recordColumns);
        fullRB.setRefbook(refbook);
        fullRB.setRefbookColumns(refbookColumns);
        fullRB.setRefbookVersion(refbookVersion);

        return fullRB;
    }

    public RefbookColumn findRefbookColumn(List<RefbookColumn> refbookColumns, String recColumn) {
        RefbookColumn rfbc = new RefbookColumn();
        for (RefbookColumn rc : refbookColumns) {
            if (rc.getName().equals(recColumn)) {
                return rc;
            }
        }
        return rfbc;
    }
}
