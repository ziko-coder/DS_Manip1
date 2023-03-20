import socket

HOST = 'localhost'
PORT = 4444


def send_message(sock, message):
    sock.sendall(message.encode())


def receive_message(sock):
    data = sock.recv(1024)
    return data.decode()


def main():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((HOST, PORT))
        print('Connected to chat server')

        # Send a message to the server
        message = 'Hello from Python client!'
        send_message(s, message)

        # Receive a message from the server
        response = receive_message(s)
        print('Received message from server:', response)

        # Send another message to the server
        message = 'Another message from Python client'
        send_message(s, message)

        # Receive another message from the server
        response = receive_message(s)
        print('Received message from server:', response)


main()
