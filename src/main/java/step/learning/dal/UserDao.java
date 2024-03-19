package step.learning.dal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entity.User;
import step.learning.services.db.DbService;
import step.learning.services.kdf.KdfService;

import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserDao {
    private final KdfService kdfService;
    private final DbService dbService;
    private final Logger logger;

    @Inject
    public UserDao(KdfService kdfService, DbService dbService, Logger logger) {
        this.kdfService = kdfService;
        this.dbService = dbService;
        this.logger = logger;
    }

    public boolean signupUser(String userName, String userPhone, String userPassword, String userEmail, String savedFilename) {
        String sql = "INSERT INTO Users(name, phone, salt, dk, email, avatar) VALUES(?,?,?,?,?,?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            String salt = kdfService.dk(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            prep.setString(1, userName);
            prep.setString(2, userPhone);
            prep.setString(3, salt);
            prep.setString(4, kdfService.dk(userPassword, salt));
            prep.setString(5, userEmail);
            prep.setString(6, savedFilename);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + "--" + sql);
            return false;
        }
    }
    private User includeRoles(User user){
        String sql = String.format("SELECT * FROM User_Roles ur JOIN Roles r ON ur.role_id = id" + " Where ur.user_id='%s'",user.getId());
        try(Statement statement = dbService.getConnection().createStatement()) {
            user.includeRules(statement.executeQuery(sql));
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage() + " " + sql);

        }
        return user;
    }
    public User getUserByCredentials(String email, String password) {
        if(email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            logger.log(Level.WARNING, "Invalid email format" );
            return null;
        }

        String sql = "SELECT * FROM Users U Where U.email=?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, email);
            ResultSet resultSet = prep.executeQuery();
            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                String dk = resultSet.getString("dk");
                if (kdfService.dk(password, salt).equals(dk)) {
                    return includeRoles(new User(resultSet));
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + "--" + sql);
        }
        return null;
    }

    public User getUserBuId(String id){
        String sql = "SELECT * FROM Users WHERE id = ? LIMIT 1";

        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)){
            prep.setString(1,id.toString());
            ResultSet resultSet = prep.executeQuery();
            return resultSet.next() ? includeRoles(new User(resultSet)) : null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage() + " " + sql);
            return null;
        }
    }
    public boolean installTable() {
        String sql = "CREATE TABLE Users(" +
                "id         CHAR(36) PRIMARY KEY DEFAULT( UUID())," +
                "name       VARCHAR(64)     NOT NULL," +
                "phone      VARCHAR(16)     NOT NULL," +
                "salt       CHAR(32)        NOT NULL," +
                "dk         CHAR(32)        NOT NULL," +
                "email      VARCHAR(128)    NOT NULL," +
                "avatar     VARCHAR(64)     NULL" +
                ")ENGINE INNODB, DEFAULT CHARSET = utf8mb4";


        try(Statement statement = dbService.getConnection().createStatement()) {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + "--" + sql);
            return false;
        }
    }
}
