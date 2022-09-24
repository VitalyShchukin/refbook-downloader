package mis.refbookdownloader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mdm_refbook_column")
public class RefbookColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "refbook_version_id")
    private RefbookVersion refbookVersion;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "refbookColumn", cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.SUBSELECT)
    private List<RecordColumn> recordColumns;
}
