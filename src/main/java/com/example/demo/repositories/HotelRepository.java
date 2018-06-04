package com.example.demo.repositories;

import com.example.demo.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    public List<Hotel> findHotelByStarsNumber(int star);
    public List<Hotel> findHotelByStarsNumberBetween(int star1, int star2);

    public List<Hotel> findHotelByName(String name);


}
