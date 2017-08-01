package com.cristianvalero.chat.servidor;

import com.cristianvalero.chat.servidor.database.Database;
import com.cristianvalero.chat.servidor.process.Server;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ejcServer
{
    private static final float version = 1.0F;
    private static int ACT_LOG_LINES = 0;
    private static int ACT_LOG_ID = 1;

    public static void main(String[] args) throws InterruptedException, SQLException, URISyntaxException {
        infoLoad();
        ejcServer.log("Iniciando servidor...", LogType.INFO);
        iniServer();
        ejcServer.log("Iniciando conexión a MySQL...", LogType.MYSQL);
        //dataBaseBasicTables(new Database("localhost", "pepito", "root", "", 3306));
        //dataBaseBasic();
    }

    private static void infoLoad() throws InterruptedException
    {
        ejcServer.log("-----------------------------------------------------------------------------------", LogType.INFO);
        ejcServer.log("¡EL SERVIDOR (JAR) DEBE INICIARSE DENTRO DE UNA CARPETA PARA MEJOR RENDIMIENTO!", LogType.WARNING);
        ejcServer.log("Versión del servidor: "+version, LogType.INFO);
        ejcServer.log("Desarrollado por: Cristian Valero Abundio", LogType.INFO);
        ejcServer.log("Twitter: @titianvalero", LogType.INFO);
        ejcServer.log("-----------------------------------------------------------------------------------", LogType.INFO);
        Thread.sleep(2000);
        ejcServer.log("Por su seguridad, el servidor iniciará en 10 segundos...", LogType.INFO);
        for (int i=9; i>0; i--)
        {
            ejcServer.log("El servidor iniciará en "+i+" segundos.", LogType.INFO);
            Thread.sleep(1000);
        }
    }

    public static void stopServer() //Aquí debemos terminar todas las conexiones, procesos, hilos, servidores...
    {
        ejcServer.log("Preparando para apagar el servidor...", LogType.EMPTY);
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
            dataBaseBasicTables(db);
            Database.addToList(db);
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    private static void dataBaseBasicTables(Database db) throws SQLException
    {
        ejcServer.log("Iniciando comprobación de tablas en la base de datos '"+db.getDatabase()+"'.", LogType.MYSQL);
        List<String> orders = new ArrayList<String>();
        orders.add("CREATE TABLE IF NOT EXISTS usuarios ( id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "nombre VARCHAR(150), email VARCHAR(150), passwd VARCHAR(150) ) Engine=InnoDB;");
        orders.add("CREATE TABLE IF NOT EXISTS estads ( id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "nombre VARCHAR(150), mensajesEnviados INT, vecesLogueado INT ) Engine=InnoDB;");
        for (String order : orders)
        {
            db.getConnection().prepareStatement(order).execute();
            ejcServer.log("Iniciando comprobación de la tabla '"+order.substring((order.indexOf('S')+4), (order.indexOf('(')-1))+"' en la base de datos '"+db.getDatabase()+"'.", LogType.MYSQL);
        }
        orders.clear();
    }

    private static void iniServer()
    {
        Server sv = new Server();
        sv.start();
    }

    public static void log(String txt, LogType type)
    {
        Calendar cal = Calendar.getInstance();
        String generated = type.getPrefix()+" ["+cal.get(Calendar.HOUR)+":"+
                           cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+"] "+txt;
        System.out.println(generated); //[LogType] [HH:MM:SS] Log message....
        try
        {
            //Obtenemos la ruta donde se está ejecutando nuestro programa
            final String programPath = new File(ejcServer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
            //Le damos una ruta a el constructor file con el nombre de nuestra carpeta para log, en este caso la fecha actual
            File logPath = new File(programPath+"//logs//"+(cal.get(Calendar.DATE)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.YEAR)));
            if (!logPath.exists())
                logPath.mkdirs(); //Si nuestra carpeta que almacenará los logs no existe, la creamos ;)
            //Ahora creamos una nueva ruta con el nombre del log correspondiente
            final String actLogPath = programPath+"//logs//"+(cal.get(Calendar.DATE)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.YEAR))+"//"+ACT_LOG_ID+".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(actLogPath,true));
            writer.write(generated+"\n"); //Escribimos el log
            writer.close();
            if (ACT_LOG_LINES > 1000)
            {
                //Si el log tiene más de 1000 lineas, creamos otro log (1, 2, 3...) y ponemos las lineas actuales a 0
                ACT_LOG_ID++;
                ACT_LOG_LINES = 0;
            }
        }
        catch (URISyntaxException | IOException e) {e.printStackTrace();}
        ACT_LOG_LINES++; //Incrementamos las lineas escritas del log.
    }
}
