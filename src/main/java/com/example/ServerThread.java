package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;


public class ServerThread extends Thread {
    private String nome = "";
    private Socket socket;
    private ArrayList<ServerThread> partecipanti;
    BufferedReader in;
    DataOutputStream out;
    
    //costrunctor
    public ServerThread(Socket socket, ArrayList<ServerThread> partecipanti) {
        this.socket = socket;//socket 
        this.partecipanti = partecipanti;//lista dei Thread

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// input
            this.out = new DataOutputStream(socket.getOutputStream());// output
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //method that return a String with the username
    private String getNome() {
        return nome;
    }

    //method that return true if the usernane exixst inside the list
    private boolean issetName(String s) {
        boolean presente = false;
        for (ServerThread thread : partecipanti) {
            if (thread.getNome().equals(s)) {

                presente = true;
                break;
            }
        }
        return presente;
    }


    //run method-- is the most important part of the thread
    public void run() {
        try {
            //first insertion is the name of the thread
            String nom = in.readLine();
            this.nome = "@" + nom;
            //double nottification of an access and a communication to other users
            System.out.println(nom + " è entrato nel server");
            System.out.println(
                    "\u001B[32m" + "---" + getNome() + " e' conesso alla chat ---" + "\u001B[37m");
            broadcast("\u001B[32m" + "---" + getNome() + " e' conesso alla chat ---" + "\u001B[37m");

            
            while (!socket.isClosed()) {
                //while the user is connected the server waits for a string
                String messaggio = in.readLine();
                //stamping thon server what message contains
                System.out.println("messaggio da " + nome + " con scritto " + messaggio);
                // checking for unicast
                if (messaggio.startsWith("@") && issetName(messaggio)) {
                    System.out.println("siamo nell'unicast");
                    //getting the name for unicast
                    String nomet = messaggio;
                    //getting the message to send
                    messaggio = in.readLine();
                    System.out.println("......." + messaggio);
                    //usign the unicast method
                    unicast(messaggio, nomet);
                
                }else if(messaggio.startsWith("@") && !issetName(messaggio) || patecipanti.size() == 1){
                     out.writeBytes("\u001B[31m" "+"ERRORE"+" \u001B[37m");
                //checking for closing
                } else if (messaggio.equals("/close")) {
                    System.out.println("chiuso");
                    //sending a broadcast message of disconnecting
                    broadcast("\u001B[31m" + getNome() + " disconected \u001B[37m");
                    //removing the user from the list an closing the connection with it
                    partecipanti.remove(currentThread());
                    socket.close();
                    //if the message is not special it's send to everyone
                } else {
                    broadcast(messaggio);
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            partecipanti.remove(currentThread());
            try {
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }

    //broadcast method that communicate a message to everyone
    private void broadcast(String message) throws IOException {
        for (ServerThread a : partecipanti) {
            if (currentThread() != a)
                //the method uses the send method
                a.send(message, nome);
        }
    }

    //unicast method it sends a message to specified user and not to everybody
    private void unicast(String message, String name) throws IOException {
        for (ServerThread a : partecipanti) {
            System.out.println("nome preso " + name + "nome del thred ciclato" + a.getNome());
            if (name.equals(a.getNome())) {
                //using the send method.the message is completed with the color
                a.send("\u001B[36m" + message + "\u001B[37m", nome);
            }
        }
    }

    //method that sends a string(message)
    private void send(String message, String name) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());// output
        out.writeBytes(name + "==>\t" + message + "\n");
    }

}
