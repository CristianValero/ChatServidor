package com.cristianvalero.chat.servidor.database;

import com.sun.istack.internal.NotNull;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;
import java.util.Collection;

public class Facade
{
    public static String encryptPassword(@NotNull String normalPassword) //Your password is safe ;)
    {
        return DigestUtils.sha1Hex(DigestUtils.md5Hex(normalPassword));
    }

    public static void registerMessage(String message, String senderEmail)
    {
        DTO.registerMessage(message, senderEmail);
    }

    public static String getBase64Encoder(String toEncode)
    {
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    public static String getBase64Decoder(String toDecode)
    {
        return new String(Base64.getDecoder().decode(toDecode.getBytes()));
    }

    public static Collection<ClientData> getAllUsers() //Get temp Collection of ClientDats from Database
    {
        return DAO.getAllUsersDatabase();
    }

    public static ClientData getUser(String email)
    {
        return DAO.getUserFromDatabase(email);
    }

    public static boolean equalsPasswd(String email, String pass) //Check if X password is correct from X user
    {
        return DAO.equalsPassword(email, pass);
    }

    public static boolean nicknameExists(String nick)
    {
        return DAO.checkNickname(nick);
    }

    public static boolean emailExists(String email)
    {
        return DAO.checkEmail(email);
    }

    public static void removeUserFromDatabase(String email)
    {
        DTO.removeUser(email);
    }

    public static void registerUserToDatabase(String name, String email, String passwd, String ip)
    {
        DTO.registerUser(name, email, passwd, ip);
    }

    public static void changeUserPassword(String email, String newPasswd)
    {
        DTO.changeUserPassword(email, newPasswd);
    }

    public static void changeUserNickname(String email, String newNickname)
    {
        DTO.changeUserNickname(email, newNickname);
    }

    public static void saveUserOnDatabase(ClientData cd)
    {
        DTO.saveChangesFromUser(cd);
    }

    public static Collection<String> getAllowedRunServerAdress()
    {
        return DAO.getAllowedRunServerAdress();
    }

    public static Collection<String> getDeniedClientAdress()
    {
        return DAO.getDeniedClientAdress();
    }
}
