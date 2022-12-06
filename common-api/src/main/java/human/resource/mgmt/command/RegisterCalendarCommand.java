package human.resource.mgmt.command;

import human.resource.mgmt.query.*;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Data
public class RegisterCalendarCommand {

    //<<< Etc / ID Generation
    private String userId; // Please comment here if you want user to enter the id directly
    //>>> Etc / ID Generation
    private List<Event> events;
}
