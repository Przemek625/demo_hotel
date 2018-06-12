package com.example.demo.services;

import com.example.demo.domain.Event;
import com.example.demo.repositories.EventRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Service
public class EventService{

    @Autowired
    private EventRepository eventRepository;

    public Event update(Long id, Map<String, Object> updates){

        Event event = eventRepository.getOne(id);

//        http://commons.apache.org/proper/commons-beanutils/
//        https://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
//        https://kobietydokodu.pl/projekt-bilet-3-konfigurujemy-spring-security-oraz-oauth/
//        https://stackoverflow.com/questions/18044978/java-get-property-value-by-property-name
        for (Map.Entry<String, Object> property: updates.entrySet()) {

            try {
                BeanUtils.setProperty(event, property.getKey(), property.getValue());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        System.out.println(event.toString());
        
        eventRepository.save(event);

        return event;


    }


}
