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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.*;

import Database.MySQLConnection;

@SuppressWarnings("rawtypes") // Remove JComboBox warning
public class AdminGUI extends JPanel implements ActionListener, KeyListener {

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

    /* -------------------- Resources -------------------- */
  
  private static ImageIcon checked = new ImageIcon(
      new ImageIcon("src/GUI/Resources/checked.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  private static ImageIcon unchecked = new ImageIcon(
      new ImageIcon("src/GUI/Resources/unchecked.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  private static ImageIcon selected = new ImageIcon(
      new ImageIcon("src/GUI/Resources/selected.png").getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
  
    /* -------------------- Variable Define -------------------- */
  private final char CUSTOMER = 'C', STAFF = 'S', PRODUCT = 'P';
  private final char DEFAULT = ' ', ASCENDING = 'A', DESCENDING = 'D';
  private char currentView = CUSTOMER, currentSort = DEFAULT;
  private String currentSearch = null;
  private LinkedList<String> deleteList = new LinkedList<>();
  private static Color linkWater = new Color(237, 242, 250);

  JPanel main;

  JComboBox view;
  JComboBox sort;
  JRadioButton deleteButton;
  JRadioButton editButton;
  JCheckBox check;
  JTextField search;
  JScrollPane dataScrollPane;

  JButton deleteSelect; 

  String prevText = new String(); // For keypress event

  AdminGUI() {
    JPanel header = new JPanel(new GridLayout(2, 1));
    JPanel topHeader = new JPanel(new GridLayout(1, 4, 10, 0));
    JPanel lowHeader = new JPanel(new GridLayout(1, 2, 10, 0));
    main = new JPanel(new BorderLayout());
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.TRAILING));

    /* -------------------- Upper Header -------------------- */
    
    String[] viewList = { "Customer", "Staff", "Product" };
    view = Style.comboBox(viewList);
    view.addActionListener(this);

    deleteButton = Style.radioButton("Delete");
    deleteButton.addActionListener(this);

    editButton = Style.radioButton("Edit");
    editButton.setSelected(true);
    editButton.addActionListener(this);

    ButtonGroup option = new ButtonGroup();
    option.add(deleteButton);
    option.add(editButton);

    check = Style.checkBox("Select multiple");
    check.addActionListener(this);
    check.setEnabled(false);

    /* -------------------- Lower Header -------------------- */
    
    String[] viewSort = { "Default", "Ascending", "Descending" };
    sort = Style.comboBox(viewSort);
    sort.addActionListener(this);

    search = Style.textField("Enter to search");
    search.addKeyListener(this);

    /* -------------------- Main -------------------- */

    main.add(new JScrollPane(mainPanel(), 
      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

    /* -------------------- Footer -------------------- */
    
    deleteSelect = Style.button("Delete");
    deleteSelect.setEnabled(false);
    deleteSelect.addActionListener(this);

    /* -------------------- Add Everything to Panel -------------------- */
    
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

  private JPanel mainPanel () {
    JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
    String query = new String();

    switch (currentView) {
      case CUSTOMER:
        query = "SELECT c.customerId as id, CONCAT(u.firstname, ' ', u.lastname) as name FROM customer c JOIN user u ON c.customerId = u.userId WHERE u.userId LIKE 'C%'";
        break;
      case STAFF:
        query = "SELECT s.staffId as id, CONCAT(u.firstname, ' ', u.lastname) as name FROM staff s JOIN user u ON s.staffId = u.userId WHERE u.userId LIKE 'S%'";
        break;
      case PRODUCT:
        query = "SELECT p.productId as id, p.name FROM product p WHERE p.productId LIKE 'P%'";
        break;
    }

    if (currentSearch != null) {
      if (currentView == CUSTOMER || currentView == STAFF) {
        query += " AND CONCAT(u.firstname, ' ', u.lastname) LIKE ?";
      } else if (currentView == PRODUCT) {
        query += " AND name LIKE ?";
      }
    }

    switch (currentSort) {
      case 'A':
        query += " ORDER BY name ASC";
        break;
    
      case 'D':
        query += " ORDER BY name DESC";
        break;
    }
    
    Connection connection = MySQLConnection.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      if (currentSearch != null) {
        preparedStatement.setString(1, '%' + currentSearch + '%');
      }
      ResultSet result = preparedStatement.executeQuery();
      for (boolean alternateColor = false; result.next(); alternateColor = !alternateColor) {
        panel.add(listPanel(new String[]{result.getString("id"), result.getString("name")}, alternateColor));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "Please try again later!", "Connection failed", JOptionPane.WARNING_MESSAGE);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return panel;
  }

  private void delete(String id) {
    String query = new String();
    StringBuilder newId = new StringBuilder(id);
    newId.setCharAt(0, 'D');

    switch (id.charAt(0)) {
      case PRODUCT:
        query = "UPDATE product SET productId = ? WHERE productId = ?";
        break;
      default:
        query = "UPDATE user u SET userId = ? WHERE userId = ?";
        break;
    }

    Connection connection = MySQLConnection.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, newId.toString());
      preparedStatement.setString(2, id);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "Please try again later!", "Connection failed", JOptionPane.WARNING_MESSAGE);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
  
  private JPanel listPanel (String[] label, boolean color) {
    JPanel style = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 0));
    for (String word : label) {
      style.add(new JLabel(word));
    }
    if (color) {
      style.setBackground(Color.LIGHT_GRAY);
    } else {
      style.setBackground(Color.WHITE);
    }
    style.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    style.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (deleteButton.isSelected()) {
          if (check.isSelected()) {
            if (!style.getBackground().equals(Color.GREEN.brighter())) {
              deleteList.add(label[0]);
              style.setBackground(Color.GREEN.brighter());
            } else if (style.getBackground().equals(Color.GREEN.brighter())) {
              if (color) {
                style.setBackground(Color.LIGHT_GRAY);
              } else {
                style.setBackground(Color.WHITE);
              }
              deleteList.remove(label[0]);
            }
          } else {
            switch (JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete Confirmation", JOptionPane.YES_NO_OPTION)) {
              case 0:
                System.out.println(label[0]);
                delete(label[0]);
                refreshMain();
                break;
            }
          }
        }
        System.out.println(deleteList);
      }

      @Override
      public void mousePressed(MouseEvent e) {


      }

      @Override
      public void mouseReleased(MouseEvent e) {


      }

      @Override
      public void mouseEntered(MouseEvent e) {
        if (!style.getBackground().equals(Color.GREEN.brighter())) {
          style.setBackground(style.getBackground().darker());
        }

      }

      @Override
      public void mouseExited(MouseEvent e) {
        if (!style.getBackground().equals(Color.GREEN.brighter())) {
          if (color) {
            style.setBackground(Color.LIGHT_GRAY);
          } else {
            style.setBackground(Color.WHITE);
          }
        }

      }
      
    });

    return style;
  }

  private void refreshMain() {
    main.revalidate();
    main.remove(0);
    main.add(new JScrollPane(mainPanel(), 
      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == view) {
      deleteList.clear();

      switch (view.getSelectedIndex()) {
        case 0:
          currentView = CUSTOMER;
          refreshMain();
          break;

        case 1:
          currentView = STAFF;
          refreshMain();
          break;

        case 2:
          currentView = PRODUCT;
          refreshMain();
          break;
      }

    } else if (e.getSource() == editButton) {
      check.setEnabled(false);
      
      if (deleteSelect.isEnabled()) {
        deleteSelect.setEnabled(!deleteSelect.isEnabled());
      }

    } else if (e.getSource() == deleteButton) {
      check.setEnabled(true);
      
      if (!deleteSelect.isEnabled() && check.isSelected()) {
        deleteSelect.setEnabled(!deleteSelect.isEnabled());
      }

    } else if (e.getSource() == check) {
      deleteSelect.setEnabled(check.isSelected());

    } else if (e.getSource() == sort) {
      
      switch (sort.getSelectedIndex()) {
        case 0:
          currentSort = DEFAULT;
          refreshMain();
          break;

        case 1:
          currentSort = ASCENDING;
          refreshMain();
          break;

        case 2:
          currentSort = DESCENDING;
          refreshMain();
          break;
      }

    } else if (e.getSource() == deleteSelect) {
      switch (JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete Confirmation", JOptionPane.YES_NO_OPTION)) {
        case 0:
          for (String id : deleteList) { delete(id); }
          refreshMain();
          break;
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

    if (e.getSource() == search ) {

      if (!prevText.equals(prevText = search.getText()) 
      && search.getText().matches("^(?=.*\\S)([a-zA-Z]+(\\s+[a-zA-Z]+)*)?$")) {

        currentSearch = search.getText();
        refreshMain();
      } else {
        currentSearch = null;
        refreshMain();
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {

  }

  @Override
  public void keyReleased(KeyEvent e) {

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
