import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static java.sql.DriverManager.*;
import static java.sql.DriverManager.getConnection;

public class LoginForm extends JDialog{
    private JPanel LoginPanel;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnCancel;
    private JButton btnOk;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

               user = getAuthenticatedUser(email, password);

               if (user !=null){
                   dispose();
               }
               else {
                   JOptionPane.showMessageDialog( LoginForm.this,
                           "Email or Password Invalid",
                           "Try again",
                           JOptionPane.ERROR_MESSAGE);
               }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    public User user;
    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/mysecureproject";
        final String USERNAME = "root";
        final String PASSWORD = "";


        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.program = resultSet.getString("program");
                user.regnumber = resultSet.getString("regnumber");
                user.email = resultSet.getString("email");
                user.gender = resultSet.getString("gender");
                user.password = resultSet.getString("password");

            }

            stmt.close();
            conn.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
    LoginForm loginForm = new LoginForm( null);
    User user = loginForm.user;
    if (user != null) {
        System.out.println("Successful Authentication of: " + user.name);
        System.out.println("          Email: " + user.email);
        System.out.println("          Program: " + user.program);
        System.out.println("          Reg Number: " + user.regnumber);
        System.out.println("          Gender: " + user.gender);

    }
    else {
        System.out.println("Authentication canceled");
    }
    }
}




