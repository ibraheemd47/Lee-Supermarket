package PresentationLayer.GUI;

import PresentationLayer.Supplier.SupplierPL;
import javax.swing.*;
import java.awt.*;

public class SupplierGUI extends JFrame {
    private final SupplierPL supplierPL;

    public SupplierGUI() {
        supplierPL = new SupplierPL();
        initialize();
    }

    private void initialize() {
        setTitle("Supplier Management");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        JButton displaySuppliersButton = new JButton("Display All Suppliers");
        JButton addSupplierButton = new JButton("Add Supplier");
        JButton findSupplierButton = new JButton("Find Supplier by ID");
        JButton deleteSupplierButton = new JButton("Delete Supplier by ID");
        JButton createAgreementButton = new JButton("Create Agreement with Supplier");
        JButton updateAgreementButton = new JButton("Update Agreement");
        JButton deleteAgreementButton = new JButton("Delete Agreement");
        JButton showContactsButton = new JButton("Show Supplier Contacts");
        JButton addContactButton = new JButton("Add Contact");
        JButton viewAgreementsButton = new JButton("View Supplier Agreements");
        JButton exitButton = new JButton("Exit");

        displaySuppliersButton.addActionListener(e ->{
//            new displaysupplierGUI();
//            this.dispose();
//
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(displaySuppliersButton);
            if (currentFrame != null) {
                currentFrame.dispose();
            }

            // Create new GUI
            displaysupplierGUI gui = new displaysupplierGUI();

            JFrame frame = new JFrame("Suppliers List");
            frame.setContentPane(gui.panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Optional
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the window
            frame.setVisible(true);
            //supplierPL.displaySuppliers();
        });





        addSupplierButton.addActionListener(e -> supplierPL.handleAddingSupplier());
        findSupplierButton.addActionListener(e -> supplierPL.handlePrintingSupplier());
        deleteSupplierButton.addActionListener(e -> supplierPL.Delete_Supplier_by_ID());
        createAgreementButton.addActionListener(e -> supplierPL.creat_agreement());
        updateAgreementButton.addActionListener(e -> supplierPL.update_agreement());
        deleteAgreementButton.addActionListener(e -> supplierPL.delete_agreement());
        showContactsButton.addActionListener(e -> supplierPL.Show_contacts_of_sup());
        addContactButton.addActionListener(e -> supplierPL.add_contact());
        viewAgreementsButton.addActionListener(e -> supplierPL.display_agreements());
        exitButton.addActionListener(e -> System.exit(0));

        add(displaySuppliersButton);
        add(addSupplierButton);
        add(findSupplierButton);
        add(deleteSupplierButton);
        add(createAgreementButton);
        add(updateAgreementButton);
        add(deleteAgreementButton);
        add(showContactsButton);
        add(addContactButton);
        add(viewAgreementsButton);
        add(exitButton);
    }

    // Optional: only if you want to run directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierGUI().setVisible(true));
    }
}
