package com.example.demo.domain;


import com.example.demo.domain.enums.EventType;
import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Check(constraints = "type IN ('TRADE_SHOW', 'BUSINESS_DINNER')")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Event {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @NotNull
    @Length(max = 15)
    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @NotNull
    private Integer seats;

    public Event(){

    }

    public Event(@NotBlank String name, EventType eventType, Integer seats) {
        this.name = name;
        this.eventType = eventType;
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
