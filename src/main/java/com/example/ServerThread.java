package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerThread extends Thread{
    private String nome = "";
    private Socket socket;
    private ArrayList<ServerThread> partecipanti;
    BufferedReader in;
    DataOutputStream out;

    public ServerThread(Socket socket, ArrayList<ServerThread>partecipanti){
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
        return nome;
   }
    
    private boolean issetName(String s){
        boolean presente = false;
        for(ServerThread thread : partecipanti){
            if(thread.getNome().equals(s)){ 
                
                presente = true;
            }
        }
        return presente;
    }
    
    public void run(){
        try{
            System.out.println("provas");
            String nome = in.readLine();
            this.nome = nome;
            System.out.println(nome+" è entrato nel server" );


            System.out.println("\u001B[32m" +"----------"+getNome()+"connected to the chat----------"+ "\u001B[37m");
            broadcast("\u001B[32m" +"----------"+getNome()+"connected to the chat----------"+ "\u001B[37m");
            
            while(true){
                String messaggio = in.readLine(); 

                //controlliamo se unicast
                if(messaggio.startsWith("@") && issetName(messaggio)){
                    unicast(in.readLine(), messaggio);
                }
                else if(messaggio.equals("/close")){
                    broadcast("\u001B[32m" + getNome() + "disconected \u001B[37m");
                    partecipanti.remove(getNome());
                    socket.close();
                }

                 
            }
                 
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
       
    }

    private void broadcast(String message) throws IOException{
        for(ServerThread a: partecipanti){
            if(currentThread() != a)
            a.send(message);
        }
    }
    
    private void unicast(String message, String name) throws IOException{
         for(ServerThread a: partecipanti){
            if(name.equals(a.getName())){
                send("\u001B[31m" + message+ "\u001B[37m");
            }
        }
    }


    private void send(String message) throws IOException{
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());//output
        out.writeBytes(getNome()+ "==>\t" + message +"\n");
    }
    

}
