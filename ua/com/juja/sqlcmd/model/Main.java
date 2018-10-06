package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Random;

public class Main {


            public static void main(String[] argv) throws ClassNotFoundException, SQLException {

               Class.forName("org.postgresql.Driver");

                    Connection connection = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/test", "postgres",
                            "postgres");

                // insert
                String sql = "INSERT INTO public.user (name, password)" +
                        "VALUES ('Vasya', '12345')";

                update(connection, sql);


                // select
                String sql1 = "SELECT * FROM public.user WHERE id > 5";
                insert(connection, sql1);


                // delete

                String sql2 = "DELETE FROM public.user " +
                        "WHERE id > 10 AND id < 100";
                update(connection, sql2);

                // update

                PreparedStatement ps = connection.prepareStatement("UPDATE public.user SET password = ? WHERE id > 3");
                String pass = "password_" + new Random().nextInt();
                ps.setString(1, pass);
                ps.executeUpdate();
                ps.close();

                connection.close();
            }


    private static void insert(Connection connection, String sql1) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql1);
        while (rs.next()) {
           System.out.println("id:" + rs.getString("id"));
            System.out.println("name:" + rs.getString("name"));
            System.out.println("password:" + rs.getString("password"));
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

}
