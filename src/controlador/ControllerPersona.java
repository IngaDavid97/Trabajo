/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.PersonaJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producto.ManageFactory;
import vista.interna.VentanaPersonas;



/**
 *
 * @author pc
 */
public class ControllerPersona {
   
    ModeloTablaPersona modeloTabla;
    VentanaPersonas  vista;
   ManageFactory manager;
   PersonaJpaController modeloPersona;
   Persona persona;
    JDesktopPane panelEscritorio;
    ListSelectionModel listapersonamodel;
    
    public ControllerPersona(VentanaPersonas vista, ManageFactory manager, PersonaJpaController modeloPersona, JDesktopPane panelEscritorio) {
        
        this.manager = manager;
        this.modeloPersona = modeloPersona;
        this.panelEscritorio= panelEscritorio;
        this.modeloTabla= new ModeloTablaPersona();
        this.modeloTabla.setFilas(modeloPersona.findPersonaEntities());
        if(ControllerAdministrador.vp==null){
            ControllerAdministrador.vp=new VentanaPersonas();
            this.vista = ControllerAdministrador.vp; 
            this.panelEscritorio.add(this.vista);
            this.vista.getJTablePersonas().setModel(modeloTabla);
            this.vista.show();
            
            inciar();
            
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        }else{
           ControllerAdministrador.vp.show(); 
        }
    }
    
    public void guardarPersona() {
        persona = new Persona();
        persona.setNombre(this.vista.getNombre().getText());
        persona.setApellido(this.vista.getApellido().getText());
        persona.setCedula(this.vista.getCedula().getText());
        persona.setCelular(this.vista.getCelular().getText());
        persona.setCorreo(this.vista.getCorreo().getText());
        persona.setDireccion(this.vista.getDireccion().getText());
        modeloPersona.create(persona);
        modeloTabla.agregar(persona);
      //  ModeloTablaPersona.agregar(persona);
       Resouces.success("Atención!!", "PERSONA CREADA CORRECTAMENTE");
//        JOptionPane.showMessageDialog(panelEscritorio, "PERSONA CREADA CORRECTAMENTE");
        limpiar();
    }
    
    public void editarPersona(){
        if (persona != null) {
            persona.setNombre(this.vista.getNombre().getText());
            persona.setApellido(this.vista.getApellido().getText());
            persona.setCedula(this.vista.getCedula().getText());
            persona.setCelular(this.vista.getCelular().getText());
            persona.setCorreo(this.vista.getCorreo().getText());
            persona.setDireccion(this.vista.getDireccion().getText());
            try {
                modeloPersona.edit(persona);
                modeloTabla.eliminar(persona);
                modeloTabla.actualizar(persona);
                limpiar();
            } catch (Exception ex) {
               Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
            //ModeloTablaPersona.eliminar(persona);
            Resouces.success("Atención!!", "PERSONA EDITADA CORRECTAMENTE");
//            JOptionPane.showMessageDialog(panelEscritorio, "PERSONA EDITADA CORRECTAMENTE");
        }
    }
    public void eliminarPersona(){
        if (persona != null) {
            try {
                modeloPersona.destroy(persona.getIdPersona());
                limpiar();
            }catch(NonexistentEntityException ex){
                Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
            modeloTabla.eliminar(persona);
            //ModeloTablaPersona.actualizar(persona);
               Resouces.success("Atención!!", "PERSONA ELIMINADA CORRECTAMENTE");
//               JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
        }
    }
    
     public void inciar() {
        this.vista.getBtnGuardar().addActionListener(l -> guardarPersona());
        this.vista.getBtnEditar().addActionListener(l -> editarPersona());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarPersona());
        this.vista.getJTablePersonas().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listapersonamodel = this.vista.getJTablePersonas().getSelectionModel();
        listapersonamodel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    personaSeleccionada();
                }
            }

        });
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnLimpiar().addActionListener(l -> limpiar());
        this.vista.getBtnLimpiarCriterio().addActionListener(l -> limpiarbuscador());
        this.vista.getBtnBuscar().addActionListener(l -> buscarpersona());
        
        this.vista.getCheckCriterio().addActionListener(l -> buscarpersona());
    }
    
    
    public void limpiar() {
        this.vista.getNombre().setText("");
        this.vista.getApellido().setText("");
        this.vista.getCedula().setText("");
        this.vista.getCelular().setText("");
        this.vista.getCorreo().setText("");
        this.vista.getDireccion().setText("");
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnGuardar().setEnabled(true);
        this.vista.getJTablePersonas().getSelectionModel().clearSelection();
    }
    
    public void personaSeleccionada() {
        if (this.vista.getJTablePersonas().getSelectedRow() != -1) {
            persona = modeloTabla.getFilas().get(this.vista.getJTablePersonas().getSelectedRow());
            this.vista.getNombre().setText(persona.getNombre());
            this.vista.getApellido().setText(persona.getApellido());
            this.vista.getCedula().setText(persona.getCedula());
            this.vista.getCelular().setText(persona.getCelular());
            this.vista.getCorreo().setText(persona.getCorreo());
            this.vista.getDireccion().setText(persona.getDireccion());
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnGuardar().setEnabled(false);
        }

    }

    public void buscarpersona() {
        if (this.vista.getCheckCriterio().isSelected()) {
            modeloTabla.setFilas(modeloPersona.findPersonaEntities());
            modeloTabla.fireTableDataChanged();

        } else { if (!this.vista.getCriterio().getText().equals("")) {
                modeloTabla.setFilas(modeloPersona.buscarPersona(this.vista.getCriterio().getText()));
                modeloTabla.fireTableDataChanged();
            } else {
                limpiarbuscador();
            }

        }

    }

    public void limpiarbuscador() {
        this.vista.getCriterio().setText("");
        modeloTabla.setFilas(modeloPersona.findPersonaEntities());
        modeloTabla.fireTableDataChanged();
    }
}

    
   


