package com.company;

import java.util.Scanner;

public class Juego {

    private boolean validez;
    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    private int minasEncontradasP1 = 0;
    private int minasEncontradasP2 = 0;

    public int getMinasEncontradasP1() {
        return minasEncontradasP1;
    }

    public int getMinasEncontradasP2() {
        return minasEncontradasP2;
    }

    public Tablero start(String coor, Tablero tablero, boolean jugador) {


        try {
            int minaEncontrada = tablero.recorrer(coor, jugador);

            if (minaEncontrada == 1 && jugador) minasEncontradasP1++;
            if (minaEncontrada == 1 && !jugador) minasEncontradasP2++;
            if (minaEncontrada != -1) validez = true;
        } catch (Exception e) {
            System.err.println(e);
        }
        if (minasEncontradasP1 + minasEncontradasP2 == tablero.numMinas) gameOver = true;
        return tablero;
    }
}