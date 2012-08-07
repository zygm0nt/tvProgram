package org.ftang.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.ftang.R;
import org.ftang.adapter.ImageUtil;
import org.ftang.wrapper.ResultWrapper;

/**
 * User: marcin
 */
public class DetailsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.program_dialog, container, false);
    }

    public static void fillView(View view, ResultWrapper result) {
        ((TextView)view.findViewById(R.id.channelName)).setText(result.title);
        ((ImageView)view.findViewById(R.id.channelIcon)).setImageDrawable(ImageUtil.getImage(view.getContext(), result.imgName));

        WebView text = (WebView) view.findViewById(R.id.text);
        text.setScrollBarStyle(WebView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        text.loadDataWithBaseURL("", "<ul>" + result.content + "</ul>", mimeType, encoding, "");
    }
}
