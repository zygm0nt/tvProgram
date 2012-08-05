package org.ftang.component;

import android.R;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import org.ftang.model.Program;

/**
 * @author marcin
 */
public class TouchingViewPager extends ViewPager {

    private GestureDetector gestureScanner;

    public TouchingViewPager(Context context) {
        super(context);
        gestureScanner = new GestureDetector(simpleOnGestureListener);
    }

    public TouchingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureScanner = new GestureDetector(simpleOnGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int currentItem = getCurrentItem();
        gestureScanner.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        public boolean onDown(Math event) {
            return true;
        }

        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            Log.d(null,"Fling");
            int MAJOR_MOVE = 120;
            if (event1 == null) event1 = MotionEvent.obtain(0, 0, 0, 0, 0, 0);
            int dx = (int) (event2.getX() - event1.getX());
            // don't accept the fling if it's too short
            // as it may conflict with a button push
            if (Math.abs(dx) > MAJOR_MOVE && Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0) {
                    // nothing
                } else {
                    ListView liveView = (ListView) findViewById(R.id.list);
                    Program program = (Program) liveView.getAdapter()
                            .getItem(liveView.pointToPosition((int)event1.getX(), (int)event1.getY()));
                    program.getName();
                } 
                return true;
            } else {
                return false;
            }
        }

    };
}
