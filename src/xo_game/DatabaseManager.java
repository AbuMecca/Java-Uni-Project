package xo_game;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    public void saveGame(Score score) {
        String sql = "INSERT INTO GameHistory (Player1, Player2, Winner) VALUES (?, ?, ?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, score.getPlayer1());
            ps.setString(2, score.getPlayer2());
            ps.setString(3, score.getWinner());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Score> fetchAllGames() {
        List<Score> list = new ArrayList<>();
        String sql = "SELECT Player1, Player2, Winner FROM GameHistory ORDER BY PlayedAt DESC";
        try (Connection c = DbUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Score(
                  rs.getString("Player1"),
                  rs.getString("Player2"),
                  rs.getString("Winner")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

public void resetGames() {
    String sql = "TRUNCATE TABLE GameHistory";
    try (Connection c = DbUtil.getConnection();
         Statement st = c.createStatement()) {
        st.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
  }
}

