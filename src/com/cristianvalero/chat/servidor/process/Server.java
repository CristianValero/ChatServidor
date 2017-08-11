package com.cristianvalero.chat.servidor.process;

import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread
{
    private static int SESSION_ID = 0;
    private static final int PORT = 4450;
    private ServerSocket serverSocket = null;

    @Override
    public void run()
    {
        init();

        while (!serverSocket.isClosed())
        {
            try
            {
                Socket socket = serverSocket.accept(); //Aceptamos cualquier conexión que recibamos.
                SESSION_ID++;

                final String id = SESSION_ID+"/"+socket.getRemoteSocketAddress().toString();

                ejcServer.log("Se ha conectado un cliente: "+id, LogType.INFO);
                //Instanciamos un nuevo hilo con la conexión
                Client cliente = new Client(socket, SESSION_ID);
                cliente.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                ejcServer.log("Ha ocurrido un error al aceptar una solicitud para acceder al servidor:", LogType.SERVER_ERROR);
                ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
            }
        }
    }

    private void init()
    {
        try
        {
            serverSocket = new ServerSocket(PORT);
            ejcServer.log("El servidor está encendido. Se encuentra disponible para recibir y procesar peticiones.", LogType.SERVER);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error al iniciar el servidor:", LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }

    public static int getServerRunningPort()
    {
        return Server.PORT;
    }
}
