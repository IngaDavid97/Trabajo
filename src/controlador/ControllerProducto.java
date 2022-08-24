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
import modelo.Producto;
import modelo.ProductoJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producto.ManageFactory;
import vista.interna.VentanaProducto;

/**
 *
 * @author pc
 */
public class ControllerProducto {

    ModeloTablaProducto modeloTabla;
    VentanaProducto vistaPro;
    ManageFactory manager;
    ProductoJpaController modeloProducto;
    Producto producto;
    JDesktopPane panelEscritorio;
    ListSelectionModel listaproductomodel;

    public ControllerProducto(VentanaProducto vistaPro, ManageFactory manager, ProductoJpaController modeloProducto, JDesktopPane panelEscritorio) {
        this.manager = manager;
        this.modeloProducto = modeloProducto;
        this.panelEscritorio = panelEscritorio;
        this.modeloTabla = new ModeloTablaProducto();
        this.modeloTabla.setFilas(modeloProducto.findProductoEntities());
        if (ControllerAdministrador.vpro == null) {
            ControllerAdministrador.vpro = new VentanaProducto();
            this.vistaPro = ControllerAdministrador.vpro;
            this.panelEscritorio.add(this.vistaPro);
            this.vistaPro.getjTableProducto().setModel(modeloTabla);
            this.vistaPro.show();
            iniciar();

            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vistaPro.getSize();
            this.vistaPro.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        } else {
            ControllerAdministrador.vpro.show();
        }
    }   
        

    public void guardarProducto() {
        producto = new Producto();
        producto.setNombre(this.vistaPro.getTxtNombrePro().getText());

        long precio = Long.parseLong(this.vistaPro.getTxtPrecio().getText());
        producto.setPrecio(precio);

        Integer cantidad = Integer.parseInt(this.vistaPro.getTxtCantidad().getText());
        producto.setCantidad(cantidad);
        modeloProducto.create(producto);
        modeloTabla.agregar(producto);
        //  ModeloTablaPersona.agregar(producto);
        Resouces.success("Atención!!", "PRODUCTO CREADA CORRECTAMENTE");
//        JOptionPane.showMessageDialog(panelEscritorio, "PRODUCTO CREADA CORRECTAMENTE");
        limpiar();
    }

    public void editarProducto() {
        if (producto != null) {
            producto.setNombre(this.vistaPro.getTxtNombrePro().getText());
            
            
            
            long precio = Long.parseLong(this.vistaPro.getTxtPrecio().getText());
            producto.setPrecio(precio);

            Integer cantidad = Integer.parseInt(this.vistaPro.getTxtCantidad().getText());
            producto.setCantidad(cantidad);
            try {
                modeloProducto.edit(producto);
                modeloTabla.eliminar(producto);
                modeloTabla.actualizar(producto);
                limpiar();
            } catch (Exception ex) {
                Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
            //ModeloTablaPersona.eliminar(producto);
            Resouces.success("Atención!!", "PRODUCTO EDITADO CORRECTAMENTE");
//            JOptionPane.showMessageDialog(panelEscritorio, "PRODUCTO EDITADA CORRECTAMENTE");
        }
    }

    public void eliminarProducto() {
        if (producto != null) {
            try {
                modeloProducto.destroy(producto.getIdproducto());
                limpiar();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
            modeloTabla.eliminar(producto);
            //ModeloTablaPersona.actualizar(producto);
            Resouces.success("Atención!!", "PRODUCTO ELIMINADO CORRECTAMENTE");
//            JOptionPane.showMessageDialog(panelEscritorio, "PRODUCTO ELIMINADA CORRECTAMENTE");
        }
    }

    public void iniciar() {
        this.vistaPro.getBtnGuardarPro().addActionListener(l -> guardarProducto());
       
        this.vistaPro.getBtnEditarPro().addActionListener(l -> editarProducto());
        this.vistaPro.getBtnBorrar().addActionListener(l -> eliminarProducto());
        this.vistaPro.getjTableProducto().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaproductomodel = this.vistaPro.getjTableProducto().getSelectionModel();
        listaproductomodel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    productoSeleccionado();
                }
            }

        });
        this.vistaPro.getBtnBorrar().setEnabled(false);
        this.vistaPro.getBtnEditarPro().setEnabled(false);
        this.vistaPro.getBtnLimpiarPro().addActionListener(l -> limpiar());
        this.vistaPro.getBtnLimpiarCriterio().addActionListener(l -> limpiarbuscador());
        this.vistaPro.getBtnBuscarCriterioPro().addActionListener(l -> buscarproducto());

        this.vistaPro.getCheckCriterioPro().addActionListener(l -> buscarproducto());
    }

    public void limpiar() {
        this.vistaPro.getTxtNombrePro().setText("");
        this.vistaPro.getTxtCantidad().setText("");
        this.vistaPro.getTxtPrecio().setText("");
        this.vistaPro.getBtnBorrar().setEnabled(false);
        this.vistaPro.getBtnEditarPro().setEnabled(false);
        this.vistaPro.getBtnGuardarPro().setEnabled(true);
        this.vistaPro.getjTableProducto().getSelectionModel().clearSelection();
    }

    public void productoSeleccionado() {
        if (this.vistaPro.getjTableProducto().getSelectedRow() != -1) {
            producto = modeloTabla.getFilas().get(this.vistaPro.getjTableProducto().getSelectedRow());

            this.vistaPro.getTxtNombrePro().setText(producto.getNombre());
            this.vistaPro.getTxtCantidad().setText(producto.getCantidad().toString());
            this.vistaPro.getTxtPrecio().setText(producto.getPrecio().toString());

            this.vistaPro.getBtnBorrar().setEnabled(true);
            this.vistaPro.getBtnEditarPro().setEnabled(true);
            this.vistaPro.getBtnGuardarPro().setEnabled(false);
        }

    }

    public void buscarproducto() {
        if (this.vistaPro.getCheckCriterioPro().isSelected()) {
            modeloTabla.setFilas(modeloProducto.findProductoEntities());
            modeloTabla.fireTableDataChanged();

        } else {
            if (!this.vistaPro.getCriterioPro().getText().equals("")) {
                modeloTabla.setFilas(modeloProducto.buscarProducto(this.vistaPro.getCriterioPro().getText()));
                modeloTabla.fireTableDataChanged();
            } else {
                limpiarbuscador();
            }

        }

    }

    public void limpiarbuscador() {
        this.vistaPro.getCriterioPro().setText("");
        modeloTabla.setFilas(modeloProducto.findProductoEntities());
        modeloTabla.fireTableDataChanged();
    }


}
