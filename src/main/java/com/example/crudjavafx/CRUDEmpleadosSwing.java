package com.example.crudjavafx;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CRUDEmpleadosSwing {
    private JPanel panelPrincipal;
    private JTable tablaEmpleados;
    private JTextField idText;
    private JTextField nombreText;
    private JTextField puestoText;
    private JTextField sueldoText;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private DefaultTableModel tablaModelo;
    private JFrame frame;

    private ManejadorArrayListEmpleado manejadorEmpleados;

    public CRUDEmpleadosSwing() {
        manejadorEmpleados = new ManejadorArrayListEmpleado();
        guardarButton.addActionListener(e -> guardarEmpleado());
        eliminarButton.addActionListener(e -> eliminarEmpleado());
        limpiarButton.addActionListener(e -> limpiarForm());

        tablaEmpleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarSeleccionado();
            }
        });
        recargarDatos();
        nombreText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() ==KeyEvent.VK_ENTER){
                    puestoText.requestFocus();
                }
            }
        });
        puestoText.addActionListener(e -> sueldoText.requestFocus());
        sueldoText.addActionListener(e -> guardarEmpleado())

        ;
    }

    private void cargarSeleccionado() {
        var renglon = tablaEmpleados.getSelectedRow();
        if(renglon != -1){
            // -1 ssignifica que no se seleccionó un renglón
            var id = tablaEmpleados.getModel().getValueAt(renglon,0).toString();
            var nombre = tablaEmpleados.getModel().getValueAt(renglon,1).toString();
            var puesto = tablaEmpleados.getModel().getValueAt(renglon,2).toString();
            var salario = tablaEmpleados.getModel().getValueAt(renglon,3).toString();

            idText.setText(id);
            nombreText.setText(nombre);
            puestoText.setText(puesto);
            sueldoText.setText(salario);
        }
    }

    private void limpiarForm() {
        // limpiar el texto de los TextField
        idText.setText(String.valueOf(manejadorEmpleados.getSiguienteId()));
        nombreText.setText("");
        puestoText.setText("");
        sueldoText.setText("");

        // deseleccionar el renglón seleccionado en la tabla
        tablaEmpleados.getSelectionModel().clearSelection();
    }

    private void eliminarEmpleado() {
        int idEliminar = Integer.parseInt(idText.getText());
        if(idEliminar != manejadorEmpleados.getSiguienteId()){
            manejadorEmpleados.eliminar(idEliminar);
            recargatTabla();
            limpiarForm();
        }
        else{
            JOptionPane.showMessageDialog(frame, "Seleccione un empleado para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardarEmpleado() {
        String nombre = nombreText.getText();
        String puesto = puestoText.getText();
        String salarioStr = sueldoText.getText();
        if (nombre.isEmpty()){
            JOptionPane.showMessageDialog(frame, "Debe ingresar un nombre", "Error", JOptionPane.ERROR_MESSAGE);
            nombreText.requestFocus();
        } else if (puesto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Debe ingresar un puesto", "Error", JOptionPane.ERROR_MESSAGE);
            puestoText.requestFocus();
        }
        double salario;

        try{
            salario = Double.parseDouble(salarioStr);

        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(frame,"El salario debe ser un numero valido", "Error",JOptionPane.ERROR_MESSAGE);
            sueldoText.requestFocus();
            return;
        }

        int idGuardar = Integer.parseInt(idText.getText());
        if(idGuardar==manejadorEmpleados.getSiguienteId()){
            manejadorEmpleados.insertar(nombre, puesto, salario);
        }
        else{ // significa que se está actualizando un empleado existente
            manejadorEmpleados.actualizar(idGuardar, nombre, puesto, salario);
        }

        recargarDatos();
    }

    private void recargatTabla(){
        tablaModelo.setRowCount(0); // limpia la tabla
        for(Empleado empleado: manejadorEmpleados.getEmpleados()){
            Object fila[] = {
                    empleado.getId(),
                    empleado.getNombre(),
                    empleado.getPuesto(),
                    empleado.getSalario()
            };
            tablaModelo.addRow(fila);
        }
    }

    private void recargarDatos(){
        recargatTabla();
        limpiarForm();
        //manejadorEmpleados.cargarCSV();
    }

    public void mostrar(){
        frame = new JFrame("Aplicación CRUD Emlpeados"); // crear la ventana principal con título
        frame.setContentPane(panelPrincipal); // se agrega el panel principal a la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack(); // ajusta la ventana al tamaño de los componentes
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ImageIcon icono = new ImageIcon(CRUDEmpleadosSwing.class.getResource("/mongus.png"));
        frame.setIconImage(icono.getImage());
    }

    public static void main(String[] args){
        try{
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
                if ("Nimbus".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        new CRUDEmpleadosSwing().mostrar();


    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.tablaModelo = new DefaultTableModel(0,4){
            @Override //evitar la edición directa en la tabla
            public boolean isCellEditable(int row, int column){
                return false; // Bloquea la edición
            }
        };
        String[] encabezados = {"ID", "Nombre", "Puesto", "Salario"};
        this.tablaModelo.setColumnIdentifiers(encabezados);
        this.tablaEmpleados = new JTable(tablaModelo);
        this.tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] datos = {"1", "JBROM", "REAL NINJA", "5"};
        // tablaModelo.addRow(datos);
        // tablaModelo.addRow(datos);
    }
}
