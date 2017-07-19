package com.cristianvalero.chat.servidor;

import com.cristianvalero.chat.servidor.database.Database;
import com.cristianvalero.chat.servidor.process.Server;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.sql.SQLException;
import java.util.Calendar;

public class ejcServer
{
    public static void main(String[] args)
    {
        ejcServer.log("Iniciando servidor...", LogType.WARNING);
        iniServer();
        ejcServer.log("Iniciando conexión a MySQL...", LogType.MYSQL);
        dataBase();
    }

    private static void dataBase()
    {
        Database db = new Database("localhost", "", "root", "", 3306);
        try
        {
            db.tryConnection();
            Database.addToList(db);
        }
        catch (ClassNotFoundException e){e.printStackTrace();}
        catch (SQLException e) {e.printStackTrace();}
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
