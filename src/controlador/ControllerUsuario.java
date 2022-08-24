/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Dimension;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.PersonaJpaController;
import modelo.Usuario;
import modelo.UsuarioJpaController;
import proyecto_producto.ManageFactory;
import vista.interna.VentanaUsuario;


/**
 *
 * @author pc
 */
public class ControllerUsuario {
    ModeloTablaUsuario modelotabla;
    VentanaUsuario vistaUsua;
    ManageFactory manager;
    UsuarioJpaController modeloUsuario;
    Usuario usuario;
    JDesktopPane panelEscritorio;
    ListSelectionModel listaUsernaModel;
    public ControllerUsuario(VentanaUsuario vistaUsuaUsua, ManageFactory manager, UsuarioJpaController modeloUsuario, JDesktopPane panelEscritorio) {
        
        this.manager = manager;
        this.modeloUsuario = modeloUsuario;
        this.panelEscritorio = panelEscritorio;
        this.modelotabla = new ModeloTablaUsuario();
        this.modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
        if(ControllerAdministrador.vusua==null){
            ControllerAdministrador.vusua=new VentanaUsuario();
          this.vistaUsua = ControllerAdministrador.vusua; 
           this.panelEscritorio.add(this.vistaUsua);
           this.vistaUsua.getTablausuario().setModel(modelotabla);
           this.vistaUsua.show();
           iniciarControl();
           cargarCombobos();
           
           //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vistaUsua.getSize();
            this.vistaUsua.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
           
        }else{
           ControllerAdministrador.vusua.show(); 
        }
    }

    public void iniciarControl() {
        this.vistaUsua.getBtnGuardarUs().addActionListener(l -> guardarUsuario());
        this.vistaUsua.getBtnEditarUsua().addActionListener(l -> editarUsuario());
        this.vistaUsua.getBtneliminarUS().addActionListener(l -> eliminarUsuario());
        this.vistaUsua.getTablausuario().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsernaModel = this.vistaUsua.getTablausuario().getSelectionModel();
        listaUsernaModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    usuarioSeleccionado();
                }
            }

        });

        this.vistaUsua.getBtneliminarUS().setEnabled(false);
        this.vistaUsua.getBtnEditarUsua().setEnabled(false);
        this.vistaUsua.getBtnLimpiar().addActionListener(l -> limpiar());
        this.vistaUsua.getBtnLimpiarCriterio().addActionListener(l -> limpiarbuscador());
        this.vistaUsua.getBtnBuscarUsua().addActionListener(l -> buscarusuario());
        this.vistaUsua.getCheckMostrar().addActionListener(l -> buscarusuario());
    }

    //GUARDAR PERSONA
    public void guardarUsuario() {
        usuario = new Usuario();
        usuario.setUsuario(this.vistaUsua.getTxtNombreUsua().getText());
        usuario.setClave(this.vistaUsua.getTxtClaveUsua().getText());
        usuario.setIdpersona((Persona) this.vistaUsua.getCmbopersona().getSelectedItem());

        modeloUsuario.create(usuario);
        modelotabla.agregar(usuario);
        modelotabla.fireTableDataChanged();
        Resouces.success("Atención!!", "USUARIO GUARDADA CORECTAMENTE");
        
        //  JOptionPane.showMessageDialog(panelEscritorio, "PERSONA CREADA CORRECTAMENTE");
        limpiar();
    }

    //EDITAR PERSONA
    public void editarUsuario() {
        if (usuario != null) {
            usuario.setUsuario(this.vistaUsua.getTxtNombreUsua().getText());
            usuario.setClave(this.vistaUsua.getTxtClaveUsua().getText());
            usuario.setIdpersona((Persona) this.vistaUsua.getCmbopersona().getSelectedItem());
            Resouces.success("Atención!!", "USUARIO EDITADA CORECTAMENTE");
            try {
                modeloUsuario.edit(usuario);
                modelotabla.eliminar(usuario);
                modelotabla.actualizar(usuario);
                limpiar();
            } catch (Exception ex) {

                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    //ELIMINAR PERSONA
    public void eliminarUsuario() {
        if (usuario != null) {
            try {
                modeloUsuario.destroy(usuario.getIdusuario());
                limpiar();
            } catch (Exception ex) {
                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
                limpiar();
            }
            modelotabla.eliminar(usuario);
            //  JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
             Resouces.success("ALERTA!!", "USUARIO ELIMINADO CORECTAMENTE");
        }
    }

    public void limpiar() {
        this.vistaUsua.getTxtNombreUsua().setText("");
        this.vistaUsua.getTxtClaveUsua().setText("");
        this.vistaUsua.getCmbopersona().setSelectedItem(0);

        this.vistaUsua.getBtneliminarUS().setEnabled(false);
        this.vistaUsua.getBtnEditarUsua().setEnabled(false);
        this.vistaUsua.getBtnGuardarUs().setEnabled(true);
        this.vistaUsua.getTablausuario().getSelectionModel().clearSelection();
    }

    public void usuarioSeleccionado() {
        if (this.vistaUsua.getTablausuario().getSelectedRow() != -1) {
            usuario = modelotabla.getFilas().get(this.vistaUsua.getTablausuario().getSelectedRow());
            this.vistaUsua.getTxtNombreUsua().setText(usuario.getUsuario());
            this.vistaUsua.getTxtClaveUsua().setText(usuario.getClave());
            this.vistaUsua.getCmbopersona().setSelectedItem(usuario.getIdpersona());
            //
            this.vistaUsua.getBtneliminarUS().setEnabled(true);
            this.vistaUsua.getBtnEditarUsua().setEnabled(true);
            this.vistaUsua.getBtnGuardarUs().setEnabled(false);
        }
    }

    public void limpiarbuscador() {
        this.vistaUsua.getTxtCriterioUsua().setText("");
        modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
        modelotabla.fireTableDataChanged();
    }

    public void cargarCombobos() {
        PersonaJpaController modeloPersona= new PersonaJpaController(manager.getEntityManagerFactory());
        try {
            Vector v = new Vector();
            v.addAll(modeloPersona.findPersonaEntities());
            this.vistaUsua.getCmbopersona().setModel(new DefaultComboBoxModel(v));

        } catch (ArrayIndexOutOfBoundsException ex) {

            System.out.println("ERROR");
        }
    }

    public void buscarusuario() {
        if (this.vistaUsua.getCheckMostrar().isSelected()) {
            modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
            modelotabla.fireTableDataChanged();
            limpiarbuscador();
           // System.out.println("llego");
        } else {
            if (!this.vistaUsua.getTxtCriterioUsua().getText().equals("")) {
                modelotabla.setFilas(modeloUsuario.buscarUsuario(this.vistaUsua.getTxtCriterioUsua().getText()));
                modelotabla.fireTableDataChanged();
              //  System.out.println("llego2");
            } else {

            }

        }

    }

    
   
}
