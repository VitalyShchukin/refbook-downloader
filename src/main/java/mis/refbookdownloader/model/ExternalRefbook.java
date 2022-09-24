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
@Table(name = "mdm_external_refbook")
public class ExternalRefbook {

    @Id
    @Column(name = "id")
    private Integer id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Refbook refbook;

    @Column(name = "code")
    private String code;
}