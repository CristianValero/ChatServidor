package com.cristianvalero.chat.servidor.utils;

public enum ServerMessages
{
    ACCES_GRANTED ("ACCES_GRANTED"),
    ACCES_DENIED ("ACCES_DENIED"),
    CORRECT_PASSWORD ("CORRECT_PASSWORD"),

    LOGIN_ATTEMPT ("LOGIN_ATTEMPT"),
    REGISTER_ATTEMPT ("REGISTER_ATTEMPT"),
    REGISTER_CORRECT ("REGISTER_CORRECT"),

    WRONG_PASSWORD ("WRONG_PASSWORD"),
    WRONG_EMAIL ("WRONG_EMAIL"),

    EMAIL_ON_USE ("EMAIL_ON_USE"),
    NICKNAME_ON_USE ("NICKNAME_ON_USE"),

    BAN_USER ("BAN_CLIENT"),

    BAN_COMMAND_NO_ARGS ("BAN_COMMAND_NO_ARGS"),

    SEND_MESSAGE ("SEND_MESSAGE"),
    SEND_COMMAND ("SEND_COMMAND");

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
