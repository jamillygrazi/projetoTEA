package com.example.projetotea;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HttpService extends AsyncTask<Void, Void, String> {
    private final String cep;

    public HttpService(String cep){
        this.cep = cep;

}
    @Override
    protected String doInBackground(Void... Voids){
        StringBuilder resposta = new StringBuilder();

        if (this.cep != null && this.cep.length() == 8) {
            try{

                URL url = new URL("https://brasilapi.com.br/api/cep/v1/" + this.cep.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.connect();

                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()){
                    resposta.append(scanner.next());
                }

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

        return resposta.toString();
    }
}
