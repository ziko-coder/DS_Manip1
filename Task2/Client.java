package com.company.NonBlockingIO;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 4444);
        OutputStream output = socket.getOutputStream();
        output.write("Hello, server!".getBytes());
        output.flush();
        InputStream input = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = input.read(buffer);
        String response = new String(buffer, 0, bytesRead);
        System.out.println("Response: " + response);
        socket.close();
    }
}
