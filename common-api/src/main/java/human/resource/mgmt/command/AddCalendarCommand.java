package human.resource.mgmt.command;

import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Data
public class AddCalendarCommand {

    @TargetAggregateIdentifier
    private String userId;

    private String title;
}
