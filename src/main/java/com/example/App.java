package com.example;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class App {

    public static void main(String[] args){

    
        try{
            //creating a serversocket
            ServerSocket server = new ServerSocket(3000);
            System.out.println("il server è in ascolto");
            

            //creating a list of users
            ArrayList<ServerThread> partecipanti = new ArrayList<ServerThread>();
            //a cycle to accept aevery user that try to connect to the chat
            while(true){

                Socket client = server.accept();
                ServerThread p = new ServerThread(client,partecipanti);
                partecipanti.add(p);
                p.start();

            }

            

        } catch (Exception e) {
                System.out.println(e.getMessage());
        }
    }  
}


