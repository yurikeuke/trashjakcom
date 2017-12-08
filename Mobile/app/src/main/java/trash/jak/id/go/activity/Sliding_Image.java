package trash.jak.id.go.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import trash.jak.id.go.R;
import trash.jak.id.go.adapter.SlidingImage_Adapter;

/**
 * Created by itp on 07/11/17.
 */

public class Sliding_Image extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] IMAGES= {"http://myshaf.com/images/kegiatan1.jpg","http://myshaf.com/images/kegiatan2.jpg","http://myshaf.com/images/kegiatan3.jpg"};
    private ArrayList<String > ImagesArray = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_image);
        init();
    }
    private void init() {
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(Sliding_Image.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

}
