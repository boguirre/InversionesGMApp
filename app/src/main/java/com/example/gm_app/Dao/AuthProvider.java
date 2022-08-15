package com.example.gm_app.Dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProvider {

    private FirebaseAuth mAuth;

    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> registrar(String email, String password){
        return  mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> login(String email, String password){
      return  mAuth.signInWithEmailAndPassword(email,password);
    }

    public Task<AuthResult> googleLogin(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
         return  mAuth.signInWithCredential(credential);
    }

    public String getUid(){
        if (mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser().getUid();
        }
        else {
            return null;
        }
    }
}
