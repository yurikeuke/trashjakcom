package trash.jak.id.go.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trash.jak.id.go.R;
import trash.jak.id.go.app.MyApplication;
import trash.jak.id.go.utils.AlbumStorageDirFactory;
import trash.jak.id.go.utils.BaseAlbumDirFactory;
import trash.jak.id.go.utils.ConnectionManager;
import trash.jak.id.go.utils.CropOption;
import trash.jak.id.go.utils.CropOptionAdapter;
import trash.jak.id.go.utils.Utils;

/**
 * Created by itp on 18/10/17.
 */

public class TakePicture extends AppCompatActivity {
    private String TAG = TakePicture.class.getSimpleName();
    private ImageView imageFoto;
    Button uploadButton, cancel;
    private static String picturePath = "", dir,imageName, imageReplace,imagecode,email, idpeserdaFb, kode,sessiond, status;
    private ProgressDialog mProgressDialog;
    private static final int TAKE_PICTURE_A = 121;
    private static final int CROP_FROM_CAMERA_IMAGE_REQUEST_CODE = 300;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private TextView idmustahik;
    final int CROP_PIC = 2;
    private Uri outputFileUri,galleryUri;
    private Bitmap photo;
    String[] upload = { "Take picture", "Choose from Gallery" };
    private static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE_A= 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                askPermissions();
            }
        }
        email = MyApplication.getInstance().getPrefManager().getUser().getAmilEmail();

        SharedPreferences shered2 = TakePicture.this.getSharedPreferences("isFirebase", Context.MODE_PRIVATE);
        idpeserdaFb = shered2.getString("id_mustahik",null);

        SharedPreferences shered = TakePicture.this.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        sessiond = shered.getString("session",null);

        if (idpeserdaFb==null||idpeserdaFb.isEmpty()) {
            SharedPreferences shered3 = TakePicture.this.getSharedPreferences("isScanPhoto", Context.MODE_PRIVATE);
            idpeserdaFb = shered3.getString("id_mustahik", null);
        }

        idmustahik = (TextView) findViewById(R.id.id_mustahik_foto);
        idmustahik.setText("NRM : "+idpeserdaFb);

        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();

        imageFoto = (ImageView) findViewById(R.id.imgFoto);
        imageFoto.setImageResource(R.drawable.camerablack);
        imageFoto.setOnClickListener(imgListener);
        cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(cancelListener);
        uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(saveListener);
    }

    View.OnClickListener imgListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (idpeserdaFb == null || idpeserdaFb.isEmpty()|| idpeserdaFb == ""){
                Toast.makeText(TakePicture.this, "Error NRM Null, Silahkan melakukan request ulang", Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> spinner_method = new ArrayAdapter<String>(
                        TakePicture.this,
                        android.R.layout.simple_spinner_dropdown_item, upload);
                new android.app.AlertDialog.Builder(TakePicture.this)
                        .setTitle("Upload Photo")
                        .setAdapter(spinner_method,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        switch (which) {
                                            case 0:
                                                captureImage();
                                                dialog.dismiss();
                                                break;
                                            case 1:
                                                getImageFromGallery();
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                }).create().show();
            }
        }
    };
    private void captureImage() {
        picturePath = dir + generateImageName()+".jpg";
        File newfile = new File(picturePath);
        try {
            newfile.createNewFile();
        }
        catch (IOException e) {
        }
        imageName = newfile.getName();
        outputFileUri = Uri.fromFile(newfile);
        Glide.with(TakePicture.this).load(outputFileUri).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imageFoto);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        TakePicture.this.startActivityForResult(cameraIntent, TAKE_PICTURE_A);
    }

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
    private void getImageFromGallery() {
/*        Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_CAPTURE_IMAGE_REQUEST_CODE_A);*/

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_CAPTURE_IMAGE_REQUEST_CODE_A);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_A) {
            if (resultCode == this.RESULT_OK) {
                Log.e("FormUpload", picturePath);
                try {

                   // doCrop();
                   galleryAddPic(picturePath);
                    Glide.with(TakePicture.this)
                            .load(new File(picturePath))
                            .asBitmap()
                            .skipMemoryCache(true)                       //禁止内存缓存,使用Glide.get(context).clearMemory()清除缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(targeta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == TakePicture.this.RESULT_CANCELED) {
                // user cancelled Image capture
                Glide.with(TakePicture.this)
                        .load(R.drawable.camerablack)
                        .asBitmap()
                        .skipMemoryCache(true)                       //禁止内存缓存,使用Glide.get(context).clearMemory()清除缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(targeta);
                Toast.makeText(TakePicture.this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(TakePicture.this,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == GALLERY_CAPTURE_IMAGE_REQUEST_CODE_A) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();

                    File f = new File(getRealPathFromURI(imageUri));
                    imageName = f.getName();
                    /*final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ftsblm_a.setImageBitmap(selectedImage);*/

                    Glide.with(TakePicture.this)
                            .load(imageUri)
                            .asBitmap()
                            .skipMemoryCache(true)                       //禁止内存缓存,使用Glide.get(context).clearMemory()清除缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(targeta);

                    /*final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ftsblm_a.setImageBitmap(selectedImage);*/

                    /*Glide.with(TakePicture.this)
                            .load(outputFileUri)
                            .asBitmap()
                            .into(targetG);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == TakePicture.this.RESULT_CANCELED) {
                // user cancelled Image capture
                Glide.with(TakePicture.this)
                        .load(R.drawable.camerablack)
                        .asBitmap()
                        .skipMemoryCache(true)                       //禁止内存缓存,使用Glide.get(context).clearMemory()清除缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(targeta);
                Toast.makeText(TakePicture.this,
                        "User cancelled select image", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(TakePicture.this,
                        "Sorry! Failed to select image", Toast.LENGTH_SHORT)
                        .show();
            }

            //Take B
        }else if (requestCode == CROP_FROM_CAMERA_IMAGE_REQUEST_CODE) {
            if (resultCode == this.RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    photo = extras.getParcelable("data");
                    try {
                        //write file
                        File f = new File(outputFileUri.getPath());
                        galleryAddPic(outputFileUri.getPath());
                        Glide.with(TakePicture.this)
                                .load(f)
                                .asBitmap()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(targeta);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //mImageView.setImageBitmap(photo);
                }



//                if (f.exists()) f.delete();
            } else if (resultCode == TakePicture.this.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(TakePicture.this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(TakePicture.this,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    //Setting target
    private SimpleTarget targeta = new SimpleTarget<Bitmap>( 900, 900 ) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            /*imageName1 = utils.getStringImage(bitmap).trim(); */
            imageFoto.setImageBitmap( bitmap );
            getStringImage(bitmap);
            imageName = imageName.replaceAll("\\s","");
            imageName = generateImageName()+".jpg";
            imagecode = getStringImage(getResizedBitmap(bitmap, 900, 900)).trim();
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
//viewHolder.mProgbar.setVisibility(View.INVISIBLE);
        }
    };


    private SimpleTarget targetG = new SimpleTarget<Bitmap>( 750, 750 ) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            /*imageName1 = utils.getStringImage(bitmap).trim(); */
            imageFoto.setImageBitmap( bitmap );
            getStringImage(bitmap);
            imageName = imageName.replaceAll("\\s","");
            imagecode = getStringImage(getResizedBitmap(bitmap, 750, 750)).trim();
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
//viewHolder.mProgbar.setVisibility(View.INVISIBLE);
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
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodeImage=null;
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

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

        StringBuilder sb = new StringBuilder();
        sb.append(idpeserdaFb);
        /*sb.append("_");
        sb.append(Utils.getSaltString());
        sb.append(mm);
        sb.append(dd);
        sb.append(hh);
        sb.append(ss);
        sb.append(s);*/
        sb.toString();

        return sb.toString();
    }
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences preferences = getSharedPreferences("isFirebase", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            imagecode = null;
            imageName = null;
            Intent intent = new Intent(TakePicture.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    };

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = TakePicture.this.getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(TakePicture.this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(outputFileUri);

            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA_IMAGE_REQUEST_CODE);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= TakePicture.this.getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= TakePicture.this.getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(TakePicture.this, cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(TakePicture.this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA_IMAGE_REQUEST_CODE);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (outputFileUri != null ) {
                            TakePicture.this.getContentResolver().delete(outputFileUri, null, null );
                            outputFileUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null); if (cursor == null) {
            result = contentURI.getPath(); }
        else {
            if(cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx); } cursor.close(); }
        return result;
    }

    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                if (idpeserdaFb == null|| idpeserdaFb.isEmpty()||idpeserdaFb == "") {
                    Toast.makeText(TakePicture.this, "NRM tidak terisi, silahkan lakukan request NRM melalui TrashJak.", Toast.LENGTH_SHORT).show();
                } else if (imageName == null || imageName.isEmpty()) {
                    Toast.makeText(TakePicture.this, "Silahkan ambil gambar", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressDialog();
                    sendFoto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Mengirim...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.show();
    }
    private void sendFoto() {
        StringRequest strReqa = new StringRequest(Request.Method.POST,
                ConnectionManager.CM_UPLOAD+"?"+Utils.getSaltString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    // check for error flag
                    if (obj.getString("status_code").equalsIgnoreCase("000")) {
                        if(mProgressDialog!=null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        imageName=null;
                        imagecode=null;
                        imageFoto.setImageResource(R.drawable.camerablack);
                        hideProgressDialog();
                        Toast.makeText(TakePicture.this, "Terkirim", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(TakePicture.this, "Pengiriman gagal", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(TakePicture.this, "Timeout, silahkan coba ulang" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> paramss = new HashMap<>();
                paramss.put("session", sessiond);
                paramss.put("image_name", imageName);
                paramss.put("image", imagecode);
                paramss.put("nrm", idpeserdaFb);

                Log.d(TAG, "PARAM: " + paramss.toString());
                return paramss;
            }
        };
        //code jika volley timeout
        strReqa.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReqa);
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }
    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_PHONE_STATE",
                "android.permission.CAMERA"

        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


}


