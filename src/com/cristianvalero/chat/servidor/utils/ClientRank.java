package com.cristianvalero.chat.servidor.utils;

import com.sun.istack.internal.NotNull;

public enum ClientRank
{
    USER(1, 50, ""),
    VIP(2, 100, "[VIP]"),
    MODERATOR(3, 150, "[MODERADOR]"),
    ADMINISTRATOR(4, 200, "[ADMINISTRADOR]");

    private int identifier;
    private int value;
    private String prefix;

    ClientRank(int id, int value, String prefix)
    {
        this.identifier = id;
        this.value = value;
        this.prefix = prefix;
    }

    public int getId()
    {
        return this.identifier;
    }

    public int getValue()
    {
        return this.value;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean isSuperiorThan(ClientRank r)
    {
        boolean a = false;
        if (this.value > r.getValue())
            a = true;
        return a;
    }

    public static ClientRank getRankWithId(int id)
    {
        ClientRank cr = null;
        for (ClientRank r : values())
            if (r.getId() == id) cr = r;
        return cr;
    }

    public static ClientRank getRankWithValue(int value)
    {
        ClientRank cr = null;
        for (ClientRank r : values())
            if (r.getValue() == value) cr = r;
        return cr;
    }

    public static ClientRank getSuperiorRank(@NotNull ClientRank a, @NotNull ClientRank b)
    {
        ClientRank r = null;
        return r = (a.getValue() > b.getValue()) ? a : b; //resultado = (condicion) ? true : false
    }
}
