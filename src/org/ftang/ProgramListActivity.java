package org.ftang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import org.ftang.adapter.ProgramAdapter;
import org.ftang.cache.SimpleExternalCache;
import org.ftang.cache.SimpleExternalCacheImpl;
import org.ftang.model.Program;
import org.ftang.parser.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramListActivity extends ListActivity {

    private static final String DEBUG_TAG = "ProgramList-NetworkStatus";

    private SimpleExternalCache externalCache;

    DownloadProgramTask downloadProgramTask;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        externalCache = new SimpleExternalCacheImpl(this);

        setListAdapter(new ProgramAdapter(this, createList()));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Program selectedValue = (Program) getListAdapter().getItem(position);

        if (isOnline()) {
            runTaskIfNeeded(selectedValue);
        } else {
            //Toast.makeText(this, "Network error!", Toast.LENGTH_SHORT).show();
            showAlert("Error!", "No network connection").show();
        }
    }
    
    private void runTaskIfNeeded(Program selectedValue) {
        if (downloadProgramTask == null)
            downloadProgramTask = new DownloadProgramTask(this, externalCache);
        if (!downloadProgramTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            Toast.makeText(this, "Starting download task!", Toast.LENGTH_SHORT).show();
            downloadProgramTask.execute(selectedValue);
        } else {
            Toast.makeText(this, "Task state conditions unmet: " + downloadProgramTask.getStatus(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private List<Program> createList() {
        return readRawTextFile(getBaseContext(), R.raw.programs);
    }


    public static List<Program> readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        List<Program> l = new ArrayList();
        
        try {
            while (( line = buffreader.readLine()) != null) {
                l.add(new Program(line));
            }
        } catch (IOException e) {
            return null;
        }
        return l;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        return isWifiConn;
    }

    public boolean isMobileConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        return isMobileConn;
    }
    
    private Dialog showAlert(String title, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialog;
    }
}