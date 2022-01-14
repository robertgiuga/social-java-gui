package com.example.socialtpygui.tests.RepositoryTest.RepoDBTest;

import com.example.socialtpygui.domain.Post;
import com.example.socialtpygui.repository.db.PostDb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostDBTest {
    private static final PostDb postDb = new PostDb("jdbc:postgresql://localhost:5432/SocialNetworkTest", "postgres", "postgres", 10);
    private PostDBTest(){}

    /**
     * Test all methods.
     */
    public static void runTests()
    {
        testFindOne();
        testFindAll();
        testSaveRemove();
        testisPostLike();
        testLikeUnlikeAPost();
        testGetAllPostFromFriends();
    }

    private static void testFindOne()
    {
        Post post = postDb.findOne(1);
        assert post.getEmailUser().equals("snj@gmail.com");
        assert post.getDescription().equals("Azi");
        assert post.getDate().toString().equals("2021-12-12");
        assert post.getId() == 1;
    }

    private static void testFindAll()
    {
        List<Integer> listId = new ArrayList<>();
        postDb.findAll(0).forEach(post -> {listId.add(post.getId());});
        assert listId.size() == 3;
        assert listId.contains(1);
        assert listId.contains(2);
        assert listId.contains(3);
    }

    private static void testSaveRemove()
    {
        List<Integer> listId = new ArrayList<>();
        postDb.findAll(0).forEach(post -> {listId.add(post.getId());});
        assert listId.size() == 3;
        Post newPost = new Post("descriere" ,"gg@gmail.com", LocalDate.parse("2021-09-09"));
        Post postt = postDb.save(newPost);
        listId.clear();
        postDb.findAll(0).forEach(post -> {listId.add(post.getId());});
        assert postDb.size() == 4;
        assert postDb.findOne(postt.getId()) != null;
        postDb.remove(postt.getId());
        listId.clear();
        postDb.findAll(0).forEach(post -> {listId.add(post.getId());});
        assert postDb.size() == 3;
    }

    private static void testisPostLike()
    {
        assert postDb.isPostLike(2, "snj@gmail.com" );
        assert ! postDb.isPostLike(1, "snj@gmail.com" );
    }

    private static void testLikeUnlikeAPost()
    {
        assert ! postDb.isPostLike(1, "snj@gmail.com");
        postDb.likeAPost(1, "snj@gmail.com");
        assert postDb.isPostLike(1, "snj@gmail.com");
        postDb.unlikeAPost(1, "snj@gmail.com");
        assert ! postDb.isPostLike(1, "snj@gmail.com");
    }

    private static void testGetAllPostFromFriends(){
        List<Integer> list = new ArrayList<>();
        postDb.getAllPostFromFriends("snj@gmail.com",0).forEach(post -> {list.add(post.getId());});
        assert list.size() == 2;
        assert list.contains(1);
        assert list.contains(2);
    }

}
