package PresentationLayer.GUI;

import ObjectDTO.Supplier.SupplierDTO;
import ServiceLayer.Inventory.Response;
import ServiceLayer.Supplier.SupplierService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class displaysupplierGUI {
    private JTable supplierTable;
    public JPanel panel1;
    private JButton Back;

    public displaysupplierGUI() {
        display();
        Back.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(Back);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                    currentFrame.dispose();
                }
                new SupplierGUI().setVisible(true);

                //Frame.getWindows()
                //this. dispose();

            }
        });
    }

    public void display() {
        System.out.println("Display Supplier GUI");
        List<SupplierDTO> suppliers=new ArrayList<>();
        // Get supplier list from the service
        SupplierService supplierService =  SupplierService.getInstance(); // adjust based on how you get the service
        Response res  =     supplierService.getAllSuppliers();
        if (res.isError())
        {
            JOptionPane.showMessageDialog(panel1,res.getMessage());
        }
        else
        {
            if (res.getMessage() ==null)// ||!(res.getMessage() instanceof List<SupplierDTO> ))
            {
                JOptionPane.showMessageDialog(panel1,"the list is null");
            }
            else{
                if(((List<SupplierDTO>)res.getMessage()).isEmpty()){
                    JOptionPane.showMessageDialog(panel1,"the list is empty");
                }
                else{
                     suppliers =(List<SupplierDTO>) res.getMessage();
                }
            }
        }

        // Define table columns
        String[] columns = {"ID", "Name", "Bank Account", "Fixed Delivery Days"};

        // Create the table model
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Fill model with supplier data
        for (SupplierDTO supplier : suppliers) {
            Object[] row = {
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getBankAccount(),
                    supplier.getFixed_delivery_days()
            };
            model.addRow(row);
        }

        // Set model to the table
        supplierTable.setModel(model);
    }

    public static JPanel main(String[] args) {
        return new displaysupplierGUI().panel1;
    }
}
