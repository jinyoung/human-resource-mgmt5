package human.resource.mgmt.query;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "VacationStatus_table")
@Data
@Relation(collectionRelation = "vacationStatuses")
public class VacationStatus {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    private Date startDate;
    private Date endDate;
    private String reason;
    private String status;
}
