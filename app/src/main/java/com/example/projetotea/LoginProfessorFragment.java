package com.example.projetotea;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginProfessorFragment extends Fragment {

    private Button btn_settings;
    private String user_email;
    private TextView tv_boas_vindas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_professor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_settings = view.findViewById(R.id.buttonSettings);
        tv_boas_vindas = view.findViewById(R.id.textViewBoasVindas);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DocumentReference documentReference = db.collection("Usuarios").document(user_email);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    tv_boas_vindas.setText("Olá, " + documentSnapshot.getString("name"));
                }
            }
        });
    }
}