package ru.net.serbis.mi4c.lockwall;

import java.io.*;

public class Shell
{
    public void command(String... commands)
    {
        OutputStreamWriter output = null;
        BufferedReader input = null;
        BufferedReader error = null;
        try
        {
            Process process = Runtime.getRuntime().exec("su");
            output = new OutputStreamWriter(process.getOutputStream());
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            for (String command : commands)
            {
                Log.info(this, "command:" + command);
                output.write(command + "\n");
                output.flush();
            }

            output.write("exit\n");
            output.flush();
            process.waitFor();

            Log.info(this, input, "input");
            Log.info(this, error, "error");
        }
        catch (Throwable e)
        {
            Log.info(this, "Error on command", e);
        }
        finally
        {
            Utils.close(input);
            Utils.close(error);
            Utils.close(output);
        }
    }
}
