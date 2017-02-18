package postgrestime;

import org.postgresql.Driver;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimeZone;

public class TestPostgresTime {

    public static void main(String[] args) {
        System.out.println(new SecureRandom().nextInt(40));
        System.out.println(new SecureRandom().nextInt(40));
    }

    public static void main2(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Driver driver = new Driver();
        try {
            Connection connection = driver.connect("jdbc:postgresql://localhost:5432/trond", null);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from test");
            resultSet.next();
            System.out.println(resultSet.getTimestamp("ts"));

            resultSet = statement.executeQuery("select now()");
            resultSet.next();
            System.out.println(resultSet.getTimestamp(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Done");

    }
}
