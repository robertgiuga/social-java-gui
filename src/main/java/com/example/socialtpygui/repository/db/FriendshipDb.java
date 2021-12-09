package com.example.socialtpygui.repository.db;

import com.example.socialtpygui.domain.Friendship;
import com.example.socialtpygui.domain.Tuple;
import com.example.socialtpygui.domain.TupleOne;
import com.example.socialtpygui.repository.Repository;
import com.example.socialtpygui.service.validators.ValidationException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipDb implements Repository<TupleOne<String>, Friendship> {
    String url,username,password;

    public FriendshipDb(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship findOne(TupleOne<String> stringTupleOne) {
        Friendship friendship = null;
        String sql = "select * from friendship where email1= ? and email2= ? or email1= ? and email2= ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,stringTupleOne.getLeft());
            statement.setString(2,stringTupleOne .getRight());
            statement.setString(3,stringTupleOne.getRight());
            statement.setString(4,stringTupleOne .getLeft());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                friendship= new Friendship(stringTupleOne.getLeft(),stringTupleOne.getRight(), LocalDate.parse(resultSet.getString("date")));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return friendship;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships= new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String email1 = resultSet.getString("email1");
                String email2 = resultSet.getString("email2");
                String date = resultSet.getString("date");

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

        String sql = "insert into friendship (email1, email2, date ) values (?, ?, ?)";

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
    public Friendship remove(TupleOne<String> stringTupleOne) {
        Friendship toremovefriendship=findOne(stringTupleOne);

        String sql = "delete from friendship where email1 = ? and email2=? or email1 = ? and email2=? ";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,stringTupleOne.getLeft());
            ps.setString(2,stringTupleOne.getRight());
            ps.setString(3,stringTupleOne.getRight());
            ps.setString(4,stringTupleOne.getLeft());

            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return toremovefriendship;
    }

    @Override
    public int size() {
        int size=0;
        String sql = "select COUNT(*) from friendship";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if(resultSet.next())
                size=resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return size;
    }

    /**
     * removes the friendships for a specific id
     * @param email .
     */
    public void removeFriendships(String email){
        String sql = "delete from friendship where email1 = ? or email2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,email);
            ps.setString(2,email);

            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param email the email of the user to search for his friends for
     * @return  the list of the email of hid friends
     */
    public List<Tuple<String, LocalDate>> getFriends(String email){
        List<Tuple<String, LocalDate>> friends=new ArrayList<>();
        String sql = "select * from friendship where email1=? or email2=?";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,email);
            ps.setString(2,email);
            ResultSet resultSet = ps.executeQuery();


            while (resultSet.next()){
                if(resultSet.getString("email1").equals(email))
                    friends.add(new Tuple<>(resultSet.getString("email2"),LocalDate.parse(resultSet.getString("date"))));
                else
                    friends.add(new Tuple<>(resultSet.getString("email1"),LocalDate.parse(resultSet.getString("date"))));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return friends;
    }

    /**
     * @param email1
     * @param email2
     * @return null if the friendship doesn t exist, and Date when the friendship was created if it exists
     */
    public Date friendshipDate(String email1, String email2)
    {
        String sql = "select date from friendship where email1 = ? and email2 = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email1);
            preparedStatement.setString(2, email2);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDate("date");
        }
        catch (SQLException e) {
            return null;
        }
    }
}
