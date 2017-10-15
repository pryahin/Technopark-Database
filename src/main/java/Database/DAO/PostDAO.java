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
            String sql = "INSERT INTO posts(author, created, forum, message, parent, thread)" +
                    "VALUES (:author, :created, :forum, :message, :parent, :thread) " +
                    "RETURNING *";

            MapSqlParameterSource namedParameters = new MapSqlParameterSource("author", post.getAuthor())
                    .addValue("created", TimestampHelper.toTimestamp(post.getCreated()))
                    .addValue("forum", post.getForum())
                    .addValue("message", post.getMessage())
                    .addValue("parent", post.getParent())
                    .addValue("thread", post.getThread());

            List<PostModel> result = this.namedParameterJdbcTemplate.query(sql, namedParameters, new PostMapper());
            post.setId(result.get(0).getId());
        }
    }

    public PostModel getPost(int id) {
        String sql = "SELECT * FROM posts " +
                "WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<PostModel> posts =  this.namedParameterJdbcTemplate.query(sql,namedParameters, new PostMapper());

        return posts.isEmpty() ? null : posts.get(0);
    }
}
