package Database.DAO;

import Database.Models.VoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class VoteDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public VoteDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int vote(VoteModel vote) {
        //long start = System.currentTimeMillis();
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(vote);
        try {
            String sql = "INSERT INTO votes(nickname, voice, thread)" +
                    "VALUES (:nickname, :voice, :thread) ";
            this.namedParameterJdbcTemplate.update(sql, namedParameters);
        } catch (DuplicateKeyException ex) {
            String sql = "UPDATE votes " +
                    "SET voice = :voice " +
                    "WHERE nickname = :nickname AND thread = :thread";
            this.namedParameterJdbcTemplate.update(sql, namedParameters);
        }
        String sql = "UPDATE threads " +
                "SET votes = (SELECT SUM(voice) FROM votes WHERE thread = :thread) " +
                "WHERE id = :thread " +
                "RETURNING votes";

        int result = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        //long end = System.currentTimeMillis();
        //System.out.println("VoteDAO: vote "+(end-start)+"ms");
        return result;
    }

    public void clearTable() {
        //long start = System.currentTimeMillis();
        String sql = "TRUNCATE TABLE votes CASCADE";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        //long end = System.currentTimeMillis();
        //System.out.println("VoteDAO: clearTable "+(end-start)+"ms");
    }
}
