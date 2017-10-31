package org.duangsuse.absh;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import bsh.Interpreter;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Interpreter bsh = new Interpreter();
        Object o = null;
        try {
            o = bsh.eval("1+1");
        } catch (Exception e) {
        }
        puts(o.toString());
    }

    void puts(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
