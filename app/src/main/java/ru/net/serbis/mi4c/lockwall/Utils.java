package ru.net.serbis.mi4c.lockwall;

import java.io.Closeable;

public class Utils
{
    public static void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Throwable ignored)
        {
        }
    }
}
