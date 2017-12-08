package trash.jak.id.go.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import trash.jak.id.go.R;

/**
 * Created by itp on 08/12/17.
 */

public class FromUploadSayaPeduli extends AppCompatActivity {
    private String TAG = FromUploadSayaPeduli.class.getSimpleName();
    String[] upload = { "Take picture", "Choose from Gallery" };
    private static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE_A= 200;
    private String longitude, lat, imei, kecamatan, sungai, user_status, username,email;
    private TextView _kecamatan, _nama_sungai, _longitude, _lat,textDate;
    private Button _save;
    private ImageView ftsmph;
    private static final int TAKE_PICTURE_A = 121;
    private ProgressDialog mProgressDialog;
    private Toolbar _toolbar;
    private String emailpelapor, estimasiberat, lokasi,sessiond, emaildb, imagecodeA1,foto, dir,picturePath = "",imageNameA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_sayapeduli);
        SharedPreferences shered = FromUploadSayaPeduli.this.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        sessiond = shered.getString("session", null);
        emaildb = shered.getString("email", null);
        foto = shered.getString("photo", null);

        _toolbar    = (Toolbar) findViewById(R.id.toolbar);
        _longitude = (TextView) findViewById(R.id.longtitude);
        _lat = (TextView) findViewById(R.id.latitude);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        textDate = (TextView) findViewById(R.id.tgl_upload);
        textDate.setText(formattedDate);

        ftsmph = (ImageView) findViewById(R.id.ftsblm_a);
        ftsmph.setOnClickListener(capture);
        _save = (Button) findViewById(R.id.send);
        _save.setOnClickListener(sendReport);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(emaildb);

        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_A) {
            if (resultCode == RESULT_OK) {
                Log.e("FormUpload", picturePath);
                try {
                    galleryAddPic(picturePath);
                    Glide.with(FromUploadSayaPeduli.this)
                            .load(new File(picturePath))
                            .asBitmap()
                            .into(targeta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == FromUploadSayaPeduli.this.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(FromUploadSayaPeduli.this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(FromUploadSayaPeduli.this,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    //Setting target
    private SimpleTarget targeta = new SimpleTarget<Bitmap>( 1280, 900 ) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            /*imageName1 = utils.getStringImage(bitmap).trim(); */
            ftsmph.setImageBitmap(bitmap);
            getStringImage(bitmap);
            imageNameA1 = imageNameA1.replaceAll("\\s", "");
            imagecodeA1 = getStringImage(getResizedBitmap(bitmap, 1280, 900)).trim();
        }
    };
        public String getStringImage(Bitmap bmp){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodeImage=null;
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
    private View.OnClickListener capture = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            picturePath = dir+generateImageName();
            File newfile = new File(picturePath);
            try {
                newfile.createNewFile();
            }
            catch (IOException e)
            {
            }
            imageNameA1 = newfile.getName();
            Uri outputFileUri = Uri.fromFile(newfile);
            //Glide.with(FormUploadFoto.this).load(outputFileUri).centerCrop().into(ftsblm_a);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            FromUploadSayaPeduli.this.startActivityForResult(cameraIntent, TAKE_PICTURE_A);

        }
    };

    /**
     * image name generator
     */
    private String generateImageName() {
        String yy, mm, dd, hh, ss, s;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        yy = strDate.substring(2, 4);
        mm = strDate.substring(5, 7);
        dd = strDate.substring(8, 10);
        hh = strDate.substring(11, 13);
        ss = strDate.substring(14, 16);
        s = strDate.substring(17, 19);
        //userid = MyApplication.getInstance().getPrefManager().getUser().getId();

        StringBuilder sb = new StringBuilder();
        sb.append(email);
        sb.append("_");
        sb.append(yy);
        sb.append(mm);
        sb.append(dd);
        sb.append(hh);
        sb.append(ss);
        sb.append(s);
        sb.toString();

        return sb.toString();
    }
    private View.OnClickListener sendReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(FromUploadSayaPeduli.this, "Send...", Toast.LENGTH_LONG).show();
            finish();
        }
    };
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
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
