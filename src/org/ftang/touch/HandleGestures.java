package org.ftang.touch;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Toast;
import org.ftang.MyActivity;
import org.ftang.ProgramListActivity;
import org.ftang.R;

/**
 * User: marcin
 */
public class HandleGestures extends GestureDetector.SimpleOnGestureListener {

    private static final String DEBUG_TAG = "ProgramList-HandleGestures";

    private Activity activity;

    private static int SWIPE_MIN_DISTANCE = 120;
    private static int SWIPE_MAX_OFF_PATH = 250;
    private static int SWIPE_THRESHOLD_VELOCITY = 200;

    public HandleGestures(Activity parentView) {
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
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
            return false;
        }

        boolean retVal = false;

        if (swipeLeftToRight(e1, e2, velocityX)) {
            Intent intent = new Intent(activity.getBaseContext(), ProgramListActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            retVal = true;
        } else if (swipeRightToLeft(e1, e2, velocityX)) {
            Intent intent = new Intent(activity.getBaseContext(), MyActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            retVal = true;
        }

        return retVal;
    }

    private boolean swipeRightToLeft(MotionEvent e1, MotionEvent e2, float velocityX) {
        return e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY;
    }

    private boolean swipeLeftToRight(MotionEvent e1, MotionEvent e2, float velocityX) {
        return e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY;
    }

}
