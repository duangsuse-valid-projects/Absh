/**
 * Display the contents of the current working directory. The format is similar to the Unix ls -l
 * <em>This is an example of a bsh command written in Java for speed.</em>
 *
 * @method void dir( [ String dirname ] )
 */
package bsh.commands;

import bsh.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class dir {
    static final String[] months = {
        "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
    };

    public static String usage() {
        return "用法: dir( String dir )\n       dir()";
    }

    /** Implement dir() command. */
    public static void invoke(Interpreter env, CallStack callstack) {
        String dir = ".";
        invoke(env, callstack, dir);
    }

    /** Implement dir( String directory ) command. */
    public static void invoke(Interpreter env, CallStack callstack, String dir) {
        File file;
        String path;
        try {
            path = env.pathToFile(dir).getAbsolutePath();
            file = env.pathToFile(dir);
        } catch (IOException e) {
            env.println("读目录 " + e + " 时错误");
            return;
        }

        if (!file.exists() || !file.canRead()) {
            env.println("无法读取文件 " + file);
            return;
        }
        if (!file.isDirectory()) {
            env.println("'" + dir + "' 不是一个目录");
        }

        String[] files = file.list();
        files = StringUtil.bubbleSort(files);

        for (int i = 0; i < files.length; i++) {
            File f = new File(path + File.separator + files[i]);
            StringBuffer sb = new StringBuffer();
            sb.append(f.canRead() ? "r" : "-");
            sb.append(f.canWrite() ? "w" : "-");
            sb.append("_");
            sb.append(" ");

            Date d = new Date(f.lastModified());
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_MONTH);
            sb.append(months[c.get(Calendar.MONTH)] + " " + day);
            if (day < 10) sb.append(" ");

            sb.append(" ");

            // hack to get fixed length 'length' field
            int fieldlen = 8;
            StringBuffer len = new StringBuffer();
            for (int j = 0; j < fieldlen; j++) len.append(" ");
            len.insert(0, f.length());
            len.setLength(fieldlen);
            // hack to move the spaces to the front
            int si = len.toString().indexOf(" ");
            if (si != -1) {
                String pad = len.toString().substring(si);
                len.setLength(si);
                len.insert(0, pad);
            }

            sb.append(len.toString());

            sb.append(" " + f.getName());
            if (f.isDirectory()) sb.append("/");

            env.println(sb.toString());
        }
    }
}
