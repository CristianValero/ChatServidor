package com.cristianvalero.chat.servidor;

import com.cristianvalero.chat.servidor.database.Database;
import com.cristianvalero.chat.servidor.database.Facade;
import com.cristianvalero.chat.servidor.process.Server;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ejcServer
{
    private static final float version = 1.0F;
    private static int ACT_LOG_LINES = 0;
    private static int ACT_LOG_ID = 1;
    private static final String NORMALLY_DATABASE_NAME = "chat_server";

    public static void main(String[] args) throws InterruptedException, SQLException, URISyntaxException
    {
        securityOnLoad();
        infoLoad();
        ejcServer.log("Iniciando servidor...", LogType.INFO);
        iniServer();
        ejcServer.log("Iniciando conexión a MySQL...", LogType.MYSQL);
        dataBaseNormal();
    }

    public static void securityOnLoad()
    {
        BufferedReader br = null;
        try
        {
            URL whatismyip = new URL("http://checkip.amazonaws.com"); //Esta web devuelve una línea en blanco con la dirección IP pública del solicitante.
            br = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            final String IP = br.readLine();

            ejcServer.log("El servidor está corriendo en la IP: "+IP+Server.getServerRunningPort(), LogType.INFO);

            for (String allowedRunAdress : Facade.getAllowedRunServerAdress())
            {
                if (IP.equals(allowedRunAdress))
                {
                    ejcServer.log("¡EL SERVIDOR ESTÁ CORRIENDO EN UNA DIRECCIÓN IP NO PERMITIDA!", LogType.ERROR);
                    ejcServer.log("Se ha registrado la incidencia. Ahora el servidor se apagará.", LogType.ERROR);
                    System.exit(-1); //System.exit(-1) Significa salida forzosa por error.
                    break;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("¡Ha ocurrido un problema con la lectura de una URL!", LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
        finally
        {
            try { br.close(); }
            catch (IOException e)
            {
                e.printStackTrace();
                ejcServer.log("No se ha podido cerrar la lectura de datos (BufferedReader):", LogType.SERVER_ERROR);
                ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
            }
        }
    }

    private static void infoLoad() throws InterruptedException
    {
        ejcServer.log("-----------------------------------------------------------------------------------", LogType.INFO);
        ejcServer.log("¡EL SERVIDOR (JAR) DEBE INICIARSE DENTRO DE UNA CARPETA PARA MEJOR RENDIMIENTO!", LogType.WARNING);
        ejcServer.log("El log funciona en sistema de hora PM: "+version, LogType.INFO);
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
        ejcServer.log("Preparando para apagar el servidor...", LogType.INFO);
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
                ejcServer.log("Ha surgido un problema al cerrar la conexión con la base de datos '"+cons.getDatabase()+"'.", LogType.MYSQL_ERROR);
                ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
            }
        }
    }

    private static void dataBaseNormal()
    {
        try
        {
            Database db = new Database("localhost", NORMALLY_DATABASE_NAME, "root", "", 3306);
            db.tryConnection();
            checkNormalDatabaseTables(db);
            Database.addToList(db);
        }
        catch (ClassNotFoundException | SQLException e)
        {
            ejcServer.log("Ha ocurrido un error al inicializar la base de datos básica: ", LogType.MYSQL);
            ejcServer.log(" - "+e.getMessage(), LogType.MYSQL_ERROR);
            e.printStackTrace();
        }
    }

    private static void checkNormalDatabaseTables(Database db) throws SQLException //El email es un dato que JAMÁS debe variar por motivos de seguridad.
    {
        ejcServer.log("Iniciando comprobación de tablas en la base de datos '"+db.getDatabase()+"'.", LogType.MYSQL);
        List<String> orders = new ArrayList<String>();

        orders.add("CREATE TABLE IF NOT EXISTS usuarios ( id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "email VARCHAR(150), ip VARCHAR(50), nick VARCHAR(150), passwd VARCHAR(150) ) Engine=InnoDB;");
        orders.add("CREATE TABLE IF NOT EXISTS estads ( id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "email VARCHAR(150), mensajes_enviados INT, veces_logueado INT, rango INT ) Engine=InnoDB;");
        orders.add("CREATE TABLE IF NOT EXISTS messages (id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "send_by_email VARCHAR (150), message LONGTEXT, hour TIME, day DATE ) Engine=InnoDB;");
        orders.add("CREATE TABLE IF NOT EXISTS banList (id INT PRIMARY KEY AUTO_INCREMENT, " +
                   "banned_email VARCHAR (150), banned_ip VARCHAR(150), banned_by_email VARCHAR(150), banMessage TEXT, hour TIME, " +
                   "day DATE ) Engine=InnoDB;");

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

    //Método encargado de registrar todo aquello que ocurra en el servidor
    public static void log(String txt, LogType type)
    {
        Calendar cal = Calendar.getInstance();
        String generated = type.getPrefix()+" ["+cal.get(Calendar.HOUR)+":"+
                           cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+"] "+txt;
        try
        {
            //Obtenemos la ruta donde se está ejecutando nuestro programa (OJO, MEJOR QUE SE EJECUTE DENTRO DE UNA CARPETA)
            final String programPath = new File(ejcServer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
            //Le damos una ruta a el constructor file con el nombre de nuestra carpeta para log, en este caso la fecha actual
            File logPath = new File(programPath+"//logs//"+(cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)));
            if (!logPath.exists())
                logPath.mkdirs(); //Si nuestra carpeta que almacenará los logs no existe, la creamos ;)
            //Ahora creamos una nueva ruta con el nombre del log correspondiente
            final String actLogPath = programPath+"//logs//"+(cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR))+"//"+ACT_LOG_ID+".txt"; //
            BufferedWriter writer = new BufferedWriter(new FileWriter(actLogPath,true));
            writer.write(generated+"\n"); //Escribimos el log
            writer.close();
            if (ACT_LOG_LINES > 1000)
            {
                //Si el log tiene más de 1000 lineas, creamos otro log (1, 2, 3...) y ponemos las lineas actuales a 0
                ACT_LOG_ID++;
                ACT_LOG_LINES = 0;
            }
            System.out.println(generated); //[LogType] [HH:MM:SS] Log message....
        }
        catch (URISyntaxException | IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha surgido un error escribiendo el registro (log)", LogType.ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.ERROR);
        }
        ACT_LOG_LINES++; //Incrementamos las lineas escritas del log.
    }

    public static String getNormallyDatabaseName() {
        return NORMALLY_DATABASE_NAME;
    }
}
