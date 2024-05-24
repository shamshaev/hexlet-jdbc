import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    // Нужно указывать базовое исключение,
    // потому что выполнение запросов может привести к исключениям
    public static void main(String[] args) throws SQLException {
        // Создаем соединение с базой в памяти
        // База создается прямо во время выполнения этой строчки
        // Здесь hexlet_test — это имя базы данных
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            // Чтобы выполнить запрос, создадим объект statement
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var statement2 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS)) {
                statement2.setString(1, "tommy");
                statement2.setString(2, "123456789");
                statement2.executeUpdate();

                var generatedKeys = statement2.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }

                statement2.setString(1, "mary");
                statement2.setString(2, "987667892");
                statement2.executeUpdate();

                statement2.setString(1, "andrew");
                statement2.setString(2, "876356431");
                statement2.executeUpdate();
            }

            var sql3 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                // Здесь вы видите указатель на набор данных в памяти СУБД
                var resultSet = statement3.executeQuery(sql3);
                // Набор данных — это итератор
                // Мы перемещаемся по нему с помощью next() и каждый раз получаем новые значения
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
                System.out.println();
            }

            var sql4 = "DELETE FROM users WHERE username = ?";
            try (var statement4 = conn.prepareStatement(sql4)) {
                statement4.setString(1, "tommy");
                statement4.executeUpdate();
            }

            try (var statement5 = conn.createStatement()) {
                var resultSet = statement5.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
        }
    }
}
