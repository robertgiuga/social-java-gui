package com.example.socialtpygui.tests.DomainTests;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.UserDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventTests {
    private EventTests(){};

    /**
     * run the particularly test of the class
     */
    public static void runTests(){
        testGet();
    }

    /**
     * test if the get and set methods are working well
     */
    private static void testGet(){
        List<UserDTO> list = new ArrayList<>();
        list.add(new UserDTO("gg@gmail.com", "gulea", "cristian"));
        EventDTO eventDTO = new EventDTO("party", LocalDate.parse("2020-02-02"), "Cluj", list,"Untold","gg@gmail.com");
        assert eventDTO.getDate().toString().equals("2020-02-02");
        assert eventDTO.getDescription().equals("party");
        assert eventDTO.getLocation().equals("Cluj");
        assert eventDTO.getName().equals("Untold");
        assert eventDTO.getParticipants().size() == 1;



    }
}
