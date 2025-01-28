package lib.test.DB;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService {

    private final JdbcTemplate jdbcTemplate;

    public MyServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS game"+getSessionID()+" (move_id INT PRIMARY KEY AUTO_INCREMENT, old_x INT, old_y INT, new_x INT, new_y INT, player varchar(1))");
        System.out.println("Game table created");
    }

    @Override
    public void insertUser(int id, String name) {
        jdbcTemplate.update("INSERT INTO users (id, name) VALUES (?, ?)", id, name);
    }

    @Override
    public String getUserName(int id) {
        return jdbcTemplate.queryForObject("SELECT name FROM users WHERE id = ?", String.class, id);
    }

    @Override
    public void addSession(int players) {
        int id;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS sessions (id INT PRIMARY KEY AUTO_INCREMENT, sessionName VARCHAR(100), players INT)");

        try {
            id = getSessionID()+1;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        jdbcTemplate.update("INSERT INTO sessions (sessionName, players) VALUES (?, ?)", "game"+id, players);
        System.out.println("Session added");
    }

    @Override
    public int getSessionID() {
        if(jdbcTemplate.queryForObject("SELECT MAX(id) FROM sessions", Integer.class) == null) {
            return 0;
        }else{
            return jdbcTemplate.queryForObject("SELECT MAX(id) FROM sessions", Integer.class);
        }
    }

    @Override
    public void updateTable(int oldX, int oldY, int newX, int newY, char playerColor) {
        String color = String.valueOf(playerColor);
        String gameSesh = "game"+getSessionID();
        jdbcTemplate.update("INSERT INTO game"+getSessionID()+" (old_x, old_y, new_x, new_y, player) VALUES (?, ?, ?, ?, ?)", oldX, oldY, newX, newY, color);
    }
}
