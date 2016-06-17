import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {

        emf = Persistence.createEntityManagerFactory("testBase");
        em = emf.createEntityManager();



//        String url = "jdbc:mysql://localhost:3306/testbase";
//        //String url = "jdbc:sqlserver://localhost;databaseName=mybase;integratedSecurity=true";
//        String username = "java";
//        String password = "java";
//
//        System.out.println("Connecting database...");
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            System.out.println("Database connected!");
//        } catch (SQLException e) {
//            throw new IllegalStateException("Cannot connect the database!", e);
//        }

    }
}
