package Database.Mappers;

import Database.Models.ForumModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForumMapper implements RowMapper<ForumModel> {
    @Override
    public ForumModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ForumModel forumModel = new ForumModel();
        forumModel.setPosts(resultSet.getInt("posts"));
        forumModel.setSlug(resultSet.getString("slug"));
        forumModel.setThreads(resultSet.getInt("threads"));
        forumModel.setTitle(resultSet.getString("title"));
        forumModel.setUser(resultSet.getString("user"));
        return forumModel;
    }
}
