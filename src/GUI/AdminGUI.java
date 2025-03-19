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

@SuppressWarnings("rawtypes") // Remove JComboBox warning
public class AdminGUI extends JPanel implements ActionListener, KeyListener {

    /* -------------------- Resources -------------------- */
  
  private static ImageIcon checked = new ImageIcon(
      new ImageIcon("src/GUI/Resources/checked.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  private static ImageIcon unchecked = new ImageIcon(
      new ImageIcon("src/GUI/Resources/unchecked.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  private static ImageIcon selected = new ImageIcon(
      new ImageIcon("src/GUI/Resources/selected.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  
  private static Color linkWater = new Color(237, 242, 250);

  JComboBox view;
  JComboBox sort;
  JRadioButton deleteButton;
  JRadioButton editButton;
  JCheckBox check;
  JTextField search;

  

  JButton deleteSelect; 

  String prevText = new String(); // For keypress event

  AdminGUI() {
    JPanel header = new JPanel(new GridLayout(2, 1));
    JPanel topHeader = new JPanel(new GridLayout(1, 4, 10, 0));
    JPanel lowHeader = new JPanel(new GridLayout(1, 2, 10, 0));
    JPanel main = new JPanel(new GridLayout(0, 1, 0, 10));
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    

    /* -------------------- Upper Header -------------------- */
    
    String[] viewList = { "Customer", "Staff", "Product" };
    view = Style.comboBox(viewList);
    view.addActionListener(this);

    deleteButton = Style.radioButton("Delete");
    deleteButton.addActionListener(this);

    editButton = Style.radioButton("Edit");
    editButton.addActionListener(this);

    ButtonGroup option = new ButtonGroup();
    option.add(deleteButton);
    option.add(editButton);

    check = Style.checkBox("Select multiple");
    check.addActionListener(this);
    check.setEnabled(false);

    /* -------------------- Lower Header -------------------- */
    
    String[] viewSort = { "Ascending", "Descending" };
    sort = Style.comboBox(viewSort);
    sort.addActionListener(this);

    search = Style.textField("Enter to search");
    search.addKeyListener(this);

    /* -------------------- Main -------------------- */

    main.setBackground(Color.CYAN);

    /* -------------------- Footer -------------------- */
    
    deleteSelect = Style.button("Delete");
    deleteSelect.setEnabled(false);
    deleteSelect.addActionListener(this);

    topHeader.add(view);
    topHeader.add(editButton);
    topHeader.add(deleteButton);
    topHeader.add(check);
    topHeader.setOpaque(false);

    lowHeader.add(sort);
    lowHeader.add(search);
    lowHeader.setOpaque(false);

    header.add(topHeader);
    header.add(lowHeader);
    header.setBackground(linkWater);

    footer.add(deleteSelect);
    footer.setBackground(linkWater);

    this.setLayout(new BorderLayout());
    this.setBackground(new Color(255, 255, 255));
    this.add(header, BorderLayout.NORTH);
    this.add(main, BorderLayout.CENTER);
    this.add(footer, BorderLayout.SOUTH);
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

    } else if (e.getSource() == editButton) {
      check.setEnabled(false);
      System.out.println("Edit");
      
      if (deleteSelect.isEnabled()) {
        deleteSelect.setEnabled(!deleteSelect.isEnabled());
      }

    } else if (e.getSource() == deleteButton) {
      check.setEnabled(true);
      System.out.println("Delete");
      
      if (!deleteSelect.isEnabled() && check.isSelected()) {
        deleteSelect.setEnabled(!deleteSelect.isEnabled());
      }

    } else if (e.getSource() == check) {
      deleteSelect.setEnabled(check.isSelected());
      System.out.println(check.isSelected());

    } else if (e.getSource() == sort) {
      switch (sort.getSelectedIndex()) {
        case 0:
          System.out.println(sort.getSelectedItem());
          break;

        case 1:
          System.out.println(sort.getSelectedItem());
          break;

        default:
          break;
      }

    } else if (e.getSource() == deleteSelect) {
      System.out.println(deleteSelect.getText());
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

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        
        JFrame frame = new JFrame("Admin panel");
    
        frame.add(new AdminGUI());
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });
  }

  private static class Style {
    
    private static JComboBox comboBox (String[] list) {
      JComboBox style = new JComboBox<>(list);
      style.setOpaque(false);

      return style;
    }

    private static JRadioButton radioButton (String label) {
      JRadioButton style = new JRadioButton(label);
      style.setOpaque(false);
      style.setIcon(AdminGUI.unchecked);
      style.setSelectedIcon(AdminGUI.selected);

      return style;
    }

    private static JCheckBox checkBox (String label) {
      JCheckBox style = new JCheckBox(label);
      style.setOpaque(false);
      style.setIcon(AdminGUI.unchecked);
      style.setSelectedIcon(AdminGUI.checked);

      return style;
    }

    private static JButton button (String label) {
      JButton style = new JButton(label);
      style.setOpaque(false);

      return style;
    }

    private static JTextField textField (String label) {
      JTextField style = new JTextField(label);
      style.setOpaque(false);

      return style;
    }
  }
}
