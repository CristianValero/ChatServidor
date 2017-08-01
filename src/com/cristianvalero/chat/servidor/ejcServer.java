package com.cristianvalero.chat.servidor;

import com.cristianvalero.chat.servidor.database.Database;
import com.cristianvalero.chat.servidor.process.Server;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ejcServer
{
    public static void main(String[] args)
    {
        ejcServer.log("Iniciando servidor...", LogType.NORMALLY);
        iniServer();
        ejcServer.log("Iniciando conexión a MySQL...", LogType.MYSQL);
        dataBaseBasic();
    }

    public static void stopServer() //Aquí debemos terminar todas las conexiones, procesos, hilos, servidores...
    {
        ejcServer.log("Preparando para apagar el servidor...", LogType.NORMALLY);
        for (Database cons : Database.getActiveConnections())
        {
            try
            {
                cons.getConnection().close();
                ejcServer.log("Conexión con la base de datos '"+cons.getDatabase()+"' cerrada con éxito.", LogType.MYSQL);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                ejcServer.log("Ha surgido un problema al cerrar la conexión con la base de datos '"+cons.getDatabase()+"'.", LogType.MYSQL);
            }
        }
    }

    private static void dataBaseBasic()
    {
        Database db;
        try
        {
            db = new Database("localhost", "", "root", "", 3306);
            db.tryConnection();
            Database.addToList(db);
            dataBaseBasicTables(db.getConnection());
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    private static void dataBaseBasicTables(Connection con) throws SQLException
    {
        List<String> orders = new ArrayList<String>();
        orders.add("CREATE TABLE IF NOT EXISTS usuarios ( id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "nombre VARCHAR(150), email VARCHAR(150), passwd VARCHAR(150) ) Engine=InnoDB;");
        orders.add("CREATE TABLE IF NOT EXISTS estads ( id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "nombre VARCHAR(150), mensajesEnviados INT, vecesLogueado INT ) Engine=InnoDB;");
        for (String order : orders)
            con.prepareStatement(order).execute();
    }

    private static void iniServer()
    {
        Server sv = new Server();
        sv.start();
    }

    public static void log(String txt, LogType type)
    {
        Calendar cal = Calendar.getInstance();
        System.out.print(type.getPrefix()+" ["+cal.get(Calendar.HOUR)+":"+
                         cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+"] "+txt); //[LogType] [HH:MM:SS] Log message....
    }
}
