package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class MessMenuPanel extends Panel implements ActionListener {

    Panel dataContainer;
    CardLayout cardLayout;
    Panel mainContent;
    Button viewMenuBtn, editMenuBtn;

    // days of the week
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    // TextAreas for each day so manager can type menu
    TextArea[] menuAreas = new TextArea[7];

    // Labels to display menu in view mode
    Label[] menuLabels = new Label[7];

    // dummy data for display
    String[] dummyMenus = {
            "Breakfast: Paratha, Chai\n Lunch: Daal Chawal\n Dinner: Karahi",
            "Breakfast: Eggs, Toast\n Lunch: Biryani\n Dinner: Qorma",
            "Breakfast: Halwa Puri\n Lunch: Chana Chawal\n Dinner: BBQ",
            "Breakfast: Paratha, Chai\n Lunch: Pulao\n Dinner: Daal Makhani",
            "Breakfast: Cereal\n Lunch: Fried Rice\n Dinner: Nihari",
            "Breakfast: Omelette\n Lunch: Pasta\n Dinner: Sajji",
            "Breakfast: Halwa Puri\n Lunch: Mutton Karahi\n Dinner: Seekh Kabab"
    };

    public MessMenuPanel(boolean isManager) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 247, 250));

        // ── Top bar ──────────────────────────────────────────
        Panel topBar = new Panel(new GridLayout(1, 2, 10, 0));
        topBar.setBackground(new Color(44, 62, 80));

        Font btnFont = new Font("SansSerif", Font.BOLD, 16);

        viewMenuBtn = new Button("View Menu");
        viewMenuBtn.setFont(btnFont);
        viewMenuBtn.setBackground(new Color(44, 62, 80));
        viewMenuBtn.setForeground(Color.WHITE);

        editMenuBtn = new Button("Edit Menu");
        editMenuBtn.setFont(btnFont);
        editMenuBtn.setBackground(new Color(44, 62, 80));
        editMenuBtn.setForeground(Color.WHITE);

        topBar.add(viewMenuBtn);
        // only show edit button to manager
        if (isManager) {
            topBar.add(editMenuBtn);
        }

        this.add(topBar, BorderLayout.NORTH);

        // ── CardLayout for view and edit panels ───────────────
        cardLayout    = new CardLayout();
        mainContent   = new Panel(cardLayout);

        mainContent.add(buildViewPanel(),  "VIEW");
        mainContent.add(buildEditPanel(),  "EDIT");

        this.add(mainContent, BorderLayout.CENTER);

        cardLayout.show(mainContent, "VIEW");

        viewMenuBtn.addActionListener(this);
        editMenuBtn.addActionListener(this);
    }

    // ── View Panel — shows menu for each day ─────────────────
    private Panel buildViewPanel() {
        Panel view = new Panel(new BorderLayout());
        view.setBackground(new Color(245, 247, 250));

        Panel title = new Panel();
        Label titleLbl = new Label("Weekly Mess Menu", Label.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLbl.setForeground(new Color(44, 62, 80));
        title.add(titleLbl);
        title.setBackground(new Color(245, 247, 250));
        view.add(title, BorderLayout.NORTH);

        // 7 day cards in a scrollable container
        Panel cardsContainer = new Panel();
        cardsContainer.setLayout(new GridLayout(7, 1, 0, 10));
        cardsContainer.setBackground(new Color(245, 247, 250));

        for (int i = 0; i < 7; i++) {
            cardsContainer.add(buildDayViewCard(i));
        }

        ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scroll.add(cardsContainer);
        view.add(scroll, BorderLayout.CENTER);

        return view;
    }

    // ── One day card for view mode ────────────────────────────
    private Panel buildDayViewCard(int index) {
        Panel card = new Panel(new BorderLayout(10, 5));
        card.setBackground(index % 2 == 0 ? Color.WHITE : new Color(235, 240, 248));

        Label dayLbl = new Label(days[index], Label.CENTER);
        dayLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        dayLbl.setForeground(new Color(44, 62, 80));
        dayLbl.setPreferredSize(new Dimension(120, 60));
        card.add(dayLbl, BorderLayout.WEST);

        // load from DB, fall back to dummy if not found
        String menuText = dummyMenus[index];
        try (Connection conn = DatabaseManager.connect()) {
            String query = "SELECT Menu FROM MessMenu WHERE Day = ?";
            PreparedStatement ptmpt = conn.prepareStatement(query);
            ptmpt.setString(1, days[index]);
            ResultSet rs = ptmpt.executeQuery();
            if (rs.next()) {
                menuText = rs.getString("Menu");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        menuLabels[index] = new Label(menuText, Label.LEFT);
        menuLabels[index].setFont(new Font("SansSerif", Font.PLAIN, 14));
        menuLabels[index].setForeground(new Color(80, 80, 80));
        card.add(menuLabels[index], BorderLayout.CENTER);

        Panel accent = new Panel();
        accent.setBackground(new Color(44, 130, 201));
        accent.setPreferredSize(new Dimension(5, 0));
        card.add(accent, BorderLayout.WEST);

        return card;
    }

    // ── Edit Panel — text areas for each day ──────────────────
    private Panel buildEditPanel() {
        Panel edit = new Panel(new BorderLayout());
        edit.setBackground(new Color(245, 247, 250));

        // title
        Panel titlePanel = new Panel();
        Label titleLbl = new Label("Edit Weekly Menu", Label.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLbl.setForeground(new Color(44, 62, 80));
        titlePanel.add(titleLbl);
        titlePanel.setBackground(new Color(245, 247, 250));
        edit.add(titlePanel, BorderLayout.NORTH);

        // one row per day
        Panel rowsContainer = new Panel();
        rowsContainer.setLayout(new GridLayout(7, 1, 0, 8));
        rowsContainer.setBackground(new Color(245, 247, 250));

        for (int i = 0; i < 7; i++) {
            rowsContainer.add(buildDayEditRow(i));
        }

        ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scroll.add(rowsContainer);
        edit.add(scroll, BorderLayout.CENTER);

        // ── Save all button at the bottom ─────────────────────
        Panel bottomBar = new Panel(new FlowLayout(FlowLayout.CENTER));
        bottomBar.setBackground(new Color(245, 247, 250));

        Button saveAllBtn = new Button("Save All Changes");
        saveAllBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        saveAllBtn.setBackground(new Color(60, 180, 100));
        saveAllBtn.setForeground(Color.WHITE);
        saveAllBtn.setPreferredSize(new Dimension(250, 45));

        saveAllBtn.addActionListener(ev -> {
             for (int i = 0; i < 7; i++) {
                 String day = days[i];
                 String menu = menuAreas[i].getText().trim();
                 try(Connection conn = DatabaseManager.connect()){
                     String query = "INSERT INTO MessMenu (Day,Menu) VALUES (?,?)";
                     PreparedStatement ptmpt = conn.prepareStatement(query);
                     ptmpt.setString(1, day);
                     ptmpt.setString(2, menu);
                     ptmpt.executeUpdate();
                 }catch(Exception ex){
                    System.out.println("Error: " + ex);

                 }

             }
            // for now just update the view labels instantly
            for (int i = 0; i < 7; i++) {
                if (!menuAreas[i].getText().trim().isEmpty()) {
                    menuLabels[i].setText(menuAreas[i].getText().trim());
                }
            }
            JOptionPane.showMessageDialog(null, "Menu saved successfully");
            cardLayout.show(mainContent, "VIEW");
        });

        bottomBar.add(saveAllBtn);
        edit.add(bottomBar, BorderLayout.SOUTH);

        return edit;
    }

    // ── One day row for edit mode ─────────────────────────────
    private Panel buildDayEditRow(int index) {
        Panel row = new Panel(new BorderLayout(10, 5));
        row.setBackground(index % 2 == 0 ? Color.WHITE : new Color(235, 240, 248));

        // day name label on the left
        Label dayLbl = new Label(days[index], Label.CENTER);
        dayLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        dayLbl.setForeground(new Color(44, 62, 80));
        dayLbl.setPreferredSize(new Dimension(110, 80));
        row.add(dayLbl, BorderLayout.WEST);

        // text area for typing the menu
            try(Connection conn = DatabaseManager.connect()){
                String query = "SELECT Menu FROM MessMenu WHERE Day = ?";
                PreparedStatement ptmpt = conn.prepareStatement(query);
                ptmpt.setString(1, days[index]);
                ResultSet rs = ptmpt.executeQuery();
                if(rs.next()){
                    menuAreas[index] = new TextArea(rs.getString("Menu"), 3, 40, TextArea.SCROLLBARS_NONE);

                }else {
                    menuAreas[index] = new TextArea(dummyMenus[index], 3, 40, TextArea.SCROLLBARS_NONE);
                }

            }catch(SQLException ex){
                menuAreas[index] = new TextArea(dummyMenus[index], 3, 40, TextArea.SCROLLBARS_NONE);
                System.out.println("Error: " + ex);
            }
        menuAreas[index].setFont(new Font("SansSerif", Font.PLAIN, 14));
        menuAreas[index].setBackground(new Color(250, 252, 255));
        row.add(menuAreas[index], BorderLayout.CENTER);
        // individual save button on the right
        Button saveBtn = new Button("Save");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        saveBtn.setBackground(new Color(44, 130, 201));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(80, 40));

        final int i = index;
        saveBtn.addActionListener(ev -> {
            String updatedMenu = menuAreas[i].getText().trim();
            if (updatedMenu.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Menu for " + days[i] + " cannot be empty");
                return;
            }
            String query = "UPDATE MessMenu SET Menu = ? WHERE Day = ?";
            try(Connection conn = DatabaseManager.connect()){
                PreparedStatement ptmpt = conn.prepareStatement(query);
                ptmpt.setString(1, updatedMenu);
                ptmpt.setString(2, days[i]);
                ptmpt.executeUpdate();
            }catch(SQLException ex){
                System.out.println("Error: " + ex);
            }
            // for now just update the view label instantly
            menuLabels[i].setText(updatedMenu);
            JOptionPane.showMessageDialog(null, days[i] + " menu updated successfully");
        });

        row.add(saveBtn, BorderLayout.EAST);
        return row;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewMenuBtn) {

            // remove old view panel and rebuild it fresh from DB
            mainContent.remove(0);
            mainContent.add(buildViewPanel(), "VIEW", 0);
            cardLayout.show(mainContent, "VIEW");

        } else if (e.getSource() == editMenuBtn) {
            cardLayout.show(mainContent, "EDIT");
        }
    }
    public static void main(String args[]){


        new MessMenuPanel(true);
        }


}
