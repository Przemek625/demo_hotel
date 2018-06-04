package com.example.demo.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "Hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @NotBlank
    private String address;

    @Range(min = 0, max = 5)
    private int starsNumber;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Valid
    private List<Reservation> reservationList = new ArrayList<>();

    public Hotel() {
    }

    public Hotel(String name, String address, int starsNumber, ArrayList<Reservation> reservationList) {
        this.name = name;
        this.address = address;
        this.starsNumber = starsNumber;
        this.reservationList = reservationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStarsNumber() {
        return starsNumber;
    }

    public void setStarsNumber(int starsNumber) {
        this.starsNumber = starsNumber;
    }

    public Long getId() {
        return id;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public String toString() {
        return String.format(
                "Hotel[id=%d, name='%s', address='%s', starts=%d]\"", id, name, address, starsNumber
        );
    }

    public void addReservation(Reservation reservation){
        reservationList.add(reservation);
        reservation.setHotel(this);
    }
}
