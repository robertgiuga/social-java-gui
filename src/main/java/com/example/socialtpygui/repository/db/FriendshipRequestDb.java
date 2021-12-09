package com.example.socialtpygui.repository.db;


import com.example.socialtpygui.domain.Friendship;
import com.example.socialtpygui.domain.TupleOne;
import com.example.socialtpygui.repository.Repository;
import com.example.socialtpygui.service.validators.ValidationException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipRequestDb implements Repository<TupleOne<String>, Friendship> {
    String url, username, password;

    public FriendshipRequestDb(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship findOne(TupleOne<String> stringTupleOne) {
        Friendship friendship = null;
        String sql = "select * from friendship_request where email1=? and email2=? or email1=? and email2=?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, stringTupleOne.getLeft());
            statement.setString(2, stringTupleOne.getRight());
            statement.setString(3, stringTupleOne.getRight());
            statement.setString(4, stringTupleOne.getLeft());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                friendship = new Friendship(stringTupleOne.getLeft(), stringTupleOne.getRight(), LocalDate.parse(resultSet.getString("request_date")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return friendship;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships= new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship_request");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String email1 = resultSet.getString("email1");
                String email2 = resultSet.getString("email2");
                String date = resultSet.getString("request_date");

                Friendship friendship= new Friendship(email1,email2,LocalDate.parse(date));
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        if (entity==null)
            throw new ValidationException("Entity must not be null");

        Friendship friendship = this.findOne(entity.getId());
        if(friendship != null){
            return friendship;
        }

        String sql = "insert into friendship_request (email1, email2, request_date) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getId().getLeft());
            ps.setString(2, entity.getId().getRight());
            ps.setDate(3, Date.valueOf(entity.getDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Friendship remove(TupleOne<String> stringTuple) {
        Friendship toRemoveFriendship=findOne(stringTuple);

        String sql = "delete from friendship_request where email1 = ? and email2=? or email1 = ? and email2=? ";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,stringTuple.getLeft());
            ps.setString(2,stringTuple.getRight());
            ps.setString(3,stringTuple.getRight());
            ps.setString(4,stringTuple.getLeft());

            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return toRemoveFriendship;
    }

    @Override
    public int size() {
        int size = 0;
        String sql = "select COUNT(*) from friendship_request";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                size = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return size;
    }

    public Iterable<String> getRequests(String email){
        List<String> emails = new ArrayList<>();
        String sql = "select * from friendship_request where email1=? or email2=?";

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                if(resultSet.getString("email1").equals(email)){
                    emails.add(resultSet.getString("email2"));
                }
                else{
                    emails.add(resultSet.getString("email1"));
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return emails;
    }
}
