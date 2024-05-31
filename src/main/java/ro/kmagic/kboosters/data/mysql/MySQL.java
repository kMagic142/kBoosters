package ro.kmagic.kboosters.data.mysql;

import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.config.ConfigBooster;
import ro.kmagic.kboosters.data.types.boosters.Booster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MySQL {

    private final ConnectionPoolManager connectionPoolManager;

    public MySQL() {
        connectionPoolManager = new ConnectionPoolManager();
        setupTables();
    }

    private void setupTables() {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS boosters_active(uuid VARCHAR(36) NOT NULL, id INT, duration INT(10), server VARCHAR(32))");
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS boosters_inactive(uuid VARCHAR(36) NOT NULL, id INT, duration INT(10), server VARCHAR(32))");
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS boosters_types(id INT NOT NULL, multiplier DOUBLE, type VARCHAR(8), server VARCHAR(32), name VARCHAR(64), PRIMARY KEY(id))");
            statement.executeUpdate();
            statement.close();

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public void addBooster(UUID owner, int id, long duration, boolean active, String server) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement;
            if(active) {
                statement = connection.prepareStatement("INSERT INTO boosters_active(uuid, id, duration, server) VALUES(?, ?, ?, ?)");
            } else {
                statement = connection.prepareStatement("INSERT INTO boosters_inactive(uuid, id, duration, server) VALUES(?, ?, ?, ?)");
            }

            statement.setString(1, owner.toString());
            statement.setInt(2, id);
            statement.setLong(3, duration);
            statement.setString(4, server);

            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public void removeBooster(UUID owner, int id, long duration, boolean active, String server) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement;
            if(active) {
                statement = connection.prepareStatement("DELETE FROM boosters_active WHERE (uuid, id, duration, server) = (?, ?, ?, ?) LIMIT 1");
                statement.setString(1, owner.toString());
                statement.setInt(2, id);
                statement.setLong(3, duration);
            } else {
                statement = connection.prepareStatement("DELETE FROM boosters_inactive WHERE (uuid, id, duration, server) = (?, ?, ?, ?) LIMIT 1");
                statement.setString(1, owner.toString());
                statement.setInt(2, id);
                statement.setLong(3, duration);
            }

            statement.setString(4, server);
            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public void setActive(Booster booster) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement;

            statement = connection.prepareStatement("INSERT INTO boosters_active(uuid, id, duration, server) VALUES(?, ?, ?, ?)");
            statement.setString(1, booster.getOwner().toString());
            statement.setInt(2, booster.getID());
            statement.setLong(3, booster.getDuration());
            statement.setString(4, Boosters.getInstance().getConfigBoosterManager().getBooster(booster.getID()).getServer());

            statement.executeUpdate();

            statement.close();
            statement = connection.prepareStatement("DELETE FROM boosters_inactive WHERE uuid=? AND id=? AND duration=? AND server=? LIMIT 1");
            statement.setString(1, booster.getOwner().toString());
            statement.setInt(2, booster.getID());
            statement.setLong(3, booster.getDuration());
            statement.setString(4, Boosters.getInstance().getConfigBoosterManager().getBooster(booster.getID()).getServer());

            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public void setInactive(Booster booster) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement;

            statement = connection.prepareStatement("INSERT INTO boosters_inactive(uuid, id, duration, server) VALUES(?, ?, ?, ?)");
            statement.setString(1, booster.getOwner().toString());
            statement.setInt(2, booster.getID());
            statement.setLong(3, booster.getDuration());
            statement.setString(4, Boosters.getInstance().getConfigBoosterManager().getBooster(booster.getID()).getServer());

            statement.executeUpdate();

            statement.close();
            statement = connection.prepareStatement("DELETE FROM boosters_active WHERE uuid=? AND id=? AND duration=? AND server=? LIMIT 1");
            statement.setString(1, booster.getOwner().toString());
            statement.setInt(2, booster.getID());
            statement.setLong(3, booster.getDuration());
            statement.setString(4, Boosters.getInstance().getConfigBoosterManager().getBooster(booster.getID()).getServer());

            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public List<Booster> getInactiveBoosters(UUID player) {
        Connection connection = null;
        LinkedList<Booster> boosters = new LinkedList<>();

        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM boosters_inactive WHERE `uuid` = ?");

            statement.setString(1, player.toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                long duration = result.getLong("duration");
                int id = result.getInt("id");

                boosters.add(new Booster(player, id, duration, false));
            }

            return boosters;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public List<Booster> getActiveBoosters(UUID player) {
        Connection connection = null;
        LinkedList<Booster> boosters = new LinkedList<>();

        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM boosters_active WHERE `uuid` = ?");

            statement.setString(1, player.toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                long duration = result.getLong("duration");
                int id = result.getInt("id");

                boosters.add(new Booster(player, id, duration, true));
            }

            return boosters;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public List<Booster> getBoostersForServer(String server) {
        Connection connection = null;
        LinkedList<Booster> activeBoosters = new LinkedList<>();
        LinkedList<Booster> inactiveBoosters = new LinkedList<>();

        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement;

            statement = connection.prepareStatement("SELECT * FROM boosters_active WHERE `server` = ?");
            statement.setString(1, server);

            ResultSet result = statement.executeQuery();

            while(result.next()) {
                String uuid = result.getString("uuid");
                long duration = result.getLong("duration");
                int id = result.getInt("id");

                activeBoosters.add(new Booster(UUID.fromString(uuid), id, duration, true));
            }

            statement.close();

            statement = connection.prepareStatement("SELECT * FROM boosters_inactive WHERE `server` = ?");
            statement.setString(1, server);

            ResultSet result2 = statement.executeQuery();

            while(result2.next()) {
                String uuid = result2.getString("uuid");
                long duration = result2.getLong("duration");
                int id = result2.getInt("id");

                inactiveBoosters.add(new Booster(UUID.fromString(uuid), id, duration, false));
            }

            LinkedList<Booster> merged = new LinkedList<>(activeBoosters);
            merged.addAll(inactiveBoosters);

            return merged;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }


    public List<ConfigBooster> getBoosterTypes() {
        Connection connection = null;
        LinkedList<ConfigBooster> boosters = new LinkedList<>();

        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM boosters_types");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                double multiplier = result.getDouble("multiplier");
                String type = result.getString("type");
                String server = result.getString("server");
                String name = result.getString("name");

                boosters.add(new ConfigBooster(id, multiplier, type, server, name));
            }

            return boosters;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public void addBoosterType(ConfigBooster booster) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("INSERT INTO boosters_types(id, multiplier, type, server, name) VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE multiplier=?, type=?, server=?, name=?");
            statement.setInt(1, booster.getID());
            statement.setDouble(2, booster.getMultiplier());
            statement.setString(3, booster.getType().name());
            statement.setString(4, booster.getServer());
            statement.setString(5, booster.getName());

            statement.setDouble(6, booster.getMultiplier());
            statement.setString(7, booster.getType().name());
            statement.setString(8, booster.getServer());
            statement.setString(9, booster.getName());

            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public void removeBoosterType(ConfigBooster booster) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("DELETE FROM boosters_types WHERE id=?");
            statement.setInt(1, booster.getID());

            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }


    public ConnectionPoolManager getConnectionPoolManager() {
        return connectionPoolManager;
    }

}
