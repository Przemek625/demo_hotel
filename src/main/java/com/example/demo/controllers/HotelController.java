package com.example.demo.controllers;

import com.example.demo.domain.Hotel;
import com.example.demo.repositories.HotelRepository;
import com.example.demo.services.HotelService;
import com.example.demo.services.HotelUniqueConstraintException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.validation.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;


class Error {

    private String title;
    private String body;

    public Error(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}


class WrapList<E> implements List<E>{

    @Valid
    @NotEmpty
    private List<E> list = new ArrayList<>();


    public WrapList() {
    }

    public WrapList(@Valid List<E> hotelList) {
        this.list = hotelList;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }


    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }
}


@RestController()
@RequestMapping("/hotele")
public class HotelController {

    //    TODO do dupy ta metoda :D
    private HashMap<String, String> getDupaErrors(List<FieldError> errorList) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (FieldError e : errorList) {

            hashMap.put(e.getField(), e.getDefaultMessage());
        }
        return hashMap;
    }

    @Autowired
    private HotelService hotelService;

    @Autowired
    private Validator validator;

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
    private HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping({"/", ""})
    public List<Hotel> list() {
        return hotelRepository.findAll();
    }


    /**
     * Example json:
     * {
     * "name": "Grand hotel",
     * "address": "Warsaw",
     * "starsNumber": 2,
     * "reservationList": [
     * {
     * "startDate": "2018-03-20",
     * "endDate": "2018-03-20",
     * "reservedBy": "Przemek"
     * }
     * ]
     * }
     */
    @PostMapping("/")
    public ResponseEntity create(@Valid @RequestBody Hotel hotel, BindingResult bindingResult) {

        System.out.println(bindingResult.getClass().getName().toString());

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    getDupaErrors(bindingResult.getFieldErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }

        hotel.getReservationList().forEach(reservation -> reservation.setHotel(hotel));
//        try {
//            hotelRepository.save(hotel);
//        } catch (DataIntegrityViolationException e){
//            System.out.println("EXCEPTION HAS HAPPENED");
//            logger.error(e.getClass().getName());
//            return new ResponseEntity<>(
//                    new Error("error", e.getMessage()),
//                    HttpStatus.BAD_REQUEST
//            );
//
//        }

        try {
            hotelService.save(hotel);
        } catch (HotelUniqueConstraintException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new Error("blad", "juz taki hotel istnieje"),
                    HttpStatus.BAD_REQUEST
            );
        }


        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }


    /**
     * [
     * {
     * "name": "Grand daswwwwdsssknnnk",
     * "address": "Warsaw",
     * "starsNumber": 10000,
     * "reservationList": [
     * {
     * "startDate": "2018-03-20",
     * "endDate": "2018-03-20",
     * "reservedBy": "Przemek"
     * }
     * ]
     * },
     * {
     * "name": "Grand daswwwwdsssknnnk",
     * "address": "Warsaw",
     * "starsNumber": 1000,
     * "reservationList": [
     * {
     * "startDate": "2018-03-20",
     * "endDate": "2018-03-20",
     * "reservedBy": "Przemek"
     * }
     * ]
     * },
     * {
     * "name": "Grand daswwwwdsssknnnk",
     * "address": "Warsaw",
     * "starsNumber": 3,
     * "reservationList": [
     * {
     * "startDate": "2018-03-20",
     * "endDate": "2018-03-20",
     * "reservedBy": "Przemek"
     * }
     * ]
     * }
     * ]
     *
     * @param hotels
     * @return
     */
    @PostMapping("/create-many/")
    public ResponseEntity createMany(@RequestBody List<Hotel> hotels) {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        List<HashMap<String, String>> arrayList = new ArrayList<>();

        for (Hotel hotel : hotels) {

            HashMap<String, String> hm = new HashMap<>();
            arrayList.add(hm);
            Set<ConstraintViolation<Hotel>> constraintViolations = validator.validate(hotel);
            constraintViolations.iterator().forEachRemaining(
                    e -> {
                        hm.put(e.getPropertyPath().toString(), e.getMessage());
                    }
            );
        }

        return new ResponseEntity<>(arrayList, HttpStatus.CREATED);
    }


    @PostMapping("/create-many-test/")
    public ResponseEntity createManyTest(@Valid @RequestBody List<Hotel> wrapHotelList, BindingResult bindingResult) {

        System.out.println(wrapHotelList.toString());


        if (bindingResult.hasErrors()){

            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(wrapHotelList, HttpStatus.CREATED);
    }

    @PostMapping("/create-many-test2/")
    public ResponseEntity createManyTest2(@Valid @RequestBody List<@Valid Hotel> hotelList, BindingResult bindingResult) {

        DataBinder binder = new DataBinder(hotelList);
        binder.setValidator((org.springframework.validation.Validator) validator);

        binder.validate();

        BindingResult bindingResult1 = binder.getBindingResult();

        System.out.println(bindingResult.hasErrors());
        System.out.println(bindingResult.hasFieldErrors());
        System.out.println(bindingResult.getModel().toString());


        if (bindingResult1.hasErrors()){

            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(hotelList, HttpStatus.CREATED);
    }

    @DeleteMapping({"/", ""})
    public void deleteAll() {
        hotelRepository.deleteAll();
    }

}

//        hotel.getReservationList().forEach(reservation -> reservation.setHotel(hotel));
//        try {
//            hotelRepository.save(hotel);
//        }
//        catch ()
//        try {
//            hotelRepository.save(hotel);
//        } catch (DataIntegrityViolationException e){
//            System.out.println("EXCEPTION HAS HAPPENED");
//            logger.error(e.getClass().getName());
//            return new ResponseEntity<>(
//                    new Error("error", e.getMessage()),
//                    HttpStatus.BAD_REQUEST
//            );
//
//        }

