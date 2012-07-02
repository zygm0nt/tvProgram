package org.ftang.cache;

import android.content.Context;
import android.util.Log;
import org.ftang.parser.Position;

import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

/**
 * User: marcin
 * <p/>
 * TODO: cache expiration - keep data only for 10min?
 */
public class SimpleExternalCacheImpl implements SimpleExternalCache {

    private static final long TIME_SKEW = 10 * 60 * 1000;
    private long lastParseDate = 0L;


    private Context ctx;

    private String filename;

    public SimpleExternalCacheImpl(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean store(String content) {
        File f = new File(ctx.getCacheDir(), "tvProgramApp-" + UUID.randomUUID().toString());
        try {
            Log.d(getClass().getSimpleName(), "Storing file " + f.getAbsolutePath());
            f.createNewFile();

            FileWriter fw = new FileWriter(f.getName());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            lastParseDate = new Date().getTime();
            filename = f.getAbsolutePath();
            return true;
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Error storing tmp file: ", e);
        }
        return false;
    }

    @Override
    public boolean flush() {
        if (filename != null) {
            new File(filename).delete();
            filename = null;
        }
        return true;
    }

    @Override
    public String get() {
        if (filename == null)
            return null;
        try {
            StringBuilder sb = new StringBuilder();
            String NL = System.getProperty("line.separator");
            Scanner scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            try {
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine() + NL);
                }
            } finally {
                scanner.close();
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            Log.e(getClass().getSimpleName(), "Error fetching file: ", e);
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return filename == null;
    }

    @Override
    public boolean isUpToDate() {
        return lastParseDate > 0 && new Date().getTime() - lastParseDate < TIME_SKEW;
    }
}
