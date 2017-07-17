package com.cristianvalero.chat.servidor;

import com.cristianvalero.chat.servidor.com.cristianvalero.chat.servidor.utils.LogType;

import java.util.Calendar;

public class ejcServer
{
    public static void main(String[] args)
    {

    }

    public static void log(String txt, LogType type) //No pueden ser nulos!!
    {
        Calendar cal = Calendar.getInstance();
        System.out.print(type.getPrefix()+" ["+cal.get(Calendar.HOUR)+":"+
                         cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+"] "+txt); //[LogType] [HH:MM:SS] Log message....
    }
}
