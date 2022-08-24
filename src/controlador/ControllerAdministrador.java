/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import modelo.PersonaJpaController;
import modelo.ProductoJpaController;
import modelo.UsuarioJpaController;
import proyecto_producto.ManageFactory;
import vista.interna.VentanaPersonas;
import vista.interna.VentanaProducto;
import vista.interna.VentanaUsuario;


import vista.view_registro;

/**
 *
 * @author pc
 */
public class ControllerAdministrador extends javax.swing.JFrame  {

    view_registro vista;
    ManageFactory manager;

    public ControllerAdministrador(view_registro vista, ManageFactory manager) {
        this.vista = vista;
        this.manager = manager;
        this.vista.setExtendedState(MAXIMIZED_BOTH);
        controlEvento();
        
    }
    
    public void controlEvento(){
//        this.vista.getjCrearPersona1().addActionListener(l->cargarVistaPersona());
         vista.getjCrearPersona1().addActionListener(l -> cargarVistaPersona());
         vista.getJMenuCrearProducto().addActionListener(l -> cargarVistaProducto());
         vista.getJMenuCrearUsuario().addActionListener(l -> cargarVistaUsuario());
    }
    public static VentanaPersonas vp;
    
    public void cargarVistaPersona(){
       new ControllerPersona(vp, manager, new PersonaJpaController(manager.getEntityManagerFactory()), this.vista.getEscritorio());
       
    }
    
     public static VentanaProducto vpro;
    
    public void cargarVistaProducto(){
        new ControllerProducto(vpro, manager, new ProductoJpaController(manager.getEntityManagerFactory()), this.vista.getEscritorio());
    }
    
    public static VentanaUsuario vusua;
    
    public void cargarVistaUsuario(){
        new ControllerUsuario(vusua , manager, new UsuarioJpaController(manager.getEntityManagerFactory()), this.vista.getEscritorio());
    }
}
