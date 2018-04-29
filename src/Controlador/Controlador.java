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
            
            Modelo mo = new Modelo();
            mo.CrearTablas();
            
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
                    
                    st.executeUpdate("INSERT INTO CLIENTE(ID_CLIENTE, NOMBRE, DIRECCION) " + "VALUES (" + (i+1) + ", " + nombre + ", " + dir);
                } 
                System.out.println("Elemento: "+ nodo2.getNodeName());
                if(nodo2.getNodeType() == Node.ELEMENT_NODE){ //ELEMENT_NODE para verificar si se trata de una etiqueta de XML
                    Element element = (Element) nodo2;
                    System.out.println(element.getElementsByTagName("costo_total").item(0).getTextContent());
                    monto_fac = Integer.parseInt(element.getElementsByTagName("costo_total").item(0).getTextContent());
                    st.executeUpdate("INSERT INTO FACTURA(ID_FACTURA, MONTO_FAC) " + "VALUES (" + (i+1) + ", " + monto_fac);
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
                    
                    st.executeUpdate("INSERT INTO VEHICULO(ID_VEHICULO, ID_FACTURA, PLACA, MODELO, MARCA) " + "VALUES (" + (i+1) + ", " + (i+1) + ", " + placas + ", " + modelo + ", " + marca);
                }
               
                
            }
            
            
            
            
        } catch (Exception e) {
        }
    }

    public static void BuscarCliente(String nombre, String direccion) {

    }

    public static void main(String[] args) {
        try {
            File archivo = new File("/home/nestorivanmo/Desktop/docs/Clientes.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.parse(archivo);

            System.out.println("Elemento raíz: " + document.getDocumentElement().getNodeName());
            NodeList listaClientes = document.getElementsByTagName("cliente");

            for (int i = 0; i < listaClientes.getLength(); i++) {
                Node nodo = listaClientes.item(i);
                System.out.println("Elemento: " + nodo.getNodeName());

                if (nodo.getNodeType() == Node.ELEMENT_NODE) { //ELEMENT_NODE para verificar si se trata de una etiqueta de XML
                    Element element = (Element) nodo;
                    System.out.println("Nombre: " + element.getElementsByTagName("nombre").item(0).getTextContent());
                    System.out.println("Direccion: " + element.getElementsByTagName("direccion").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
