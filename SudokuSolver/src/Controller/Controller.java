package Controller;

import Viewer.Janela;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    //Métodos
    public void setView(Janela newView) {
        view = newView;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Carregar")) {
            try {
                int[][] data = file.readInput();
                view.updateTable(data);
            } catch (Exception e) {
                System.out.println("Nenhum arquivo encontrado. Erro: " + e.toString());
            }
        } else if (ae.getActionCommand().equals("Profundidade")) {
            int[][] data = view.getData(); //Chama método que preenche a matriz data com valores obtidos do arquivo de texto "SudokuInput.txt".
            solucionador.buscaCega(data, 0, 0); //Método para buscar solução com busca cega.
            view.updateTable(data); //Atualiza tabela com a solução.
        } else if (ae.getActionCommand().equals("Heuristica")) {
            System.out.println("Não tem nada aqui");
        } else if (ae.getActionCommand().equals("Restricao")) {
            int[][] data = view.getData();
            //int[][][] nValidos = new int[9][9][9]; //Matriz 9x9x9 onde duas dimensões referem-se à posição [i,j] no Sudoku e a outra dimensão refere-se aos possiveis valores daquela posição.
            
            ArrayList<Integer>[][] nValidos = new ArrayList[9][9];
            
            for(int A=0; A<9; A++){
                for(int B=0; B<9; B++){
                    nValidos[A][B] = new ArrayList<Integer>();
                }
            }
            
            solucionador.buscaRestricoes2(data, nValidos, 0, 0, 0);
            /*if(solucionador.buscaRestricoes2(data, nValidos, 0, 0, 0)){
            view.updateTable(data);
            }else{
                System.out.println("Não há solução possível");
            }*/
                        view.updateTable(data);
        }
    }
}
