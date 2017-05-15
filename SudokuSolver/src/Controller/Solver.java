package Controller;

//Classe que contém os métodos para buscar a solução.
import java.util.ArrayList;

public class Solver {

    public boolean buscaRestricoes(int[][] MSudoku, int i, int j, int R) {

        if (j == 9) {
            j = 0;  //Não existe coluna 9, chegou no limite. Volta para a primeira coluna.
            if (++i == 9) { //Não existe linha 9, chegou no final do Sudoku.
                if (ValidarSudoku(MSudoku)) {
                    return true; // Retorna true pois para chegar no final é preciso preencher todas as outras células corretamente.
                } else {
                    return buscaRestricoes(MSudoku, 0, 0, R + 1);
                }
            }
        }

        ArrayList<Integer> nValidos = new ArrayList();

        if (MSudoku[i][j] != 0) { //Já possui valor, recursão na próxima coluna.
            return buscaRestricoes(MSudoku, i, j + 1, R);
        }

        for (int v = 1; v <= 9; v++) {
            if (Valido(MSudoku, i, j, v)) {
                nValidos.add(v);
            }
        }

        switch (R) {
            case 0:
                if (nValidos.size() == 1) {
                    MSudoku[i][j] = nValidos.get(0);
                    if (buscaRestricoes(MSudoku, i, j + 1, R)) {
                        return true;
                    }
                    MSudoku[i][j] = 0;
                    return false;
                } else if (nValidos.size() == 0) {
                    return false;
                } else {
                    return buscaRestricoes(MSudoku, i, j + 1, R);
                }

            case 1:
                if (nValidos.size() >= 1) {
                    for (int D = 0; D < nValidos.size(); D++) {
                        MSudoku[i][j] = nValidos.get(D);
                        if (buscaRestricoes(MSudoku, 0, 0, 0)) {
                            return true;
                        }
                    }

                    MSudoku[i][j] = 0;
                    return false;
                } else {
                    return false;
                }
        }

        return false;
    }

    public boolean buscaCega(int[][] MSudoku, int i, int j) {
        if (j == 9) {
            j = 0;  //Não existe coluna 9, chegou no limite. Volta para a primeira coluna.
            if (++i == 9) { //Não existe linha 9, chegou no final do Sudoku.
                return true; // Retorna true pois para chegar no final é preciso preencher todas as outras células corretamente.
            }
        }

        if (MSudoku[i][j] != 0) { //Já possui valor, recursão na próxima coluna.
            return buscaCega(MSudoku, i, j + 1);
        }

        // Procura por um número válido.
        for (int v = 1; v <= 9; v++) {
            if (Valido(MSudoku, i, j, v)) {
                MSudoku[i][j] = v;
                // Recursão para a próxima coluna.
                if (buscaCega(MSudoku, i, j + 1)) {
                    return true;
                }
            }
        }

        //Solução 'v' proposta falhou, volta para a chamada anterior e testa outro valor 'v'. 
        MSudoku[i][j] = 0;
        return false;

    }

    public boolean Valido(int[][] MSudoku, int i, int j, int valor) {
        for (int col = 0; col < 9; col++) { //Percorre a coluna para descobrir se o número candidato já existe nela.
            if (MSudoku[col][j] == valor) {
                return false;
            }
        }
        for (int lin = 0; lin < 9; lin++) { //Percorre a linha para descobrir se o número candidato já existe nela.
            if (MSudoku[i][lin] == valor) {
                return false;
            }
        }
        int boxLin = (j / 3) * 3; //Obs: Divisão de números inteiros. Retorna o piso.
        int boxCol = (i / 3) * 3;
        for (int boxL = 0; boxL < 3; boxL++) { //Percorre a grid 3x3 para descobrir se o número candidato já existe nela.
            for (int boxC = 0; boxC < 3; boxC++) {
                if (MSudoku[boxCol + boxC][boxLin + boxL] == valor) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean ValidarSudoku(int[][] MSudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (MSudoku[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
