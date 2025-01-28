package lib.test.DB;

import lib.test.Replay.Move;

public interface MyService {
    void createTable();
    void insertUser(int id, String name);
    String getUserName(int id);

    void addSession(int players);

    int getSessionID();

    void updateTable(int oldX, int oldY, int newX, int newY, char playerColor);

    Move[] getFullReplay(int gameId);

    int getPlayerCount(int gameId);
}