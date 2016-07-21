/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selecionador;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Fabio
 */
public class SelecionadorResultados {
    private int indexReturnado;    

    public SelecionadorResultados(String resultado) {
        switch(resultado){
            case "Positivo":
                indexReturnado = 2;
                break;
            case "Negativo":
                indexReturnado = 1;
                break;
            default:
                indexReturnado = 0;
                break;            
        }
    }

    public int getIndeReturnado() {
        return indexReturnado;
    }
     
}
