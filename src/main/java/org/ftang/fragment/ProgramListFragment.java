package org.ftang.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
import org.ftang.DownloadProgramTask;
import org.ftang.R;
import org.ftang.adapter.ProgramAdapter;
import org.ftang.cache.SimpleExternalCache;
import org.ftang.cache.SimpleExternalCacheImpl;
import org.ftang.model.Program;
import org.ftang.touch.HandleGestures;
import org.ftang.wrapper.ResultWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ProgramListFragment extends ListFragment implements View.OnTouchListener {

    private SimpleExternalCache externalCache;

    DownloadProgramTask downloadProgramTask;

    private GestureDetector gestureScanner;

    OnProgramSelectedListener mCallback;

    private ProgressDialog progressDialog;

    // Container Activity must implement this interface
    public interface OnProgramSelectedListener {
        public void onArticleSelected(ResultWrapper result);
    }

    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        super.onCreate(savedInstanceState);
        externalCache = new SimpleExternalCacheImpl(getActivity());

        gestureScanner = new GestureDetector(new HandleGestures(this));
        setListAdapter(new ProgramAdapter(getActivity(), createList()));

        return inflater.inflate(R.layout.program_list, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnTouchListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Program selectedValue = (Program) getListAdapter().getItem(position);

        if (isOnline()) {
            runTaskIfNeeded(selectedValue);
        } else {
            showAlert("Error!", "No network connection").show();
        }
    }


    private void runTaskIfNeeded(Program selectedValue) {
        if (downloadProgramTask == null || downloadProgramTask.getStatus().equals(AsyncTask.Status.FINISHED))
            downloadProgramTask = new DownloadProgramTask(getActivity(), externalCache, mCallback);
        if (!downloadProgramTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            progressDialog = ProgressDialog.show(getActivity(), "I'm in a middle of sth","Fetching program info", true, false, null);
            downloadProgramTask.setProgressDialog(progressDialog);
            downloadProgramTask.execute(selectedValue);
        } else {
            Toast.makeText(getActivity(), "Task state conditions unmet: " + downloadProgramTask.getStatus(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<Program> createList() {
        return readRawTextFile(getActivity().getBaseContext(), R.raw.programs);
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
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        return isWifiConn;
    }

    public boolean isMobileConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        return isMobileConn;
    }

    private Dialog showAlert(String title, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialog;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureScanner.onTouchEvent(motionEvent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnProgramSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnProgramSelectedListener");
        }
    }
}