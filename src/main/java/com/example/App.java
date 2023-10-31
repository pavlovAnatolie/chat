package com.example;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    try{
            // metto il server in ascolto sulla porta 3000 per poter acquisire e creare la socket
        ServerSocket server = new ServerSocket(3000);
        System.out.println("il server è in ascolto");
        System.out.println("inserisci lo username da usare");
        Scanner cin = new Scanner(System.in);
        String nome = cin.nextLine();
        HashMap<String,ServerThread> partecipanti = new HashMap();

        while(true){

            ServerThread p = new ServerThread(server.accept(),partecipanti);
            partecipanti.put(nome, p);
            p.start();

        }

    } catch (Exception e) {
            System.out.println(e.getMessage());
    }
}


