package com.example.projetotea;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {
    private Button buttonCadastro;
    private Button buttonLogin;

    private ImageButton imageButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int LOCATION_PERMISSION_REQUEST = 9;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LOGIN E CADASTRO
        buttonCadastro = findViewById(R.id.buttonCadastro);
        buttonLogin = findViewById(R.id.buttonLogin);

        imageButton = findViewById(R.id.imageButton);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // Inicialize aqui

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePicture();
            }
        });

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // Verificar se a permissão de localização já foi concedida.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // A permissão já foi concedida. Você pode iniciar o rastreamento de GPS aqui.
            startLocationTracking();
        } else {
            // A permissão ainda não foi concedida. Solicite-a ao usuário.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
    }

    private void startLocationTracking() {
        // Implemente a lógica de rastreamento de GPS aqui.
        // Por exemplo, você pode usar fusedLocationClient para obter a localização atual.
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // A localização foi obtida com sucesso.
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            // Faça algo com os valores de latitude e longitude.
                            // Por exemplo, exiba-os em um Toast:
                            Toast.makeText(MainActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                        } else {
                            // Não foi possível obter a localização.
                            Toast.makeText(MainActivity.this, "Não foi possível obter a localização.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Trate qualquer erro que possa ocorrer ao obter a localização.
                        // Isso pode incluir erros de permissão, configurações de localização desativadas, etc.
                        Toast.makeText(MainActivity.this, "Erro ao obter a localização: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

// Código relacionado à câmera
    private void dispatchTakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Defina a imagem capturada no ImageButton
            imageButton.setImageBitmap(imageBitmap);
        }
    }
    // Lida com a resposta do usuário à solicitação de permissão de localização
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            // Verifique se a permissão foi concedida.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, você pode iniciar o rastreamento de GPS aqui.
                startLocationTracking();
            } else {
                // Permissão negada pelo usuário. Lide com isso adequadamente, por exemplo, mostrando uma mensagem.
                Toast.makeText(this, "A permissão de localização foi negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

