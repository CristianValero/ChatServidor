package com.cristianvalero.chat.servidor.database;

import java.util.HashMap;
import java.util.Map;

public class ClientData
{
    private static HashMap<String, ClientData> clientsData = new HashMap<>();

    private String name = "";
    private String email = "";
    private String ip = "";
    private int messagesSent = 0;
    private int timesLoggedIn = 0;

    public ClientData(String name, String email, String ip, int messagesSent, int timesLoggedIn)
    {
        this.name = name;
        this.email = email;
        this.ip = ip;
        this.messagesSent = messagesSent;
        this.timesLoggedIn = timesLoggedIn;
    }

    public static void addClientData(ClientData cd) {
        if (clientsData.containsKey(cd.getName()))
            clientsData.put(cd.getName(), cd);
    }

    public static void removeClientData(String name) {
        clientsData.remove(name);
    }

    public static ClientData getClientData(String name) {
        ClientData clientData = null;
        for (Map.Entry<String, ClientData> entry : clientsData.entrySet())
            if (entry.getKey().equals(name)) clientData = entry.getValue();
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
}
