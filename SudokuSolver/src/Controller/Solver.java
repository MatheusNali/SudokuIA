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
        if(i==9 && j==0)
            return true;
        
        if (MSudoku[i][j]!=0) {
            if (j == 9) {
                return buscaCega(MSudoku, i++, 0);
            } 
            else {
                return buscaCega(MSudoku, i, j++);
            }
        }
        
        else {
            for(int valor=1; valor<=9; valor++){
                if(valido(MSudoku,i,j,valor)){
                    MSudoku[i][j]=valor;
                    if(buscaCega(MSudoku,i,j++))
                        return true;
                }
            }
            MSudoku[i][j]=0;
            return false;
        } 
    }
    public boolean valido(int[][] MSudoku, int i, int j, int valor){
        for(int col=0; col<9; col++){
                    if(MSudoku[col][j]==valor)
                        return false;
                }
                for(int lin=0; lin<9; lin++){
                    if(MSudoku[i][lin]==valor)
                        return false;
                }
                int boxLin = (j/3)*3;
                int boxCol = (i/3)*3;
                for (int boxL = 0; boxL<3; boxL++){
                    for(int boxC = 0; boxC<3; boxC++){
                        if(MSudoku[boxCol + boxC][boxLin + boxL]==valor)
                            return false;
                    }
                }
                return true;
    }
}
