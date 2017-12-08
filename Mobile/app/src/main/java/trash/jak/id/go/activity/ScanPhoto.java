package trash.jak.id.go.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by itp on 21/10/17.
 */

public class ScanPhoto extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

    }


    @Override
    public void handleResult(Result result) {
        Log.v("TAG", result.getText());
        // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", result.getBarcodeFormat().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        barcode = result.getText();
        alert1.show();
        goToPhoto();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume

        mScannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    private void goToPhoto() {
        try {
            if (barcode.equalsIgnoreCase("") || barcode.length()<3 || barcode.isEmpty() || barcode == null||barcode.length()>=26) {
                ;
                Toast.makeText(ScanPhoto.this, "Error Barcode, silahkan melakukan request ulang.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ScanPhoto.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                setUserSesson();
                Intent intent = new Intent(ScanPhoto.this, TakePicture.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void setUserSesson() {
        SharedPreferences shered = getSharedPreferences("isScanPhoto", Context.MODE_PRIVATE);
        SharedPreferences.Editor sheredEdit = shered.edit();
        sheredEdit.putString("id_mustahik", barcode);
        sheredEdit.commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


}
