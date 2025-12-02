import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;

// ======= MODEL CLASSES =======

class Train {
    private int trainNo;
    private String name;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private double farePerSeat;

    public Train(int trainNo, String name, String source, String destination,
                 int totalSeats, double farePerSeat) {
        this.trainNo = trainNo;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.farePerSeat = farePerSeat;
    }

    public int getTrainNo() {
        return trainNo;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getFarePerSeat() {
        return farePerSeat;
    }

    public void reduceAvailableSeats(int seats) {
        this.availableSeats -= seats;
    }

    public void increaseAvailableSeats(int seats) {
        this.availableSeats += seats;
    }

    @Override
    public String toString() {
        return "Train No: " + trainNo +
               "\nName: " + name +
               "\nFrom: " + source +
               "\nTo: " + destination +
               "\nTotal Seats: " + totalSeats +
               "\nAvailable: " + availableSeats +
               "\nFare/Seat: " + farePerSeat + "\n";
    }
}

class Passenger {
    private int passengerId;
    private String name;
    private int age;
    private String gender;

    public Passenger(int passengerId, String name, int age, String gender) {
        this.passengerId = passengerId;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Passenger ID: " + passengerId +
               "\nName: " + name +
               "\nAge: " + age +
               "\nGender: " + gender + "\n";
    }
}

class Booking {
    private int bookingId;
    private Passenger passenger;
    private Train train;
    private int seatsBooked;
    private double totalFare;
    private LocalDate travelDate;

    public Booking(int bookingId, Passenger passenger, Train train,
                   int seatsBooked, double totalFare, LocalDate travelDate) {
        this.bookingId = bookingId;
        this.passenger = passenger;
        this.train = train;
        this.seatsBooked = seatsBooked;
        this.totalFare = totalFare;
        this.travelDate = travelDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Train getTrain() {
        return train;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
               "\nPassenger: " + passenger.getName() +
               "\nTrain: " + train.getTrainNo() + " (" + train.getName() + ")" +
               "\nSeats: " + seatsBooked +
               "\nTotal Fare: " + totalFare +
               "\nTravel Date: " + travelDate + "\n";
    }
}

// ======= GUI + CONTROLLER =======

public class RailwayManagementSystemGUI extends JFrame {

    // Data storage (in-memory)
    private Map<Integer, Train> trains = new HashMap<>();
    private Map<Integer, Passenger> passengers = new HashMap<>();
    private java.util.List<Booking> bookings = new ArrayList<>();

    // ID generators
    private int passengerIdCounter = 1;
    private int bookingIdCounter = 1000;

