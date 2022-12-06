package human.resource.mgmt.api;

import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import human.resource.mgmt.query.*;


  
@RestController
public class QueryController {

  private final QueryGateway queryGateway;


  public QueryController(QueryGateway queryGateway) {
      this.queryGateway = queryGateway;
  }
  

  @GetMapping("/vacationStatuses")
  public CompletableFuture findAll(Query query) {
      return queryGateway.query(query , ResponseTypes.multipleInstancesOf(.class))
            
             .thenApply(resources -> {
                List modelList = new ArrayList<EntityModel<>>();
                
                resources.stream().forEach(resource ->{
                    EntityModel<> model = EntityModel.of(
                        resource
                    );

                    model.add(
                        Link
                        .of("/vacationStatuses/" + resource.get())
                        .withSelfRel()
                    );
    
                    modelList.add(model);
                });

                CollectionModel<> model = CollectionModel.of(
                    modelList
                );

                return new ResponseEntity<>(model, HttpStatus.OK);
            });
            

  }

  @GetMapping("/vacationStatuses/{id}")
  public CompletableFuture findById(@PathVariable("id")  id) {
    SingleQuery query = new SingleQuery();
    query.set(id);

      return queryGateway.query(query, ResponseTypes.optionalInstanceOf(.class))
              .thenApply(resource -> {
                if(!resource.isPresent()){
                  return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                EntityModel<> model = EntityModel.of(resource.get());
                model
                      .add(Link.of("/vacationStatuses/" + resource.get().get()).withSelfRel());
              
                return new ResponseEntity<>(model, HttpStatus.OK);
            }).exceptionally(ex ->{
              throw new RuntimeException(ex);
            });

  }


}
