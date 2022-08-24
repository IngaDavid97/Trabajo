/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producto;

import controlador.ControllerLogin;
import modelo.UsuarioJpaController;
import vista.view_login;

/**
 *
 * @author pc
 */
public class proyecto_producto {
    public static void main (String[] args){
        ManageFactory manager = new ManageFactory();
        view_login vista = new view_login();
        UsuarioJpaController modelo= new UsuarioJpaController(manager.getEntityManagerFactory());
        ControllerLogin controlador=new ControllerLogin(manager,vista,modelo);
    }
}
