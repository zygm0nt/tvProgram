package org.ftang.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * User: marcin
 */
public class ImageUtil {


    public static Drawable getImage(Context ctx, String name) {
        String uri = "drawable/" + name;
        Log.d("ProgramAdapter", "Fetching " + name);
        int imageResource = ctx.getResources().getIdentifier(uri, null, ctx.getPackageName());
        Drawable image = ctx.getResources().getDrawable(imageResource);
        return image;
    }
}
