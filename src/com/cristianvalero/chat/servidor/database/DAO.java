package com.cristianvalero.chat.servidor.database;

import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.ClientRank;
import com.cristianvalero.chat.servidor.utils.LogType;
import com.sun.istack.internal.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

public class DAO //Data access object
{
    protected static Collection<ClientData> getAllUsersDatabase()
    {
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());
        Vector<ClientData> v = new Vector<>();

        final String queryUsuarios = "SELECT * FROM usuarios;";
        final String queryDataUsuarios = "SELECT * FROM estads;";

        try
        {
            ResultSet resQueryUsuarios = con.prepareStatement(queryUsuarios).executeQuery();
            ResultSet resQueryDataUsuarios = con.prepareStatement(queryDataUsuarios).executeQuery();

            while (resQueryUsuarios.next() && resQueryDataUsuarios.next())
            {
                ClientData cd = new ClientData();
                cd.setName(resQueryUsuarios.getString("name"));
                cd.setEmail(resQueryUsuarios.getString("email"));
                cd.setIp(resQueryUsuarios.getString("ip"));
                cd.setPasswd(resQueryUsuarios.getString("passwd"));
                cd.setMessagesSent(resQueryDataUsuarios.getInt("mensajesEnviados"));
                cd.setTimesLoggedIn(resQueryDataUsuarios.getInt("vecesLogueado"));
                cd.setRank(ClientRank.getRankWithId(resQueryDataUsuarios.getInt("rango")));
                v.add(cd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error obteniendo datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
        return v;
    }

    protected static boolean equalsPassword(@NotNull String email, @NotNull String pass)
    {
        boolean a = false;
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String queryPasswd = "SELECT passwd FROM usuarios WHERE email = '"+email+"';";

        try
        {
            ResultSet resQueryPasswd = con.prepareStatement(queryPasswd).executeQuery();
            if (resQueryPasswd.next())
                if (resQueryPasswd.getString("passwd").equals(pass))
                    a = true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error obteniendo datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }
        return a;
    }

    protected static boolean checkNickname(@NotNull String name)
    {
        boolean a = false;
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String queryNick = "SELECT name FROM usuarios;";

        try
        {
            ResultSet resQueryNick = con.prepareStatement(queryNick).executeQuery();
            while (resQueryNick.next())
                if (resQueryNick.getString("name").equals(name))
                    a = true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error obteniendo datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }

        return a;
    }

    protected static boolean checkEmail(@NotNull String email)
    {
        boolean a = false;
        Connection con = Database.getConnection(ejcServer.getNormallyDatabaseName());

        final String queryEmail = "SELECT email FROM usuarios;";

        try
        {
            ResultSet resQueryEmail = con.prepareStatement(queryEmail).executeQuery();
            while (resQueryEmail.next())
                if (resQueryEmail.getString("email").equals(email))
                    a = true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error obteniendo datos de usuarios en la base de datos '"+ejcServer.getNormallyDatabaseName()+"'.", LogType.MYSQL_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
        }

        return a;
    }
}
