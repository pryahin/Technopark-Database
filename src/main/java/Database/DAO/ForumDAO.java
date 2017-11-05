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
        //long start = System.currentTimeMillis();
        String sql = "INSERT INTO forums " +
                "VALUES (:posts, :slug, :threads, :title, :user)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(forum);
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        //long end = System.currentTimeMillis();
        //System.out.println("ForumDAO: createForum " + (end - start) + "ms");
    }

    public ForumModel getForum(String slug) {
        //long start = System.currentTimeMillis();
        String sql = "SELECT * FROM forums " +
                "WHERE LOWER(slug) = LOWER(:slug)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("slug", slug);
        List<ForumModel> forumList = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ForumMapper());

        //long end = System.currentTimeMillis();
        //System.out.println("ForumDAO: getForum " + (end - start) + "ms");
        return forumList.isEmpty() ? null : forumList.get(0);
    }

    public List<UserModel> getUsers(String slug, int limit, String since, boolean desc) {
        //long start = System.currentTimeMillis();
//        String sql = "SELECT DISTINCT u.* " +
//                "FROM threads t FULL OUTER JOIN posts p ON (t.forum = p.forum) " +
//                "FULL OUTER JOIN forums f ON (t.author = f.user) OR (p.author = f.user)" +
//                "FULL OUTER JOIN users u ON (t.author = u.nickname) OR (p.author = u.nickname) OR (f.user = u.nickname) " +
//                "WHERE LOWER(t.forum) = LOWER(:slug) ";

        String sql = "SELECT u.* " +
                "FROM forumUsers fu JOIN users u ON(LOWER(fu.userNickname) = LOWER(u.nickname)) " +
                "WHERE LOWER(forumSlug) = LOWER(:slug)";

        if (!since.isEmpty()) {
            sql += " AND LOWER(u.nickname) " + (desc ? "<" : ">") + " LOWER(:since) ";
        }

        sql += " ORDER BY LOWER(u.nickname) " + (desc ? "DESC " : "ASC ");

        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource("slug", slug)
                .addValue("since", since);
        List<UserModel> result = this.namedParameterJdbcTemplate.query(sql, namedParameters, new UserMapper());
        //long end = System.currentTimeMillis();
        //System.out.println("ForumDAO: getUsers " + (end - start) + "ms");
        return result;
    }

    public int getCount() {
        //long start = System.currentTimeMillis();
        String sql = "SELECT COUNT(*) FROM forums";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        int result = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        //long end = System.currentTimeMillis();
        //System.out.println("ForumDAO: getCount " + (end - start) + "ms");
        return result;
    }

    public void clearTable() {
        //long start = System.currentTimeMillis();
        String sql = "TRUNCATE TABLE forums CASCADE";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        //long end = System.currentTimeMillis();
        //System.out.println("ForumDAO: clearTable " + (end - start) + "ms");

    }
}
