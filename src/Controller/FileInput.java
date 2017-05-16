package Controller;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileInput {

    /**
     * Função que lê um arquivo de dados contendo o puzzle e retorna um vetor
     * de inteiro com o valor obtido.
     * @param Path
     * @return data[9][9]
     * @throws Exception 
     */
    public int[][] readInput(String Path) throws Exception {

        int[][] input = new int[9][9]; //Matriz 9x9 que será preenchida com os números do arquivo "SudokuInput.txt"

        FileReader File = new FileReader(Path);  
        BufferedReader Reader = new BufferedReader(File);

        String Line = Reader.readLine();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                input[i][j] = Integer.parseInt(Line.split(" ")[j]);
            }
            Line = Reader.readLine();
        }

        return input;

    }
}

/* 0 representa os espaços não preenchidos.

Exemplo do arquivo txt:
6 0 0 0 0 0 2 9 5
7 0 0 4 9 0 6 0 0
2 8 0 0 5 0 0 0 0
0 0 0 9 2 7 0 3 0
0 9 2 8 0 5 7 1 0
0 4 0 1 6 3 0 0 0
0 0 0 0 3 0 0 5 9
0 0 3 0 7 8 0 0 2
4 2 8 0 0 0 0 0 7

*/