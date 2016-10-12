import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import oracle.jdbc.OracleDriver;

/**
 * @author trond.
 */
public class OracleClient {

    final OracleDriver oracleDriver = new OracleDriver();

    final String sortColumn = "createdDate";

    public static void main(final String[] args) {
        new OracleClient().runTest();
    }

    public void runTest() {
        try {
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from foo");
            connection.commit();
            statement.close();
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 4; i++) {
            new Writer().start();
        }
        for (int i = 0; i < 1; i++) {
            new Reader().start();
        }
    }

    public void doWork(final Connection connection, final Consumer<Connection> work) {
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            work.accept(connection);
        }
    }

    private Connection createConnection() throws SQLException {
        final Properties properties = new Properties();
        properties.put("user", "sys as sysdba");
        properties.put("password", "oracle");
        Connection connection = oracleDriver.connect("jdbc:oracle:thin:@localhost:1521:orcl", properties);
        connection.setAutoCommit(false);
        return connection;
    }

    class Reader extends Thread {

        final OracleDriver oracleDriver = new OracleDriver();

        Object lastRead = sortColumn.equals("createdDate")? new Timestamp(System.currentTimeMillis() - Integer.MAX_VALUE) : 0;

        @Override
        public void run() {
            final Properties properties = new Properties();
            properties.put("user", "sys as sysdba");
            properties.put("password", "oracle");
            try (Connection connection = oracleDriver.connect("jdbc:oracle:thin:@localhost:1521:orcl", properties)) {
                doWork(connection, this::read);
            }
            catch (final SQLException e1) {
                throw new RuntimeException(e1);
            }
        }

        public void read(final Connection connection) {
            try {
                String sql = "select ora_rowscn, createdDate from (" + "select ora_rowscn, createdDate from foo " + "where "
                             + sortColumn + " > :1 " + "order by " + sortColumn + ") where rownum <= 20";
                final PreparedStatement st = connection.prepareStatement(sql);

                if (sortColumn.equals("createdDate"))
                    st.setTimestamp(1, (Timestamp) lastRead);
                else
                    st.setInt(1, (Integer) lastRead);

                final ResultSet resultSet = st.executeQuery();
                int rowCount = 0;
                List<Object> sortValues = new ArrayList<>();
                while (resultSet.next()) {
                    rowCount++;
                    Object sortValue = sortColumn.equals("createdDate")? resultSet.getTimestamp(2) : resultSet.getInt(1);
                    sortValues.add(sortValue);
                    System.out.println("ora_rowscn " + resultSet.getInt(1));
                    System.out.println("createdDate " + resultSet.getTimestamp(2));
                }
                if (sortValues.isEmpty())
                    return;

                final Comparator<Object> comparator = sortColumn.equals("createdDate")?
                                                      (a, b) -> ((Timestamp) a).compareTo((Timestamp) b) :
                                                      (a, b) -> ((Integer) a).compareTo((Integer) b);
                lastRead = sortValues.stream().max(comparator).get();
                st.close();
                resultSet.close();
                System.out.println("row count= " + rowCount);
            }
            catch (final SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    class Writer extends Thread {

        @Override
        public void run() {
            try (Connection connection = createConnection()) {
                doWork(connection, this::insert);
            }
            catch (final SQLException e1) {
                throw new RuntimeException(e1);
            }
        }

        public void insert(final Connection connection) {
            try {
                final PreparedStatement st = connection.prepareStatement("insert into foo(id) values(:1)");
                st.setLong(1, System.currentTimeMillis());

                st.executeUpdate();
                connection.commit();
                st.close();
            }
            catch (final SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