    public RailwayManagementSystemGUI() {
        setTitle("Railway Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Seed initial trains
        seedTrains();

        // Title label
        JLabel titleLabel = new JLabel("Railway Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAddTrain = new JButton("Add Train");
        JButton btnViewTrains = new JButton("View All Trains");
        JButton btnSearchTrain = new JButton("Search Train");
        JButton btnBookTicket = new JButton("Book Ticket");
        JButton btnViewBookings = new JButton("View All Bookings");
        JButton btnCancelBooking = new JButton("Cancel Booking");
        JButton btnExit = new JButton("Exit");

        buttonPanel.add(btnAddTrain);
        buttonPanel.add(btnViewTrains);
        buttonPanel.add(btnSearchTrain);
        buttonPanel.add(btnBookTicket);
        buttonPanel.add(btnViewBookings);
        buttonPanel.add(btnCancelBooking);
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.CENTER);

        // Action listeners
        btnAddTrain.addActionListener(e -> addTrain());
        btnViewTrains.addActionListener(e -> viewAllTrains());
        btnSearchTrain.addActionListener(e -> searchTrainByNumber());
        btnBookTicket.addActionListener(e -> bookTicket());
        btnViewBookings.addActionListener(e -> viewAllBookings());
        btnCancelBooking.addActionListener(e -> cancelBooking());
        btnExit.addActionListener(e -> System.exit(0));
    }

    // Seed some initial trains
    private void seedTrains() {
        Train t1 = new Train(11001, "Kolkata Express", "Kolkata", "Delhi", 100, 750.0);
        Train t2 = new Train(12002, "Duronto Express", "Mumbai", "Pune", 80, 300.0);
        Train t3 = new Train(13003, "Shatabdi Express", "Chennai", "Bangalore", 120, 600.0);

        trains.put(t1.getTrainNo(), t1);
        trains.put(t2.getTrainNo(), t2);
        trains.put(t3.getTrainNo(), t3);
    }

    // ======= FEATURES =======

    private void addTrain() {
        try {
            String trainNoStr = JOptionPane.showInputDialog(this, "Enter train number:");
            if (trainNoStr == null) return;
            int trainNo = Integer.parseInt(trainNoStr.trim());

            if (trains.containsKey(trainNo)) {
                JOptionPane.showMessageDialog(this, "Train with this number already exists!");
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Enter train name:");
            if (name == null) return;

            String source = JOptionPane.showInputDialog(this, "Enter source station:");
            if (source == null) return;

            String destination = JOptionPane.showInputDialog(this, "Enter destination station:");
            if (destination == null) return;

            String seatsStr = JOptionPane.showInputDialog(this, "Enter total seats:");
            if (seatsStr == null) return;
            int totalSeats = Integer.parseInt(seatsStr.trim());

            String fareStr = JOptionPane.showInputDialog(this, "Enter fare per seat:");
            if (fareStr == null) return;
            double farePerSeat = Double.parseDouble(fareStr.trim());

            Train train = new Train(trainNo, name, source, destination, totalSeats, farePerSeat);
            trains.put(trainNo, train);

            JOptionPane.showMessageDialog(this, "Train added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Train not added.");
        }
    }

    private void viewAllTrains() {
        if (trains.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No trains available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Train t : trains.values()) {
            sb.append(t.toString()).append("\n----------------------\n");
        }

        showLargeMessage("All Trains", sb.toString());
    }

    private void searchTrainByNumber() {
        try {
            String trainNoStr = JOptionPane.showInputDialog(this, "Enter train number:");
            if (trainNoStr == null) return;
            int trainNo = Integer.parseInt(trainNoStr.trim());

            Train t = trains.get(trainNo);
            if (t == null) {
                JOptionPane.showMessageDialog(this, "Train not found!");
            } else {
                showLargeMessage("Train Details", t.toString());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid train number.");
        }
    }

    private void bookTicket() {
        try {
            String trainNoStr = JOptionPane.showInputDialog(this, "Enter train number:");
            if (trainNoStr == null) return;
            int trainNo = Integer.parseInt(trainNoStr.trim());

            Train train = trains.get(trainNo);
            if (train == null) {
                JOptionPane.showMessageDialog(this, "Train not found.");
                return;
            }

            if (train.getAvailableSeats() <= 0) {
                JOptionPane.showMessageDialog(this, "No seats available on this train.");
                return;
            }

            String passengerName = JOptionPane.showInputDialog(this, "Enter passenger name:");
            if (passengerName == null) return;

            String ageStr = JOptionPane.showInputDialog(this, "Enter age:");
            if (ageStr == null) return;
            int age = Integer.parseInt(ageStr.trim());

            String gender = JOptionPane.showInputDialog(this, "Enter gender:");
            if (gender == null) return;

            String seatsStr = JOptionPane.showInputDialog(this, "Enter number of seats to book:");
            if (seatsStr == null) return;
            int seatsRequired = Integer.parseInt(seatsStr.trim());

            if (seatsRequired <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid seat count.");
                return;
            }
            if (seatsRequired > train.getAvailableSeats()) {
                JOptionPane.showMessageDialog(this,
                        "Only " + train.getAvailableSeats() + " seats available. Booking failed.");
                return;
            }

            String dateStr = JOptionPane.showInputDialog(this, "Enter travel date (YYYY-MM-DD):");
            if (dateStr == null) return;

            LocalDate travelDate;
            try {
                travelDate = LocalDate.parse(dateStr.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Booking cancelled.");
                return;
            }

            // Create passenger
            Passenger passenger = new Passenger(passengerIdCounter++, passengerName, age, gender);
            passengers.put(passenger.getPassengerId(), passenger);

            // Calculate fare & create booking
            double totalFare = seatsRequired * train.getFarePerSeat();
            Booking booking = new Booking(bookingIdCounter++, passenger, train, seatsRequired, totalFare, travelDate);

            // Update seats in train
            train.reduceAvailableSeats(seatsRequired);

            // Store booking
            bookings.add(booking);

            showLargeMessage("Booking Successful", booking.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Booking cancelled.");
        }
    }

    private void viewAllBookings() {
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bookings done yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Booking b : bookings) {
            sb.append(b.toString()).append("\n----------------------\n");
        }

        showLargeMessage("All Bookings", sb.toString());
    }

    private void cancelBooking() {
        try {
            String bookingIdStr = JOptionPane.showInputDialog(this, "Enter booking ID:");
            if (bookingIdStr == null) return;
            int bookingId = Integer.parseInt(bookingIdStr.trim());

            Booking bookingToCancel = null;
            for (Booking b : bookings) {
                if (b.getBookingId() == bookingId) {
                    bookingToCancel = b;
                    break;
                }
            }

            if (bookingToCancel == null) {
                JOptionPane.showMessageDialog(this, "Booking not found.");
                return;
            }

            // Increase train seats back
            Train train = bookingToCancel.getTrain();
            train.increaseAvailableSeats(bookingToCancel.getSeatsBooked());

            // Remove booking
            bookings.remove(bookingToCancel);

            showLargeMessage("Booking Cancelled", bookingToCancel.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid booking ID.");
        }
    }

    // Helper to show large text in scrollable dialog
    private void showLargeMessage(String title, String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // ======= MAIN =======

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RailwayManagementSystemGUI frame = new RailwayManagementSystemGUI();
            frame.setVisible(true);
        });
    }
}
