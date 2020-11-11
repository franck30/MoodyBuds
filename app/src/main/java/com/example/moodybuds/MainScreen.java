package com.example.moodybuds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainScreen extends AppCompatActivity {

    List<ProfileCard> listOfPeople;
    FirebaseUser currentFirebaseUser;
    DatabaseReference mRootRef;
    DatabaseReference mUserRef;
    String userUID;

    public MainScreen() {

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Intent intent = getIntent();

        // initialize list of people and current firebase user
        listOfPeople = new ArrayList<>();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // create database references
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUserRef = mRootRef.child("users");

        // get userUID
        userUID = currentFirebaseUser.getUid();

        // add to database
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();

        // if new user, add their data to the database
        if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
            ProfileCard user = new ProfileCard(currentFirebaseUser.getDisplayName(), 0, "", userUID);
            mUserRef.child(userUID).setValue(user);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        // pull information from database whenever data changes and also when listener attached
        // for the first time
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // populate/update list of people
                listOfPeople.clear();
                for(DataSnapshot person: snapshot.getChildren()) {
                    listOfPeople.add(person.getValue(ProfileCard.class));
                }

                // recycler view stuff here


                // Debug to print list of people
                for(ProfileCard i: listOfPeople) {
                    Log.d("MAINSCREEN", i.toString());
                }
                Log.d("MAINSCREEN", "Num of people: " + listOfPeople.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("MAINSCREEN", "size of list of people: " + listOfPeople.size());

    }

}