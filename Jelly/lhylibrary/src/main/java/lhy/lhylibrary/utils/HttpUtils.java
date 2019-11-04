package lhy.lhylibrary.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lilaoda on 2016/9/1.
 */
public class HttpUtils {

    private HttpUtils( ) {
    }

    private Callback callback;
    public static final int TIME_OUT_MILLIS = 5000;

    public static void doGetAsync(final String urlPath, final Callback callback) {
        new Thread() {
            @Override
            public void run() {
                callback.callBack(doGet(urlPath));
            }
        }.start();
    }

    public static void doPostAsync(final String urlPath, final String param, final Callback callback) {
        new Thread() {
            @Override
            public void run() {
                callback.callBack( doPost(urlPath, param));
            }
        }.start();
    }

    public static String doGet(String urlPath) {
        String result = null;
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT_MILLIS);
            conn.setReadTimeout(TIME_OUT_MILLIS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.connect();
            int code = conn.getResponseCode();
            if (code == 200) {
                result = getStringFromStream(conn.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    private static String doPost(String urlPath, String param) {
        String result = null;
        URL url = null;
        HttpURLConnection conn = null;
        PrintStream printStream = null;
        try {
            url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT_MILLIS);
            conn.setReadTimeout(TIME_OUT_MILLIS);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            //post 以下必须要写
            conn.setDoInput(true);
            conn.setDoOutput(true);
            int code = conn.getResponseCode();
            if (code == 200) {
                printStream = new PrintStream(conn.getOutputStream());
                printStream.print(param);
                printStream.flush();
                result = getStringFromStream(conn.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (printStream != null) {
                printStream.close();
            }
        }
        return result;
    }

    public static String getStringFromStream(InputStream is) {

        String result = null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            result = os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


   public interface Callback {
        void callBack(String result);
    }
}
