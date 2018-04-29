/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import Modelo.Modelo;

import java.io.File;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




/**
 *
 * @author nestorivanmo
 */
public class Controlador {

    private java.sql.Connection con = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    private static String url = "jdbc:mysql://localhost:3306";
    private static String user = "root";
    private static String password = "root";

    public void llenarDb() {
        try {
            String nombre = ""; 
            String dir = "";
            String marca = "", modelo = "", placas = "";
            int monto_fac = 0;
            int facV, costoPoliza, valorPrima;
            Modelo mo = new Modelo();
            mo.CrearTablas();
            String query = "";
            
            con = DriverManager.getConnection(url, user, password);
            Statement st = (Statement) con.createStatement();
            
            File clientes = new File("/home/nestorivanmo/Desktop/docs/Clientes.xml");
            File facturas = new File("/home/nestorivanmo/Desktop/docs/Facturas.xml");
            File vehiculos = new File("/home/nestorivanmo/Desktop/docs/Vehiculos.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.parse(clientes);
            Document doc2 = documentBuilder.parse(facturas);
            Document doc3 = documentBuilder.parse(vehiculos);
            
            NodeList listaClientes = document.getElementsByTagName("cliente");
            NodeList listaFacturas = doc2.getElementsByTagName("factura");
            NodeList listaVehiculos = doc3.getElementsByTagName("vehiculo");

            for(int i = 0; i<listaClientes.getLength(); i++){
                Node nodo = listaClientes.item(i);
                Node nodo2 = listaFacturas.item(i);
                Node nodo3 = listaVehiculos.item(i);
                System.out.println("Elemento: "+ nodo.getNodeName());
                
                if(nodo.getNodeType() == Node.ELEMENT_NODE){ //ELEMENT_NODE para verificar si se trata de una etiqueta de XML
                    Element element = (Element) nodo;
                    System.out.println(element.getElementsByTagName("nombre").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("direccion").item(0).getTextContent());
                    
                    nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    dir = element.getElementsByTagName("direccion").item(0).getTextContent();
                    
                    st.executeUpdate("INSERT INTO CLIENTE(ID_CLIENTE, NOMBRE, DIRECCION) " + "VALUES (" + (i+1) + ", " + nombre + ", " + dir + ")");
                } 
                System.out.println("Elemento: "+ nodo2.getNodeName());
                if(nodo2.getNodeType() == Node.ELEMENT_NODE){ //ELEMENT_NODE para verificar si se trata de una etiqueta de XML
                    Element element = (Element) nodo2;
                    System.out.println(element.getElementsByTagName("costo_total").item(0).getTextContent());
                    monto_fac = Integer.parseInt(element.getElementsByTagName("costo_total").item(0).getTextContent());
                    st.executeUpdate("INSERT INTO FACTURA(ID_FACTURA, MONTO_FAC) " + "VALUES (" + (i+1) + ", " + monto_fac + ")");
                }
                System.out.println("Elemento: "+ nodo3.getNodeName());
                if(nodo3.getNodeType() == Node.ELEMENT_NODE){ //ELEMENT_NODE para verificar si se trata de una etiqueta de XML
                    Element element = (Element) nodo3;
                    System.out.println(element.getElementsByTagName("placas").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("marca").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("modelo").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("id_factura").item(0).getTextContent());
                    
                    placas = element.getElementsByTagName("placas").item(0).getTextContent();
                    modelo = element.getElementsByTagName("modelo").item(0).getTextContent();
                    marca = element.getElementsByTagName("marca").item(0).getTextContent();
                    facV = Integer.parseInt(element.getElementsByTagName("id_factura").item(0).getTextContent());
                 
                    st.executeUpdate("INSERT INTO VEHICULO(ID_VEHICULO, ID_FACTURA, PLACA, MODELO, MARCA) " + "VALUES (" + (i+1) + ", " + facV + ", " + placas + ", " + modelo + ", " + marca + ")");
              
                      query = "SELECT f.MONTO_FAC FROM FACTURA AS f INNER JOIN VEHICULO AS v ON f.ID_FACTURA = v.ID_FACTURA";
                
                }

               costoPoliza = obtenerCostoPoliza(query);
               valorPrima = obtenerValorPrima(query);
               
               
                
                st.executeUpdate("INSERT INTO POLIZA(ID_POLIZA, ID_VEHICULO, ID_CLIENTE, COSTO_POLIZA, VALOR_PRIMA, FECHA_VENCIMIENTO, FECHA_APERTURA) " + "VALUES ("+ (i+1) +", "+(i+1)+", "+(i+1)+", "+costoPoliza+", "+valorPrima+", SELECT ADDDATE(SELECT CURDATE(), INTERVAL 31 DAY), SELECT CURDATE())");
                
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int obtenerCostoPoliza(String query){
        int cp = 0; //costo poliza, valor prima
        try{
          con = DriverManager.getConnection(url, user, password);
          Statement st = (Statement) con.createStatement();  
          rs = st.executeQuery(query);
          while(rs.next()){
              cp = (int) (rs.getInt("f.MONTO_FAC") *(6.67 * 12)/100);
          }
        }catch(SQLException e){}
        
        return cp;
    }
    
    public int obtenerValorPrima(String query){
        int vp = 0; //costo poliza, valor prima
        try{
          con = DriverManager.getConnection(url, user, password);
          Statement st = (Statement) con.createStatement();  
          rs = st.executeQuery(query);
          while(rs.next()){
              vp =  (int) (0.85 * rs.getInt("f.MONTO_FAC"));
          }
        }catch(SQLException e){}
        
        return vp;
    }

    public static void BuscarCliente(String nombre, String direccion) {

    }

}

