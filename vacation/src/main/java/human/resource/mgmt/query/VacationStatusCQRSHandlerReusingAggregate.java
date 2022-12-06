package human.resource.mgmt.query;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("vacationStatus")
public class VacationStatusCQRSHandlerReusingAggregate {

    @Autowired
    private VacationStatusRepository vacationStatusRepository;

    @QueryHandler
    public List<VacationReadModel> handle(VacationStatusQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<VacationReadModel> handle(VacationStatusSingleQuery query) {
        return repository.findById(query.getId());
    }

    @EventHandler
    public void whenVacationRegistered_then_CREATE(
        VacationRegisteredEvent event
    ) throws Exception {
        VacationReadModel entity = new VacationReadModel();
        VacationAggregate aggregate = new VacationAggregate();
        aggregate.on(event);

        BeanUtils.copyProperties(aggregate, entity);

        repository.save(entity);
    }

    @EventHandler
    public void whenVacationCancelled_then_UPDATE(VacationCancelledEvent event)
        throws Exception {
        repository
            .findById(event.getId())
            .ifPresent(entity -> {
                VacationAggregate aggregate = new VacationAggregate();

        if (vacationStatusOptional.isPresent()) {
            VacationStatus vacationStatus = vacationStatusOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
            vacationStatus.setStatus("APPROVED");
            // view 레파지 토리에 save
            vacationStatusRepository.save(vacationStatus);
        }
    }

    @EventHandler
    public void whenVacationApproved_then_UPDATE(VacationApprovedEvent event)
        throws Exception {
        repository
            .findById(event.getId())
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
            .findById(event.getId())
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
            .findById(event.getId())
            .ifPresent(entity -> {
                VacationAggregate aggregate = new VacationAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);
            });
    }
}
