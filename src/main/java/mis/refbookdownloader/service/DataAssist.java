package mis.refbookdownloader.service;

import mis.refbookdownloader.model.ExternalRefbook;
import mis.refbookdownloader.model.Refbook;
import mis.refbookdownloader.model.RefbookColumn;
import mis.refbookdownloader.model.RefbookVersion;
import mis.refbookdownloader.model.rootPassport.RootPassport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Transactional
public class DataAssist {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public String findCodeOid(String oid) {
        String result = null;
        String regEx = "([^.]+)$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(oid);
        while (m.find()) {
            result = m.group();
        }
        return result;
    }

    public String findRootOid(String oid) {
        String result = null;
        String regEx = "((\\d+\\.)+)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(oid);
        while (m.find()) {
            result = m.group();
        }
        String root = result.substring(0, result.length() - 1);
        return root;
    }

    public Integer getRefSrcId(String rootOid) {
        Integer srcId = null;
        switch (rootOid) {
            case ("1.2.643.5.1.13.2.1.1"):
                srcId = 1;
                break;
            case ("1.2.643.5.1.13.3.7728241212886.1.1"):
                srcId = 2;
                break;
            case ("1.2.643.5.1.13.3.1047796261424.1.1"):
                srcId = 3;
                break;
            case ("1.2.643.5.1.13.3.8554345321447.2.1"):
                srcId = 4;
                break;
            case ("1.2.643.5.1.13.9.1035008861140.82.76.83"):
                srcId = 5;
                break;
            case ("1.2.643.5.1.13.2.7.1"):
                srcId = 7;
                break;
            case ("1.2.643.5.1.13.13.11"):
                srcId = 8;
                break;
            case ("1.2.643.5.1.13.13.99.2"):
                srcId = 10;
                break;
        }
        return srcId;
    }

    public Refbook checkCurrRefbookInDB(RootPassport rootPassport) {
        Refbook refbook = new Refbook();
        String sql = "SELECT mr.id ref_id, mr.full_name full_name, mr.short_name short_name, mr.object_id object_id, mr.source_id source_id " +
                "FROM public.mdm_refbook mr " +
                "INNER JOIN public.mdm_refbook_source mrs ON mrs.id = mr.source_id " +
                "WHERE mrs.object_id = CAST (:root AS text) " +
                "AND mr.object_id = CAST (:code AS text)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("root", findRootOid(rootPassport.getOid()));
        params.addValue("code", findCodeOid(rootPassport.getOid()));

        Integer refId = null;
        String fullName = null;
        String shortName = null;
        String objectId = null;
        Integer sourceId = null;
        try {
            List<Map<String, Object>> elemInfo = namedJdbcTemplate.queryForList(sql, params);
            if (!elemInfo.isEmpty()) {
                refId = (Integer) elemInfo.get(0).get("ref_id");
                fullName = (String) elemInfo.get(0).get("full_name");
                shortName = (String) elemInfo.get(0).get("short_name");
                objectId = (String) elemInfo.get(0).get("object_id");
                sourceId = (Integer) elemInfo.get(0).get("source_id");
            }
        } catch (DataAccessException e) {
            System.out.println(e);
        }

        refbook.setId(refId);
        refbook.setFullName(fullName);
        refbook.setShortName(shortName);
        refbook.setObjectId(objectId);
        refbook.setSourceId(sourceId);
        return refbook;
    }

    public ExternalRefbook checkCurrExternalRefbookInDB(RootPassport rootPassport) {
        ExternalRefbook externalRefbook = new ExternalRefbook();
        String sql = "SELECT mer.id id, mer.code code FROM " +
                "public.mdm_refbook mr " +
                "INNER JOIN mdm_external_refbook mer on mer.id = mr.id " +
                "WHERE mr.object_id = cast (:object_id as text) " +
                "AND mr.source_id = :source_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("object_id", findCodeOid(rootPassport.getOid()));
        params.addValue("source_id", getRefSrcId(findRootOid(rootPassport.getOid())));

        Integer id = null;
        try {
            List<Map<String, Object>> elemInfo = namedJdbcTemplate.queryForList(sql, params);
            if (!elemInfo.isEmpty()) {
                id = (Integer) elemInfo.get(0).get("id");
            }
        } catch (DataAccessException e) {
            System.out.println(e);
        }

        externalRefbook.setId(id);
        return externalRefbook;
    }

    public RefbookVersion checkCurrRefbookVersionInDB(RootPassport rootPassport) {
        RefbookVersion refbookVersion = new RefbookVersion();
        String sql = "SELECT " +
                "mr.full_name, mr.object_id object_id, mrv.\"version\" \"version\" " +
                "FROM public.mdm_refbook mr " +
                "INNER JOIN public.mdm_refbook_version mrv ON mrv.refbook_id = mr.id " +
                "WHERE mr.object_id = CAST (:object_id AS text) " +
                "AND mr.source_id = :source_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("object_id", findRootOid(rootPassport.getOid()));
        params.addValue("source_id", getRefSrcId(rootPassport.getOid()));

        String version = null;
        try {
            List<Map<String, Object>> elemInfo = namedJdbcTemplate.queryForList(sql, params);
            if (!elemInfo.isEmpty()) {
                version = (String) elemInfo.get(0).get("version");
            }
        } catch (DataAccessException e) {
            System.out.println(e);
        }

        refbookVersion.setVersion(version);
        return refbookVersion;
    }

//    public List<RefbookColumn> checkCurrRefbookColumns() {
//        List<RefbookColumn> refbookColumns = new ArrayList<>();
//
//
//
//        return refbookColumns;
//    }
}
