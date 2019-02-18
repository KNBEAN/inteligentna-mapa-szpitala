/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.QRCodeReader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;


import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.zxing.BarcodeFormat;

import java.util.Arrays;

import bean.pwr.imskamieskiego.R;

public class QRCodeReaderActivity extends AppCompatActivity {

    public static final int QR_READER_CODE = 101;
    public static final String QR_READER_RESULT_KEY = "result";

    private static final String TAG = "QRCodeReaderActivity";
    private CodeScanner codeScanner;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getWindow() != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            Log.e(TAG, "Barcode scanner could not go into fullscreen mode!");
        }

        setContentView(R.layout.activity_qrcode_reader);
        Log.i(TAG, "onCreate: activity created");

        if (checkPermission()) {
            Log.i(TAG, "onCreate: camera permissions OK!");
            initCamera();
        } else {
            Log.e(TAG, "onCreate: no camera permissions");
            requestPermission();
        }
    }

    private void initCamera() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setFormats(Arrays.asList(BarcodeFormat.QR_CODE));
        codeScanner.setDecodeCallback(
                result -> QRCodeReaderActivity.this.runOnUiThread(() -> processResult(result.getText()))
        );
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
    }


    private void processResult(String result) {
        if (result.matches("#[0-9]+")) {
            Intent data = new Intent();
            data.putExtra(QR_READER_RESULT_KEY, Integer.valueOf(result.substring(1)));
            setResult(RESULT_OK, data);
            finish();
        } else {
            Snackbar.make(findViewById(R.id.cameraLayout), R.string.incorrect_qr_code, Snackbar.LENGTH_SHORT)
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            codeScanner.startPreview();
                        }
                    })
                    .show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (codeScanner != null) {
            codeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if (codeScanner != null) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            showRequestPermissionRationale();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCamera();
                } else {
                    showNoCameraMessage();
                }
                break;
        }
    }

    private void showNoCameraMessage() {
        Snackbar.make(findViewById(R.id.cameraLayout), R.string.camera_not_available, Snackbar.LENGTH_INDEFINITE).show();
    }

    private void showRequestPermissionRationale() {
        new AlertDialog.Builder(QRCodeReaderActivity.this)
                .setMessage(R.string.camera_permission_request_rationale)
                .setPositiveButton("OK", (dialogInterface, i) ->
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE)
                )
                .create()
                .show();
    }
}

