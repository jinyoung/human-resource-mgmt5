package human.resource.mgmt.query;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class SearchCalendarQuery {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date from;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date to;

    String userId;
}
