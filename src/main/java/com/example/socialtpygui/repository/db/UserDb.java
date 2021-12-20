package com.example.socialtpygui.repository.db;

import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.repository.Repository;
import com.example.socialtpygui.service.validators.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDb implements Repository<String,User> {
    String url,username,password;

    public UserDb(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public User findOne(String s) {
        User user = null;
        String sql = "select * from users where email= ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1,s);
            ResultSet resultSet= statement.executeQuery();
            while (resultSet.next())
                user = new User(resultSet.getString("first_name")
                        ,resultSet.getString("last_name")
                        ,s,resultSet.getString("password"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(firstName,lastName,email,password);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    @Override
    public User save(User entity) {
        if (entity==null)
            throw new ValidationException("Entity must not be null");

        User a = this.findOne(entity.getId());
        if(a != null) return null;

        String sql = "insert into users (email,first_name, last_name,password ) values (?, ?, ?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3,entity.getLastName());
            ps.setString(4,entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public User remove(String s) {
        User toremove = findOne(s);

        String sql = "delete from users where email = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,s);

            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return toremove;
    }

    @Override
    public int size() {
        int size=0;
        String sql = "select COUNT(*) from users";
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
     * Return a list with UserDto, where first_name and last_name contain string1 or string2 or reverse.
     * @param string1
     * @param string2
     * @throws SQLException
     */
    public List<UserDTO> getUsersByName(String string1, String string2){
        List<UserDTO> listReturn = new ArrayList<>();
        String s1 = "%" + string1 + "%"; String s2 = "%" + string2 + "%";
        String sql = "select * from users where (first_name ilike ? and last_name ilike ?) or (first_name ilike ? and last_name ilike ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, s1); preparedStatement.setString(2, s2);
            preparedStatement.setString(3, s2); preparedStatement.setString(4, s1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(firstName, lastName, email, password);
                UserDTO userDTO = new UserDTO(user);
                listReturn.add(userDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listReturn;
    }

}
