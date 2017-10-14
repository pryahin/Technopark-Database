package Database.DAO;

import Database.Mappers.UserMapper;
import Database.Models.UserModel;
import Database.Models.UserUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.createTable();
    }

    public void createTable() {
        String sql = "DROP TABLE IF EXISTS users;" +
                "CREATE EXTENSION IF NOT EXISTS CITEXT;" +
                "CREATE TABLE IF NOT EXISTS users (" +
                "about TEXT NOT NULL," +
                "email CITEXT UNIQUE NOT NULL," +
                "fullname TEXT NOT NULL," +
                "nickname CITEXT UNIQUE NOT NULL PRIMARY KEY" +
                ");";
        this.namedParameterJdbcTemplate.getJdbcOperations().execute(sql);
    }

    public void addUser(UserModel user) {
        String sql = "INSERT INTO users " +
                "VALUES (:about, :email, :fullname, :nickname)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public UserModel getUser(String nickname) {
        String sql = "SELECT * FROM users " +
                "WHERE LOWER(nickname) = LOWER(:nickname)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("nickname", nickname);
        List<UserModel> users = this.namedParameterJdbcTemplate.query(sql, namedParameters, new UserMapper());

        return users.isEmpty() ? null : users.get(0);
    }

    public List<UserModel> getUsers(String nickname, String email) {
        String sql = "SELECT * FROM users " +
                "WHERE LOWER(email) = LOWER(:email) OR LOWER(nickname) = LOWER(:nickname)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("email", email).addValue("nickname", nickname);
        return this.namedParameterJdbcTemplate.query(sql, namedParameters, new UserMapper());
    }

    public void updateUser(String nickname, UserUpdateModel user) {
        StringBuilder sql = new StringBuilder().append("UPDATE users SET ");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("nickname", nickname);
        if (user.getAbout() != null) {
            sql.append("about = :about, ");
            namedParameters.addValue("about", user.getAbout());
        }
        if (user.getEmail() != null) {
            sql.append("email = :email, ");
            namedParameters.addValue("email", user.getEmail());
        }
        if (user.getFullname() != null) {
            sql.append("fullname = :fullname, ");
            namedParameters.addValue("fullname", user.getFullname());
        }
        sql.deleteCharAt(sql.length() - 2);
        sql.append("WHERE (LOWER(nickname) = LOWER(:nickname));");

        this.namedParameterJdbcTemplate.update(sql.toString(), namedParameters);
    }

}
