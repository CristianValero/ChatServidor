package com.cristianvalero.chat.servidor;

import com.cristianvalero.chat.servidor.utils.LogType;

public class tmpTest
{
    public static void main(String[] args)
    {
        int i = (int) System.currentTimeMillis();
        ejcServer.log("Hola estoy redactando una línea del LOG", LogType.INFO);
        int f = (int) System.currentTimeMillis();
        int secs = f-i;
        System.out.println("El log ha tardado "+secs+" ms.");

        int in = (int) System.currentTimeMillis();
        System.out.println("Hola estoy mostrando un mensaje de consola.");
        int fn = (int) System.currentTimeMillis();
        int secsn = fn-in;
        System.out.print("El log ha tardado "+secsn+" segundos.");
    }
}
