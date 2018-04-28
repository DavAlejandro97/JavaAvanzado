/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.DriverManager;
import java.sql.Statement;
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

/**
 *
 * @author nestorivanmo
 */
public class Modelo {

    private java.sql.Connection con = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    private static String url = "jdbc:mysql://localhost:8889/deliveryEarn";
    private static String user = "root";
    private static String password = "root";

    public void CrearTablas() {
        try {
            con = DriverManager.getConnection(url, user, password);
            Statement st = (Statement) con.createStatement();

            String cliente = "CREATE TABLE CLIENTE "
                    + "(ID_CLIENTE INTEGER NOT NULL, "
                    + " NOMBRE VARCHAR(255), "
                    + " DIRECCION VARCHAR(255), "
                    + " PRIMARY KEY ( ID_CLIENTE ) )";

            st.executeUpdate(cliente);

            String poliza = "CREATE TABLE POLIZA "
                    + "(ID_POLIZA INTEGER NOT NULL, "
                    + " ID_VEHICULO INTEGER NOT NULL, "
                    + " ID_CLIENTE INTEGER NOT NULL, "
                    + " COSTO_POLIZA INTEGER, "
                    + " VALOR_PRIMA INTEGER, "
                    + " FECHA_VENCIMIENTO DATE, "
                    + " FECHA_APERTURA DATE, "
                    + " PRIMARY KEY ( ID_POLIZA )" 
                    + " FOREIGN KEY (ID_CLIENTE))"
                    + " FOREIGN KEY (ID_VEHICULO)";

            st.executeUpdate(poliza);
            
             String vehiculo = "CREATE TABLE VEHICULO "
                    + "(ID_VEHICULO INTEGER NOT NULL, "
                    + " PLACA VARCHAR(255), "
                    + " MODELO VARCHAR(255), "
                    + " MARCA VARCHAR(255)"
                    + " PRIMARY KEY ( ID_CLIENTE ) )"
                    + " FOREIGN KEY ( ID_FACTURA )";

            st.executeUpdate(vehiculo);
            
            String factura = "CREATE TABLE FACTURA "
                    + "(ID_FACTURA INTEGER NOT NULL, "
                    + " MONTO_FAC INTEGER NOT NULL, "
                    + " PRIMARY KEY ( ID_FACTURA ) )";

            st.executeUpdate(factura);
            con.close();

        } catch (Exception e) {
        }
    }
}
