package org.ftang;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.ftang.cache.SimpleExternalCache;
import org.ftang.fragment.ProgramListFragment;
import org.ftang.model.Program;
import org.ftang.parser.Position;
import org.ftang.wrapper.ResultWrapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * User: marcin
 */
public class DownloadProgramTask extends AsyncTask<Program, String, ResultWrapper> {
    
    private static final String URL = "http://www.teleman.pl/program-tv?stations=all";
    private Context ctx;
    private SimpleExternalCache externalCache;
    private ProgramListFragment.OnProgramSelectedListener mCallback;

    private ProgressDialog progressDialog;

    public DownloadProgramTask(Context ctx, SimpleExternalCache externalCache,
                               ProgramListFragment.OnProgramSelectedListener mCallback) {
        this.ctx = ctx;
        this.externalCache = externalCache;
        this.mCallback = mCallback;
    }

    /*
       This method receives value = '02_stationName'
    */
    @Override
    protected ResultWrapper doInBackground(Program... params) {

        String programName = params[0].getName().toUpperCase();
        String imgName = params[0].getImage();
        // params comes from the execute() call: params[0] is the url.
        try {
            if (externalCache.isEmpty() || !externalCache.isUpToDate())
                externalCache.store(downloadUrl(URL));
            return prepareContent(externalCache.get(programName), programName, imgName);
            //return prepareContent(downloadUrl(URL), params[0]);
        } catch (IOException e) {
            return new ResultWrapper("ERROR", "Unable to retrieve web page. URL may be invalid.", imgName);
        }
    }


    private ResultWrapper prepareContent(List<Position> positions, String programName, String imgName) {
         return new ResultWrapper(programName, stringify(positions), imgName);
    }

    private String stringify(List<Position> positions) {
        if (positions.size() == 0)
            return "Empty...";

        StringBuilder sb = new StringBuilder();
        for (Position p : positions) {
            sb.append(p.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResultWrapper result) {
        mCallback.onArticleSelected(result);
        if (progressDialog != null)
            progressDialog.dismiss();
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

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }
}
