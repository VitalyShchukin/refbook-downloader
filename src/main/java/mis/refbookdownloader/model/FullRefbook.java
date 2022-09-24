package mis.refbookdownloader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullRefbook {
    private List<Record> records;
    private List<RecordColumn> recordColumns;
    private Refbook refbook;
    private List<RefbookColumn> refbookColumns;
    private RefbookVersion refbookVersion;
    private ExternalRefbook externalRefbook;
}
