package com.example.socialtpygui.service.entityservice;

import com.example.socialtpygui.domain.Post;
import com.example.socialtpygui.repository.db.PostDb;
import com.example.socialtpygui.service.validators.NonExistingException;

import java.util.List;

public class PostService {
    PostDb postDb;

    public PostService(PostDb postDb){
        this.postDb = postDb;
    }

    /**
     * Find one post with id "id"
     * @param id Integer
     * @return if the post exist, return the post, else return null
     */
    public Post findOne(Integer id) {
        return postDb.findOne(id);
    }

    /**
     * @return all posts.
     */
    public Iterable<Post> findAll(int pageSize) {
        return postDb.findAll(pageSize);
    }

    /**
     * Save a post.
     * @param post Post
     * @return post, if was saved, null otherwise
     */
    public Post save(Post post){
        return postDb.save(post);
    }

    /**
     * Remove a post
     * @param id Integer
     * @return null
     * @throws NonExistingException if the post with id "id" does not exist
     */
    public Post remove(Integer id) {
        if (postDb.findOne(id) == null){
            throw new NonExistingException("Post with id " + id + " does not exist!");
        }
        return postDb.remove(id);
    }

    /**
     * @return number of posts
     */
    public int size() {
        return postDb.size();
    }

    /**
     * Like a post, add in like_post table
     * @param idPost Integer
     * @param email String
     * @throws NonExistingException if the post with id "idPost" does not exist
     */
    public void likeAPost(int idPost, String email){
        if (postDb.findOne(idPost) == null) {
            throw new NonExistingException("Post with id " + idPost + " does not exist!");
        }
        postDb.likeAPost(idPost, email);
    }

    /**
     * Take the like back, remove from like_post table
     * @param idPost Integer
     * @param email String
     * @throws NonExistingException if the post with id "idPost" does not exist
     */
    public void unlikeAPost(int idPost, String email){
            if (postDb.findOne(idPost) == null) {
                throw new NonExistingException("Post with id " + idPost + " does not exist!");
            }
        postDb.unlikeAPost(idPost, email);
    }

    /**
     * @param idPost Integer
     * @param email String
     * @return true, if the user with email "email" like the post with id "idPost", false otherwise
     * @throws NonExistingException if the post with id "idPost" does not exist
     */
    public boolean isPostLike(int idPost, String email){
        if (postDb.findOne(idPost) == null) {
            throw new NonExistingException("Post with id " + idPost + " does not exist!");
        }
        return postDb.isPostLike(idPost, email);
    }

    /**
     * @param email String
     * @return all posts from the friends of user with email "email and his/her posts"
     */
    public List<Post> getAllPostFromFriends(String email,int pageId){
        return postDb.getAllPostFromFriends(email,pageId);
    }


}
