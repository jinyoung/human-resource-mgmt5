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
public class VacationStatusQueryController {

    private final QueryGateway queryGateway;

    public VacationStatusQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/vacations")
    public CompletableFuture findAll(VacationStatusQuery query) {
        return queryGateway
            .query(
                query,
                ResponseTypes.multipleInstancesOf(VacationReadModel.class)
            )
            .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<VacationReadModel>>();

                resources
                    .stream()
                    .forEach(resource -> {
                        modelList.add(hateoas(resource));
                    });

                CollectionModel<VacationReadModel> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
    }

    @GetMapping("/vacations/{id}")
    public CompletableFuture findById(@PathVariable("id") String id) {
        VacationStatusSingleQuery query = new VacationStatusSingleQuery();
        query.setId(id);

        return queryGateway
            .query(
                query,
                ResponseTypes.optionalInstanceOf(VacationReadModel.class)
            )
            .thenApply(resource -> {
                if (!resource.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(
                    hateoas(resource.get()),
                    HttpStatus.OK
                );
            })
            .exceptionally(ex -> {
                throw new RuntimeException(ex);
            });
    }

    EntityModel<VacationReadModel> hateoas(VacationReadModel resource) {
        EntityModel<VacationReadModel> model = EntityModel.of(resource);

        model.add(Link.of("/vacations/" + resource.getId()).withSelfRel());

        model.add(
            Link
                .of("/vacations/" + resource.getId() + "/cancel")
                .withRel("cancel")
        );
        model.add(
            Link
                .of("/vacations/" + resource.getId() + "/approve")
                .withRel("approve")
        );
        model.add(
            Link
                .of("/vacations/" + resource.getId() + "/confirmused")
                .withRel("confirmused")
        );
        model.add(
            Link
                .of("/vacations/" + resource.getId() + "/events")
                .withRel("events")
        );

        return model;
    }
}
