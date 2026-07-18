package Project;
import java.awt.*;
import java.awt.event.*;

public class First extends Frame implements ActionListener {
    Label l1;
    Button btn1, btn2;

    //
    public First() {
        this.setSize(500, 350);
        this.setBackground(Color.BLUE);
        setLayout(null);

        // Window close handler
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Font titleFont = new Font("SansSerif", Font.BOLD, 17);

        l1 = new Label("Welcome to PIEAS Hostel Management System");
        l1.setFont(titleFont);
        l1.setForeground(Color.YELLOW);
        l1.setBounds(40, 70, 450, 40);
        this.add(l1);

        Font buttonFont = new Font("Dialog", Font.BOLD, 16);

        btn1 = new Button("Student");
        btn1.setBounds(100, 200, 100, 50);
        btn1.setBackground(Color.GREEN);
        btn1.setFont(buttonFont);
        this.add(btn1);

        btn2 = new Button("Manager");
        btn2.setBounds(270, 200, 100, 50);
        btn2.setBackground(Color.GREEN);
        btn2.setFont(buttonFont);
        this.add(btn2);

        btn1.addActionListener(this);
        btn2.addActionListener(this);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            this.dispose();

            new StudentLogin();
        }
        if (e.getSource() == btn2) {
            this.dispose();
            new ManagerLogin();
        }
    }

}
