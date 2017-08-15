package com.cristianvalero.chat.servidor.process;

import com.cristianvalero.chat.servidor.database.Facade;
import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.LogType;
import com.cristianvalero.chat.servidor.utils.ServerMessages;

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
    private boolean accesGranted = true;

    Client(Socket socket, int ID)
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

        if (accesGranted)
        {
            this.sendUTF(ServerMessages.ACCES_GRANTED);

            while (!socket.isClosed())
            {
                switch (getServerMessage())
                {
                    case LOGIN_ATTEMPT:
                        break;
                    case REGISTER_ATTEMPT:
                        break;
                    case SEND_MESSAGE:
                        break;
                    case SEND_COMMAND:
                        break;
                    default:
                        break;
                }
            }
        }
        else
        {
            this.sendUTF(ServerMessages.ACCES_DENIED);
            pause(10);
            this.close();
        }
    }

    private void pause(int seconds)
    {
        try
        {
            sleep(seconds*1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();

            ejcServer.log("Ha ocurrido un error pausando un hilo (Client-Thread)...", LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }

    private void checkClient()
    {
        ejcServer.log("Iniciando comprobación de seguirdad para el cliente: '"+atribute+"'...", LogType.INFO);

        for (String deniedAdress : Facade.getDeniedClientAdress())
        {
            if (socket.getRemoteSocketAddress().toString().equals(deniedAdress))
            {
                ejcServer.log("El cliente "+atribute+" utiliza una dirección IP que está en blacklist.", LogType.WARNING);
                ejcServer.log("Se ha denegado el acceso al cliente "+atribute, LogType.WARNING);
                accesGranted = false;
                break;
            }
        }
    }

    public void close()
    {
        try
        {
            socket.close();
            this.interrupt();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error al desconectar el cliente: "+atribute, LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }

    public int getClientID()
    {
        return CLIENT_ID;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public String getAtribute()
    {
        return atribute;
    }

    public ServerMessages getServerMessage()
    {
        ServerMessages sv = null;
        try {
            final String msg = dis.readUTF();
            sv = ServerMessages.getServerMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sv;
    }

    public void sendUTF(ServerMessages a)
    {
        try {
            dos.writeUTF(a.getMessage());
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInt(int a)
    {
        try {
            dos.writeInt(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
