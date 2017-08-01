package com.cristianvalero.chat.servidor.utils;

public enum LogType
{
    EMPTY ("[]"),
    WARNING ("[WARNING]"),
    ERROR ("[ALERT/SEVERE]"),
    MYSQL ("[MYSQL]"),
    INFO ("[INFO]");

    private String prefix;

    LogType(String prefix)
    {
        this.prefix = prefix;
    }

    public String getPrefix()
    {
        return this.prefix;
    }
}
