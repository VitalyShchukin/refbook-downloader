package mis.refbookdownloader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mdm_refbook_version")
public class RefbookVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "version")
    private String version;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "refbook_id",referencedColumnName = "id")
    private Refbook refbook;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "refbookVersion", cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.SUBSELECT)
    private List<RefbookColumn> refbookColumns;
}
