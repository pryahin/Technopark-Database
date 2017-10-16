package Database.DAO;

import Database.Mappers.ForumMapper;
import Database.Mappers.UserMapper;
import Database.Models.ForumModel;
import Database.Models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ForumDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void createForum(ForumModel forum) {
        String sql = "INSERT INTO forums " +
                "VALUES (:posts, :slug, :threads, :title, :user)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(forum);
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public ForumModel getForum(String slug) {
        String sql = "SELECT * FROM forums " +
                "WHERE LOWER(slug) = LOWER(:slug)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("slug", slug);
        List<ForumModel> forumList = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ForumMapper());

        return forumList.isEmpty() ? null : forumList.get(0);
    }

    public List<UserModel> getUsers(String slug, int limit, String since, boolean desc) {
        String sql = "SELECT DISTINCT u.* " +
                "FROM threads t FULL OUTER JOIN posts p ON (t.forum = p.forum) " +
                "FULL OUTER JOIN forums f ON (t.author = f.user) OR (p.author = f.user)" +
                "FULL OUTER JOIN users u ON (t.author = u.nickname) OR (p.author = u.nickname) OR (f.user = u.nickname) " +
                "WHERE LOWER(t.forum) = LOWER(:slug) ";

        if (!since.isEmpty()) {
            sql += " AND LOWER(u.nickname) " + (desc ? "<" : ">") + " LOWER(:since) ";
        }

        sql += " ORDER BY u.nickname " + (desc ? "DESC " : "ASC ");

        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("slug", slug)
                .addValue("since", since);
        return this.namedParameterJdbcTemplate.query(sql, namedParameters, new UserMapper());
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM forums";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public void clearTable() {
        String sql = "TRUNCATE TABLE forums CASCADE";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
    }
}
