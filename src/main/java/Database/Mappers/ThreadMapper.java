package Database.Mappers;

import Database.Helpers.TimestampHelper;
import Database.Models.ThreadModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ThreadMapper implements RowMapper<ThreadModel> {
    @Override
    public ThreadModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ThreadModel threadModel = new ThreadModel();
        threadModel.setAuthor(resultSet.getString("author"));
        threadModel.setCreated(TimestampHelper.fromTimestamp(resultSet.getTimestamp("created")));
        threadModel.setForum(resultSet.getString("forum"));
        threadModel.setId(resultSet.getInt("id"));
        threadModel.setMessage(resultSet.getString("message"));
        threadModel.setSlug(resultSet.getString("slug"));
        threadModel.setTitle(resultSet.getString("title"));
        threadModel.setVotes(resultSet.getInt("votes"));

        return threadModel;
    }
}
