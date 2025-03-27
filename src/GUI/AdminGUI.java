package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
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
  private String currentEdit = null;
  private LinkedList<String> deleteList = new LinkedList<>();
  private static Color linkWater = new Color(237, 242, 250);

  JPanel wholePanel;
  JPanel main;

  JComboBox view;
  JComboBox sort;
  JRadioButton deleteButton;
  JRadioButton editButton;
  JCheckBox check;
  JTextField search;
  JScrollPane dataScrollPane;

  JTextField name;
  JTextField price;
  JTextField stock;
  JTextField category;
  JTextArea description;

  JTextField firstname;
  JTextField lastname;
  JTextField email;
  JTextField password;
  JTextField position;
  JTextField street;
  JTextField city;
  JTextField state;
  JTextField postalCode;
  JTextField country;
  JTextField phoneNumber;
  JTextField dateOfBirth;

  JButton deleteSelect; 
  JButton cancel; 
  JButton update; 

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

    search = Style.textField("");
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
    lowHeader.add(Style.textLabel("Search", search));
    lowHeader.setOpaque(false);

    header.add(topHeader);
    header.add(lowHeader);
    header.setBackground(linkWater);

    footer.add(deleteSelect);
    footer.setBackground(linkWater);

    wholePanel = new JPanel(new BorderLayout());
    wholePanel.setBackground(new Color(255, 255, 255));
    wholePanel.add(header, BorderLayout.NORTH);
    wholePanel.add(main, BorderLayout.CENTER);
    wholePanel.add(footer, BorderLayout.SOUTH);

    this.setLayout(new GridLayout());
    this.add(wholePanel);
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
        } else if (editButton.isSelected()) {
          pageSwitch(label[0]);
        }
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

  private JPanel editPanel(String id) {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    JPanel info = new JPanel(new BorderLayout(10, 10));
    JPanel footPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    String query = new String();

    cancel = Style.button("Cancel");
    cancel.addActionListener(this);
    update = Style.button("Save changes");
    update.addActionListener(this);

    switch (id.charAt(0)) {
        case CUSTOMER:
            query = "SELECT * FROM user u JOIN customer c ON u.userId = c.customerId WHERE userId LIKE ?";
            break;
        case STAFF:
            query = "SELECT * FROM user u JOIN staff s ON u.userId = s.staffId WHERE userId LIKE ?";
            break;
        case PRODUCT:
            query = "SELECT * FROM product p WHERE p.productId LIKE ?";
            break;
    }

    Connection connection = MySQLConnection.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, id);
        ResultSet result = preparedStatement.executeQuery();
        if (result.next()) {
          JPanel labelPanel = new JPanel(new GridLayout(0, 1, 0, 10));
          JPanel fieldPanel = new JPanel(new GridLayout(0, 1, 0, 10));

          if (id.charAt(0) == PRODUCT) {
            addLabelAndField(labelPanel, fieldPanel, "Name", name, result.getString("name"));
            addLabelAndField(labelPanel, fieldPanel, "Price", price, String.valueOf(result.getFloat("price")));
            addLabelAndField(labelPanel, fieldPanel, "Stock", stock, String.valueOf(result.getInt("stock")));
            addLabelAndField(labelPanel, fieldPanel, "CategoryId", category, result.getString("categoryId"));
            
            description = new JTextArea(result.getString("description"), 5, 20);
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(description, 
              ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            labelPanel.add(new JLabel("Description", JLabel.TRAILING));
            fieldPanel.add(scrollPane);

          } else if (id.charAt(0) == CUSTOMER || id.charAt(0) == STAFF) {
            addLabelAndField(labelPanel, fieldPanel, "Firstname", firstname, result.getString("firstname"));
            addLabelAndField(labelPanel, fieldPanel, "Lastname", lastname, result.getString("lastname"));
            addLabelAndField(labelPanel, fieldPanel, "Email", email, result.getString("email"));
            addLabelAndField(labelPanel, fieldPanel, "Password", password, result.getString("password"));
            if (id.charAt(0) == STAFF) {
              addLabelAndField(labelPanel, fieldPanel, "Position", position, result.getString("position"));
            }
            addLabelAndField(labelPanel, fieldPanel, "Street", street, result.getString("street"));
            addLabelAndField(labelPanel, fieldPanel, "City", city, result.getString("city"));
            addLabelAndField(labelPanel, fieldPanel, "State", state, result.getString("state"));
            addLabelAndField(labelPanel, fieldPanel, "Postal Code", postalCode, result.getString("postalCode"));
            addLabelAndField(labelPanel, fieldPanel, "Country", country, result.getString("country"));
            addLabelAndField(labelPanel, fieldPanel, "Phone Number", phoneNumber, result.getString("phoneNumber"));
            addLabelAndField(labelPanel, fieldPanel, "Date of Birth", dateOfBirth, String.valueOf(result.getDate("dateOfBirth")));
          }

          info.add(labelPanel, BorderLayout.WEST);
          info.add(fieldPanel, BorderLayout.CENTER);
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
    currentEdit = id;

    footPanel.add(cancel);
    footPanel.add(update);
    footPanel.setBackground(linkWater);

    panel.add(new JScrollPane(info, 
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), 
        BorderLayout.CENTER);
    panel.add(footPanel, BorderLayout.SOUTH);

    return panel;
  }

  private void addLabelAndField(JPanel sidePanel, JPanel centerPanel, String labelText, JTextField textField, String fieldText) {
      JLabel label = new JLabel(labelText, JLabel.TRAILING);
      textField = Style.textField(fieldText);
      sidePanel.add(label);
      centerPanel.add(textField);
  }

  private void pageSwitch(String id) {
    if (this.getComponent(0) == wholePanel) {
      this.remove(wholePanel);
      this.add(editPanel(id));
    } else {
      this.remove(0);
      this.add(wholePanel);
    }
    this.revalidate();
    this.repaint();
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
          deleteList.clear();
          refreshMain();
          break;
      }
    } else if (e.getSource() == cancel) {
      pageSwitch("");
    } else if (e.getSource() == update && currentEdit != null) {
      String query = new String();

      switch (currentEdit.charAt(0)) {
        case CUSTOMER:
          query = "UPDATE user SET firstname = ?, lastname = ?, email = ?, password = ?, street = ?, city = ?, state = ?, postalCode = ?, country = ?, phoneNumber = ?, dateOfBirth = ? WHERE userId = ?";
          break;
        case STAFF:
          query = "UPDATE user JOIN staff ON userId = staffId SET firstname = ?, lastname = ?, email = ?, password = ?, street = ?, city = ?, state = ?, postalCode = ?, country = ?, phoneNumber = ?, dateOfBirth = ?, position = ? WHERE userId = ?";
          break;
        case PRODUCT:
          query = "UPDATE product SET name = ?, price = ?, stock = ?, category = ?, description = ? WHERE productId = ?";
          break;
      }
      Connection connection = MySQLConnection.getConnection();
      try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        if (currentEdit.charAt(0) == PRODUCT) {
          preparedStatement.setString(1,  name.getText().trim());
          preparedStatement.setFloat(2, Float.valueOf(price.getText().trim()));
          preparedStatement.setInt(3, Integer.valueOf(stock.getText().trim()));
          preparedStatement.setString(4, category.getText().trim());
          preparedStatement.setString(5, description.getText().trim());
          preparedStatement.setString(6, currentEdit);
        } else if (currentEdit.charAt(0) == CUSTOMER || currentEdit.charAt(0) == STAFF) {
          preparedStatement.setString(1, firstname.getText().trim());
          preparedStatement.setString(2, lastname.getText().trim());
          preparedStatement.setString(3, email.getText().trim());
          preparedStatement.setString(4, password.getText().trim());
          preparedStatement.setString(5, street.getText().trim());
          preparedStatement.setString(6, city.getText().trim());
          preparedStatement.setString(7, state.getText().trim());
          preparedStatement.setString(8, postalCode.getText().trim());
          preparedStatement.setString(9, country.getText().trim());
          preparedStatement.setString(10, phoneNumber.getText().trim());
          preparedStatement.setString(11, dateOfBirth.getText().trim());
          if (currentEdit.charAt(0) == STAFF) {
            preparedStatement.setString(12, position.getText().trim());
            preparedStatement.setString(13, currentEdit);
          } else {
            preparedStatement.setString(12, currentEdit);
          }
        }

        preparedStatement.executeUpdate();
      } catch (SQLException except) {
        JOptionPane.showMessageDialog(null, "Please try again later!", "Connection failed", JOptionPane.WARNING_MESSAGE);
      } finally {
        try {
          connection.close();
        } catch (SQLException except) {
          except.printStackTrace();
        }
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

    private static JPanel textLabel (String label, JTextField text) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new Label(label), BorderLayout.WEST);
      panel.add(text, BorderLayout.CENTER);

      return panel;
    }
  }
}
