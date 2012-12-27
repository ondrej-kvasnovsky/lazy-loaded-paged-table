package lazyloadedpagedtable;

import org.vaadin.addons.lazycontainer.DAO;
import org.vaadin.addons.lazycontainer.OrderByColumn;
import org.vaadin.addons.lazycontainer.SearchCriteria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ondrej Kvasnovsky
 */
public class AgreementDAO implements DAO<Agreement> {

    private String driver = "org.hsqldb.jdbcDriver";
    private String connectionUri = "jdbc:hsqldb:file:devdb";
    private String username = "sa";
    private String password = "";

    public AgreementDAO() {
        try {
            try {
                Class.forName(driver).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Connection connection = DriverManager.getConnection(connectionUri, username, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("DROP TABLE AGREEMENTS IF EXISTS;");
            statement.executeQuery("CREATE TABLE AGREEMENTS (id int,name varchar(10));");
            for (int i = 0; i < 1000; i++) {
                statement.executeQuery("INSERT INTO AGREEMENTS VALUES(" + i + " ,'Agreement nr: ' + " + i + ");");
            }
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count(SearchCriteria searchCriteria) {
        try {
            Connection connection = DriverManager.getConnection(connectionUri, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM AGREEMENTS");
            resultSet.next();
            int fetchSize = resultSet.getInt(1);
            connection.close();
            return fetchSize;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Agreement> find(SearchCriteria criteria, int startIndex, int numberOfIds, List<OrderByColumn> columns) {
        try {
            Connection connection = DriverManager.getConnection(connectionUri, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM AGREEMENTS ORDER BY ID LIMIT " + numberOfIds + " OFFSET " + startIndex;
            ResultSet resultSet = statement.executeQuery(query);
            List<Agreement> res = new ArrayList<Agreement>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                res.add(new Agreement(id, name));
            }
            connection.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
