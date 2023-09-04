package com.example.projetotea;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;

import br.eti.roberto.android20222.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;
    Usuario usuario;
    FirebaseAuth autenticacao;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().
                getReference();
        String id = database.push().getKey();
        Usuario user = new Usuario(
                id, "Roberto Franco",
                "roberto.franco@ufra.edu.br",
                "123456");
        database.child(id).setValue(user);
        database.removeValue();


        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String retorno = null;
                try {
                    String cepp = binding.editTextTextCEP.getText().toString();
                    Log.d("CEP", cepp);
                    retorno = new HttpService(cepp).execute().get();
                    Log.d("CEP Result", retorno);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), retorno, Toast.LENGTH_LONG).show();
                Log.d("Result CEP", retorno);
            }
        });




        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void cadastrarusuario() {
        autenticacao = ConficuracaoFireBase.Firebaseaautenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                }else{
                    String excessao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        excessao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excessao = "Digite um E-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excessao = "Conta já cadastrada!";
                    }catch (Exception e){
                        excessao = "Erro ao cadastrar usuário!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), excessao.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}


