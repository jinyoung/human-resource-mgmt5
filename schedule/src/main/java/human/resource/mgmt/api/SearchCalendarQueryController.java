package human.resource.mgmt.api;

import human.resource.mgmt.query.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchCalendarQueryController {

    private final QueryGateway queryGateway;

    public SearchCalendarQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/calendars")
    public CompletableFuture findAll(SearchCalendarQuery query) {
        return queryGateway
            .query(
                query,
                ResponseTypes.multipleInstancesOf(CalendarReadModel.class)
            )
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<CalendarReadModel>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        EntityModel<CalendarReadModel> model = EntityModel.of(
                            resource
                        );

                        model.add(
                            Link
                                .of("/calendars/" + resource.getUserId())
                                .withSelfRel()
                        );

                        modelList.add(model);
                    });

                CollectionModel<CalendarReadModel> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/calendars/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        SearchCalendarSingleQuery query = new SearchCalendarSingleQuery();
        query.setUserId(id);

        return queryGateway
            .query(
                query,
                ResponseTypes.optionalInstanceOf(CalendarReadModel.class)
            )
            .thenApply(resource -> {
                if (!resource.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                EntityModel<CalendarReadModel> model = EntityModel.of(
                    resource.get()
                );
                model.add(
                    Link
                        .of("/calendars/" + resource.get().getUserId())
                        .withSelfRel()
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            })
            .exceptionally(ex -> {
                throw new RuntimeException(ex);
            });
    }
}
