package trash.jak.id.go.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.Target;

import trash.jak.id.go.R;

import static android.content.ContentValues.TAG;
import static com.android.volley.VolleyLog.setTag;

/**
 * Created by itp on 08/12/17.
 */

public class ImJnsSampah  extends AppCompatActivity{
    private ImageView mIvMain, mIvMain2;
    private Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jenis_sampah_img);

        mIvMain = (ImageView) findViewById(R.id.iv_main);
        mIvMain2 = (ImageView) findViewById(R.id.iv_main2);

        _toolbar    = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Jenis Sampah");
        loadingInternetPic();
        loadingInternetPic2();
        fitXY();
    }

    private void fitXY(){
        Display display = ImJnsSampah.this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        mIvMain.getLayoutParams().width = width;
        mIvMain.getLayoutParams().height = height;

        mIvMain2.getLayoutParams().width = width;


    }

    private void loadingInternetPic() {
        DrawableRequestBuilder<Integer> thumbnailRequest = Glide
                .with(this)
                .load(R.drawable.jns_sampah);

        Glide.with(this)
                .load("http://myshaf.com/images/jns_sampah.png")
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)//图片地址,还可以使用byte[],File,Integer,Uri参数
                .priority(Priority.HIGH)                       //优先级高
                .thumbnail(0.5f)                             //用原图的1/2作为缩略图
                //.thumbnail(thumbnailRequest)                   //使用自定义的缩略图
                .skipMemoryCache(true)                       //禁止内存缓存,使用Glide.get(context).clearMemory()清除缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)   //禁止磁盘缓存,使用Glide.get(applicationContext).clearDiskCache();清除缓存
                .bitmapTransform(new CenterCrop(this))       //圆形裁剪,使用前需要首先添加glide-transformations依赖
                .into(mIvMain);
    }

    private void loadingInternetPic2() {
        DrawableRequestBuilder<Integer> thumbnailRequest = Glide
                .with(this)
                .load(R.drawable.jns_sampah);

        Glide.with(this)
                .load("http://myshaf.com/images/Perbandingan_Komposisi_Sampah_2005_2011.png")
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)//图片地址,还可以使用byte[],File,Integer,Uri参数
                .priority(Priority.HIGH)                       //优先级高
                .thumbnail(0.5f)                             //用原图的1/2作为缩略图
                //.thumbnail(thumbnailRequest)                   //使用自定义的缩略图
                .skipMemoryCache(true)                       //禁止内存缓存,使用Glide.get(context).clearMemory()清除缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)   //禁止磁盘缓存,使用Glide.get(applicationContext).clearDiskCache();清除缓存
                .bitmapTransform(new CenterCrop(this))       //圆形裁剪,使用前需要首先添加glide-transformations依赖
                .into(mIvMain2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
