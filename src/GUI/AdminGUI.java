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

import javax.swing.*;

import Database.MySQLConnection;

@SuppressWarnings("rawtypes") // Remove JComboBox warning
public class AdminGUI extends JPanel implements ActionListener, KeyListener, MouseListener {

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

    main.add(new JScrollPane(mainPanel(currentView, currentSort, currentSearch), 
      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

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

  private JPanel mainPanel (char option, char sortBy, String searchString) {
    JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
    String query = new String();

    switch (option) {
      case CUSTOMER:
        query = "SELECT c.customerId as id, CONCAT(u.firstname, ' ', u.lastname) as name FROM customer c JOIN user u ON c.customerId = u.userId";
        break;
      case STAFF:
        query = "SELECT s.staffId as id, CONCAT(u.firstname, ' ', u.lastname) as name FROM staff s JOIN user u ON s.staffId = u.userId";
        break;
      case PRODUCT:
        query = "SELECT p.productId as id, p.name FROM product p";
        break;
    }

    if (searchString != null) {
      if (option == CUSTOMER || option == STAFF) {
        query += " WHERE CONCAT(u.firstname, ' ', u.lastname) LIKE ?";
      } else if (option == PRODUCT) {
        query += " WHERE name LIKE ?";
      }
    }

    switch (sortBy) {
      case 'A':
        query += " ORDER BY name ASC";
        break;
    
      case 'D':
        query += " ORDER BY name DESC";
        break;
    }
    
    Connection connection = MySQLConnection.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      if (searchString != null 
      && ((option == CUSTOMER || option == STAFF) 
      || (option == PRODUCT))) {
        preparedStatement.setString(1, '%' + searchString + '%');
      }
      ResultSet result = preparedStatement.executeQuery();
      System.out.println(preparedStatement);
      for (boolean alternateColor = false; result.next(); alternateColor = !alternateColor) {
        JPanel list = Style.listPanel(new String[]{result.getString("id"), result.getString("name")}, alternateColor);
        list.addMouseListener(null);
        panel.add(list);
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

  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == view) {
      main.revalidate();
      main.remove(0);
      
      switch (view.getSelectedIndex()) {
        case 0:
          main.add(new JScrollPane(mainPanel(currentView = CUSTOMER, currentSort, currentSearch), 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
          break;

        case 1:
          main.add(new JScrollPane(mainPanel(currentView = STAFF, currentSort, currentSearch), 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
          break;

        case 2:
          main.add(new JScrollPane(mainPanel(currentView = PRODUCT, currentSort, currentSearch), 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
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
      main.revalidate();
      main.remove(0);
      
      switch (sort.getSelectedIndex()) {
        case 0:
          main.add(new JScrollPane(mainPanel(currentView, currentSort = DEFAULT, currentSearch), 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
          break;

        case 1:
          main.add(new JScrollPane(mainPanel(currentView, currentSort = ASCENDING, currentSearch), 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
          break;

        case 2:
          main.add(new JScrollPane(mainPanel(currentView, currentSort = DESCENDING, currentSearch), 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
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

    if (e.getSource() == search ) {
      main.revalidate();
      if (!prevText.equals(prevText = search.getText()) 
      && search.getText().matches("^(?=.*\\S)([a-zA-Z]+(\\s+[a-zA-Z]+)*)?$")) {
        main.remove(0);
        main.add(new JScrollPane(mainPanel(currentView, currentSort, currentSearch = search.getText()), 
          ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
      } else if (!search.getText().matches("^(?=.*\\S)([a-zA-Z]+(\\s+[a-zA-Z]+)*)?$")) {
        main.remove(0);
        main.add(new JScrollPane(mainPanel(currentView, currentSort, currentSearch = null), 
          ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    

  }

  @Override
  public void mousePressed(MouseEvent e) {
    

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    

  }

  @Override
  public void mouseExited(MouseEvent e) {
    

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

    private static JPanel listPanel (String[] label, boolean color) {
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
          style.setBackground(Color.GREEN);

        }

        @Override
        public void mousePressed(MouseEvent e) {


        }

        @Override
        public void mouseReleased(MouseEvent e) {


        }

        @Override
        public void mouseEntered(MouseEvent e) {
          style.setBackground(style.getBackground().darker());

        }

        @Override
        public void mouseExited(MouseEvent e) {
          if (color) {
            style.setBackground(Color.LIGHT_GRAY);
          } else {
            style.setBackground(Color.WHITE);
          }

        }
        
      });

      return style;
    }
  }
}
