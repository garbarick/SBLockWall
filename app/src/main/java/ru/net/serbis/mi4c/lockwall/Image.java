package ru.net.serbis.mi4c.lockwall;

import android.app.Activity;
import android.graphics.*;
import android.view.Display;

import java.io.File;
import java.io.FileOutputStream;

public class Image
{
    public File resize(Activity context, File source)
    {
        Bitmap input = null;
        Bitmap output = null;
        try
        {
            input = load(source);
            if (input != null)
            {
                Rect inputRect = new Rect(0, 0, input.getWidth(), input.getHeight());
                Rect outputRect = getRect(context, inputRect);

                output = Bitmap.createBitmap(outputRect.width(), outputRect.height(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                canvas.drawColor(Color.BLACK);
                Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
                canvas.drawBitmap(input, outputRect.left, outputRect.top, paint);

                File result = context.getFileStreamPath("image.png");
                if (save(output, result))
                {
                    return result;
                }
            }
        }
        catch (Throwable e)
        {
            Log.info(this, "Error on resize", e);
        }
        finally
        {
            recycle(input);
            recycle(output);
        }

        return source;
    }

    private Bitmap load(File source)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        return BitmapFactory.decodeFile(source.getAbsolutePath(), options);
    }

    private void recycle(Bitmap bitmap)
    {
        if (bitmap != null)
        {
            bitmap.recycle();
        }
    }

    private Rect getRect(Activity context, Rect source)
    {
        Display display = context.getWindowManager().getDefaultDisplay();
        int sW = display.getWidth();
        int sH = display.getHeight();
        int w = source.width();
        int h = source.height();
        float cW = ((float) sW / sH) * ((float) h / w);
        float cH = ((float) sH / sW) * ((float) w / h);

        int width = (int) (cW < cH ? w * cW : w);
        int height = (int) (cH < cW ? h * cH : h);
        int left = (width - w) / 2;
        int top = (height - h) / 2;
        int right = left + width;
        int bottom = top + height;

        return new Rect(left, top, right, bottom);
    }

    private boolean save(Bitmap image, File file)
    {
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            return true;
        }
        catch (Throwable e)
        {
            Log.info(this, "Error on save", e);
            return false;
        }
        finally
        {
            Utils.close(out);
        }
    }
}
