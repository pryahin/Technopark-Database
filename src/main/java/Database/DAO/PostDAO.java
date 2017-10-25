package Database.DAO;

import Database.Helpers.TimestampHelper;
import Database.Mappers.PostMapper;
import Database.Models.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PostDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createPost(List<PostModel> posts) {
        final Timestamp created = new Timestamp(System.currentTimeMillis());

        String sql = "INSERT INTO posts(id, author, created, forum, message, parent, thread, path)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, array_append((SELECT path FROM posts WHERE id = ?), ?))";
        if (posts.isEmpty()) {
            return;
        }
        try(Connection con = this.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.NO_GENERATED_KEYS)) {

            for (PostModel post : posts) {
                if (post.getCreated() == null) {
                    post.setCreated(TimestampHelper.fromTimestamp(created));
                }

                post.setId(this.jdbcTemplate.queryForObject("SELECT nextval(pg_get_serial_sequence('posts', 'id'))", Integer.class));

                ps.setInt(1, post.getId());
                ps.setString(2, post.getAuthor());
                ps.setTimestamp(3, TimestampHelper.toTimestamp(post.getCreated()));
                ps.setString(4, post.getForum());
                ps.setString(5, post.getMessage());
                ps.setInt(6, post.getParent());
                ps.setInt(7, post.getThread());
                ps.setInt(8, post.getParent());
                ps.setInt(9, post.getId());
                ps.addBatch();
            }
            ps.executeBatch();

            sql = "UPDATE forums " +
                    "SET posts = posts + ?" +
                    "WHERE LOWER(slug) = LOWER(?) ";
            this.jdbcTemplate.update(sql, posts.size(), posts.get(0).getForum());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PostModel getPost(int id) {
        String sql = "SELECT * FROM posts " +
                "WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<PostModel> postList = this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());

        return postList.isEmpty() ? null : postList.get(0);
    }

    public void updatePost(PostModel post) {
        String sql = "UPDATE posts SET message = :message, isEdited = TRUE " +
                "WHERE id = :id";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(post);

        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        post.setEdited(true);
    }

    public List<PostModel> getPosts(int thread, int limit, int since, String sort, boolean desc) {
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

    private List<PostModel> flatSort(int thread, int limit, int since, boolean desc) {
        String sql = "SELECT * FROM posts " +
                "WHERE thread = :thread";
        if (since != -1) {
            if (desc) {
                sql += " AND id < :since ";
            } else {
                sql += " AND id > :since ";
            }
        }
        if (desc) {
            sql += " ORDER BY created DESC, id DESC ";
        } else {
            sql += " ORDER BY created ASC, id ASC ";
        }

        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("thread", thread)
                .addValue("since", since);

        return this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());
    }

    private List<PostModel> treeSort(int thread, int limit, int since, boolean desc) {
        String sql = "SELECT * FROM posts " +
                "WHERE thread = :thread ";

        if (since != -1) {
            if (desc) {
                sql += " AND path < (SELECT path FROM posts WHERE id = :since) ";
            } else {
                sql += " AND path > (SELECT path FROM posts WHERE id = :since) ";
            }
        }
        if (desc) {
            sql += " ORDER BY path DESC ";
        } else {
            sql += " ORDER BY path ASC ";
        }
        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("thread", thread)
                .addValue("since", since);

        return this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());
    }

    private List<PostModel> parentTreeSort(int thread, int limit, int since, boolean desc) {
        List<Integer> parents = getParents(thread, limit, since, desc);
        String sql = "SELECT * FROM posts " +
                "WHERE thread = :thread AND path[1] = :parent ";

        if (since != -1) {
            if (desc) {
                sql += " AND path[1] < (SELECT path[1] FROM posts WHERE id = :since) ";
            } else {
                sql += " AND path[1] > (SELECT path[1] FROM posts WHERE id = :since) ";
            }
        }
        if (desc) {
            sql += " ORDER BY path DESC, id DESC ";
        } else {
            sql += " ORDER BY path ASC, id ASC ";
        }

        List<PostModel> posts = new ArrayList<>();
        for (int parent : parents) {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource("thread", thread)
                    .addValue("since", since)
                    .addValue("parent", parent);
            posts.addAll(this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper()));
        }

        return posts;
    }

    private List<Integer> getParents(int thread, int limit, int since, boolean desc) {
        String sql = "SELECT id FROM posts " +
                "WHERE thread = :thread AND parent = 0 ";

        if (since != -1) {
            if (desc) {
                sql += " AND path[1] < (SELECT path[1] FROM posts WHERE id = :since) ";
            } else {
                sql += " AND path[1] > (SELECT path[1] FROM posts WHERE id = :since) ";
            }
        }
        if (desc) {
            sql += " ORDER BY id DESC ";
        } else {
            sql += " ORDER BY id ASC ";
        }
        if (limit != 0) {
            sql += " LIMIT " + limit;
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("thread", thread)
                .addValue("since", since);

        return this.namedParameterJdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> rs.getInt("id"));
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM posts";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public void clearTable() {
        String sql = "TRUNCATE TABLE posts CASCADE";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
    }
}
