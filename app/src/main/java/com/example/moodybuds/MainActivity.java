package com.example.moodybuds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    public static final String SIGN_IN = "Sign In";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Log.d("SIGN_IN", "Initial user is " + currentUser);
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.TwitterBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.mipmap.ic_launcher_round)
                            .build(),
                    RC_SIGN_IN);
        }
        else {
            Intent toMain = new Intent(this, MainScreen.class);
//                toMain.putExtra("userUID", user.getUid());
            startActivity(toMain);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SIGN_IN", "on result bruh");
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Log.i("SIGN_IN", "BEFORE");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.i("SIGN_IN", "user is " + user);
                Intent toMain = new Intent(this, MainScreen.class);
//                toMain.putExtra("userUID", user.getUid());
                startActivity(toMain);


            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.e("SIGN_IN", "Sign in failed");
            }
        }
    }

}