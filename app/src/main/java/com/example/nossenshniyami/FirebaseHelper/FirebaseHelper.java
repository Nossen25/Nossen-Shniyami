package com.example.nossenshniyami.FirebaseHelper;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nossenshniyami.BusinessModel.Business;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.Map;

public class FirebaseHelper {
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    Context context;
    private String info;


    public FirebaseHelper(Context context) {
        this.firestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public interface userFound {
        void onUserFound(String name, String phone);
    }

    public interface Completed {
        void onComplete(Boolean flag);
    }

    public void getUserInfo(String email, userFound callback) {

        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.getData().get("Email").equals(email)) {
                            callback.onUserFound(document.getData().get("FullName").toString(), document.getData().get("PhoneNumber").toString());
                        }
                    }
                    if (task.getResult() == null) {
                        callback.onUserFound(null, null);
                    }

                    //

                }
            }
        });
    }

    public void SignIn(String email, String password, Completed callback) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onComplete(true);
                        } else {
                            callback.onComplete(false);
                        }
                    }
                });
    }


    public void SignUp(Context context, String email, String password, Map<String, Object> user) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User sign-up successful
                        firestore.collection("Users") // Adjust collection name as needed
                                .add(user)
                                .addOnSuccessListener(documentReference ->
                                        Toast.makeText(context, "Sign Up Success", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error adding document", e);
                                    Toast.makeText(context, "Error adding user data", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // User sign-up failed
                        Toast.makeText(context, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface ListOfBus {
        void onGotBus(LinkedList<Business> listOfBus);
    }
//    public void ReadAllBusData(ListOfBus callback)
//    {
//
//        firestore.collection("Business")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            LinkedList<Business> tempL = new LinkedList<>();
//
//                            for (QueryDocumentSnapshot document : task.getResult())
//                            {
//                                Business bus = new Business();
//                                bus.setAddress(document.getData().get("BusinessAddress").toString());
//                                bus.setName(document.getData().get("BusinessName").toString());
//                                bus.setPhoneNumber(document.getData().get("PhoneNumber").toString());
//                                bus.setImageURL(R.drawable.businesspic1);
//                                tempL.add(bus);
//                            }
//                            callback.onGotBus(tempL);
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
//
//    }

    public void ReadAllBusData(ListOfBus callback) {
        firestore.collection("Business")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            LinkedList<Business> tempL = new LinkedList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Business bus = new Business();
                                bus.setAddress(document.getData().get("BusinessAddress").toString());
                                bus.setName(document.getData().get("BusinessName").toString());
                                bus.setPhoneNumber(document.getData().get("PhoneNumber").toString());

                                // Retrieve the image URL from Firestore
                                String imageURL = document.getData().get("Image").toString();
                                bus.setImageURL(imageURL);

                                tempL.add(bus);
                            }
                            callback.onGotBus(tempL);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public interface InfoCallback {
        void onInfoReceived(String info);
    }
//    public void GetInfoF(InfoCallback callback) {
//        firestore.collection("Business")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        String info = null;
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            info = document.getData().get("Basic Info").toString();
//                            // Assuming you want to retrieve only the info from the first document,
//                            // you can break the loop here
//                            break;
//                        }
//                        callback.onInfoReceived(info);
//                    } else {
//                        Log.w(TAG, "Error getting documents.", task.getException());
//                        callback.onInfoReceived(null);
//                    }
//                });
//    }
public void GetInfoF(String phoneNumber, InfoCallback callback) {
    firestore.collection("Business")
            .whereEqualTo("PhoneNumber", phoneNumber)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String info = document.getString("Basic Info");
                        callback.onInfoReceived(info);
                        return; // Exit the loop after finding the first match
                    }
                    // Handle the case when no matching document is found
                    callback.onInfoReceived(null);
                } else {
                    // Handle the case when the task fails
                    Log.e(TAG, "Failed to get documents.", task.getException());
                    callback.onInfoReceived(null);
                }
            });
}


    // Other helper methods
}


    // Other helper methods







