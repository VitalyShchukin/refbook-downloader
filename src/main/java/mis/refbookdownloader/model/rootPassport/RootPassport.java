package mis.refbookdownloader.model.rootPassport;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RootPassport{
    private String result;
    private Object resultText;
    private Object resultCode;
    private String identifier;
    private String oid;
    private String version;
    private int rowsCount;
    private String createDate;
    private String publishDate;
    private String lastUpdate;
    private String fullName;
    private String shortName;
    private String description;
    private String structureNotes;
    private String releaseNotes;
    private ArrayList<Object> laws;
    private int respOrganizationId;
    private int authOrganizationId;
    private int typeId;
    private int groupId;
    private String approveDate;
    private ArrayList<RootField> fields;
    private ArrayList<RootKey> keys;
    private ArrayList<RootCode> codes;
    private Object archive;
    private boolean hierarchical;
}

