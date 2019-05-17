package com.example.sanskarmodi.uberarts.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.sanskarmodi.uberarts.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";
    private Listener listener;
    FirebaseAuth mAuth;
    ImageButton imageChooserBtn;
    EditText nameField;
    TextView verifyText;
    Button saveButton;
    String profileImageUrl;
    Uri uriProfileImage;
    ProgressBar progressBar;

    private static final int CHOOSE_IMAGE = 101;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageChooserBtn = view.findViewById(R.id.imageChooserBtn);
        saveButton = view.findViewById(R.id.updateProfileBtn);
        nameField = view.findViewById(R.id.editTextDisplayName);
        verifyText = view.findViewById(R.id.textViewVerified);

        saveButton.setOnClickListener(this);
        imageChooserBtn.setOnClickListener(this);
        loadUserProfile();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener)
            listener = (Listener) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateProfileBtn:
                if (nameField.getText().toString().isEmpty()) {
                    nameField.setError("Required name");
                    nameField.requestFocus();
                    return;
                }
                saveUserProfile();
                break;
            case R.id.imageChooserBtn:
                showImageChooser();
                break;
        }
//        listener.onInteraction(v);
    }

    public interface Listener {
        void onInteraction(View v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && data != null && data.getData() != null && resultCode == RESULT_OK) {
            uriProfileImage = data.getData();
            try {
                Log.i(TAG, "onActivityResult: " + getContext().getContentResolver());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriProfileImage);
                imageChooserBtn.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadImageToFirebaseStorage();
        }

    }

    private void saveUserProfile() {
        String displayName = nameField.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Profile Not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loadUserProfile() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null && user.getDisplayName() != null) {
                Glide.with(this).load(profileImageUrl).into(imageChooserBtn);
                nameField.setText(user.getDisplayName());
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profileImageUrl = uri.toString();
                                Toast.makeText(getActivity(), "Uploaded Image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    private void showImageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Profile Image"), CHOOSE_IMAGE);
    }
}
