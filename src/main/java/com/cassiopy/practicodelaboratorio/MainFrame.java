/*
    Práctico de laboratorio
    Base de datos
    Sofía Flores
    2022
*/
package com.cassiopy.practicodelaboratorio;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import java.util.Vector;
import java.io.*;
import java.net.URISyntaxException;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;

public class MainFrame extends javax.swing.JFrame {

    // Valores para la conexión a la base de datos (su nombre, URL, Usuario y Contraseña)
    private static final String DB_NAME = "Practico de laboratorio";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/" + DB_NAME; 
    private static final String DB_USER = "postgres";
    private static final String DB_PWD = "admin";
    
    // Objetos utilizados para interactuar con la base de datos
    // (conexión, realizar consultas con y sin parámetros, y recibir los resultados)
    private static Connection conn = null;
    private static Statement query = null;
    private static ResultSet result = null;
    private static PreparedStatement statement = null;
    
    //Defino los recursos que voy a utilizar//
       //Iconos 
        private final Icon botonInicioFlechaIcon = new ImageIcon(getClass().getResource("/botonInicioFlecha.jpg"));
        private final Icon botonInicioIcon = new ImageIcon(getClass().getResource("/botonInicio.jpg"));
        private final Icon botonMozosFlechaIcon = new ImageIcon(getClass().getResource("/botonMozosFlecha.jpg"));
        private final Icon botonMozosIcon = new ImageIcon(getClass().getResource("/botonMozos.jpg"));
        private final Icon botonMesasFlechaIcon = new ImageIcon(getClass().getResource("/botonMesasFlecha.jpg"));
        private final Icon botonMesasIcon = new ImageIcon(getClass().getResource("/botonMesas.jpg"));
        private final Icon botonConsumosFlechaIcon = new ImageIcon(getClass().getResource("/botonConsumosFlecha.jpg"));
        private final Icon botonConsumosIcon = new ImageIcon(getClass().getResource("/botonConsumos.jpg"));
        private final Icon botonPlatosFlechaIcon = new ImageIcon(getClass().getResource("/botonPlatosFlecha.jpg"));
        private final Icon botonPlatosIcon = new ImageIcon(getClass().getResource("/botonPlatos.jpg"));
        
        //Listas
        private LinkedList<JPanel> listaPaneles; //En esta estructura voy a guardar
        //todos los paneles que voy a utilizar para, con una función, mantener
        //visible solo el panel a mostrar
                
        private LinkedList<Integer> listaPlatos; //A la hora de ingresar o modificar un consumo
        //uso esta lista auxiliar para guardar el código de todos los platos de un consumo
        
        private LinkedList<Integer> listaPlatosFullCodigo; //Codigo de todos los platos que existen
        
        private LinkedList<String> listaCaracteres; //En esta lista guardo todos los caracteres permitidos en los text field
        
        private int codigoAux; //Uso esta variable auxiliar para el momento de modificar
        
        //Fuentes
        private final String FONT_NAME = "CaviarDreams.ttf";
        private static Font font = null;
        private static Font fontAux = null;
        
        //Tablas
        private final Color RESALTADO_TABLA = new Color(5,88,102);
        
    public MainFrame() throws SQLException, FontFormatException, IOException {
        initComponents();
        
        //Registro la fuente a utilizar
        try {
             font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(FONT_NAME));
             GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
             ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
             e.printStackTrace();
             System.out.println("No se ha cargado la fuente tipográfica");
        }
        
        //Instancio las listas
        listaPaneles = new LinkedList<>();
        listaPlatos = new LinkedList<>();
        listaPlatosFullCodigo = new LinkedList<>();
        listaCaracteres = new LinkedList<>();
        
        //Inicializo los jspinners
            //Cambio el editor para que no se permitan divisores de miles
        NumberEditor editor = new JSpinner.NumberEditor(jSpinner1, "0"); 
        jSpinner1.setEditor(editor);
        editor = new JSpinner.NumberEditor(jSpinner2, "0");
        jSpinner2.setEditor(editor);
        editor = new JSpinner.NumberEditor(jSpinner3, "0");
        jSpinner3.setEditor(editor);
        editor = new JSpinner.NumberEditor(jSpinner4, "0");
        jSpinner4.setEditor(editor);
        editor = new JSpinner.NumberEditor(jSpinner5, "0");
        jSpinner5.setEditor(editor);
        editor = new JSpinner.NumberEditor(jSpinner6, "0");
        jSpinner6.setEditor(editor);
        
        //Agrego todos los paneles
        listaPaneles.add(panelInicio);
        listaPaneles.add(panelTablaMozos); 
        listaPaneles.add(panelTablaMesas);
        listaPaneles.add(panelTablaPlatos); 
        listaPaneles.add(panelTablaConsumos); 
        listaPaneles.add(panelInsertarMozo);
        listaPaneles.add(panelInsertarMesa);
        listaPaneles.add(panelInsertarPlato);
        listaPaneles.add(panelInsertarConsumo1);
        listaPaneles.add(panelEliminarMozo);
        listaPaneles.add(panelEliminarMesa);
        listaPaneles.add(panelEliminarPlato);
        listaPaneles.add(panelEliminarConsumo);
        listaPaneles.add(panelMozos); //Sección opciones sobre mozos
        listaPaneles.add(panelMesas); //Sección opciones sobre mesas
        listaPaneles.add(panelPlatos); //Sección opciones sobre platos
        listaPaneles.add(panelConsumos); //Sección opciones sobre consumos
        listaPaneles.add(panelMesasAsignadas); //
        listaPaneles.add(panelMozosLibres);
        listaPaneles.add(panelAsignarMozo); //Asignar mozo a una mesa
        listaPaneles.add(panelMesasPorMozo); //A partir de un mozo ver sus mesas asignadas
        listaPaneles.add(panelPlatosPorMesa); //A partir de una mesa ver los platos
        listaPaneles.add(panelPlatosPorFecha); //A partir de dos fechas ver los platos consumudos
        listaPaneles.add(panelPlatosMasConsumidos);
        listaPaneles.add(panelCostosPlatos); //Max,min,prom del precio costo platos principales
        listaPaneles.add(panelPlatosNuncaConsumidos);
        listaPaneles.add(panelCantTotalPlatosPorMesa);
        listaPaneles.add(panelInsertarConsumo2);
        listaPaneles.add(panelModificarMozo1);
        listaPaneles.add(panelModificarMozo2);
        listaPaneles.add(panelModificarMesa1);
        listaPaneles.add(panelModificarMesa2);
        listaPaneles.add(panelModificarPlato1);
        listaPaneles.add(panelModificarPlato2);
        listaPaneles.add(panelModificarConsumo1);
        listaPaneles.add(panelModificarConsumo2);
        listaPaneles.add(panelModificarConsumo3);
        listaPaneles.add(panelEliminarMozoMesa);

        //Agrego todos los caracteres a la lista de caracteres 
            for(int i = 65; i <= 90; i++) //Agrego las letras mayúsculas
            {
                char c = (char)i;
                listaCaracteres.add(String.valueOf(c));
            }
            for(int i = 97; i <= 122; i++) //Agrego las letras minúsculas
            {
                char c = (char)i;
                listaCaracteres.add(String.valueOf(c));
            }
            listaCaracteres.add("á");
            listaCaracteres.add("é");
            listaCaracteres.add("í");
            listaCaracteres.add("ó");
            listaCaracteres.add("ú");
            listaCaracteres.add("Á");
            listaCaracteres.add("É");
            listaCaracteres.add("Í");
            listaCaracteres.add("Ó");
            listaCaracteres.add("Ú"); 
            listaCaracteres.add("ñ");
            listaCaracteres.add("Ñ");
            listaCaracteres.add(" "); //Espacio
            
        //Inicializo tablas
         //Bloqueo las celdas
            tablaMozos.setDefaultEditor(Object.class, null);
            tablaMozos1.setDefaultEditor(Object.class, null);
            tablaMozos2.setDefaultEditor(Object.class, null);
            tablaMesas3.setDefaultEditor(Object.class, null);
            tablaMesas.setDefaultEditor(Object.class, null);
            tablaConsumos.setDefaultEditor(Object.class, null);
            tablaPlatos.setDefaultEditor(Object.class, null);
            tablaPlatosEntreFechas.setDefaultEditor(Object.class, null);
            tablaPlatosNunca.setDefaultEditor(Object.class, null);
            tablaPlatosPorMesa.setDefaultEditor(Object.class, null);
            tablaMozosLibres.setDefaultEditor(Object.class, null);
            tablaPlatos1.setDefaultEditor(Object.class, null);
            tablaCantMesasPorMozos.setDefaultEditor(Object.class, null);
            tablaEliminarConsumo.setDefaultEditor(Object.class, null);
            tablaEliminarMesa.setDefaultEditor(Object.class, null);
            tablaEliminarPlato.setDefaultEditor(Object.class, null);
            tablaEliminarMozo.setDefaultEditor(Object.class, null);
            tablaMesasPorMozos.setDefaultEditor(Object.class, null);
            tablaPlatosConsumoInsertar.setDefaultEditor(Object.class, null);
            tablaPlatosConsumoModificar.setDefaultEditor(Object.class, null);
            tablaPlatosConsumoModificar2.setDefaultEditor(Object.class, null);
            tablaModificarConsumo.setDefaultEditor(Object.class, null);
            tablaModificarMesa.setDefaultEditor(Object.class, null);
            tablaModificarMozo.setDefaultEditor(Object.class, null);
            tablaModificarPlato1.setDefaultEditor(Object.class, null);
            tablaMozoMesaModificar.setDefaultEditor(Object.class, null);
            tablaMesas4.setDefaultEditor(Object.class, null);
            tablaMesasConsumoModificar.setDefaultEditor(Object.class, null);
            tablaMesasPorMozos.setDefaultEditor(Object.class, null);
            tablaCantPlatos.setDefaultEditor(Object.class, null);
            tablaEliminarMozoMesa.setDefaultEditor(Object.class, null);
            
            //Centro las columnas
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment( JLabel.CENTER );
            
            tablaMozos.setDefaultRenderer(Object.class, centerRenderer);
            tablaMozos1.setDefaultRenderer(Object.class, centerRenderer);
            tablaMozos2.setDefaultRenderer(Object.class, centerRenderer);
            tablaMesas3.setDefaultRenderer(Object.class, centerRenderer);
            tablaMesas.setDefaultRenderer(Object.class, centerRenderer);
            tablaConsumos.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatos.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatosEntreFechas.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatosNunca.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatosPorMesa.setDefaultRenderer(Object.class, centerRenderer);
            tablaMozosLibres.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatos1.setDefaultRenderer(Object.class, centerRenderer);
            tablaCantMesasPorMozos.setDefaultRenderer(Object.class, centerRenderer);
            tablaEliminarConsumo.setDefaultRenderer(Object.class, centerRenderer);
            tablaEliminarMesa.setDefaultRenderer(Object.class, centerRenderer);
            tablaEliminarPlato.setDefaultRenderer(Object.class, centerRenderer);
            tablaEliminarMozo.setDefaultRenderer(Object.class, centerRenderer);
            tablaMesasPorMozos.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatosConsumoInsertar.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatosConsumoModificar.setDefaultRenderer(Object.class, centerRenderer);
            tablaPlatosConsumoModificar2.setDefaultRenderer(Object.class, centerRenderer);
            tablaModificarConsumo.setDefaultRenderer(Object.class, centerRenderer);
            tablaModificarMesa.setDefaultRenderer(Object.class, centerRenderer);
            tablaModificarMozo.setDefaultRenderer(Object.class, centerRenderer);
            tablaModificarPlato1.setDefaultRenderer(Object.class, centerRenderer);
            tablaMozoMesaModificar.setDefaultRenderer(Object.class, centerRenderer);
            tablaMesas4.setDefaultRenderer(Object.class, centerRenderer);
            tablaMesasConsumoModificar.setDefaultRenderer(Object.class, centerRenderer);
            tablaMesasPorMozos.setDefaultRenderer(Object.class, centerRenderer);
            tablaCantPlatos.setDefaultRenderer(Object.class, centerRenderer);
            tablaEliminarMozoMesa.setDefaultRenderer(Object.class, centerRenderer);
            
            //Bloqueo las columnas
            tablaMozos.getTableHeader().setReorderingAllowed(false);
            tablaMozos1.getTableHeader().setReorderingAllowed(false);
            tablaMozos2.getTableHeader().setReorderingAllowed(false);
            tablaMesas3.getTableHeader().setReorderingAllowed(false);
            tablaMesas.getTableHeader().setReorderingAllowed(false);
            tablaConsumos.getTableHeader().setReorderingAllowed(false);
            tablaPlatos.getTableHeader().setReorderingAllowed(false);
            tablaPlatosEntreFechas.getTableHeader().setReorderingAllowed(false);
            tablaPlatosNunca.getTableHeader().setReorderingAllowed(false);
            tablaPlatosPorMesa.getTableHeader().setReorderingAllowed(false);
            tablaMozosLibres.getTableHeader().setReorderingAllowed(false);
            tablaPlatos1.getTableHeader().setReorderingAllowed(false);
            tablaCantMesasPorMozos.getTableHeader().setReorderingAllowed(false);
            tablaEliminarConsumo.getTableHeader().setReorderingAllowed(false);
            tablaEliminarMesa.getTableHeader().setReorderingAllowed(false);
            tablaEliminarPlato.getTableHeader().setReorderingAllowed(false);
            tablaEliminarMozo.getTableHeader().setReorderingAllowed(false);
            tablaMesasPorMozos.getTableHeader().setReorderingAllowed(false);
            tablaPlatosConsumoInsertar.getTableHeader().setReorderingAllowed(false);
            tablaPlatosConsumoModificar.getTableHeader().setReorderingAllowed(false);
            tablaPlatosConsumoModificar2.getTableHeader().setReorderingAllowed(false);
            tablaModificarConsumo.getTableHeader().setReorderingAllowed(false);
            tablaModificarMesa.getTableHeader().setReorderingAllowed(false);
            tablaModificarMozo.getTableHeader().setReorderingAllowed(false);
            tablaModificarPlato1.getTableHeader().setReorderingAllowed(false);
            tablaMozoMesaModificar.getTableHeader().setReorderingAllowed(false);
            tablaMesas4.getTableHeader().setReorderingAllowed(false);
            tablaMesasConsumoModificar.getTableHeader().setReorderingAllowed(false);
            tablaMesasPorMozos.getTableHeader().setReorderingAllowed(false);
            tablaCantPlatos.getTableHeader().setReorderingAllowed(false);
            tablaEliminarMozoMesa.getTableHeader().setReorderingAllowed(false);
            
            //Cambio la fuente de los headers
            fontAux = font.deriveFont(1, 14);
            tablaMozos.getTableHeader().setFont(fontAux);
            tablaMozos1.getTableHeader().setFont(fontAux);
            tablaMozos2.getTableHeader().setFont(fontAux);
            tablaMesas3.getTableHeader().setFont(fontAux);
            tablaMesas.getTableHeader().setFont(fontAux);
            tablaConsumos.getTableHeader().setFont(fontAux);
            tablaPlatos.getTableHeader().setFont(fontAux);
            tablaPlatosEntreFechas.getTableHeader().setFont(fontAux);
            tablaPlatosNunca.getTableHeader().setFont(fontAux);
            tablaMozosLibres.getTableHeader().setFont(fontAux);
            tablaCantMesasPorMozos.getTableHeader().setFont(fontAux);
            tablaEliminarConsumo.getTableHeader().setFont(fontAux);
            tablaEliminarMesa.getTableHeader().setFont(fontAux);
            tablaEliminarPlato.getTableHeader().setFont(fontAux);
            tablaEliminarMozo.getTableHeader().setFont(fontAux);
            tablaMesasPorMozos.getTableHeader().setFont(fontAux);
            tablaModificarConsumo.getTableHeader().setFont(fontAux);
            tablaModificarMozo.getTableHeader().setFont(fontAux);
            tablaMozoMesaModificar.getTableHeader().setFont(fontAux);
            tablaMesas4.getTableHeader().setFont(fontAux);
            tablaMesasConsumoModificar.getTableHeader().setFont(fontAux);
            tablaMesasPorMozos.getTableHeader().setFont(fontAux);
            tablaEliminarMozoMesa.getTableHeader().setFont(fontAux);
            
            fontAux = font.deriveFont(1, 10);
            tablaPlatosConsumoInsertar.getTableHeader().setFont(fontAux);
            tablaPlatosConsumoModificar.getTableHeader().setFont(fontAux);
            tablaPlatosConsumoModificar2.getTableHeader().setFont(fontAux);
            tablaPlatos1.getTableHeader().setFont(fontAux);
            tablaModificarPlato1.getTableHeader().setFont(fontAux);
            
            fontAux = font.deriveFont(1, 11);
            tablaCantPlatos.getTableHeader().setFont(fontAux);
            
