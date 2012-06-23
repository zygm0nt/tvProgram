package org.ftang;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import org.ftang.adapter.ProgramAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ProgramAdapter(this, createList()));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
    }
    
    private Map<String, String> createList() {
        Map<String, String> programs = new HashMap<String, String>();
        List<String> rawLines = readRawTextFile(getBaseContext(), R.raw.programs);
        for (String line : rawLines) {
            String[] tokens = line.split(",");
            if (tokens.length == 4)
                programs.put(tokens[2] + "_" + tokens[1], tokens[3]); // FIXME TODO
            else
                programs.put(tokens[2] + "_" + tokens[1], "placeholder"); // FIXME TODO
        }
        
        return programs;
    }

    public static List<String> readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        List<String> l = new ArrayList<String>();
        
        try {
            while (( line = buffreader.readLine()) != null) {
                l.add(line);
            }
        } catch (IOException e) {
            return null;
        }
        return l;
    }
}