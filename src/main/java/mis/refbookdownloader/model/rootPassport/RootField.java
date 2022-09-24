package mis.refbookdownloader.model.rootPassport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RootField {
    private String field;
    private String dataType;
    private String alias;
    private String description;
}
