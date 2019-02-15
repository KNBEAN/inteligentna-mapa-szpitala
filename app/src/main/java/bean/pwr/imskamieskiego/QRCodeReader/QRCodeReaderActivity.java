/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.QRCodeReader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import bean.pwr.imskamieskiego.R;

public class QRCodeReaderActivity extends AppCompatActivity {

    private static final String TAG = "QRCodeReaderActivity";
    private CodeScanner codeScanner;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QRCodeReaderActivity.this.runOnUiThread(
                        () -> Toast.makeText(QRCodeReaderActivity.this, result.getText(), Toast.LENGTH_SHORT).show());
            }
        });
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
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
        Snackbar.make(findViewById(R.id.cameraLayout), "Kamera jest niedostępna", Snackbar.LENGTH_INDEFINITE).show();
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

