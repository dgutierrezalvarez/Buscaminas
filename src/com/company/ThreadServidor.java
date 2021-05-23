package com.company;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ThreadServidor implements Runnable{

    Socket clientSocket1 = null;
    Socket clientSocket2 = null;
    Tablero tablero;
    boolean turno = true;
    byte[] mensaje;
    Juego juego;
    String jugada;

    public ThreadServidor(Socket clientSocket1, Socket clientSocket2) {
        this.clientSocket1 = clientSocket1;
        this.clientSocket2 = clientSocket2;
        tablero = new Tablero(8, 10);
        juego = new Juego();
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream outToClient1 = new ObjectOutputStream(clientSocket1.getOutputStream());
            ObjectInputStream inFromClient1 = new ObjectInputStream(clientSocket1.getInputStream());

            ObjectOutputStream outToClient2 = new ObjectOutputStream(clientSocket2.getOutputStream());
            ObjectInputStream inFromClient2 = new ObjectInputStream(clientSocket2.getInputStream());

            outToClient1.writeObject("Ets el jugador 1");
            outToClient1.writeObject(true);

            outToClient2.writeObject("Ets el jugador 2");
            outToClient2.writeObject(false);

            Thread.sleep(2000);

            while(!juego.isGameOver()){
                outToClient1.reset();
                outToClient2.reset();

                //enviar turno
                if(turno){
                    outToClient1.writeObject(true);
                    outToClient2.writeObject(true);

                    outToClient1.writeObject(tablero);
                    outToClient1.writeObject("Fes la teva jugada");
                    outToClient2.writeObject("El jugador 1 està fent la seva jugada");

                    jugada = (String) inFromClient1.readObject();

                    //metodo que procesa la jugada y devuelve el tablero actualizado

                    tablero = juego.start(jugada, tablero, true);
                    outToClient1.reset();

                    outToClient1.writeObject(tablero);

                    turno = !turno;

                }else{
                    outToClient2.writeObject(false);
                    outToClient1.writeObject(false);

                    outToClient2.writeObject(tablero);
                    outToClient2.writeObject("Fes la teva jugada");
                    outToClient1.writeObject("El jugador 2 està fent la seva jugada");

                    jugada = (String) inFromClient2.readObject();

                    tablero = juego.start(jugada, tablero, false);

                    outToClient2.reset();
                    outToClient2.writeObject(tablero);

                    turno = !turno;
                }

                outToClient1.writeObject(juego.isGameOver());
                outToClient2.writeObject(juego.isGameOver());

                outToClient1.flush();
                outToClient2.flush();
            }

            if(juego.getMinasEncontradasP1() > juego.getMinasEncontradasP2()){
                outToClient1.writeObject("GANAS CON  " + juego.getMinasEncontradasP1() + " MINAS ENCONTRADAS");
                outToClient2.writeObject("GANA EL JUGADOR 1 CON  " +juego.getMinasEncontradasP1() + " MINAS ENCONTRADAS\nHAS ENCONTRADO: " + juego.getMinasEncontradasP2());
            }
            else if(juego.getMinasEncontradasP1() < juego.getMinasEncontradasP2()){
                outToClient2.writeObject("GANAS CON " + juego.getMinasEncontradasP2() + " mines trobades");
                outToClient1.writeObject("GANA EL JUGADOR 2 CON " +juego.getMinasEncontradasP2() + " MINAS ENCONTRADAS\nHAS ENCONTRADO: " + juego.getMinasEncontradasP1());
            }else{
                outToClient1.writeObject("EMPATE");
                outToClient2.writeObject("EMPATE");
            }

        }catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
