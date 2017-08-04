package com.cristianvalero.chat.servidor.database;
import com.sun.istack.internal.NotNull;
import org.apache.commons.codec.digest.DigestUtils;

public class Facade
{
    public static String encryptPassword(@NotNull String normalPassword) //Your password is safe ;)
    {
        return DigestUtils.sha1Hex(DigestUtils.md5Hex(normalPassword));
    }


}
