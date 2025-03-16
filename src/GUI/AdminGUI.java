package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class AdminGUI extends JPanel implements ActionListener, KeyListener {

  ImageIcon checked = new ImageIcon(
      new ImageIcon("src/GUI/Resources/checked.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  ImageIcon unchecked = new ImageIcon(
      new ImageIcon("src/GUI/Resources/unchecked.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  ImageIcon selected = new ImageIcon(
      new ImageIcon("src/GUI/Resources/selected.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  Color gray = new Color(121, 133, 165);

  JComboBox view;
  JComboBox sort;
  JRadioButton deleteButton;
  JRadioButton editButton;
  JCheckBox check;
  JTextField search;

  String prevText; // For keypress event

  AdminGUI() {
    JPanel header = new JPanel();
    JPanel topHeader = new JPanel();
    JPanel lowHeader = new JPanel();

    String[] viewList = { "Customer", "Staff", "Product" };
    view = new JComboBox(viewList);
    view.addActionListener(this);
    view.setOpaque(false);

    String[] viewSort = { "Ascending", "Descending" };
    sort = new JComboBox(viewSort);
    sort.addActionListener(this);
    sort.setOpaque(false);

    deleteButton = new JRadioButton("Delete");
    deleteButton.addActionListener(this);
    deleteButton.setOpaque(false);
    deleteButton.setIcon(unchecked);
    deleteButton.setSelectedIcon(selected);

    editButton = new JRadioButton("Edit");
    editButton.addActionListener(this);
    editButton.setOpaque(false);
    editButton.setIcon(unchecked);
    editButton.setSelectedIcon(selected);

    ButtonGroup option = new ButtonGroup();
    option.add(deleteButton);
    option.add(editButton);

    check = new JCheckBox("Select multiple");
    check.addActionListener(this);
    check.setEnabled(false);
    check.setOpaque(false);
    check.setIcon(unchecked);
    check.setSelectedIcon(checked);

    search = new JTextField("Enter to search");
    search.addKeyListener(this);
    search.setOpaque(false);

    topHeader.setLayout(new GridLayout(1, 4, 10, 0));
    topHeader.add(view);
    topHeader.add(editButton);
    topHeader.add(deleteButton);
    topHeader.add(check);
    topHeader.setOpaque(false);

    lowHeader.setLayout(new GridLayout(1, 2, 10, 0));
    lowHeader.add(sort);
    lowHeader.add(search);
    lowHeader.setOpaque(false);

    header.setLayout(new GridLayout(2, 1, 0, 10));
    header.add(topHeader);
    header.add(lowHeader);
    header.setBackground(gray);

    this.setLayout(new BorderLayout());
    this.setBackground(new Color(255, 255, 255));
    this.add(header, BorderLayout.NORTH);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == view) {
      switch (view.getSelectedIndex()) {
        case 0:
          System.out.println(view.getSelectedItem());
          break;

        case 1:
          System.out.println(view.getSelectedItem());
          break;

        case 2:
          System.out.println(view.getSelectedItem());
          break;

        default:
          break;
      }
    }

    if (e.getSource() == deleteButton) {
      System.out.println("Delete");
      check.setEnabled(true);
    } else if (e.getSource() == editButton) {
      System.out.println("Edit");
      check.setEnabled(false);
    } else if (e.getSource() == check) {
      System.out.println(check.isSelected());
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {

  }

  @Override
  public void keyReleased(KeyEvent e) {

    if (e.getSource() == search && !prevText.equals(prevText = search.getText())) {
      System.out.println(search.getText());
    }
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();

    frame.add(new AdminGUI());
    frame.setSize(500, 500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
