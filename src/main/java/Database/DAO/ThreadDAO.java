package Database.DAO;

import Database.Helpers.TimestampHelper;
import Database.Mappers.ThreadMapper;
import Database.Models.ThreadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ThreadDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ThreadDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void createThread(ThreadModel thread) {
        boolean hasCreated = thread.getCreated() == null;
        String sql = "INSERT INTO threads(author, " + (hasCreated ? "" : "created,") +" forum, message, slug, title, votes)" +
                "VALUES (:author, " + (hasCreated ? "" : ":created,") + " :forum, :message, :slug, :title, :votes) " +
                "RETURNING id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("author", thread.getAuthor())
                .addValue("created", hasCreated ? null : TimestampHelper.toTimestamp(thread.getCreated()))
                .addValue("forum", thread.getForum())
                .addValue("message", thread.getMessage())
                .addValue("slug", thread.getSlug())
                .addValue("title", thread.getTitle())
                .addValue("votes", thread.getVotes());
        final int id = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        thread.setId(id);
    }

    public ThreadModel getThread(String forum) {
        String sql = "SELECT * FROM threads " +
                "WHERE LOWER(forum) = LOWER(:forum)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("forum", forum);
        List<ThreadModel> threads = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ThreadMapper());

        return threads.isEmpty() ? null : threads.get(0);
    }

    public List<ThreadModel> getThreads(String forum, int limit, String since, boolean desc) {
        String sql = "SELECT * FROM threads " +
                "WHERE LOWER(forum) = LOWER(:forum) ";
        if (!since.isEmpty()) {
            if (desc) {
                sql += " AND created <= :created";
            } else {
                sql += " AND created >= :created";
            }
        }
        if (desc) {
            sql += " ORDER BY created DESC";
        } else {
            sql += " ORDER BY created";
        }

        if (limit!=0) {
            sql += " LIMIT " + limit;
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("forum", forum)
                .addValue("created", since.isEmpty() ? null : TimestampHelper.toTimestamp(since));
        List<ThreadModel> threads = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ThreadMapper());
        return threads;
    }
}