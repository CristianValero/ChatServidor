package com.cristianvalero.chat.servidor.com.cristianvalero.chat.servidor.utils;

public enum LogType
{
<<<<<<< HEAD
    NORMALLY ("[]"),
    WARNING ("[WARNING]"),
    ERROR ("[ALERT/SEVERE]");

    String prefix;

    LogType(String prefix)
    {
        this.prefix = prefix;
=======
    NORMALLY ("[]", 0),
    WARNING ("[WARNING]", 50),
    ERROR ("[ALERT/SEVERE]", 100);

    private String prefix;
    private int value;

    LogType(String prefix, int value)
    {
        this.prefix = prefix;
        this.value = value;
>>>>>>> 18e07c3... New updates
    }

    public String getPrefix()
    {
        return this.prefix;
    }
<<<<<<< HEAD
=======

    public int getValue()
    {
        return this.value;
    }
>>>>>>> 18e07c3... New updates
}
