package ru.net.serbis.mi4c.lockwall;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

public class Main extends Activity
{
    private static final String LOCK_WALL_PATH = "/system/media/theme/default/lock_wallpaper";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.info(this, "start");

        Intent intent = getIntent();
        Uri uri = intent.getData();

        File source = new File(uri.getPath());
        File target = new File(LOCK_WALL_PATH);
        source = new Image().resize(this, source);

        new Shell().command(
            "mount -o rw,remount,rw /system",
            "cp " + source.getAbsolutePath() + " " + target.getAbsolutePath(),
            "mount -o ro,remount,ro /system");

        Log.info(this, "finish");
        finish();
    }
}
