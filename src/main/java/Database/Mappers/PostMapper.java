package Database.Mappers;

import Database.Helpers.TimestampHelper;
import Database.Models.PostModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<PostModel> {
    @Override
    public PostModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        PostModel postModel = new PostModel();
        postModel.setAuthor(resultSet.getString("author"));
        postModel.setCreated(TimestampHelper.fromTimestamp(resultSet.getTimestamp("created")));
        postModel.setForum(resultSet.getString("forum"));
        postModel.setId(resultSet.getInt("id"));
        postModel.setEdited(resultSet.getBoolean("isEdited"));
        postModel.setMessage(resultSet.getString("message"));
        postModel.setParent(resultSet.getInt("parent"));
        postModel.setThread(resultSet.getInt("thread"));
        return postModel;
    }
}
