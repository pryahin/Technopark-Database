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
        createTable();
    }

    public void createTable() {
        String sql = "DROP TABLE IF EXISTS threads; " +
                "CREATE EXTENSION IF NOT EXISTS CITEXT;" +
                "CREATE TABLE IF NOT EXISTS threads (" +
                "    author CITEXT NOT NULL, " +
                "    created TIMESTAMP DEFAULT current_timestamp, " +
                "    forum TEXT NOT NULL, " +
                "    id SERIAL PRIMARY KEY, " +
                "    message TEXT NOT NULL, " +
                "    slug TEXT UNIQUE, " +
                "    title TEXT NOT NULL, " +
                "    votes INTEGER " +
                ");";
        this.namedParameterJdbcTemplate.getJdbcOperations().execute(sql);
    }

    public void createThread(ThreadModel thread) {
        String created = thread.getCreated();
        String sql = "INSERT INTO threads(author, " + (created == null ? "" : "created,") +" forum, message, slug, title, votes)" +
                "VALUES (:author, " + (created == null ? "" : ":created,") + " :forum, :message, :slug, :title, :votes)";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("author", thread.getAuthor())
                .addValue("created", created == null ? null : TimestampHelper.toTimestamp(thread.getCreated()))
                .addValue("forum", thread.getForum())
                .addValue("message", thread.getMessage())
                .addValue("slug", thread.getSlug())
                .addValue("title", thread.getTitle())
                .addValue("votes", thread.getVotes());
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public ThreadModel getThread(String forum) {
        String sql = "SELECT * FROM threads " +
                "WHERE LOWER(forum) = LOWER(:forum)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("forum", forum);
        List<ThreadModel> threads = this.namedParameterJdbcTemplate.query(sql, namedParameters, new ThreadMapper());

        return threads.isEmpty() ? null : threads.get(0);
    }

//    public List<ThreadModel> getThreads(String forum, limit, )
}