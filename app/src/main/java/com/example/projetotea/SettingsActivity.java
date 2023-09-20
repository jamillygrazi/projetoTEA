package com.example.projetotea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SettingsActivity extends AppCompatActivity {

    private EditText et_name, et_username;
    private Button btn_save, btn_logout, btn_del_acc;
    private String user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et_name = findViewById(R.id.editTextName);
        et_username = findViewById(R.id.editTextUsername);
        btn_save = findViewById(R.id.buttonSave);
        btn_logout = findViewById(R.id.buttonLogOut);
        btn_del_acc = findViewById(R.id.buttonDeleteAccount);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuarios").document(user_email).update("name", et_name.getText().toString(), "username", et_username.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            Snackbar snackbar;
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    snackbar = Snackbar.make(v, "Alterações salvas com sucesso.", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                } else{
                                    snackbar = Snackbar.make(v, "Ocorreu um erro ao salvar as alterações.", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            }
                        });
                //db.collection("Usuarios").document(user_email).update("username", et_username.getText().toString());
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btn_del_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                db.collection("Usuarios").document(user_email).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Conta excluída com sucesso.", Toast.LENGTH_SHORT);
                                            toast.show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        } else{
                                            Snackbar snackbar = Snackbar.make(v, "Ocorreu algum erro ao excluir a conta.", Snackbar.LENGTH_SHORT);
                                            snackbar.show();
                                        }
                                    }
                                });

                            } else {
                                Snackbar snackbar = Snackbar.make(v, "Ocorreu algum erro ao excluir os dados.", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        DocumentReference documentReference = db.collection("Usuarios").document(user_email);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    et_name.setText(documentSnapshot.getString("name"));
                    et_username.setText(documentSnapshot.getString("username"));
                }
            }
        });
    }
}