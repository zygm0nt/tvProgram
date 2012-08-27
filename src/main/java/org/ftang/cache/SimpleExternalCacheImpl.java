package org.ftang.cache;

import android.content.Context;
import android.util.Log;
import org.ftang.parser.JSoupParser;
import org.ftang.parser.Position;

import java.io.*;
import java.util.*;

/**
 * User: marcin
 * <p/>
 * TODO: cache expiration - keep data only for 10min?
 */
public class SimpleExternalCacheImpl implements SimpleExternalCache {

    private static final long TIME_SKEW = 10 * 60 * 1000;
    private long lastParseDate = 0L;

    private Map<String, List<Position>> programs = new HashMap<String, List<Position>>();
            
    private Context ctx;

    private String filename;

    public SimpleExternalCacheImpl(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean store(String programName, String content) {
        lastParseDate = new Date().getTime();
        filename = "";
        JSoupParser parser = new JSoupParser();
        try {
            programs.put(programName, parser.parse(content));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean storeToFile(String content) {
        File f = new File(ctx.getCacheDir(), "tvProgramApp-" + UUID.randomUUID().toString());
        try {
            Log.d(getClass().getSimpleName(), "Storing file " + f.getAbsolutePath());
            f.createNewFile();

            FileWriter fw = new FileWriter(f.getAbsolutePath());
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
    public List<Position> get(String programName) {
        if (programs.containsKey(programName))
            return programs.get(programName);
        return new ArrayList<Position>();
    }

    @Override
    public boolean isEmpty() {
        return filename == null;
    }

    @Override
    public boolean isUpToDate() {
        return lastParseDate > 0 && new Date().getTime() - lastParseDate < TIME_SKEW;
    }

    @Override
    public boolean contains(String programName) {
        return programs.containsKey(programName);
    }
}