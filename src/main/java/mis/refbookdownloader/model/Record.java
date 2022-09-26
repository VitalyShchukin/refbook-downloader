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
@Table(name = "mdm_record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mdm_record_seq")
    @SequenceGenerator(name = "mdm_record_seq", sequenceName = "mdm_record_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "refbook_version_id",referencedColumnName = "id")
    private RefbookVersion refbookVersion;
}
