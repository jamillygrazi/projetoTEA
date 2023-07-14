package com.example.projetotea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
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

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarNtificacao();
            }
        });
    }
}