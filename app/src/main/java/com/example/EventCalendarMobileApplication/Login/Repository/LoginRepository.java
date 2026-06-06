package com.example.EventCalendarMobileApplication.Login.Repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginRepository {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface AuthCallback {
        void onResult(boolean success);
    }

    public void authenticateUser(String username, String password, AuthCallback callback) {
        // Attempts to Login with the provided username and password
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> callback.onResult(task.isSuccessful()));
    }

    public void createAccount(String username, String password, String phoneNumber, AuthCallback callback) {

        auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful() || task.getResult() == null) {
                        callback.onResult(false);
                        return;
                    }
                    String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("phoneNumber", phoneNumber);

                    db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnCompleteListener(dbTask ->
                                    callback.onResult(dbTask.isSuccessful()));
                });
    }
}