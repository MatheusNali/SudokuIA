package Controller;

import Viewer.Janela;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import Utils.Coordinates;

public class Controller implements ActionListener {

    public static void main(String[] args) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Janela view = new Janela();
                Controller controller = new Controller();
                view.setVisible(true);
                view.addController(controller);
            }
        });
    }
    //Variaveis -------------------------------------------------------------
    //View
    Janela view;
    FileInput file;
    Solver solucionador;

    //Construtor
    public Controller() {
        file = new FileInput();
        solucionador = new Solver();
    }

    //Métodos ------------------------------------------------------------------
    /**
     * Define view do programa
     * @param newView 
     */
    public void setView(Janela newView) {
        view = newView;
    }

    /**
     * Define ações para quando clicar no botão
     * @param ae 
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        //Botao carregar
        if (ae.getActionCommand().equals("Carregar")) {
            try {
                String path = "Puzzle" + view.getFileNumber() + ".txt";
                int[][] data = file.readInput(path);
                view.updateTable(data);
            } catch (Exception e) {
                view.setText("Nenhum arquivo encontrado.");
            }
        } 
        
        //Botao de busca em profundidade
        else if (ae.getActionCommand().equals("Profundidade")) {
            int[][] data = view.getData(); //Chama método que preenche a matriz data com valores obtidos do arquivo de texto "SudokuInput.txt".
            long startTime = System.nanoTime();
            Boolean resultado = solucionador.buscaCega(data, 0, 0); //Método para buscar solução com busca cega.
            long finalTime = System.nanoTime();
            if (resultado) {
                view.updateTable(data); //Atualiza tabela com a solução.
                view.setText("Tempo: " + (finalTime-startTime)/1000 + " us");
            }
            else
                view.setText("Não tem solução. Tempo: " + (finalTime-startTime)/1000 + " us");
        } 
        
        //Botao de busca heurística
        else if (ae.getActionCommand().equals("Heuristica")) {
            int[][] data = view.getData();
            
            // Allocate both the possibilityTable and the occurrenceVector
            ArrayList<ArrayList<ArrayList<Integer>>> possibilityTable = new ArrayList<>();
            ArrayList<ArrayList<Integer>> occurrenceVector = new ArrayList<>();
            for(int i = 0; i < 9; i++) {
                possibilityTable.add(new ArrayList<>());
                occurrenceVector.add(new ArrayList<>());
                for(int j = 0; j < 9; j++) {
                    possibilityTable.get(i).add(new ArrayList<>());
                }
            }   
            
            // Allocate and instantiate both the subGroupOrder and the howManyToFill
            ArrayList<Coordinates> subGroupOrder = new ArrayList<>();
            // This Array holds the number of states to be filled on each subGroup(3x3)
            ArrayList<Integer> howManyToFill = new ArrayList<>();
            
            getSubGroupOrder(data, subGroupOrder);
            for(Coordinates currCoord : subGroupOrder) {
                howManyToFill.add(getNumberOfEmptyStatesOnSubGroup(data, currCoord.x(), currCoord.y()));
            }
            
            long startTime = System.nanoTime();
            
            Boolean resultado = solucionador.RareNumbersSearch(data, subGroupOrder, 0, possibilityTable, occurrenceVector,
                    howManyToFill);
            long finalTime = System.nanoTime();
            if (resultado) {
                view.updateTable(data); //Atualiza tabela com a solução.
                view.setText("Tempo: " + (finalTime-startTime)/1000 + " us");
            }
            else
                view.setText("Não tem solução. Tempo: " + (finalTime-startTime)/1000 + " us");
        } 
        
        //Botao de busca por restrição
        else if (ae.getActionCommand().equals("Restricao")) {
            int[][] data = view.getData();

            long startTime = System.nanoTime();
            Boolean resultado = solucionador.buscaRestricoes(data, 0, 0, 0);
            long finalTime = System.nanoTime();
            if (resultado) {
                view.updateTable(data);
                view.setText("Tempo: " + (finalTime-startTime)/1000 + " us");
            }
            else
                view.setText("Não tem solução. Tempo: " + (finalTime-startTime)/1000 + " us");
        }
    }
    
    /**
     * Cria um vetor ordenado com as cordenadas de ínício de cada quadrante, de maneira
     * que esteja em ordem decrescente de número de dicas naquele quadrante
     * @param sudokuBoard
     * @param subGroupOrder 
     */
    public void getSubGroupOrder(int[][] sudokuBoard, ArrayList<Coordinates> subGroupOrder) {
        
        // Temp array to store all the sub group's initial coordinates
        ArrayList<Coordinates> temp = new ArrayList<>();
        
        for(int i = 0; i < 9; i=i+3) {
            for(int j = 0; j < 9; j=j+3) {
                temp.add(new Coordinates(i, j));
            }
        }
        
        // Sort the Sub Groups according to how many empty states they have.
        // The one with the less empty states will be the first on the subGroupOrder vector
        int lessNumberOfStates = 15;
        int lessNumberPosition = 0;
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < temp.size(); j++) {
                if(lessNumberOfStates > getNumberOfEmptyStatesOnSubGroup(sudokuBoard, temp.get(j).x(), temp.get(j).y())) {
                    lessNumberOfStates = getNumberOfEmptyStatesOnSubGroup(sudokuBoard, temp.get(j).x(), temp.get(j).y());
                    lessNumberPosition = j;
                }
            }
            lessNumberOfStates = 15;
            subGroupOrder.add(new Coordinates(temp.get(lessNumberPosition).x(), temp.get(lessNumberPosition).y()));
            temp.remove(lessNumberPosition);
        }
    }
    
    /**
     * Função auxiliar para encontrar quantos grupos vazios possui um quadrante
     * @param sudokuBoard
     * @param startX
     * @param startY
     * @return numberOfEmptyStates
     */
    public int getNumberOfEmptyStatesOnSubGroup(int[][] sudokuBoard, int startX, int startY) {
        
        int counter = 0;
        
        // Goes through a local sub group and counts how many empty states it has
        for(int i = startX; i < 3+startX; i++) {
            for(int j = startY; j < 3+startY; j++) {
                if(sudokuBoard[i][j] == 0) {
                   counter++; 
                }
            }
        }
        
        return counter;
    }

}
