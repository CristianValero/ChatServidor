package com.cristianvalero.chat.servidor.database;
import com.sun.istack.internal.NotNull;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collection;

public class Facade extends DAO
{
    public static String encryptPassword(@NotNull String normalPassword) //Your password is safe ;)
    {
        return DigestUtils.sha1Hex(DigestUtils.md5Hex(normalPassword));
    }

    public static Collection<ClientData> getAllUsers() //Get temp Collection of ClientDats from Database
    {
        return Facade.getAllUsersDatabase();
    }

    public static boolean equalsPasswd(String email, String pass) //Check if X password is correct from X user
    {
        return Facade.equalsPassword(email, pass);
    }

    public static boolean nicknameExists(String nick)
    {
        return Facade.checkNickname(nick);
    }

    public static boolean emailExists(String email)
    {
        return Facade.checkEmail(email);
    }

    public static void removeUserFromDatabase(String email)
    {
        Facade.removeUser(email);
    }

    public static void registerUserToDatabase(String name, String email, String passwd, String ip)
    {
        Facade.registerUser(name, email, passwd, ip);
    }
}
