package com.cristianvalero.chat.servidor.utils;

public enum ClientRank
{
    USER(1, 50),
    VIP(2, 100),
    MODERATOR(3, 150),
    ADMINISTRATOR(4, 200);

    private int identifier;
    private int value;

    ClientRank(int id, int value)
    {
        this.identifier = id;
        this.value = value;
    }

    public int getId()
    {
        return this.identifier;
    }

    public int getValue()
    {
        return this.value;
    }

    public boolean isSuperiorThan(ClientRank r)
    {
        boolean a = false;
        if (this.value > r.getValue()) a = true;
        return a;
    }

    public static ClientRank getRankWithId(int id)
    {
        ClientRank cr = null;
        for (ClientRank r : values())
        {
            if (r.getId() == id) cr = r;
        }
        return cr;
    }

    public static ClientRank getRankWithValue(int value)
    {
        ClientRank cr = null;
        for (ClientRank r : values())
        {
            if (r.getValue() == value) cr = r;
        }
        return cr;
    }
}
