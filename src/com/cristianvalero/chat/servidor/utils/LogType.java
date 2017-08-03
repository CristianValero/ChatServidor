package com.cristianvalero.chat.servidor.utils;

public enum LogType
{
    EMPTY ("[]"),
    WARNING ("[WARNING]"),
    ERROR ("[ERROR]"),
    MYSQL ("[MYSQL]"),
    MYSQL_ERROR("[MYSQL/ERROR]"),
    SERVER("[SERVER]"),
    SERVER_ERROR("[SERVER/ERROR]"),
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
