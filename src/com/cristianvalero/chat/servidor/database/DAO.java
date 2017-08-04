package com.cristianvalero.chat.servidor.database;

import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.ClientRank;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

public class DAO //Data access object
{
    public static Collection<ClientData> getAllUsersDatabase()
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
}
