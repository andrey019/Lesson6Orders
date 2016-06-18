
import java.sql.*;
import java.util.Scanner;

public class Main {

    static String address = "jdbc:mysql://localhost:3306/lesson6order";
    static String user = "root";
    static String password = "password";
    static Connection connection;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(address, user, password);
            if (!initDB()) {
                System.out.println("Error while initializing database!");
                connection.close();
                System.exit(0);
            }
            mainMenu(scanner, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void mainMenu(Scanner scanner, Connection connection) {
        instructionsOnScreen();
        while (true) {
            switch (scanner.nextLine()) {
                case "1":
                    addClient(scanner, connection);
                    break;
                case "2":
                    addProduct(scanner, connection);
                    break;
                case "3":
                    if (!addOrder(scanner, connection)) {
                        instructionsOnScreen();
                    }
                    break;
                case "4":
                    showTableOnScreen(connection, "client");
                    break;
                case "5":
                    showTableOnScreen(connection, "product");
                    break;
                case "6":
                    showTableOnScreen(connection, "orders");
                    break;
                case "7":
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                    System.exit(0);
                default:
                    instructionsOnScreen();
            }
        }
    }

    private static void addClient(Scanner scanner, Connection connection) {
        System.out.print("Type in client name: ");
        String name = getStringInput(scanner);
        System.out.print("Type in client age: ");
        int age = getIntInput(scanner);
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO client (name, age) " +
                "VALUES (?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.executeUpdate();
            addedSuccessfully();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addProduct(Scanner scanner, Connection connection) {
        System.out.print("Type in product name: ");
        String name = getStringInput(scanner);
        System.out.print("Type in product price: ");
        int price = getIntInput(scanner);
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO product (name, price) " +
                "VALUES (?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, price);
            preparedStatement.executeUpdate();
            addedSuccessfully();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean addOrder(Scanner scanner, Connection connection) {
        int clientId;
        if ( (clientId = identifyClient(scanner, connection)) == -1 ) {
            System.out.println("No such client in database!\r\n");
            return false;
        }
        int productId;
        if ( (productId = identifyProduct(scanner, connection)) == -1 ) {
            System.out.println("No such product in database!\r\n");
            return false;
        }
        System.out.print("Type in amount of product: ");
        int amount = getIntInput(scanner);
        int productPrice;
        if ( (productPrice = getProductPrice(connection, productId)) == -1 ) {
            return false;
        }
        int totalPrice = amount * productPrice;
        return addOrderExecute(connection, clientId, productId, amount, totalPrice);
    }

    private static int identifyClient(Scanner scanner, Connection connection) {
        System.out.print("Type in client name: ");
        String name = getStringInput(scanner);
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM client " +
                "WHERE client.name = '" + name + "'")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {}
        return -1;
    }

    private static int identifyProduct(Scanner scanner, Connection connection) {
        System.out.print("Type in product name: ");
        String name = getStringInput(scanner);
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM product " +
                "WHERE product.name = '" + name + "'")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {}
        return -1;
    }

    private static int getProductPrice(Connection connection, int productId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT price FROM product " +
                "WHERE product.id = '" + productId + "'")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static boolean addOrderExecute(Connection connection, int clientId, int productId,
                                        int amount, int totalPrice) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders " +
                "(client_id, product_id, amount, total_price) VALUES (?,?,?,?)")) {
            preparedStatement.setInt(1, clientId);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, amount);
            preparedStatement.setInt(4, totalPrice);
            preparedStatement.executeUpdate();
            addedSuccessfully();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void addedSuccessfully() {
        System.out.println("Added successfully");
        System.out.println();
    }

    private static String getStringInput(Scanner scanner) {
        String string = scanner.nextLine();
        while (string.isEmpty()) {
            System.out.println("Field can't be empty! Try again");
            string = scanner.nextLine();
        }
        return string;
    }

    private static int getIntInput(Scanner scanner) {
        int integer = -1;
        while (integer == -1) {
            try {
                integer = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Wrong type! Try again");
            }
        }
        return integer;
    }

    private static void instructionsOnScreen() {
        System.out.println("1) Add client");
        System.out.println("2) Add product");
        System.out.println("3) Add order");
        System.out.println("4) Show all clients");
        System.out.println("5) Show all products");
        System.out.println("6) Show all orders");
        System.out.println("7) Exit");
        System.out.println();
    }

    private static void showTableOnScreen(Connection connection, String tableName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.print(resultSet.getMetaData().getColumnName(i) + "\t\t");
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.print(resultSet.getString(i) + "\t\t");
                }
                System.out.println();
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean initDB() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS orders");
            statement.execute("DROP TABLE IF EXISTS client");
            statement.execute("DROP TABLE IF EXISTS product");

            statement.execute("CREATE TABLE client (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR (20) NOT NULL, age INT)");
            statement.execute("CREATE TABLE product (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR (20) NOT NULL, price INT NOT NULL)");
            statement.execute("CREATE TABLE orders (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "client_id INT NOT NULL, product_id INT NOT NULL, " +
                    "amount INT NOT NULL, total_price INT NOT NULL, FOREIGN KEY (client_id) REFERENCES client(id), " +
                    "FOREIGN KEY (product_id) REFERENCES product(id) )");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
