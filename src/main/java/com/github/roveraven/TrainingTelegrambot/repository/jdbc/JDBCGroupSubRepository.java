package com.github.roveraven.TrainingTelegrambot.repository.jdbc;

import com.github.roveraven.TrainingTelegrambot.repository.GroupSubRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("jdbc")
public class JDBCGroupSubRepository implements GroupSubRepository {
    private final String findGroupSubWithUsers = """
            SELECT id, title, last_post_id, user_id, active 
            FROM group_sub LEFT JOIN group_x_user ON id = group_sub_id 
            LEFT JOIN tg_user ON user_id = chat_id
            WHERE id =?;""";

    private final String findOnlyIds = """
                    SELECT id, user_id 
                    FROM group_sub LEFT JOIN group_x_user ON id = group_sub_id
                    WHERE id = %d
                    """;
    private final String insertNewGroupSub = "INSERT group_sub (id, title, last_post_id) VALUES (?, ?, ?)";
    private final Connection connection;
    @Autowired
    public JDBCGroupSubRepository(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }

    public Optional<GroupSub> findById(Integer groupId){
        try {                                                           //create query for DB
            PreparedStatement statement = connection.prepareStatement(findGroupSubWithUsers);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) {                                     //if no GroupSub found - return empty Optional
                statement.close();
                return Optional.empty();
            } else {                                                    //else - create new GroupSub
                GroupSub groupSub = new GroupSub();
                groupSub.setId(resultSet.getInt(1));
                groupSub.setTitle(resultSet.getString(2));
                groupSub.setLastPostId(resultSet.getInt(3));
                List<TelegramUser> users = new ArrayList<>();
                TelegramUser firstUser = new TelegramUser();
                if(resultSet.getInt(4)!=0) {                //check user for null id
                    firstUser.setChatId(resultSet.getLong(4));
                    firstUser.setActive(resultSet.getBoolean(5));
                    users.add(firstUser);
                }
                while (resultSet.next()) {                             //complement list of users
                    TelegramUser otherUser = new TelegramUser();
                    otherUser.setChatId(resultSet.getLong(4));
                    otherUser.setActive(resultSet.getBoolean(5));
                    users.add(otherUser);
                }
                groupSub.setUsers(users);
                statement.close();
                return  Optional.of(groupSub);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @SneakyThrows
    public GroupSub save(GroupSub groupSub) {
        Savepoint save = null;
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(String.format(findOnlyIds, groupSub.getId()));
            save = connection.setSavepoint();
            Set<Long> ids = groupSub.getUsers().stream().map(TelegramUser::getChatId).collect(Collectors.toSet());
            if (!resultSet.next()) {                                    //if no GroupSub with this id in DB - insert new
                PreparedStatement addNewGroupSub = connection.prepareStatement(insertNewGroupSub);
                addNewGroupSub.setInt(1, groupSub.getId());
                addNewGroupSub.setString(2, groupSub.getTitle());
                addNewGroupSub.setInt(3, groupSub.getLastPostId());

                addNewGroupSub.executeUpdate();
                addNewGroupSub.close();
            }                                                     // refresh usersList
                resultSet.beforeFirst();
                Set<Long> savedIds = new HashSet<>();
                while (resultSet.next()) {                              //get list of userId from DB
                    savedIds.add(resultSet.getLong(2));
                }
                for (Long id : ids) {
                    if (!savedIds.contains(id))                         //if usersList in DB don't contain current id - write it in DB
                        statement.execute(String.format("INSERT group_x_user(group_sub_id, user_id) VALUES (%d, %d)", groupSub.getId(), id.longValue()));
                }
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            connection.rollback(save);
            throw new RuntimeException(e);
        }
        return findById(groupSub.getId()).get();
    }

    public List<GroupSub> findAll() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT id, title, last_post_id, user_id, active
                    FROM group_sub LEFT JOIN group_x_user ON id = group_sub_id
                    JOIN tg_user ON user_id = chat_id
                    ORDER BY 1, 4;
                    """);
            List<GroupSub> groupSubs = new ArrayList<>();
            if(!resultSet.next()){                                      //if there are no GroupSubs in DB - return empty list
                statement.close();
                return groupSubs;
            } else {                                                    //if resultSet not empty - create first GroupSub with user & add to result list
                GroupSub firstGroupSub = new GroupSub();
                firstGroupSub.setId(resultSet.getInt(1));
                firstGroupSub.setTitle(resultSet.getString(2));
                firstGroupSub.setLastPostId(resultSet.getInt(3));
                List<TelegramUser> users = new ArrayList<>();
                TelegramUser firstUser = new TelegramUser();
                firstUser.setChatId(resultSet.getLong(4));
                firstUser.setActive(resultSet.getBoolean(5));
                users.add(firstUser);
                firstGroupSub.setUsers(users);
                groupSubs.add(firstGroupSub);
                while (resultSet.next()) {                              //if resultSet has another rows - process them
                    if(resultSet.getInt(1)==groupSubs.get(groupSubs.size()-1).getId()){  //check existence of GroupSub with this id in list
                        TelegramUser anotherUser = new TelegramUser();                                    //if true - add new user to it's list
                        anotherUser.setChatId(resultSet.getLong(4));
                        anotherUser.setActive(resultSet.getBoolean(5));
                        groupSubs.get(groupSubs.size()-1).getUsers().add(anotherUser);
                    } else {                                                                        //else  - create new GroupSub and add it to List
                        GroupSub anotherGroupSub = new GroupSub();
                        anotherGroupSub.setId(resultSet.getInt(1));
                        anotherGroupSub.setTitle(resultSet.getString(2));
                        anotherGroupSub.setLastPostId(resultSet.getInt(3));
                        List<TelegramUser> anotherUsers = new ArrayList<>();
                        TelegramUser anotherUser = new TelegramUser();
                        anotherUser.setChatId(resultSet.getLong(4));
                        anotherUser.setActive(resultSet.getBoolean(5));
                        anotherUsers.add(anotherUser);
                        anotherGroupSub.setUsers(anotherUsers);
                        groupSubs.add(anotherGroupSub);
                    }
                }
                statement.close();
                return groupSubs;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
