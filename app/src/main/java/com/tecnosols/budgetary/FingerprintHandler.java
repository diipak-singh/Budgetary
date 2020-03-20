package com.tecnosols.budgetary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    public static final int FINGERPRINT_PERMISSION = 1;
    private Context context;
    SplashActivity mainActivity=new SplashActivity();

    public FingerprintHandler(Context context) {

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        //MainActivity.sensorResponse("There was an Auth Error. " + errString, false);
        mainActivity.sensorResponse("There was an Auth Error. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        //MainActivity.sensorResponse("Auth Failed. ", false);
        mainActivity.sensorResponse("Auth Failed. ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        //MainActivity.sensorResponse("Error: " + helpString, false);
        mainActivity.sensorResponse("Error: " + helpString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        //MainActivity.sensorResponse("Finger Verified Successfully.", true);
        mainActivity.sensorResponse("Finger Verified Successfully.", true);

    }
}
