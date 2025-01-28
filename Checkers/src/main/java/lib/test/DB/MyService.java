package lib.test.DB;

public interface MyService {
    void createTable();
    void insertUser(int id, String name);
    String getUserName(int id);

    void addSession(int players);

    int getSessionID();

    void updateTable(int oldX, int oldY, int newX, int newY, char playerColor);
}