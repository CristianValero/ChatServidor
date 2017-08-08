package com.cristianvalero.chat.servidor.process;

import java.net.Socket;

public class Client extends Thread
{
    private Socket socket;
    private int CLIENT_ID;

    public Client(Socket socket, int ID)
    {
        this.socket = socket;
        this.CLIENT_ID = ID;
    }

    @Override
    public void run()
    {

    }
}
