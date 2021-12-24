package com.example.socialtpygui.tests.RepositoryTest.RepoDBTest;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.repository.db.EventDb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class EventDBTest {
    private static final EventDb eventDb = new EventDb("jdbc:postgresql://localhost:5432/SocialNetworkTest", "postgres", "postgres");
    private EventDBTest(){}

    /**
     * Test all methods.
     */
    public static void runTests()
    {
        testFindOne();
        testSaveRemove();
        testAddRemoveParticipants();
        testFindAll();
    }

    private static void testFindOne()
    {
        assert eventDb.findOne(1).getParticipants().size() == 4;
        assert eventDb.findOne(1).getName().equals("Untold");
        assert eventDb.findOne(1).getDescription().equals("Festival");
        assert eventDb.findOne(1).getLocation().equals("Cluj");
        assert eventDb.findOne(12) == null;

    }

    private static void testSaveRemove(){
        List<UserDTO> list = new ArrayList<>();
        list.add(new UserDTO("gc@gmail.com", "Cristian", "Gulea"));
        EventDTO eventDTO = new EventDTO("Muzica", LocalDate.parse("2021-09-09"), "Mures", list, "Concert");
        assert eventDb.size() == 2;
        eventDb.save(eventDTO);
        assert eventDb.size() == 3;
        int id = eventDTO.getId();
        assert eventDb.findOne(id) != null;
        eventDb.remove(id);
        assert eventDb.size() == 2;
        assert eventDb.findOne(id) == null;
    }

    private static void testAddRemoveParticipants()
    {
        List<String> list = new ArrayList<>();
        for (UserDTO userDTO : eventDb.findOne(1).getParticipants())
        {
            list.add(userDTO.getId());
        }
        assert  ! (list.contains("aand@hotmail.com"));
        eventDb.addParticipants(new User("s", "s","aand@hotmail.com", "p"), 1);
        list.clear();
        for (UserDTO userDTO : eventDb.findOne(1).getParticipants())
        {
            list.add(userDTO.getId());
        }
        assert   (list.contains("aand@hotmail.com"));
        eventDb.removeParticipants("aand@hotmail.com", 1);
        list.clear();
        for (UserDTO userDTO : eventDb.findOne(1).getParticipants())
        {
            list.add(userDTO.getId());
        }
        assert   ! (list.contains("aand@hotmail.com"));
    }

    private static void testFindAll(){
        Iterable<EventDTO> list1 = eventDb.findAll();
        List<EventDTO> list = new ArrayList<>();
        list1.forEach(list::add);
        assert list.size() == 2;
        List<Integer> idList = new ArrayList<>();
        idList.add(list.get(0).getId());
        idList.add(list.get(1).getId());
        assert idList.contains(1);
        assert idList.contains(2);
    }



}
