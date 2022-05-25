package com.example.siingat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class cobaLogin extends AppCompatActivity implements View.OnClickListener {

    //Google Signup
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private static final int REQ_ONE_TAP_GOOGLE = 97521;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore dbFire;

    //Facebook Signup
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private static final int REQ_ONE_TAP_FACEBOOK = 64206;  // Fixed Activity From Facebook

    //Local Java
    private User usr;

    protected int REQ_ONE_TAP_GENERAL;

    private SignInButton signInButton;
    private Button btnLog;
    private TextView tvUser;

    //Database
    Database database;
    private TextView tvUID, tvName, tvGender, tvBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_login);

        signInButton = findViewById(R.id.btn_gog);
        signInButton.setOnClickListener(this);

        btnLog = findViewById(R.id.btn_logt);
        btnLog.setOnClickListener(this);

        tvUser = findViewById(R.id.tv_user);

        //google Button Initial
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.firebaseClientId))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.btn_fbk);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook", "facebook:onError", error);
            }
        });

        //Firestore
        dbFire = FirebaseFirestore.getInstance();
        DocumentReference docRef = dbFire.collection("users").document(currentUser.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firestore doc", "DocumentSnapshot data: " + document.toObject(User.class).getName());
                    } else {
                        Log.d("Firestore doc", "No such document");
                    }
                } else {
                    Log.d("Firestore doc", "get failed with ", task.getException());
                }
            }
        });

//        //Database
//        database = new Database(this);
//        SQLiteDatabase db = database.getReadableDatabase();
//        Log.d("GetUID","UID : " + currentUser.getUid());
//        Cursor c = db.rawQuery("SELECT * FROM Users WHERE TRIM(UID) = '"+ currentUser.getUid().toString().trim() +"'", null);
//        c.moveToNext();
////        Log.d("Data select","UID : " + c.getString(c.getColumnIndex("UID")));
//        tvUID = findViewById(R.id.tv_UID);
//        tvUID.setText(c.getString(c.getColumnIndex("UID")));
//
//        tvName = findViewById(R.id.tv_Namee);
//        tvName.setText(c.getString(c.getColumnIndex("Name")));
//
//        tvGender = findViewById(R.id.tv_Genderr);
//        tvGender.setText(c.getString(c.getColumnIndex("Gender")));
//
//        tvBirth = findViewById(R.id.tv_Birthh);
//        tvBirth.setText(c.getString(c.getColumnIndex("Birth")));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser!=null){
            tvUser.setText("Hello " + currentUser.getDisplayName());
        }
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Continue With");

//        updateUI(currentUser);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_gog):
                REQ_ONE_TAP_GENERAL = 97521;
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                try {
                                    startIntentSenderForResult(
                                            result.getPendingIntent().getIntentSender(), REQ_ONE_TAP_GENERAL,
                                            null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.e("ONE TAP", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No Google Accounts found. Just continue presenting the signed-out UI.
                                Log.d("ONE TAP", e.getLocalizedMessage());
                            }
                        });
                break;
            case (R.id.btn_logt):
                tvUser.setText("Hello user");
                FirebaseAuth.getInstance().signOut();
                Log.d("Logout","Logout Firebase success");
                Toast.makeText(getApplicationContext(),"Logout Success",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Request","code : " +requestCode);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP_GOOGLE:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d("lOGIN", "Got ID token.");
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("Firebase", "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
//                                            updateUI(user);
                                            tvUser.setText("Hello " + credential.getDisplayName());
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("Firebase", "signInWithCredential:failure", task.getException());
//                                            updateUI(null);
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case CommonStatusCodes.CANCELED:
                            Log.d("Login", "One-tap dialog was closed.");
                            // Don't re-prompt the user.
                            showOneTapUI = false;
                            break;
                        case CommonStatusCodes.NETWORK_ERROR:
                            Log.d("Login", "One-tap encountered a network error.");
                            // Try again or just ignore.
                            break;
                        default:
                            Log.d("Login","Couldnt get credential from result"
                                    + e.getLocalizedMessage());
                            break;
                    }
                }
                break;

            case REQ_ONE_TAP_FACEBOOK:
                // Pass the activity result back to the Facebook SDK
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Facebook handle", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Facebook handle", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            tvUser.setText("Hello " + user.getDisplayName());
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Facebook handle", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }
}