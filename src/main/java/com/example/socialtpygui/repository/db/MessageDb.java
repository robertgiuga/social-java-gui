package com.example.socialtpygui.repository.db;


import com.example.socialtpygui.domain.*;
import com.example.socialtpygui.domain.MessageDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.repository.Repository;
import com.example.socialtpygui.service.validators.ValidationException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
public class MessageDb implements Repository<Integer, MessageDTO> {
    String url, username, password;

    public MessageDb(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public MessageDTO findOne(Integer id) {
        MessageDTO messageDTO = null;
        String sql = "select * from message where id = ?";
        String sqlQueryMakeList = "select email from message_recipient where message = ?";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sqlQueryMakeList))
        {
            preparedStatement1.setInt(1, id);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            List<String> listEmails = new ArrayList<>();
            while (resultSet1.next())
            {
                listEmails.add(resultSet1.getString("email"));
            }
            preparedStatement2.setInt(1, id);
            ResultSet resultSet= preparedStatement2.executeQuery();
            while (resultSet.next())
            {
                messageDTO = new MessageDTO(resultSet.getString("ms_from"), listEmails, resultSet.getString("text"), LocalDate.parse(resultSet.getString("date")));
                messageDTO.setId(resultSet.getInt("id"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messageDTO;
    }


    @Override
    public Iterable<MessageDTO> findAll()
    {
        return null;
    }

    @Override
    public MessageDTO save(MessageDTO entity) {
        if (entity==null)
            throw new ValidationException("Entity must not be null");
        String sqlMessageTable = "insert into message (ms_from, text, date) values (?, ?, ?) returning id";
        String sqlMessageRecipientTable = "insert into message_recipient(message, email) values (?, ?)";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement statement1 = connection.prepareStatement(sqlMessageTable);
            PreparedStatement statement2 = connection.prepareStatement(sqlMessageRecipientTable))
        {
            statement1.setString(1, entity.getFrom());
            statement1.setString(2, entity.getMessage());
            statement1.setDate(3, Date.valueOf(entity.getData()));
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            statement2.setInt(1, id);
            entity.setId(id);
            for (String email : entity.getTo())
            {
                statement2.setString(2, email);
                statement2.executeUpdate();
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }


    /**
     * Save a reply message.
     * @param replyMessage .
     * @return null- if the given entity is saved
     * @throws ValidationException if the given entity is null.
     */
    public MessageDTO saveReplyMessage(ReplyMessage replyMessage)
    {
        if (replyMessage == null) throw new ValidationException("Entity must not be null");
        String sqlMessageTable = "insert into message (ms_from, text, date, reply_to) values (?, ?, ?, ?)";
        String sqlMessageRecipientTable = "insert into message_recipient(message, email) values (?, ?)";
        String sqlSelectLastRecordMessage = "SELECT id FROM message ORDER BY ID DESC LIMIT 1";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement statement1 = connection.prepareStatement(sqlMessageTable);
            PreparedStatement statement2 = connection.prepareStatement(sqlMessageRecipientTable);
            PreparedStatement statement3 = connection.prepareStatement(sqlSelectLastRecordMessage))
        {
            statement1.setString(1, replyMessage.getFrom());
            statement1.setString(2, replyMessage.getMessage());
            statement1.setDate(3, Date.valueOf(replyMessage.getData()));
            statement1.setInt(4, replyMessage.getOriginal().getId());
            statement1.executeUpdate();
            ResultSet resultSet = statement3.executeQuery();
            resultSet.next();
            statement2.setInt(1, resultSet.getInt("id"));
            replyMessage.setId(resultSet.getInt("id"));
            for (String email : replyMessage.getTo())
            {
                statement2.setString(2, email);
                statement2.executeUpdate();
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return replyMessage;
    }

    /**
     * @param emailUser1 .
     * @param emailUser2 .
     * @return a map representing all ids of messages between 2 users and a integer if the value is 0 the message is
     * a Message or a ReplayMessage otherwise, the second integer being the message witch it replayed to
     * @throws ValidationException if the given email1 is null or email2 is null.
     */
    public List<ReplyMessage> findAllMessageBetweenTwoUsers(String emailUser1, String emailUser2)
    {
        if (emailUser1 == null || emailUser2 == null) throw new ValidationException("Entity must not be null");
        List<ReplyMessage> resultList = new ArrayList<>();
        String sqlAllMessagesFromBothUsers = "select * from message where ms_from = ? or ms_from = ?";
        String sqlVerify = "select * from message_recipient where message = ? and (email = ? or email = ?)";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sqlAllMessagesFromBothUsers);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sqlVerify))
        {
            preparedStatement1.setString(1, emailUser1);
            preparedStatement1.setString(2, emailUser2);
            ResultSet resultSet = preparedStatement1.executeQuery();
            while (resultSet.next())
            {
                preparedStatement2.setInt(1, resultSet.getInt("id"));
                preparedStatement2.setString(2, emailUser1);
                preparedStatement2.setString(3, emailUser2);
                ResultSet resultSet1 = preparedStatement2.executeQuery();
                if (resultSet1.next())
                    resultList.add(new ReplyMessage(findOne(resultSet.getInt("id")), findOne(resultSet.getInt("reply_to"))));
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultList;
    }



    @Override
    public MessageDTO remove(Integer id)
    {
        String sql = "delete from message where id = ?";
        String sql1 = "delete from message_recipient where message = ?";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1)) {
            preparedStatement.setInt(1, id);
            preparedStatement1.setInt(1, id);
            preparedStatement1.executeUpdate();
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Set ms_from null when user is removed.
     * @param email .
     */
    public void removeFromUser(String email)
    {
        String sql = "UPDATE message SET ms_from = null WHERE ms_from = ?";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,email);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public int size() {
        int size = 0;
        String sqlCount = "select Count(*) from message";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCount))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                size = resultSet.getInt(1);

            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return size;
    }

    public List<String> getAllEmailsFromSendMessage(String email)
    {
        List<String> list = new ArrayList<>();
        String sqlAllIdMessage = "select id from message where ms_from = ?";
        String sqlAllEmails = "select email from message_recipient where message = ?";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlAllIdMessage);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sqlAllEmails)) {
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                preparedStatement1.setInt(1, id);
                ResultSet resultSetEmails = preparedStatement1.executeQuery();
                while (resultSetEmails.next())
                {
                    String emailSQLResult = resultSetEmails.getString("email");
                    if (!list.contains(emailSQLResult)) list.add(emailSQLResult);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public List<String> getAllEmailsFromReceiveEmails(String email)
    {
        List<String> list = new ArrayList<>();
        String sql = "select m.ms_from from message as m inner join message_recipient as mp on m.id = mp.message where mp.email = ?";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                String emailSQL = resultSet.getString("ms_from");
                if (!list.contains(emailSQL)) list.add(emailSQL);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    /**
     * @param email String
     * @return a list with GroupDTO, only the groups where the user with email "email" is in
     */
    public List<GroupDTO> getUserGroups(String email)
    {
        List<GroupDTO> returnList = new ArrayList<>();
        String sql1 = "select id, name from group_user inner join social_group on group_user.group_id = social_group.id where email = ?";
        String sql2 = "select email from group_user where group_id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql2)) {
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                int groupId = resultSet.getInt("id");
                List<String> membersEmails = new ArrayList<>();
                preparedStatement1.setInt(1, groupId);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()){
                    String memberEmail = resultSet1.getString("email");
                    membersEmails.add(memberEmail);
                }
                returnList.add(new GroupDTO(groupId, name, membersEmails));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * @param id Integer
     * @return a GroupDto which contain the group with id "id"
     */
    public GroupDTO getGroup(int id)
    {
        String sql = "select name, group_user.email from social_group inner join group_user on social_group.id = group_user.group_id where id = ?";
        List<String> membersEmails = new ArrayList<>();
        String name = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                name = resultSet.getString("name");
                String email = resultSet.getString("email");
                membersEmails.add(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new GroupDTO(id, name, membersEmails);
    }

    /**
     * Add a user to a specify group.
     * @param user User
     * @param groupId Integer
     * @return null, if the user was not added and the user, if the user was added
     */
    public User addUserToGroup(User user, int groupId)
    {
        String sql = "insert into group_user(group_id, email) values (?,?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, groupId);
            preparedStatement.setString(2, user.getId());
            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Remove a user from a groupe, remove from group_user table.
     * @param email String
     * @param groupId Integer
     */
    public void removeUserFromGroup(String email, int groupId)
    {
        String sqlLeavingUserFromGroup = "delete from group_user where group_id = ? and email = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlLeavingUserFromGroup)) {
            preparedStatement.setInt(1, groupId);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a group, add in table social_group and in table group_user.
     * @param group Group
     * @return null, if the group was not added and the group, if the group was added
     */
    public Group addGroup(Group group)
    {
        String sql = "insert into social_group(name) values (?) returning id";
        String sqlInsertInGroupUser = "insert into group_user(group_id, email) values (?, ?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sqlInsertInGroupUser)) {
            preparedStatement.setString(1, group.getNameGroup());
            ResultSet resultSet1 = preparedStatement.executeQuery();
            resultSet1.next();
            int id = resultSet1.getInt("id");
            group.setId(id);
            for (User user : group.getMembersList()){
                preparedStatement2.setInt(1, id);
                preparedStatement2.setString(2, user.getId());
                preparedStatement2.executeUpdate();
            }
            return group;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Remove a group, with a specify id. First remove all from message_recipient with group_id = "id"
     * ,then remove all messages was sent to this group, then remove all from group_user and ,finally, remove
     * the group from social_group
     * @param id Integer
     */
    public void removeGroup(int id){
        String sqlRemoveGroup = "delete from social_group where id = ?";
        String sqlRemoveGroupUser = "delete from group_user where group_id = ?";
        String sqlRemoveMessageAndMessageGroup = "with t1 as (delete from message_group where id_group = ? returning id_message) delete from message where id in (select distinct * from t1)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRemoveMessageAndMessageGroup);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sqlRemoveGroup);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sqlRemoveGroupUser)) {
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            preparedStatement1.setInt(1, id);
            preparedStatement2.setInt(1, id);
            preparedStatement2.executeUpdate();
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the number of groups
     */
    public int sizeGroup() {
        int size = 0;
        String sqlCount = "select Count(*) from social_group";
        try(Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCount))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                size = resultSet.getInt(1);

            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return size;
    }

    /**
     * insert into table message a new message of type MessageDTO
     * insert into table message_group row with id of new added message and id of group where is sent message
     * @param entity MessageDTO
     * @param idGroup int
     * @return MessageDTO entity(added message)
     */
    public MessageDTO saveGroupMessage(MessageDTO entity, int idGroup) {
        if (entity == null)
            throw new ValidationException("Entity must not be null");
        String sqlMessageTable = "insert into message (ms_from, text, date) values (?, ?, ?) returning id";
        String sqlMessageGroup = "insert into message_group(id_message, id_group)values (?, ?)";
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement1 = connection.prepareStatement(sqlMessageTable);
             PreparedStatement statement2 = connection.prepareStatement(sqlMessageGroup)) {
            statement1.setString(1, entity.getFrom());
            statement1.setString(2, entity.getMessage());
            statement1.setDate(3, Date.valueOf(entity.getData()));
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            statement2.setInt(1, id);
            statement2.setInt(2, idGroup);
            statement2.executeUpdate();
            entity.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    /**
     * @param groupId Integer
     * @return a list of replyMessage, it returns all the messages from a group
     * if ReplayMessage has currentMessage null that means it is a Message entity
     */
    public List<ReplyMessage> getGroupMessages(int groupId)
    {
        List<ReplyMessage> returnList = new ArrayList<>();
        String sql = "select distinct message.id, message.reply_to from message inner join message_group on message.id = message_group.id_message where message_group.id_group = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                returnList.add(new ReplyMessage(findOne(resultSet.getInt("id")), findOne(resultSet.getInt("reply_to"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnList;
    }
}

