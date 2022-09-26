package mis.refbookdownloader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mdm_refbook")
public class Refbook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mdm_refbook_seq")
    @SequenceGenerator(name = "mdm_refbook_seq", sequenceName = "mdm_refbook_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "source_id")
    private Integer sourceId;
}
