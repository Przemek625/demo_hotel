package com.example.demo.validators;

import com.example.demo.domain.Event;
import com.example.demo.repositories.EventRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class EventValidator implements Validator {

    private EventRepository eventRepository;

    public EventValidator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Event event = (Event) target;
        if (!eventRepository.findByName(event.getName()).isEmpty()){
            errors.rejectValue("name", "name must be unique across the table.");
        }

    }
}
