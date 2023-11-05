import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Executable;
import java.sql.*;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;

import javax.swing.*;

import com.mysql.cj.xdevapi.Result;

public class LoginForm extends JFrame {

    final private Font mainFont = new Font("Segoe print", Font.BOLD, 18);
    JTextField tfEmail;
    JPasswordField pfPassword;

    public void initialize() {
        /**************** Form Panel ****************/
        JLabel lbLoginForm = new JLabel("Login Form", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        JLabel lbEmail = new JLabel("Email");
        lbEmail.setFont(mainFont);

        tfEmail = new JTextField();
        tfEmail.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password"); // Beschreibung neben PW-Feld
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();  // Passwortfeld
        pfPassword.setFont(mainFont);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));

        /*************** Add attributes to the Form Panel ****************/
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Add some margin to the panel
        formPanel.add(lbLoginForm);
        formPanel.add(lbEmail);
        formPanel.add(tfEmail);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);

        /**************** Buttons Panel (Login & Cancel) ****************/

        /**************** Login Button ****************/
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        btnLogin.addActionListener(new ActionListener() {

            // Method which will be executed when the Login Button was clicked
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());     
                
               /* 
               /// This is the implementation without information leakage ///
               **************************************************************
                User user = getAuthenticatedUser(email, password);
                // MainFrame will be created when we click on the "Login" button
                if (user != null) {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initialize(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, 
                            "Email or Password Invalid", 
                            "Try again", 
                            JOptionPane.ERROR_MESSAGE);
                }
*/


                
                // **** SQL Injection and Information Leakage **** //
                // *********************************************** //

                try {
                    User user = getAuthenticatedUser(email, password);
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initialize(user);
                    dispose();
                } catch (EmailNotFoundException emailNotFoundException) {
                    JOptionPane.showMessageDialog(LoginForm.this, 
                    "Email does not exist. ", 
                    "Try again", 
                    JOptionPane.ERROR_MESSAGE);
                } catch (IncorrectPasswordException incorrectPasswordException) {
                    JOptionPane.showMessageDialog(LoginForm.this, 
                    "Password invalid. ", 
                    "Try again", 
                    JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception sqlException) {

                }


/*              
                /// Without information or SQL injection / Prevention ///

                // MainFrame will be created when we click on the "Login" button
                if (user != null) {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initialize(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, 
                            "Email or Password Invalid", 
                            "Try again", 
                            JOptionPane.ERROR_MESSAGE);
                } */
            }
        
            
        }); 

        /**************** Cancel Button ****************/

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(mainFont);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                dispose();
            }

        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));    // Add some Margin to the panel
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);


        /*************** General Window of Login Form **************/
        add(formPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle("Login Form");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); //when clicking on close, window will be closed
        setSize(400, 500);
        setMinimumSize(new Dimension(350, 450));
        //setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // **** SQL Injection without Information Leakage *****//
    // *************************************************** //

/*    private User getAuthenticatedUser(String email, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:8889/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "root";
    
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    
            // Vulnerable SQL query
            String sql = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
    
            if (resultSet.next()) {
                // Authentication successful, populate the user object
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }
    
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database connection or query errors here.
        }
    
        return user;
    }
*/ 
    // **** SQL Injection with Information Leakage ***** //
    // *************************************************** //
    

    private User getAuthenticatedUser(String email, String password) throws EmailNotFoundException, IncorrectPasswordException {
        User user = null; 
        //http://localhost:8888/index.php?route=/sql&pos=0&db=mystore&table=users
        final String DB_URL = "jdbc:mysql://localhost:8889/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // vulnerable SQL query
            //String sql = "SELECT * FROM users WHERE email = " + email + "'' AND password'" + password + "'";
            //ResultSet resultSet = statement.executeQuery(sql);

            Statement statement = conn.createStatement();

            String emailCheckSql = "SELECT * FROM users WHERE email = '" + email + "'";
            ResultSet emailCheckResultSet = statement.executeQuery(emailCheckSql);

            boolean emailExists = emailCheckResultSet.next();

            if (emailExists) {
                String passwordCheckSql = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "'";
                ResultSet passwordCResultSet = statement.executeQuery(passwordCheckSql);
                
                if (passwordCResultSet.next()) {
                    user = new User();
                    user.name = passwordCResultSet.getString("name");
                    user.email = passwordCResultSet.getString("email");
                    user.phone = passwordCResultSet.getString("phone");
                    user.address = passwordCResultSet.getString("address");
                    user.password = passwordCResultSet.getString("password");
                } else {
                    throw new IncorrectPasswordException("Incorrect password");
                }
            } 
            else {
                throw new EmailNotFoundException("Such an email does not exist");
            }
            
            emailCheckResultSet.close();
            statement.close();
            conn.close();

        } 
        catch(SQLException e) {
            e.printStackTrace();
        }

        return user;
    }




    // **** Without SQL Injection and Information Leakage ***** //
    // **** Prevention with prepared statements and with correct error handling **** //
    // ***************************************************************************** //
/*
    private User getAuthenticatedUser(String email, String password) {
        User user = null; 
        //http://localhost:8888/index.php?route=/sql&pos=0&db=mystore&table=users
        final String DB_URL = "jdbc:mysql://localhost:8889/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            preparedStatement.close();
            conn.close();
        } catch(Exception e) {
            System.out.println("Database connection failed!");
        }

        return user;
    }
    */

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.initialize();
    }
}