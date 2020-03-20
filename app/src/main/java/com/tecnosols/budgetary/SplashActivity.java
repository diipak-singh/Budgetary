package com.tecnosols.budgetary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SplashActivity extends AppCompatActivity {
    FirebaseUser user;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    BottomSheetDialog mBottomSheetDialog;
    private Dialog fDialog;
    private ImageView imgBiometric;
    private TextView textBottom;
    private Context ctx;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    private final int REQUEST_READ_PHONE_STATE = 1;
    public SplashActivity activity;

    private Button temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_splash);

        user = FirebaseAuth.getInstance().getCurrentUser();
        sp = getSharedPreferences("BIOMETRIC", Activity.MODE_PRIVATE);
        editor = sp.edit();
        ctx = SplashActivity.this;
        final Boolean check = sp.getBoolean("biometricFlag", false);

        /*mBottomSheetDialog = new BottomSheetDialog(SplashActivity.this, R.style.DialogStyle);
        View sheetView = this.getLayoutInflater().inflate(R.layout.verify_finger_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        //mBottomSheetDialog.setCancelable(false);
        imgBiometric = mBottomSheetDialog.findViewById(R.id.imageBiometric);
        textBottom = mBottomSheetDialog.findViewById(R.id.textView7Bottom);*/


        fDialog = new Dialog(SplashActivity.this);
        fDialog.setContentView(R.layout.verify_finger_layout);
        //detailsConfirmDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
        fDialog.getWindow().setGravity(Gravity.BOTTOM);
        fDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        fDialog.setCancelable(false);
        imgBiometric = fDialog.findViewById(R.id.imageBiometric);
        textBottom = fDialog.findViewById(R.id.textView7Bottom);





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (user != null) {

                    if (check) {
                        //mBottomSheetDialog.show();
                        //verifyFinger();
                        Toast.makeText(getApplicationContext(),"Biometric Lock Enabled",Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finishAffinity();
                    }

                } else {
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, 2 * 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }

    private void verifyFinger() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.USE_BIOMETRIC)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.USE_BIOMETRIC)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.USE_BIOMETRIC},
                            REQUEST_READ_PHONE_STATE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }


            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {

                textBottom.setText("Fingerprint Scanner not detected in Device");

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                textBottom.setText("Permission not granted to use Fingerprint Scanner");
                Toast.makeText(SplashActivity.this, "Permission not granted to use Fingerprint Scanner", Toast.LENGTH_SHORT).show();

            } else if (!keyguardManager.isKeyguardSecure()) {

                textBottom.setText("Add Lock to your Phone in Settings");

            } else if (!fingerprintManager.hasEnrolledFingerprints()) {

                textBottom.setText("You should add atleast 1 Fingerprint to use this Feature");

            } else {

                textBottom.setText("Place your Finger on Scanner to Access the App.");

                generateKey();

                if (cipherInit()) {

                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);

                }
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);

            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();


        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }

    public void sensorResponse(String data, boolean flag) {
        Toast.makeText(ctx, data, Toast.LENGTH_SHORT).show();

        /*if (flag == false) {
            fingerNotVerified();
        } else if (flag) {
            fingerVerified();
        }*/

    }


    private void fingerVerified() {
        imgBiometric.setImageResource(R.drawable.ic_fingerprint_green);
        textBottom.setText("Finger verified Successfully.");

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finishAffinity();
            }
        }, 500);*/

    }

    private void fingerNotVerified() {
        imgBiometric.setImageResource(R.drawable.ic_fingerprint_red);
        textBottom.setText("Mismatch! Please try again.");
    }


}
