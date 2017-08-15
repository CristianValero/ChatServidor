package com.cristianvalero.chat.servidor.database;

import com.cristianvalero.chat.servidor.utils.ClientRank;

import java.util.HashMap;
import java.util.Map;

public class ClientData
{
    private static HashMap<String, ClientData> clientsData = new HashMap<>(); //<email, ClientData>

    private String name = "";
    private String email = "";
    private String passwd = "";
    private String ip = "";

    private int messagesSent = 0;
    private int timesLoggedIn = 0;

    private ClientRank rank = null;
    private Thread hilo = null;

    public ClientData(String name, String email, String ip, int messagesSent, int timesLoggedIn)
    {
        this.name = name;
        this.email = email;
        this.ip = ip;
        this.messagesSent = messagesSent;
        this.timesLoggedIn = timesLoggedIn;
    }

    public ClientData() {}

    public static void addClientData(ClientData cd) {
        if (clientsData.containsKey(cd.getName()))
            clientsData.put(cd.getName(), cd);
    }

    public static void removeClientData(String email) {
        clientsData.remove(email);
    }

    public static ClientData getClientData(String email) {
        ClientData clientData = null;
        for (Map.Entry<String, ClientData> entry : clientsData.entrySet())
            if (entry.getKey().equals(email)) clientData = entry.getValue();
        return clientData;
    }

    public static HashMap<String, ClientData> getClientsData() {
        return clientsData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(int messagesSent) {
        this.messagesSent = messagesSent;
    }

    public int getTimesLoggedIn() {
        return timesLoggedIn;
    }

    public void setTimesLoggedIn(int timesLoggedIn) {
        this.timesLoggedIn = timesLoggedIn;
    }

    public void setRank(ClientRank r) {
        this.rank = r;
    }

    public ClientRank getRank() {
        return this.rank;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Thread getHilo() {
        return hilo;
    }

    public void setHilo(Thread hilo) {
        this.hilo = hilo;
    }
}
