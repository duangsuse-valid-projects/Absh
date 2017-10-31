package org.duangsuse.absh;

import android.os.Bundle;
import android.app.Activity;
import bsh.Interpreter;

public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        Interpreter bsh = new Interpreter();
    }
}
