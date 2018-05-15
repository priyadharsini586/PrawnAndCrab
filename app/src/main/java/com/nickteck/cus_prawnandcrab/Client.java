package com.nickteck.cus_prawnandcrab;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Thread {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    String disconnectReason;
    boolean disconnected = false;
    public boolean running;
    String TAG = Client.class.getName();

    int port = 20222;
    ServerSocket listenSock = null; //the listening server socket
    Socket sock = null;
    public static void main(String[] args) {
        Client client = new Client();
        client.sendMessage("Hello!");
    }

    public Client() {
        new Thread(this).start();
        running = true;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("192.168.1.6", 25003);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (running) {
                String line;
                if((line = br.readLine()) != null) {
                    Log.e(TAG, "Server response:  linse "+line );
                    System.out.println("Server response: " + line);
                }
            }
            Log.e(TAG, "Disconnected. Reason: " + disconnectReason);
            disconnected = true;
            running = false;
            br.close();
            out.close();
            socket.close();
            System.out.println("Shutted down!");
            System.exit(0);
        } catch (IOException e) {
            if(e.getMessage().equals("Connection reset")) {
                disconnectReason = "Connection lost with server";
                disconnected = true;
                Log.e(TAG, "Disconnected from server. Reason: Connection reset");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if(running) {
            if (!disconnected) {
                if (out != null && socket != null) {
                    out.println(message);
                    out.flush();
                }
            }
        }
    }
}