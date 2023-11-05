import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class MainFrame extends JFrame {
    public void initialize(User user) {
        /************* Info Panel **************/
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        infoPanel.add(new JLabel("Name"));
        infoPanel.add(new JLabel(user.name));
        infoPanel.add(new JLabel("Email"));
        infoPanel.add(new JLabel(user.email));
        infoPanel.add(new JLabel("Phone"));
        infoPanel.add(new JLabel(user.phone));
        infoPanel.add(new JLabel("Address"));
        infoPanel.add(new JLabel(user.address));

        add(infoPanel, BorderLayout.NORTH);


        /************* Logout Button **************/
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe print", Font.BOLD, 18));
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the Logout action here
                LoginForm loginForm = new LoginForm();
                loginForm.initialize();
                dispose(); // Close the MainFrame
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogout);
        add(buttonPanel, BorderLayout.SOUTH);




        setTitle("Dashboard");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }   
}
