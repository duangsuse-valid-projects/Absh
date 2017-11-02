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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Interpreter bsh = new Interpreter();
        try {
            bsh.set("ctx", this.getApplicationContext());
            bsh.set("me", this);
            bsh.set("sh", bsh);
            bsh.DEBUG = true;
            bsh.TRACE = true;
            bsh.redirectOutputToFile(getFilesDir().toString() + "/stack.txt");
        } catch (Throwable e) {
            printf(e.getMessage());
            e.printStackTrace();
        }
        public LinearLayout l = new LinearLayout(this);
        public final EditText t = new EditText(this);
        public final Button b = new Button(this);
        l.setOrientation(LinearLayout.VERTICAL);
        t.setHint("put your awesome code here┐(´-｀)┌\nlong press to inspect returning object");
        l.addView(b);
        l.addView(t);
        t.setTextSize(13);
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
                        String s = "()";
                        try {
                            f = new FileInputStream(getFilesDir().toString() + "/stack.txt");
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
                        String[] s = new String[c.getMethods().length + c.getFields().length];
                        int p = 0;
                        for (Method m : c.getMethods()) {
                            s[p] = m.toString();
                            p++;
                        }
                        for (Field f : c.getFields()) {
                            s[p] = f.toString();
                            p++;
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("BshInfoClass")
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
