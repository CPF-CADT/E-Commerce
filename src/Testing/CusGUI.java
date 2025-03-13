package Testing;

import java.awt.*;
import javax.swing.*;

public class CusGUI{
    private JFrame frame;
    private DefaultListModel<String> producListModel;
    private DefaultListModel<String> cartListModel;
    private JList<String> producList;
    private JList<String> cartList;

    public static void main(String[] args) {
        new CusGUI();
    }

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
    }

    
}