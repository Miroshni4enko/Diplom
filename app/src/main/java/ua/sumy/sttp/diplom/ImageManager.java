package ua.sumy.sttp.diplom;

/**
 * Created by Слава on 21.06.2016.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageManager {
    private final static String TAG = "ImageManager";
    private static Bitmap image;
    static Bitmap bitmap = null;
    static HttpURLConnection conn = null;
    static BufferedInputStream buf_stream = null;

    /**
     * Private constructor prevents instantiation from other classes
     */
    private ImageManager() {
    }

    public static void fetchImage(final String iUrl, final ImageView iView) {
        if (iUrl == null || iView == null)
            return;

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                final Bitmap image = (Bitmap) message.obj;
                iView.setImageBitmap(image);
            }
        };

        final Thread thread = new Thread() {
            @Override
            public void run() {
                final Bitmap image = downloadImage(iUrl);
                if (image != null) {
                    Log.v(TAG, "Got image by URL: " + iUrl);
                    final Message message = handler.obtainMessage(1, image);
                    handler.sendMessage(message);
                }
            }
        };
        iView.setImageResource(R.drawable.ic_menu_camera);
        thread.setPriority(3);
        thread.start();
    }

    public static ImageView dowloadImageWithThread(final String iUrl, final ImageView iView) {
        if (iUrl == null || iView == null)
            return null;

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                final Bitmap image = (Bitmap) message.obj;
                iView.setImageBitmap(image);
            }
        };

        final Thread thread = new Thread() {
            @Override
            public void run() {
                final Bitmap image = downloadImage(iUrl);
                if (image != null) {
                    Log.v(TAG, "Got image by URL: " + iUrl);
                    final Message message = handler.obtainMessage(1, image);
                    handler.sendMessage(message);
                }
            }
        };
        iView.setImageResource(R.drawable.ic_menu_camera);
        thread.setPriority(3);
        thread.start();
        return iView;
    }

    public static Bitmap downloadImage(final String iUrl) {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.v(TAG, "Starting loading image by URL: " + iUrl);
                    conn = (HttpURLConnection) new URL(iUrl).openConnection();
                    conn.setDoInput(true);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.connect();
                    buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
                    bitmap = BitmapFactory.decodeStream(buf_stream);
                    buf_stream.close();
                    conn.disconnect();
                    buf_stream = null;
                    conn = null;
                } catch (MalformedURLException ex) {
                    Log.e(TAG, "Url parsing was failed: " + iUrl);
                } catch (IOException ex) {
                    Log.d(TAG, iUrl + " does not exists");
                } catch (OutOfMemoryError e) {
                    Log.w(TAG, "Out of memory!!!");
                    return;
                } finally {
                    if (buf_stream != null)
                        try {
                            buf_stream.close();
                        } catch (IOException ex) {
                        }
                    if (conn != null)
                        conn.disconnect();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}