/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Viewer.Janela;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Arthur
 */
public class Controller implements ActionListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
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
        if(ae.getActionCommand().equals("Carregar")) {
            try {
                int[][] data = file.readInput();
                view.updateTable(data);
            }
            catch(Exception e) {
                System.out.println("Nenhum arquivo encontrado. Erro: " + e.toString());
            }
        }
        else if (ae.getActionCommand().equals("Profundidade")) {
            int[][] data = view.getData();
            solucionador.buscaCega2(data,0,0);
            view.updateTable(data);
        }
        else if (ae.getActionCommand().equals("Heuristica")) {
            System.out.println("Não tem nada aqui");
        }
        else if (ae.getActionCommand().equals("Restricao")) {
            System.out.println("Para de clicar em mim");
        }
    }
}
