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
                String path = "Puzzle" + view.getFileNumber() + ".txt";
                int[][] data = file.readInput(path);
                view.updateTable(data);
            } catch (Exception e) {
                view.setText("Nenhum arquivo encontrado.");
            }
        } else if (ae.getActionCommand().equals("Profundidade")) {
            int[][] data = view.getData(); //Chama método que preenche a matriz data com valores obtidos do arquivo de texto "SudokuInput.txt".
            long startTime = System.nanoTime();
            solucionador.buscaCega(data, 0, 0); //Método para buscar solução com busca cega.
            long finalTime = System.nanoTime();
            view.updateTable(data); //Atualiza tabela com a solução.
            view.setText("Tempo: " + (finalTime-startTime)/1000 + " us");
        } else if (ae.getActionCommand().equals("Heuristica")) {
            int[][] data = view.getData();
            long startTime = System.nanoTime();
            //solucionador.buscaHeuristica();
            long finalTime = System.nanoTime();
            view.updateTable(data); //Atualiza tabela com a solução.
            view.setText("Não tem nada aqui, mas... Tempo: " + (finalTime-startTime)/1000 + " us");
        } else if (ae.getActionCommand().equals("Restricao")) {
            int[][] data = view.getData();

            long startTime = System.nanoTime();
            solucionador.buscaRestricoes(data, 0, 0, 0);
            long finalTime = System.nanoTime();
            view.updateTable(data);
            view.setText("Tempo: " + (finalTime-startTime)/1000 + " us");
        }
    }
}
