/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaavanzado_seguros;
import Modelo.Modelo;
import Controlador.Controlador;

/**
 *
 * @author nestorivanmo
 */
public class JavaAvanzado_seguros {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Controlador cont = new Controlador();
        cont.llenarDb();
    }
    
}
