package com.example.demo.controllers;

import com.example.demo.repositories.EventRepository;
import com.example.demo.validators.ListOfEntitiesValidator;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.Event;
import java.util.List;

import org.slf4j.LoggerFactory;


@RestController()
@RequestMapping("/events/")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private EventRepository eventRepository;
    private ListOfEntitiesValidator<Event> listOfEventValidator;

    public EventController(EventRepository eventRepository, ListOfEntitiesValidator<Event> listOfEventValidator) {
        this.eventRepository = eventRepository;
        this.listOfEventValidator = listOfEventValidator;
    }


    @PostMapping()
    public ResponseEntity create(@RequestBody List<Event> eventList) {

        List<List<FieldError>> listOfErrors = listOfEventValidator.validate(eventList);

        if (!listOfEventValidator.hasErrors(listOfErrors)){
            eventRepository.saveAll(eventList);
            return new ResponseEntity<>(eventList, HttpStatus.OK);
        }
        return new ResponseEntity<>(listOfErrors, HttpStatus.BAD_REQUEST);

    }

    @GetMapping()
    public ResponseEntity get(){
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }
}
