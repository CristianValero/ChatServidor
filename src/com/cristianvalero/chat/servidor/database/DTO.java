package com.cristianvalero.chat.servidor.database;

import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.ClientRank;
import com.cristianvalero.chat.servidor.utils.LogType;
import com.sun.istack.internal.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

public class DTO //Data transfer object
{
    public static void registerMessage(@NotNull String message, @NotNull String senderEmail)
    {
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());
        Calendar cal = Calendar.getInstance();

        final String hour = cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        final String date = (cal.get(Calendar.DATE)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
        final String insertExecute = "INSERT INTO messages ( send_by_email, message, hour, day ) VALUES "+
                                     "( '"+senderEmail+"', '"+message+"', '"+hour+"', '"+date+"' );";

        try
        {
            con.prepareStatement(insertExecute).execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error insertando datos en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
    }

    public static void removeUser(@NotNull String email)
    {
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String deleteExecute = "DELECTE FROM usuarios WHERE email = '"+email+"';";

        try
        {
            con.prepareStatement(deleteExecute).execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error obteniendo datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
    }

    public static void registerUser(@NotNull String name, @NotNull String email, @NotNull String passwd, @NotNull String ip)
    {
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String regUserOnUsers = "INSERT INTO usuarios ( email, ip, nombre, passwd ) " +
                "VALUES ('"+name+"', '"+ip+"', '"+email+"', '"+passwd+"');";
        final String regUserOnEstads = "INSERT INTO estads ( email, mensajes_enviados, veces_logueado, rango ) " +
                "VALUES ( '"+email+"', 0, 0, "+ ClientRank.USER.getId()+" );";

        try
        {
            con.prepareStatement(regUserOnUsers).execute();
            con.prepareStatement(regUserOnEstads).execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error registrando datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
    }

    public static void changeUserPassword(@NotNull String email, @NotNull String newPasswd)
    {
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String updateStatement = "UPDATE usuarios SET passwd = '"+newPasswd+"' WHERE email = '"+email+"';";

        try
        {
            con.prepareStatement(updateStatement).executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error actualizando datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
    }

    public static void changeUserNickname(@NotNull String email, @NotNull String newNick)
    {
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String updateStatement = "UPDATE usuarios SET nombre = '"+newNick+"' WHERE email = '"+email+"';";

        try
        {
            con.prepareStatement(updateStatement).executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error actualizando datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
    }

    public static void saveChangesFromUser(ClientData cd)
    {
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String updateStatement = "UPDATE estads SET mensajes_enviados = "+cd.getMessagesSent()+", "+
                "veces_logueado = "+cd.getTimesLoggedIn()+";";

        try
        {
            con.prepareStatement(updateStatement).executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error actualizando datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
    }
}
