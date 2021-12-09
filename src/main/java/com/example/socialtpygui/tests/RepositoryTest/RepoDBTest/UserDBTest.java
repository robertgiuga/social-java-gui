package com.example.socialtpygui.tests.RepositoryTest.RepoDBTest;


import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.repository.db.UserDb;
import com.example.socialtpygui.service.validators.ValidationException;
import java.util.ArrayList;
import java.util.List;

public class UserDBTest {
    private static UserDb userRepo = new UserDb("jdbc:postgresql://localhost:5432/SocialNetworkTest", "postgres", "postgres");


    private UserDBTest() {}

    /**
     * Test all methods.
     */
    public static void runTests()
    {
        testFindAll();
        testFindOne();
        testSize();
        testSaveRemove();
    }

    /**
     * Test findOne from UserDb.
     */
    private static void testFindOne()
    {
        User user1 = userRepo.findOne("gg@gmail.com");
        User correctUser = new User("Robert", "Giuga", "gg@gmail.com","parola5",false);
        assert(user1.equals(correctUser));
        User user2 = userRepo.findOne("cristiang@gmail.com");
        assert (user2 == null);
    }

    /**
     * Test findAll from UserDb.
     */
    private static void testFindAll()
    {
        assert userRepo.size() == 6;
        assert(userRepo.findOne("jon1@yahoo.com") != null);
        assert(userRepo.findOne("snj@gmail.com") != null);
        assert(userRepo.findOne("andr@gamail.com") != null);
        assert(userRepo.findOne("aand@hotmail.com") != null);
        assert(userRepo.findOne("gg@gmail.com") != null);
        assert(userRepo.findOne("gc@gmail.com") != null);
    }

    /**
     * Test save and remove from UserDb.
     */
    private static void testSaveRemove()
    {
        try{
            userRepo.save(null);
            assert false;
        }catch(ValidationException e){
            assert true;
        }
        userRepo.save(new User("Cristian", "Gulea", "gulea@ymail.com","parola6",false));
        List<User> result = new ArrayList<User>();
        userRepo.findAll().forEach(result::add);
        assert (result.size() == 7);
        assert (userRepo.findOne("gulea@ymail.com") != null);
        userRepo.remove("gulea@ymail.com");
        assert (userRepo.size() == 6);
        assert (userRepo.findOne("gulea@ymail.com") == null);


    }

    /**
     * Test size from UserDb.
     */
    private static void testSize()
    {
        assert (userRepo.size() == 6);
        userRepo.save(new User("Cristian", "Gulea", "gulea@ymail.com","parola6",false));
        assert (userRepo.size() == 7);
        userRepo.remove("gulea@ymail.com");
        assert (userRepo.size() == 6);


    }
}
