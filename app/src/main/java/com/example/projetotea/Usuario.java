package com.example.projetotea;

public class Usuario {
    private String id, nome,email,senha;

    public Usuario(String id, String nome, String email, String senha){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
}

    public Usuario() {
    }

    public void setId(String id){ this.id = id;}

    public String getId(){return id;}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}