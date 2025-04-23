package marcel.weatherapp2.repository;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.model.State;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class StateRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<State> findAll() {
        String sql = "SELECT id, name FROM state";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new State(rs.getLong("id"), rs.getString("name")));
    }

    public State findById(Long id) {
        String sql = "SELECT id, name FROM state WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new State(rs.getLong("id"), rs.getString("name")));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM state WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public State update(State state) {
        String sql = "UPDATE state SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, state.getName(), state.getId());
        return findById(state.getId());
    }

    public State save(State state) {
        if (state.getId() != null) {
            return update(state);
        }
        var keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO state (name) VALUES (?)";
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, state.getName());
            return ps;
        }, keyHolder);
        state.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return state;
    }



}
