package com.company;

import java.util.Scanner;


public class JuegoLocal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int minasEncontradaP1 = 0;
        int minasEncontradasP2 = 0;
        boolean jugador;
        boolean validez;
        //para repetir turno si no se introducen bien los parámetros

        Tablero tablero = new Tablero(8, 10);
        jugador = true; //va cambiando cada turno

        while (minasEncontradaP1 + minasEncontradasP2 != tablero.numMinas) {
            //mientras no se encuentren todas las minas
            System.out.println("TURNO DEL JUGADOR " + jugador);

            do {
                validez = false;
                tablero.mostrarTablero();

                System.out.print("\nIntroduce (fila y columna):  ");
                String tirada = scanner.nextLine();

                try {
                    int minaEncontrada = tablero.recorrer(tirada, jugador);


                    if (minaEncontrada == 1 && jugador == true) minasEncontradaP1++;
                    if (minaEncontrada == 1 && jugador == false) minasEncontradasP2++;
                    if (minaEncontrada != -1) validez = true;
                    if (minaEncontrada == 0) System.out.println("No has descubierto ninguna mina");

                } catch (Exception e) {
                    System.err.println("¡Ups! parece que has puesto valor incorrecto");
                }


            }while(!validez);


            System.out.println("QUEDAN " + (tablero.numMinas - minasEncontradaP1 + minasEncontradasP2) + " MINAS RESTANTES");
            if (jugador) jugador = false;
            if (!jugador) jugador = true;

        }

        System.out.println("Game Over");
        if (minasEncontradaP1 > minasEncontradasP2) System.out.println("Has ganado jugador 1");
        if (minasEncontradasP2 > minasEncontradaP1) System.out.println("Has ganado jugador 2");
        if (minasEncontradaP1 == minasEncontradasP2) System.out.println("EMPATE");

        scanner.close();

    }


}