package trond.java.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TestHsqldb {

    public static void main(String[] args) {

        TestHsqldb testHsqldb = new TestHsqldb();

        testHsqldb.setup();

        testHsqldb.useHibernate();

        new Configuration();

    }

    private void useHibernate() {
        Configuration configuration = new Configuration();
        configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE)
                        .configure("hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .build();

        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        Criteria criteria = sessionFactory.openSession().createCriteria(Ticket.class);
        List tickets = criteria.list();

        System.out.println(tickets);

    }

    private void setup() {
        try {
            Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");

            String sql = "create table ticket(" +
                    "ticket_id integer identity primary key, " +
                    "holder_name varchar(60))";
            c.createStatement().executeUpdate(sql);

            c.createStatement().execute("insert into ticket values (1, 'Trond')");
            c.createStatement().execute("insert into ticket values (null, 'Trond')");

            c.createStatement().execute("commit");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
