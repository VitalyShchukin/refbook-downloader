package mis.refbookdownloader.service;

import com.google.gson.Gson;
import mis.refbookdownloader.model.rootData.RootData;
import mis.refbookdownloader.model.rootPassport.RootPassport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

public class NsiSwagger {

    private static final String NSI_INTEGRATION_REST_URL = "http://nsi.rosminzdrav.ru/port/rest/";
    private static final String NSI_TOKEN = "1fee45c9-a20c-42f3-bbb8-4f4f88c428b2";

    public static String getJson(String sectionName, String identifier, String version, int page) {
        String jsonString = null;

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NSI_INTEGRATION_REST_URL + sectionName)
                    .queryParam("userKey", NSI_TOKEN)
                    .queryParam("identifier", identifier)
                    .queryParam("version", version)
                    .queryParam("page", page);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            jsonString = response.getBody();

        } catch (Exception e) {
            System.out.println(e);
        }
        return jsonString;
    }

    public static RootPassport getRefbookPassport(String refbookName, String version) {
        String refbookPassport = getJson("passport", refbookName, version, 1);
        Gson gp = new Gson();
        RootPassport rootPassport = gp.fromJson(refbookPassport, RootPassport.class);
        return rootPassport;
    }

    public static RootData getRefbookData(String refbookName, String version, int page) {
        String refbookData = getJson("data", refbookName, version, page);
        Gson gd = new Gson();
        RootData rootData = gd.fromJson(refbookData, RootData.class);
        return rootData;
    }

    public static List<List<Object>> getListOfRecords(String refbookName, String version, int page) {
        List<List<Object>> listOfrec = new ArrayList<>();

        boolean isEndOfRefbook = false;
        while (!isEndOfRefbook) {
            page++;
            RootData rootData = getRefbookData(refbookName, version, page);
            for (int i = 0; i < rootData.getList().size(); i++) {
                listOfrec.add(rootData.getList().get(i));
            }
            if (rootData.getList().size() == 0) {
                isEndOfRefbook = true;
            }
        }
        return listOfrec;
    }
}
