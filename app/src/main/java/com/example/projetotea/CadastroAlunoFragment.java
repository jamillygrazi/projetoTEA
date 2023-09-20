package com.example.projetotea;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroAlunoFragment extends Fragment {

    private EditText name, username, email, pass;
    private Button btn_cadastrar;
    private String msg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastro_aluno, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.PlainTextNome);
        username = view.findViewById(R.id.PlainTextUsuario);
        email = view.findViewById(R.id.EmailProfessor);
        pass = view.findViewById(R.id.SenhaProfessor);
        btn_cadastrar = view.findViewById(R.id.buttonCadastrarAluno);

        btn_cadastrar.setOnClickListener(view1 -> {
            validacao(view1);
        });
    }

    private void validacao(View v){
        String name = this.name.getText().toString();
        String username = this.username.getText().toString();
        String email = this.email.getText().toString();
        String password = this.pass.getText().toString();

        if(name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar s = Snackbar.make(v, "Preencha todos os campos.", Snackbar.LENGTH_SHORT);
            s.show();
        } else{
            cadastrar(v, name, username, email, password);
        }
    }

    private void cadastrar(View v, String name, String username, String email, String password){
        //Criar usuário com credenciais
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Salvar dados no FireStore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> usuarios = new HashMap<>();
                    usuarios.put("name", name);
                    usuarios.put("username", username);

                    Toast toast = Toast.makeText(getContext(),"Aguarde alguns segundos...", Toast.LENGTH_SHORT);
                    toast.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DocumentReference documentReference = db.collection("Usuarios").document(email);
                            documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast toast = Toast.makeText(getContext(), "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar s = Snackbar.make(v, "Erro aos salvar os dados.", Snackbar.LENGTH_SHORT);
                                    s.show();
                                }
                            });
                        }
                    }, 5000);
                }else{
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        msg = "Digite uma senha com mais de 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException e){
                        msg = "Este endereço de e-mail já foi cadastrado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        msg = "Endereço de e-mail inválido.";
                    } catch (Exception e){
                        msg = "Erro ao cadastrar usuário.";
                    }
                    Snackbar s = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
                    s.show();
                }
            }
        });
    }
}