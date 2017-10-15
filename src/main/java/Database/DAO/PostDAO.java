package Database.DAO;

import Database.Helpers.TimestampHelper;
import Database.Mappers.PostMapper;
import Database.Models.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PostDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PostDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void createPost(List<PostModel> posts) {
        final Timestamp created = new Timestamp(System.currentTimeMillis());
        for (PostModel post : posts) {
            if (post.getCreated() == null) {
                post.setCreated(TimestampHelper.fromTimestamp(created));
            }

            MapSqlParameterSource namedParameters = new MapSqlParameterSource("author", post.getAuthor())
                    .addValue("created", TimestampHelper.toTimestamp(post.getCreated()))
                    .addValue("forum", post.getForum())
                    .addValue("message", post.getMessage())
                    .addValue("parent", post.getParent())
                    .addValue("thread", post.getThread());

            post.setId(this.namedParameterJdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('posts', 'id'))", namedParameters, Integer.class));
            namedParameters.addValue("id", post.getId());
            String sql = "INSERT INTO posts(id, author, created, forum, message, parent, thread, path)" +
                    "VALUES (:id, :author, :created, :forum, :message, :parent, :thread, array_append((SELECT path FROM posts WHERE id = :parent), :id))" +
                    "RETURNING *";

            List<PostModel> result = this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());
            post.setId(result.get(0).getId());
        }
    }

    public PostModel getPost(int id) {
        String sql = "SELECT * FROM posts " +
                "WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<PostModel> posts = this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());

        return posts.isEmpty() ? null : posts.get(0);
    }

    public List<PostModel> getPosts(int thread, int limit, String since, String sort, boolean desc) {
        List<PostModel> posts = null;
        switch (sort) {
            case "flat": {
                posts = flatSort(thread, limit, since, desc);
                break;
            }
            case "tree": {
                posts = treeSort(thread, limit, since, desc);
                break;
            }
            case "parent_tree": {
                posts = parentTreeSort(thread, limit, since, desc);
                break;
            }
            default:
                break;
        }
        return posts;
    }

    private List<PostModel> flatSort(int thread, int limit, String since, boolean desc) {
        String sql = "SELECT * FROM posts " +
                "WHERE thread = :thread";
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

        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("thread", thread)
                .addValue("created", since.isEmpty() ? null : TimestampHelper.toTimestamp(since));

        return this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());
    }

    private List<PostModel> treeSort(int thread, int limit, String since, boolean desc) {
        String sql = "SELECT * FROM posts " +
                "WHERE thread = :thread ";

        if (!since.isEmpty()) {
            if (desc) {
                sql += " AND created < :created";
            } else {
                sql += " AND created > :created";
            }
        }
        if (desc) {
            sql += "ORDER BY path DESC";
        } else {
            sql += "ORDER BY path";
        }
        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("thread", thread)
                .addValue("created", since.isEmpty() ? null : TimestampHelper.toTimestamp(since));

        return this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());
    }

    private List<PostModel> parentTreeSort(int thread, int limit, String since, boolean desc) {
        return null;
    }
}
