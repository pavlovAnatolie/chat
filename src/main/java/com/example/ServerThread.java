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

    public ServerThread(Socket socket, ArrayList<ServerThread> partecipanti) {
        this.socket = socket;
        this.partecipanti = partecipanti;

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// input
            this.out = new DataOutputStream(socket.getOutputStream());// output
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String getNome() {
        return nome;
    }

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

    public void run() {
        try {
            String nom = in.readLine();
            this.nome = "@" + nom;
            System.out.println(nom + " è entrato nel server");
            System.out.println(
                    "\u001B[32m" + "---" + getNome() + " e' conesso alla chat ---" + "\u001B[37m");
            broadcast("\u001B[32m" + "---" + getNome() + " e' conesso alla chat ---" + "\u001B[37m");

            while (!socket.isClosed()) {
                String messaggio = in.readLine();
                System.out.println("messaggio da " + nome + " con scritto " + messaggio);
                // controlliamo se unicast
                if (messaggio.startsWith("@") && issetName(messaggio)) {
                    System.out.println("siamo nell'unicast");
                    String nomet = messaggio;
                    messaggio = in.readLine();
                    System.out.println("......." + messaggio);
                    unicast(messaggio, nomet);
                } else if (messaggio.equals("/close")) {
                    System.out.println("chiuso");
                    broadcast("\u001B[31m" + getNome() + " disconected \u001B[37m");
                    partecipanti.remove(currentThread());
                    socket.close();
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

    private void broadcast(String message) throws IOException {
        for (ServerThread a : partecipanti) {
            if (currentThread() != a)
                a.send(message, nome);
        }
    }

    private void unicast(String message, String name) throws IOException {
        for (ServerThread a : partecipanti) {
            System.out.println("nome preso " + name + "nome del tred ciclato" + a.getNome());
            if (name.equals(a.getNome())) {
                a.send("\u001B[36m" + message + "\u001B[37m", nome);
            }
        }
    }

    private void send(String message, String name) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());// output
        out.writeBytes(name + "==>\t" + message + "\n");
    }

}
