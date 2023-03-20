package com.company.NonBlockingIO;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class NonBlockingServer {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public void start() throws Exception {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(4444));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int readyChannels = selector.select();

            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                }
                keyIterator.remove();
            }
        }
    }

    private void accept(SelectionKey key) throws Exception {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        buffer.clear();
        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            channel.close();
            key.cancel();
            return;
        }
        buffer.flip();
        String message = new String(buffer.array(), 0, bytesRead);
        System.out.println("Received: " + message);
        channel.write(ByteBuffer.wrap(("Echo: " + message).getBytes()));
    }

    public static void main(String[] args) throws Exception {
        NonBlockingServer server = new NonBlockingServer();
        server.start();
    }
}
