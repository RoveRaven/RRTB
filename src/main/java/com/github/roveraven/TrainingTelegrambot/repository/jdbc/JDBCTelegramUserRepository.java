package com.github.roveraven.TrainingTelegrambot.repository.jdbc;

import com.github.roveraven.TrainingTelegrambot.repository.TelegramUserRepository;
import com.github.roveraven.TrainingTelegrambot.repository.entity.GroupSub;
import com.github.roveraven.TrainingTelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JDBCTelegramUserRepository implements TelegramUserRepository {
    private final String findUserQuery = "SELECT * FROM tg_user WHERE chat_id = ?;";
    private final String insertUserQuery = "INSERT tg_user(chat_id, active) VALUES (?, ?);";
    private final String updateUserQuery = "UPDATE tg_user SET chat_id = ?, active = ? WHERE chat_id = ?;";
    private final String getGroupSubsQuery = """
            SELECT group_sub_id, title, last_post_id 
            FROM group_x_user JOIN group_sub ON group_sub.id = group_x_user.group_sub_id  
            WHERE user_id = ?""";
    private final String getUsersWithGroupsQuery = """
            SELECT chat_id, active, group_sub_id, title, last_post_id 
            FROM tg_user JOIN group_x_user ON chat_id=user_id JOIN group_sub ON group_sub.id = group_x_user.group_sub_id 
            ORDER BY chat_id""";
    private final Connection connection;
    @Autowired
    public JDBCTelegramUserRepository(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }


    public TelegramUser save(TelegramUser telegramUser) {
        Long chatId = telegramUser.getChatId();
        try {                                                       //check for existing user with this id in DB
            PreparedStatement findStatement = connection.prepareStatement(findUserQuery);
            findStatement.setLong(1, chatId);
            ResultSet userSaved = findStatement.executeQuery();
            if (!userSaved.next()) {                                //user not found in DB, insert new value in table
                PreparedStatement insertStatement = connection.prepareStatement(insertUserQuery);
                insertStatement.setLong(1, chatId);
                insertStatement.setBoolean(2, telegramUser.isActive());
                insertStatement.executeUpdate();
                insertStatement.close();
            } else {                                                //user found, then update activity
                PreparedStatement updateStatement = connection.prepareStatement(updateUserQuery);
                updateStatement.setLong(1, chatId);
                updateStatement.setBoolean(2, telegramUser.isActive());
                updateStatement.setLong(3, chatId);
                updateStatement.executeUpdate();
                updateStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(telegramUser.getChatId()).get();
    }

        public Optional<TelegramUser> findById(Long id) {
        try {                                                       //check for existing user with this id in DB
            PreparedStatement findStatement = connection.prepareStatement(findUserQuery);
            findStatement.setLong(1, id);
            ResultSet resultSet = findStatement.executeQuery();
            if (!resultSet.next()) {                                //user not found, return empty Optional;
                findStatement.close();
                return Optional.empty();
            } else {                                                //user found, getting necessary  data from DB
                PreparedStatement getGroupSubsStatement = connection.prepareStatement(getGroupSubsQuery);
                getGroupSubsStatement.setLong(1, id);
                ResultSet groupSubsResultQuery = getGroupSubsStatement.executeQuery();
                List<GroupSub> groupSubList = new ArrayList<>();
                while (groupSubsResultQuery.next()) {               //fill groupSubs List of user
                    GroupSub groupSub = new GroupSub();
                    groupSub.setId(groupSubsResultQuery.getInt(1));
                    groupSub.setTitle(groupSubsResultQuery.getString(2));
                    groupSub.setLastPostId(groupSubsResultQuery.getInt(3));
                    groupSubList.add(groupSub);
                }
                TelegramUser user = new TelegramUser();             //prepare and return user
                user.setActive(resultSet.getBoolean(2));
                user.setChatId(resultSet.getLong(1));
                user.setGroupSubs(groupSubList);
                getGroupSubsStatement.close();
                return Optional.of(user);
            }
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
    }

    public List<TelegramUser> findAllByActiveTrue() {
        return findAllUsersDependOnActive(true);
    }
    public List<TelegramUser> findAllByActiveFalse() {
        return findAllUsersDependOnActive(false);
    }

    public List<TelegramUser> findAllUsersDependOnActive(Boolean isActive) {
        try {                                                                   //request all users with GroupSubs from DB
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet usersWithGroupsFromDB = statement.executeQuery(getUsersWithGroupsQuery);
            List<TelegramUser> usersList = new ArrayList<>();
            if (!usersWithGroupsFromDB.next()) {                                //if no users returned - return empty List
                statement.close();
                return usersList;
            } else {                                                            //prepare List of users
                usersWithGroupsFromDB.beforeFirst();
                while (usersWithGroupsFromDB.next()) {
                    if (usersWithGroupsFromDB.getBoolean(2)&& isActive) {   //filter users by active status
                        TelegramUser telegramUser;
                        if (usersList.isEmpty() || usersList.get(usersList.size() - 1).getChatId() != usersWithGroupsFromDB.getLong(1)) {
                            telegramUser = new TelegramUser();                                                  //if usersList don't contain current user -
                            telegramUser.setChatId(usersWithGroupsFromDB.getLong(1));                // create new user and add him to list
                            telegramUser.setActive(usersWithGroupsFromDB.getBoolean(2));             //
                            List<GroupSub> groupSubs = new ArrayList<>();
                            if (!(usersWithGroupsFromDB.getObject(3, Integer.class)==null)) {
                                GroupSub groupSub = new GroupSub();
                                groupSub.setId(usersWithGroupsFromDB.getInt(3));
                                groupSub.setTitle(usersWithGroupsFromDB.getString("title"));
                                groupSub.setLastPostId(usersWithGroupsFromDB.getInt(5));
                                groupSubs.add(groupSub);
                            }
                            telegramUser.setGroupSubs(groupSubs);
                            usersList.add(telegramUser);
                        } else {                                                                                //if 'usersList' contain this user  -
                            List<GroupSub> groupSubs = usersList.get(usersList.size() - 1).getGroupSubs();      //complement his GroupSubs List
                            GroupSub groupSub = new GroupSub();
                            groupSub.setId(usersWithGroupsFromDB.getInt(3));
                            groupSubs.add(groupSub);
                        }
                    }
                }
            }
            statement.close();
            return usersList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


