package com.company;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

    String hostname;
    int port;
    Tablero tablero;
    boolean terminado = false;
    String mensaje;
    boolean jugador, turno;
    String jugada;

    public Client(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void run(){
        Socket socket;
        Scanner scanner = new Scanner(System.in);

        try{
            socket = new Socket(InetAddress.getByName(hostname), port);
            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

            mensaje = (String)  inFromServer.readObject();
            System.out.println(mensaje);

            jugador = (boolean) inFromServer.readObject();

            while(!terminado){
                //checkear turno, y hacer jugadaa
                turno = (boolean) inFromServer.readObject();

                if(turno == jugador){
                    tablero = (Tablero) inFromServer.readObject();
                    mensaje = (String) inFromServer.readObject();

                    tablero.mostrarTablero();

                    System.out.println("\n" + mensaje);
                    System.out.println("\nCoordenades: ");
                    jugada = scanner.nextLine();

                    outToServer.writeObject(jugada);

                    tablero = (Tablero) inFromServer.readObject();
                    tablero.mostrarTablero();

                    terminado = (boolean) inFromServer.readObject();

                }else{
                    mensaje = (String) inFromServer.readObject();
                    System.out.println("\n"+mensaje);

                    terminado = (boolean) inFromServer.readObject();
                }

            }

            mensaje = (String) inFromServer.readObject();
            System.out.println("\n"+mensaje);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}