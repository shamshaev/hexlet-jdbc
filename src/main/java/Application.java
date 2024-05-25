import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        try (var connection = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = connection.createStatement()) {
                statement.execute(sql);
            }

            var userDAO = new UserDAO(connection);

            userDAO.save(new User("tommy", "123456789"));
            userDAO.save(new User("mary", "987667892"));
            userDAO.save(new User("andrew", "876356431"));

            var users = userDAO.getEntities();

            for (var user : users) {
                System.out.println(user.getUsername() + " " + user.getPhone());
            }
            System.out.println();

            userDAO.delete(1L);

            users = userDAO.getEntities();

            for (var user : users) {
                System.out.println(user.getUsername() + " " + user.getPhone());
            }
        }
    }
}
