package com.example.socialtpygui.tests.RepositoryTest.RepoDBTest;


import com.example.socialtpygui.domain.Friendship;
import com.example.socialtpygui.domain.TupleOne;
import com.example.socialtpygui.repository.db.FriendshipDb;
import com.example.socialtpygui.service.validators.ValidationException;
import java.time.LocalDate;

public class FriendshipDBTest {

    private static final FriendshipDb friendshipRepo = new FriendshipDb("jdbc:postgresql://localhost:5432/SocialNetworkTest", "postgres", "postgres");

    private FriendshipDBTest() {}

    /**
     * Test all methods.
     */
    public static void runTests()
    {
        testFindOne();
        testFindAll();
        testSaveRemove();
        testSize();
        testRemoveFriendships();
    }

    /**
     * Test findOne method from FriendshipDb.
     */
    private static void testFindOne()
    {
        assert(friendshipRepo.findOne(new TupleOne<>("gg@gmail.ro", "jon1@yahoo.com")) == null );
        Friendship friendship = friendshipRepo.findOne(new TupleOne<>("gg@gmail.com", "jon1@yahoo.com"));
        Friendship friendship1 = friendshipRepo.findOne(new TupleOne<>("gg@gmail.com", "jon1@yahoo.com"));
        assert(friendship.getId().equals(friendship1.getId()));
    }

    /**
     * Test findAll method from FriendshipDb.
     */
    private static void testFindAll()
    {
        assert(friendshipRepo.size() == 3);
        assert(friendshipRepo.findOne(new TupleOne<>("gg@gmail.com", "jon1@yahoo.com")) != null);
        assert(friendshipRepo.findOne(new TupleOne<>("andr@gamail.com", "snj@gmail.com")) != null);
        assert(friendshipRepo.findOne(new TupleOne<>("andr@gamail.com", "aand@hotmail.com")) != null);
    }

    /**
     * Test save and remove from FriendshipDb.
     */
    private static void testSaveRemove()
    {
        try{
            friendshipRepo.save(null);
            assert false;
        }
        catch (ValidationException e)
        {
            assert true;
        }
        friendshipRepo.save(new Friendship("snj@gmail.com", "gg@gmail.com", LocalDate.now()));
        assert(friendshipRepo.size() == 4);
        assert(friendshipRepo.findOne(new TupleOne<>("snj@gmail.com", "gg@gmail.com")) != null);
        friendshipRepo.remove(new TupleOne<>("snj@gmail.com", "gg@gmail.com"));
        assert(friendshipRepo.size() == 3);
    }

    /**
     * Test size from FriendshipDb.
     */
    private static void testSize()
    {
        assert(friendshipRepo.size() == 3);
        friendshipRepo.save(new Friendship("snj@gmail.com", "gg@gmail.com", LocalDate.now()));
        assert(friendshipRepo.size() == 4);
        friendshipRepo.remove(new TupleOne<>("snj@gmail.com", "gg@gmail.com"));
        assert(friendshipRepo.size() == 3);
    }

    /**
     * Test removeFriendships from FriendshipDb.
     */
    private static void testRemoveFriendships()
    {
        assert(friendshipRepo.size() == 3);
        friendshipRepo.save(new Friendship("gc@gmail.com", "gg@gmail.com", LocalDate.now()));
        assert(friendshipRepo.size() == 4);
        assert (friendshipRepo.findOne(new TupleOne<>("gc@gmail.com", "gg@gmail.com")) != null);
        friendshipRepo.removeFriendships("gc@gmail.com");
        assert(friendshipRepo.size() == 3);
        assert (friendshipRepo.findOne(new TupleOne<>("gc@gmail.com", "gg@gmail.com")) == null);
    }
}
