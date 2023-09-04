package com.example.projetotea;

import com.google.firebase.auth.FirebaseAuth;

public class ConficuracaoFireBase {
    private static FirebaseAuth auth;
    public static FirebaseAuth Firebaseaautenticacao(){
        if (auth ==null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;

    }



}


