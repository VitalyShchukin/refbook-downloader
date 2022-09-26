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
@Table(name = "mdm_record_column")
public class RecordColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mdm_record_column_seq")
    @SequenceGenerator(name = "mdm_record_column_seq", sequenceName = "mdm_record_column_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "value")
    private String value;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "record_id")
    private Record record;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "column_id")
    private RefbookColumn refbookColumn;
}
