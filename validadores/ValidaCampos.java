/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validadores;

import javax.swing.JOptionPane;

/**
 *
 * @author Fabio
 */
public class ValidaCampos {
    public boolean validaNomeCompleto(String nome){
        if (nome.equals("")) {
            return true;
        }else{
            return false;
        }
    }
    
    public boolean validaCampoNrCaso(int nrCaso){
        if (nrCaso <= 0) {
            return true;
        }else if(nrCaso > 500){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean validaCampoCPF(String nrCPF){
        if (nrCPF == null) {
            return false;
        }else{
            return true;
        }
    }
}
