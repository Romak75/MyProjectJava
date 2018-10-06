package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Arrays;

public class JDBCDatabaseManager implements DatabaseManager {


    private Connection connection;

    public static void main(String[] argv) throws ClassNotFoundException, SQLException {

                String database = "test";
                String user = "postgres";
                String password = "postgres";
                JDBCDatabaseManager manager = new JDBCDatabaseManager();
                manager.connect(database, user, password);

                Connection connection = manager.getConnection();

                // delete

                manager.clear("user");

                // insert
        DataSet data = new DataSet();
        data.put("name", "Vasya");
        data.put("password", "12345");
        data.put("id", "1");
        manager.create("user", data);



                // select

                String sql1 = "SELECT * FROM public.user WHERE id > 5";
                insert(connection, sql1);

                //select tables

        String[] tables = manager.getTableNames();
        System.out.println(Arrays.toString(tables));
        String tableName = "user";
        DataSet[] result = manager.getTableData(tableName);
        System.out.println(Arrays.toString(result));


                // update



                connection.close();
            }

    @Override
    public DataSet[] getTableData(String tableName) {
            int size = getSize(tableName);
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName)) {
                ResultSetMetaData rsmd = rs.getMetaData();
                DataSet[] result = new DataSet[size];
                int index = 0;
                while (rs.next()) {
                    DataSet dataSet = new DataSet();
                    result[index++] = dataSet;
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        dataSet.put(rsmd.getColumnName(i), rs.getObject(i));

                    }
                }
                return result;

        }catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    public int getSize(String tableName) {
        try (Statement stmt = connection.createStatement(); ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName))
        {
                rsCount.next();
                int size = rsCount.getInt(1);
                return size;
            } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void connect(String database, String userName, String password) throws RuntimeException {
        try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add JDBC jar to project.", e);
        }
            try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/" + database, userName,
                        password);
            } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format("Can't receive connection for model:%s, user:%s",
                    database, userName), e);

            }
    }


    private static void insert(Connection connection, String sql1) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql1);
        while (rs.next()) {
            System.out.println("name:" + rs.getString("name"));
            System.out.println("password:" + rs.getString("password"));
           System.out.println("id:" + rs.getString("id"));
            System.out.println("--------------------");
        }
        rs.close();
        stmt.close();
    }

            private static void update(Connection connection, String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }


    @Override
    public String[] getTableNames() {
        String[] tables;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public'"))
        {
            tables = new String[100];
            int index = 0;

            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
        return tables;

    }

    private Connection getConnection() {
        return connection;
    }

    @Override
    public void clear(String tableName) {
    try(Statement stmt = connection.createStatement()) {
        stmt.executeUpdate("DELETE FROM public." + tableName);
    }catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try (Statement stmt = connection.createStatement()) {
            String tableNames = getNameFormated(input, "%s,");
            String values = getValuesFormated(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                    "VALUES (" + values + ")");
        }catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public String getValuesFormated(DataSet input, String format) {
        String values = "";

        for(Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
            String tableNames = getNameFormated(newValue, "%s = ?,");
            String sql = "UPDATE public." + tableName + " SET " + tableNames + " WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int index = 1;
            for (Object value : newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setInt(index, id);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] getTableColoumns(String tableName) {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.columns " +
                    "WHERE table_schema = 'public' AND table_name = '" + tableName + "'"))
        {
            String[] tables = new String[100];
            int index = 0;

            while (rs.next()) {
                tables[index++] = rs.getString("column_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
                    }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    public String getNameFormated(DataSet newValue, String format) {
        String string = "";
        for(String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}
