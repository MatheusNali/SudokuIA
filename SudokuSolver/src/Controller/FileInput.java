package Controller;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author Nali
 */
public class FileInput {
	
    public int[][] readInput() throws Exception {
        
        int[][] input = new int[9][9];
        
        FileReader File = new FileReader("SudokuInput.txt");
        BufferedReader Reader = new BufferedReader(File);

        String Line = Reader.readLine();
        
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                input[i][j] = Integer.parseInt(Line.split(" ")[j]);
            }
            Line = Reader.readLine();
        }
        
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(input[i][j]+" ");
            }
            System.out.print("\n");
        }
        
        return input;

    }
}
