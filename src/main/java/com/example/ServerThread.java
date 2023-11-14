package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread{
    private Socket socket;
    private HashMap <String, ServerThread> partecipanti;
    BufferedReader in;
    DataOutputStream out;

    public ServerThread(Socket socket, HashMap partecipanti){
        this.socket = socket;
        this.partecipanti = partecipanti;
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//input
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());//output
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private String getNome(){
        String nome  ="";
        for (String nomi : partecipanti.keySet()) {
            if(currentThread().equals(partecipanti.get(nomi))){
                nome = nomi;
            }
        }
        
        return nome;
    }
    
    public void run(){
        try{
            System.out.println("----------connected to the chat----------");
            broadcast("----------"+getNome()+"connected to the chat----------");
            in.readLine();
            while(true){
                
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
       
    }

    private void broadcast(String message) throws IOException{
        for(String name: partecipanti.keySet()){
            if(currentThread() != partecipanti.get(name))
                unicast(message, name);
        }
    }
    
    private void unicast(String message, String name) throws IOException{
        partecipanti.get(name).send(message);
    }


    private void send(String message) throws IOException{
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());//output
        out.writeBytes(message);
    }
    

}
