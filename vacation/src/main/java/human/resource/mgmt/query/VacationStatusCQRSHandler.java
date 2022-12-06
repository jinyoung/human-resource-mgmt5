package human.resource.mgmt.query;

import human.resource.mgmt.aggregate.*;
import human.resource.mgmt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("vacationStatus")
public class VacationStatusCQRSHandler {

    @Autowired
    private VacationReadModelRepository repository;

    @QueryHandler
    public List<VacationReadModel> handle(VacationStatusQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<VacationReadModel> handle(VacationStatusSingleQuery query) {
        return repository.findById(query.get());
    }

    @EventHandler
    public void whenVacationRegistered_then_UPDATE(
        VacationRegisteredEvent event
    ) throws Exception {
        repository
            .findById(event.get())
            .ifPresent(entity -> {
                VacationAggregate aggregate = new VacationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);
            });
    }

    @EventHandler
    public void whenVacationCancelled_then_UPDATE(VacationCancelledEvent event)
        throws Exception {
        repository
            .findById(event.get())
            .ifPresent(entity -> {
                VacationAggregate aggregate = new VacationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);
            });
    }

    @EventHandler
    public void whenVacationApproved_then_UPDATE(VacationApprovedEvent event)
        throws Exception {
        repository
            .findById(event.get())
            .ifPresent(entity -> {
                VacationAggregate aggregate = new VacationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);
            });
    }

    @EventHandler
    public void whenVacationRejected_then_UPDATE(VacationRejectedEvent event)
        throws Exception {
        repository
            .findById(event.get())
            .ifPresent(entity -> {
                VacationAggregate aggregate = new VacationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);
            });
    }

    @EventHandler
    public void whenVacationUsed_then_UPDATE(VacationUsedEvent event)
        throws Exception {
        repository
            .findById(event.get())
            .ifPresent(entity -> {
                VacationAggregate aggregate = new VacationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);
            });
    }
}