            //Centro el texto de los headers
           ((DefaultTableCellRenderer)tablaMozos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMozos1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMozos2.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMesas3.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMesas.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaConsumos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatosEntreFechas.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatosNunca.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatosPorMesa.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMozosLibres.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatos1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaCantMesasPorMozos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaEliminarConsumo.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaEliminarMesa.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaEliminarPlato.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaEliminarMozo.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMesasPorMozos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatosConsumoInsertar.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatosConsumoModificar.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaPlatosConsumoModificar2.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaModificarConsumo.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaModificarMesa.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaModificarMozo.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaModificarPlato1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMozoMesaModificar.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMesas4.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMesasConsumoModificar.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaMesasPorMozos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaCantPlatos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           ((DefaultTableCellRenderer)tablaEliminarMozoMesa.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           
           //Desactivo el agrandamiento de celdas 
            tablaMozos.getTableHeader().setResizingAllowed(false);
            tablaMozos1.getTableHeader().setResizingAllowed(false);
            tablaMozos2.getTableHeader().setResizingAllowed(false);
            tablaMesas3.getTableHeader().setResizingAllowed(false);
            tablaMesas.getTableHeader().setResizingAllowed(false);
            tablaConsumos.getTableHeader().setResizingAllowed(false);
            tablaPlatos.getTableHeader().setResizingAllowed(false);
            tablaPlatosEntreFechas.getTableHeader().setResizingAllowed(false);
            tablaPlatosNunca.getTableHeader().setResizingAllowed(false);
            tablaPlatosPorMesa.getTableHeader().setResizingAllowed(false);
            tablaMozosLibres.getTableHeader().setResizingAllowed(false);
            tablaPlatos1.getTableHeader().setResizingAllowed(false);
            tablaCantMesasPorMozos.getTableHeader().setResizingAllowed(false);
            tablaEliminarConsumo.getTableHeader().setResizingAllowed(false);
            tablaEliminarMesa.getTableHeader().setResizingAllowed(false);
            tablaEliminarPlato.getTableHeader().setResizingAllowed(false);
            tablaEliminarMozo.getTableHeader().setResizingAllowed(false);
            tablaMesasPorMozos.getTableHeader().setReorderingAllowed(false);
            tablaPlatosConsumoInsertar.getTableHeader().setResizingAllowed(false);
            tablaPlatosConsumoModificar.getTableHeader().setResizingAllowed(false);
            tablaPlatosConsumoModificar2.getTableHeader().setResizingAllowed(false);
            tablaModificarConsumo.getTableHeader().setResizingAllowed(false);
            tablaModificarMesa.getTableHeader().setResizingAllowed(false);
            tablaModificarMozo.getTableHeader().setResizingAllowed(false);
            tablaModificarPlato1.getTableHeader().setResizingAllowed(false);
            tablaMozoMesaModificar.getTableHeader().setResizingAllowed(false);
            tablaMesas4.getTableHeader().setResizingAllowed(false);
            tablaMesasConsumoModificar.getTableHeader().setResizingAllowed(false);
            tablaMesasPorMozos.getTableHeader().setResizingAllowed(false);
            tablaCantPlatos.getTableHeader().setResizingAllowed(false);
            tablaEliminarMozoMesa.getTableHeader().setResizingAllowed(false);
                    
           //Cambio el color de la selección de filas
           tablaMozos.setSelectionBackground(RESALTADO_TABLA);
           tablaMozos1.setSelectionBackground(RESALTADO_TABLA);
           tablaMozos2.setSelectionBackground(RESALTADO_TABLA);
           tablaMesas3.setSelectionBackground(RESALTADO_TABLA);
           tablaMesas.setSelectionBackground(RESALTADO_TABLA);
           tablaConsumos.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatos.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatosEntreFechas.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatosNunca.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatosPorMesa.setSelectionBackground(RESALTADO_TABLA);
           tablaMozosLibres.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatos1.setSelectionBackground(RESALTADO_TABLA);
           tablaCantMesasPorMozos.setSelectionBackground(RESALTADO_TABLA);
           tablaEliminarConsumo.setSelectionBackground(RESALTADO_TABLA);
           tablaEliminarMesa.setSelectionBackground(RESALTADO_TABLA);
           tablaEliminarPlato.setSelectionBackground(RESALTADO_TABLA);
           tablaEliminarMozo.setSelectionBackground(RESALTADO_TABLA);
           tablaMesasPorMozos.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatosConsumoInsertar.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatosConsumoModificar.setSelectionBackground(RESALTADO_TABLA);
           tablaPlatosConsumoModificar2.setSelectionBackground(RESALTADO_TABLA);
           tablaModificarConsumo.setSelectionBackground(RESALTADO_TABLA);
           tablaModificarMesa.setSelectionBackground(RESALTADO_TABLA);
           tablaModificarMozo.setSelectionBackground(RESALTADO_TABLA);
           tablaModificarPlato1.setSelectionBackground(RESALTADO_TABLA);
           tablaMozoMesaModificar.setSelectionBackground(RESALTADO_TABLA);
           tablaMesas4.setSelectionBackground(RESALTADO_TABLA);
           tablaMesasConsumoModificar.setSelectionBackground(RESALTADO_TABLA);
           tablaMesasPorMozos.setSelectionBackground(RESALTADO_TABLA);
           tablaCantPlatos.setSelectionBackground(RESALTADO_TABLA);
           tablaEliminarMozoMesa.setSelectionBackground(RESALTADO_TABLA);
           
        //Inicializo el panel lateral
            panelInicio.setVisible(true);
            botonInicio.setIcon(botonInicioFlechaIcon);
            botonMozos.setIcon(botonMozosIcon);
            botonMesas.setIcon(botonMesasIcon);
            botonConsumos.setIcon(botonConsumosIcon);
            botonPlatos.setIcon(botonPlatosIcon);

        //----------------------------------
            
        // Enlazamos el DBMS para conectarnos a la base de datos solicitada 
        // utilizando las credenciales correspondientes
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
        // una vez conectados, nuestro programa creará las tabas que sean necesarias
        // para funcionar, en el caso de que ya no estén creadas
        // (de ahí el "IF NOT EXISTS" luego del "CREATE TABLE").
        query = conn.createStatement();
        query.execute("CREATE TABLE IF NOT EXISTS Mozos("
                + "Mo_Cod SERIAL, "
                + "Mo_NombreApellido TEXT NOT NULL, "
                + "Mo_Domicilio TEXT NOT NULL, "
                + "Mo_DNI INTEGER NOT NULL UNIQUE, "
                + "PRIMARY KEY (Mo_Cod))");
        query.execute("CREATE TABLE IF NOT EXISTS Platos("
                + "P_Cod SERIAL, "
                + "P_Nombre TEXT NOT NULL, "
                + "P_Descripcion TEXT NOT NULL, "
                + "P_Tipo TEXT NOT NULL, "
                + "P_PrecioCosto INTEGER NOT NULL, "
                + "P_PrecioVenta INTEGER NOT NULL, "
                + "P_PrecioPromocion INTEGER NOT NULL, "
                + "PRIMARY KEY (P_Cod))");
        query.execute("CREATE TABLE IF NOT EXISTS Mesas("
                + "Me_Cod SERIAL, "
                + "Me_Sector TEXT NOT NULL, "
                + "Mo_Cod_Atiende INTEGER, " //Permito que sea null como una forma de decir que en esta mesa no hay un mozo asignado                
                + "PRIMARY KEY (Me_Cod), "
                + "FOREIGN KEY (Mo_Cod_Atiende) REFERENCES Mozos(Mo_Cod))");
        query.execute("CREATE TABLE IF NOT EXISTS Consumos("
                + "C_Cod SERIAL, "
                + "C_Fecha DATE NOT NULL, " 
                + "C_Hora TIME NOT NULL, "
                + "Me_Cod_Realiza INTEGER NOT NULL,"
                + "PRIMARY KEY (C_Cod), "
                + "FOREIGN KEY (Me_Cod_Realiza) REFERENCES Mesas(Me_Cod))");
        query.execute("CREATE TABLE IF NOT EXISTS Se_Consume("
                + "C_Cod INTEGER NOT NULL, "
                + "P_Cod INTEGER NOT NULL, "
                + "PRIMARY KEY (C_Cod,P_Cod),"
                + "FOREIGN KEY (C_Cod) REFERENCES Consumos(C_Cod), "
                + "FOREIGN KEY (P_Cod) REFERENCES Platos(P_Cod))");

        //Inicializo el panel principal
        visibilidadPaneles(panelInicio);
    }
    
    public static void inicio() throws IOException, SQLException, URISyntaxException
    {
        //Cargo el archivo si no ha sido cargado
        String s;
        BufferedReader br = null;
        FileReader fr;
        File f = new File("temp.txt"); 
        
        //Para controlar si esta operación ya la hice, creo un archivo. Si el archivo no existe
        //es que aún no se ha hecho la primera ejecución
        
        if(!f.exists()) 
        {
            f.createNewFile();
            
            try {
                fr = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource("inserts2021.sql").toURI())); //Cargo el archivo
                br = new BufferedReader(fr); //Instancio el lector
            } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"No se pudo cargar el archivo");
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            while ((s=br.readLine())!=null) //Mientras hata líneas por leer ejecuto las queries
            {
                if(!s.equals("") && !s.substring(0,1).equals("-")) //Mientras el primer valor de la línea no se a un guión (comentario) o una línea vacía, ejecuto el query
                {
                    try{
                        query.executeUpdate(s);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                }
            }

            //Cargo las sentencias si no han sido cargadas
            query.executeUpdate("INSERT INTO Mozos VALUES(DEFAULT,'Krusty','Aristóbulo del Valle 3432',25544555)");
            query.executeUpdate("UPDATE Platos SET P_PrecioVenta = 690 WHERE P_Nombre = 'Semillas Senzu'");
            query.executeUpdate("DELETE FROM Mozos WHERE Mo_NombreApellido = 'Jesse Pinkman'");
        }
    }
    
    public class MyCellRenderer extends JTextArea implements TableCellRenderer {
        //Esta función es para ser usada con las tablas de platos. El campo descripción
        //normalmente contiene un texto largo y la celda de la tabla no lo muestra 
        //completamente. Entonces, modifico la configuración de la tabla para mostrar
        //todo el campo.
        public MyCellRenderer() {
          setLineWrap(true);
          setWrapStyleWord(true);
          setBackground(new Color(255,255,255));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(String.valueOf(value));
            setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) 
            {
                table.setRowHeight(row, getPreferredSize().height);
            }
            return this;
        }
    } 
    
    public void updateRowHeight()
    {
        //Mediante el método anterior actualizo la altura de la celdas de acuerdo al
        //texto que contengan
        tablaPlatos.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        tablaPlatos.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        tablaPlatosPorMesa.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        tablaPlatos1.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        tablaModificarPlato1.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        tablaPlatosConsumoInsertar.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        tablaPlatosConsumoModificar.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        tablaPlatosConsumoModificar2.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
    }
    
    @Override
    public Image getIconImage() {
        //Este método es para cambiar el favicon del programa
        Image retValue = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png"));
        return retValue; 
    }
   
    private boolean caracterInvalido(String c)
    {
        boolean aux = true;
        for(String o: listaCaracteres)
        {
            if(o.equals(c)) aux = false; //Si el caracter ingresado coincide con alguno de los permitidos, se devuelve false
        }
        return aux;
    }

    private void visibilidadPaneles(JPanel panel)
    {
        for(int i = 0; i < listaPaneles.size(); i++)
        {
            if(listaPaneles.get(i) != panel) listaPaneles.get(i).setVisible(false);
            else listaPaneles.get(i).setVisible(true);
        }
    }       
            
    //LIMPIEZA DE FORMULARIOS hago una función de limpieza por panel para no limpiar paneles que ya están limpios

    private void limpiezaInsertarMozo()
    {
	textNombreMozoIngreso.setText("");//Vaciar
	DNIMozoIngreso.setValue(1000000); //Vaciar campo
	textDomicilioMozoIngreso.setText(""); //Vaciar campo
    }
    private void limpiezaInsertarMesa()
    {
	textSectorMesaIngreso.setText("");
	comboMozoMesa.setSelectedIndex(0);  //Elegir mozo que atiende
    }
    private void limpiezaInsertarPlato()
    {
	textCodIngNombrePlato.setText(""); //vaciar
	textCodIngMesaD.setText(""); //vaciar
	jComboBox2.setSelectedIndex(0); //Poner opcion 0
	jSpinner1.setValue(0); //Precio costo, poner en 0
	jSpinner2.setValue(0); // Precio venta, poner en 0
	jSpinner3.setValue(0); //Precio promocion, poner en 0
    }
    private void limpiezaPlatosEntreFechas()
    {        
        DefaultTableModel tabla = (DefaultTableModel)tablaPlatosEntreFechas.getModel();
        tabla.setRowCount(0);
        jDateChooser3.setDate(new Date());
        jDateChooser2.setDate(new Date());
    }
    private void limpiezaEliminarConsumo()
    {
        jComboBox6.setSelectedIndex(0); //Poner opcion 0
    }
    private void limpiezaEliminarMozo()
    {
        jComboBox3.setSelectedIndex(0); //Poner opcion 0
    }
    private void limpiezaEliminarMesa()
    {
        jComboBox4.setSelectedIndex(0); //Poner opcion 0
    }
    private void limpiezaEliminarPlato()
    {
        jComboBox5.setSelectedIndex(0); //Poner opcion 0
    }
    private void limpiezaModificarConsumo1()
    {
        jComboBox11.setSelectedIndex(0); //Poner opción 0
    }
    private void limpiezaModificarConsumo2()
    {
        spinnerHoraConsumosModificar.setValue(0);
        spinnerMinutosConsumosModificar.setValue(0);
        jDateChooser4.setDate(new Date());
        jComboBox13.setSelectedIndex(0); //Poner opción 0
        
    }
    private void limpiezaModificarConsumo3()
    {
        jComboBox1.setSelectedIndex(0);
    }
    private void limpiezaModificarMozo1()
    {
        jComboBox9.setSelectedIndex(0);
    }
    private void limpiezaModificarMozo2()
    {
        textCodMozo.setEditable(false);
        textNombreMozoModificar.setText("");
        DNIMozoModificar.setValue(10000000);
        textDomicilioMozoModificar.setText("");
    }
    private void limpiezaModificarMesa1()
    {
        jComboBox10.setSelectedIndex(0);
    }
    private void limpiezaModificarMesa2()
    {
        textSectorMesaModificar.setText("");
    }
    private void limpiezaModificarPlato1()
    {
        jComboBox14.setSelectedIndex(0);
    }
    private void limpiezaModificarPlato2()
    {
        textNombrePlato1.setText("");
        textDescMesaMod.setText("");
        jComboBox12.setSelectedIndex(0);
        jSpinner6.setValue(0);
        jSpinner5.setValue(0);
        jSpinner4.setValue(0);
    }
    private void limpiezaCantPlatos()
    {
        jComboBox15.setSelectedIndex(0); 
        labelCantPlatos.setText("");
    }
    private void limpiezaAsignarMozo()
    {
        comboMozo1.setSelectedIndex(0);
	comboMesa1.setSelectedIndex(0); 
    }
    private void limpiezaPlatosPorMesas()
    {
	comboPlatosPorMesa.setSelectedIndex(0); 
        DefaultTableModel tabla = (DefaultTableModel)tablaPlatosPorMesa.getModel();
        tabla.setRowCount(0);
    }
    private void limpiezaCanTotalPlatosPorMesa()
    {
        jComboBox15.setSelectedIndex(0);
        labelCantPlatos.setText("");
    }
    private void limpiezaMesasPorMozo()
    {	
        comboMesasPorMozo.setSelectedIndex(0);
        DefaultTableModel tabla = (DefaultTableModel)tablaMesasPorMozos.getModel();
        tabla.setRowCount(0);
    }
    private void limpiezaIngresarConsumo2()
    {
        jComboBox8.setSelectedIndex(0);
    }
    private void limpiezaIngresarConsumo1() throws SQLException
    {
        spinnerHoraConsumos.setValue(0);
        spinnerMinutosConsumos.setValue(0);
        jComboBox7.setSelectedIndex(0); //Selector de mesa
        jDateChooser1.setDate(new Date());
        DefaultTableModel tabla = (DefaultTableModel)tablaPlatos1.getModel();
        tabla.setRowCount(0);
        tabla = (DefaultTableModel)tablaPlatosConsumoInsertar.getModel();
        tabla.setRowCount(0);
    }
    private void limpiezaEliminarMozoDeMesa()
    {
        DefaultTableModel tabla = (DefaultTableModel)tablaEliminarMozoMesa.getModel();
        tabla.setRowCount(0);
        jComboBox17.setSelectedIndex(0);
        jComboBox16.setSelectedIndex(0);
    }
    private void limpiezaPlatosNunca()
    {
        DefaultTableModel tabla = (DefaultTableModel)tablaPlatosNunca.getModel();
        tabla.setRowCount(0);    
    }
    //Funciones de actualización. Decido hacer una método por consulta para
    //no hacer consultas extras cuando no las requiero
    
    private void updateTablaMozos() throws SQLException
    {
        statement = conn.prepareStatement("SELECT * FROM Mozos"); //Tomo todo el detalle de mozos
        result = statement.executeQuery();
        TableModel tabla = resultToTable(result,6);
        tablaMozos.setModel(tabla);
        tablaMozos1.setModel(tabla);
        tablaMozos2.setModel(tabla);
        tablaMozoMesaModificar.setModel(tabla);
        tablaEliminarMozo.setModel(tabla);
        tablaModificarMozo.setModel(tabla);
        
        statement = conn.prepareStatement("SELECT Mo_Cod FROM Mozos"); //Tomo los códigos de mozos
        result = statement.executeQuery();
        LinkedList<String> lista = resultToList(result, 1);
        comboMozo1.removeAllItems();
        jComboBox3.removeAllItems();
        comboMesasPorMozo.removeAllItems();
        comboMozoMesaMod1.removeAllItems();
        jComboBox9.removeAllItems();
        comboMozoMesa.removeAllItems();
        jComboBox16.removeAllItems();
        for(int i = 0; i < lista.size();i++)
        {
            comboMozo1.addItem(lista.get(i)); //Asignar mozo a una mesa
            jComboBox3.addItem(lista.get(i));  //Eliminar mozo
            comboMozoMesaMod1.addItem(lista.get(i)); //Modificar mesa
            jComboBox9.addItem(lista.get(i)); //Modificar mozo
            comboMozoMesa.addItem(lista.get(i)); 
            jComboBox16.addItem(lista.get(i)); 
        }
        statement = conn.prepareStatement("SELECT Mo_NombreApellido FROM Mozos"); //Tomo los nombres de mozos
        result = statement.executeQuery();
        lista = resultToList(result, 1);
        for(int i = 0; i < lista.size();i++)
        {
            comboMesasPorMozo.addItem(lista.get(i));  //Mesas por mozo            
        }
        
    }
    private void updatePlatosPorConsumo2() throws SQLException
    {
        String string = "(";
        if(!listaPlatos.isEmpty())
        {
            for(int i = 0; i < listaPlatos.size();i++)
            {
                string = string.concat(String.valueOf(listaPlatos.get(i)));
                if((i+1)!=listaPlatos.size())
                {
                    string = string.concat(",");
                }
            }
        }
        else
        {
            string = string.concat("-1");
        }
        string = string.concat(")");
        query = conn.createStatement();
        result = query.executeQuery("SELECT * FROM Platos WHERE P_Cod IN "+ string); //No uso preparedStatement porque es una consulta particular que no corresponde a ninguno de los tipos a ingresar pero es controlada porque yo genero la cadena
        //Tomo todo de platos tal que el código coincida con alguno del string ingresado
        TableModel tabla = resultToTable(result,4);
        tablaPlatosConsumoInsertar.setModel(tabla);
        tablaPlatos1.setModel(tabla);            
    }
    private void updateTablaMesas() throws SQLException
    {
        statement = conn.prepareStatement("SELECT * FROM Mesas"); //Tomo todo el detalle de mesas
        result = statement.executeQuery();
        TableModel tabla = resultToTable(result,7);
        tablaMesas.setModel(tabla);
        tablaEliminarMesa.setModel(tabla);
        tablaMesas4.setModel(tabla);
        tablaMesasConsumoModificar.setModel(tabla);
        tablaModificarMesa.setModel(tabla);
        tablaCantPlatos.setModel(tabla);
        tablaMesas3.setModel(tabla);
        tablaEliminarMozoMesa.setModel(tabla);
                
        statement = conn.prepareStatement("SELECT Me_Cod FROM Mesas"); //Tomo los códigos de mesas
        result = statement.executeQuery();
        LinkedList<String> lista = resultToList(result, 2);
        
        jComboBox7.removeAllItems(); //Consumos
        comboPlatosPorMesa.removeAllItems(); //Platos por mesa
        comboMesa1.removeAllItems(); //Asignar mozo a una mesa
        jComboBox4.removeAllItems(); //Eliminar mesa
        jComboBox13.removeAllItems(); //Modificar consumo
        jComboBox10.removeAllItems(); //Modificar mesa
        jComboBox15.removeAllItems();
        
        for(int i = 0; i < lista.size();i++)
        {
            jComboBox7.addItem(lista.get(i)); //Consumos
            comboPlatosPorMesa.addItem(lista.get(i)); //Platos por mesa
            comboMesa1.addItem(lista.get(i)); //Asignar mozo a una mesa
            jComboBox4.addItem(lista.get(i)); //Eliminar mesa
            jComboBox13.addItem(lista.get(i)); //Modificar consumo
            jComboBox10.addItem(lista.get(i)); //Modificar mesa
            jComboBox15.addItem(lista.get(i));
        }
    }
    private void updateTablaPlatos() throws SQLException
    {
        statement = conn.prepareStatement("SELECT * FROM Platos"); //Tomo todo el detalle dep latos
        result = statement.executeQuery();
        TableModel tabla = resultToTable(result,4);
        tablaPlatos.setModel(tabla);
        tablaEliminarPlato.setModel(tabla);
        tablaModificarPlato1.setModel(tabla);
        
        statement = conn.prepareStatement("SELECT P_Cod FROM Platos"); //Tomo los códigos de platos
        result = statement.executeQuery();
        LinkedList<String> lista = resultToList(result, 3);
        
        statement = conn.prepareStatement("SELECT P_Cod FROM Platos"); //Tomo los códigos de platos. Repito la consulta porque ocurre un error sino
        result = statement.executeQuery();
        listaPlatosFullCodigo = resultToListInt(result);
        
        jComboBox8.removeAllItems(); //Insertar consumo
        jComboBox5.removeAllItems();
        jComboBox1.removeAllItems(); //Modificar consumo
        jComboBox14.removeAllItems(); //Modificar plato
        
        for(int i = 0; i < lista.size();i++)
        {
            jComboBox5.addItem(lista.get(i));
            jComboBox14.addItem(lista.get(i)); //Modificar plato
        }
        
        statement = conn.prepareStatement("SELECT P_Nombre FROM Platos"); //Tomo el nombre de todos los platos
        result = statement.executeQuery();
        lista = resultToList(result, 3);        
        for(int i=0;i<lista.size();i++)
        {
            jComboBox8.addItem(lista.get(i)); //Insertar consumo
            jComboBox1.addItem(lista.get(i)); //Modificar consumo
        }
    }
    private void updateTablaConsumos() throws SQLException
    {
        statement = conn.prepareStatement("SELECT * FROM Consumos"); //Tomo todo el detalle de consumos
        result = statement.executeQuery();
        TableModel tabla = resultToTable(result,8);
        tablaConsumos.setModel(tabla);
        tablaEliminarConsumo.setModel(tabla);
        tablaModificarConsumo.setModel(tabla);
        
        //Listas de combo box
        statement = conn.prepareStatement("SELECT C_Cod FROM Consumos"); //Tomo el código de consumos
        result = statement.executeQuery();
        LinkedList<String> lista = resultToList(result, 4);
        jComboBox6.removeAllItems();
        jComboBox11.removeAllItems();  
        for(int i = 0; i < lista.size();i++)
        {
            jComboBox6.addItem(lista.get(i));
            jComboBox11.addItem(lista.get(i));
        }
    }
    private boolean updatePlatosEntreFechas(String date1, String date2) throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(C_Cod) FROM Consumos WHERE C_Fecha BETWEEN ? AND ?"); //Cuento cuantos platos se consumieron en esa fecha para mostrar un cartel cuando no se haya consumido ninguno
        statement.setDate(1,java.sql.Date.valueOf(date1));
        statement.setDate(2,java.sql.Date.valueOf(date2));
        result = statement.executeQuery();
        result.next();
        
        if(!String.valueOf(result.getObject(1)).equals("0"))
        {   
            statement = conn.prepareStatement("SELECT P_Cod, P_Nombre FROM Platos WHERE P_Cod IN (SELECT P_Cod FROM Se_Consume WHERE C_Cod IN (SELECT C_Cod FROM Consumos WHERE C_Fecha BETWEEN ? AND ?))");

                //La consulta sería: tomo todos los C_Cod en Consumos donde la fecha esté entre las fechas ingresadas
                //Luego tomo los P_Cod de Se_Consume tal que C_Cod sea igual a alguno de los C_Cod obtenidos anteriormente
                //Tomo el código y el nombre en Platos tal que el código sea igual a alguno de los P_Cod obtenidos anteriormente
            statement.setDate(1,java.sql.Date.valueOf(date1));
            statement.setDate(2,java.sql.Date.valueOf(date2));
            result = statement.executeQuery();
            tablaPlatosEntreFechas.setModel(resultToTable(result,9));      
            return true;
        }
        else return false;
    }
    private void updatePlatosNuncaConsumidos() throws SQLException
    {
        statement = conn.prepareStatement("SELECT P_Nombre, P_Descripcion FROM Platos WHERE P_Cod IN ((SELECT P_Cod FROM Platos) EXCEPT (SELECT P_Cod FROM Se_Consume))");
        //La consulta sería: tomo todos los P_Cod de platos y tomo todos los P_Cod de Se_Consume y realizo la diferencia entre ellos para obtener
        //el código de los platos que no han sido consumidos
        //Luego tomo el nombre y la descripción en Platos tal que el código coincida con alguno de los códigos obtenidos previamente
        result = statement.executeQuery();
        tablaPlatosNunca.setModel(resultToTable(result,3));
    }
    private void updateMozosLibres() throws SQLException
    {
       statement = conn.prepareStatement("SELECT Mo_Cod, Mo_NombreApellido FROM Mozos WHERE Mo_Cod IN ((SELECT Mo_Cod FROM Mozos) EXCEPT (SELECT Mo_Cod_Atiende FROM Mesas))");
        //La consulta sería: tomo todos los Mo_Cod de Mozos y tomo todos los Mo_Cod de Mesas y realizo la diferencia entre ellos para obtener
        //el código de los mozos que no tienen mesas asignadas
        //Luego tomo el código y el nombre en Mozos tal que el código coincida con alguno de los códigos obtenidos previamente 
        result = statement.executeQuery();
        tablaMozosLibres.setModel(resultToTable(result,1));
    }
    private void updateCantMesasPorMozos() throws SQLException
    {
        statement = conn.prepareStatement("SELECT Mo_NombreApellido, COUNT(Me_Cod) FROM Mozos, Mesas WHERE Mo_Cod = Mo_Cod_Atiende GROUP BY Mo_Cod ORDER BY Mo_NombreApellido");
        //Genero el ensambre Mozos y Mesas con condición de ensambre Mo_Cod = Mo_Cod_Atiende
        //Agrupo la tabla por Mo_Cod y la ordeno alfabéticamente
        //Tomo el nombre de los mozos y la cantidad de mesas que tienen asignadas 
        result = statement.executeQuery();
        tablaCantMesasPorMozos.setModel(resultToTable(result,2));
    }
    private void updateMaxMinProm() throws SQLException
    {
        statement = conn.prepareStatement("SELECT MAX(P_PrecioCosto) FROM Platos WHERE P_Tipo = 'Plato Principal'");
        //Tomo el máximo del precio de costo de Platos tal que el tipo sea igual a Plato Principal
        result = statement.executeQuery();
        result.next();
        labelMaximo.setText(String.valueOf(result.getObject(1)));
        statement = conn.prepareStatement("SELECT MIN(P_PrecioCosto) FROM Platos WHERE P_Tipo = 'Plato Principal'");
        //Tomo el mínimo del precio de costo de Platos tal que el tipo sea igual a Plato Principal
        result = statement.executeQuery();
        result.next();
        labelMinimo.setText(String.valueOf(result.getObject(1)));
        statement = conn.prepareStatement("SELECT AVG(P_PrecioCosto) FROM Platos WHERE P_Tipo = 'Plato Principal'");
        //Tomo el promedio del precio de costo de Platos tal que el tipo sea igual a Plato Principal
        result = statement.executeQuery();
        result.next();
        labelPromedio.setText(String.valueOf(result.getObject(1)));
    }
    
    private void updatePrincipal()throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(Mo_Cod) FROM Mozos");
        //Cuento la cantidad de mozos que existen en el sistema contando los códigos en la tabla Mozos
        result = statement.executeQuery();
        result.next();
        labelCantMozos.setText(String.valueOf(result.getObject(1)));
        statement = conn.prepareStatement("SELECT COUNT(Me_Cod) FROM Mesas");
        //Cuento la cantidad de mesas que existen en el sistema contando los códigos en la tabla Mesas
        result = statement.executeQuery();
        result.next();
        labelCantMesas.setText(String.valueOf(result.getObject(1)));
        statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Platos WHERE P_Tipo = 'Plato Principal'");
        //Cuento la cantidad de platos principales en el sistema contando el código de platos
        //tal que el tipo es igual a Plato Principal
        result = statement.executeQuery();
        result.next();
        labelCantPlatosPr.setText(String.valueOf(result.getObject(1)));
        statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Platos WHERE P_Tipo = 'Postre'");
        //Cuento la cantidad de postres en el sistema contando el código de platos
        //tal que el tipo es igual a Postre        
        result = statement.executeQuery();
        result.next();
        labelCantPlatosP.setText(String.valueOf(result.getObject(1)));
        statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Platos WHERE P_Tipo = 'Entrada'");
        //Cuento la cantidad de entradas en el sistema contando el código de platos
        //tal que el tipo es igual a Entrada 
        result = statement.executeQuery();
        result.next();
        labelCantEntradas.setText(String.valueOf(result.getObject(1)));
    }
    private void updateCantTotalPlatosConsumidos(int cod) throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Se_Consume WHERE C_Cod IN (SELECT C_Cod FROM Consumos WHERE Me_Cod_Realiza = ?)");
        //La consulta sería: selecciono todos los C_Cod de Consumos donde Me_Cod_Realiza = cod
        //Luego selecciono los P_Cod y los cuento de Se_Consume donde C_Cod sea igual a los C_Cod obtenidos anteriormente
        statement.setInt(1,cod);
        result = statement.executeQuery();
        result.next();
        labelCantPlatos.setText(String.valueOf(result.getObject(1)));
    }
    private void updateMesaPlatos(int cod) throws SQLException
    {
        statement = conn.prepareStatement("SELECT P_Cod,P_Nombre,P_Descripcion FROM Platos WHERE P_Cod IN (SELECT P_Cod FROM Se_Consume WHERE C_Cod IN (SELECT C_Cod FROM Consumos WHERE Me_Cod_Realiza = ?))");
        //La consulta sería: Selecciono los C_Cod de Consumos donde Me_Cod_Realiza = cod
        //Luego selecciono los P_Cod de Se_Consumo donde C_Cod se corresponde con los de la selección anterior
        //Selecciono P_Cod, P_Nombre y P_Descripcion de Platos donde P_Cod se corresponda con los P_Cod de lo obtenido previamente
        statement.setInt(1,cod);
        result = statement.executeQuery();
        tablaPlatosPorMesa.setModel(resultToTable(result,5));
    }
    private void updateTiposConsumidos() throws SQLException
    {   
        statement = conn.prepareStatement("SELECT P_Nombre\n" +
                                    "FROM( \n" +
                                        "SELECT P_Nombre, COUNT(Platos.P_Cod) contador\n" +
                                        "FROM Platos,Se_Consume\n" +
                                        "WHERE Platos.P_Cod = Se_Consume.P_Cod AND P_Tipo = 'Entrada'\n" +
                                        "GROUP BY Platos.P_Cod) AS T1\n" +
                                    "WHERE contador in \n" +
                                         "(SELECT MAX(contador)\n" +
                                         "FROM \n" +
                                             "(SELECT Platos.P_Cod, COUNT(Platos.P_Cod) contador\n" +
                                             "FROM Platos, Se_Consume\n" +
                                             "WHERE Platos.P_Cod = Se_Consume.P_Cod AND P_Tipo = 'Entrada'\n" +
                                             "GROUP BY Platos.P_Cod) AS T2)");
        /*A grandes rasgos sería:
            SELECT el nombre de los platos
            FROM la siguiente tabla:
                SELECT el nombre de platos y cantidad de platos
                FROM el ensamble Platos,Se_Consume
                WHERE condición de ensamble: Platos.P_Cod = Se_Consume.P_Cod y el tipo de plato es Entrada
                GROUP BY por el código de los platos 
            WHERE el valor obtenido del contador esté en la siguiente tabla:
                SELECT el contador con magnitud más grande
                FROM la siguiente tabla:
                    SELECT el nombre de platos y cantidad de platos
                    FROM el ensamble Platos,Se_Consume
                    WHERE condición de ensamble: Platos.P_Cod = Se_Consume.P_Cod y el tipo de plato es Entrada
                    GROUP BY por el código de los platos 
        */
	result = statement.executeQuery();
        if(!result.next()) //Si no se ha consumido ningun plato de entrada
        {
            labelEntrada.setText("Aún no consumidos");
	}
	else
	{
            labelEntrada.setText(String.valueOf(result.getObject(1)));
	}

        statement = conn.prepareStatement("SELECT P_Nombre\n" +
                                    "FROM( \n" +
                                        "SELECT P_Nombre, COUNT(Platos.P_Cod) contador\n" +
                                        "FROM Platos,Se_Consume\n" +
                                        "WHERE Platos.P_Cod = Se_Consume.P_Cod AND P_Tipo = 'Plato Principal'\n" +
                                        "GROUP BY Platos.P_Cod) AS T1\n" +
                                    "WHERE contador in \n" +
                                         "(SELECT MAX(contador)\n" +
                                         "FROM \n" +
                                             "(SELECT Platos.P_Cod, COUNT(Platos.P_Cod) contador\n" +
                                             "FROM Platos, Se_Consume\n" +
                                             "WHERE Platos.P_Cod = Se_Consume.P_Cod AND P_Tipo = 'Plato Principal'\n" +
                                             "GROUP BY Platos.P_Cod) AS T2)");

	//Idem que antes solo que con principales
        result = statement.executeQuery();
        if(!result.next()) //Si no se ha consumido ningun plato principal
	{
            labelPrincipal.setText("Aún no consumidos");
        }
	else
	{
            labelPrincipal.setText(String.valueOf(result.getObject(1)));
	}
        
       statement = conn.prepareStatement("SELECT P_Nombre\n" +
                                    "FROM( \n" +
                                        "SELECT P_Nombre, COUNT(Platos.P_Cod) contador\n" +
                                        "FROM Platos,Se_Consume\n" +
                                        "WHERE Platos.P_Cod = Se_Consume.P_Cod AND P_Tipo = 'Postre'\n" +
                                        "GROUP BY Platos.P_Cod) AS T1\n" +
                                    "WHERE contador in \n" +
                                         "(SELECT MAX(contador)\n" +
                                         "FROM \n" +
                                             "(SELECT Platos.P_Cod, COUNT(Platos.P_Cod) contador\n" +
                                             "FROM Platos, Se_Consume\n" +
                                             "WHERE Platos.P_Cod = Se_Consume.P_Cod AND P_Tipo = 'Postre'\n" +
                                             "GROUP BY Platos.P_Cod) AS T2)");

        //Idem que antes solo que con postres
        result = statement.executeQuery();
	if(!result.next()) //Si no se ha consumido ningun plato de postre
	{
            labelPostre.setText("Aún no consumidos");
	}
	else
	{
            labelPostre.setText(String.valueOf(result.getObject(1)));
	}
    }
    private boolean updateMesasPorUnMozo(int cod) throws SQLException
    {
        LinkedList<String> lista = null;
        statement = conn.prepareStatement("SELECT COUNT(Me_Cod) FROM Mesas,Mozos WHERE Mo_Cod_Atiende = Mo_Cod AND Mo_Cod= ?");
        //La consulta sería tomo la cantidad de mesas de Mesas del ensamble Mesas, Mozos tal que Mo_Cod sea igual a cod y la condición de ensamble Mo_Cod_Atiende = Mo_Cod
        statement.setInt(1,cod);
        result = statement.executeQuery();
        result.next();
        
        if(String.valueOf(result.getObject(1)).equals("0"))
        {
            return false;
        }
        else
        {
            statement = conn.prepareStatement("SELECT Me_Cod,Me_Sector,Mo_Cod_Atiende FROM Mesas,Mozos WHERE Mo_Cod_Atiende = Mo_Cod AND Mo_Cod = ?");
            //La consulta sería: tomo el código, el sector y el código del mozo que atiende la mesa del ensable Mesas,Mozos tal que
            //Mo_Cod sea igual a cod y la condición de ensamble Mo_Cod_Atiende = Mo_Cod
            statement.setInt(1,cod);
            result = statement.executeQuery();
            
            TableModel tabla = resultToTable(result,7);
            tablaMesasPorMozos.setModel(tabla);     
            tablaEliminarMozoMesa.setModel(tabla);
            jComboBox17.removeAllItems();
            
            statement = conn.prepareStatement("SELECT Me_Cod FROM Mesas,Mozos WHERE Mo_Cod_Atiende = Mo_Cod AND Mo_Cod= ?");
            //La consulta sería: tomo el código de mesas del ensambre Mesas,Mozos tal que Mo_Cod = cod y la condicón de ensamble Mo_Cod_Atiende = Mo_Cod
            statement.setInt(1,cod);
            result = statement.executeQuery();
            lista = resultToList(result,2);
            for(int i=0; i<lista.size();i++)
            {
                jComboBox17.addItem(lista.get(i));
            }
            return true;
        }
    }
   
    private void updatePlatosPorConsumo(int cod) throws SQLException
    {
        statement = conn.prepareStatement("SELECT Platos.P_Cod,P_Nombre,P_Descripcion,P_Tipo,P_PrecioCosto,P_PrecioVenta,P_PrecioPromocion FROM Se_Consume,Platos WHERE Platos.P_Cod = Se_Consume.P_Cod AND C_Cod= ?"); 
        //La consulta sería: tomo todo el detalle de platos del ensambre Se_Consume, Platos tal que C_Cod = cod y la condición de ensamble Platos.P_Cod = Se_Consume.P_Cod
        statement.setInt(1,cod);
        result = statement.executeQuery();
        TableModel tabla = resultToTable(result,4);
        tablaPlatosConsumoModificar.setModel(tabla);
        tablaPlatosConsumoModificar2.setModel(tabla);
    }
    private void updatePlatosPorConsumo3() throws SQLException
    {
        String string = "(";
        if(!listaPlatos.isEmpty())
        {
            for(int i = 0; i < listaPlatos.size();i++)
            {
                string = string.concat(String.valueOf(listaPlatos.get(i)));
                if((i+1)!=listaPlatos.size())
                {
                    string = string.concat(",");
                }
            }
        }
        else
        {
            string = string.concat("-1");
        }
        string = string.concat(")");
        query = conn.createStatement();
        result = query.executeQuery("SELECT * FROM Platos WHERE P_Cod IN"+string); //No uso preparedStatement porque es una consulta particular que no corresponde a ninguno de los tipos a ingresar pero es controlada porque yo genero la cadena
        //La consulta sería: tomo todo el detalle de Platos tal que P_Cod se encuentra en el string 
        TableModel tabla = resultToTable(result,4);
        tablaPlatosConsumoModificar.setModel(tabla);
        tablaPlatosConsumoModificar2.setModel(tabla);          
    }    
    private static LinkedList<String> resultToList(ResultSet rs, int nroLista) throws SQLException{
        // Esta es una función auxiliar que permite convertir los resultados de las
        // consultas (ResultSet) de una columna a una lista para luego mostrar en la interfaz en forma
        // de combo box
        LinkedList<String> lista = new LinkedList<String>();
        
        if(nroLista == 1 ) //Lista de mozos
        {
            lista.add("Elija un mozo");
        }
        else if(nroLista == 2) //Lista de mesas
        {
            lista.add("Elija una mesa");
        }
        else if(nroLista == 3) //Lista de Platos
        {
            lista.add("Elija un plato");
        }
        else if(nroLista == 4) //Lista de consumos
        {
            lista.add("Elija un consumo");
        }
        while (rs.next()) {
            lista.add(String.valueOf(rs.getObject(1)));
        }
        
        return  lista;
    }
    private static LinkedList<Integer> resultToListInt(ResultSet rs) throws SQLException
    {
        LinkedList<Integer> lista = new LinkedList<>();
        while (rs.next()) {
            lista.add(Integer.parseInt(String.valueOf(rs.getObject(1)).trim()));
        }
        return lista;
    }
    private static DefaultTableModel resultToTable(ResultSet rs, int nroTabla) throws SQLException {
        // Esta es una función auxiliar que permite convertir los resultados de las
        // consultas (ResultSet) a un modelo interpretable para la tabla mostrada en pantalla
        // (es decir, para un objeto de tipo JTable)
        ResultSetMetaData metaData = rs.getMetaData();

        // creando las columnas de la tabla
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        
        //Si la tabla es mozos_libres
        if(nroTabla == 1)
        {
            columnNames.add("Código");
            columnNames.add("Nombre");
        }
        //Si la tabla es mesas_asignadas
        else if(nroTabla == 2)
        {
            columnNames.add("Nombre");
            columnNames.add("Cantidad de mesas");
        }
        //Si la tabla es plato_nunca_consumidos
        else if(nroTabla == 3)
        {
            columnNames.add("Nombre");
            columnNames.add("Descripcion");
        }
       //Si la tabla es Platos completa
        else if(nroTabla == 4)
        {
           columnNames.add("Código");
           columnNames.add("Nombre");
           columnNames.add("Descripción");
           columnNames.add("Tipo");
           columnNames.add("Costo");
           columnNames.add("Venta");
           columnNames.add("Promoción");
        }
        //Si la tabla es platos_por_mesa
        else if(nroTabla == 5)
        {
            columnNames.add("Código");
            columnNames.add("Nombre");
            columnNames.add("Descripción");
        }
        //Si la tabla es Mozos completa
        else if(nroTabla == 6)
        {
            columnNames.add("Código");
            columnNames.add("Nombre");
            columnNames.add("Domicilio");
            columnNames.add("DNI");
        }
        //Si la tabla es Mesas completa
        else if(nroTabla == 7)
        {
            columnNames.add("Código");
            columnNames.add("Sector");
            columnNames.add("Atiende mozo nro");
        }
        //Si la tabla es Consumos
        else if(nroTabla == 8)
        {
            columnNames.add("Código");
            columnNames.add("Fecha");
            columnNames.add("Hora");
            columnNames.add("Código mesa");
        }
        //Si la tabla es platos por fecha
        else if(nroTabla == 9)
        {
            columnNames.add("Código");
            columnNames.add("Nombre");
        }
        
        // creando las filas de la tabla con los resultados de la consulta
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                if(rs.getObject(columnIndex) == null)
                {
                    vector.add("Sin atender");
                }
                else
                {
                    vector.add(rs.getObject(columnIndex));
                }
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    
    //Consultas en mozos--------------------------------------------------------
    private int insertarMozo(String nombre, String domicilio, int DNI) throws SQLException
    {
        statement = conn.prepareStatement("INSERT INTO mozos VALUES(DEFAULT,?,?,?) RETURNING Mo_Cod");
        //La consulta sería: inserto en mozos la nupla con el detalle de los parámetros y el código generado por sql. la consulta deuvelve el código generado
       statement.setString(1,nombre);
        statement.setString(2,domicilio);
        statement.setInt(3,DNI);
        result = statement.executeQuery();
        result.next();
        return Integer.parseInt(String.valueOf(result.getObject(1)));
    }
    private void eliminarMozo(int codigo) throws SQLException
    {
        statement = conn.prepareStatement("DELETE FROM Mozos WHERE Mo_cod = ?");
        //La consulta sería: elimino de mozos el mozo que cumple la condición Mo_Cod = codigo
        statement.setInt(1,codigo);
        statement.executeUpdate();
    }
    private boolean buscarMozoDNI(int dni) throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(Mo_Cod) FROM Mozos WHERE Mo_DNI = ?");
        //La consulta sería: cuanto la cantidad de mozos que tienen el dni dni
        statement.setInt(1,dni);
        result = statement.executeQuery();
        result.next();
        return !(String.valueOf(result.getObject(1)).equals("0"));
    }
    private void modificarMozo(int codigo, String nombre, String domicilio, int DNI) throws SQLException
    {
       statement = conn.prepareStatement("UPDATE Mozos SET Mo_NombreApellido = ?,Mo_Domicilio = ? ,Mo_DNI = ? WHERE Mo_Cod = ?");
       //La consulta sería: modifico la nupla con sus respectivos valores que cumpla la condición Mo_Cod = codigo
       statement.setString(1,nombre);
       statement.setString(2,domicilio);
       statement.setInt(3,DNI);
       statement.setInt(4,codigo);
       statement.executeUpdate();
    }
    private boolean hayMozo() throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(Mo_Cod) FROM Mozos");
        //Cuento la cantidad de mozos que hay
        result = statement.executeQuery();
        result.next();
        return !(String.valueOf(result.getObject(1))).equals("0");
    }
    private boolean mozoEnMesa(int codigo) throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(Mo_Cod) FROM Mozos,Mesas WHERE Mo_Cod = Mo_Cod_Atiende AND Mo_Cod = ? ");
        //Cuanto la cantidad de mozos que cumplen atender una mesa y tener el código codigo
        statement.setInt(1, codigo);
        result = statement.executeQuery();
        result.next();
        return !(String.valueOf(result.getObject(1))).equals("0");
    }
    private LinkedList<String> devolverMozo(int cod) throws SQLException //Devuelve toda la data de un mozo para modificar
    {
       statement = conn.prepareStatement("SELECT * FROM Mozos WHERE Mo_Cod = ?");
       //Tomo el detalle de Mozos tal que Mo_Cod = cod
       statement.setInt(1,cod);
       result = statement.executeQuery();
        LinkedList<String> lista = new LinkedList<>();
        result.next();
        if(result.getObject(1) != null)
        {
            lista.add(String.valueOf(result.getObject(1)));
            lista.add(String.valueOf(result.getObject(2)));
            lista.add(String.valueOf(result.getObject(3)));
            lista.add(String.valueOf(result.getObject(4)));
        }
        return lista;
    }
    private LinkedList<String> devolverMozoDNI(int dni) throws SQLException //Devuelve toda la data de un mozo para modificar
    {
        statement = conn.prepareStatement("SELECT * FROM Mozos WHERE Mo_DNI = ?");
        //Tomo el detalle de mozos en Mozos tak que Mo_DNI = dni
        statement.setInt(1,dni);
        result = statement.executeQuery();
        LinkedList<String> lista = new LinkedList<>();
        result.next();
        if(result.getObject(1) != null)
        {
            lista.add(String.valueOf(result.getObject(1)));
            lista.add(String.valueOf(result.getObject(2)));
            lista.add(String.valueOf(result.getObject(3)));
            lista.add(String.valueOf(result.getObject(4)));
        }
        return lista;
    }
    private LinkedList<Integer> devolverListaMozos() throws SQLException
    {
        statement = conn.prepareStatement("SELECT Mo_Cod FROM Mozos");
        //Tomo todos los códigos de mozos
        result = statement.executeQuery();
        LinkedList<Integer> lista = resultToListInt(result);
        return lista;
    }
    //Consultas en mesas--------------------------------------------------------
    private int insertarMesa(String sector, int codigoMozo) throws SQLException
    {
	statement = conn.prepareStatement("INSERT INTO Mesas VALUES (DEFAULT,?,?) RETURNING Me_Cod");
         //La consulta sería: inserto en mesas la nupla con el detalle de los parámetros y el código generado por sql. la consulta deuvelve el código generado
        statement.setString(1,sector);
        statement.setInt(2,codigoMozo);
        result = statement.executeQuery();
        result.next();
        return Integer.parseInt(String.valueOf(result.getObject(1)));
    }
    private void eliminarMesa(int codigo) throws SQLException
    {
        statement = conn.prepareStatement("DELETE FROM Mesas WHERE Me_Cod = ?");
        //Elimino la nupla en mesas que cumple Me_Cod = codigo
        statement.setInt(1,codigo);
        statement.executeUpdate();
    }
    private void modificarMesa(int codigo, String sector, int codigoMozo) throws SQLException
    {
        statement = conn.prepareStatement("UPDATE Mesas SET Me_Sector = ?, Mo_Cod_Atiende = ? WHERE Me_Cod = ?");
        //Modifico la nupla en Mesas con sus respectivos valores tal que cumple Me_Cod = codigo
        statement.setString(1,sector);
        statement.setInt(2, codigoMozo);
        statement.setInt(3,codigo);
        statement.executeUpdate();
    }
    private boolean hayMesa() throws SQLException
    {   
        statement = conn.prepareStatement("SELECT COUNT(Me_Cod) FROM Mesas");
        //Cuento la cantidad de mesas que hay en el sistema
        result = statement.executeQuery();
        result.next();
        return !(String.valueOf(result.getObject(1))).equals("0");
    }
    private LinkedList<String> devolverMesa(int cod) throws SQLException
    {
        statement = conn.prepareStatement("SELECT * FROM Mesas WHERE Me_Cod = ?");
        //Tomo el detalle de mesas tal que Me_Cod = cod
        statement.setInt(1,cod);
        result = statement.executeQuery();
        LinkedList<String> lista = new LinkedList<>();
        result.next();
        if(result.getObject(1) != null)
        {
            lista.add(String.valueOf(result.getObject(1)));
            lista.add(String.valueOf(result.getObject(2)));
            lista.add(String.valueOf(result.getObject(3)));
        }
        return lista;
    }
    private void asignarMozo(int codMesa,int codMozo) throws SQLException
    {
        statement = conn.prepareStatement("UPDATE Mesas SET Mo_Cod_Atiende = ? WHERE Me_Cod = ?");
        //Modifico la nupla en Mesas tal que Me_Cod = codMesa
        statement.setInt(1,codMozo);
        statement.setInt(2,codMesa);
        statement.executeUpdate();
    }
    
    private void liberarMesa(int codMesa,int codMozo,int aux) throws SQLException
    {
        if(aux == 1)
        {
            statement = conn.prepareStatement("UPDATE Mesas SET Mo_Cod_Atiende = null WHERE Me_Cod = ? ");
            //Modifico la nupla en Mesas tal que Me_Cod = codMesa
            statement.setInt(1,codMesa);
        }
        else
        {
             statement = conn.prepareStatement("UPDATE Mesas SET Mo_Cod_Atiende = null WHERE Mo_Cod_Atiende = ?");
             //Modifico la nupla en Mesas tal que Me_Cod = codMesa
             statement.setInt(1,codMozo);
        }
        statement.executeUpdate();
    }
    
    //Consultas en consumos-----------------------------------------------------
    private int insertarConsumo(String fecha, int hora, int minuto, int codigoMesa) throws SQLException
    {
        statement = conn.prepareStatement("INSERT INTO Consumos VALUES(DEFAULT,?,?,?) RETURNING C_Cod");
        statement.setDate(1,java.sql.Date.valueOf(fecha));
        statement.setTime(2,new java.sql.Time(hora,minuto,00));
        statement.setInt(3,codigoMesa);
        result = statement.executeQuery();
        result.next();
        return Integer.parseInt(String.valueOf(result.getObject(1)));
    }
    private void eliminarConsumo(int codigo) throws SQLException
    {
        statement = conn.prepareStatement("DELETE FROM Consumos WHERE C_Cod = ?");
        statement.setInt(1,codigo);
        statement.executeUpdate();
    }
    private void modificarConsumo(int codigo, String fecha, int hora, int minuto, int codigoMesa) throws SQLException
    {
	statement = conn.prepareStatement("UPDATE Consumos SET C_Fecha = ?,C_Hora = ?,Me_Cod_Realiza = ? WHERE C_Cod = ?");
        statement.setDate(1,java.sql.Date.valueOf(fecha));
        statement.setTime(2,new java.sql.Time(hora,minuto,00));
        statement.setInt(3,codigoMesa);
        statement.setInt(4, codigo);
        statement.executeUpdate();
    }
    private boolean hayConsumo() throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(C_Cod) FROM Consumos");
        result = statement.executeQuery();
        result.next();
        return !(String.valueOf(result.getObject(1)).equals("0"));
    }
    private LinkedList<String> devolverConsumo(int cod) throws SQLException
    {
        statement = conn.prepareStatement("SELECT * FROM Consumos WHERE C_Cod = ?");
        statement.setInt(1,cod);
        result = statement.executeQuery();
        LinkedList<String> lista = new LinkedList<>();
        result.next();
        if(result.getObject(1) != null)
        {
            lista.add(String.valueOf(result.getObject(1)));
            lista.add(String.valueOf(result.getObject(2)));
            lista.add(String.valueOf(result.getObject(3)));
            lista.add(String.valueOf(result.getObject(4)));
        }
        return lista;
    }

    //Consultas en platos-------------------------------------------------------
    private void insertarPlato(String nombre, String descripcion, String tipo, int precioCosto, int precioVenta, int precioPromocion) throws SQLException
    {
	statement = conn.prepareStatement("INSERT INTO Platos VALUES(DEFAULT,?,?,?,?,?,?)");
        //La consulta sería: inserto en Platos la nupla con el detalle de los parámetros y el código generado por sql. la consulta deuvelve el código generado
        statement.setString(1,nombre);
        statement.setString(2,descripcion);
        statement.setString(3,tipo);
        statement.setInt(4,precioCosto); //Acorto la precisiòn del float ingresado pero como lo convierte en string vuelvo a convertirlo en float
        statement.setInt(5,precioVenta);
        statement.setInt(6,precioPromocion);
        statement.executeUpdate();    
    }
    private void eliminarPlatos(int codigo) throws SQLException
    {
        statement = conn.prepareStatement("DELETE FROM Platos WHERE P_Cod = ?");
       //Elimino el plato de Platos que cumple P_Cod = codigo
        statement.setInt(1,codigo);
        statement.executeUpdate();
    }
    private void modificarPlato(int codigo, String nombre, String descripcion, String tipo, int precioCosto, int precioVenta, int precioPromocion) throws SQLException
    {
        statement = conn.prepareStatement("UPDATE Platos SET P_Nombre = ?,P_Descripcion = ?,P_Tipo = ?,P_PrecioCosto = ?,P_PrecioVenta = ?,P_PrecioPromocion = ? WHERE P_Cod = ?");
        //Modifico el plato en Platos tal que P_Cod = codigo
        statement.setString(1,nombre);
        statement.setString(2,descripcion);
        statement.setString(3,tipo);
        statement.setInt(4,precioCosto);
        statement.setInt(5,precioVenta);
        statement.setInt(6,precioPromocion);
        statement.setInt(7, codigo);
        statement.executeUpdate();    
    }
    private boolean hayPlato(int filtra) throws SQLException
    {
        if(filtra == 1)
        {
            statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Platos WHERE P_Tipo='Plato Principal'");
            //Cuento la cantidad de platos principales
            result = statement.executeQuery();
            result.next();
            return !(String.valueOf(result.getObject(1)).equals("0"));
        }
        else
        {
            statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Platos");
            //Cuento la cantidad de platos que existen
            result = statement.executeQuery();
            result.next();
            return !(String.valueOf(result.getObject(1)).equals("0"));
        }
    }
    private boolean tienePlato(int codMesa) throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Se_Consume,Consumos WHERE Se_Consume.C_Cod = Consumos.C_Cod AND Me_Cod_Realiza = ?");
        //A partir del código de una mesa cuento cuántos platos han sido consumidos en ella
        statement.setInt(1,codMesa);
        result = statement.executeQuery();
        result.next();
        return !String.valueOf(result.getObject(1)).equals("0");
    }
            
    private LinkedList<String> devolverPlato(int cod) throws SQLException
    {
        statement = conn.prepareStatement("SELECT * FROM Platos WHERE P_Cod= ?");
        //Tomo el detalle de Platos tal que P_Cod = cod
        statement.setInt(1,cod);
        result = statement.executeQuery();
        LinkedList<String> lista = new LinkedList<>();
        result.next();
        lista.add(String.valueOf(result.getObject(1)));
        lista.add(String.valueOf(result.getObject(2)));
        lista.add(String.valueOf(result.getObject(3)));
        lista.add(String.valueOf(result.getObject(4)));
        lista.add(String.valueOf(result.getObject(5)));
        lista.add(String.valueOf(result.getObject(6)));
        lista.add(String.valueOf(result.getObject(7)));
        return lista;
    }
    private LinkedList<Integer> devolverPlatosConsumo(int cod) throws SQLException
    {
        statement = conn.prepareStatement("SELECT Platos.P_Cod FROM Platos,Se_Consume WHERE Platos.P_Cod = Se_Consume.P_Cod AND C_Cod = ?");
        //Tomo todos los platos de un consumo
        statement.setInt(1,cod);
        result = statement.executeQuery();
        LinkedList<Integer> lista = resultToListInt(result);
        return lista;
    }

    //Consultas en Se_Consume
    private void insertarSe_Consume(int codigoConsumo, int codigoPlato) throws SQLException
    {
	statement = conn.prepareStatement("INSERT INTO Se_Consume VALUES(?,?)");
        //La consulta sería: inserto en Se_Consume la nupla con el detalle de los parámetros y el código generado por sql. la consulta deuvelve el código generado
        statement.setInt(1,codigoConsumo);
        statement.setInt(2,codigoPlato);
        statement.executeUpdate();
    }
    private void eliminarSe_ConsumeC(int codigoConsumo) throws SQLException
    {
	statement = conn.prepareStatement("DELETE FROM Se_Consume WHERE C_Cod = ?");
        //Elimino de Se_Consumo las nuplas que cumplen C_Cod = codigoConsumo
        statement.setInt(1,codigoConsumo);
        statement.executeUpdate();
    }
    private void eliminarSe_ConsumeP(int codigoPlato) throws SQLException
    {
	statement = conn.prepareStatement("DELETE FROM Se_Consume WHERE P_Cod = ?");
        //Elimino de Se_Consume las nuplas que cumplen P_Cod = codigoPlato
        statement.setInt(1,codigoPlato);
        statement.executeUpdate();
    }
    //Otras consultas
    private boolean hayMozoLibre() throws SQLException
    {
       statement = conn.prepareStatement("SELECT COUNT(Mo_Cod) FROM Mozos WHERE Mo_Cod IN ((SELECT Mo_Cod FROM Mozos) EXCEPT (SELECT Mo_Cod_Atiende FROM Mesas))"); //Devuelvue cuántos mozos no atienden mesas
       //Tomo el código de todos los mozos y tomo el código de todos los mozos que atienden, hago la diferencia y obtengo el código de todos los mozos que no atiende
       //Cuento la cantidad de mozos que no atienden
       result = statement .executeQuery();
       result.next();
        return !((String.valueOf(result.getObject(1))).equals("0")); 
    }
    private boolean hayPlatosNuncaConsumidos() throws SQLException
    {
        statement = conn.prepareStatement("SELECT COUNT(P_Cod) FROM Platos WHERE P_Cod IN ((SELECT P_Cod FROM Platos) EXCEPT (SELECT P_Cod FROM Se_Consume))");
       //Tomo el código de todos los platos y tomo el código de todos los platos consumos, hago la diferencia y obtengo el código de todos los platos no consumidos
       //Cuento la cantidad de platos no consumidos
        result = statement.executeQuery();
        result.next();
        return !((String.valueOf(result.getObject(1))).equals("0")); 
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelModificarPlato2 = new javax.swing.JPanel();
        botonModificarPlato2 = new javax.swing.JButton();
        botonCancelarModificarPlato1 = new javax.swing.JButton();
        jScrollPane28 = new javax.swing.JScrollPane();
        textNombrePlato1 = new javax.swing.JTextArea();
        jSpinner4 = new javax.swing.JSpinner();
        jSpinner5 = new javax.swing.JSpinner();
        labelCodigoConsumo13 = new javax.swing.JLabel();
        labelCodigoConsumo17 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jComboBox12 = new javax.swing.JComboBox<>();
        labelCodigoConsumo18 = new javax.swing.JLabel();
        jScrollPane29 = new javax.swing.JScrollPane();
        textDescMesaMod = new javax.swing.JTextArea();
        labelCodigoConsumo19 = new javax.swing.JLabel();
        labelIngreseConsumo2 = new javax.swing.JLabel();
        jScrollPane30 = new javax.swing.JScrollPane();
        textCodPlato = new javax.swing.JTextArea();
        labelCodigoConsumo20 = new javax.swing.JLabel();
        labelCodigoConsumo21 = new javax.swing.JLabel();
        labelCodigoConsumo22 = new javax.swing.JLabel();
        panelModificarMesa2 = new javax.swing.JPanel();
        labelIngreseMesa1 = new javax.swing.JLabel();
        labelCodigoMesa1 = new javax.swing.JLabel();
        labelCodigoMesa2 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        textCodMesa = new javax.swing.JTextArea();
        jScrollPane26 = new javax.swing.JScrollPane();
        textSectorMesaModificar = new javax.swing.JTextArea();
        tablaMozosScroll2 = new javax.swing.JScrollPane();
        tablaMozoMesaModificar = new javax.swing.JTable();
        comboMozoMesaMod1 = new javax.swing.JComboBox<>();
        botonModificarMesa2 = new javax.swing.JButton();
        botonCancelarModificarMesa1 = new javax.swing.JButton();
        panelModificarMozo2 = new javax.swing.JPanel();
        jScrollPane21 = new javax.swing.JScrollPane();
        textCodMozo = new javax.swing.JTextArea();
        jScrollPane23 = new javax.swing.JScrollPane();
        textDomicilioMozoModificar = new javax.swing.JTextArea();
        labelDomMozo1 = new javax.swing.JLabel();
        labelIngreseMozo1 = new javax.swing.JLabel();
        botonModificarMozo2 = new javax.swing.JButton();
        botonCancelarMozoModificar2 = new javax.swing.JButton();
        jScrollPane19 = new javax.swing.JScrollPane();
        textNombreMozoModificar = new javax.swing.JTextArea();
        labelCodigoMozo1 = new javax.swing.JLabel();
        labelNombreMozo1 = new javax.swing.JLabel();
        labelDNIIng1 = new javax.swing.JLabel();
        DNIMozoModificar = new javax.swing.JSpinner();
        panelInsertarMozo = new javax.swing.JPanel();
        labelIngreseMozo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textNombreMozoIngreso = new javax.swing.JTextArea();
        labelNombreMozo = new javax.swing.JLabel();
        labelDNIIng = new javax.swing.JLabel();
        labelDomMozo = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        textDomicilioMozoIngreso = new javax.swing.JTextArea();
        botonCancelarMozosIngreso = new javax.swing.JButton();
        botonIngresarMozos = new javax.swing.JButton();
        DNIMozoIngreso = new javax.swing.JSpinner();
        panelInsertarPlato = new javax.swing.JPanel();
        labelCodigoConsumo6 = new javax.swing.JLabel();
        labelCodigoConsumo3 = new javax.swing.JLabel();
        labelIngreseConsumo1 = new javax.swing.JLabel();
        labelCodigoConsumo4 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        textCodIngMesaD = new javax.swing.JTextArea();
        labelCodigoConsumo5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jSpinner1 = new javax.swing.JSpinner();
        labelCodigoConsumo7 = new javax.swing.JLabel();
        labelCodigoConsumo8 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jSpinner3 = new javax.swing.JSpinner();
        jScrollPane11 = new javax.swing.JScrollPane();
        textCodIngNombrePlato = new javax.swing.JTextArea();
        botonCancelarInsertarPlato = new javax.swing.JButton();
        botonInsertarPlato1 = new javax.swing.JButton();
        panelInsertarMesa = new javax.swing.JPanel();
        labelMozoAsignadoMesa = new javax.swing.JLabel();
        labelIngreseMesa = new javax.swing.JLabel();
        labelSectorMesa = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        textSectorMesaIngreso = new javax.swing.JTextArea();
        tablaMozosScroll1 = new javax.swing.JScrollPane();
        tablaMozos1 = new javax.swing.JTable();
        botonCancelarIngresoMesa = new javax.swing.JButton();
        botonMesaIngresar = new javax.swing.JButton();
        comboMozoMesa = new javax.swing.JComboBox<>();
        labelSectorMesa1 = new javax.swing.JLabel();
        panelInsertarConsumo1 = new javax.swing.JPanel();
        labelIngreseConsumo = new javax.swing.JLabel();
        labelCodigoMesaConsumo = new javax.swing.JLabel();
        tablaMesasScroll1 = new javax.swing.JScrollPane();
        tablaPlatos1 = new javax.swing.JTable();
        labelCodigoMesaConsumo1 = new javax.swing.JLabel();
        spinnerHoraConsumos = new javax.swing.JSpinner();
        spinnerMinutosConsumos = new javax.swing.JSpinner();
        botonCancelarInsertarConsumo = new javax.swing.JButton();
        botonInsertarConsumo1 = new javax.swing.JButton();
        labelCodigoMesaConsumo2 = new javax.swing.JLabel();
        tablaMesasScroll2 = new javax.swing.JScrollPane();
        tablaMesas4 = new javax.swing.JTable();
        jComboBox7 = new javax.swing.JComboBox<>();
        botonPlatoConsumoModificar1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        labelCodigoMesaConsumo11 = new javax.swing.JLabel();
        panelInsertarConsumo2 = new javax.swing.JPanel();
        labelCodigoMesaConsumo9 = new javax.swing.JLabel();
        tablaMesasScroll7 = new javax.swing.JScrollPane();
        tablaPlatosConsumoInsertar = new javax.swing.JTable();
        labelCodigoMesaConsumo10 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        botonEliminarPlatoConsumoIng1 = new javax.swing.JButton();
        botonInsertarPlatoConsumoIng1 = new javax.swing.JButton();
        botonVolverPlatosConsumoIngreso = new javax.swing.JButton();
        panelModificarConsumo3 = new javax.swing.JPanel();
        labelCodigoMesaConsumo7 = new javax.swing.JLabel();
        tablaMesasScroll6 = new javax.swing.JScrollPane();
        tablaPlatosConsumoModificar2 = new javax.swing.JTable();
        botonVolverPlatosConsumo = new javax.swing.JButton();
        labelCodigoMesaConsumo8 = new javax.swing.JLabel();
        botonEliminarPlatoConsumoMod = new javax.swing.JButton();
        botonInsertarPlatoConsumoMod = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        panelEliminarPlato = new javax.swing.JPanel();
        labelCodigoConsumo11 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tablaEliminarPlato = new javax.swing.JTable();
        jComboBox5 = new javax.swing.JComboBox<>();
        botonEliminarPlato1 = new javax.swing.JButton();
        botonCancelarEliminarPlato = new javax.swing.JButton();
        panelCantTotalPlatosPorMesa = new javax.swing.JPanel();
        labelCantPlatos = new javax.swing.JLabel();
        jComboBox15 = new javax.swing.JComboBox<>();
        botonVolverCantPlatos = new javax.swing.JButton();
        botonVerCantPlatos1 = new javax.swing.JButton();
        jScrollPane33 = new javax.swing.JScrollPane();
        tablaCantPlatos = new javax.swing.JTable();
        fondo = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        panelModificarPlato1 = new javax.swing.JPanel();
        jScrollPane32 = new javax.swing.JScrollPane();
        tablaModificarPlato1 = new javax.swing.JTable();
        labelCodigoConsumo23 = new javax.swing.JLabel();
        jComboBox14 = new javax.swing.JComboBox<>();
        botonCancelarModificarPlato = new javax.swing.JButton();
        botonModificarPlato1 = new javax.swing.JButton();
        panelPlatosPorFecha = new javax.swing.JPanel();
        labelMozosTitulo5 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tablaPlatosEntreFechas = new javax.swing.JTable();
        botonCancelarPlatosEntreFechas = new javax.swing.JButton();
        botonBuscarPlatosEntreFechas = new javax.swing.JButton();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        panelModificarConsumo2 = new javax.swing.JPanel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        labelIngreseConsumo3 = new javax.swing.JLabel();
        labelCodigoMesaConsumo3 = new javax.swing.JLabel();
        spinnerMinutosConsumosModificar = new javax.swing.JSpinner();
        spinnerHoraConsumosModificar = new javax.swing.JSpinner();
        labelCodigoMesaConsumo4 = new javax.swing.JLabel();
        jComboBox13 = new javax.swing.JComboBox<>();
        labelCodigoMesaConsumo5 = new javax.swing.JLabel();
        botonCancelarModificarConsumo2 = new javax.swing.JButton();
        botonModificarConsumo2 = new javax.swing.JButton();
        tablaMesasScroll3 = new javax.swing.JScrollPane();
        tablaPlatosConsumoModificar = new javax.swing.JTable();
        tablaMesasScroll4 = new javax.swing.JScrollPane();
        tablaMesasConsumoModificar = new javax.swing.JTable();
        botonPlatoConsumoModificar = new javax.swing.JButton();
        labelCodigoMesaConsumo6 = new javax.swing.JLabel();
        panelEliminarMozoMesa = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jComboBox17 = new javax.swing.JComboBox<>();
        botonLiberarMozo = new javax.swing.JButton();
        BotonLiberarTodo = new javax.swing.JButton();
        tabla = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaEliminarMozoMesa = new javax.swing.JTable();
        lateral = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        botonBuscarMozoMesa = new javax.swing.JButton();
        jComboBox16 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        botonCancelarEliminarMM = new javax.swing.JButton();
        panelMozos = new javax.swing.JPanel();
        botonVerMozos = new javax.swing.JButton();
        botonModificarMozo = new javax.swing.JButton();
        botonEliminarMozo = new javax.swing.JButton();
        botonInsertarMozo = new javax.swing.JButton();
        botonMozosLibres = new javax.swing.JButton();
        botonAsignarMozo = new javax.swing.JButton();
        botonMesasAsignadas = new javax.swing.JButton();
        botonEliminarMozoMesa = new javax.swing.JButton();
        panelModificarConsumo1 = new javax.swing.JPanel();
        jScrollPane27 = new javax.swing.JScrollPane();
        tablaModificarConsumo = new javax.swing.JTable();
        labelCodigoConsumo16 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        botonModificarConsumo1 = new javax.swing.JButton();
        botonCancelarModificarConsumo1 = new javax.swing.JButton();
        panelModificarMesa1 = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        tablaModificarMesa = new javax.swing.JTable();
        labelCodigoConsumo15 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox<>();
        botonModificarMesa1 = new javax.swing.JButton();
        botonCancelarModificarMozo1 = new javax.swing.JButton();
        panelModificarMozo1 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        tablaModificarMozo = new javax.swing.JTable();
        labelCodigoConsumo14 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        botonModificarMozo1 = new javax.swing.JButton();
        botonCancelarModificarMozo = new javax.swing.JButton();
        panelEliminarMesa = new javax.swing.JPanel();
        labelCodigoConsumo10 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tablaEliminarMesa = new javax.swing.JTable();
        jComboBox4 = new javax.swing.JComboBox<>();
        botonEliminarMesa1 = new javax.swing.JButton();
        botonCancelarEliminarMesa = new javax.swing.JButton();
        panelEliminarMozo = new javax.swing.JPanel();
        labelCodigoConsumo9 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaEliminarMozo = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox<>();
        botonEliminarMozo1 = new javax.swing.JButton();
        botonCancelarEliminarMozo = new javax.swing.JButton();
        panelEliminarConsumo = new javax.swing.JPanel();
        labelCodigoConsumo12 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tablaEliminarConsumo = new javax.swing.JTable();
        jComboBox6 = new javax.swing.JComboBox<>();
        botonEliminarConsumo1 = new javax.swing.JButton();
        botonCancelarEliminarConsumo = new javax.swing.JButton();
        panelMozosLibres = new javax.swing.JPanel();
        labelMozosLibres = new javax.swing.JLabel();
        tablaCantMesasPorMozoss2 = new javax.swing.JScrollPane();
        tablaMozosLibres = new javax.swing.JTable();
        botonVolverMozosLibres = new javax.swing.JButton();
        panelMesasAsignadas = new javax.swing.JPanel();
        tablaCantMesasPorMozoss = new javax.swing.JScrollPane();
        tablaCantMesasPorMozos = new javax.swing.JTable();
        labelMozosTitulo1 = new javax.swing.JLabel();
        botonVolverMesasAsignadas = new javax.swing.JButton();
        panelMesasPorMozo = new javax.swing.JPanel();
        labelMozosTitulo2 = new javax.swing.JLabel();
        tablaCantMesasPorMozoss1 = new javax.swing.JScrollPane();
        tablaMesasPorMozos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        comboMesasPorMozo = new javax.swing.JComboBox<>();
        botonVolverMesasPorMozos = new javax.swing.JButton();
        botonBuscarMesasPorMozos1 = new javax.swing.JButton();
        panelPlatosPorMesa = new javax.swing.JPanel();
        labelMozosTitulo3 = new javax.swing.JLabel();
        tablaCantMesasPorMozoss3 = new javax.swing.JScrollPane();
        tablaPlatosPorMesa = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        comboPlatosPorMesa = new javax.swing.JComboBox<>();
        botonVolverPlatosPorMesa = new javax.swing.JButton();
        buscarPlatosPorMesa = new javax.swing.JButton();
        panelPlatosNuncaConsumidos = new javax.swing.JPanel();
        labelMozosTitulo7 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tablaPlatosNunca = new javax.swing.JTable();
        botonVolverPlatosNunca = new javax.swing.JButton();
        panelTablaMozos = new javax.swing.JPanel();
        tablaMozosScroll = new javax.swing.JScrollPane();
        tablaMozos = new javax.swing.JTable();
        labelMozosTitulo = new javax.swing.JLabel();
        botonVolverMozos = new javax.swing.JButton();
        panelPlatosMasConsumidos = new javax.swing.JPanel();
        labelMozosTitulo6 = new javax.swing.JLabel();
        labelEntrada = new javax.swing.JLabel();
        labelPrincipal = new javax.swing.JLabel();
        labelPostre = new javax.swing.JLabel();
        botonVolverPlatosConsumidos = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        panelTablaConsumos = new javax.swing.JPanel();
        tablaConsumosScroll = new javax.swing.JScrollPane();
        tablaConsumos = new javax.swing.JTable();
        labelConsumosTitulo = new javax.swing.JLabel();
        botonVolverConsumos = new javax.swing.JButton();
        panelCostosPlatos = new javax.swing.JPanel();
        labelMozosTitulo8 = new javax.swing.JLabel();
        labelMaximo = new javax.swing.JLabel();
        labelMinimo = new javax.swing.JLabel();
        labelPromedio = new javax.swing.JLabel();
        botonVolverAnalisis = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        panelAsignarMozo = new javax.swing.JPanel();
        labelMozosTitulo4 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tablaMozos2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        comboMozo1 = new javax.swing.JComboBox<>();
        jScrollPane16 = new javax.swing.JScrollPane();
        tablaMesas3 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        comboMesa1 = new javax.swing.JComboBox<>();
        botonCancelarAsignarMozo = new javax.swing.JButton();
        botonModificarAsignarMozo = new javax.swing.JButton();
        panelConsumos = new javax.swing.JPanel();
        botonInsertarConsumo = new javax.swing.JButton();
        botonEliminarConsumo = new javax.swing.JButton();
        botonModificarConsumo = new javax.swing.JButton();
        botonVerConsumos = new javax.swing.JButton();
        panelMesas = new javax.swing.JPanel();
        botonModificarMesa = new javax.swing.JButton();
        botonVerMesas = new javax.swing.JButton();
        botonEliminarMesa = new javax.swing.JButton();
        botonInsertarMesa = new javax.swing.JButton();
        botonMesaPorMozo = new javax.swing.JButton();
        panelPlatos = new javax.swing.JPanel();
        botonModificarPlato = new javax.swing.JButton();
        botonVerPlatos = new javax.swing.JButton();
        botonEliminarPlato = new javax.swing.JButton();
        botonInsertarPlato = new javax.swing.JButton();
        botonPlatosNuncaCons = new javax.swing.JButton();
        botonPlatoMasConsumido = new javax.swing.JButton();
        botonCantsPlatosPorMesa = new javax.swing.JButton();
        botonPlatosPorFecha = new javax.swing.JButton();
        botonPlatosPorMesa = new javax.swing.JButton();
        botonCostosPlatos = new javax.swing.JButton();
        panelBanner = new javax.swing.JPanel();
        titulo = new javax.swing.JButton();
        botonInicio = new javax.swing.JButton();
        botonMozos = new javax.swing.JButton();
        botonPlatos = new javax.swing.JButton();
        botonMesas = new javax.swing.JButton();
        botonConsumos = new javax.swing.JButton();
        panelTablaPlatos = new javax.swing.JPanel();
        labelPlatosTitulo = new javax.swing.JLabel();
        tablaPlatosScroll = new javax.swing.JScrollPane();
        tablaPlatos = new javax.swing.JTable();
        botonVolverPlatos = new javax.swing.JButton();
        panelTablaMesas = new javax.swing.JPanel();
        tablaMesasScroll = new javax.swing.JScrollPane();
        tablaMesas = new javax.swing.JTable();
        labelMesasTitulo = new javax.swing.JLabel();
        botonVolverMesas = new javax.swing.JButton();
        panelInicio = new javax.swing.JPanel();
        panelInicioValores = new javax.swing.JPanel();
        labelCantMozos = new javax.swing.JLabel();
        labelCantMesas = new javax.swing.JLabel();
        labelCantPlatosPr = new javax.swing.JLabel();
        labelCantPlatosP = new javax.swing.JLabel();
        labelCantEntradas = new javax.swing.JLabel();
        panelInicioFondo = new javax.swing.JPanel();
        labelInicio = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("El Resto del Freaky");
        setBounds(new java.awt.Rectangle(1200, 700, 1200, 700));
        setIconImage(getIconImage());
        setLocation(new java.awt.Point(1200, 700));
        setMaximumSize(new java.awt.Dimension(1200, 700));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setName("FRAME PRINCIPAL"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);
        setSize(new java.awt.Dimension(1200, 700));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        panelModificarPlato2.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarPlato2.setPreferredSize(new java.awt.Dimension(1000, 700));

        botonModificarPlato2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarPlato2.setBorder(null);
        botonModificarPlato2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarPlato2ActionPerformed(evt);
            }
        });

        botonCancelarModificarPlato1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarModificarPlato1.setBorder(null);
        botonCancelarModificarPlato1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarModificarPlato1ActionPerformed(evt);
            }
        });

        jScrollPane28.setBackground(new java.awt.Color(255, 255, 255));

        textNombrePlato1.setBackground(new java.awt.Color(255, 255, 255));
        textNombrePlato1.setColumns(20);
        textNombrePlato1.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textNombrePlato1.setForeground(new java.awt.Color(5, 88, 102));
        textNombrePlato1.setRows(1);
        textNombrePlato1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textNombrePlato1.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textNombrePlato1.setSelectionColor(new java.awt.Color(153, 153, 153));
        textNombrePlato1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textNombrePlato1KeyTyped(evt);
            }
        });
        jScrollPane28.setViewportView(textNombrePlato1);

        jSpinner4.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jSpinner5.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        labelCodigoConsumo13.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo13.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo13.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo13.setText("Precio de promoción");

        labelCodigoConsumo17.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo17.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo17.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo17.setText("Precio de venta");

        jSpinner6.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jComboBox12.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox12.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox12.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija el tipo", "Entrada", "Plato Principal", "Postre" }));
        jComboBox12.setPreferredSize(new java.awt.Dimension(272, 30));

        labelCodigoConsumo18.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo18.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo18.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo18.setText("Tipo");

        jScrollPane29.setBackground(new java.awt.Color(255, 255, 255));

        textDescMesaMod.setBackground(new java.awt.Color(255, 255, 255));
        textDescMesaMod.setColumns(20);
        textDescMesaMod.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textDescMesaMod.setForeground(new java.awt.Color(5, 88, 102));
        textDescMesaMod.setLineWrap(true);
        textDescMesaMod.setRows(5);
        textDescMesaMod.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textDescMesaMod.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textDescMesaMod.setSelectionColor(new java.awt.Color(153, 153, 153));
        textDescMesaMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textDescMesaModKeyTyped(evt);
            }
        });
        jScrollPane29.setViewportView(textDescMesaMod);

        labelCodigoConsumo19.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo19.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo19.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo19.setText("Descripción");

        labelIngreseConsumo2.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseConsumo2.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseConsumo2.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseConsumo2.setText("Ingrese los datos del plato");

        jScrollPane30.setBackground(new java.awt.Color(255, 255, 255));

        textCodPlato.setBackground(new java.awt.Color(255, 255, 255));
        textCodPlato.setColumns(20);
        textCodPlato.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textCodPlato.setForeground(new java.awt.Color(5, 88, 102));
        textCodPlato.setRows(1);
        textCodPlato.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textCodPlato.setEnabled(false);
        textCodPlato.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textCodPlato.setSelectionColor(new java.awt.Color(153, 153, 153));
        jScrollPane30.setViewportView(textCodPlato);

        labelCodigoConsumo20.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo20.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo20.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo20.setText("Nombre");

        labelCodigoConsumo21.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo21.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo21.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo21.setText("Código (automático)");

        labelCodigoConsumo22.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo22.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo22.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo22.setText("Precio de costo");

        javax.swing.GroupLayout panelModificarPlato2Layout = new javax.swing.GroupLayout(panelModificarPlato2);
        panelModificarPlato2.setLayout(panelModificarPlato2Layout);
        panelModificarPlato2Layout.setHorizontalGroup(
            panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                        .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCodigoConsumo21, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCodigoConsumo19, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCodigoConsumo20, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(105, 105, 105))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelModificarPlato2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(botonCancelarModificarPlato1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCodigoConsumo18, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCodigoConsumo22, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCodigoConsumo17, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCodigoConsumo13, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(botonModificarPlato2)))
                .addGap(132, 132, 132))
            .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addComponent(labelIngreseConsumo2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelModificarPlato2Layout.setVerticalGroup(
            panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(labelIngreseConsumo2)
                .addGap(47, 47, 47)
                .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                        .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCodigoConsumo21)
                            .addComponent(labelCodigoConsumo18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCodigoConsumo20)
                    .addComponent(labelCodigoConsumo22))
                .addGap(18, 18, 18)
                .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(labelCodigoConsumo17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelCodigoConsumo13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelModificarPlato2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(labelCodigoConsumo19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(57, 57, 57)
                .addGroup(panelModificarPlato2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonModificarPlato2)
                    .addComponent(botonCancelarModificarPlato1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelModificarPlato2);
        panelModificarPlato2.setBounds(200, 0, 1000, 700);

        panelModificarMesa2.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarMesa2.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelModificarMesa2.setLayout(null);

        labelIngreseMesa1.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseMesa1.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseMesa1.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseMesa1.setText("Ingrese los datos de la mesa");
        panelModificarMesa2.add(labelIngreseMesa1);
        labelIngreseMesa1.setBounds(180, 106, 640, 56);

        labelCodigoMesa1.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesa1.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesa1.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesa1.setText("Sector");
        panelModificarMesa2.add(labelCodigoMesa1);
        labelCodigoMesa1.setBounds(184, 372, 87, 36);

        labelCodigoMesa2.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesa2.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesa2.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesa2.setText("Código (automático)");
        panelModificarMesa2.add(labelCodigoMesa2);
        labelCodigoMesa2.setBounds(184, 225, 294, 36);

        jScrollPane25.setBackground(new java.awt.Color(255, 255, 255));

        textCodMesa.setBackground(new java.awt.Color(255, 255, 255));
        textCodMesa.setColumns(20);
        textCodMesa.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textCodMesa.setForeground(new java.awt.Color(5, 88, 102));
        textCodMesa.setRows(1);
        textCodMesa.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textCodMesa.setEnabled(false);
        textCodMesa.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textCodMesa.setSelectionColor(new java.awt.Color(153, 153, 153));
        jScrollPane25.setViewportView(textCodMesa);

        panelModificarMesa2.add(jScrollPane25);
        jScrollPane25.setBounds(184, 279, 294, 30);

        jScrollPane26.setBackground(new java.awt.Color(255, 255, 255));

        textSectorMesaModificar.setBackground(new java.awt.Color(255, 255, 255));
        textSectorMesaModificar.setColumns(20);
        textSectorMesaModificar.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textSectorMesaModificar.setForeground(new java.awt.Color(5, 88, 102));
        textSectorMesaModificar.setRows(1);
        textSectorMesaModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textSectorMesaModificar.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textSectorMesaModificar.setSelectionColor(new java.awt.Color(153, 153, 153));
        jScrollPane26.setViewportView(textSectorMesaModificar);

        panelModificarMesa2.add(jScrollPane26);
        jScrollPane26.setBounds(184, 426, 294, 30);

        tablaMozoMesaModificar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "DNI", "Nombre", "Domicilio"
            }
        ));
        tablaMozoMesaModificar.setShowGrid(false);
        tablaMozosScroll2.setViewportView(tablaMozoMesaModificar);

        panelModificarMesa2.add(tablaMozosScroll2);
        tablaMozosScroll2.setBounds(526, 227, 294, 184);

        comboMozoMesaMod1.setBackground(new java.awt.Color(255, 255, 255));
        comboMozoMesaMod1.setFont(new java.awt.Font("Caviar Dreams", 1, 14)); // NOI18N
        comboMozoMesaMod1.setForeground(new java.awt.Color(5, 88, 102));
        comboMozoMesaMod1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un mozo" }));
        comboMozoMesaMod1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMozoMesaMod1ActionPerformed(evt);
            }
        });
        panelModificarMesa2.add(comboMozoMesaMod1);
        comboMozoMesaMod1.setBounds(527, 426, 294, 33);

        botonModificarMesa2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarMesa2.setBorder(null);
        botonModificarMesa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarMesa2ActionPerformed(evt);
            }
        });
        panelModificarMesa2.add(botonModificarMesa2);
        botonModificarMesa2.setBounds(620, 530, 200, 50);

        botonCancelarModificarMesa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarModificarMesa1.setBorder(null);
        botonCancelarModificarMesa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarModificarMesa1ActionPerformed(evt);
            }
        });
        panelModificarMesa2.add(botonCancelarModificarMesa1);
        botonCancelarModificarMesa1.setBounds(180, 530, 200, 50);

        getContentPane().add(panelModificarMesa2);
        panelModificarMesa2.setBounds(200, 0, 1000, 700);

        panelModificarMozo2.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarMozo2.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelModificarMozo2.setPreferredSize(new java.awt.Dimension(1000, 700));

        jScrollPane21.setBackground(new java.awt.Color(255, 255, 255));

        textCodMozo.setBackground(new java.awt.Color(255, 255, 255));
        textCodMozo.setColumns(20);
        textCodMozo.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textCodMozo.setForeground(new java.awt.Color(5, 88, 102));
        textCodMozo.setRows(1);
        textCodMozo.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textCodMozo.setEnabled(false);
        textCodMozo.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textCodMozo.setSelectionColor(new java.awt.Color(153, 153, 153));
        jScrollPane21.setViewportView(textCodMozo);

        jScrollPane23.setBackground(new java.awt.Color(255, 255, 255));

        textDomicilioMozoModificar.setBackground(new java.awt.Color(255, 255, 255));
        textDomicilioMozoModificar.setColumns(40);
        textDomicilioMozoModificar.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textDomicilioMozoModificar.setForeground(new java.awt.Color(5, 88, 102));
        textDomicilioMozoModificar.setRows(1);
        textDomicilioMozoModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textDomicilioMozoModificar.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textDomicilioMozoModificar.setSelectionColor(new java.awt.Color(153, 153, 153));
        jScrollPane23.setViewportView(textDomicilioMozoModificar);

        labelDomMozo1.setBackground(new java.awt.Color(255, 255, 255));
        labelDomMozo1.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelDomMozo1.setForeground(new java.awt.Color(5, 88, 102));
        labelDomMozo1.setText("Domicilio");

        labelIngreseMozo1.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseMozo1.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseMozo1.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseMozo1.setText("Ingrese los datos del mozo");

        botonModificarMozo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarMozo2.setBorder(null);
        botonModificarMozo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarMozo2ActionPerformed(evt);
            }
        });

        botonCancelarMozoModificar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarMozoModificar2.setBorder(null);
        botonCancelarMozoModificar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarMozoModificar2ActionPerformed(evt);
            }
        });

        jScrollPane19.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane19KeyPressed(evt);
            }
        });

        textNombreMozoModificar.setBackground(new java.awt.Color(255, 255, 255));
        textNombreMozoModificar.setColumns(40);
        textNombreMozoModificar.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textNombreMozoModificar.setForeground(new java.awt.Color(5, 88, 102));
        textNombreMozoModificar.setRows(1);
        textNombreMozoModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textNombreMozoModificar.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textNombreMozoModificar.setSelectionColor(new java.awt.Color(153, 153, 153));
        textNombreMozoModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textNombreMozoModificarKeyTyped(evt);
            }
        });
        jScrollPane19.setViewportView(textNombreMozoModificar);

        labelCodigoMozo1.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMozo1.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMozo1.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMozo1.setText("Código (automático)");

        labelNombreMozo1.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreMozo1.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelNombreMozo1.setForeground(new java.awt.Color(5, 88, 102));
        labelNombreMozo1.setText("Nombre y apellido");

        labelDNIIng1.setBackground(new java.awt.Color(255, 255, 255));
        labelDNIIng1.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelDNIIng1.setForeground(new java.awt.Color(5, 88, 102));
        labelDNIIng1.setText("DNI (número)");

        DNIMozoModificar.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        DNIMozoModificar.setModel(new javax.swing.SpinnerNumberModel(10000000, 10000000, 100000000, 1));

        javax.swing.GroupLayout panelModificarMozo2Layout = new javax.swing.GroupLayout(panelModificarMozo2);
        panelModificarMozo2.setLayout(panelModificarMozo2Layout);
        panelModificarMozo2Layout.setHorizontalGroup(
            panelModificarMozo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarMozo2Layout.createSequentialGroup()
                .addGap(204, 204, 204)
                .addComponent(DNIMozoModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(580, Short.MAX_VALUE))
            .addGroup(panelModificarMozo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelModificarMozo2Layout.createSequentialGroup()
                    .addGap(202, 202, 202)
                    .addGroup(panelModificarMozo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane23)
                        .addGroup(panelModificarMozo2Layout.createSequentialGroup()
                            .addComponent(botonCancelarMozoModificar2)
                            .addGap(195, 195, 195)
                            .addComponent(botonModificarMozo2))
                        .addComponent(jScrollPane21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelCodigoMozo1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelIngreseMozo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelNombreMozo1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelDNIIng1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelDomMozo1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGap(203, 203, 203)))
        );
        panelModificarMozo2Layout.setVerticalGroup(
            panelModificarMozo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarMozo2Layout.createSequentialGroup()
                .addContainerGap(409, Short.MAX_VALUE)
                .addComponent(DNIMozoModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(250, 250, 250))
            .addGroup(panelModificarMozo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelModificarMozo2Layout.createSequentialGroup()
                    .addGap(53, 53, 53)
                    .addComponent(labelIngreseMozo1)
                    .addGap(48, 48, 48)
                    .addComponent(labelCodigoMozo1)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(labelNombreMozo1)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(labelDNIIng1)
                    .addGap(66, 66, 66)
                    .addComponent(labelDomMozo1)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(40, 40, 40)
                    .addGroup(panelModificarMozo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botonCancelarMozoModificar2)
                        .addComponent(botonModificarMozo2))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(panelModificarMozo2);
        panelModificarMozo2.setBounds(200, 0, 1000, 700);

        panelInsertarMozo.setBackground(new java.awt.Color(255, 255, 255));
        panelInsertarMozo.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelIngreseMozo.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseMozo.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseMozo.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseMozo.setText("Ingrese los datos del mozo");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyPressed(evt);
            }
        });

        textNombreMozoIngreso.setBackground(new java.awt.Color(255, 255, 255));
        textNombreMozoIngreso.setColumns(40);
        textNombreMozoIngreso.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textNombreMozoIngreso.setForeground(new java.awt.Color(5, 88, 102));
        textNombreMozoIngreso.setRows(1);
        textNombreMozoIngreso.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textNombreMozoIngreso.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textNombreMozoIngreso.setSelectionColor(new java.awt.Color(153, 153, 153));
        textNombreMozoIngreso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textNombreMozoIngresoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(textNombreMozoIngreso);

        labelNombreMozo.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreMozo.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelNombreMozo.setForeground(new java.awt.Color(5, 88, 102));
        labelNombreMozo.setText("Nombre y apellido");

        labelDNIIng.setBackground(new java.awt.Color(255, 255, 255));
        labelDNIIng.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelDNIIng.setForeground(new java.awt.Color(5, 88, 102));
        labelDNIIng.setText("DNI (número)");

        labelDomMozo.setBackground(new java.awt.Color(255, 255, 255));
        labelDomMozo.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelDomMozo.setForeground(new java.awt.Color(5, 88, 102));
        labelDomMozo.setText("Domicilio");

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));

        textDomicilioMozoIngreso.setBackground(new java.awt.Color(255, 255, 255));
        textDomicilioMozoIngreso.setColumns(40);
        textDomicilioMozoIngreso.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textDomicilioMozoIngreso.setForeground(new java.awt.Color(5, 88, 102));
        textDomicilioMozoIngreso.setRows(1);
        textDomicilioMozoIngreso.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textDomicilioMozoIngreso.setSelectionColor(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(textDomicilioMozoIngreso);

        botonCancelarMozosIngreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarMozosIngreso.setBorder(null);
        botonCancelarMozosIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarMozosIngresoActionPerformed(evt);
            }
        });

        botonIngresarMozos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertar.jpg"))); // NOI18N
        botonIngresarMozos.setBorder(null);
        botonIngresarMozos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonIngresarMozosActionPerformed(evt);
            }
        });

        DNIMozoIngreso.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        DNIMozoIngreso.setModel(new javax.swing.SpinnerNumberModel(5000000, 5000000, 100000000, 1));

        javax.swing.GroupLayout panelInsertarMozoLayout = new javax.swing.GroupLayout(panelInsertarMozo);
        panelInsertarMozo.setLayout(panelInsertarMozoLayout);
        panelInsertarMozoLayout.setHorizontalGroup(
            panelInsertarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInsertarMozoLayout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addGroup(panelInsertarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInsertarMozoLayout.createSequentialGroup()
                        .addComponent(DNIMozoIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelInsertarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelInsertarMozoLayout.createSequentialGroup()
                            .addComponent(botonCancelarMozosIngreso)
                            .addGap(195, 195, 195)
                            .addComponent(botonIngresarMozos))
                        .addComponent(labelIngreseMozo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelNombreMozo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelDNIIng, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelDomMozo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(199, 199, 199))
        );
        panelInsertarMozoLayout.setVerticalGroup(
            panelInsertarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInsertarMozoLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(labelIngreseMozo)
                .addGap(65, 65, 65)
                .addComponent(labelNombreMozo)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelDNIIng)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DNIMozoIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelDomMozo)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addGroup(panelInsertarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonCancelarMozosIngreso)
                    .addComponent(botonIngresarMozos))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        getContentPane().add(panelInsertarMozo);
        panelInsertarMozo.setBounds(200, 0, 1000, 700);

        panelInsertarPlato.setBackground(new java.awt.Color(255, 255, 255));
        panelInsertarPlato.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelInsertarPlato.setLayout(null);

        labelCodigoConsumo6.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo6.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo6.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo6.setText("Precio de costo");
        panelInsertarPlato.add(labelCodigoConsumo6);
        labelCodigoConsumo6.setBounds(570, 150, 306, 36);

        labelCodigoConsumo3.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo3.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo3.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo3.setText("Nombre");
        panelInsertarPlato.add(labelCodigoConsumo3);
        labelCodigoConsumo3.setBounds(160, 150, 306, 36);

        labelIngreseConsumo1.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseConsumo1.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseConsumo1.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseConsumo1.setText("Ingrese los datos del plato");
        panelInsertarPlato.add(labelIngreseConsumo1);
        labelIngreseConsumo1.setBounds(204, 53, 598, 56);

        labelCodigoConsumo4.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo4.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo4.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo4.setText("Descripción");
        panelInsertarPlato.add(labelCodigoConsumo4);
        labelCodigoConsumo4.setBounds(160, 250, 306, 36);

        jScrollPane10.setBackground(new java.awt.Color(255, 255, 255));

        textCodIngMesaD.setBackground(new java.awt.Color(255, 255, 255));
        textCodIngMesaD.setColumns(20);
        textCodIngMesaD.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textCodIngMesaD.setForeground(new java.awt.Color(5, 88, 102));
        textCodIngMesaD.setLineWrap(true);
        textCodIngMesaD.setRows(5);
        textCodIngMesaD.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textCodIngMesaD.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textCodIngMesaD.setSelectionColor(new java.awt.Color(153, 153, 153));
        jScrollPane10.setViewportView(textCodIngMesaD);

        panelInsertarPlato.add(jScrollPane10);
        jScrollPane10.setBounds(160, 300, 294, 126);

        labelCodigoConsumo5.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo5.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo5.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo5.setText("Tipo");
        panelInsertarPlato.add(labelCodigoConsumo5);
        labelCodigoConsumo5.setBounds(160, 440, 306, 36);

        jComboBox2.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox2.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija el tipo", "Entrada", "Plato Principal", "Postre" }));
        jComboBox2.setPreferredSize(new java.awt.Dimension(272, 30));
        panelInsertarPlato.add(jComboBox2);
        jComboBox2.setBounds(160, 490, 290, 30);

        jSpinner1.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        panelInsertarPlato.add(jSpinner1);
        jSpinner1.setBounds(570, 220, 272, 28);

        labelCodigoConsumo7.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo7.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo7.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo7.setText("Precio de venta");
        panelInsertarPlato.add(labelCodigoConsumo7);
        labelCodigoConsumo7.setBounds(570, 290, 306, 36);

        labelCodigoConsumo8.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo8.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo8.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo8.setText("Precio de promoción");
        panelInsertarPlato.add(labelCodigoConsumo8);
        labelCodigoConsumo8.setBounds(570, 430, 306, 36);

        jSpinner2.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        panelInsertarPlato.add(jSpinner2);
        jSpinner2.setBounds(570, 350, 272, 28);

        jSpinner3.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        panelInsertarPlato.add(jSpinner3);
        jSpinner3.setBounds(570, 490, 272, 28);

        jScrollPane11.setBackground(new java.awt.Color(255, 255, 255));

        textCodIngNombrePlato.setBackground(new java.awt.Color(255, 255, 255));
        textCodIngNombrePlato.setColumns(20);
        textCodIngNombrePlato.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textCodIngNombrePlato.setForeground(new java.awt.Color(5, 88, 102));
        textCodIngNombrePlato.setRows(1);
        textCodIngNombrePlato.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textCodIngNombrePlato.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textCodIngNombrePlato.setSelectionColor(new java.awt.Color(153, 153, 153));
        textCodIngNombrePlato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textCodIngNombrePlatoKeyTyped(evt);
            }
        });
        jScrollPane11.setViewportView(textCodIngNombrePlato);

        panelInsertarPlato.add(jScrollPane11);
        jScrollPane11.setBounds(160, 210, 294, 30);

        botonCancelarInsertarPlato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarInsertarPlato.setBorder(null);
        botonCancelarInsertarPlato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarInsertarPlatoActionPerformed(evt);
            }
        });
        panelInsertarPlato.add(botonCancelarInsertarPlato);
        botonCancelarInsertarPlato.setBounds(205, 584, 200, 50);

        botonInsertarPlato1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertar.jpg"))); // NOI18N
        botonInsertarPlato1.setBorder(null);
        botonInsertarPlato1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarPlato1ActionPerformed(evt);
            }
        });
        panelInsertarPlato.add(botonInsertarPlato1);
        botonInsertarPlato1.setBounds(610, 584, 200, 50);

        getContentPane().add(panelInsertarPlato);
        panelInsertarPlato.setBounds(200, 0, 1000, 700);

        panelInsertarMesa.setBackground(new java.awt.Color(255, 255, 255));
        panelInsertarMesa.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelInsertarMesa.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelInsertarMesa.setLayout(null);

        labelMozoAsignadoMesa.setBackground(new java.awt.Color(255, 255, 255));
        labelMozoAsignadoMesa.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelMozoAsignadoMesa.setForeground(new java.awt.Color(5, 88, 102));
        panelInsertarMesa.add(labelMozoAsignadoMesa);
        labelMozoAsignadoMesa.setBounds(790, 202, 0, 0);

        labelIngreseMesa.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseMesa.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseMesa.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseMesa.setText("Ingrese los datos de la mesa");
        panelInsertarMesa.add(labelIngreseMesa);
        labelIngreseMesa.setBounds(190, 70, 640, 56);

        labelSectorMesa.setBackground(new java.awt.Color(255, 255, 255));
        labelSectorMesa.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelSectorMesa.setForeground(new java.awt.Color(5, 88, 102));
        labelSectorMesa.setText("Mozo a asignar:");
        panelInsertarMesa.add(labelSectorMesa);
        labelSectorMesa.setBounds(360, 260, 240, 36);

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));

        textSectorMesaIngreso.setBackground(new java.awt.Color(255, 255, 255));
        textSectorMesaIngreso.setColumns(20);
        textSectorMesaIngreso.setFont(new java.awt.Font("Caviar Dreams", 0, 20)); // NOI18N
        textSectorMesaIngreso.setForeground(new java.awt.Color(5, 88, 102));
        textSectorMesaIngreso.setRows(1);
        textSectorMesaIngreso.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textSectorMesaIngreso.setSelectedTextColor(new java.awt.Color(5, 88, 102));
        textSectorMesaIngreso.setSelectionColor(new java.awt.Color(153, 153, 153));
        jScrollPane6.setViewportView(textSectorMesaIngreso);
        textSectorMesaIngreso.getAccessibleContext().setAccessibleName("");

        panelInsertarMesa.add(jScrollPane6);
        jScrollPane6.setBounds(360, 210, 294, 30);

        tablaMozos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "DNI", "Nombre", "Title 4"
            }
        ));
        tablaMozos1.setShowGrid(false);
        tablaMozosScroll1.setViewportView(tablaMozos1);

        panelInsertarMesa.add(tablaMozosScroll1);
        tablaMozosScroll1.setBounds(360, 310, 294, 184);

        botonCancelarIngresoMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarIngresoMesa.setBorder(null);
        botonCancelarIngresoMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarIngresoMesaActionPerformed(evt);
            }
        });
        panelInsertarMesa.add(botonCancelarIngresoMesa);
        botonCancelarIngresoMesa.setBounds(190, 590, 200, 50);

        botonMesaIngresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertar.jpg"))); // NOI18N
        botonMesaIngresar.setBorder(null);
        botonMesaIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMesaIngresarActionPerformed(evt);
            }
        });
        panelInsertarMesa.add(botonMesaIngresar);
        botonMesaIngresar.setBounds(630, 590, 200, 50);

        comboMozoMesa.setBackground(new java.awt.Color(255, 255, 255));
        comboMozoMesa.setFont(new java.awt.Font("Caviar Dreams", 1, 14)); // NOI18N
        comboMozoMesa.setForeground(new java.awt.Color(5, 88, 102));
        comboMozoMesa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un mozo" }));
        comboMozoMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMozoMesaActionPerformed(evt);
            }
        });
        panelInsertarMesa.add(comboMozoMesa);
        comboMozoMesa.setBounds(360, 510, 294, 33);

        labelSectorMesa1.setBackground(new java.awt.Color(255, 255, 255));
        labelSectorMesa1.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelSectorMesa1.setForeground(new java.awt.Color(5, 88, 102));
        labelSectorMesa1.setText("Sector");
        panelInsertarMesa.add(labelSectorMesa1);
        labelSectorMesa1.setBounds(360, 160, 87, 36);

        getContentPane().add(panelInsertarMesa);
        panelInsertarMesa.setBounds(200, 0, 1000, 700);

        panelInsertarConsumo1.setBackground(new java.awt.Color(255, 255, 255));
        panelInsertarConsumo1.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelInsertarConsumo1.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelInsertarConsumo1.setLayout(null);

        labelIngreseConsumo.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseConsumo.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseConsumo.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseConsumo.setText("Ingrese los datos del consumo");
        panelInsertarConsumo1.add(labelIngreseConsumo);
        labelIngreseConsumo.setBounds(170, 80, 678, 56);

        labelCodigoMesaConsumo.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo.setText("Platos consumidos");
        panelInsertarConsumo1.add(labelCodigoMesaConsumo);
        labelCodigoMesaConsumo.setBounds(450, 290, 260, 36);

        tablaPlatos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción", "Tipo", "Costo", "Venta", "Promoción"
            }
        ));
        tablaPlatos1.setShowGrid(false);
        tablaMesasScroll1.setViewportView(tablaPlatos1);

        panelInsertarConsumo1.add(tablaMesasScroll1);
        tablaMesasScroll1.setBounds(450, 340, 480, 184);

        labelCodigoMesaConsumo1.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo1.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo1.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo1.setText("Fecha");
        panelInsertarConsumo1.add(labelCodigoMesaConsumo1);
        labelCodigoMesaConsumo1.setBounds(640, 170, 90, 36);

        spinnerHoraConsumos.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        spinnerHoraConsumos.setModel(new javax.swing.SpinnerNumberModel(0, 0, 23, 1));
        spinnerHoraConsumos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelInsertarConsumo1.add(spinnerHoraConsumos);
        spinnerHoraConsumos.setBounds(160, 220, 80, 28);

        spinnerMinutosConsumos.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        spinnerMinutosConsumos.setModel(new javax.swing.SpinnerNumberModel(0, 0, 59, 1));
        panelInsertarConsumo1.add(spinnerMinutosConsumos);
        spinnerMinutosConsumos.setBounds(250, 220, 80, 28);

        botonCancelarInsertarConsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarInsertarConsumo.setBorder(null);
        botonCancelarInsertarConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarInsertarConsumoActionPerformed(evt);
            }
        });
        panelInsertarConsumo1.add(botonCancelarInsertarConsumo);
        botonCancelarInsertarConsumo.setBounds(170, 590, 200, 50);

        botonInsertarConsumo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertar.jpg"))); // NOI18N
        botonInsertarConsumo1.setBorder(null);
        botonInsertarConsumo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarConsumo1ActionPerformed(evt);
            }
        });
        panelInsertarConsumo1.add(botonInsertarConsumo1);
        botonInsertarConsumo1.setBounds(630, 590, 200, 50);

        labelCodigoMesaConsumo2.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo2.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo2.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo2.setText("Mesa");
        panelInsertarConsumo1.add(labelCodigoMesaConsumo2);
        labelCodigoMesaConsumo2.setBounds(100, 290, 90, 36);

        tablaMesas4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        tablaMesas4.setShowGrid(false);
        tablaMesasScroll2.setViewportView(tablaMesas4);

        panelInsertarConsumo1.add(tablaMesasScroll2);
        tablaMesasScroll2.setBounds(100, 340, 306, 184);

        jComboBox7.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox7.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox7.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });
        panelInsertarConsumo1.add(jComboBox7);
        jComboBox7.setBounds(180, 290, 220, 40);

        botonPlatoConsumoModificar1.setBackground(new java.awt.Color(5, 88, 102));
        botonPlatoConsumoModificar1.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonPlatoConsumoModificar1.setForeground(new java.awt.Color(255, 255, 255));
        botonPlatoConsumoModificar1.setText(" Agregar platos");
        botonPlatoConsumoModificar1.setBorder(null);
        botonPlatoConsumoModificar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlatoConsumoModificar1ActionPerformed(evt);
            }
        });
        panelInsertarConsumo1.add(botonPlatoConsumoModificar1);
        botonPlatoConsumoModificar1.setBounds(710, 290, 220, 40);

        jDateChooser1.setPreferredSize(new java.awt.Dimension(290, 30));
        panelInsertarConsumo1.add(jDateChooser1);
        jDateChooser1.setBounds(540, 220, 290, 30);

        labelCodigoMesaConsumo11.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo11.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo11.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo11.setText("Hora");
        panelInsertarConsumo1.add(labelCodigoMesaConsumo11);
        labelCodigoMesaConsumo11.setBounds(210, 170, 70, 36);

        getContentPane().add(panelInsertarConsumo1);
        panelInsertarConsumo1.setBounds(200, 0, 1000, 700);

        panelInsertarConsumo2.setBackground(new java.awt.Color(255, 255, 255));
        panelInsertarConsumo2.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelInsertarConsumo2.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelCodigoMesaConsumo9.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo9.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo9.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo9.setText("Platos consumidos");

        tablaPlatosConsumoInsertar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción", "Tipo", "Costo", "Venta", "Promoción"
            }
        ));
        tablaPlatosConsumoInsertar.setShowGrid(false);
        tablaMesasScroll7.setViewportView(tablaPlatosConsumoInsertar);

        labelCodigoMesaConsumo10.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo10.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo10.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo10.setText("<html><p style=\"text-align:center\">Seleccione un plato<br>y elimine o agregue<br>a la lista</p></html>");

        jComboBox8.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox8.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox8.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un plato" }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        botonEliminarPlatoConsumoIng1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminar.jpg"))); // NOI18N
        botonEliminarPlatoConsumoIng1.setBorder(null);
        botonEliminarPlatoConsumoIng1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarPlatoConsumoIng1ActionPerformed(evt);
            }
        });

        botonInsertarPlatoConsumoIng1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertar.jpg"))); // NOI18N
        botonInsertarPlatoConsumoIng1.setBorder(null);
        botonInsertarPlatoConsumoIng1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarPlatoConsumoIng1ActionPerformed(evt);
            }
        });

        botonVolverPlatosConsumoIngreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverPlatosConsumoIngreso.setBorder(null);
        botonVolverPlatosConsumoIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverPlatosConsumoIngresoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInsertarConsumo2Layout = new javax.swing.GroupLayout(panelInsertarConsumo2);
        panelInsertarConsumo2.setLayout(panelInsertarConsumo2Layout);
        panelInsertarConsumo2Layout.setHorizontalGroup(
            panelInsertarConsumo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                .addGroup(panelInsertarConsumo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addComponent(labelCodigoMesaConsumo9))
                    .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(botonVolverPlatosConsumoIngreso))
                    .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(tablaMesasScroll7, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelInsertarConsumo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(labelCodigoMesaConsumo10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(panelInsertarConsumo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(botonInsertarPlatoConsumoIng1)
                                    .addComponent(botonEliminarPlatoConsumoIng1)))
                            .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        panelInsertarConsumo2Layout.setVerticalGroup(
            panelInsertarConsumo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInsertarConsumo2Layout.createSequentialGroup()
                .addContainerGap(144, Short.MAX_VALUE)
                .addComponent(labelCodigoMesaConsumo9)
                .addGap(18, 18, 18)
                .addGroup(panelInsertarConsumo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelInsertarConsumo2Layout.createSequentialGroup()
                        .addComponent(labelCodigoMesaConsumo10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonEliminarPlatoConsumoIng1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonInsertarPlatoConsumoIng1))
                    .addComponent(tablaMesasScroll7, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addComponent(botonVolverPlatosConsumoIngreso)
                .addGap(66, 66, 66))
        );

        getContentPane().add(panelInsertarConsumo2);
        panelInsertarConsumo2.setBounds(200, 0, 1000, 700);

        panelModificarConsumo3.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarConsumo3.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelCodigoMesaConsumo7.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo7.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo7.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo7.setText("<html><p style=\"text-align:center\">Seleccione un plato<br>y elimine o agregue<br>a la lista</p></html>");

        tablaPlatosConsumoModificar2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción", "Tipo", "Costo", "Venta", "Promoción"
            }
        ));
        tablaPlatosConsumoModificar2.setShowGrid(false);
        tablaMesasScroll6.setViewportView(tablaPlatosConsumoModificar2);

        botonVolverPlatosConsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverPlatosConsumo.setBorder(null);
        botonVolverPlatosConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverPlatosConsumoActionPerformed(evt);
            }
        });

        labelCodigoMesaConsumo8.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo8.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo8.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo8.setText("Platos consumidos");

        botonEliminarPlatoConsumoMod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminar.jpg"))); // NOI18N
        botonEliminarPlatoConsumoMod.setBorder(null);
        botonEliminarPlatoConsumoMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarPlatoConsumoModActionPerformed(evt);
            }
        });

        botonInsertarPlatoConsumoMod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertar.jpg"))); // NOI18N
        botonInsertarPlatoConsumoMod.setBorder(null);
        botonInsertarPlatoConsumoMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarPlatoConsumoModActionPerformed(evt);
            }
        });

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un plato" }));

        javax.swing.GroupLayout panelModificarConsumo3Layout = new javax.swing.GroupLayout(panelModificarConsumo3);
        panelModificarConsumo3.setLayout(panelModificarConsumo3Layout);
        panelModificarConsumo3Layout.setHorizontalGroup(
            panelModificarConsumo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarConsumo3Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addGroup(panelModificarConsumo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelModificarConsumo3Layout.createSequentialGroup()
                        .addComponent(tablaMesasScroll6, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelModificarConsumo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelModificarConsumo3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panelModificarConsumo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelCodigoMesaConsumo7)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelModificarConsumo3Layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(panelModificarConsumo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(botonInsertarPlatoConsumoMod)
                                    .addComponent(botonEliminarPlatoConsumoMod)))))
                    .addGroup(panelModificarConsumo3Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(labelCodigoMesaConsumo8)))
                .addContainerGap(112, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarConsumo3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonVolverPlatosConsumo)
                .addGap(368, 368, 368))
        );
        panelModificarConsumo3Layout.setVerticalGroup(
            panelModificarConsumo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarConsumo3Layout.createSequentialGroup()
                .addContainerGap(136, Short.MAX_VALUE)
                .addComponent(labelCodigoMesaConsumo8)
                .addGap(18, 18, 18)
                .addGroup(panelModificarConsumo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelModificarConsumo3Layout.createSequentialGroup()
                        .addComponent(labelCodigoMesaConsumo7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonEliminarPlatoConsumoMod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonInsertarPlatoConsumoMod)
                        .addGap(26, 26, 26))
                    .addComponent(tablaMesasScroll6, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(73, 73, 73)
                .addComponent(botonVolverPlatosConsumo)
                .addGap(76, 76, 76))
        );

        getContentPane().add(panelModificarConsumo3);
        panelModificarConsumo3.setBounds(200, 0, 1000, 700);

        panelEliminarPlato.setBackground(new java.awt.Color(255, 255, 255));
        panelEliminarPlato.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelCodigoConsumo11.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo11.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo11.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo11.setText("Seleccione un plato");

        tablaEliminarPlato.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción", "Tipo", "Costo", "Venta", "Promoción"
            }
        ));
        jScrollPane13.setViewportView(tablaEliminarPlato);

        jComboBox5.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox5.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox5.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un plato" }));

        botonEliminarPlato1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminar.jpg"))); // NOI18N
        botonEliminarPlato1.setBorder(null);
        botonEliminarPlato1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarPlato1ActionPerformed(evt);
            }
        });

        botonCancelarEliminarPlato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarEliminarPlato.setBorder(null);
        botonCancelarEliminarPlato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarEliminarPlatoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEliminarPlatoLayout = new javax.swing.GroupLayout(panelEliminarPlato);
        panelEliminarPlato.setLayout(panelEliminarPlatoLayout);
        panelEliminarPlatoLayout.setHorizontalGroup(
            panelEliminarPlatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarPlatoLayout.createSequentialGroup()
                .addGroup(panelEliminarPlatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEliminarPlatoLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelEliminarPlatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelEliminarPlatoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panelEliminarPlatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelCodigoConsumo11, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelEliminarPlatoLayout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(botonEliminarPlato1))))
                    .addGroup(panelEliminarPlatoLayout.createSequentialGroup()
                        .addGap(399, 399, 399)
                        .addComponent(botonCancelarEliminarPlato)))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        panelEliminarPlatoLayout.setVerticalGroup(
            panelEliminarPlatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarPlatoLayout.createSequentialGroup()
                .addContainerGap(102, Short.MAX_VALUE)
                .addGroup(panelEliminarPlatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelEliminarPlatoLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo11)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonEliminarPlato1)))
                .addGap(57, 57, 57)
                .addComponent(botonCancelarEliminarPlato)
                .addGap(64, 64, 64))
        );

        getContentPane().add(panelEliminarPlato);
        panelEliminarPlato.setBounds(200, 0, 1000, 700);

        panelCantTotalPlatosPorMesa.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelCantTotalPlatosPorMesa.setLayout(null);

        labelCantPlatos.setFont(new java.awt.Font("Caviar Dreams", 0, 36)); // NOI18N
        labelCantPlatos.setForeground(new java.awt.Color(5, 88, 102));
        labelCantPlatos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCantPlatos.setPreferredSize(new java.awt.Dimension(100, 16));
        panelCantTotalPlatosPorMesa.add(labelCantPlatos);
        labelCantPlatos.setBounds(575, 429, 180, 50);

        jComboBox15.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox15.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox15.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));
        jComboBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox15ActionPerformed(evt);
            }
        });
        panelCantTotalPlatosPorMesa.add(jComboBox15);
        jComboBox15.setBounds(500, 240, 310, 30);

        botonVolverCantPlatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverCantPlatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverCantPlatosActionPerformed(evt);
            }
        });
        panelCantTotalPlatosPorMesa.add(botonVolverCantPlatos);
        botonVolverCantPlatos.setBounds(360, 550, 250, 60);

        botonVerCantPlatos1.setBackground(new java.awt.Color(5, 88, 102));
        botonVerCantPlatos1.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonVerCantPlatos1.setForeground(new java.awt.Color(255, 255, 255));
        botonVerCantPlatos1.setText("Ver");
        botonVerCantPlatos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerCantPlatos1ActionPerformed(evt);
            }
        });
        panelCantTotalPlatosPorMesa.add(botonVerCantPlatos1);
        botonVerCantPlatos1.setBounds(500, 310, 300, 50);

        tablaCantPlatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        jScrollPane33.setViewportView(tablaCantPlatos);

        panelCantTotalPlatosPorMesa.add(jScrollPane33);
        jScrollPane33.setBounds(170, 102, 320, 300);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/panelCantidadPlatos.jpg"))); // NOI18N

        javax.swing.GroupLayout fondoLayout = new javax.swing.GroupLayout(fondo);
        fondo.setLayout(fondoLayout);
        fondoLayout.setHorizontalGroup(
            fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fondoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        fondoLayout.setVerticalGroup(
            fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fondoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelCantTotalPlatosPorMesa.add(fondo);
        fondo.setBounds(0, 0, 1000, 700);

        getContentPane().add(panelCantTotalPlatosPorMesa);
        panelCantTotalPlatosPorMesa.setBounds(200, 0, 1000, 700);

        panelModificarPlato1.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarPlato1.setPreferredSize(new java.awt.Dimension(1000, 700));

        tablaModificarPlato1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción", "Tipo", "Costo", "Venta", "Promoción"
            }
        ));
        jScrollPane32.setViewportView(tablaModificarPlato1);

        labelCodigoConsumo23.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo23.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo23.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo23.setText("Seleccione un plato");
        labelCodigoConsumo23.setToolTipText("");

        jComboBox14.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox14.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox14.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        botonCancelarModificarPlato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarModificarPlato.setBorder(null);
        botonCancelarModificarPlato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarModificarPlatoActionPerformed(evt);
            }
        });

        botonModificarPlato1.setBackground(new java.awt.Color(255, 255, 255));
        botonModificarPlato1.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonModificarPlato1.setForeground(new java.awt.Color(255, 255, 255));
        botonModificarPlato1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarPlato1.setBorder(null);
        botonModificarPlato1.setPreferredSize(new java.awt.Dimension(200, 100));
        botonModificarPlato1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarPlato1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarPlato1Layout = new javax.swing.GroupLayout(panelModificarPlato1);
        panelModificarPlato1.setLayout(panelModificarPlato1Layout);
        panelModificarPlato1Layout.setHorizontalGroup(
            panelModificarPlato1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarPlato1Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(panelModificarPlato1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonCancelarModificarPlato)
                    .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelModificarPlato1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModificarPlato1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(botonModificarPlato1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelCodigoConsumo23, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        panelModificarPlato1Layout.setVerticalGroup(
            panelModificarPlato1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarPlato1Layout.createSequentialGroup()
                .addContainerGap(104, Short.MAX_VALUE)
                .addGroup(panelModificarPlato1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModificarPlato1Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo23)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(botonModificarPlato1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(59, 59, 59)
                .addComponent(botonCancelarModificarPlato)
                .addGap(60, 60, 60))
        );

        getContentPane().add(panelModificarPlato1);
        panelModificarPlato1.setBounds(200, 0, 1000, 700);

        panelPlatosPorFecha.setBackground(new java.awt.Color(255, 255, 255));
        panelPlatosPorFecha.setMaximumSize(new java.awt.Dimension(1000, 800));
        panelPlatosPorFecha.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelMozosTitulo5.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo5.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo5.setText("Platos entre dos fechas");

        jLabel5.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(5, 88, 102));
        jLabel5.setText("Elija la fecha de fin:");

        jLabel6.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(5, 88, 102));
        jLabel6.setText("Elija la fecha de inicio:");

        tablaPlatosEntreFechas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Código", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane17.setViewportView(tablaPlatosEntreFechas);

        botonCancelarPlatosEntreFechas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonCancelarPlatosEntreFechas.setBorder(null);
        botonCancelarPlatosEntreFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarPlatosEntreFechasActionPerformed(evt);
            }
        });

        botonBuscarPlatosEntreFechas.setBackground(new java.awt.Color(5, 88, 102));
        botonBuscarPlatosEntreFechas.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonBuscarPlatosEntreFechas.setForeground(new java.awt.Color(255, 255, 255));
        botonBuscarPlatosEntreFechas.setText("Buscar");
        botonBuscarPlatosEntreFechas.setBorder(null);
        botonBuscarPlatosEntreFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarPlatosEntreFechasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPlatosPorFechaLayout = new javax.swing.GroupLayout(panelPlatosPorFecha);
        panelPlatosPorFecha.setLayout(panelPlatosPorFechaLayout);
        panelPlatosPorFechaLayout.setHorizontalGroup(
            panelPlatosPorFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlatosPorFechaLayout.createSequentialGroup()
                .addGap(393, 393, 393)
                .addComponent(botonCancelarPlatosEntreFechas)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlatosPorFechaLayout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addGroup(panelPlatosPorFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPlatosPorFechaLayout.createSequentialGroup()
                        .addGroup(panelPlatosPorFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelPlatosPorFechaLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(15, 15, 15))
                            .addComponent(botonBuscarPlatosEntreFechas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelMozosTitulo5, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(185, 185, 185))
        );
        panelPlatosPorFechaLayout.setVerticalGroup(
            panelPlatosPorFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlatosPorFechaLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(labelMozosTitulo5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelPlatosPorFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPlatosPorFechaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelPlatosPorFechaLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(botonBuscarPlatosEntreFechas, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(botonCancelarPlatosEntreFechas)
                .addGap(40, 40, 40))
        );

        getContentPane().add(panelPlatosPorFecha);
        panelPlatosPorFecha.setBounds(200, 0, 1000, 700);

        panelModificarConsumo2.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarConsumo2.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelModificarConsumo2.setLayout(null);

        jDateChooser4.setAlignmentX(0.0F);
        jDateChooser4.setPreferredSize(new java.awt.Dimension(272, 22));
        panelModificarConsumo2.add(jDateChooser4);
        jDateChooser4.setBounds(520, 220, 290, 30);

        labelIngreseConsumo3.setBackground(new java.awt.Color(255, 255, 255));
        labelIngreseConsumo3.setFont(new java.awt.Font("Caviar Dreams", 0, 48)); // NOI18N
        labelIngreseConsumo3.setForeground(new java.awt.Color(5, 88, 102));
        labelIngreseConsumo3.setText("Ingrese los datos del consumo");
        panelModificarConsumo2.add(labelIngreseConsumo3);
        labelIngreseConsumo3.setBounds(170, 70, 678, 56);

        labelCodigoMesaConsumo3.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo3.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo3.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo3.setText("Fecha");
        panelModificarConsumo2.add(labelCodigoMesaConsumo3);
        labelCodigoMesaConsumo3.setBounds(620, 170, 90, 36);

        spinnerMinutosConsumosModificar.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        spinnerMinutosConsumosModificar.setModel(new javax.swing.SpinnerNumberModel(0, 0, 59, 1));
        panelModificarConsumo2.add(spinnerMinutosConsumosModificar);
        spinnerMinutosConsumosModificar.setBounds(270, 220, 80, 28);

        spinnerHoraConsumosModificar.setFont(new java.awt.Font("Caviar Dreams", 0, 18)); // NOI18N
        spinnerHoraConsumosModificar.setModel(new javax.swing.SpinnerNumberModel(0, 0, 23, 1));
        spinnerHoraConsumosModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelModificarConsumo2.add(spinnerHoraConsumosModificar);
        spinnerHoraConsumosModificar.setBounds(170, 220, 80, 28);

        labelCodigoMesaConsumo4.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo4.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo4.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo4.setText("Mesa");
        panelModificarConsumo2.add(labelCodigoMesaConsumo4);
        labelCodigoMesaConsumo4.setBounds(110, 270, 90, 36);

        jComboBox13.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox13.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox13.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));
        panelModificarConsumo2.add(jComboBox13);
        jComboBox13.setBounds(190, 280, 220, 30);

        labelCodigoMesaConsumo5.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo5.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo5.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo5.setText("Platos consumidos");
        panelModificarConsumo2.add(labelCodigoMesaConsumo5);
        labelCodigoMesaConsumo5.setBounds(440, 270, 250, 36);

        botonCancelarModificarConsumo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarModificarConsumo2.setBorder(null);
        botonCancelarModificarConsumo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarModificarConsumo2ActionPerformed(evt);
            }
        });
        panelModificarConsumo2.add(botonCancelarModificarConsumo2);
        botonCancelarModificarConsumo2.setBounds(180, 570, 200, 50);

        botonModificarConsumo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarConsumo2.setBorder(null);
        botonModificarConsumo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarConsumo2ActionPerformed(evt);
            }
        });
        panelModificarConsumo2.add(botonModificarConsumo2);
        botonModificarConsumo2.setBounds(600, 570, 200, 50);

        tablaPlatosConsumoModificar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "", null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción", "Tipo", "Costo", "Venta", "Promoción"
            }
        ));
        tablaPlatosConsumoModificar.setShowGrid(false);
        tablaMesasScroll3.setViewportView(tablaPlatosConsumoModificar);

        panelModificarConsumo2.add(tablaMesasScroll3);
        tablaMesasScroll3.setBounds(440, 320, 480, 184);

        tablaMesasConsumoModificar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        tablaMesasConsumoModificar.setShowGrid(false);
        tablaMesasScroll4.setViewportView(tablaMesasConsumoModificar);

        panelModificarConsumo2.add(tablaMesasScroll4);
        tablaMesasScroll4.setBounds(110, 320, 306, 184);

        botonPlatoConsumoModificar.setBackground(new java.awt.Color(5, 88, 102));
        botonPlatoConsumoModificar.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonPlatoConsumoModificar.setForeground(new java.awt.Color(255, 255, 255));
        botonPlatoConsumoModificar.setText("Modificar platos");
        botonPlatoConsumoModificar.setBorder(null);
        botonPlatoConsumoModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlatoConsumoModificarActionPerformed(evt);
            }
        });
        panelModificarConsumo2.add(botonPlatoConsumoModificar);
        botonPlatoConsumoModificar.setBounds(700, 280, 220, 33);

        labelCodigoMesaConsumo6.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoMesaConsumo6.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoMesaConsumo6.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoMesaConsumo6.setText("Hora");
        panelModificarConsumo2.add(labelCodigoMesaConsumo6);
        labelCodigoMesaConsumo6.setBounds(220, 170, 70, 36);

        getContentPane().add(panelModificarConsumo2);
        panelModificarConsumo2.setBounds(200, 0, 1000, 700);

        panelEliminarMozoMesa.setBackground(new java.awt.Color(255, 255, 255));
        panelEliminarMozoMesa.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelEliminarMozoMesa.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Caviar Dreams", 0, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(5, 88, 102));
        jLabel10.setText("Seleccione una mesa:");
        panelEliminarMozoMesa.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 200, 368, -1));

        jComboBox17.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox17.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox17.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox17.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));
        jComboBox17.setVerifyInputWhenFocusTarget(false);
        panelEliminarMozoMesa.add(jComboBox17, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 260, 392, -1));

        botonLiberarMozo.setBackground(new java.awt.Color(5, 88, 102));
        botonLiberarMozo.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonLiberarMozo.setForeground(new java.awt.Color(255, 255, 255));
        botonLiberarMozo.setText("Liberar mozo de la mesa");
        botonLiberarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLiberarMozoActionPerformed(evt);
            }
        });
        panelEliminarMozoMesa.add(botonLiberarMozo, new org.netbeans.lib.awtextra.AbsoluteConstraints(537, 380, 392, 54));

        BotonLiberarTodo.setBackground(new java.awt.Color(5, 88, 102));
        BotonLiberarTodo.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        BotonLiberarTodo.setForeground(new java.awt.Color(255, 255, 255));
        BotonLiberarTodo.setText("Liberar mozo de todas las mesas");
        BotonLiberarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonLiberarTodoActionPerformed(evt);
            }
        });
        panelEliminarMozoMesa.add(BotonLiberarTodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(537, 310, -1, 54));

        tabla.setBackground(new java.awt.Color(255, 255, 255));
        tabla.setPreferredSize(new java.awt.Dimension(500, 700));

        tablaEliminarMozoMesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        jScrollPane3.setViewportView(tablaEliminarMozoMesa);

        javax.swing.GroupLayout tablaLayout = new javax.swing.GroupLayout(tabla);
        tabla.setLayout(tablaLayout);
        tablaLayout.setHorizontalGroup(
            tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablaLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tablaLayout.setVerticalGroup(
            tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablaLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        panelEliminarMozoMesa.add(tabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 550));

        lateral.setBackground(new java.awt.Color(255, 255, 255));
        lateral.setPreferredSize(new java.awt.Dimension(500, 700));

        jLabel11.setFont(new java.awt.Font("Caviar Dreams", 0, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(5, 88, 102));
        jLabel11.setText("Seleccione un mozo:");

        botonBuscarMozoMesa.setBackground(new java.awt.Color(5, 88, 102));
        botonBuscarMozoMesa.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonBuscarMozoMesa.setForeground(new java.awt.Color(255, 255, 255));
        botonBuscarMozoMesa.setText("Buscar");
        botonBuscarMozoMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarMozoMesaActionPerformed(evt);
            }
        });

        jComboBox16.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox16.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox16.setForeground(new java.awt.Color(5, 88, 102));
        jComboBox16.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un mozo" }));

        javax.swing.GroupLayout lateralLayout = new javax.swing.GroupLayout(lateral);
        lateral.setLayout(lateralLayout);
        lateralLayout.setHorizontalGroup(
            lateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lateralLayout.createSequentialGroup()
                .addGroup(lateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lateralLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(lateralLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(botonBuscarMozoMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(lateralLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        lateralLayout.setVerticalGroup(
            lateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lateralLayout.createSequentialGroup()
                .addContainerGap(235, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonBuscarMozoMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163))
        );

        panelEliminarMozoMesa.add(lateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, -1, 550));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(1000, 150));

        botonCancelarEliminarMM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonCancelarEliminarMM.setBorder(null);
        botonCancelarEliminarMM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarEliminarMMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(387, Short.MAX_VALUE)
                .addComponent(botonCancelarEliminarMM)
                .addGap(363, 363, 363))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(botonCancelarEliminarMM)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        panelEliminarMozoMesa.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, -1, -1));

        getContentPane().add(panelEliminarMozoMesa);
        panelEliminarMozoMesa.setBounds(200, 0, 1000, 700);

        panelMozos.setBackground(new java.awt.Color(255, 255, 255));
        panelMozos.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelMozos.setPreferredSize(new java.awt.Dimension(1000, 700));

        botonVerMozos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVerMozos.jpg"))); // NOI18N
        botonVerMozos.setBorder(null);
        botonVerMozos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerMozosActionPerformed(evt);
            }
        });

        botonModificarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificarMozo.jpg"))); // NOI18N
        botonModificarMozo.setBorder(null);
        botonModificarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarMozoActionPerformed(evt);
            }
        });

        botonEliminarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminarMozo.jpg"))); // NOI18N
        botonEliminarMozo.setAlignmentY(0.0F);
        botonEliminarMozo.setBorder(null);
        botonEliminarMozo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEliminarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarMozoActionPerformed(evt);
            }
        });

        botonInsertarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertarMozo.jpg"))); // NOI18N
        botonInsertarMozo.setBorder(null);
        botonInsertarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarMozoActionPerformed(evt);
            }
        });

        botonMozosLibres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonMozosLibres.jpg"))); // NOI18N
        botonMozosLibres.setBorder(null);
        botonMozosLibres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMozosLibresActionPerformed(evt);
            }
        });

        botonAsignarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonAsignarMozo.jpg"))); // NOI18N
        botonAsignarMozo.setBorder(null);
        botonAsignarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAsignarMozoActionPerformed(evt);
            }
        });

        botonMesasAsignadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonMesasAsignadas.jpg"))); // NOI18N
        botonMesasAsignadas.setBorder(null);
        botonMesasAsignadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMesasAsignadasActionPerformed(evt);
            }
        });

        botonEliminarMozoMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonLiberarMozo.jpg"))); // NOI18N
        botonEliminarMozoMesa.setBorder(null);
        botonEliminarMozoMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarMozoMesaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMozosLayout = new javax.swing.GroupLayout(panelMozos);
        panelMozos.setLayout(panelMozosLayout);
        panelMozosLayout.setHorizontalGroup(
            panelMozosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMozosLayout.createSequentialGroup()
                .addGap(236, 236, 236)
                .addGroup(panelMozosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMozosLayout.createSequentialGroup()
                        .addComponent(botonAsignarMozo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonEliminarMozoMesa))
                    .addGroup(panelMozosLayout.createSequentialGroup()
                        .addComponent(botonMozosLibres)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonMesasAsignadas))
                    .addGroup(panelMozosLayout.createSequentialGroup()
                        .addComponent(botonEliminarMozo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addComponent(botonModificarMozo))
                    .addGroup(panelMozosLayout.createSequentialGroup()
                        .addComponent(botonInsertarMozo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonVerMozos)))
                .addGap(240, 240, 240))
        );
        panelMozosLayout.setVerticalGroup(
            panelMozosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMozosLayout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addGroup(panelMozosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMozosLayout.createSequentialGroup()
                        .addGroup(panelMozosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonInsertarMozo)
                            .addComponent(botonVerMozos))
                        .addGap(69, 69, 69)
                        .addComponent(botonEliminarMozo))
                    .addComponent(botonModificarMozo))
                .addGap(56, 56, 56)
                .addGroup(panelMozosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonMozosLibres)
                    .addComponent(botonMesasAsignadas))
                .addGap(44, 44, 44)
                .addGroup(panelMozosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonAsignarMozo)
                    .addComponent(botonEliminarMozoMesa))
                .addGap(53, 53, 53))
        );

        getContentPane().add(panelMozos);
        panelMozos.setBounds(200, 0, 1000, 700);

        panelModificarConsumo1.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarConsumo1.setPreferredSize(new java.awt.Dimension(1000, 700));

        tablaModificarConsumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Fecha", "Hora", "Código mesa"
            }
        ));
        jScrollPane27.setViewportView(tablaModificarConsumo);

        labelCodigoConsumo16.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo16.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo16.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo16.setText("Seleccione un consumo");
        labelCodigoConsumo16.setToolTipText("");

        jComboBox11.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox11.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox11.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un consumo", " " }));

        botonModificarConsumo1.setBackground(new java.awt.Color(255, 255, 255));
        botonModificarConsumo1.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonModificarConsumo1.setForeground(new java.awt.Color(255, 255, 255));
        botonModificarConsumo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarConsumo1.setBorder(null);
        botonModificarConsumo1.setPreferredSize(new java.awt.Dimension(200, 100));
        botonModificarConsumo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarConsumo1ActionPerformed(evt);
            }
        });

        botonCancelarModificarConsumo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarModificarConsumo1.setBorder(null);
        botonCancelarModificarConsumo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarModificarConsumo1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarConsumo1Layout = new javax.swing.GroupLayout(panelModificarConsumo1);
        panelModificarConsumo1.setLayout(panelModificarConsumo1Layout);
        panelModificarConsumo1Layout.setHorizontalGroup(
            panelModificarConsumo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarConsumo1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelModificarConsumo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarConsumo1Layout.createSequentialGroup()
                        .addComponent(botonCancelarModificarConsumo1)
                        .addGap(355, 355, 355))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarConsumo1Layout.createSequentialGroup()
                        .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelModificarConsumo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelModificarConsumo1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(labelCodigoConsumo16, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelModificarConsumo1Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(panelModificarConsumo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelModificarConsumo1Layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(botonModificarConsumo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(92, 92, 92))))
        );
        panelModificarConsumo1Layout.setVerticalGroup(
            panelModificarConsumo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarConsumo1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelModificarConsumo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModificarConsumo1Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificarConsumo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(58, 58, 58)
                .addComponent(botonCancelarModificarConsumo1)
                .addGap(68, 68, 68))
        );

        getContentPane().add(panelModificarConsumo1);
        panelModificarConsumo1.setBounds(200, 0, 1000, 700);

        panelModificarMesa1.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarMesa1.setPreferredSize(new java.awt.Dimension(1000, 700));

        tablaModificarMesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        jScrollPane24.setViewportView(tablaModificarMesa);

        labelCodigoConsumo15.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo15.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo15.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo15.setText("Seleccione una mesa");
        labelCodigoConsumo15.setToolTipText("");

        jComboBox10.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox10.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox10.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));

        botonModificarMesa1.setBackground(new java.awt.Color(255, 255, 255));
        botonModificarMesa1.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonModificarMesa1.setForeground(new java.awt.Color(255, 255, 255));
        botonModificarMesa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarMesa1.setBorder(null);
        botonModificarMesa1.setPreferredSize(new java.awt.Dimension(200, 100));
        botonModificarMesa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarMesa1ActionPerformed(evt);
            }
        });

        botonCancelarModificarMozo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarModificarMozo1.setBorder(null);
        botonCancelarModificarMozo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarModificarMozo1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarMesa1Layout = new javax.swing.GroupLayout(panelModificarMesa1);
        panelModificarMesa1.setLayout(panelModificarMesa1Layout);
        panelModificarMesa1Layout.setHorizontalGroup(
            panelModificarMesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarMesa1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelModificarMesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarMesa1Layout.createSequentialGroup()
                        .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelModificarMesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelModificarMesa1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(panelModificarMesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelCodigoConsumo15, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelModificarMesa1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelModificarMesa1Layout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(botonModificarMesa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(93, 93, 93))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarMesa1Layout.createSequentialGroup()
                        .addComponent(botonCancelarModificarMozo1)
                        .addGap(355, 355, 355))))
        );
        panelModificarMesa1Layout.setVerticalGroup(
            panelModificarMesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarMesa1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelModificarMesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModificarMesa1Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo15)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificarMesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53)
                .addComponent(botonCancelarModificarMozo1)
                .addGap(68, 68, 68))
        );

        getContentPane().add(panelModificarMesa1);
        panelModificarMesa1.setBounds(200, 0, 1000, 700);

        panelModificarMozo1.setBackground(new java.awt.Color(255, 255, 255));
        panelModificarMozo1.setPreferredSize(new java.awt.Dimension(1000, 700));

        tablaModificarMozo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "DNI", "Nombre", "Domicilio"
            }
        ));
        jScrollPane20.setViewportView(tablaModificarMozo);

        labelCodigoConsumo14.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo14.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo14.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo14.setText("Seleccione un mozo");

        jComboBox9.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox9.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox9.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un mozo" }));

        botonModificarMozo1.setBackground(new java.awt.Color(255, 255, 255));
        botonModificarMozo1.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonModificarMozo1.setForeground(new java.awt.Color(255, 255, 255));
        botonModificarMozo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarMozo1.setBorder(null);
        botonModificarMozo1.setPreferredSize(new java.awt.Dimension(200, 100));
        botonModificarMozo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarMozo1ActionPerformed(evt);
            }
        });

        botonCancelarModificarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarModificarMozo.setBorder(null);
        botonCancelarModificarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarModificarMozoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarMozo1Layout = new javax.swing.GroupLayout(panelModificarMozo1);
        panelModificarMozo1.setLayout(panelModificarMozo1Layout);
        panelModificarMozo1Layout.setHorizontalGroup(
            panelModificarMozo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarMozo1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelModificarMozo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelModificarMozo1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(panelModificarMozo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCodigoConsumo14, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelModificarMozo1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(botonModificarMozo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(118, 118, 118))
            .addGroup(panelModificarMozo1Layout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(botonCancelarModificarMozo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelModificarMozo1Layout.setVerticalGroup(
            panelModificarMozo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModificarMozo1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelModificarMozo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModificarMozo1Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo14)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificarMozo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(55, 55, 55)
                .addComponent(botonCancelarModificarMozo)
                .addGap(63, 63, 63))
        );

        getContentPane().add(panelModificarMozo1);
        panelModificarMozo1.setBounds(200, 0, 1000, 700);

        panelEliminarMesa.setBackground(new java.awt.Color(255, 255, 255));
        panelEliminarMesa.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelCodigoConsumo10.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo10.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo10.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo10.setText("Seleccione una mesa");

        tablaEliminarMesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        jScrollPane12.setViewportView(tablaEliminarMesa);

        jComboBox4.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox4.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox4.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));

        botonEliminarMesa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminar.jpg"))); // NOI18N
        botonEliminarMesa1.setBorder(null);
        botonEliminarMesa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarMesa1ActionPerformed(evt);
            }
        });

        botonCancelarEliminarMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarEliminarMesa.setBorder(null);
        botonCancelarEliminarMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarEliminarMesaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEliminarMesaLayout = new javax.swing.GroupLayout(panelEliminarMesa);
        panelEliminarMesa.setLayout(panelEliminarMesaLayout);
        panelEliminarMesaLayout.setHorizontalGroup(
            panelEliminarMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarMesaLayout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelEliminarMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEliminarMesaLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelEliminarMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCodigoConsumo10, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelEliminarMesaLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelEliminarMesaLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(botonEliminarMesa1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEliminarMesaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonCancelarEliminarMesa)
                .addGap(368, 368, 368))
        );
        panelEliminarMesaLayout.setVerticalGroup(
            panelEliminarMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarMesaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEliminarMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelEliminarMesaLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo10)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonEliminarMesa1)))
                .addGap(56, 56, 56)
                .addComponent(botonCancelarEliminarMesa)
                .addGap(65, 65, 65))
        );

        getContentPane().add(panelEliminarMesa);
        panelEliminarMesa.setBounds(200, 0, 1000, 700);

        panelEliminarMozo.setBackground(new java.awt.Color(255, 255, 255));
        panelEliminarMozo.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelCodigoConsumo9.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo9.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo9.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo9.setText("Seleccione un mozo");

        tablaEliminarMozo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "DNI", "Nombre", "Domicilio"
            }
        ));
        jScrollPane9.setViewportView(tablaEliminarMozo);

        jComboBox3.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox3.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox3.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un mozo" }));

        botonEliminarMozo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminar.jpg"))); // NOI18N
        botonEliminarMozo1.setBorder(null);
        botonEliminarMozo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarMozo1ActionPerformed(evt);
            }
        });

        botonCancelarEliminarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarEliminarMozo.setBorder(null);
        botonCancelarEliminarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarEliminarMozoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEliminarMozoLayout = new javax.swing.GroupLayout(panelEliminarMozo);
        panelEliminarMozo.setLayout(panelEliminarMozoLayout);
        panelEliminarMozoLayout.setHorizontalGroup(
            panelEliminarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarMozoLayout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelEliminarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEliminarMozoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelEliminarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCodigoConsumo9, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelEliminarMozoLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(botonEliminarMozo1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEliminarMozoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonCancelarEliminarMozo)
                .addGap(368, 368, 368))
        );
        panelEliminarMozoLayout.setVerticalGroup(
            panelEliminarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarMozoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEliminarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelEliminarMozoLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo9)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonEliminarMozo1)))
                .addGap(56, 56, 56)
                .addComponent(botonCancelarEliminarMozo)
                .addGap(65, 65, 65))
        );

        getContentPane().add(panelEliminarMozo);
        panelEliminarMozo.setBounds(200, 0, 1000, 700);

        panelEliminarConsumo.setBackground(new java.awt.Color(255, 255, 255));
        panelEliminarConsumo.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelCodigoConsumo12.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigoConsumo12.setFont(new java.awt.Font("Caviar Dreams", 0, 30)); // NOI18N
        labelCodigoConsumo12.setForeground(new java.awt.Color(5, 88, 102));
        labelCodigoConsumo12.setText("Seleccione un consumo");

        tablaEliminarConsumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Fecha", "Hora", "Código mesa"
            }
        ));
        jScrollPane14.setViewportView(tablaEliminarConsumo);

        jComboBox6.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox6.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        jComboBox6.setForeground(new java.awt.Color(5, 88, 120));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un consumo" }));

        botonEliminarConsumo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminar.jpg"))); // NOI18N
        botonEliminarConsumo1.setBorder(null);
        botonEliminarConsumo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarConsumo1ActionPerformed(evt);
            }
        });

        botonCancelarEliminarConsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarEliminarConsumo.setBorder(null);
        botonCancelarEliminarConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarEliminarConsumoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEliminarConsumoLayout = new javax.swing.GroupLayout(panelEliminarConsumo);
        panelEliminarConsumo.setLayout(panelEliminarConsumoLayout);
        panelEliminarConsumoLayout.setHorizontalGroup(
            panelEliminarConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarConsumoLayout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelEliminarConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEliminarConsumoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelEliminarConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCodigoConsumo12)
                            .addGroup(panelEliminarConsumoLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelEliminarConsumoLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(botonEliminarConsumo1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEliminarConsumoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonCancelarEliminarConsumo)
                .addGap(368, 368, 368))
        );
        panelEliminarConsumoLayout.setVerticalGroup(
            panelEliminarConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarConsumoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEliminarConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelEliminarConsumoLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(labelCodigoConsumo12)
                        .addGap(12, 12, 12)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonEliminarConsumo1)))
                .addGap(56, 56, 56)
                .addComponent(botonCancelarEliminarConsumo)
                .addGap(65, 65, 65))
        );

        getContentPane().add(panelEliminarConsumo);
        panelEliminarConsumo.setBounds(200, 0, 1000, 700);

        panelMozosLibres.setBackground(new java.awt.Color(255, 255, 255));
        panelMozosLibres.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelMozosLibres.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosLibres.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosLibres.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosLibres.setText("Mozos libres");

        tablaMozosLibres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Código", "Nombre"
            }
        ));
        tablaCantMesasPorMozoss2.setViewportView(tablaMozosLibres);

        botonVolverMozosLibres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverMozosLibres.setBorder(null);
        botonVolverMozosLibres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverMozosLibresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMozosLibresLayout = new javax.swing.GroupLayout(panelMozosLibres);
        panelMozosLibres.setLayout(panelMozosLibresLayout);
        panelMozosLibresLayout.setHorizontalGroup(
            panelMozosLibresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMozosLibresLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMozosLibresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMozosLibresLayout.createSequentialGroup()
                        .addComponent(tablaCantMesasPorMozoss2, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(217, 217, 217))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMozosLibresLayout.createSequentialGroup()
                        .addComponent(botonVolverMozosLibres)
                        .addGap(359, 359, 359))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMozosLibresLayout.createSequentialGroup()
                        .addComponent(labelMozosLibres, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(277, 277, 277))))
        );
        panelMozosLibresLayout.setVerticalGroup(
            panelMozosLibresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMozosLibresLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(labelMozosLibres, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tablaCantMesasPorMozoss2, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(botonVolverMozosLibres)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelMozosLibres);
        panelMozosLibres.setBounds(200, 0, 1000, 700);

        panelMesasAsignadas.setBackground(new java.awt.Color(255, 255, 255));
        panelMesasAsignadas.setPreferredSize(new java.awt.Dimension(1000, 700));

        tablaCantMesasPorMozos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre", "Cantidad de mesas"
            }
        ));
        tablaCantMesasPorMozoss.setViewportView(tablaCantMesasPorMozos);

        labelMozosTitulo1.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo1.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo1.setText("Cantidad de mesas por mozo");

        botonVolverMesasAsignadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverMesasAsignadas.setBorder(null);
        botonVolverMesasAsignadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverMesasAsignadasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMesasAsignadasLayout = new javax.swing.GroupLayout(panelMesasAsignadas);
        panelMesasAsignadas.setLayout(panelMesasAsignadasLayout);
        panelMesasAsignadasLayout.setHorizontalGroup(
            panelMesasAsignadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMesasAsignadasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMesasAsignadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMesasAsignadasLayout.createSequentialGroup()
                        .addComponent(botonVolverMesasAsignadas)
                        .addGap(381, 381, 381))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMesasAsignadasLayout.createSequentialGroup()
                        .addComponent(labelMozosTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133))))
            .addGroup(panelMesasAsignadasLayout.createSequentialGroup()
                .addGap(270, 270, 270)
                .addComponent(tablaCantMesasPorMozoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelMesasAsignadasLayout.setVerticalGroup(
            panelMesasAsignadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMesasAsignadasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelMozosTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablaCantMesasPorMozoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonVolverMesasAsignadas)
                .addGap(43, 43, 43))
        );

        getContentPane().add(panelMesasAsignadas);
        panelMesasAsignadas.setBounds(200, 0, 1000, 700);

        panelMesasPorMozo.setBackground(new java.awt.Color(255, 255, 255));
        panelMesasPorMozo.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelMesasPorMozo.setLayout(null);

        labelMozosTitulo2.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo2.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo2.setText("Mesas por mozo");
        panelMesasPorMozo.add(labelMozosTitulo2);
        labelMozosTitulo2.setBounds(283, 39, 422, 90);

        tablaMesasPorMozos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        tablaCantMesasPorMozoss1.setViewportView(tablaMesasPorMozos);

        panelMesasPorMozo.add(tablaCantMesasPorMozoss1);
        tablaCantMesasPorMozoss1.setBounds(230, 240, 551, 330);

        jLabel1.setFont(new java.awt.Font("Caviar Dreams", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(5, 88, 102));
        jLabel1.setText("Seleccione un mozo:");
        panelMesasPorMozo.add(jLabel1);
        jLabel1.setBounds(330, 130, 335, 43);

        comboMesasPorMozo.setBackground(new java.awt.Color(255, 255, 255));
        comboMesasPorMozo.setFont(new java.awt.Font("Caviar Dreams", 1, 14)); // NOI18N
        comboMesasPorMozo.setForeground(new java.awt.Color(5, 88, 102));
        comboMesasPorMozo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un mozo" }));
        panelMesasPorMozo.add(comboMesasPorMozo);
        comboMesasPorMozo.setBounds(280, 180, 210, 40);

        botonVolverMesasPorMozos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverMesasPorMozos.setBorder(null);
        botonVolverMesasPorMozos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverMesasPorMozosActionPerformed(evt);
            }
        });
        panelMesasPorMozo.add(botonVolverMesasPorMozos);
        botonVolverMesasPorMozos.setBounds(375, 600, 250, 60);

        botonBuscarMesasPorMozos1.setBackground(new java.awt.Color(5, 88, 102));
        botonBuscarMesasPorMozos1.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        botonBuscarMesasPorMozos1.setForeground(new java.awt.Color(255, 255, 255));
        botonBuscarMesasPorMozos1.setText("Buscar");
        botonBuscarMesasPorMozos1.setBorder(null);
        botonBuscarMesasPorMozos1.setPreferredSize(new java.awt.Dimension(200, 50));
        botonBuscarMesasPorMozos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarMesasPorMozos1ActionPerformed(evt);
            }
        });
        panelMesasPorMozo.add(botonBuscarMesasPorMozos1);
        botonBuscarMesasPorMozos1.setBounds(510, 180, 200, 40);

        getContentPane().add(panelMesasPorMozo);
        panelMesasPorMozo.setBounds(200, 0, 1000, 700);

        panelPlatosPorMesa.setBackground(new java.awt.Color(255, 255, 255));
        panelPlatosPorMesa.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelMozosTitulo3.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo3.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo3.setText("Platos por mesa");

        tablaPlatosPorMesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción"
            }
        ));
        tablaCantMesasPorMozoss3.setViewportView(tablaPlatosPorMesa);

        jLabel2.setFont(new java.awt.Font("Caviar Dreams", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(5, 88, 102));
        jLabel2.setText("Seleccione una mesa:");

        comboPlatosPorMesa.setBackground(new java.awt.Color(255, 255, 255));
        comboPlatosPorMesa.setFont(new java.awt.Font("Caviar Dreams", 1, 14)); // NOI18N
        comboPlatosPorMesa.setForeground(new java.awt.Color(5, 88, 102));
        comboPlatosPorMesa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));
        comboPlatosPorMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPlatosPorMesaActionPerformed(evt);
            }
        });

        botonVolverPlatosPorMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverPlatosPorMesa.setBorder(null);
        botonVolverPlatosPorMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverPlatosPorMesaActionPerformed(evt);
            }
        });

        buscarPlatosPorMesa.setBackground(new java.awt.Color(5, 88, 102));
        buscarPlatosPorMesa.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        buscarPlatosPorMesa.setForeground(new java.awt.Color(255, 255, 255));
        buscarPlatosPorMesa.setText("Buscar");
        buscarPlatosPorMesa.setBorder(null);
        buscarPlatosPorMesa.setPreferredSize(new java.awt.Dimension(200, 50));
        buscarPlatosPorMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarPlatosPorMesaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPlatosPorMesaLayout = new javax.swing.GroupLayout(panelPlatosPorMesa);
        panelPlatosPorMesa.setLayout(panelPlatosPorMesaLayout);
        panelPlatosPorMesaLayout.setHorizontalGroup(
            panelPlatosPorMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                .addGroup(panelPlatosPorMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                        .addGap(283, 283, 283)
                        .addComponent(labelMozosTitulo3, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                        .addGap(375, 375, 375)
                        .addComponent(botonVolverPlatosPorMesa))
                    .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                        .addGap(305, 305, 305)
                        .addGroup(panelPlatosPorMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                                .addComponent(comboPlatosPorMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(buscarPlatosPorMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel2))))
                    .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                        .addGap(220, 220, 220)
                        .addComponent(tablaCantMesasPorMozoss3, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPlatosPorMesaLayout.setVerticalGroup(
            panelPlatosPorMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlatosPorMesaLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(labelMozosTitulo3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(panelPlatosPorMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buscarPlatosPorMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboPlatosPorMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablaCantMesasPorMozoss3, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(botonVolverPlatosPorMesa)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelPlatosPorMesa);
        panelPlatosPorMesa.setBounds(200, 0, 1000, 700);

        panelPlatosNuncaConsumidos.setBackground(new java.awt.Color(255, 255, 255));
        panelPlatosNuncaConsumidos.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelMozosTitulo7.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo7.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo7.setText("Platos nunca consumidos");

        tablaPlatosNunca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre", "Descripción"
            }
        ));
        jScrollPane18.setViewportView(tablaPlatosNunca);

        botonVolverPlatosNunca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverPlatosNunca.setMaximumSize(new java.awt.Dimension(250, 60));
        botonVolverPlatosNunca.setMinimumSize(new java.awt.Dimension(250, 60));
        botonVolverPlatosNunca.setPreferredSize(new java.awt.Dimension(250, 60));
        botonVolverPlatosNunca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverPlatosNuncaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPlatosNuncaConsumidosLayout = new javax.swing.GroupLayout(panelPlatosNuncaConsumidos);
        panelPlatosNuncaConsumidos.setLayout(panelPlatosNuncaConsumidosLayout);
        panelPlatosNuncaConsumidosLayout.setHorizontalGroup(
            panelPlatosNuncaConsumidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlatosNuncaConsumidosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelPlatosNuncaConsumidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlatosNuncaConsumidosLayout.createSequentialGroup()
                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(267, 267, 267))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlatosNuncaConsumidosLayout.createSequentialGroup()
                        .addComponent(labelMozosTitulo7, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(187, 187, 187))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlatosNuncaConsumidosLayout.createSequentialGroup()
                        .addComponent(botonVolverPlatosNunca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(363, 363, 363))))
        );
        panelPlatosNuncaConsumidosLayout.setVerticalGroup(
            panelPlatosNuncaConsumidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlatosNuncaConsumidosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelMozosTitulo7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(botonVolverPlatosNunca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        getContentPane().add(panelPlatosNuncaConsumidos);
        panelPlatosNuncaConsumidos.setBounds(200, 0, 1000, 700);

        panelTablaMozos.setBackground(new java.awt.Color(255, 255, 255));
        panelTablaMozos.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelTablaMozos.setMinimumSize(new java.awt.Dimension(1000, 700));
        panelTablaMozos.setName(""); // NOI18N
        panelTablaMozos.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelTablaMozos.setLayout(null);

        tablaMozos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "DNI", "Nombre", "Domicilio"
            }
        ));
        tablaMozos.setShowGrid(false);
        tablaMozosScroll.setViewportView(tablaMozos);

        panelTablaMozos.add(tablaMozosScroll);
        tablaMozosScroll.setBounds(200, 160, 600, 400);

        labelMozosTitulo.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo.setText("Mozos");
        panelTablaMozos.add(labelMozosTitulo);
        labelMozosTitulo.setBounds(420, 50, 160, 90);

        botonVolverMozos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverMozos.setToolTipText("");
        botonVolverMozos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverMozosActionPerformed(evt);
            }
        });
        panelTablaMozos.add(botonVolverMozos);
        botonVolverMozos.setBounds(380, 590, 256, 66);

        getContentPane().add(panelTablaMozos);
        panelTablaMozos.setBounds(200, 0, 1000, 700);

        panelPlatosMasConsumidos.setBackground(new java.awt.Color(255, 255, 255));
        panelPlatosMasConsumidos.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelPlatosMasConsumidos.setLayout(null);

        labelMozosTitulo6.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo6.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo6.setText("Platos más consumidos");
        panelPlatosMasConsumidos.add(labelMozosTitulo6);
        labelMozosTitulo6.setBounds(204, 68, 577, 90);

        labelEntrada.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelEntrada.setForeground(new java.awt.Color(5, 88, 102));
        labelEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelEntrada.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelPlatosMasConsumidos.add(labelEntrada);
        labelEntrada.setBounds(427, 214, 315, 56);

        labelPrincipal.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelPrincipal.setForeground(new java.awt.Color(5, 88, 102));
        labelPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPrincipal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelPlatosMasConsumidos.add(labelPrincipal);
        labelPrincipal.setBounds(427, 326, 315, 56);

        labelPostre.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelPostre.setForeground(new java.awt.Color(5, 88, 102));
        labelPostre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPostre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelPlatosMasConsumidos.add(labelPostre);
        labelPostre.setBounds(427, 438, 315, 56);

        botonVolverPlatosConsumidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverPlatosConsumidos.setBorder(null);
        botonVolverPlatosConsumidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverPlatosConsumidosActionPerformed(evt);
            }
        });
        panelPlatosMasConsumidos.add(botonVolverPlatosConsumidos);
        botonVolverPlatosConsumidos.setBounds(370, 560, 250, 60);

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));
        jPanel1.setLayout(null);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/panelPlatosMasConsumidos.jpg"))); // NOI18N
        jLabel7.setText("jLabel7");
        jLabel7.setPreferredSize(new java.awt.Dimension(1000, 700));
        jPanel1.add(jLabel7);
        jLabel7.setBounds(0, 0, 1000, 700);

        panelPlatosMasConsumidos.add(jPanel1);
        jPanel1.setBounds(0, 0, 1000, 700);

        getContentPane().add(panelPlatosMasConsumidos);
        panelPlatosMasConsumidos.setBounds(200, 0, 1000, 700);

        panelTablaConsumos.setBackground(new java.awt.Color(255, 255, 255));
        panelTablaConsumos.setPreferredSize(new java.awt.Dimension(1000, 700));

        tablaConsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Fecha", "Hora", "Código mesa"
            }
        ));
        tablaConsumos.setShowGrid(false);
        tablaConsumosScroll.setViewportView(tablaConsumos);

        labelConsumosTitulo.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelConsumosTitulo.setForeground(new java.awt.Color(5, 88, 102));
        labelConsumosTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelConsumosTitulo.setText("Consumos");

        botonVolverConsumos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverConsumos.setToolTipText("");
        botonVolverConsumos.setMaximumSize(new java.awt.Dimension(250, 60));
        botonVolverConsumos.setMinimumSize(new java.awt.Dimension(250, 60));
        botonVolverConsumos.setPreferredSize(new java.awt.Dimension(250, 60));
        botonVolverConsumos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverConsumosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTablaConsumosLayout = new javax.swing.GroupLayout(panelTablaConsumos);
        panelTablaConsumos.setLayout(panelTablaConsumosLayout);
        panelTablaConsumosLayout.setHorizontalGroup(
            panelTablaConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaConsumosLayout.createSequentialGroup()
                .addGroup(panelTablaConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTablaConsumosLayout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(botonVolverConsumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTablaConsumosLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(tablaConsumosScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTablaConsumosLayout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(labelConsumosTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTablaConsumosLayout.setVerticalGroup(
            panelTablaConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaConsumosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelConsumosTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablaConsumosScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(botonVolverConsumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );

        getContentPane().add(panelTablaConsumos);
        panelTablaConsumos.setBounds(200, 0, 1000, 700);

        panelCostosPlatos.setBackground(new java.awt.Color(255, 255, 255));
        panelCostosPlatos.setPreferredSize(new java.awt.Dimension(1000, 700));
        panelCostosPlatos.setLayout(null);

        labelMozosTitulo8.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo8.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo8.setText("Análisis platos principales");
        panelCostosPlatos.add(labelMozosTitulo8);
        labelMozosTitulo8.setBounds(170, 60, 630, 90);

        labelMaximo.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelMaximo.setForeground(new java.awt.Color(5, 88, 102));
        labelMaximo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMaximo.setText("0");
        labelMaximo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelMaximo.setPreferredSize(new java.awt.Dimension(9, 20));
        panelCostosPlatos.add(labelMaximo);
        labelMaximo.setBounds(429, 217, 310, 50);

        labelMinimo.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelMinimo.setForeground(new java.awt.Color(5, 88, 102));
        labelMinimo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMinimo.setText("0");
        labelMinimo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelMinimo.setPreferredSize(new java.awt.Dimension(9, 20));
        panelCostosPlatos.add(labelMinimo);
        labelMinimo.setBounds(429, 329, 310, 50);

        labelPromedio.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelPromedio.setForeground(new java.awt.Color(5, 88, 102));
        labelPromedio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPromedio.setText("0");
        labelPromedio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelPromedio.setPreferredSize(new java.awt.Dimension(9, 20));
        panelCostosPlatos.add(labelPromedio);
        labelPromedio.setBounds(429, 440, 310, 50);

        botonVolverAnalisis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverAnalisis.setBorder(null);
        botonVolverAnalisis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverAnalisisActionPerformed(evt);
            }
        });
        panelCostosPlatos.add(botonVolverAnalisis);
        botonVolverAnalisis.setBounds(380, 560, 250, 60);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/panelAnalisis.jpg"))); // NOI18N
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelCostosPlatos.add(jPanel2);
        jPanel2.setBounds(0, 0, 1000, 700);

        getContentPane().add(panelCostosPlatos);
        panelCostosPlatos.setBounds(200, 0, 1000, 700);

        panelAsignarMozo.setBackground(new java.awt.Color(255, 255, 255));
        panelAsignarMozo.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelMozosTitulo4.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMozosTitulo4.setForeground(new java.awt.Color(5, 88, 102));
        labelMozosTitulo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMozosTitulo4.setText("Asignar mozo a una mesa");

        tablaMozos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "DNI", "Nombre", "Title 4"
            }
        ));
        jScrollPane15.setViewportView(tablaMozos2);

        jLabel3.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(5, 88, 102));
        jLabel3.setText("Seleccione un mozo");

        comboMozo1.setBackground(new java.awt.Color(255, 255, 255));
        comboMozo1.setFont(new java.awt.Font("Caviar Dreams", 1, 14)); // NOI18N
        comboMozo1.setForeground(new java.awt.Color(5, 88, 102));
        comboMozo1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija un mozo" }));

        tablaMesas3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        jScrollPane16.setViewportView(tablaMesas3);

        jLabel4.setFont(new java.awt.Font("Caviar Dreams", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(5, 88, 102));
        jLabel4.setText("Seleccione una mesa");

        comboMesa1.setBackground(new java.awt.Color(255, 255, 255));
        comboMesa1.setFont(new java.awt.Font("Caviar Dreams", 1, 14)); // NOI18N
        comboMesa1.setForeground(new java.awt.Color(5, 88, 102));
        comboMesa1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una mesa" }));

        botonCancelarAsignarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCancelar.jpg"))); // NOI18N
        botonCancelarAsignarMozo.setBorder(null);
        botonCancelarAsignarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarAsignarMozoActionPerformed(evt);
            }
        });

        botonModificarAsignarMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificar.jpg"))); // NOI18N
        botonModificarAsignarMozo.setBorder(null);
        botonModificarAsignarMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarAsignarMozoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAsignarMozoLayout = new javax.swing.GroupLayout(panelAsignarMozo);
        panelAsignarMozo.setLayout(panelAsignarMozoLayout);
        panelAsignarMozoLayout.setHorizontalGroup(
            panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAsignarMozoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelAsignarMozoLayout.createSequentialGroup()
                        .addComponent(botonCancelarAsignarMozo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonModificarAsignarMozo))
                    .addGroup(panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAsignarMozoLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelMozosTitulo4, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13))
                        .addGroup(panelAsignarMozoLayout.createSequentialGroup()
                            .addGroup(panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboMozo1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboMesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(180, 180, 180))
        );
        panelAsignarMozoLayout.setVerticalGroup(
            panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAsignarMozoLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(labelMozosTitulo4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAsignarMozoLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(comboMozo1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAsignarMozoLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(comboMesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAsignarMozoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonCancelarAsignarMozo)
                    .addComponent(botonModificarAsignarMozo))
                .addGap(47, 47, 47))
        );

        getContentPane().add(panelAsignarMozo);
        panelAsignarMozo.setBounds(200, 0, 1000, 700);

        panelConsumos.setBackground(new java.awt.Color(255, 255, 255));
        panelConsumos.setPreferredSize(new java.awt.Dimension(1000, 700));

        botonInsertarConsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertarConsumo.jpg"))); // NOI18N
        botonInsertarConsumo.setAlignmentY(0.0F);
        botonInsertarConsumo.setBorder(null);
        botonInsertarConsumo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonInsertarConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarConsumoActionPerformed(evt);
            }
        });

        botonEliminarConsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminarConsumo.jpg"))); // NOI18N
        botonEliminarConsumo.setBorder(null);
        botonEliminarConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarConsumoActionPerformed(evt);
            }
        });

        botonModificarConsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificarConsumo.jpg"))); // NOI18N
        botonModificarConsumo.setBorder(null);
        botonModificarConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarConsumoActionPerformed(evt);
            }
        });

        botonVerConsumos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVerConsumos.jpg"))); // NOI18N
        botonVerConsumos.setBorder(null);
        botonVerConsumos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerConsumosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelConsumosLayout = new javax.swing.GroupLayout(panelConsumos);
        panelConsumos.setLayout(panelConsumosLayout);
        panelConsumosLayout.setHorizontalGroup(
            panelConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsumosLayout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addGroup(panelConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonEliminarConsumo)
                    .addComponent(botonInsertarConsumo))
                .addGap(148, 148, 148)
                .addGroup(panelConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonModificarConsumo)
                    .addComponent(botonVerConsumos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelConsumosLayout.setVerticalGroup(
            panelConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConsumosLayout.createSequentialGroup()
                .addGap(192, 192, 192)
                .addGroup(panelConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonInsertarConsumo)
                    .addComponent(botonModificarConsumo))
                .addGap(106, 106, 106)
                .addGroup(panelConsumosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonEliminarConsumo)
                    .addComponent(botonVerConsumos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelConsumos);
        panelConsumos.setBounds(200, 0, 1000, 700);

        panelMesas.setBackground(new java.awt.Color(255, 255, 255));
        panelMesas.setPreferredSize(new java.awt.Dimension(1000, 700));

        botonModificarMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificarMesa.jpg"))); // NOI18N
        botonModificarMesa.setBorder(null);
        botonModificarMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarMesaActionPerformed(evt);
            }
        });

        botonVerMesas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVerMesas.jpg"))); // NOI18N
        botonVerMesas.setBorder(null);
        botonVerMesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerMesasActionPerformed(evt);
            }
        });

        botonEliminarMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminarMesa.jpg"))); // NOI18N
        botonEliminarMesa.setAlignmentY(0.0F);
        botonEliminarMesa.setBorder(null);
        botonEliminarMesa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEliminarMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarMesaActionPerformed(evt);
            }
        });

        botonInsertarMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertarMesa.jpg"))); // NOI18N
        botonInsertarMesa.setBorder(null);
        botonInsertarMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarMesaActionPerformed(evt);
            }
        });

        botonMesaPorMozo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonMesasPorMozo.jpg"))); // NOI18N
        botonMesaPorMozo.setBorder(null);
        botonMesaPorMozo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMesaPorMozoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMesasLayout = new javax.swing.GroupLayout(panelMesas);
        panelMesas.setLayout(panelMesasLayout);
        panelMesasLayout.setHorizontalGroup(
            panelMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMesasLayout.createSequentialGroup()
                .addGap(388, 388, 388)
                .addComponent(botonMesaPorMozo)
                .addContainerGap(412, Short.MAX_VALUE))
            .addGroup(panelMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMesasLayout.createSequentialGroup()
                    .addGap(238, 238, 238)
                    .addGroup(panelMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMesasLayout.createSequentialGroup()
                            .addComponent(botonEliminarMesa)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonVerMesas))
                        .addGroup(panelMesasLayout.createSequentialGroup()
                            .addComponent(botonInsertarMesa)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonModificarMesa)))
                    .addGap(238, 238, 238)))
        );
        panelMesasLayout.setVerticalGroup(
            panelMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMesasLayout.createSequentialGroup()
                .addContainerGap(457, Short.MAX_VALUE)
                .addComponent(botonMesaPorMozo)
                .addGap(143, 143, 143))
            .addGroup(panelMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMesasLayout.createSequentialGroup()
                    .addGap(137, 137, 137)
                    .addGroup(panelMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelMesasLayout.createSequentialGroup()
                            .addGroup(panelMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(botonInsertarMesa)
                                .addComponent(botonModificarMesa))
                            .addGap(69, 69, 69)
                            .addComponent(botonEliminarMesa))
                        .addComponent(botonVerMesas))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(panelMesas);
        panelMesas.setBounds(200, 0, 1000, 700);

        panelPlatos.setBackground(new java.awt.Color(255, 255, 255));
        panelPlatos.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelPlatos.setPreferredSize(new java.awt.Dimension(1000, 700));

        botonModificarPlato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonModificarPlato.jpg"))); // NOI18N
        botonModificarPlato.setBorder(null);
        botonModificarPlato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarPlatoActionPerformed(evt);
            }
        });

        botonVerPlatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVerPlatos.jpg"))); // NOI18N
        botonVerPlatos.setBorder(null);
        botonVerPlatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerPlatosActionPerformed(evt);
            }
        });

        botonEliminarPlato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonEliminarPlato.jpg"))); // NOI18N
        botonEliminarPlato.setAlignmentY(0.0F);
        botonEliminarPlato.setBorder(null);
        botonEliminarPlato.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEliminarPlato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarPlatoActionPerformed(evt);
            }
        });

        botonInsertarPlato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInsertarPlato.jpg"))); // NOI18N
        botonInsertarPlato.setBorder(null);
        botonInsertarPlato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarPlatoActionPerformed(evt);
            }
        });

        botonPlatosNuncaCons.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonPlatosNuncaConsumidos.jpg"))); // NOI18N
        botonPlatosNuncaCons.setBorder(null);
        botonPlatosNuncaCons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlatosNuncaConsActionPerformed(evt);
            }
        });

        botonPlatoMasConsumido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonPlatoMasConsumidos.jpg"))); // NOI18N
        botonPlatoMasConsumido.setBorder(null);
        botonPlatoMasConsumido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlatoMasConsumidoActionPerformed(evt);
            }
        });

        botonCantsPlatosPorMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCantsPlatosPorMesa.jpg"))); // NOI18N
        botonCantsPlatosPorMesa.setBorder(null);
        botonCantsPlatosPorMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCantsPlatosPorMesaActionPerformed(evt);
            }
        });

        botonPlatosPorFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonPlatosPorFecha.jpg"))); // NOI18N
        botonPlatosPorFecha.setBorder(null);
        botonPlatosPorFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlatosPorFechaActionPerformed(evt);
            }
        });

        botonPlatosPorMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonPlatosPorMesa.jpg"))); // NOI18N
        botonPlatosPorMesa.setBorder(null);
        botonPlatosPorMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlatosPorMesaActionPerformed(evt);
            }
        });

        botonCostosPlatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonCostosPlatos.jpg"))); // NOI18N
        botonCostosPlatos.setBorder(null);
        botonCostosPlatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCostosPlatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPlatosLayout = new javax.swing.GroupLayout(panelPlatos);
        panelPlatos.setLayout(panelPlatosLayout);
        panelPlatosLayout.setHorizontalGroup(
            panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlatosLayout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonInsertarPlato)
                    .addComponent(botonEliminarPlato)
                    .addComponent(botonPlatosNuncaCons))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonPlatosPorMesa)
                    .addGroup(panelPlatosLayout.createSequentialGroup()
                        .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonPlatoMasConsumido)
                            .addComponent(botonVerPlatos)
                            .addComponent(botonModificarPlato))
                        .addGap(84, 84, 84)
                        .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(botonCantsPlatosPorMesa)
                                .addComponent(botonPlatosPorFecha, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(botonCostosPlatos, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(109, 109, 109))
        );
        panelPlatosLayout.setVerticalGroup(
            panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlatosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonInsertarPlato)
                    .addComponent(botonModificarPlato)
                    .addComponent(botonCantsPlatosPorMesa))
                .addGap(42, 42, 42)
                .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonEliminarPlato)
                    .addComponent(botonVerPlatos)
                    .addComponent(botonPlatosPorFecha))
                .addGap(42, 42, 42)
                .addGroup(panelPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonPlatosNuncaCons)
                    .addComponent(botonPlatoMasConsumido)
                    .addComponent(botonCostosPlatos))
                .addGap(44, 44, 44)
                .addComponent(botonPlatosPorMesa)
                .addGap(75, 75, 75))
        );

        getContentPane().add(panelPlatos);
        panelPlatos.setBounds(200, 0, 1000, 700);

        panelBanner.setBackground(new java.awt.Color(22, 181, 161));
        panelBanner.setAlignmentX(0.0F);
        panelBanner.setAlignmentY(0.0F);
        panelBanner.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelBanner.setPreferredSize(new java.awt.Dimension(200, 700));

        titulo.setBackground(new java.awt.Color(22, 181, 161));
        titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/titulo.jpg"))); // NOI18N
        titulo.setAlignmentY(0.0F);
        titulo.setBorder(null);
        titulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tituloActionPerformed(evt);
            }
        });

        botonInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonInicio.jpg"))); // NOI18N
        botonInicio.setAlignmentY(0.0F);
        botonInicio.setBorder(null);
        botonInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInicioActionPerformed(evt);
            }
        });

        botonMozos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonMozos.jpg"))); // NOI18N
        botonMozos.setAlignmentY(0.0F);
        botonMozos.setBorder(null);
        botonMozos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMozosActionPerformed(evt);
            }
        });

        botonPlatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonPlatos.jpg"))); // NOI18N
        botonPlatos.setAlignmentY(0.0F);
        botonPlatos.setBorder(null);
        botonPlatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlatosActionPerformed(evt);
            }
        });

        botonMesas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonMesas.jpg"))); // NOI18N
        botonMesas.setAlignmentY(0.0F);
        botonMesas.setBorder(null);
        botonMesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMesasActionPerformed(evt);
            }
        });

        botonConsumos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonConsumos.jpg"))); // NOI18N
        botonConsumos.setAlignmentY(0.0F);
        botonConsumos.setBorder(null);
        botonConsumos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConsumosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBannerLayout = new javax.swing.GroupLayout(panelBanner);
        panelBanner.setLayout(panelBannerLayout);
        panelBannerLayout.setHorizontalGroup(
            panelBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBannerLayout.createSequentialGroup()
                .addGroup(panelBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titulo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonInicio)
                    .addComponent(botonMozos)
                    .addComponent(botonPlatos)
                    .addComponent(botonMesas)
                    .addComponent(botonConsumos))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelBannerLayout.setVerticalGroup(
            panelBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBannerLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonMozos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonPlatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonMesas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonConsumos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelBanner);
        panelBanner.setBounds(0, 0, 200, 700);

        panelTablaPlatos.setBackground(new java.awt.Color(255, 255, 255));
        panelTablaPlatos.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelPlatosTitulo.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelPlatosTitulo.setForeground(new java.awt.Color(5, 88, 102));
        labelPlatosTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPlatosTitulo.setText("Platos");

        tablaPlatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Descripción", "Tipo", "Costo", "Venta", "Promoción"
            }
        ));
        tablaPlatos.setShowGrid(false);
        tablaPlatosScroll.setViewportView(tablaPlatos);

        botonVolverPlatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverPlatos.setToolTipText("");
        botonVolverPlatos.setPreferredSize(new java.awt.Dimension(250, 60));
        botonVolverPlatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverPlatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTablaPlatosLayout = new javax.swing.GroupLayout(panelTablaPlatos);
        panelTablaPlatos.setLayout(panelTablaPlatosLayout);
        panelTablaPlatosLayout.setHorizontalGroup(
            panelTablaPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaPlatosLayout.createSequentialGroup()
                .addGroup(panelTablaPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTablaPlatosLayout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(botonVolverPlatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTablaPlatosLayout.createSequentialGroup()
                        .addGap(368, 368, 368)
                        .addComponent(labelPlatosTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTablaPlatosLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(tablaPlatosScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(155, Short.MAX_VALUE))
        );
        panelTablaPlatosLayout.setVerticalGroup(
            panelTablaPlatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaPlatosLayout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(labelPlatosTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tablaPlatosScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(botonVolverPlatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );

        getContentPane().add(panelTablaPlatos);
        panelTablaPlatos.setBounds(200, 0, 1000, 700);

        panelTablaMesas.setBackground(new java.awt.Color(255, 255, 255));
        panelTablaMesas.setPreferredSize(new java.awt.Dimension(1000, 700));

        tablaMesas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Sector", "Atiende mozo nro"
            }
        ));
        tablaMesas.setShowGrid(false);
        tablaMesasScroll.setViewportView(tablaMesas);

        labelMesasTitulo.setFont(new java.awt.Font("Caviar Dreams", 1, 48)); // NOI18N
        labelMesasTitulo.setForeground(new java.awt.Color(5, 88, 102));
        labelMesasTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMesasTitulo.setText("Mesas");

        botonVolverMesas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/botonVolver.jpg"))); // NOI18N
        botonVolverMesas.setToolTipText("");
        botonVolverMesas.setMaximumSize(new java.awt.Dimension(250, 60));
        botonVolverMesas.setMinimumSize(new java.awt.Dimension(250, 60));
        botonVolverMesas.setPreferredSize(new java.awt.Dimension(250, 60));
        botonVolverMesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverMesasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTablaMesasLayout = new javax.swing.GroupLayout(panelTablaMesas);
        panelTablaMesas.setLayout(panelTablaMesasLayout);
        panelTablaMesasLayout.setHorizontalGroup(
            panelTablaMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaMesasLayout.createSequentialGroup()
                .addGroup(panelTablaMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTablaMesasLayout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(botonVolverMesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTablaMesasLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(tablaMesasScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaMesasLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelMesasTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(419, 419, 419))
        );
        panelTablaMesasLayout.setVerticalGroup(
            panelTablaMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaMesasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelMesasTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablaMesasScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(botonVolverMesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );

        getContentPane().add(panelTablaMesas);
        panelTablaMesas.setBounds(200, 0, 1000, 700);

        panelInicio.setBackground(panelInicio.getBackground());
        panelInicio.setMaximumSize(new java.awt.Dimension(1000, 700));
        panelInicio.setPreferredSize(new java.awt.Dimension(1000, 700));

        panelInicioValores.setAlignmentX(50.0F);
        panelInicioValores.setAlignmentY(50.0F);
        panelInicioValores.setAutoscrolls(true);
        panelInicioValores.setOpaque(false);
        panelInicioValores.setPreferredSize(new java.awt.Dimension(50, 700));

        labelCantMozos.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelCantMozos.setForeground(new java.awt.Color(5, 88, 102));
        labelCantMozos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCantMozos.setText("0");
        labelCantMozos.setPreferredSize(new java.awt.Dimension(100, 50));

        labelCantMesas.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelCantMesas.setForeground(new java.awt.Color(5, 88, 102));
        labelCantMesas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCantMesas.setText("0");
        labelCantMesas.setPreferredSize(new java.awt.Dimension(100, 50));

        labelCantPlatosPr.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelCantPlatosPr.setForeground(new java.awt.Color(5, 88, 102));
        labelCantPlatosPr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCantPlatosPr.setText("0");
        labelCantPlatosPr.setPreferredSize(new java.awt.Dimension(100, 50));

        labelCantPlatosP.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelCantPlatosP.setForeground(new java.awt.Color(5, 88, 102));
        labelCantPlatosP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCantPlatosP.setText("0");
        labelCantPlatosP.setPreferredSize(new java.awt.Dimension(100, 50));

        labelCantEntradas.setFont(new java.awt.Font("Caviar Dreams", 1, 18)); // NOI18N
        labelCantEntradas.setForeground(new java.awt.Color(5, 88, 102));
        labelCantEntradas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCantEntradas.setText("0");
        labelCantEntradas.setPreferredSize(new java.awt.Dimension(100, 50));

        javax.swing.GroupLayout panelInicioValoresLayout = new javax.swing.GroupLayout(panelInicioValores);
        panelInicioValores.setLayout(panelInicioValoresLayout);
        panelInicioValoresLayout.setHorizontalGroup(
            panelInicioValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioValoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInicioValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCantMozos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCantMesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCantPlatosPr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCantPlatosP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCantEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        panelInicioValoresLayout.setVerticalGroup(
            panelInicioValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioValoresLayout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addComponent(labelCantMozos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(labelCantMesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(labelCantPlatosPr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(labelCantPlatosP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(labelCantEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        panelInicioFondo.setPreferredSize(new java.awt.Dimension(1000, 700));

        labelInicio.setBackground(new java.awt.Color(255, 255, 255));
        labelInicio.setForeground(new java.awt.Color(0, 0, 0));
        labelInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/panelInicio.jpg"))); // NOI18N
        labelInicio.setToolTipText("");
        labelInicio.setAlignmentY(0.0F);

        javax.swing.GroupLayout panelInicioFondoLayout = new javax.swing.GroupLayout(panelInicioFondo);
        panelInicioFondo.setLayout(panelInicioFondoLayout);
        panelInicioFondoLayout.setHorizontalGroup(
            panelInicioFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioFondoLayout.createSequentialGroup()
                .addComponent(labelInicio)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelInicioFondoLayout.setVerticalGroup(
            panelInicioFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioFondoLayout.createSequentialGroup()
                .addComponent(labelInicio)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelInicioLayout = new javax.swing.GroupLayout(panelInicio);
        panelInicio.setLayout(panelInicioLayout);
        panelInicioLayout.setHorizontalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInicioLayout.createSequentialGroup()
                .addContainerGap(590, Short.MAX_VALUE)
                .addComponent(panelInicioValores, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(290, 290, 290))
            .addGroup(panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelInicioLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelInicioFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelInicioLayout.setVerticalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInicioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelInicioValores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelInicioLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelInicioFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        getContentPane().add(panelInicio);
        panelInicio.setBounds(200, 0, 1000, 700);

        getAccessibleContext().setAccessibleName("frame");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tituloActionPerformed
        //DECORACIÓN
    }//GEN-LAST:event_tituloActionPerformed

    private void comboMozoMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMozoMesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboMozoMesaActionPerformed

    //BOTON ELIMINAR MOZO
    private void botonEliminarMozo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarMozo1ActionPerformed
        if(jComboBox3.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija un mozo");
        }
        else
        {
            int cod = Integer.parseInt(String.valueOf(jComboBox3.getSelectedItem()).trim());
            boolean temp1 = false;
            try {
                temp1 = mozoEnMesa(cod);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!temp1)
            {
                int ventana = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el mozo?", "Eliminar Mozo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                //0=si, 1=no
                if(ventana == 0) {
                    try {
                        eliminarMozo(cod);
                        JOptionPane.showMessageDialog(null, "Mozo eliminado con éxito");
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    try {
                        updateTablaMozos(); //Actualizo la tabla y la lista de selección                
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Este mozo ha sido asignado a una mesa, para eliminarlo tiene que estar libre");
            }  
            //Limpio el campo
            limpiezaEliminarMozo();
        }
    }//GEN-LAST:event_botonEliminarMozo1ActionPerformed
    
    //BOTON ELIMINAR MESA
    private void botonEliminarMesa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarMesa1ActionPerformed

        if(jComboBox4.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija una mesa");
        }
        else
        {
            int cod = Integer.parseInt(String.valueOf(jComboBox4.getSelectedItem()).trim());
            if(!(String.valueOf(cod)).equals("Sin mozo"))
            {
                JOptionPane.showMessageDialog(null, "Desasigne el mozo de esta mesa");
            }
            else
            {

                int ventana = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la mesa?", "Eliminar mesa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                //0=si, 1=no
                if(ventana == 0) {
                    try {
                        eliminarMesa(cod);
                        JOptionPane.showMessageDialog(null, "Mesa eliminada con éxito"); 
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    try {
                        updateTablaMesas(); //Actualizo la tabla y la lista de selección            
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Limpio el campo
                limpiezaEliminarMesa();
            }
        }
    }//GEN-LAST:event_botonEliminarMesa1ActionPerformed
    
    //BOTON ELIMINAR PLATO
    private void botonEliminarPlato1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarPlato1ActionPerformed
        if(jComboBox5.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija un plato"); 
        }
        else
        {
            int cod = Integer.parseInt(String.valueOf(jComboBox5.getSelectedItem()).trim());
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el plato?", "Eliminar plato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    eliminarSe_ConsumeP(cod);
                    eliminarPlatos(cod);
                    JOptionPane.showMessageDialog(null, "Plato eliminado con éxito"); 
                } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    updateTablaPlatos(); //Actualizo la tabla y la lista de selección            
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateRowHeight();
            }
            //Limpio el campo
            limpiezaEliminarPlato();
        }
    }//GEN-LAST:event_botonEliminarPlato1ActionPerformed

    //BOTON ELIMINAR CONSUMO
    private void botonEliminarConsumo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarConsumo1ActionPerformed
        if(jComboBox6.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija un consumo");
        }
        else
        {
            int cod = Integer.parseInt(String.valueOf(jComboBox6.getSelectedItem()).trim());
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el consumo?", "Eliminar consumo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    eliminarSe_ConsumeC(cod);
                    eliminarConsumo(cod);
                    JOptionPane.showMessageDialog(null, "Consumo eliminado con éxito");
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    updateTablaConsumos(); //Actualizo la tabla y la lista de selección            
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Limpio el campo
            limpiezaEliminarConsumo();
        }
    }//GEN-LAST:event_botonEliminarConsumo1ActionPerformed

    //BOTON IR A INSERTAR MOZO
    private void botonInsertarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarMozoActionPerformed
        visibilidadPaneles(panelInsertarMozo);
        limpiezaInsertarMozo();
    }//GEN-LAST:event_botonInsertarMozoActionPerformed

    //BOTON IR A ELIMINAR MOZO
    private void botonEliminarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarMozoActionPerformed
        boolean temp = false;
        try {
            temp = hayMozo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                 updateTablaMozos();
            } catch (SQLException ex) {
                 Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelEliminarMozo);
        }
        else
        {
            JOptionPane.showConfirmDialog(null,"No existen mozos en el sistema");
        }
    }//GEN-LAST:event_botonEliminarMozoActionPerformed

    //BOTON IR A ELIMINAR MESA
    private void botonEliminarMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarMesaActionPerformed
        boolean temp = false;
        try {
            temp = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaMesas();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelEliminarMesa);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
        }
    }//GEN-LAST:event_botonEliminarMesaActionPerformed

    //BOTON IR A INSERTAR MESA
    private void botonInsertarMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarMesaActionPerformed
        visibilidadPaneles(panelInsertarMesa);
        try {
            updateTablaMozos();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botonInsertarMesaActionPerformed

    //BOTON IR A ELIMINAR PLATO
    private void botonEliminarPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarPlatoActionPerformed
        boolean temp = false;
        try {
            temp = hayPlato(0);
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaPlatos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateRowHeight();
            visibilidadPaneles(panelEliminarPlato);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen platos en el sistema");
        }
    }//GEN-LAST:event_botonEliminarPlatoActionPerformed

    //BOTON IR A INSERTAR PLATO
    private void botonInsertarPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarPlatoActionPerformed
        visibilidadPaneles(panelInsertarPlato);
        limpiezaInsertarPlato();
    }//GEN-LAST:event_botonInsertarPlatoActionPerformed

    //BOTON IR A INSERTAR CONSUMO
    private void botonInsertarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarConsumoActionPerformed
        boolean aux = false;
        try {
            aux = hayPlato(0);
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean aux1 = false;
        try {
            aux1 = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(aux && aux1)
        {
            visibilidadPaneles(panelInsertarConsumo1);
            try {
            //Limpio todo el panel
            limpiezaIngresarConsumo1();
             } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            //Cargo las tablas y listas
            try{
                updateTablaMesas();
            }catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
             JOptionPane.showMessageDialog(null,"No existen platos o mesas en el sistema");
        }
        
    }//GEN-LAST:event_botonInsertarConsumoActionPerformed

    //BOTON PANEL INICIO
    private void botonInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInicioActionPerformed
        visibilidadPaneles(panelInicio);
        botonInicio.setIcon(botonInicioFlechaIcon);
        botonMozos.setIcon(botonMozosIcon);
        botonPlatos.setIcon(botonPlatosIcon);
        botonMesas.setIcon(botonMesasIcon);
        botonConsumos.setIcon(botonConsumosIcon);
        try {
            updatePrincipal();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botonInicioActionPerformed

    //BOTON PANEL MOZOS
    private void botonMozosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMozosActionPerformed
        visibilidadPaneles(panelMozos);
        botonInicio.setIcon(botonInicioIcon);
        botonMozos.setIcon(botonMozosFlechaIcon);
        botonPlatos.setIcon(botonPlatosIcon);
        botonMesas.setIcon(botonMesasIcon);
        botonConsumos.setIcon(botonConsumosIcon);
    }//GEN-LAST:event_botonMozosActionPerformed

    //BOTON PANEL PLATOS
    private void botonPlatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlatosActionPerformed
        visibilidadPaneles(panelPlatos);
        botonInicio.setIcon(botonInicioIcon);
        botonMozos.setIcon(botonMozosIcon);
        botonPlatos.setIcon(botonPlatosFlechaIcon);
        botonMesas.setIcon(botonMesasIcon);
        botonConsumos.setIcon(botonConsumosIcon);
    }//GEN-LAST:event_botonPlatosActionPerformed

    //BOTON PANEL MESAS
    private void botonMesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMesasActionPerformed
        visibilidadPaneles(panelMesas);
        botonInicio.setIcon(botonInicioIcon);
        botonMozos.setIcon(botonMozosIcon);
        botonPlatos.setIcon(botonPlatosIcon);
        botonMesas.setIcon(botonMesasFlechaIcon);
        botonConsumos.setIcon(botonConsumosIcon);
    }//GEN-LAST:event_botonMesasActionPerformed

    //BOTON PANEL CONSUMOS
    private void botonConsumosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConsumosActionPerformed
        visibilidadPaneles(panelConsumos);
        botonInicio.setIcon(botonInicioIcon);
        botonMozos.setIcon(botonMozosIcon);
        botonPlatos.setIcon(botonPlatosIcon);
        botonMesas.setIcon(botonMesasIcon);
        botonConsumos.setIcon(botonConsumosFlechaIcon);
    }//GEN-LAST:event_botonConsumosActionPerformed

    //BOTON VOLVER VER MOZOS
    private void botonVolverMozosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverMozosActionPerformed
         visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonVolverMozosActionPerformed

    //BOTON VOLVER VER CONSUMOS
    private void botonVolverConsumosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverConsumosActionPerformed
        visibilidadPaneles(panelConsumos);
    }//GEN-LAST:event_botonVolverConsumosActionPerformed
   
    //BOTON VOLVER VER PLATOS
    private void botonVolverPlatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverPlatosActionPerformed
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonVolverPlatosActionPerformed

    //BOTON VOLVER VER MESAS
    private void botonVolverMesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverMesasActionPerformed
        visibilidadPaneles(panelMesas);
    }//GEN-LAST:event_botonVolverMesasActionPerformed

    //BOTON VER MOZOS
    private void botonVerMozosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerMozosActionPerformed
        boolean temp = false; 
        try {
            temp = hayMozo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaMozos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelTablaMozos);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No hay mozos en el sistema");
        }
    }//GEN-LAST:event_botonVerMozosActionPerformed
    
    //BOTON IR MODIFICAR MOZO
    private void botonModificarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarMozoActionPerformed
        boolean temp = false; 
        try {
            temp = hayMozo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaMozos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelModificarMozo1);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No hay mozos en el sistema");
        }
    }//GEN-LAST:event_botonModificarMozoActionPerformed
    
    //BOTON VER MOZOS LIBRES
    private void botonMozosLibresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMozosLibresActionPerformed
        boolean temp = false;
        boolean temp1 = false;
        try {
            temp = hayMozoLibre();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            temp1 = hayMozo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!temp1)
        {
            JOptionPane.showMessageDialog(null,"No existen mozos en el sistema");
        }
        else if(temp)
        {
            try {
                updateMozosLibres();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelMozosLibres);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Todos los mozos están asignados");
        }
    }//GEN-LAST:event_botonMozosLibresActionPerformed

    //BOTON IR A ASIGNAR MOZO
    private void botonAsignarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAsignarMozoActionPerformed
        boolean temp1 = false;
        try {
            temp1 = hayMozo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean temp2 = false;
        try {
            temp2 = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp1)
        {
            if(temp2)
            {
                try {
                    updateTablaMozos();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    updateTablaMesas();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                visibilidadPaneles(panelAsignarMozo);
            }
            else
            {
                 JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mozos en el sistema");
        }
    }//GEN-LAST:event_botonAsignarMozoActionPerformed

    //BOTON IR A MESAS ASIGNADAS
    private void botonMesasAsignadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMesasAsignadasActionPerformed
        boolean temp = false;
        try {
            temp = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateCantMesasPorMozos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelMesasAsignadas);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
        }
    }//GEN-LAST:event_botonMesasAsignadasActionPerformed

    //BOTON IR A MODIFICAR MESA
    private void botonModificarMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarMesaActionPerformed
        boolean temp = false;
        try {
            temp = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaMesas();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
             visibilidadPaneles(panelModificarMesa1);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
        }
    }//GEN-LAST:event_botonModificarMesaActionPerformed

    //BOTON IR A VER MESAS
    private void botonVerMesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerMesasActionPerformed
        boolean temp = false;
        try {
            temp = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaMesas();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelTablaMesas);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
        }
    }//GEN-LAST:event_botonVerMesasActionPerformed
    
    //BOTON IR A MESAS POR UN MOZO
    private void botonMesaPorMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMesaPorMozoActionPerformed
        boolean temp = false;
        try {
            temp = hayMozo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            limpiezaMesasPorMozo();
            visibilidadPaneles(panelMesasPorMozo);
            try {
                updateTablaMozos();
            } catch (SQLException ex){ 
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mozos en el sistema");
        }      
    }//GEN-LAST:event_botonMesaPorMozoActionPerformed
    
    //BOTON IR A ELIMINAR UN CONSUMO
    private void botonEliminarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarConsumoActionPerformed
        boolean temp = false;
        try {
            temp = hayConsumo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaConsumos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelEliminarConsumo);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen consumos en el sistema");
        }
    }//GEN-LAST:event_botonEliminarConsumoActionPerformed

    //BOTON IR A MODIFICAR UN CONSUMO
    private void botonModificarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarConsumoActionPerformed
        boolean temp1 = false;
        boolean temp2 = false;
        boolean temp3 = false;
        try {
            temp1 = hayConsumo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            temp2 = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            temp3 = hayPlato(0);
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }        
        if(!temp1)
        {
            JOptionPane.showMessageDialog(null,"No existen consumos en el sistema");
        }
        else if(!temp2)
        {
            JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
        }
        else if(!temp3)
        {
            JOptionPane.showMessageDialog(null,"No existen platos en el sistema");
        }   
        else
        {
            try {
                updateTablaConsumos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelModificarConsumo1);
            limpiezaModificarConsumo1();
        }
    }//GEN-LAST:event_botonModificarConsumoActionPerformed
    
    //BOTON IR A VER CONSUMOS
    private void botonVerConsumosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerConsumosActionPerformed
        boolean temp = false;
        try {
            temp = hayConsumo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaConsumos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelTablaConsumos);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen consumos en el sistema");
        }
    }//GEN-LAST:event_botonVerConsumosActionPerformed
    
    //BOTON IR A MODIFICAR PLATO
    private void botonModificarPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarPlatoActionPerformed
        boolean temp = false;
        try {
            temp = hayPlato(0);
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaPlatos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateRowHeight();
            visibilidadPaneles(panelModificarPlato1);
            limpiezaModificarPlato1();
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen platos en el sistema");
        }
    }//GEN-LAST:event_botonModificarPlatoActionPerformed
    
    //BOTON IR A VER PLATOS
    private void botonVerPlatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerPlatosActionPerformed
        boolean temp = false;
        try {
            temp = hayPlato(0);
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateTablaPlatos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateRowHeight();
            visibilidadPaneles(panelTablaPlatos);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen platos en el sistema");
        }
    }//GEN-LAST:event_botonVerPlatosActionPerformed

    //BOTON IR A PLATOS NUNCA CONSUMIDOS
    private void botonPlatosNuncaConsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlatosNuncaConsActionPerformed
        boolean temp = false;
        try {
            temp = hayPlatosNuncaConsumidos();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            limpiezaPlatosNunca();
            try {
                 updatePlatosNuncaConsumidos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelPlatosNuncaConsumidos);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Todos los platos se han consumido al menos una vez");
        }
    }//GEN-LAST:event_botonPlatosNuncaConsActionPerformed
    
    //BOTON IR A PLATOS MAS CONSUMIDOS
    private void botonPlatoMasConsumidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlatoMasConsumidoActionPerformed
        try {
            updateTiposConsumidos();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        visibilidadPaneles(panelPlatosMasConsumidos);
    }//GEN-LAST:event_botonPlatoMasConsumidoActionPerformed
    
    //BOTON IR A CANTIDAD DE PLATOS POR MESA
    private void botonCantsPlatosPorMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCantsPlatosPorMesaActionPerformed
        boolean temp = false;
        try {
            temp = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            limpiezaCantPlatos();
            try {
                updateTablaMesas();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelCantTotalPlatosPorMesa);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
        }
    }//GEN-LAST:event_botonCantsPlatosPorMesaActionPerformed

    //BOTON IR A PLATOS POR FECHAS 
    private void botonPlatosPorFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlatosPorFechaActionPerformed
        boolean temp = false;
        try {
            temp = hayConsumo();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            limpiezaPlatosEntreFechas();
            visibilidadPaneles(panelPlatosPorFecha);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen consumos en el sistema");
        }
    }//GEN-LAST:event_botonPlatosPorFechaActionPerformed

    //BOTON IR A PLATOS POR MESA
    private void botonPlatosPorMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlatosPorMesaActionPerformed
        boolean temp = false;
        try {
            temp = hayMesa();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            limpiezaPlatosPorMesas();
            try {
                updateTablaMesas();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateRowHeight();
            visibilidadPaneles(panelPlatosPorMesa);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen mesas en el sistema");
        }
    }//GEN-LAST:event_botonPlatosPorMesaActionPerformed
    
    //BOTON IR A ANALISIS DE COSTOS DE PLATOS
    private void botonCostosPlatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCostosPlatosActionPerformed
        boolean temp = false;
        try {
            temp = hayPlato(1);
        } catch (SQLException ex){
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(temp)
        {
            try {
                updateMaxMinProm();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            visibilidadPaneles(panelCostosPlatos);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"No existen platos principales en el sistema");
        }
    }//GEN-LAST:event_botonCostosPlatosActionPerformed

    //BOTON VOLVER A PLATOS EN PLATOS CONSUMIDOS POR MESA
    private void botonVolverPlatosConsumidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverPlatosConsumidosActionPerformed
        limpiezaPlatosPorMesas();
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonVolverPlatosConsumidosActionPerformed
    
    //BOTON VOLVER A PLATOS EN ANALISIS
    private void botonVolverAnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverAnalisisActionPerformed
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonVolverAnalisisActionPerformed
    
    //BOTON VOLVER A PLATOS EN PLATOS NUNCA CONSUMIDOS
    private void botonVolverPlatosNuncaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverPlatosNuncaActionPerformed
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonVolverPlatosNuncaActionPerformed
    
    //BOTON CANCELAR ASIGNAR MOZO
    private void botonCancelarAsignarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarAsignarMozoActionPerformed
        limpiezaAsignarMozo();
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonCancelarAsignarMozoActionPerformed

    private void botonModificarAsignarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarAsignarMozoActionPerformed
        if(comboMozo1.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un mozo");
        }
        else if (comboMesa1.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija una mesa");
        }
        else{
            int codMozo = Integer.parseInt(String.valueOf(comboMozo1.getSelectedItem()).trim());
            int codMesa = Integer.parseInt(String.valueOf(comboMesa1.getSelectedItem()).trim());
            try {
                asignarMozo(codMesa,codMozo);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null,"Mozo asignado con éxito");
            try {
                updateTablaMesas();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            comboMozo1.setSelectedIndex(0);
            comboMesa1.setSelectedIndex(0);
        }
    }//GEN-LAST:event_botonModificarAsignarMozoActionPerformed

    private void botonVolverPlatosPorMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverPlatosPorMesaActionPerformed
        limpiezaPlatosPorMesas();
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonVolverPlatosPorMesaActionPerformed

    private void botonVolverMozosLibresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverMozosLibresActionPerformed
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonVolverMozosLibresActionPerformed

    private void botonVolverMesasPorMozosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverMesasPorMozosActionPerformed
        limpiezaMesasPorMozo();
        visibilidadPaneles(panelMesas);
    }//GEN-LAST:event_botonVolverMesasPorMozosActionPerformed

    private void botonVolverMesasAsignadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverMesasAsignadasActionPerformed
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonVolverMesasAsignadasActionPerformed

    private void botonCancelarEliminarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarEliminarConsumoActionPerformed
        limpiezaEliminarConsumo();
        visibilidadPaneles(panelConsumos);
    }//GEN-LAST:event_botonCancelarEliminarConsumoActionPerformed

    private void botonCancelarEliminarPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarEliminarPlatoActionPerformed
        limpiezaEliminarPlato();
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonCancelarEliminarPlatoActionPerformed

    private void botonCancelarEliminarMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarEliminarMesaActionPerformed
        limpiezaEliminarMesa();
        visibilidadPaneles(panelMesas);
    }//GEN-LAST:event_botonCancelarEliminarMesaActionPerformed

    private void botonCancelarEliminarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarEliminarMozoActionPerformed
        limpiezaEliminarMozo();
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonCancelarEliminarMozoActionPerformed

    private void botonCancelarInsertarPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarInsertarPlatoActionPerformed
        limpiezaInsertarPlato();
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonCancelarInsertarPlatoActionPerformed

    private void botonInsertarPlato1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarPlato1ActionPerformed
        //Tomar los datos
        String nombre = textCodIngNombrePlato.getText();
        String descripcion = textCodIngMesaD.getText();
        String tipo = String.valueOf(jComboBox2.getSelectedItem());
        int costo = Integer.parseInt(String.valueOf(jSpinner1.getValue()));
        int venta = Integer.parseInt(String.valueOf(jSpinner2.getValue()));
        int promocion = Integer.parseInt(String.valueOf(jSpinner3.getValue()));
        
        //Realizo una cascada de errores para que no aparezcan todos a la vez
        if(nombre.equals(""))
        {
           JOptionPane.showMessageDialog(null,"El campo nombre no puede estar vacío");
        }
        else if(descripcion.equals(""))
        {
            JOptionPane.showMessageDialog(null,"El campo descripción no puede estar vacío");
        }
        else if(jComboBox2.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija el tipo");
        }
        else if(costo == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un valor de costo válido");
        }
        else if(venta == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un valor de venta válido");
        }
        else if(promocion == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un valor de promoción válido");
        }
        else
        {
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea ingresar el plato?", "Ingresar plato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    insertarPlato(nombre,descripcion,tipo,costo,venta,promocion);
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "Plato ingresado con éxito");   
                limpiezaInsertarPlato();
             }   
        }
    }//GEN-LAST:event_botonInsertarPlato1ActionPerformed

    private void botonCancelarInsertarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarInsertarConsumoActionPerformed
        try {
            limpiezaIngresarConsumo1();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        visibilidadPaneles(panelConsumos);
    }//GEN-LAST:event_botonCancelarInsertarConsumoActionPerformed

    private void botonInsertarConsumo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarConsumo1ActionPerformed
        //Necesito los datos en String para la función que ingresa
        //Tomo los datos
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        Date jfecha = jDateChooser1.getDate();
        Date afecha = new Date();
        String fecha = format.format(jfecha);
        int hora = Integer.parseInt(String.valueOf(spinnerHoraConsumos.getValue()).trim());
        int minuto = Integer.parseInt(String.valueOf(spinnerMinutosConsumos.getValue()).trim());
        int codigoMesa;
        int clave = -1;
        
        if(jfecha.after(afecha)) //Si la fecha elegida es futura a la fecha actual
        {
            JOptionPane.showMessageDialog(null,"Elija una fecha válida");
        }
        else if(jComboBox7.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija una mesa");
        }
        else if(listaPlatos.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Debe ingresar al menos un plato");
        }
        else //Inserto el consumo
        {
            codigoMesa = Integer.parseInt(String.valueOf(jComboBox7.getSelectedItem()).trim());
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea ingresar el consumo?", "Ingresar consumo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    clave = insertarConsumo(fecha,hora,minuto,codigoMesa);
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                for(int i = 0; i < listaPlatos.size(); i++)
                {  
                    try {
                        insertarSe_Consume(clave,listaPlatos.get(i));
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                JOptionPane.showMessageDialog(null, "Consumo ingresado con éxito");  
                listaPlatos.clear();
                try {
                    limpiezaIngresarConsumo1();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                limpiezaIngresarConsumo2();
            } 
        }
    }//GEN-LAST:event_botonInsertarConsumo1ActionPerformed

    private void botonCancelarIngresoMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarIngresoMesaActionPerformed
        limpiezaInsertarMesa();
        visibilidadPaneles(panelMesas);
    }//GEN-LAST:event_botonCancelarIngresoMesaActionPerformed

    private void botonMesaIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMesaIngresarActionPerformed
        //Tomar los datos
        String sector = textSectorMesaIngreso.getText();
        int mozo;
        //Verificar que estén bien (error sino)
        if(sector.equals(""))
        {
            JOptionPane.showMessageDialog(null,"El campo sector no puede estar vacío");
        }
        else if(comboMozoMesa.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un mozo");
        }
        else //Insertar en la base de datos
        {
            mozo = Integer.parseInt(String.valueOf(comboMozoMesa.getSelectedItem()).trim());
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea ingresar la mesa?", "Ingresar mesa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    insertarMesa(sector,mozo);
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "Mesa ingresada con éxito");
                limpiezaInsertarMesa();
            }
        }
    }//GEN-LAST:event_botonMesaIngresarActionPerformed

    private void botonCancelarMozosIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarMozosIngresoActionPerformed
        limpiezaInsertarMozo();
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonCancelarMozosIngresoActionPerformed

    private void botonIngresarMozosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIngresarMozosActionPerformed
        //Necesito los datos en String para la función que ingresa
        //Uso temporalmente el dni como entero para verificar su valor
        String nombre = textNombreMozoIngreso.getText();
        int dni = Integer.parseInt(String.valueOf(DNIMozoIngreso.getValue()).trim());
        String domicilio = textDomicilioMozoIngreso.getText();
        boolean temp = false;
        try {
            temp = buscarMozoDNI(dni);
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(temp)
        {
            JOptionPane.showMessageDialog(null, "Ya existe un mozo con ese DNI, ingrese nuevamente");
        }
        else if(nombre.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "El campo nombre no puede estar vacío");
        }
        else if(domicilio.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "El campo domicilio no puede estar vacío");
        }
        else //Insertar en la base de datos
        {
            try {
                insertarMozo(nombre,domicilio,dni);
            } catch (SQLException ex){
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Mozo ingresado con éxito");
            limpiezaInsertarMozo();
        }
    }//GEN-LAST:event_botonIngresarMozosActionPerformed

    //BOTON CANCELAR PLATOS ENTRE FECHAS
    private void botonCancelarPlatosEntreFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarPlatosEntreFechasActionPerformed
        limpiezaPlatosEntreFechas();
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonCancelarPlatosEntreFechasActionPerformed

    private void botonBuscarMesasPorMozos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBuscarMesasPorMozos1ActionPerformed
        int index = comboMesasPorMozo.getSelectedIndex();
        LinkedList<Integer> listaMozos = null;
        limpiezaMesasPorMozo();
        boolean aux = false;
        
        if(index == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija una mesa");
        }
        else
        {
            try {
                listaMozos =  devolverListaMozos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(listaMozos.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "No hay mozos en el sistema");
            }
            else
            {
                try {
                    aux = updateMesasPorUnMozo(listaMozos.get(index-1));
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(aux == false)
                {
                    JOptionPane.showMessageDialog(null, "Este mozo no tiene mesas asignadas");
                }
            }
        }
    }//GEN-LAST:event_botonBuscarMesasPorMozos1ActionPerformed

    private void botonBuscarPlatosEntreFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBuscarPlatosEntreFechasActionPerformed
        Date f1 = jDateChooser3.getDate();
        Date f2 = jDateChooser2.getDate();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        
        String fecha1 = format.format(f1);
        String fecha2 = format.format(f2);
        
        boolean aux = false;
        
        if(f1.after(f2))
        {
            JOptionPane.showMessageDialog(null, "La fecha de inicio es posterior a la fecha de fin");
        }
        else
        {
            try {
                aux = updatePlatosEntreFechas(fecha1, fecha2);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(aux == false)
            {
                JOptionPane.showMessageDialog(null, "No se han consumido platos en este período");
            }
        }
    }//GEN-LAST:event_botonBuscarPlatosEntreFechasActionPerformed

    private void jScrollPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyPressed

    }//GEN-LAST:event_jScrollPane1KeyPressed

    private void botonModificarMozo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarMozo1ActionPerformed
       if(jComboBox9.getSelectedIndex() == 0)
       {
           JOptionPane.showMessageDialog(null, "Elija un mozo");
       }
       else
       {
            codigoAux = Integer.parseInt(String.valueOf(jComboBox9.getSelectedItem()).trim());
            limpiezaModificarMozo2();
            visibilidadPaneles(panelModificarMozo2);
            LinkedList<String> lista = null;
           try {
               lista = devolverMozo(codigoAux);
           } catch (SQLException ex) {
               Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
           }
            textCodMozo.setText(String.valueOf(codigoAux));
            textNombreMozoModificar.setText(lista.get(2));
            DNIMozoModificar.setValue(Integer.parseInt(lista.get(1)));
            textDomicilioMozoModificar.setText(lista.get(3));
       }
    }//GEN-LAST:event_botonModificarMozo1ActionPerformed

    private void botonCancelarModificarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarModificarMozoActionPerformed
        limpiezaModificarMozo1();
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonCancelarModificarMozoActionPerformed

    private void jScrollPane19KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane19KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane19KeyPressed

    private void botonCancelarMozoModificar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarMozoModificar2ActionPerformed
        limpiezaModificarMozo2();
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonCancelarMozoModificar2ActionPerformed

    private void botonModificarMozo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarMozo2ActionPerformed
        String nombre = textNombreMozoModificar.getText();
        String domicilio = textDomicilioMozoModificar.getText();
        int dni = Integer.parseInt(String.valueOf(DNIMozoModificar.getValue()).trim());
        Boolean aux = false;
        Boolean temp = false;
        LinkedList<String> lista = null;
        
        try {
            temp = buscarMozoDNI(dni); //Veo si existe otro mozo con ese dni
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(temp) //Si se repite, determino porqué
        {
            try {
                lista = devolverMozoDNI(dni); //Tomo los datos del mozo con el dni obtenido
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            aux = !(codigoAux == (Integer.parseInt(lista.get(0)))); 
            //Aux será verdadero cuando exista otro mozo con el mismo DNI
        }
        
        if(aux) //Si el DNI se repite
        {
            JOptionPane.showMessageDialog(null, "Ya existe un mozo con ese DNI, ingrese nuevamente");
        }
        else if(nombre.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "El campo nombre no puede estar vacío");
        }
        else if(domicilio.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "El campo domicilio no puede estar vacío");
        }        
        else
        {
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea modificar el mozo?", "Modificar mozo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    modificarMozo(codigoAux,nombre,domicilio,dni);
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "Mozo modificado con éxito"); 
                limpiezaModificarMozo2();
            }
        }
    }//GEN-LAST:event_botonModificarMozo2ActionPerformed

    private void botonModificarMesa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarMesa1ActionPerformed
        limpiezaModificarMesa1();
        if(jComboBox10.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija una mesa");
        }
        else
        {
            LinkedList<String> lista = null;
            visibilidadPaneles(panelModificarMesa2);                
            codigoAux = Integer.parseInt(String.valueOf(jComboBox10.getSelectedItem()).trim());
            try {
                updateTablaMesas();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                updateTablaMozos();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {                
                lista = devolverMesa(codigoAux);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            textCodMesa.setEditable(false);
            textCodMesa.setText(String.valueOf(codigoAux));
            textSectorMesaModificar.setText(lista.get(1));
            comboMozoMesaMod1.setSelectedItem(lista.get(2));
        }
    }//GEN-LAST:event_botonModificarMesa1ActionPerformed

    private void botonCancelarModificarMozo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarModificarMozo1ActionPerformed
        limpiezaModificarMozo1();
        limpiezaModificarMozo2();
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonCancelarModificarMozo1ActionPerformed

    private void comboMozoMesaMod1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMozoMesaMod1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboMozoMesaMod1ActionPerformed

    private void botonModificarMesa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarMesa2ActionPerformed
        if(comboMozoMesaMod1.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un mozo");
        }
        else if(textSectorMesaModificar.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"El campo sector no puede estar vacío");
        }
        else
        {
            int codigoMozo = Integer.parseInt(String.valueOf(comboMozoMesaMod1.getSelectedItem()).trim());
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea modificar la mesa?", "Modificar mesa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    modificarMesa(codigoAux, textSectorMesaModificar.getText(),codigoMozo);
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "Mesa modificada con éxito"); 
                limpiezaModificarMesa2();
                visibilidadPaneles(panelMesas);
            } 
        }
    }//GEN-LAST:event_botonModificarMesa2ActionPerformed

    private void botonCancelarModificarMesa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarModificarMesa1ActionPerformed
        limpiezaModificarMesa2();
        visibilidadPaneles(panelMesas);
    }//GEN-LAST:event_botonCancelarModificarMesa1ActionPerformed

    private void botonModificarConsumo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarConsumo1ActionPerformed

        int hora,minuto;
        String horaS,minutoS;
        if(jComboBox11.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un consumo");
        }
        else
        {
            visibilidadPaneles(panelModificarConsumo2);         
            try {
                updateTablaMesas(); //Actualizo la tabla mesas y la lista mesas
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            codigoAux = Integer.parseInt(String.valueOf(jComboBox11.getSelectedItem()).trim()); //Tomo el codigo
            LinkedList<String> listaConsumo = null ;
            
            try {
                listaConsumo = devolverConsumo(codigoAux); //Creo una lista con todos los datos del consumo
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }            
            horaS = (listaConsumo.get(2)).substring(0,2); //Tomo la hora 
            hora = Integer.parseInt(horaS); //Convierto la hora a entero
            minutoS = (listaConsumo.get(2)).substring(3,5); //Tomo los minutos
            minuto = Integer.parseInt(minutoS); //Convierto los minutos en entero
            spinnerHoraConsumosModificar.setValue(hora); //Muestro la hora del correspondiende consumo
            spinnerMinutosConsumosModificar.setValue(minuto); //Muestro el minuto del correspondiente consumo
            jComboBox13.setSelectedItem(listaConsumo.get(3)); //Muestro el código de la correspondiente mesa del consumo
            try {
                listaPlatos = devolverPlatosConsumo(codigoAux);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                updatePlatosPorConsumo(codigoAux);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateRowHeight();
        }
    }//GEN-LAST:event_botonModificarConsumo1ActionPerformed

    private void botonCancelarModificarConsumo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarModificarConsumo1ActionPerformed
        limpiezaModificarConsumo1();
        visibilidadPaneles(panelConsumos);
    }//GEN-LAST:event_botonCancelarModificarConsumo1ActionPerformed

    private void botonModificarPlato2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarPlato2ActionPerformed
        String nombre = textNombrePlato1.getText(); 
        String descripcion = textDescMesaMod.getText();
        String tipo;
        
        int costo = Integer.parseInt(String.valueOf(jSpinner6.getValue()));
        int venta = Integer.parseInt(String.valueOf(jSpinner5.getValue()));
        int promocion = Integer.parseInt(String.valueOf(jSpinner4.getValue()));
        
        if(nombre.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"El campo nombre no puede estar vacío");
        }
        else if(descripcion.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"El campo descripción no puede estar vacío");
        }
        else if(jComboBox12.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija el tipo");
        }
        else if (costo == 0)
        {
            JOptionPane.showMessageDialog(null,"El valor de costo no es válido");
        }
        else if (venta == 0)
        {
           JOptionPane.showMessageDialog(null,"El valor de venta no es válido");
        }
        else if (promocion == 0)
        {
           JOptionPane.showMessageDialog(null,"El valor de promoción no es válido");
        }        
        else
        {
            tipo = String.valueOf(jComboBox12.getSelectedItem());
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea modificar el plato?", "Modificar plato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                try {
                    modificarPlato(codigoAux,nombre,descripcion,tipo,costo,venta,promocion);
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "Plato modificado con éxito");  
                limpiezaModificarPlato2();
                updateRowHeight();
                visibilidadPaneles(panelPlatos);
            } 
        }
    }//GEN-LAST:event_botonModificarPlato2ActionPerformed

    private void botonCancelarModificarPlato1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarModificarPlato1ActionPerformed
        limpiezaModificarPlato2();
        visibilidadPaneles(panelPlatos);
        listaPlatos.clear();
    }//GEN-LAST:event_botonCancelarModificarPlato1ActionPerformed

    private void botonCancelarModificarConsumo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarModificarConsumo2ActionPerformed
        limpiezaModificarConsumo2();
        limpiezaModificarConsumo1();
        listaPlatos.clear();
        visibilidadPaneles(panelConsumos);
    }//GEN-LAST:event_botonCancelarModificarConsumo2ActionPerformed

    private void botonModificarConsumo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarConsumo2ActionPerformed
        Date jfecha = jDateChooser4.getDate();
        Date afecha = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String fecha = format.format(jfecha);
        int hora = Integer.parseInt(String.valueOf(spinnerHoraConsumosModificar.getValue()).trim());
        int minuto = Integer.parseInt(String.valueOf(spinnerMinutosConsumosModificar.getValue()).trim());
        int mesa = Integer.parseInt(String.valueOf(jComboBox13.getSelectedItem()).trim());
        
        if(jfecha.after(afecha))
        {
            JOptionPane.showMessageDialog(null,"Elija una fecha válida");
        }
        else if(jComboBox13.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija una mesa");
        }
        else if(listaPlatos.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Debe seleccionar al menos un plato");
        }
        else
        {
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea modificar el consumo?", "Modificar consumo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
             //0=si, 1=no
            if(ventana == 0) {            
                try {
                     eliminarSe_ConsumeC(codigoAux); //Elimino primero las nuplas de Se_Consume para que no haya problemas de foreing keys
                } catch (SQLException ex1) {
                     Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex1);
                }
                
                try {
                    modificarConsumo(codigoAux,fecha,hora,minuto,mesa); //Modifico el conusmo
                }
                
                catch (SQLException ex) {
                   Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for(int i = 0; i < listaPlatos.size(); i++) //Inserto las nuplas de Se_Consume
                {
                        try {
                            insertarSe_Consume(codigoAux,listaPlatos.get(i));
                        } catch (SQLException ex1) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                }          
                
                listaPlatos.clear();                    
                JOptionPane.showMessageDialog(null, "Consumo modificado con éxito");
                limpiezaModificarConsumo1();
                visibilidadPaneles(panelConsumos);
            }
        }
    }//GEN-LAST:event_botonModificarConsumo2ActionPerformed

    private void botonPlatoConsumoModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlatoConsumoModificarActionPerformed
        visibilidadPaneles(panelModificarConsumo3);
        try {
            updateTablaPlatos();
            updateRowHeight();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botonPlatoConsumoModificarActionPerformed

    private void botonCancelarModificarPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarModificarPlatoActionPerformed
        limpiezaModificarPlato1();
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonCancelarModificarPlatoActionPerformed

    private void botonModificarPlato1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarPlato1ActionPerformed
        if(jComboBox14.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un plato");
        }
        else
        {
            LinkedList<String> lista = null;
            codigoAux = Integer.parseInt(String.valueOf(jComboBox14.getSelectedItem()).trim());
            try {
                lista = devolverPlato(codigoAux);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            textCodPlato.setText(String.valueOf(codigoAux));
            textNombrePlato1.setText(lista.get(1));
            textDescMesaMod.setText(lista.get(2));
            jComboBox12.setSelectedItem(lista.get(3));
            jSpinner6.setValue(Float.parseFloat(lista.get(4)));
            jSpinner5.setValue(Float.parseFloat(lista.get(5)));
            jSpinner4.setValue(Float.parseFloat(lista.get(6)));  
            visibilidadPaneles(panelModificarPlato2);
            limpiezaModificarPlato1();
        }
    }//GEN-LAST:event_botonModificarPlato1ActionPerformed

    private void botonPlatoConsumoModificar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlatoConsumoModificar1ActionPerformed
        visibilidadPaneles(panelInsertarConsumo2);
        try {
            updateTablaPlatos();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateRowHeight();
    }//GEN-LAST:event_botonPlatoConsumoModificar1ActionPerformed

    private void botonEliminarPlatoConsumoModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarPlatoConsumoModActionPerformed
        int index = jComboBox1.getSelectedIndex();
        
        if(index == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un plato");
        }
        else if(!listaPlatos.contains(listaPlatosFullCodigo.get(index-1))) //Verifico que exista el elemento que se quiere eliminar
        {
           JOptionPane.showMessageDialog(null,"El plato que quiere eliminar no pertenece a este consumo");
        }
        else
        {
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el plato?", "Eliminar plato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
             if(ventana == 0) {
                listaPlatos.remove(listaPlatosFullCodigo.get(index-1));
                JOptionPane.showMessageDialog(null, "Plato eliminado con éxito");       
                try {
                    updatePlatosPorConsumo3();                    
                } 
                catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateRowHeight();
                limpiezaModificarConsumo3();
            }
        }
    }//GEN-LAST:event_botonEliminarPlatoConsumoModActionPerformed

    private void botonInsertarPlatoConsumoModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarPlatoConsumoModActionPerformed
        int index = jComboBox1.getSelectedIndex();
        if(index == 0)
        {
            JOptionPane.showMessageDialog(null,"Elija un plato");
        }
        else if(listaPlatos.contains(listaPlatosFullCodigo.get(index-1))) //Verifico que no exista el elemento que quiero ingresar
        {
           JOptionPane.showMessageDialog(null,"El plato que quiere agregar ya pertenece a este consumo");
        }
        else
        {
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea insertar el plato?", "Insertar plato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                listaPlatos.add(listaPlatosFullCodigo.get(index-1));              
                JOptionPane.showMessageDialog(null, "Plato insertado con éxito");            
                try {
                    updatePlatosPorConsumo3();                          
                } 
                catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateRowHeight();
                limpiezaModificarConsumo3();
            }
        }
    }//GEN-LAST:event_botonInsertarPlatoConsumoModActionPerformed

    private void botonVolverPlatosConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverPlatosConsumoActionPerformed
        visibilidadPaneles(panelModificarConsumo2);        
    }//GEN-LAST:event_botonVolverPlatosConsumoActionPerformed

    private void botonInsertarPlatoConsumoIng1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarPlatoConsumoIng1ActionPerformed
 	int index = jComboBox8.getSelectedIndex();
        if(index == 0)
        {
            JOptionPane.showConfirmDialog(null, "Elija un plato");
        }
        else if(listaPlatos.contains(listaPlatosFullCodigo.get(index-1))) //Verifico que no exista el elemento que quiero ingresar
        {
           JOptionPane.showMessageDialog(null, "El plato que quiere agregar ya pertenece a este consumo");
        }
        else
        {
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea agregar el plato?", "Agregar plato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            //0=si, 1=no
            if(ventana == 0) {
                //Agrego a la lista el plato 
                listaPlatos.add(listaPlatosFullCodigo.get(index-1));
                JOptionPane.showMessageDialog(null, "Plato agregado con éxito");
                //Actualizo las tablas de platos del consumo
                try {    
                        updatePlatosPorConsumo2(); //Actualizo la lista de platos agregados al consumo
                    }    
                catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateRowHeight();
                //Vuelvo a cero el índice del combo
                limpiezaIngresarConsumo2();
              }       
        }
    }//GEN-LAST:event_botonInsertarPlatoConsumoIng1ActionPerformed

    
    private void botonEliminarPlatoConsumoIng1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarPlatoConsumoIng1ActionPerformed
        int index = jComboBox8.getSelectedIndex();
        if(index == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija un plato");
        }
        else if(!listaPlatos.contains(listaPlatosFullCodigo.get(index-1))) //Verifico que exista el elemento que se quiere eliminar
        {
           JOptionPane.showMessageDialog(null, "El plato que quiere eliminar no pertenece a este consumo");
        }
        else
        {
            int ventana = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el plato?", "Eliminar plato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		//0=si, 1=no
            if(ventana == 0) {
                    //Elimino de la lista el plato
                    listaPlatos.remove(listaPlatosFullCodigo.get(index-1));
                    JOptionPane.showMessageDialog(null, "Plato eliminado con éxito");
                    try {
                            //Actualizo la lista de platos agregados al consumo
                            updatePlatosPorConsumo2();
                        }    
                    catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    updateRowHeight();
                    //Vuelvo a cero el índice del combo
                    limpiezaIngresarConsumo2();
            }
        }
    }//GEN-LAST:event_botonEliminarPlatoConsumoIng1ActionPerformed

    private void botonVolverPlatosConsumoIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverPlatosConsumoIngresoActionPerformed
        visibilidadPaneles(panelInsertarConsumo1);
    }//GEN-LAST:event_botonVolverPlatosConsumoIngresoActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox15ActionPerformed

    private void botonVerCantPlatos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerCantPlatos1ActionPerformed
        if(jComboBox15.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija una mesa");
        }
        else
        {
            try {
                updateCantTotalPlatosConsumidos(Integer.parseInt(String.valueOf(jComboBox15.getSelectedItem())));
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_botonVerCantPlatos1ActionPerformed

    private void botonVolverCantPlatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverCantPlatosActionPerformed
        limpiezaCanTotalPlatosPorMesa();
        visibilidadPaneles(panelPlatos);
    }//GEN-LAST:event_botonVolverCantPlatosActionPerformed

    private void comboPlatosPorMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPlatosPorMesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPlatosPorMesaActionPerformed

    private void buscarPlatosPorMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarPlatosPorMesaActionPerformed
        boolean aux = false;
        if(comboPlatosPorMesa.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija una mesa");
        }
        else
        {
            int codigo = Integer.parseInt(String.valueOf(comboPlatosPorMesa.getSelectedItem()).trim());
            try {
                aux = tienePlato(codigo);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(!aux)
            {
                JOptionPane.showMessageDialog(null, "No se han cosumido platos en esta mesa");
                limpiezaPlatosPorMesas();
            }
            else
            {
                try {
                    updateMesaPlatos(codigo);
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } 
                updateRowHeight();
            }
        }
    }//GEN-LAST:event_buscarPlatosPorMesaActionPerformed

    private void BotonLiberarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonLiberarTodoActionPerformed
        int codMozo = Integer.parseInt(String.valueOf(jComboBox16.getSelectedItem()).trim());
        try {
            liberarMesa(-1,codMozo,2);
            JOptionPane.showMessageDialog(null, "Mesas liberadas exitosamente");
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpiezaEliminarMozoDeMesa();
        lateral.setVisible(true);
        jLabel11.setVisible(true);
        jComboBox16.setVisible(true);
        botonBuscarMozoMesa.setVisible(true);
        jLabel10.setVisible(false);
        jComboBox17.setVisible(false);
        botonLiberarMozo.setVisible(false);
        BotonLiberarTodo.setVisible(false);
    }//GEN-LAST:event_BotonLiberarTodoActionPerformed

    private void botonLiberarMozoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLiberarMozoActionPerformed
        int codMesa = Integer.parseInt(String.valueOf(jComboBox17.getSelectedItem()).trim());
        int codMozo = Integer.parseInt(String.valueOf(jComboBox16.getSelectedItem()).trim());
        try {
            liberarMesa(codMesa,codMozo,1);
            JOptionPane.showMessageDialog(null, "Mesa liberada exitosamente");
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpiezaEliminarMozoDeMesa();
        lateral.setVisible(true);
        jLabel11.setVisible(true);
        jComboBox16.setVisible(true);
        botonBuscarMozoMesa.setVisible(true);
        jLabel10.setVisible(false);
        jComboBox17.setVisible(false);
        botonLiberarMozo.setVisible(false);
        BotonLiberarTodo.setVisible(false);
    }//GEN-LAST:event_botonLiberarMozoActionPerformed

    private void botonBuscarMozoMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBuscarMozoMesaActionPerformed
        boolean aux = false;
        if(jComboBox16.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Elija una mozo");
        }
        
        else
        {
            int codigo = Integer.parseInt(String.valueOf(jComboBox16.getSelectedItem()).trim());
            try {
                aux = updateMesasPorUnMozo(codigo);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(aux == false)
            {
                JOptionPane.showMessageDialog(null, "Este mozo no ha sigo asignado a ninguna mesa");
            }
            else
            {
                lateral.setVisible(true);
                jLabel11.setVisible(false);
                jComboBox16.setVisible(false);
                botonBuscarMozoMesa.setVisible(false);
                jLabel10.setVisible(true);
                jComboBox17.setVisible(true);
                botonLiberarMozo.setVisible(true);
                BotonLiberarTodo.setVisible(true);
            }
        }
        
                
    }//GEN-LAST:event_botonBuscarMozoMesaActionPerformed

    private void botonEliminarMozoMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarMozoMesaActionPerformed
        visibilidadPaneles(panelEliminarMozoMesa);
        limpiezaEliminarMozoDeMesa();
        jLabel10.setVisible(false);
        jComboBox17.setVisible(false);
        botonLiberarMozo.setVisible(false);
        BotonLiberarTodo.setVisible(false);
        try {
            updateTablaMozos();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botonEliminarMozoMesaActionPerformed

    private void botonCancelarEliminarMMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarEliminarMMActionPerformed
        limpiezaEliminarMozoDeMesa();
        jLabel11.setVisible(true);
        jComboBox16.setVisible(true);
        botonBuscarMozoMesa.setVisible(true);
        visibilidadPaneles(panelMozos);
    }//GEN-LAST:event_botonCancelarEliminarMMActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void textNombrePlato1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNombrePlato1KeyTyped
        char caracter = evt.getKeyChar();      
        if(caracterInvalido(String.valueOf(caracter)))
        {
           evt.consume();  // ignorar el evento de teclado
        }
    }//GEN-LAST:event_textNombrePlato1KeyTyped

    private void textDescMesaModKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textDescMesaModKeyTyped
    }//GEN-LAST:event_textDescMesaModKeyTyped

    private void textNombreMozoIngresoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNombreMozoIngresoKeyTyped
        char caracter = evt.getKeyChar();      
        if(caracterInvalido(String.valueOf(caracter)))
        {
           evt.consume();  // ignorar el evento de teclado
      }
    }//GEN-LAST:event_textNombreMozoIngresoKeyTyped

    private void textNombreMozoModificarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNombreMozoModificarKeyTyped
        char caracter = evt.getKeyChar();      
        if(caracterInvalido(String.valueOf(caracter)))
        {
           evt.consume();  // ignorar el evento de teclado
        }
    }//GEN-LAST:event_textNombreMozoModificarKeyTyped

    private void textCodIngNombrePlatoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textCodIngNombrePlatoKeyTyped
        char caracter = evt.getKeyChar();      
        if(caracterInvalido(String.valueOf(caracter)))
        {
           evt.consume();  // ignorar el evento de teclado
        }
    }//GEN-LAST:event_textCodIngNombrePlatoKeyTyped

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            updatePrincipal();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowOpened
  
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainFrame().setVisible(true);
                    MainFrame.inicio();
                } catch (SQLException | FontFormatException | IOException | URISyntaxException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonLiberarTodo;
    private javax.swing.JSpinner DNIMozoIngreso;
    private javax.swing.JSpinner DNIMozoModificar;
    private javax.swing.JButton botonAsignarMozo;
    private javax.swing.JButton botonBuscarMesasPorMozos1;
    private javax.swing.JButton botonBuscarMozoMesa;
    private javax.swing.JButton botonBuscarPlatosEntreFechas;
    private javax.swing.JButton botonCancelarAsignarMozo;
    private javax.swing.JButton botonCancelarEliminarConsumo;
    private javax.swing.JButton botonCancelarEliminarMM;
    private javax.swing.JButton botonCancelarEliminarMesa;
    private javax.swing.JButton botonCancelarEliminarMozo;
    private javax.swing.JButton botonCancelarEliminarPlato;
    private javax.swing.JButton botonCancelarIngresoMesa;
    private javax.swing.JButton botonCancelarInsertarConsumo;
    private javax.swing.JButton botonCancelarInsertarPlato;
    private javax.swing.JButton botonCancelarModificarConsumo1;
    private javax.swing.JButton botonCancelarModificarConsumo2;
    private javax.swing.JButton botonCancelarModificarMesa1;
    private javax.swing.JButton botonCancelarModificarMozo;
    private javax.swing.JButton botonCancelarModificarMozo1;
    private javax.swing.JButton botonCancelarModificarPlato;
    private javax.swing.JButton botonCancelarModificarPlato1;
    private javax.swing.JButton botonCancelarMozoModificar2;
    private javax.swing.JButton botonCancelarMozosIngreso;
    private javax.swing.JButton botonCancelarPlatosEntreFechas;
    private javax.swing.JButton botonCantsPlatosPorMesa;
    private javax.swing.JButton botonConsumos;
    private javax.swing.JButton botonCostosPlatos;
    private javax.swing.JButton botonEliminarConsumo;
    private javax.swing.JButton botonEliminarConsumo1;
    private javax.swing.JButton botonEliminarMesa;
    private javax.swing.JButton botonEliminarMesa1;
    private javax.swing.JButton botonEliminarMozo;
    private javax.swing.JButton botonEliminarMozo1;
    private javax.swing.JButton botonEliminarMozoMesa;
    private javax.swing.JButton botonEliminarPlato;
    private javax.swing.JButton botonEliminarPlato1;
    private javax.swing.JButton botonEliminarPlatoConsumoIng1;
    private javax.swing.JButton botonEliminarPlatoConsumoMod;
    private javax.swing.JButton botonIngresarMozos;
    private javax.swing.JButton botonInicio;
    private javax.swing.JButton botonInsertarConsumo;
    private javax.swing.JButton botonInsertarConsumo1;
    private javax.swing.JButton botonInsertarMesa;
    private javax.swing.JButton botonInsertarMozo;
    private javax.swing.JButton botonInsertarPlato;
    private javax.swing.JButton botonInsertarPlato1;
    private javax.swing.JButton botonInsertarPlatoConsumoIng1;
    private javax.swing.JButton botonInsertarPlatoConsumoMod;
    private javax.swing.JButton botonLiberarMozo;
    private javax.swing.JButton botonMesaIngresar;
    private javax.swing.JButton botonMesaPorMozo;
    private javax.swing.JButton botonMesas;
    private javax.swing.JButton botonMesasAsignadas;
    private javax.swing.JButton botonModificarAsignarMozo;
    private javax.swing.JButton botonModificarConsumo;
    private javax.swing.JButton botonModificarConsumo1;
    private javax.swing.JButton botonModificarConsumo2;
    private javax.swing.JButton botonModificarMesa;
    private javax.swing.JButton botonModificarMesa1;
    private javax.swing.JButton botonModificarMesa2;
    private javax.swing.JButton botonModificarMozo;
    private javax.swing.JButton botonModificarMozo1;
    private javax.swing.JButton botonModificarMozo2;
    private javax.swing.JButton botonModificarPlato;
    private javax.swing.JButton botonModificarPlato1;
    private javax.swing.JButton botonModificarPlato2;
    private javax.swing.JButton botonMozos;
    private javax.swing.JButton botonMozosLibres;
    private javax.swing.JButton botonPlatoConsumoModificar;
    private javax.swing.JButton botonPlatoConsumoModificar1;
    private javax.swing.JButton botonPlatoMasConsumido;
    private javax.swing.JButton botonPlatos;
    private javax.swing.JButton botonPlatosNuncaCons;
    private javax.swing.JButton botonPlatosPorFecha;
    private javax.swing.JButton botonPlatosPorMesa;
    private javax.swing.JButton botonVerCantPlatos1;
    private javax.swing.JButton botonVerConsumos;
    private javax.swing.JButton botonVerMesas;
    private javax.swing.JButton botonVerMozos;
    private javax.swing.JButton botonVerPlatos;
    private javax.swing.JButton botonVolverAnalisis;
    private javax.swing.JButton botonVolverCantPlatos;
    private javax.swing.JButton botonVolverConsumos;
    private javax.swing.JButton botonVolverMesas;
    private javax.swing.JButton botonVolverMesasAsignadas;
    private javax.swing.JButton botonVolverMesasPorMozos;
    private javax.swing.JButton botonVolverMozos;
    private javax.swing.JButton botonVolverMozosLibres;
    private javax.swing.JButton botonVolverPlatos;
    private javax.swing.JButton botonVolverPlatosConsumidos;
    private javax.swing.JButton botonVolverPlatosConsumo;
    private javax.swing.JButton botonVolverPlatosConsumoIngreso;
    private javax.swing.JButton botonVolverPlatosNunca;
    private javax.swing.JButton botonVolverPlatosPorMesa;
    private javax.swing.JButton buscarPlatosPorMesa;
    private javax.swing.JComboBox<String> comboMesa1;
    private javax.swing.JComboBox<String> comboMesasPorMozo;
    private javax.swing.JComboBox<String> comboMozo1;
    private javax.swing.JComboBox<String> comboMozoMesa;
    private javax.swing.JComboBox<String> comboMozoMesaMod1;
    private javax.swing.JComboBox<String> comboPlatosPorMesa;
    private javax.swing.JPanel fondo;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JLabel labelCantEntradas;
    private javax.swing.JLabel labelCantMesas;
    private javax.swing.JLabel labelCantMozos;
    private javax.swing.JLabel labelCantPlatos;
    private javax.swing.JLabel labelCantPlatosP;
    private javax.swing.JLabel labelCantPlatosPr;
    private javax.swing.JLabel labelCodigoConsumo10;
    private javax.swing.JLabel labelCodigoConsumo11;
    private javax.swing.JLabel labelCodigoConsumo12;
    private javax.swing.JLabel labelCodigoConsumo13;
    private javax.swing.JLabel labelCodigoConsumo14;
    private javax.swing.JLabel labelCodigoConsumo15;
    private javax.swing.JLabel labelCodigoConsumo16;
    private javax.swing.JLabel labelCodigoConsumo17;
    private javax.swing.JLabel labelCodigoConsumo18;
    private javax.swing.JLabel labelCodigoConsumo19;
    private javax.swing.JLabel labelCodigoConsumo20;
    private javax.swing.JLabel labelCodigoConsumo21;
    private javax.swing.JLabel labelCodigoConsumo22;
    private javax.swing.JLabel labelCodigoConsumo23;
    private javax.swing.JLabel labelCodigoConsumo3;
    private javax.swing.JLabel labelCodigoConsumo4;
    private javax.swing.JLabel labelCodigoConsumo5;
    private javax.swing.JLabel labelCodigoConsumo6;
    private javax.swing.JLabel labelCodigoConsumo7;
    private javax.swing.JLabel labelCodigoConsumo8;
    private javax.swing.JLabel labelCodigoConsumo9;
    private javax.swing.JLabel labelCodigoMesa1;
    private javax.swing.JLabel labelCodigoMesa2;
    private javax.swing.JLabel labelCodigoMesaConsumo;
    private javax.swing.JLabel labelCodigoMesaConsumo1;
    private javax.swing.JLabel labelCodigoMesaConsumo10;
    private javax.swing.JLabel labelCodigoMesaConsumo11;
    private javax.swing.JLabel labelCodigoMesaConsumo2;
    private javax.swing.JLabel labelCodigoMesaConsumo3;
    private javax.swing.JLabel labelCodigoMesaConsumo4;
    private javax.swing.JLabel labelCodigoMesaConsumo5;
    private javax.swing.JLabel labelCodigoMesaConsumo6;
    private javax.swing.JLabel labelCodigoMesaConsumo7;
    private javax.swing.JLabel labelCodigoMesaConsumo8;
    private javax.swing.JLabel labelCodigoMesaConsumo9;
    private javax.swing.JLabel labelCodigoMozo1;
    private javax.swing.JLabel labelConsumosTitulo;
    private javax.swing.JLabel labelDNIIng;
    private javax.swing.JLabel labelDNIIng1;
    private javax.swing.JLabel labelDomMozo;
    private javax.swing.JLabel labelDomMozo1;
    private javax.swing.JLabel labelEntrada;
    private javax.swing.JLabel labelIngreseConsumo;
    private javax.swing.JLabel labelIngreseConsumo1;
    private javax.swing.JLabel labelIngreseConsumo2;
    private javax.swing.JLabel labelIngreseConsumo3;
    private javax.swing.JLabel labelIngreseMesa;
    private javax.swing.JLabel labelIngreseMesa1;
    private javax.swing.JLabel labelIngreseMozo;
    private javax.swing.JLabel labelIngreseMozo1;
    private javax.swing.JLabel labelInicio;
    private javax.swing.JLabel labelMaximo;
    private javax.swing.JLabel labelMesasTitulo;
    private javax.swing.JLabel labelMinimo;
    private javax.swing.JLabel labelMozoAsignadoMesa;
    private javax.swing.JLabel labelMozosLibres;
    private javax.swing.JLabel labelMozosTitulo;
    private javax.swing.JLabel labelMozosTitulo1;
    private javax.swing.JLabel labelMozosTitulo2;
    private javax.swing.JLabel labelMozosTitulo3;
    private javax.swing.JLabel labelMozosTitulo4;
    private javax.swing.JLabel labelMozosTitulo5;
    private javax.swing.JLabel labelMozosTitulo6;
    private javax.swing.JLabel labelMozosTitulo7;
    private javax.swing.JLabel labelMozosTitulo8;
    private javax.swing.JLabel labelNombreMozo;
    private javax.swing.JLabel labelNombreMozo1;
    private javax.swing.JLabel labelPlatosTitulo;
    private javax.swing.JLabel labelPostre;
    private javax.swing.JLabel labelPrincipal;
    private javax.swing.JLabel labelPromedio;
    private javax.swing.JLabel labelSectorMesa;
    private javax.swing.JLabel labelSectorMesa1;
    private javax.swing.JPanel lateral;
    private javax.swing.JPanel panelAsignarMozo;
    private javax.swing.JPanel panelBanner;
    private javax.swing.JPanel panelCantTotalPlatosPorMesa;
    private javax.swing.JPanel panelConsumos;
    private javax.swing.JPanel panelCostosPlatos;
    private javax.swing.JPanel panelEliminarConsumo;
    private javax.swing.JPanel panelEliminarMesa;
    private javax.swing.JPanel panelEliminarMozo;
    private javax.swing.JPanel panelEliminarMozoMesa;
    private javax.swing.JPanel panelEliminarPlato;
    private javax.swing.JPanel panelInicio;
    private javax.swing.JPanel panelInicioFondo;
    private javax.swing.JPanel panelInicioValores;
    private javax.swing.JPanel panelInsertarConsumo1;
    private javax.swing.JPanel panelInsertarConsumo2;
    private javax.swing.JPanel panelInsertarMesa;
    private javax.swing.JPanel panelInsertarMozo;
    private javax.swing.JPanel panelInsertarPlato;
    private javax.swing.JPanel panelMesas;
    private javax.swing.JPanel panelMesasAsignadas;
    private javax.swing.JPanel panelMesasPorMozo;
    private javax.swing.JPanel panelModificarConsumo1;
    private javax.swing.JPanel panelModificarConsumo2;
    private javax.swing.JPanel panelModificarConsumo3;
    private javax.swing.JPanel panelModificarMesa1;
    private javax.swing.JPanel panelModificarMesa2;
    private javax.swing.JPanel panelModificarMozo1;
    private javax.swing.JPanel panelModificarMozo2;
    private javax.swing.JPanel panelModificarPlato1;
    private javax.swing.JPanel panelModificarPlato2;
    private javax.swing.JPanel panelMozos;
    private javax.swing.JPanel panelMozosLibres;
    private javax.swing.JPanel panelPlatos;
    private javax.swing.JPanel panelPlatosMasConsumidos;
    private javax.swing.JPanel panelPlatosNuncaConsumidos;
    private javax.swing.JPanel panelPlatosPorFecha;
    private javax.swing.JPanel panelPlatosPorMesa;
    private javax.swing.JPanel panelTablaConsumos;
    private javax.swing.JPanel panelTablaMesas;
    private javax.swing.JPanel panelTablaMozos;
    private javax.swing.JPanel panelTablaPlatos;
    private javax.swing.JSpinner spinnerHoraConsumos;
    private javax.swing.JSpinner spinnerHoraConsumosModificar;
    private javax.swing.JSpinner spinnerMinutosConsumos;
    private javax.swing.JSpinner spinnerMinutosConsumosModificar;
    private javax.swing.JPanel tabla;
    private javax.swing.JTable tablaCantMesasPorMozos;
    private javax.swing.JScrollPane tablaCantMesasPorMozoss;
    private javax.swing.JScrollPane tablaCantMesasPorMozoss1;
    private javax.swing.JScrollPane tablaCantMesasPorMozoss2;
    private javax.swing.JScrollPane tablaCantMesasPorMozoss3;
    private javax.swing.JTable tablaCantPlatos;
    private javax.swing.JTable tablaConsumos;
    private javax.swing.JScrollPane tablaConsumosScroll;
    private javax.swing.JTable tablaEliminarConsumo;
    private javax.swing.JTable tablaEliminarMesa;
    private javax.swing.JTable tablaEliminarMozo;
    private javax.swing.JTable tablaEliminarMozoMesa;
    private javax.swing.JTable tablaEliminarPlato;
    private javax.swing.JTable tablaMesas;
    private javax.swing.JTable tablaMesas3;
    private javax.swing.JTable tablaMesas4;
    private javax.swing.JTable tablaMesasConsumoModificar;
    private javax.swing.JTable tablaMesasPorMozos;
    private javax.swing.JScrollPane tablaMesasScroll;
    private javax.swing.JScrollPane tablaMesasScroll1;
    private javax.swing.JScrollPane tablaMesasScroll2;
    private javax.swing.JScrollPane tablaMesasScroll3;
    private javax.swing.JScrollPane tablaMesasScroll4;
    private javax.swing.JScrollPane tablaMesasScroll6;
    private javax.swing.JScrollPane tablaMesasScroll7;
    private javax.swing.JTable tablaModificarConsumo;
    private javax.swing.JTable tablaModificarMesa;
    private javax.swing.JTable tablaModificarMozo;
    private javax.swing.JTable tablaModificarPlato1;
    private javax.swing.JTable tablaMozoMesaModificar;
    private javax.swing.JTable tablaMozos;
    private javax.swing.JTable tablaMozos1;
    private javax.swing.JTable tablaMozos2;
    private javax.swing.JTable tablaMozosLibres;
    private javax.swing.JScrollPane tablaMozosScroll;
    private javax.swing.JScrollPane tablaMozosScroll1;
    private javax.swing.JScrollPane tablaMozosScroll2;
    private javax.swing.JTable tablaPlatos;
    private javax.swing.JTable tablaPlatos1;
    private javax.swing.JTable tablaPlatosConsumoInsertar;
    private javax.swing.JTable tablaPlatosConsumoModificar;
    private javax.swing.JTable tablaPlatosConsumoModificar2;
    private javax.swing.JTable tablaPlatosEntreFechas;
    private javax.swing.JTable tablaPlatosNunca;
    private javax.swing.JTable tablaPlatosPorMesa;
    private javax.swing.JScrollPane tablaPlatosScroll;
    private javax.swing.JTextArea textCodIngMesaD;
    private javax.swing.JTextArea textCodIngNombrePlato;
    private javax.swing.JTextArea textCodMesa;
    private javax.swing.JTextArea textCodMozo;
    private javax.swing.JTextArea textCodPlato;
    private javax.swing.JTextArea textDescMesaMod;
    private javax.swing.JTextArea textDomicilioMozoIngreso;
    private javax.swing.JTextArea textDomicilioMozoModificar;
    private javax.swing.JTextArea textNombreMozoIngreso;
    private javax.swing.JTextArea textNombreMozoModificar;
    private javax.swing.JTextArea textNombrePlato1;
    private javax.swing.JTextArea textSectorMesaIngreso;
    private javax.swing.JTextArea textSectorMesaModificar;
    private javax.swing.JButton titulo;
    // End of variables declaration//GEN-END:variables

}
