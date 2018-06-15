package com.example.demo.controllers;

import com.example.demo.repositories.EventRepository;
import com.example.demo.services.EventService;
import com.example.demo.utilities.Enums.MessageType;
import com.example.demo.validators.ListOfEntitiesValidator;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.Event;
import com.example.demo.utilities.Message;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;


@RestController()
@RequestMapping("/events/")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private EventRepository eventRepository;
    private ListOfEntitiesValidator<Event> listOfEventValidator;
    private EventService eventService;

    public EventController(EventRepository eventRepository, ListOfEntitiesValidator<Event> listOfEventValidator,
                           EventService eventService) {
        this.eventRepository = eventRepository;
        this.listOfEventValidator = listOfEventValidator;
        this.eventService = eventService;
    }

    /**
     * http://127.0.0.1:8080/events/?access_token=30a6a5df-68f5-4179-8db2-f5622fadacf1
     * Example body:
     * [
     * {
     * "name": "Event1",
     * "eventType": "BUSINESS_DINNER",
     * "seats": 100,
     * "attractionList": [
     * {"name": "Free Bear"},
     * {"name": "Free Coffee"},
     * {"name": "Free Water"}
     * ]
     * },
     * {"name": "Event2", "eventType": "BUSINESS_DINNER", "seats": 100},
     * {"name": "Event3", "eventType": "BUSINESS_DINNER", "seats": 100}
     * ]
     *
     * @param eventList
     * @return ResponseEntity
     */
    @PostMapping()
    public ResponseEntity create(@RequestBody List<Event> eventList) {

        List<List<FieldError>> listOfErrors = listOfEventValidator.validate(eventList);

//        TODO put it into service in the future.
//        TODO try it with java streams?.
        if (!listOfEventValidator.hasErrors(listOfErrors)) {
            eventList.forEach(event -> {
                event.getAttractionList().forEach(
                        attraction -> {
                            attraction.setEvent(event);
                        });
            });
            eventRepository.saveAll(eventList);
            return new ResponseEntity<>(eventList, HttpStatus.OK);
        }
        return new ResponseEntity<>(listOfErrors, HttpStatus.BAD_REQUEST);

    }

    @GetMapping()
    public ResponseEntity get() {
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Message> delete(@PathVariable Long id) {

        Event event;
        try {
            event = eventRepository.getOne(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    new Message(MessageType.ERROR, "There is no event with id=" + id), HttpStatus.BAD_REQUEST
            );
        }

        eventRepository.deleteById(id);
        return new ResponseEntity<>(
                new Message(MessageType.INFO, "You have deleted object " + event.toString()), HttpStatus.OK
        );

    }

    //TODO handle situation where no id where given.
//    https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/PUT
//    Send status 201 or 200.
    @PutMapping("/{id}")
    public ResponseEntity putUpdate(
            @PathVariable Long id, @RequestBody Event updatedEvent, BindingResult bindingResult) {

//        TODO code repetition - put it into service
        if (!bindingResult.hasErrors()) {

            Event event;
            try {
                event = eventRepository.getOne(id);

//                TODO handle situation with attractions. I think that we should delete then if there where no provided
//                TODO or replace existing ones.

                updatedEvent.setId(event.getId());
                eventRepository.save(updatedEvent);
                return new ResponseEntity<>(
                        new Message(MessageType.INFO, "You have successfully updated event with id=" + id),
                        HttpStatus.BAD_REQUEST
                );

            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(
                        new Message(MessageType.ERROR, "There is no event with id=" + id), HttpStatus.BAD_REQUEST
                );
            }

        }

        return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);
    }

    //    TODO return updated entity and handle
    //  http://www.baeldung.com/http-put-patch-difference-spring
    //    {
    //    "timestamp": "2018-06-12T12:23:35.191+0000",
    //    "status": 500,
    //    "error": "Internal Server Error",
    //    "message": "Type definition error: [simple type, class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer]; nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.example.demo.domain.Event_$$_jvst33_3[\"handler\"])",
    //    "path": "/events/1/"
    //}
    @PatchMapping("/{id}")
    public ResponseEntity patchUpdate(@RequestBody Map<String, Object> updates, @PathVariable Long id) {

        Event event = eventService.update(id, updates);
        return new ResponseEntity<>("You have successfully updated resource.", HttpStatus.OK);

    }

}
