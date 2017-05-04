/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Arthur
 */
public class Solver {

    public boolean buscaCega(int[][] MSudoku, int i, int j) {
        if (i == 9 && j == 0) {
            return true;
        }

        if (MSudoku[i][j] != 0) {
            if (++j == 9) {
                return buscaCega(MSudoku, i+1, 0);
            } else {
                return buscaCega(MSudoku, i, j+1);
            }
        } else {
            for (int valor = 1; valor <= 9; valor++) {
                if (valido(MSudoku, i, j, valor)) {
                    MSudoku[i][j] = valor;

                    if (buscaCega(MSudoku, i, j+1)) {
                        return true;
                    }
                }
            }
            MSudoku[i][j] = 0;
            return false;
        }
    }

    public boolean buscaCega2(int[][] MSudoku, int i, int j) {
        if (i == 9) {
            i = 0; // row 9 doesn't exist, overflow back to 0!
            if (++j == 9) { // col 9 doesn't exist! You've reach the end of the grid!
                return true; // By right, that must be the solution.
            }
        }

        if (MSudoku[i][j] != 0) { // Already answered, recurse somewhere else!
            return buscaCega2(MSudoku, i+1, j);
        }

        // Keep filling in numbers until they are valid.
        for (int v = 1; v <= 9; v++) {
            if (valido(MSudoku, i, j, v)) {
                MSudoku[i][j] = v;
                // Recurse into child node.
                if (buscaCega2(MSudoku, i+1, j)) {
                    return true;
                }
            }
        }

        //System.out.print(".");
        // This solution failed, backtracking...
        MSudoku[i][j] = 0;
        return false;

    }

    public boolean valido(int[][] MSudoku, int i, int j, int valor) {
        for (int col = 0; col < 9; col++) {
            if (MSudoku[col][j] == valor) {
                return false;
            }
        }
        for (int lin = 0; lin < 9; lin++) {
            if (MSudoku[i][lin] == valor) {
                return false;
            }
        }
        int boxLin = (j / 3) * 3;
        int boxCol = (i / 3) * 3;
        for (int boxL = 0; boxL < 3; boxL++) {
            for (int boxC = 0; boxC < 3; boxC++) {
                if (MSudoku[boxCol + boxC][boxLin + boxL] == valor) {
                    return false;
                }
            }
        }
        return true;
    }
}
