package org.ftang.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.ftang.R;

import java.util.*;

public class ProgramAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final Map<String, String> values;
    
    private final List<String> namesOrdered;

    public ProgramAdapter(Context context, Map<String, String> values) {
        super(context, R.layout.program_list);
        this.context = context;
        this.values = values;
        namesOrdered = new ArrayList<String> (values.keySet());
        Collections.sort(namesOrdered);
        for (String name : namesOrdered)
            add(name);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        String current = namesOrdered.get(position);
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.program_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView programNumber = (TextView) rowView.findViewById(R.id.program_number);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);

        programNumber.setText("" + Integer.parseInt(current.split("_")[0]));
        textView.setText(current.split("_")[1]);

        Log.d("ProgramAdapter", "I'm at " + current);

        imageView.setImageDrawable(getImage(values.get(current)));
        return rowView;
    }

    private Drawable getImage(String name) {
        String uri = "drawable/" + name;
        Log.d("ProgramAdapter", "Fetching " + name);
        int imageResource = getContext().getResources().getIdentifier(uri, null, getContext().getPackageName());
        Drawable image = getContext().getResources().getDrawable(imageResource);
        return image;
    }
}