package com.example.demo.validators;

import com.example.demo.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListOfEntitiesValidator<E> {

    private Validator validator;
    private EventValidator eventValidator;

    @Autowired
    public ListOfEntitiesValidator(Validator validator, EventValidator eventValidator) {
        this.validator = validator;
        this.eventValidator = eventValidator;
    }

    public List<List<FieldError>> validate(List<E> target) {

        List<List<FieldError>> errorList = new ArrayList<>();

        for (E e : target) {
            WebDataBinder binder = new WebDataBinder(e);
            binder.setValidator((org.springframework.validation.Validator) this.validator);
            binder.addValidators(eventValidator);
            binder.validate();
            BindingResult bindingResult = binder.getBindingResult();
            errorList.add(bindingResult.getFieldErrors());
        }
        return errorList;
    }


    public boolean hasErrors(List<List<FieldError>> eList){
        for (List<FieldError> e: eList) {

            if (!e.isEmpty()){
                return true;
            }
            
        }
        return false;

    }

}
