package com.example.projetotea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class CadastroActivity extends AppCompatActivity {

    private Button buttonAlunoFamilia, buttonProfessor;
    private FrameLayout framelayout1;
    private CadastroAlunoFragment cadAlunoFragment;
    private CadastroProfessorFragment cadProfessorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        buttonAlunoFamilia = findViewById(R.id.buttonCadastro);
        buttonProfessor = findViewById(R.id.buttonLogin);
        framelayout1 = findViewById(R.id.framelayout1);

        buttonAlunoFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadAlunoFragment = new CadastroAlunoFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout1, cadAlunoFragment);
                transaction.commit();
            }
        });
        buttonProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadProfessorFragment = new CadastroProfessorFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout1, cadProfessorFragment);
                transaction.commit();

            }
        });


    }
}