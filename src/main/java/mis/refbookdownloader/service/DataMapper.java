package mis.refbookdownloader.service;

import mis.refbookdownloader.model.*;
import mis.refbookdownloader.model.rootPassport.RootField;
import mis.refbookdownloader.model.rootPassport.RootPassport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataMapper {

    @Autowired
    private DataAssist dataAssist;

    public Refbook getRefbook(RootPassport rootPassport) {
        Refbook refbook = new Refbook();
        refbook.setFullName(rootPassport.getFullName());
        refbook.setShortName(rootPassport.getShortName());
        refbook.setObjectId(dataAssist.findCodeOid(rootPassport.getOid()));
        refbook.setSourceId(dataAssist.getRefSrcId(dataAssist.findRootOid(rootPassport.getOid())));
        return refbook;
    }

    public ExternalRefbook getExternalRefbook(RootPassport rootPassport) {
        ExternalRefbook externalRefbook = new ExternalRefbook();
        externalRefbook.setCode(rootPassport.getOid());
        return externalRefbook;
    }

    public RefbookVersion getRefbookVersion(RootPassport rootPassport) {
        RefbookVersion refbookVersion = new RefbookVersion();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String dateInString = rootPassport.getPublishDate();
        LocalDate date = LocalDate.parse(dateInString, formatter);
        refbookVersion.setDate(date);
        refbookVersion.setVersion(rootPassport.getVersion());
        return refbookVersion;
    }

    public List<RefbookColumn> getRefbookColumns(RootPassport rootPassport, RefbookVersion refbookVersion) {
        List<RefbookColumn> refbookColumns = new ArrayList<>();
        for (RootField i : rootPassport.getFields()) {
            RefbookColumn col = new RefbookColumn();
            col.setName(i.getField());
            col.setTitle(i.getAlias());
            col.setRefbookVersion(refbookVersion);
            refbookColumns.add(col);
        }
        return refbookColumns;
    }

    public RecordsAndRecordColumns getRecordsAndRecordColumns(List<List<Object>> listOfJsonRecords, RefbookVersion refbookVersion, List<RefbookColumn> refbookColumns) {
        RecordsAndRecordColumns recordsAndRecordColumns = new RecordsAndRecordColumns();
        List<RecordColumn> recordColumns = new ArrayList<>();
        List<Record> records = new ArrayList<>();

        for (List<Object> i : listOfJsonRecords) {
            Record rec = new Record();
            rec.setRefbookVersion(refbookVersion);
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
        recordsAndRecordColumns.setRecords(records);
        recordsAndRecordColumns.setRecordColumns(recordColumns);

        return recordsAndRecordColumns;
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
