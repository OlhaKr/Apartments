package flats;

import java.sql.*;
import java.util.Scanner;

public class Main {


    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/flats?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "151298";

    static Connection conn;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add flat");
                    System.out.println("2: delete flat");
                    System.out.println("3: change flat");
                    System.out.println("4: find flat by rooms");
                    System.out.println("5: find flat by price");
                    System.out.println("6: find flat by district");
                    System.out.println("7: view flats");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlats(sc);
                            break;
                        case "2":
                            delFlat(sc);
                            break;
                        case "3":
                            changeFlat(sc);
                            break;
                        case "4":
                            findFlatByRooms(sc);
                            break;
                        case "5":
                            findFlatByPrice(sc);
                            break;
                        case "6":
                            findFlatByDistrict(sc);
                            break;
                        case "7":
                            viewAllApartments();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }


    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Flats");
            st.execute("CREATE TABLE Flats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "district VARCHAR(30) DEFAULT NULL, address VARCHAR (30) DEFAULT NULL," +
                    "square DOUBLE DEFAULT NULL, rooms INT DEFAULT NULL, price DOUBLE DEFAULT NULL)");
        } finally {
            st.close();
        }
    }

    private static void addFlats(Scanner sc) throws SQLException {
        System.out.println("Enter district: ");
        String district = sc.nextLine();
        System.out.println("Enter address: ");
        String address = sc.nextLine();
        System.out.println("Enter square: ");
        String sSquare = sc.nextLine();
        double square = Double.parseDouble(sSquare);
        System.out.println("Enter rooms: ");
        String sRooms = sc.nextLine();
        int rooms = Integer.parseInt(sRooms);
        System.out.println("Enter price: ");
        String sPrice = sc.nextLine();
        double price = Double.parseDouble(sPrice);

        PreparedStatement ps = conn.prepareStatement("INSERT INTO Flats (district, address, square, rooms, price) VALUES (?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setDouble(3, square);
            ps.setInt(4, rooms);
            ps.setDouble(5, price);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void delFlat(Scanner sc) throws SQLException {
        System.out.println("Enter flat id: ");
        String id = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM Flats WHERE id = ?");
        try {
            ps.setString(1, id);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void changeFlat(Scanner sc) throws SQLException {
        System.out.println("Enter flat id: ");
        String id = sc.nextLine();
        System.out.println("Enter new price: ");
        String sPrice = sc.nextLine();
        double price = Double.parseDouble(sPrice);
        PreparedStatement ps = conn.prepareStatement("UPDATE Flats set PRICE = ? WHERE id = ?");
        try {
            ps.setDouble(1, price);
            ps.setString(2, id);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }


    private static void findFlatByRooms(Scanner sc) throws SQLException {
        System.out.println("Choose number of rooms: ");
        String sRoomNumber = sc.nextLine();
        int roomNumber = Integer.parseInt(sRoomNumber);

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM  Flats WHERE rooms =?");
        try {
            ps.setInt(1, roomNumber);
            viewApartments(ps);
        } finally {
            ps.close();
        }

    }

    private static void findFlatByPrice(Scanner sc) throws SQLException {
        System.out.println("Choose min price");
        String sMinPrice = sc.nextLine();
        double minPrice = Double.parseDouble(sMinPrice);
        System.out.println("Choose max price");
        String sMaxPrice = sc.nextLine();
        double maxPrice = Double.parseDouble(sMaxPrice);

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Flats WHERE price >= ? AND price <= ?");
        try {
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            viewApartments(ps);
        } finally {
            ps.close();
        }
    }


    private static void findFlatByDistrict(Scanner sc) throws SQLException {
        System.out.println("Choose district: ");
        String chosenDistrict = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Flats WHERE district = ?");
        try {
            ps.setString(1, chosenDistrict);
            viewApartments(ps);
        } finally {
            ps.close();
        }
    }


    private static void viewAllApartments() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Flats");
        try {
            viewApartments(ps);
        } finally {
            ps.close();
        }
    }

    private static void viewApartments(PreparedStatement ps) throws SQLException {

        try {
            ResultSet rs = ps.executeQuery();

            try {
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print(md.getColumnName(i) + "\t\t\t");
                }
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }

}


















