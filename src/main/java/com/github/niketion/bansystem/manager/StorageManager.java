package com.github.niketion.bansystem.manager;

import com.github.niketion.bansystem.BanSystemPlugin;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import org.bukkit.Bukkit;

import javax.swing.text.html.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StorageManager {
    private final String USERNAME="root";
    private final String PASSWORD="";
    private final String URL= "jdbc:mysql://localhost:3306/test";

    private BanSystemPlugin plugin;
    private BanManager manager;
    private Connection connection;

    public StorageManager(BanSystemPlugin plugin, BanManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void initStorage() {
        try {
            this.connection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);

            String sqlBanPlayer = "CREATE TABLE IF NOT EXISTS BanPlayer (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "banned BOOLEAN NOT NULL, " +
                    "muted BOOLEAN NOT NULL" +
                    ");";

            String sqlPunishment = "CREATE TABLE IF NOT EXISTS Punishment (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "idPlayer INT NOT NULL, " +
                    "staffer VARCHAR(36) NOT NULL, " +
                    "fromDate BIGINT NOT NULL, " +
                    "toDate BIGINT, " +
                    "permanent BOOLEAN NOT NULL, " +
                    "message TEXT, " +
                    "type VARCHAR(10) NOT NULL, " +
                    "FOREIGN KEY (idPlayer) REFERENCES BanPlayer(id) ON DELETE CASCADE" +
                    ");";

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(sqlBanPlayer);
            stmt.executeUpdate(sqlPunishment);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllBanPlayers() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            List<BanPlayer> banPlayers = new ArrayList<>();

            String sqlBanPlayers = "SELECT * FROM BanPlayer";
            String sqlPunishments = "SELECT * FROM Punishment WHERE idPlayer = ?";

            try (Statement stmtBanPlayers = connection.createStatement();
                 ResultSet rsBanPlayers = stmtBanPlayers.executeQuery(sqlBanPlayers)) {

                while (rsBanPlayers.next()) {
                    BanPlayer banPlayer = new BanPlayer();
                    banPlayer.setId(rsBanPlayers.getInt("id"));
                    banPlayer.setUuid(UUID.fromString(rsBanPlayers.getString("uuid")));

                    List<Punishment> punishments = new ArrayList<>();
                    try (PreparedStatement stmtPunishments = connection.prepareStatement(sqlPunishments)) {
                        stmtPunishments.setInt(1, banPlayer.getId());
                        ResultSet rsPunishments = stmtPunishments.executeQuery();

                        while (rsPunishments.next()) {
                            Punishment punishment = new Punishment();
                            punishment.setId(rsPunishments.getInt("id"));
                            punishment.setIdPlayer(rsPunishments.getInt("idPlayer"));
                            punishment.setStaffer(rsPunishments.getString("staffer"));
                            punishment.setFromDate(rsPunishments.getLong("fromDate"));
                            punishment.setToDate(rsPunishments.getLong("toDate"));
                            punishment.setPermanent(rsPunishments.getBoolean("permanent"));
                            punishment.setMessage(rsPunishments.getString("message"));
                            punishment.setType(Punishment.Type.valueOf(rsPunishments.getString("type")));
                            punishments.add(punishment);
                        }
                    }

                    banPlayer.setPunishments(punishments);
                    banPlayers.add(banPlayer);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            banPlayers.forEach((banPlayer) -> {
                manager.loadedPlayers.put(banPlayer.getUuid(), banPlayer);
            });
        });
    }

    public void updatePunishment(Punishment punishment) {
        String sql = "UPDATE Punishment SET fromDate = ?, toDate = ?, permanent = ?, message = ?, type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, punishment.getFromDate());
            stmt.setLong(2, punishment.getToDate());
            stmt.setBoolean(3, punishment.isPermanent());
            stmt.setString(4, punishment.getMessage());
            stmt.setString(5, punishment.getType().name());
            stmt.setInt(6, punishment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBanPlayer(UUID uuid) {
        String sql = "DELETE FROM BanPlayer WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePunishment(int punishmentId) {
        String sql = "DELETE FROM Punishment WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, punishmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertBanPlayer(BanPlayer banPlayer) {
        String sql = "INSERT INTO BanPlayer (uuid) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, banPlayer.getUuid().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertPunishment(Punishment punishment) {
        String sql = "INSERT INTO Punishment (idPlayer, staffer, fromDate, toDate, permanent, message, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, punishment.getIdPlayer());
            stmt.setString(2, punishment.getStaffer().toString());
            stmt.setLong(3, punishment.getFromDate());
            stmt.setLong(4, punishment.getToDate());
            stmt.setBoolean(5, punishment.isPermanent());
            stmt.setString(6, punishment.getMessage());
            stmt.setString(7, punishment.getType().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
