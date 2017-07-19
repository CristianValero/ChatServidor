package com.cristianvalero.chat.servidor.database;
import com.cristianvalero.chat.servidor.utils.LogType;
import com.cristianvalero.chat.servidor.ejcServer;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database
{
    private String host = null;
    private String database = null;
    private String username = null;
    private String password = null;
    private int port = 3306;
    private final String driver = "com.mysql.jdbc.Driver";

    private Connection con = null;

    private static ArrayList<Database> connections = new ArrayList<Database>();

    public Database(String h, String d, String u, String p, int port)
    {
        this.host = h;
        this.database = d;
        this.username = u;
        this.password = p;
        this.port = port;
    }

    public Database() {}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDriver() {
        return driver;
    }

    public boolean isConnected()
    {
        boolean a = false;
        try
        {
            if (con != null && !con.isClosed()) a = true;
        }
        catch (SQLException e) {e.printStackTrace();}
        return a;
    }

    public static void addToList(Database d)
    {
        connections.add(d);
    }

    public Connection getConnection() {
        return this.con;
    }

    public static List<Database> getActiveConnections()
    {
        return connections;
    }

    public static Connection getConnection(String databaseName)
    {
        Connection c = null;
        for (Database d : connections)
        {
            if (d.getDatabase().equals(databaseName))
            {
                c = d.getConnection();
            }
        }
        return c;
    }

    public static Database getDatabase(String databaseName)
    {
        Database d = null;
        for (Database db : connections)
        {
            if (db.getDatabase().equals(databaseName))
            {
                d = db;
            }
        }
        return d;
    }

    public void tryConnection() throws ClassNotFoundException, SQLException
    {
        if (host != null && database != null && username != null && password != null)
        {
            final String url = "jdbc:mysql//"+host+":"+port+"/"+database+"?autoReconect=true";
            Class.forName(driver);
            DriverManager.getConnection(url, username, password);
            ejcServer.log("Conectado correctamente a MySQL: jdbc:mysql//"+host+":"+port+"/"+database, LogType.MYSQL);
        }
        else
            ejcServer.log("No se puede conectar a la base de datos. No hay datos de conexión (nulos).", LogType.MYSQL);
    }

    public void killConnection() throws SQLException
    {
        con.close();
    }

    public static void removeFromList(String databaseName) throws SQLException
    {
        if (databaseName != null)
        {
            List<Database> temp = new ArrayList<Database>();
            for (Database database : connections)
                if (database.getDatabase().equals(databaseName)) temp.add(database);
            for (Database removes : temp)
            {
                removes.killConnection();
                connections.remove(removes);
            }
        }
        else
            ejcServer.log("Imposible eliminar la base de datos de la lista. Parámetros no especificados.", LogType.MYSQL);
    }
}
