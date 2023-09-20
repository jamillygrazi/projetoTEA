package com.example.projetotea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_pass;
    private Button buttonLogin;

    private String id_canal = "CANAL TEA";

    private void criarCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id_canal, "Canal_TEA", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Descrição.");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void criarNtificacao() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id_canal)
                .setSmallIcon(R.drawable.baseline_info_24)
                .setContentTitle("Bem-vindo ao TEA Capitão Poço")
                .setContentText("Vamos fortalecer a rede de apoio ao autismo.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        criarCanal();

        et_email = findViewById(R.id.editTextTextEmailAddress);
        et_pass = findViewById(R.id.editTextTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarNtificacao();
                validacao(v);
            }
        });
    }

    private void validacao(View v){
        String email = et_email.getText().toString();
        String password = et_pass.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Snackbar s = Snackbar.make(v, "Preencha todos os campos.", Snackbar.LENGTH_SHORT);
            s.show();
        } else{
            logar(v, email, password);
        }
    }

    private void logar(View v, String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    telaPrincipal();
                }else{
                    String msg;
                    try {
                        throw task.getException();
                    } catch (Exception e){
                        msg = "Erro ao logar usuário.";
                    }
                    Snackbar s = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
                    s.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioatual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioatual != null){
            telaPrincipal();
        }
    }

    private void telaPrincipal(){
        LoginProfessorFragment logProfessorFragment = new LoginProfessorFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout1, logProfessorFragment);
        transaction.commit();
    }
}