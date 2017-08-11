package com.cristianvalero.chat.servidor.process;

import com.cristianvalero.chat.servidor.database.Facade;
import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.LogType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread
{
    private Socket socket = null;
    private int CLIENT_ID;

    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    private String atribute = null;

    public Client(Socket socket, int ID)
    {
        this.socket = socket;
        this.CLIENT_ID = ID;

        try
        {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            atribute = ID+"/"+socket.getRemoteSocketAddress().toString();

            ejcServer.log("Se ha instanciado correctamente el cliente: "+atribute, LogType.SERVER_ERROR);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error de instancia (Client-Thread)...", LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }

    @Override
    public void run()
    {
        checkClient();

        while (!socket.isClosed())
        {

        }
    }
    //Sería mejor si... Mandamos antes un mensaje de desconexión para que el cliente pueda procesar la solicitud y no arrojar un error.
    private void checkClient() //Esto será temporal... No quiero que sea programado de esta forma
    {
        ejcServer.log("Iniciando comprobación de seguirdad para el cliente: '"+atribute+"'...", LogType.INFO);

        for (String deniedAdress : Facade.getDeniedClientAdress())
        {
            if (socket.getRemoteSocketAddress().toString().equals(deniedAdress))
            {
                ejcServer.log("El cliente "+atribute+" utiliza una dirección IP que está en blacklist.", LogType.WARNING);
                this.close();
                ejcServer.log("Se ha denegado el acceso al cliente "+atribute, LogType.WARNING);

                break;
            }
        }
    }

    public void close()
    {
        try
        {
            socket.close(); //Cerramos la conexión socket
            this.interrupt(); //Interrumpimos la ejecución del hilo que contiene el cliente
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error al desconectar el cliente: "+atribute, LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }
}
