package Database.DAO;

import Database.Mappers.ForumMapper;
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
        createTable();
    }

    public void createTable() {
        String sql = "DROP TABLE IF EXISTS forums; " +
                "CREATE EXTENSION IF NOT EXISTS CITEXT;" +
                "CREATE TABLE IF NOT EXISTS forums (" +
                "    posts BIGINT, " +
                "    slug TEXT NOT NULL, " +
                "    threads INTEGER, " +
                "    title TEXT NOT NULL, " +
                "    \"user\" CITEXT NOT NULL UNIQUE " +
                ");";
        this.namedParameterJdbcTemplate.getJdbcOperations().execute(sql);
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
        List<ForumModel> forums = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ForumMapper());

        return forums.isEmpty() ? null : forums.get(0);
    }


}
