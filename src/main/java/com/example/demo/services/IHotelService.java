package com.example.demo.services;

import com.example.demo.domain.Hotel;

public interface IHotelService {

    public void save(Hotel hotel) throws HotelUniqueConstraintException;
}
