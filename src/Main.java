import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.protocol.Resultset;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            File irs_file = new File("IRSdata.dat");
            Scanner scanner = new Scanner(irs_file);
            Scanner usr_scanner = new Scanner(System.in);
            LinkedList<Employer> empList = new LinkedList<Employer>();

            for (int i = 0; i < 1200; i++) {
                String data = scanner.nextLine();
                //System.out.println(data.substring(2, 18));

                //Adding to a link list
                if (i > 0) {
                    String EINnum = data.substring(2, 11);
                    String tax_pd = data.substring(12, 18);
                    //System.out.println(EINnum + " " + tax_pd);
                    Employer emp = new Employer(EINnum, tax_pd);
                    empList.add(emp);
                }

            }

            System.out.println(empList);

            String url = "lunastra-db-instance.cgmwncma8yqq.us-west-2.rds.amazonaws.com";
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            System.out.println("Enter a Database username: ");
            String username = usr_scanner.nextLine();

            System.out.println();
            System.out.println("Enter password");
            String password = usr_scanner.nextLine();

            //Finding a specific employer using Employer ID
//            System.out.println("Enter a EIN to search: ");
//            String user_input = usr_scanner.nextLine();

            System.out.println("Connecting to the database ...");
            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                Connection conn = DriverManager.getConnection(url, username, password);

                MysqlDataSource dataSource = new MysqlDataSource();
                dataSource.setUser(username);
                dataSource.setPassword(password);
                dataSource.setServerName("lunastra-db-instance.cgmwncma8yqq.us-west-2.rds.amazonaws.com");

                Connection conn = dataSource.getConnection();
                //Choose Lunastra as the database to edit
                conn.setCatalog("Lunastra");

                System.out.println("Connected to the database. ");

                System.out.println("Starting to print all emp");
                ResultSet rs = getAllEmp(conn);
                printResultSet(rs);
                System.out.println("Finish displaying all emp");

//                Statement stmt = conn.createStatement();
//                int raffect = stmt.executeUpdate("INSERT into employers (EIN, tax_pd) values (123456,9786)");
//                System.out.println("Rows affected: " + raffect);


                //Add the first 1500 employers to the database
//                System.out.println("Adding employers to the database");
//                addEmployer(conn);
//                System.out.println("Finish adding employers to the database");

                //Prompting the user to enter a EIN number
                System.out.println("Enter a EIN to search");
                String user_EIN = usr_scanner.nextLine();
                ResultSet emp_rs= findEmp(conn, user_EIN);

                System.out.println();
                System.out.println("-----------------------------");
                System.out.println("Result of the search");
                printResultSet(emp_rs);

                conn.close();
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }


//            int found_emp = -1;
//            for (int i = 0; i < empList.size(); i++) {
//                Employer tempE = empList.get(i);
//                if (user_input.equals(tempE.getEINnum())) {
//                    System.out.println(tempE);
//                    found_emp = 0;
//                    break;
//                }
//            }
//
//            if (found_emp == -1) {
//                System.out.println("Employer does not exist.");
//            }
//            while (scanner.hasNextLine()) {
//                String data = scanner.nextLine();
//                System.out.println(data);
//            }
        }
        catch (FileNotFoundException e) {
            System.out.println("No file with the matching name");
            e.printStackTrace();
        }
    }
    public static void addEmployer(Connection conn) {
        try {
            File irs_file = new File("IRSdata.dat");
            Scanner scanner = new Scanner(irs_file);
            Scanner usr_scanner = new Scanner(System.in);
            LinkedList<Employer> empList = new LinkedList<Employer>();

            //Make a prepare statement to add to employers to DB
            String sql = "INSERT into employers (EIN, tax_pd) values (?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < 1200; i++) {
                pstmt.clearParameters();
                String data = scanner.nextLine();
                //System.out.println(data.substring(2, 18));

                //Adding to a link list
                if (i > 0) {
                    String EINnum = data.substring(2, 11);
                    String tax_pd = data.substring(12, 18);
                    //System.out.println(EINnum + " " + tax_pd);
                    Employer emp = new Employer(EINnum, tax_pd);
                    empList.add(emp);

                    pstmt.setString(1, EINnum);
                    pstmt.setString(2, tax_pd);
                    int numRows = pstmt.executeUpdate();

                }

            }
            pstmt.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found!!!");
            e.printStackTrace();
        }
        catch  (SQLException se) {
            System.out.println("Error involving database");
            se.printStackTrace();
        }
    }

    public static ResultSet getAllEmp(Connection conn) {
        try {
            String sql = "SELECT * FROM employers ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException se) {
            // Handle errors for JDBC
            //se.printStackTrace();
            System.out.println("Failed to list all employers!");
            return null;
        }
    }

    //Better looking print function
    public static void printResultSet(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            //String space = String.format("%-" + 15 + "s", "");
            //String row_sp = String.format("%-" + 20 + "s", "");
            for (int j = 1; j <= numCol; j++){
                System.out.format("%-25s",rsmd.getColumnName(j));
            }
            System.out.println("");
            while (rs.next()) {
                for (int i = 1; i <= numCol; i++) {
                    String colValue = rs.getString(i);
                    //System.out.print(colValue);
                    System.out.format("%-25s",colValue);
//                    System.out.print(row_sp);
                }
                System.out.println("");
            }
        } catch (SQLException se) {
            // Handle errors for JDBC
            //se.printStackTrace();
            System.out.println("Error when printing result set!");
        }
    }

    public static ResultSet findEmp(Connection conn, String user_EIN) {
        try {
            String sql = "SELECT * from employers where EIN = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user_EIN);
            ResultSet rs = pstmt.executeQuery();
            return rs;
        }
        catch (SQLException se) {
            System.out.println("Error searching for employer");
            se.printStackTrace();
            return null;
        }
    }
}


