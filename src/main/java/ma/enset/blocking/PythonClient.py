import socket

HOST = 'localhost'
PORT = 1234

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))
welcome_msg = sock.recv(1024).decode()
print(welcome_msg)

while True:
    message = input(" ")
    sock.sendall(message.encode())
    # Réception de la réponse du serveur
    response = sock.recv(1024).decode()
    print(response)

sock.close()
