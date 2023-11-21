package com.example;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */

public class App {

    public static void main(String[] args){

    
        try{
            // metto il server in ascolto sulla porta 3000 per poter acquisire e creare la socket
            ServerSocket server = new ServerSocket(3000);
            System.out.println("il server è in ascolto");
            


            ArrayList<ServerThread> partecipanti = new ArrayList<ServerThread>();

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


