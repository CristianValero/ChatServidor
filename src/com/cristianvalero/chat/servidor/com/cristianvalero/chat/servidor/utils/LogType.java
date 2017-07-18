package com.cristianvalero.chat.servidor.com.cristianvalero.chat.servidor.utils;

public enum LogType
{
    NORMALLY("[]"),
    WARNING("[WARNING]"),
    ERROR("[ALERT/SEVERE]");

    String prefix;

    LogType(String prefix)
    {
        this.prefix = prefix;
    }

    public String getPrefix()
    {
        return this.prefix;
    }
}
