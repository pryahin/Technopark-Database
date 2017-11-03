package Database.DAO;

import Database.Helpers.TimestampHelper;
import Database.Mappers.ThreadMapper;
import Database.Models.ThreadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ThreadDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ThreadDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void createThread(ThreadModel thread) {
        //long start = System.currentTimeMillis();
        final Timestamp created = new Timestamp(System.currentTimeMillis());
        if (thread.getCreated() == null) {
            thread.setCreated(TimestampHelper.fromTimestamp(created));
        }

        String sql = "INSERT INTO threads(author, created, forum, message, slug, title, votes)" +
                "VALUES (:author, :created, :forum, :message, :slug, :title, :votes) " +
                "RETURNING id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("author", thread.getAuthor())
                .addValue("created", TimestampHelper.toTimestamp(thread.getCreated()))
                .addValue("forum", thread.getForum())
                .addValue("message", thread.getMessage())
                .addValue("slug", thread.getSlug())
                .addValue("title", thread.getTitle())
                .addValue("votes", thread.getVotes());
        final int id = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        thread.setId(id);

        sql = "UPDATE forums " +
                "SET threads = (SELECT COUNT(*) FROM threads WHERE forum = :forum) " +
                "WHERE LOWER(slug) = LOWER(:forum) ";
        this.namedParameterJdbcTemplate.update(sql, namedParameters);

        sql = "INSERT INTO forumUsers(userNickname, forumSlug) " +
                "VALUES (:author, :forum) " +
                "ON CONFLICT (usernickname, forumslug) DO NOTHING";
        this.namedParameterJdbcTemplate.update(sql, namedParameters);

        //long end = System.currentTimeMillis();
        //System.out.println("ThreadDAO: createThread "+(end-start)+"ms");
    }

    public ThreadModel getThreadBySlug(String slug) {
        //long start = System.currentTimeMillis();
        String sql = "SELECT * FROM threads " +
                "WHERE LOWER(slug) = LOWER(:slug)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("slug", slug);
        List<ThreadModel> threadList = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ThreadMapper());

        //long end = System.currentTimeMillis();
        //System.out.println("ThreadDAO: getThreadBySlug "+(end-start)+"ms");
        return threadList.isEmpty() ? null : threadList.get(0);
    }

    public ThreadModel getThreadById(int id) {
        //long start = System.currentTimeMillis();
        String sql = "SELECT * FROM threads " +
                "WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<ThreadModel> threadList = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ThreadMapper());

        //long end = System.currentTimeMillis();
        //System.out.println("ThreadDAO: getThreadById "+(end-start)+"ms");
        return threadList.isEmpty() ? null : threadList.get(0);
    }

    public ThreadModel getThreadBySlugOrId(String slug) {
        if (slug.matches("[-+]?\\d*\\.?\\d+")) {
            return getThreadById(Integer.parseInt(slug));
        } else {
            return getThreadBySlug(slug);
        }
    }

    public void updateThread(ThreadModel thread) {
        //long start = System.currentTimeMillis();
        String sql = "UPDATE threads SET message = :message, title = :title " +
                "WHERE id = :id";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(thread);

        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        //long end = System.currentTimeMillis();
        //System.out.println("ThreadDAO: updateThread "+(end-start)+"ms");
    }

    public List<ThreadModel> getThreads(String forum, int limit, String since, boolean desc) {
        //long start = System.currentTimeMillis();
        String sql = "SELECT * FROM threads " +
                "WHERE LOWER(forum) = LOWER(:forum) ";
        if (!since.isEmpty()) {
            if (desc) {
                sql += " AND created <= :created ";
            } else {
                sql += " AND created >= :created ";
            }
        }
        if (desc) {
            sql += " ORDER BY created DESC ";
        } else {
            sql += " ORDER BY created ";
        }

        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("forum", forum)
                .addValue("created", since.isEmpty() ? null : TimestampHelper.toTimestamp(since));
        List<ThreadModel> threads = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ThreadMapper());
        //long end = System.currentTimeMillis();
        //System.out.println("ThreadDAO: getThreads "+(end-start)+"ms");
        return threads;
    }

    public int getCount() {
        //long start = System.currentTimeMillis();
        String sql = "SELECT COUNT(*) FROM threads";
        SqlParameterSource namedParameters = new MapSqlParameterSource();

        int result = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        //long end = System.currentTimeMillis();
        //System.out.println("ThreadDAO: getCount "+(end-start)+"ms");
        return result;
    }

    public void clearTable() {
        //long start = System.currentTimeMillis();
        String sql = "TRUNCATE TABLE threads CASCADE";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        //long end = System.currentTimeMillis();
        //System.out.println("ThreadDAO: clearTable "+(end-start)+"ms");
    }
}
