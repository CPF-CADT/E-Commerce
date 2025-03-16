package Testing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CusGUI{
    private JFrame frame;
    private DefaultListModel<String> producListModel;
    private DefaultListModel<String> cartListModel;
    private JList<String> producList;
    private JList<String> cartList;

    public CusGUI(){
        frame = new JFrame("Costumer Shop");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2));

        JPanel productPanel = new JPanel(new BorderLayout());
        producListModel = new DefaultListModel<>();
        producList = new JList<>(producListModel);
        productPanel.add(new JLabel("Avaible Prodcuts"), BorderLayout.NORTH);
        productPanel.add(new JScrollPane(producList), BorderLayout.CENTER);
        JButton addToCartButton = new JButton("Add to Cart");
        productPanel.add(addToCartButton, BorderLayout.SOUTH);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartListModel = new DefaultListModel<>();   
        cartList = new JList<>(cartListModel);
        cartPanel.add(new JLabel("Your Cart"), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        JButton checkoutButton = new JButton("Check Out");
        cartPanel.add(checkoutButton, BorderLayout.SOUTH);

        frame.add(productPanel);
        frame.add(cartPanel);


        producListModel.addElement("Laptop - $1000");
        producListModel.addElement("Iphone - $1000");
        producListModel.addElement("Samsung - $1000");
        producListModel.addElement("Smartwatch - $1000");

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartListModel.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Your cart is empty!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Checkout successful! Thank you for shopping.");
                    cartListModel.clear();
                }
            }
        });
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new CusGUI();
    }

    
}