package com.example.demo.services;

import com.example.demo.domain.Hotel;
import com.example.demo.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HotelService implements IHotelService{

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public void save(Hotel hotel) throws HotelUniqueConstraintException {

        List<Hotel> hotelList = hotelRepository.findHotelByName(hotel.getName());
        if (!hotelList.isEmpty()){
            throw new HotelUniqueConstraintException("Some message");
        }
        else {
            hotelRepository.save(hotel);
        }
    }
}
