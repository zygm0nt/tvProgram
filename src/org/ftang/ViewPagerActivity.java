package org.ftang;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import org.ftang.fragment.CustomPagerAdapter;
import org.ftang.fragment.MyFragment;
import org.ftang.fragment.ProgramListFragment;


/**
 * The <code>ViewPagerFragmentActivity</code> class is the fragment activity hosting the ViewPager
 * @author mwho
 */
public class ViewPagerActivity extends FragmentActivity implements ProgramListFragment.OnProgramSelectedListener {
    /** maintains the pager adapter*/
    private PagerAdapter mPagerAdapter;

    private final String DEBUG_TAG = "ProgramList-ViewPager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.viewpager);
        //initialize the pager
        this.initialisePaging();
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, ProgramListFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, MyFragment.class.getName()));
        this.mPagerAdapter  = new CustomPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Log.d(DEBUG_TAG, "Second page is now active");
                }
            }
        });
    }

    @Override
    public void onArticleSelected(int position) {
        Log.d(DEBUG_TAG, "onArticleSelected");
        ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setCurrentItem(1);
    }
}