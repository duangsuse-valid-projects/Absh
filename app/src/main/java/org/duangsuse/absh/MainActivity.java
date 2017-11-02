package org.duangsuse.absh;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import bsh.*;
import java.io.*;
import java.lang.reflect.*;

public class MainActivity extends Activity {
    public final Interpreter bsh = new Interpreter();
    public final String STACKTRACE_FILE = getFilesDir().toString() + "/stack.txt";
    public LinearLayout l = new LinearLayout(this);
    public final EditText t = new EditText(this);
    public final Button b = new Button(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            bsh.set("ctx", this.getApplicationContext());
            bsh.set("me", this);
            bsh.set("sh", bsh);
            bsh.DEBUG = true;
            bsh.TRACE = true;
            bsh.redirectOutputToFile(STACKTRACE_FILE);
        } catch (Throwable e) {
            printf(e.getMessage());
            e.printStackTrace();
        }
        t.setTextSize(13);
        t.setHint(
                "put your awesome code here┐(´-｀)┌\nlong press to get methods for returning object");
        l.setOrientation(LinearLayout.VERTICAL);
        l.addView(b);
        l.addView(t);
        b.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object o = null;
                        try {
                            o = bsh.eval(t.getText().toString());
                        } catch (Throwable e) {
                            e.printStackTrace();
                            printf(e.getMessage());
                        }
                        if (o != null) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(o.getClass().getName())
                                    .setMessage(o.toString())
                                    .show();
                            b.setText(o.getClass().getPackage().getName());
                        }
                    }
                });
        b.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        FileInputStream f = null;
                        String s = "";
                        try {
                            f = new FileInputStream(STACKTRACE_FILE);
                            s = inputStream2String(f);
                        } catch (Throwable e) {
                            printf(e.getMessage());
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("TraceBsh")
                                .setMessage(s)
                                .show();
                        return false;
                    }
                });
        t.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Object o = null;
                        try {
                            o = bsh.eval(t.getText().toString());
                        } catch (Throwable e) {
                            e.printStackTrace();
                            printf(e.getMessage());
                        }
                        if (o == null) return false;
                        Class<?> c = o.getClass();
                        Method[] mhs = c.getMethods();
                        String[] s = new String[mhs.length];
                        int p = 0;
                        for (Method m : mhs) {
                            s[p] = m.toString();
                            p++;
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Methods of " + c.toString())
                                .setItems(s, null)
                                .show();
                        return false;
                    }
                });
        setContentView(l);
    }

    void printf(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}
