package com.cristianvalero.chat.servidor.utils;

public enum ServerMessages
{
    ACCES_GRANTED ("ACCES_GRANTED"),
    ACCES_DENIED ("ACCES_DENIED"),
    LOGIN_ATTEMPT ("LOGIN_ATEMPT"),
    REGISTER_ATTEMPT ("REGISTER_ATEMPT");

    private String message;

    ServerMessages(String msg)
    {
        this.message = msg;
    }

    public String getMessage()
    {
        return this.message;
    }

    @Override
    public String toString()
    {
        return this.message;
    }

    public static ServerMessages getServerMessage(String txt)
    {
        ServerMessages serverMessages = null;
        for (ServerMessages sv : ServerMessages.values())
        {
            if (sv.getMessage().equals(txt))
                serverMessages = sv;
        }
        return serverMessages;
    }
}
