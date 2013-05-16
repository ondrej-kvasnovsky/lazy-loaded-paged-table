# Lazy Loaded Paged Table

This is an example of how lazy loaded paged table can be implemented in Vaadin. Lazy loaded paged table is table where you request displaying of each page (there is no scrollabar) and data for each page are fetched lazily from database. Lazy loaded table can look like this.

![Paged Table](http://gerades.savana.cz/github/lazy-loaded-paged-table/lazy-loaded-paged-table-example.png)

## Used add-ons

* [forked PagedTable](https://github.com/ondrej-kvasnovsky/PagedTable)
* [LazyContainer](https://github.com/ondrej-kvasnovsky/lazy-container)

## Example code snippets

### Maven dependencies
```xml
<dependency>
    <groupId>org.vaadin.addons</groupId>
    <artifactId>pagedtable</artifactId>
    <version>0.6.7</version>
</dependency>
<dependency>
    <groupId>org.vaadin.addons</groupId>
    <artifactId>lazy-container</artifactId>
    <version>0.0.2</version>
</dependency>
<dependency>
    <groupId>hsqldb</groupId>
    <artifactId>hsqldb</artifactId>
    <version>1.8.0.10</version>
</dependency>
```

```xml
<repository>
    <id>qiiip-repo</id>
    <url>http://qiiip.org/mavenRepo</url>
</repository>
```

### Domain object

We will use agreement entity for this example.

```java
public class Agreement {

    private int id;
    private String name;

    public Agreement(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
}
```

### Search criteria

We made simple search criteria that we might enhance for e.g. fields that would represent user's input.

```java
public class AgreementSearchCriteria extends AbstractSearchCriteria {
}
```

### DAO (Data Access Object)

Here we make simple DAO that will use in-memory HSQLDB database. This is actually the biggest part of the example code (it is just faking database so we can use SQL queries as demonstrative showcase).

Note: it is just example code to show how we could implement DAO interface.

```java
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
```

### UI layout

We create layout, table, controls and container. Then we put it together and we can run the application in browser.

```java
public class MyVaadinUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        PagedTable table = new PagedTable("PagedTable Example");
        ControlsLayout controls = table.createControls();

        LazyBeanContainer container = new LazyBeanContainer(Agreement.class, new AgreementDAO(), new AgreementSearchCriteria());
        table.setContainerDataSource(container);

        table.setWidth("500px");
        controls.setWidth("500px");

        layout.addComponent(table);
        layout.addComponent(controls);
    }
}
```
