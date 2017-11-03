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
    }

    public void addUser(UserModel user) {
        //long start = System.currentTimeMillis();
        String sql = "INSERT INTO users " +
                "VALUES (:about, :email, :fullname, :nickname)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        //long end = System.currentTimeMillis();
        //System.out.println("UserDAO: addUser "+(end-start)+"ms");
    }

    public UserModel getUser(String nickname) {
        //long start = System.currentTimeMillis();
        String sql = "SELECT * FROM users " +
                "WHERE LOWER(nickname) = LOWER(:nickname)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("nickname", nickname);
        List<UserModel> userList = this.namedParameterJdbcTemplate.query(sql, namedParameters, new UserMapper());

        //long end = System.currentTimeMillis();
        //System.out.println("UserDAO: getUser "+(end-start)+"ms");
        return userList.isEmpty() ? null : userList.get(0);
    }

    public List<UserModel> getUsers(String nickname, String email) {
        //long start = System.currentTimeMillis();
        String sql = "SELECT * FROM users " +
                "WHERE LOWER(email) = LOWER(:email) OR LOWER(nickname) = LOWER(:nickname)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("email", email).addValue("nickname", nickname);
        List<UserModel> result = this.namedParameterJdbcTemplate.query(sql, namedParameters, new UserMapper());
        //long end = System.currentTimeMillis();
        //System.out.println("UserDAO: getUsers "+(end-start)+"ms");
        return result;
    }

    public void updateUser(String nickname, UserUpdateModel user) {
        //long start = System.currentTimeMillis();
        if (user.getAbout() == null && user.getEmail() == null && user.getFullname() == null) {
            //long end = System.currentTimeMillis();
            //System.out.println("UserDAO: updateUser "+(end-start)+"ms");
            return;
        }
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
        //long end = System.currentTimeMillis();
        //System.out.println("UserDAO: updateUser "+(end-start)+"ms");
    }

    public int getCount() {
        //long start = System.currentTimeMillis();
        String sql = "SELECT COUNT(*) FROM users";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        int result = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        //long end = System.currentTimeMillis();
        //System.out.println("UserDAO: getCount "+(end-start)+"ms");
        return result;
    }

    public void clearTable() {
        //long start = System.currentTimeMillis();
        String sql = "TRUNCATE TABLE users CASCADE";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        //long end = System.currentTimeMillis();
        //System.out.println("UserDAO: clearTable "+(end-start)+"ms");
    }
}
