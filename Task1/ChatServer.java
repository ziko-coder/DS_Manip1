package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ChatServer extends Thread {
    private List<Conversation> conversations = new ArrayList<Conversation>();
    int clientsCount = 0;

    public static void main(String[] args){
        new ChatServer().start();
    }
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(4444);
            while (true) {
                Socket socket = ss.accept();
                ++clientsCount;
                Conversation conv = new Conversation(socket, clientsCount);
                conversations.add(conv);
                conv.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    class Conversation extends Thread{

        private int clientId;
        private Socket socket;

        public Conversation(Socket socket, int clientId ) {
            this.socket = socket;
            this.clientId = clientId;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("New connection from Number"+ clientId +" IP= " + socket.getRemoteSocketAddress());
                out.println("U are Client Number: " + clientId);

                String request;
                String message = "";
                List<Integer> ids = new ArrayList<Integer>();

                while( (request = in.readLine()) != null) {
                    if(request.contains("=>")) {
                        ids = new ArrayList<Integer>();
                        String[] items = request.split("=>");
                        String clients = items[0];
                        message = items[1];

                        if (clients.contains(",")) {
                            String[] idsListStr = clients.split(",");
                            for(String id: idsListStr) {
                                ids.add(Integer.parseInt(id));
                            }
                        }else {
                            ids.add(Integer.parseInt(clients));
                        }

                    } else {
                        message = request;
                        for (Conversation c : conversations) {
                            ids.add(c.clientId);
                        }
                    }
                    System.out.println("New request =>" + request+ "from" +socket.getRemoteSocketAddress());
                    broadcastMessage(message,socket,ids);
                }

            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        public void broadcastMessage(String message, Socket from, List<Integer> clientIds) {
            try {
                for (Conversation conversation: conversations) {
                    Socket s = conversation.socket;
                    if ((s!=from) && clientIds.contains(conversation.clientId)) {
                        OutputStream out = socket.getOutputStream();
                        PrintWriter in = new PrintWriter(out, true);
                        in.println(message);
                    }
                }

            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}



