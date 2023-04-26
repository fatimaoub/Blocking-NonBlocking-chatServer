import socket

HOST = 'localhost'
PORT = 4444

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((HOST, PORT))

while True:
    request = input("Entrez une requête: ")
    client_socket.send(request.encode())
    response = client_socket.recv(1024).decode()
    print("Réponse du serveur: ", response)

client_socket.close()
