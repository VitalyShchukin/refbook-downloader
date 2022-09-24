package mis.refbookdownloader.service;

import mis.refbookdownloader.model.Refbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Transactional
public class DataAssist {

    @PersistenceContext
    private EntityManager entityManager;

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

    public Refbook checkRefbookInBase(String root, String code) {
        Refbook refbook = new Refbook();
        try {
            String sql = "SELECT * FROM public.mdm_refbook mr " +
                    "INNER JOIN mdm_refbook_source mrs ON mrs.id = mr.source_id " +
                    "WHERE mrs.object_id = CAST (? AS text) " +
                    "AND mr.object_id = CAST (? AS text)";
//            Query query = entityManager.createNativeQuery(sql, Refbook.class);
//            query.setParameter(1, root);
//            query.setParameter(2, code);
//            refbook = (Refbook) query.getSingleResult();

            TypedQuery<Refbook> query = entityManager.createQuery(sql, Refbook.class);
            query.setParameter(1, root);
            query.setParameter(2, code);
            List<Refbook> ref = query.getResultList();
            String a = null;
        } catch (Exception e) {
            System.out.println("e");
        }

        return refbook;
    }


    //    public Integer getDepartmentIdByNsiCode(String depCode) {
//        String sql = "SELECT * FROM public.pim_department_code WHERE value = '"
//                + depCode.trim() + "'";
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        Integer depId = null;
//
//        try {
//            List<Map<String, Object>> elemInfo = jdbcTemplate.queryForList(sql, params);
//            if (!elemInfo.isEmpty()) {
//                depId = (Integer) elemInfo.get(0).get("department_id");
//            }
//        } catch (DataAccessException e) {
//            log.log(Level.WARNING, "Exception: ", e);
//        }
//        return depId;
//    }
}
