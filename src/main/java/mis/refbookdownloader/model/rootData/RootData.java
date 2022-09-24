package mis.refbookdownloader.model.rootData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RootData {
    private String result;
    private Object resultText;
    private Object resultCode;
    private int total;
    private List<List<Object>> list;
}