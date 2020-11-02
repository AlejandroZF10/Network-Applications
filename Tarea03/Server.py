import socket

print("Servidor iniciado")

server = socket.socket()
server.bind(('localhost',8000))
server.listen(5)

datos = [['Nombre','tiempo','Status']]

while True:
    conexion,address = server.accept()
    print(f"Cliente conectado desde {address}!")
    conexion.send(bytes("Bienvenido al servidor!","utf-8"))

    while True:
        data = conexion.recv(1024)
        option = int(str(data.decode()))
        if option == 1: #Sending stadistics
            conexion.send(bytes(str(len(datos)),"utf-8"))
            data = conexion.recv(1024)
            respuesta = str(data.decode())
            print(respuesta)
            for i in range(len(datos)):
                for j in range(len(datos[i])):
                    conexion.send(bytes(str(datos[i][j])+"@","utf-8"))
                    data = conexion.recv(1024)
                    respuesta = str(data.decode())
            print("Datos enviados!")
            nombre = input("Introduce tu nombre: ")+"@"
            conexion.send(bytes(nombre,"utf-8"))
            data = conexion.recv(1024)
            respuesta = str(data.decode())
            palabra = input("Introduce la palabra: ")+"@"
            conexion.send(bytes(palabra,"utf-8"))
            data = conexion.recv(1024)
            respuesta = str(data.decode())
        if option == 2:
            data = conexion.recv(1024)
            name = str(data.decode())
            conexion.send(bytes("Nombre recibido","utf-8"))
            data = conexion.recv(1024)
            time = str(data.decode())
            conexion.send(bytes("Tiempo recibido","utf-8"))
            data = conexion.recv(1024)
            status = str(data.decode())
            conexion.send(bytes("Status recibido","utf-8"))
            datos.append([name,time,status])
        if option == 3:
            conexion.close()
            print("Cliente desconectado")
            break;
