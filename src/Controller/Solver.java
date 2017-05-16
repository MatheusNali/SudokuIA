package Controller;

//Classe que contém os métodos para buscar a solução.
import Utils.Coordinates;
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
    
    /**
     *
     * @param sudokuBoard // The sudoku board to be solved
     * 
     * @param subGroupOrder // The coordinate of the first element of the 3x3 subGrops
     *                      // in decreasing order of states already attributed.   
     * 
     * @param currSubGroup // Indicates the current position on the subGroupOrder vector
     * 
     * @param possibilityTable // Table that stores the possibilities for each Sudoku state
     * 
     * @param occurrenceVector
     * 
     * @param howManyToFill // Must be prepared from the caller of the function
     * @return
     */
    public boolean RareNumbersSearch(int[][] sudokuBoard, ArrayList<Coordinates> subGroupOrder, 
            int currSubGroup, ArrayList<ArrayList<ArrayList<Integer>>> possibilityTable, 
            ArrayList<ArrayList<Integer>> occurrenceVector, ArrayList<Integer> howManyToFill) {   
        
        if(currSubGroup >= 9) {
            return true; // Finished the Sudoku
        }
        
        // Update the current sub group possibilityTable
        updateLocalPossibilityTable(possibilityTable, sudokuBoard, 
            subGroupOrder.get(currSubGroup).x(), subGroupOrder.get(currSubGroup).y());
                
        // Update the current sub group occurrence vector 
        updateLocalOccurrenceVector(occurrenceVector, currSubGroup, possibilityTable, 
            subGroupOrder.get(currSubGroup).x(), subGroupOrder.get(currSubGroup).y());
        
        int smallestValue = 20; // To find the number that most occurr on the sub group
        int killerNumber = -1; // Stores the number that occurs more on the subGroup possibility
        // Find the number with less repetition on the occurrence vector of the current state
        for(int i = 0; i < 9; i++) {
            if(smallestValue > occurrenceVector.get(currSubGroup).get(i)) {
                if(occurrenceVector.get(currSubGroup).get(i) == 0) {
                    continue;
                }
                smallestValue = occurrenceVector.get(currSubGroup).get(i);
                killerNumber = i;
            }
        }
        
        // Translate vector position to the wanted number (The number 2 is in the position 1 of the vector)
        killerNumber++;
                
        int currX = subGroupOrder.get(currSubGroup).x();
        int currY = subGroupOrder.get(currSubGroup).y();        
        ArrayList<Coordinates> posToFill = new ArrayList<>();
        
        // Make an array with the places where you can put the killer number
        for(int i = currX; i < 3+currX; i++) {
            for(int j = currY; j < 3+currY; j++) {
                for(Integer currPossi : possibilityTable.get(i).get(j)) {
                    if(currPossi == killerNumber) {
                        posToFill.add(new Coordinates(i,j));
                    }                    
                }
            }
        }
        
        // Sort the vector of containing the places where you can put the killer number
        int smallestNumPossi;
        int smallestNumPossiPos = -1;
        ArrayList<Coordinates> posToFillSorted = new ArrayList<>();
        while(posToFill.isEmpty() == false) {
            smallestNumPossi = 15;
            for(int j = 0; j < posToFill.size(); j++) {
                int x = posToFill.get(j).x();
                int y = posToFill.get(j).y();
                if(smallestNumPossi > possibilityTable.get(x).get(y).size()) {
                    smallestNumPossi = possibilityTable.get(x).get(y).size();
                    smallestNumPossiPos = j;
                }
            }
            posToFillSorted.add(posToFill.get(smallestNumPossiPos));
            posToFill.remove(smallestNumPossiPos);
        }
        
        // Goes throght the Sorted positions list to try to put the killer number on them 
        for(Coordinates currCoord : posToFillSorted) {
            sudokuBoard[currCoord.x()][currCoord.y()] = killerNumber;
            // Sets the number of states to fill on the Sub Group (3x3) to be less 1
            howManyToFill.set(currSubGroup, howManyToFill.get(currSubGroup)-1);
            
            // Check if it already have filled all the states on the sub group (3x3)
            if(howManyToFill.get(currSubGroup) == 0) { // if yes                
                
                if(RareNumbersSearch(sudokuBoard, subGroupOrder, currSubGroup+1, possibilityTable, 
                        occurrenceVector, howManyToFill)) { // 
                    
                    return true;
                    
                } else {
                    
                    // Reset the previous wrong attribution
                    sudokuBoard[currCoord.x()][currCoord.y()] = 0;
                    howManyToFill.set(currSubGroup, howManyToFill.get(currSubGroup)+1);
                    
                }
            } else { // if it had not filled all the available states on the sub group
                
                if(RareNumbersSearch(sudokuBoard, subGroupOrder, currSubGroup, possibilityTable, 
                        occurrenceVector, howManyToFill)) {
                    
                    return true;
                    
                } else {
                    
                    // Reset the previous wrong attribution 
                    sudokuBoard[currCoord.x()][currCoord.y()] = 0;
                    howManyToFill.set(currSubGroup, howManyToFill.get(currSubGroup)+1);
                    
                }
                
            }
        }
        
        return false;
    }
    
    // Supposes that the currSubGroup does not have any value on its occurrence vector
    private void updateLocalOccurrenceVector(ArrayList<ArrayList<Integer>> occurrenceVector, 
            int currSubGroup, ArrayList<ArrayList<ArrayList<Integer>>> possibilityTable, int startX, int startY) {
        
        // Clear the previous vector 
        occurrenceVector.get(currSubGroup).clear();
        
        // Adds nine 0's on the empty currSubGroup occurrence vector 
        for(int i = 0; i < 9; i++) {        
            occurrenceVector.get(currSubGroup).add(0);            
        }
        
        // Calculate the occurrences
        // Goes through the sub group matrix 
        for(int i = startX; i < 3+startX; i++) {
            for(int j = startY; j < 3+startY; j++) {
                // If there is no possible value, do nothing
                if(possibilityTable.get(i).get(j).isEmpty()) {
                    continue;
                }
                // incrment the counter of the possibilities found
                for(int k = 0; k < possibilityTable.get(i).get(j).size(); k++) {  
                    int a = possibilityTable.get(i).get(j).get(k);
                    occurrenceVector.get(currSubGroup).set(a-1, occurrenceVector.get(currSubGroup).get(a-1)+1);                    
                }
            }
        }
        
    }
    
    
    private void updateLocalPossibilityTable(ArrayList<ArrayList<ArrayList<Integer>>> possibilityTable, 
            int[][] sudokuBoard, int startX, int startY) {
                    
        // Goes through the Sub Group Matrix
        for(int i = startX; i < 3+startX; i++) {
            for(int j = startY; j < 3+startY; j++) { 
                // Clear the local state previous possibilities 
                possibilityTable.get(i).get(j).clear(); 
                // If there are already an value, do nothing 
                if(sudokuBoard[i][j] != 0) {
                    // will let the List empty
                    continue;
                }                               
                // Checks all possible values to see if they apply for that state
                for(int value = 1; value < 10; value++) {
                    if(Valido(sudokuBoard, i, j, value)) {
                        possibilityTable.get(i).get(j).add(value);
                    }
                }
            }
        }
    }    
}
