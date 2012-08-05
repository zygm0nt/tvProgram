package org.ftang.touch;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * User: marcin
 */
public class HandleGestures extends GestureDetector.SimpleOnGestureListener {

    private static final String DEBUG_TAG = "ProgramList-HandleGestures";

    private Activity activity;

    private static int SWIPE_MIN_DISTANCE = 120;
    private static int SWIPE_MAX_OFF_PATH = 250;
    private static int SWIPE_THRESHOLD_VELOCITY = 200;

    public HandleGestures(Fragment fragment) {
        Activity parentView = fragment.getActivity();
        this.activity = parentView;
        initConstants(parentView);
    }

    private void initConstants(Activity parentView) {
        final ViewConfiguration vc = ViewConfiguration.get(parentView.getBaseContext());
        SWIPE_MIN_DISTANCE = vc.getScaledTouchSlop();
        SWIPE_MAX_OFF_PATH = vc.getScaledMaximumFlingVelocity();
        SWIPE_THRESHOLD_VELOCITY = vc.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling called!");
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
            return false;
        }
        boolean retVal = false;
        if (swipeLeftToRight(e1, e2, velocityX)) {
            retVal = true;
        } else if (swipeRightToLeft(e1, e2, velocityX)) {
            retVal = true;
        }

        // listAdapter.getItem( listView.pointToPosition(e1.getX(), e1.getY() ) TODO
        return retVal;
    }

    private boolean swipeRightToLeft(MotionEvent e1, MotionEvent e2, float velocityX) {
        return e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY;
    }

    private boolean swipeLeftToRight(MotionEvent e1, MotionEvent e2, float velocityX) {
        return e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY;
    }
}