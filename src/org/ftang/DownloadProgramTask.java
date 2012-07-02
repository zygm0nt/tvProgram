package org.ftang;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.ftang.cache.SimpleExternalCache;
import org.ftang.parser.JSoupParser;
import org.ftang.parser.Position;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User: marcin
 */
public class DownloadProgramTask extends AsyncTask<String, String, String> {
    
    private static final String URL = "http://www.teleman.pl/program-tv?stations=all";
    private Context ctx;
    private SimpleExternalCache externalCache;

    public DownloadProgramTask(Context ctx, SimpleExternalCache externalCache) {
        this.ctx = ctx;
        this.externalCache = externalCache;
    }

    @Override
    protected String doInBackground(String... params) {

        // params comes from the execute() call: params[0] is the url.
        try {
            /*if (externalCache.isEmpty() || !externalCache.isUpToDate())
                externalCache.store(downloadUrl(URL));
            return prepareContent(externalCache.get(), params[0]);*/
            return prepareContent(downloadUrl(URL), params[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    /*
        This method receives value = '02_stationName'
     */
    private String prepareContent(String content, String value) {
        try {
            Map<String, List<Position>> programs = new JSoupParser().parse(content);
            String programName = value.split("_")[1].toUpperCase();
            Log.d(getClass().getSimpleName(), "Content length " + content.length() );
            Log.d(getClass().getSimpleName(), "Fetching program for " + programName);
            Log.d(getClass().getSimpleName(), "Have this keys available:" + Arrays.toString(programs.keySet().toArray()));

            if (programs.containsKey(programName))
                return programs.get(programName).toString();
            return "";
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        createDialog(result).show();
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(getClass().getSimpleName(), "The response is: " + response);
            is = conn.getInputStream();

            String contentAsString = readIt(is);
            return contentAsString;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException {
        try {
            return new java.util.Scanner(stream).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
    
    private Dialog createDialog(String content) {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.program_dialog);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(content);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.freebsd_logo);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
