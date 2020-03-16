package com.tecnosols.budgetary;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView profileImg;
    private TextView profileUsrName, profileUsrMail;
    private FirebaseUser user;

    private MaterialCardView logout, share,removeAds,help_center;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImg = view.findViewById(R.id.profile_image);
        profileUsrName = view.findViewById(R.id.profile_username);
        profileUsrMail = view.findViewById(R.id.profile_usermail);
        logout = view.findViewById(R.id.card_logout);
        share = view.findViewById(R.id.card_share);
        removeAds=view.findViewById(R.id.card_removeAds);
        help_center=view.findViewById(R.id.card_help);

        user = FirebaseAuth.getInstance().getCurrentUser();

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
                Intent intent=new Intent(getContext(),HelpActivity.class);
                startActivity(intent);
            }
        });


        return view;
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
        String subject = "https://play.google.com/store/apps/details?id=com.tecnosols.mistri&hl=en";
        String body = "https://play.google.com/store/apps/details?id=com.tecnosols.mistri&hl=en";
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, "Share via.."));

    }

    private void featureNotAvailable() {
        Toast.makeText(getContext(), "This feature is not available right now.\n This would be available in next update.", Toast.LENGTH_SHORT).show();
    }

}
