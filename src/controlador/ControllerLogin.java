/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import modelo.Usuario;
import modelo.UsuarioJpaController;
import proyecto_producto.ManageFactory;
import vista.view_login;
import vista.view_registro;

/**
 *
 * @author pc
 */
public class ControllerLogin {

    private ManageFactory manager;
    private view_login vista;
    private view_registro vista_ad;
    private UsuarioJpaController modelo;

    public ControllerLogin() {
    }

    public ControllerLogin( view_registro vista_ad) {   
        this.vista_ad = vista_ad;
    }
    
    public ControllerLogin(ManageFactory manager, view_login vista, UsuarioJpaController modelo) {
        this.manager = manager;
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        iniciarControl();

    }

    public void iniciarControl() {
        vista.getBtnEntrar().addActionListener(l->controlLogin());
        vista.getBtnCerrar().addActionListener(l->System.exit(0));
        
    }

    public void controlLogin() {
        String usuario = vista.getTxtUsuario().getText();
        String clave = new String(vista.getjPas_Contra().getPassword());

        Usuario user = modelo.buscarUsuario(usuario, clave);
        try{
           if (user != null) {
            JOptionPane.showMessageDialog(vista, "Usuario correcto"  + user.getIdpersona().toString());
             view_registro vista_a=new view_registro();
                        
                        new ControllerAdministrador(vista_a, manager);
                        vista_a.setVisible(true);
                        vista.setVisible(false);
                        
            
        } else {
            JOptionPane.showMessageDialog(vista, "Usuario incorrecto");
        }   
        }catch(PersistenceException e){
            JOptionPane.showMessageDialog(vista, "No existe conexion con la base de datos");
        }
      
    }

}
