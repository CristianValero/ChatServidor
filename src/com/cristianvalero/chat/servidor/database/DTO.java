package com.cristianvalero.chat.servidor.database;

import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

public class DTO //Data transfer object
{
    String a = "CREATE TABLE IF NOT EXISTS messages (id INT PRIMARY KEY AUTO_INCREMENT, " +
            "send_by_email VARCHAR (150), message LONGTEXT, hour TIME, day DATE ) Engine=InnoDB;";

    public static void registerMessage(String message, String senderEmail)
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
}
