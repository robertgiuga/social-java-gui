package com.example.socialtpygui.tests.RepositoryTest.RepoDBTest;


import com.example.socialtpygui.domain.Friendship;
import com.example.socialtpygui.domain.TupleOne;
import com.example.socialtpygui.repository.db.FriendshipRequestDb;
import com.example.socialtpygui.service.validators.ValidationException;

import java.time.LocalDate;
import java.util.stream.StreamSupport;

public class FriendshipRequestDBTest {

    private  static FriendshipRequestDb friendshipRequestDb = new FriendshipRequestDb("jdbc:postgresql://localhost:5432/SocialNetworkTest", "postgres", "postgres");

    private FriendshipRequestDBTest() {}

    public static void runTests(){
        testFindOne();
        testFindAll();
        testSaveRemoveSize();
    }

    private static void testFindOne(){
        assert (friendshipRequestDb.findOne(new TupleOne<String>("andr@gamail.com", "gg@gmail.com")) == null);
        Friendship friendship = friendshipRequestDb.findOne(new TupleOne<String>("andr@gamail.com", "snj@gmail.com"));
        Friendship friendship1 = friendshipRequestDb.findOne(new TupleOne<String>("snj@gmail.com", "andr@gamail.com"));
        assert (friendship.getId().equals(friendship1.getId()));
    }

    private static void testFindAll(){
        Iterable<Friendship> friendshipRequests = friendshipRequestDb.findAll();
        long size = StreamSupport.stream(friendshipRequests.spliterator(), false).count();
        assert (size == 3);
    }

    private static void testSaveRemoveSize(){
        try{
            friendshipRequestDb.save(null);
            assert false;
        }catch (ValidationException e){
            assert true;
        }
        friendshipRequestDb.save(new Friendship("gg@gmail.com", "jon1@yahoo.com", LocalDate.now()));
        assert (friendshipRequestDb.size() == 3);
        friendshipRequestDb.save(new Friendship("gg@gmail.com", "snj@gmail.com", LocalDate.now()));
        assert (friendshipRequestDb.size() == 4);
        friendshipRequestDb.remove(new TupleOne<String>("snj@gmail.com", "gg@gmail.com"));
        assert (friendshipRequestDb.size() == 3);
        assert (friendshipRequestDb.remove(new TupleOne<String>("user1@mail.com", "user1@mail.com"))==null);
    }
}
