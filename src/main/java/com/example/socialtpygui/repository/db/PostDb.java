package com.example.socialtpygui.repository.db;

import com.example.socialtpygui.domain.Post;
import com.example.socialtpygui.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostDb implements Repository<Integer, Post> {
    private String url,username,password;
    private int pageSize;

    public PostDb(String url, String username, String password, int pageSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.pageSize = pageSize;
    }

    @Override
    public Post findOne(Integer id) {
        String sql = "select * from post where id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Post post = new Post(resultSet.getString("text"), resultSet.getString("user_from"), LocalDate.parse(resultSet.getDate("date").toString()));
            post.setId(resultSet.getInt("id"));
            return post;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Post> findAll(int pageSize) {
        String sql = "select * from post";
        List<Post> set = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Post post = new Post(resultSet.getString("text"), resultSet.getString("user_from"), LocalDate.parse(resultSet.getDate("date").toString()));
                post.setId(resultSet.getInt("id"));
                set.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return set;
    }

    @Override
    public Post save(Post post){
        String sql = "insert into post (text, user_from, date) values (?, ?, ?) returning id";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, post.getDescription());
            preparedStatement.setString(2, post.getEmailUser());
            preparedStatement.setDate(3, Date.valueOf(post.getDate()));
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int idd = resultSet.getInt("id");
            post.setId(idd);
            return post;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Post remove(Integer id) {
        String sql = "delete from post where id = ?";
        String sql1 = "delete from like_post where id_post = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1)) {
            preparedStatement.setInt(1, id);
            preparedStatement1.setInt(1, id);
            preparedStatement1.executeUpdate();
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int size() {
        int size = 0;
        String sql = "select count(*) from post";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            size = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * Like a post, add in like_post table
     * @param idPost Integer
     * @param email String
     */
    public void likeAPost(int idPost, String email){
        String sql = "insert into like_post(id_post, email) values (?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPost);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Take the like back, remove from like_post table
     * @param idPost Integer
     * @param email String
     */
    public void unlikeAPost(int idPost, String email){
        String sql = "delete from like_post where id_post = ?  and email = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPost);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param idPost Integer
     * @param email String
     * @return true, if the user with email "email" like the post with id "idPost", false otherwise
     */
    public boolean isPostLike(int idPost, String email){
        String sql = "select count(*) from like_post where email = ? and id_post = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, idPost);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count == 1) { return true; }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param email String
     * @return all posts from the friends of user with email "email and his/her posts"
     */
    public List<Post> getAllPostFromFriends(String email,int pageID){
        List<Post> list = new ArrayList<>();
        String sql = "select distinct post.id, post.user_from, post.text, post.date from post inner join friendship on " +
                "(((friendship.email1 = ?) and (friendship.email2 = post.user_from)) or ((friendship.email1 = " +
                "post.user_from) and (friendship.email2 = ?)) or (post.user_from = ?)) order by date desc offset ? limit ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4,pageSize*pageID);
            preparedStatement.setInt(5,pageSize);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Post post = new Post(resultSet.getString("text"), resultSet.getString("user_from"), LocalDate.parse(resultSet.getDate("date").toString()));
                post.setId(resultSet.getInt("id"));
                list.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param idPost Integer
     * @return number of like from a post
     */
    public int numberOfLikes(int idPost) {
        String sql = "select count(*) from like_post where id_post = ?";
        int count = 0;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPost);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
