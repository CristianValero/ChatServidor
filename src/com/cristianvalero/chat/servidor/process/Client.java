package com.cristianvalero.chat.servidor.process;

import com.cristianvalero.chat.servidor.database.ClientData;
import com.cristianvalero.chat.servidor.database.Facade;
import com.cristianvalero.chat.servidor.ejcServer;
import com.cristianvalero.chat.servidor.utils.LogType;
import com.cristianvalero.chat.servidor.utils.ServerMessages;
import com.sun.istack.internal.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Client extends Thread
{
    private Socket socket = null;
    private int CLIENT_ID;

    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    private String atribute = null;
    private boolean accesGranted = true;
    private boolean isLogged = false;

    private ClientData clientData = null;

    Client(Socket socket, int ID) //Constructor... Hacemos alguna cosilla que otra.
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
    public void run() //Proceso principal
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
                        login();
                        break;
                    case REGISTER_ATTEMPT:
                        register();
                        break;
                    case SEND_MESSAGE:
                        reciveMessage();
                        break;
                    case SEND_COMMAND: //Para futuras implementaciones
                        //reciveCommand();
                        break;
                    default:
                        unknownCommand();
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
            sleep(seconds * 1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();

            ejcServer.log("Ha ocurrido un error pausando un hilo (Client-Thread)...", LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }

    private void login()
    {
        final String recivedEmail = getUTF();
        final String recivedPasswd = getUTF();

        if (Facade.emailExists(recivedEmail))
            if (Facade.equalsPasswd(recivedEmail, recivedPasswd))
            {
                sendUTF(ServerMessages.CORRECT_PASSWORD);
                isLogged = true;

                this.clientData = Facade.getUser(recivedEmail); //Instanciamos la variable clientdata
                this.clientData.setHilo(this); //Le asignamos su hilo en ejecución
                ClientData.addClientData(this.clientData); //Lo añadimos al hashmap contenedor de clientdats.
            }
        else
            sendUTF(ServerMessages.WRONG_EMAIL);
    }

    private void register() //Luego del registro, el cliente debe pasar al LoginWindow
    {
        final String recivedEmail = getUTF();
        final String recivedPasswd = getUTF();
        final String recivedNickname = getUTF();

        if (!Facade.emailExists(recivedEmail)) //Si el email no está en uso
        {
            if (!Facade.nicknameExists(recivedNickname)) //Si el nickname no está en uso
            {
                Facade.registerUserToDatabase(recivedNickname, recivedEmail, recivedPasswd, socket.getRemoteSocketAddress().toString()); //Lo registramos
                sendUTF(ServerMessages.REGISTER_CORRECT); //Mandamos el OKEY
                ejcServer.log("Se ha registrado un usuario correctamente con el email: "+recivedEmail, LogType.INFO); //Guardamos un registro
            }
            else
                sendUTF(ServerMessages.NICKNAME_ON_USE);
        }
        else
            sendUTF(ServerMessages.EMAIL_ON_USE);
    }

    private void reciveMessage()
    {
        final String recivedMsg = getUTF();
        final String msgFormat = this.clientData.getName()+": "+recivedMsg;
        Facade.registerMessage(recivedMsg, this.clientData.getEmail());
        broadcastMessage(ServerMessages.SEND_MESSAGE, msgFormat);
    }

    private void unknownCommand()
    {
        ejcServer.log("Se han recibido datos sin cabecera imposibles de procesar del cliente: "+atribute, LogType.ERROR);
        ejcServer.log("Dato corrupto del cliente ("+atribute+"): "+getUTF(), LogType.ERROR);
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

    private void close()
    {
        try
        {
            socket.close();
            ClientData.removeClientData(this.clientData.getEmail());
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

    private ServerMessages getServerMessage()
    {
        ServerMessages sv = null;
        try
        {
            final String msg = dis.readUTF();
            sv = ServerMessages.getServerMessage(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error al recibir datos del cliente: "+atribute, LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
        return sv;
    }

    private String getUTF()
    {
        String get = null;
        try
        {
            get = dis.readUTF();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error al recibir datos del cliente: "+atribute, LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
        return get;
    }

    private void sendUTF(ServerMessages a)
    {
        try
        {
            dos.writeUTF(a.getMessage());
            dos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error al recibir datos del cliente: "+atribute, LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }

    private void sendNormalUTF(String txt)
    {
        try
        {
            dos.writeUTF(txt);
            dos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ejcServer.log("Ha ocurrido un error al recibir datos del cliente: "+atribute, LogType.SERVER_ERROR);
            ejcServer.log(" - "+e.getMessage(), LogType.SERVER_ERROR);
        }
    }

    public static void broadcastMessage(@NotNull ServerMessages svmsgs, @NotNull String txt)
    {
        for (Map.Entry<String, ClientData> entry : ClientData.getClientsData().entrySet())
        {
            entry.getValue().getHilo().sendUTF(svmsgs);
            entry.getValue().getHilo().sendNormalUTF(txt);
        }
    }
}
