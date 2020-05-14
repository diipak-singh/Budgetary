package com.tecnosols.budgetary;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView profileImg;
    private TextView profileUsrName, profileUsrMail;
    private FirebaseUser user;

    private MaterialCardView logout, share, removeAds, help_center,rateApp;
    private SwitchMaterial switchBiometric;
    private Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();

        profileImg = view.findViewById(R.id.profile_image);
        profileUsrName = view.findViewById(R.id.profile_username);
        profileUsrMail = view.findViewById(R.id.profile_usermail);
        logout = view.findViewById(R.id.card_logout);
        share = view.findViewById(R.id.card_share);
        removeAds = view.findViewById(R.id.card_removeAds);
        help_center = view.findViewById(R.id.card_help);
        switchBiometric = view.findViewById(R.id.switchFingerprint);
        rateApp=view.findViewById(R.id.card_rate);

        user = FirebaseAuth.getInstance().getCurrentUser();

        sp = context.getSharedPreferences("BIOMETRIC", Activity.MODE_PRIVATE);
        editor = sp.edit();

        Boolean check=sp.getBoolean("biometricFlag",false);
        if(check){
            switchBiometric.setChecked(true);
        }

        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).into(profileImg);
        }
        profileUsrName.setText(user.getDisplayName());
        profileUsrMail.setText(user.getEmail());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_power)
                        .setTitle("Logging Out of the session.")
                        .setMessage("Are you sure, you want to LogOut?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logout();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });

        removeAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                featureNotAvailable();
            }
        });

        help_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        rateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });

        switchBiometric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (isSensorAvialable()) {
                        editor.putBoolean("biometricFlag", true);
                        editor.commit();
                        Toast.makeText(getContext(), "Fingerprint Lock set successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        switchBiometric.setChecked(false);
                        Toast.makeText(getContext(), "Fingerprint Sensor not available", Toast.LENGTH_SHORT).show();
                    }
                }
                /*if(!isChecked){
                    editor.putBoolean("biometricFlag", false);
                    editor.commit();
                }*/
            }
        });


        return view;
    }

    private void rateApp(){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String subject = "https://play.google.com/store/apps/details?id=com.tecnosols.budgetary";
        //String subject = "Hey check this, one of the best apps to manage your budget on daily basis.";
        String body = "https://play.google.com/store/apps/details?id=com.tecnosols.budgetary";
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, "Share via.."));

    }

    private void featureNotAvailable() {
        Toast.makeText(getContext(), "This feature is not available right now.\n This would be available in next update.", Toast.LENGTH_SHORT).show();
    }

    private boolean isSensorAvialable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {
                if (!fingerprintManager.isHardwareDetected()) {
                    return false;
                    // Device doesn't support fingerprint authentication
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    return false;
                    // User hasn't enrolled any fingerprints to authenticate with
                } else {
                    return true;
                    // Everything is ready for fingerprint authentication
                }

            } else
                return false;
        } else {
            return false;
        }
    }

}
