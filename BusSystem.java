import java.awt.*;
import java.awt.GradientPaint;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class BusSystem {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private int[] availableSeats = {20, 20, 20, 20, 20};
    private String[] destinations = {"Arvi City", "Amravati City", "Wardha", "Nagpur", "Akola"};
    private double[] fares = {145, 75, 75, 145, 250};
    private String[] sources = {"Mumbai", "Pune", "Nagpur", "Indore", "Aurangabad"};

    public BusSystem() {
        initialize();
    }

    public void initialize() {
        frame = new JFrame("Bus Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createLoginPanel();
        createMenuPanel();
        createBookingPanel();

        frame.add(cardPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createLoginPanel() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0,
                        new Color(173, 216, 230),
                        0, height,
                        new Color(240, 248, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };

        backgroundPanel.setLayout(null);
        JPanel loginPanel = new JPanel(null);
        loginPanel.setBounds(150, 100, 300, 300);
        loginPanel.setBackground(new Color(255, 255, 255, 200));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 30, 100, 30);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField();
        usernameField.setBounds(20, 60, 240, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 100, 100, 30);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        passwordField.setBounds(20, 130, 240, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 180, 240, 40);
        styleButton(loginButton, new Color(70, 130, 180));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("1234")) {
                cardLayout.show(cardPanel, "Menu");
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        backgroundPanel.add(loginPanel);
        cardPanel.add(backgroundPanel, "Login");
    }

    private void createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        menuPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Bus Reservation System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton viewDestinationsButton = new JButton("View Destinations");
        styleButton(viewDestinationsButton, new Color(100, 149, 237));
        viewDestinationsButton.addActionListener(e -> showDestinations());

        JButton bookTicketButton = new JButton("Book Ticket");
        styleButton(bookTicketButton, new Color(70, 130, 180));
        bookTicketButton.addActionListener(e -> cardLayout.show(cardPanel, "Booking"));

        JButton exitButton = new JButton("Exit");
        styleButton(exitButton, new Color(178, 34, 34));
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "Thank you for using the Bus Reservation System!\nDo you want to exit?",
                    "Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuPanel.add(titleLabel);
        menuPanel.add(viewDestinationsButton);
        menuPanel.add(bookTicketButton);
        menuPanel.add(new JLabel());
        menuPanel.add(exitButton);
        cardPanel.add(menuPanel, "Menu");
    }

    private void createBookingPanel() {
        JPanel bookingPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        bookingPanel.setBackground(new Color(240, 248, 255));

        JLabel sourceLabel = new JLabel("Select Source:");
        JComboBox<String> sourceBox = new JComboBox<>(sources);
        JLabel destinationLabel = new JLabel("Select Destination:");
        JComboBox<String> destinationBox = new JComboBox<>(destinations);
        JLabel passengersLabel = new JLabel("Number of Passengers:");
        JTextField passengersField = new JTextField();

        JButton bookButton = new JButton("Book");
        styleButton(bookButton, new Color(70, 130, 180));

        bookButton.addActionListener(e -> {
            int sourceIndex = sourceBox.getSelectedIndex();
            int destinationIndex = destinationBox.getSelectedIndex();
            int passengers;

            try {
                passengers = Integer.parseInt(passengersField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid number of passengers!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (passengers > 0 && passengers <= availableSeats[destinationIndex]) {
                availableSeats[destinationIndex] -= passengers;
                double totalFare = passengers * fares[destinationIndex];
                String bill = "Source: " + sources[sourceIndex] +
                        "\nDestination: " + destinations[destinationIndex] +
                        "\nPassengers: " + passengers +
                        "\nTotal Fare: Rs." + totalFare;
                JOptionPane.showMessageDialog(frame, "Booking Successful!\n" + bill);
                saveBillToFile(bill);
                cardLayout.show(cardPanel, "Menu");
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Not enough seats available or invalid passenger count!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(70, 130, 180));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));

        bookingPanel.add(sourceLabel);
        bookingPanel.add(sourceBox);
        bookingPanel.add(destinationLabel);
        bookingPanel.add(destinationBox);
        bookingPanel.add(passengersLabel);
        bookingPanel.add(passengersField);
        bookingPanel.add(bookButton);
        bookingPanel.add(backButton);
        cardPanel.add(bookingPanel, "Booking");
    }

    private void saveBillToFile(String bill) {
        try (FileWriter writer = new FileWriter("BusReservationBill.txt", true)) {
            writer.write(bill + "\n\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                    "Error saving bill to file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    private void showDestinations() {
        StringBuilder message = new StringBuilder("Destinations and Fares:\n");
        for (int i = 0; i < destinations.length; i++) {
            message.append((i + 1) + ". " + destinations[i] + " | Fare: Rs. " + fares[i]
                    + " | Seats: " + availableSeats[i] + "\n");
        }
        JOptionPane.showMessageDialog(frame, message.toString(),
                "Destinations", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new BusSystem();
    }
}
