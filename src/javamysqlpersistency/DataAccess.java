package javamysqlpersistency;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author: Dejan Smiljić; e-mail: dej4n.s@gmail.com
 *
 */
//Singleton Pattern Class
public class DataAccess {

    private String dbURL;
    private String username;
    private String password;
    private Connection dbConnection;
    private PreparedStatement sqlStatement;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private Vector<Object> vectorColumnNames;
    private Vector<Object> vectorData;

    //Setting up Singleton pattern
    //Class instantiation
    private static DataAccess instance = new DataAccess();

    //Constructor has to be private
    private DataAccess() {
    }

    //Static method returns the instance/object of Singleton class
    protected static DataAccess getInstance() {

        return instance;

    }

    /*//Singleton Lazy Instantiation, not thread safe, obsolete
    private static DataAccess instance;
    
    protected static DataAccess getInstance() {
    if (instance == null) {
    instance = new DataAccess();
    }
    return instance;
    }*/
    protected void setDataBaseConnection() {

        //prevents multiple Connection objects creation
        if (dbConnection != null) {
            return;
        }

        dbURL = "jdbc:mysql://localhost/javacafe";
        username = "root";
        password = "";

        try {
            dbConnection = DriverManager.getConnection(dbURL, username, password);
            if (!dbConnection.isClosed()) {
                System.out.println("connection established: " + !dbConnection.isClosed());
            }
        } catch (SQLException sqlExc) {
            System.out.println(sqlExc);
        }
    }// setDataBaseConnection() END

    protected void closeDataBaseConnection() {
        try {
            if (null != dbConnection) {
                dbConnection.close();
            }
            if (dbConnection.isClosed()) {
                System.out.println("connection closed: " + dbConnection.isClosed());
            }//OBRISATI
        } catch (SQLException sqlExc) {
            System.out.println(sqlExc);
        }
    }// closeDataBaseConnection() END

    protected DefaultTableModel getTableModel(String sql) {
        DefaultTableModel model;

        try {
            sqlStatement = dbConnection.prepareStatement(sql);
            /*String sql = ((sqlStatement.toString().split(":")[1]).trim());
            System.out.println(sql);*/
            sqlStatement.executeQuery(sql);
            resultSet = sqlStatement.getResultSet();
            //########################################
            resultSetMetaData = resultSet.getMetaData();
            int numberOfColumns = resultSetMetaData.getColumnCount();
            vectorColumnNames = new Vector<>(numberOfColumns);
            for (int i = 1; i <= numberOfColumns; i++) {
                vectorColumnNames.addElement(resultSetMetaData.getColumnName(i));
            }
            //#######################################
            vectorData = new Vector<>();
            while (resultSet.next()) {
                Vector<Object> vectorRow = new Vector<>(numberOfColumns);
                for (int i = 1; i <= numberOfColumns; i++) {
                    vectorRow.addElement(resultSet.getObject(i));
                }
                vectorData.addElement(vectorRow);

            }//Loop while END

        } catch (SQLException sqlExc) {
            System.out.println(sqlExc);
        } finally {
            try {

                resultSet.close();
                if (resultSet.isClosed()) {
                    System.out.println("resultset closed: " + resultSet.isClosed());
                }//OBRISATI
                sqlStatement.close();
                if (sqlStatement.isClosed()) {
                    System.out.println("statement closed: " + sqlStatement.isClosed());
                }//OBRISATI

            } catch (SQLException sExc) {
                System.out.println(sExc);
            }

        }//finally{} END

        model = new DefaultTableModel(vectorData, vectorColumnNames) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {

                for (int i = 0; i < getRowCount(); i++) {
                    Object object = getValueAt(i, columnIndex);

                    if (object != null) {
                        return object.getClass();
                    }
                }
                return Object.class;
            }
        };

        return model;
    }// getTableModel() END

    protected void insertData() {

        PreparedStatement preparedStatement = null;
        Integer ageInteger = null;
        Double salaryDouble;
        BigDecimal salaryDecimal = null;

        try {

            ageInteger = Integer.parseInt(EntryPanel.txtFieldList.get(2).getText());
            salaryDouble = Double.parseDouble(EntryPanel.txtFieldList.get(4).getText());
            salaryDecimal = BigDecimal.valueOf(salaryDouble);

        } catch (NumberFormatException nfExc) {
            System.out.println(nfExc);
        }

        try {
            /*System.out.println(EntryPanel.txtFieldList.get(1).getText());
            System.out.println(EntryPanel.txtFieldList.get(2).getText());
            System.out.println(ageInteger);
            System.out.println(EntryPanel.txtFieldList.get(3).getText());
            System.out.println(EntryPanel.txtFieldList.get(4).getText());
            System.out.println(salaryDecimal);*/
            preparedStatement = dbConnection.prepareStatement("INSERT INTO javacafe.staff (name, age, address, salary)"
                    + " VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, EntryPanel.txtFieldList.get(1).getText());
            preparedStatement.setInt(2, ageInteger);
            preparedStatement.setString(3, EntryPanel.txtFieldList.get(3).getText());
            preparedStatement.setBigDecimal(4, salaryDecimal);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlExc) {
            System.out.println(sqlExc);
        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    System.out.println(preparedStatement.isClosed());
                }
            } catch (SQLException sqlExc) {
                System.out.println(sqlExc);
            }

        }

    }//insertData() END

    protected void updateData() {

        PreparedStatement preparedStatement = null;
        Integer ageInteger = null;
        Double salaryDouble;
        BigDecimal salaryDecimal = null;
        Integer idInteger = null;

        try {

            ageInteger = Integer.parseInt(EntryPanel.txtFieldList.get(2).getText());
            salaryDouble = Double.parseDouble(EntryPanel.txtFieldList.get(4).getText());
            salaryDecimal = BigDecimal.valueOf(salaryDouble);
            idInteger = Integer.parseInt(EntryPanel.txtFieldList.get(0).getText());

        } catch (NumberFormatException nfExc) {
            System.out.println(nfExc);
        }

        try {

            preparedStatement = dbConnection.prepareStatement("UPDATE javacafe.staff"
                    + " SET name = ?, age = ?, address = ?, salary = ?"
                    + " WHERE staff_id = ?");

            preparedStatement.setString(1, EntryPanel.txtFieldList.get(1).getText());
            preparedStatement.setInt(2, ageInteger);
            preparedStatement.setString(3, EntryPanel.txtFieldList.get(3).getText());
            preparedStatement.setBigDecimal(4, salaryDecimal);
            preparedStatement.setInt(5, idInteger);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlExc) {
            System.out.println(sqlExc);
        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    System.out.println(preparedStatement.isClosed());
                }
            } catch (SQLException sqlExc) {
                System.out.println(sqlExc);
            }
        }

    }//updateData() END

    protected void deleteData() {

        PreparedStatement preparedStatement = null;
        Integer idInteger = null;

        try {

            idInteger = Integer.parseInt(EntryPanel.txtFieldList.get(0).getText());

        } catch (NumberFormatException nfExc) {
            JOptionPane.showMessageDialog(null, "Failed\nNo value found for 'staff_id'", "Error", JOptionPane.WARNING_MESSAGE);
            System.out.println(nfExc);
        }

        try {

            preparedStatement = dbConnection.prepareStatement("DELETE FROM javacafe.staff WHERE staff_id = ?");
            preparedStatement.setInt(1, idInteger);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlExc) {
            System.out.println(sqlExc);
        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    System.out.println(preparedStatement.isClosed());
                }
            } catch (SQLException sqlExc) {
                System.out.println(sqlExc);
            }
        }
    }// deleteData() END

}
