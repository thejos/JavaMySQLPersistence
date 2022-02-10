package javamysqlpersistency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author: Dejan Smiljić; e-mail: dej4n.s@gmail.com
 *
 */
public class CustomActiontListener implements ActionListener { 
    
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "insert":
                //Getting connection to database and processing data insertion
                DataAccess.getInstance().setDataBaseConnection();
                DataAccess.getInstance().insertData();
                //Setting up a TableModel so data is shown immediately upon insert, update, delete actions
                DefaultTableModel dtmRetrieveAfterInsert = DataAccess.getInstance().getTableModel(TablePanel.selectAllQuery);
                TablePanel.table.setModel(dtmRetrieveAfterInsert);
                break;
            case "update":
                DataAccess.getInstance().setDataBaseConnection();
                DataAccess.getInstance().updateData();
                DefaultTableModel dtmRetrieveAfterUpdate = DataAccess.getInstance().getTableModel(TablePanel.selectAllQuery);
                TablePanel.table.setModel(dtmRetrieveAfterUpdate);
                break;
            case "delete":
                DataAccess.getInstance().setDataBaseConnection();
                DataAccess.getInstance().deleteData();
                DefaultTableModel dtmRetrieveAfterDelete = DataAccess.getInstance().getTableModel(TablePanel.selectAllQuery);
                TablePanel.table.setModel(dtmRetrieveAfterDelete);
                break;
            case "search":
                //Getting input values
                String searchCriteria = (String) ControlPanel.searchComboBox.getSelectedItem();
                String searchParameter = ControlPanel.searchTxtField.getText()+"%";
                //Creating SQL query
                String sqlSelectByCriteria = "SELECT * FROM javacafe.staff WHERE " + searchCriteria +" LIKE '" + searchParameter +"'";
                //Getting DefaultTableModel
                DefaultTableModel dataModelSearch = DataAccess.getInstance().getTableModel(sqlSelectByCriteria);
                //Setting DefaultTableModel for static object "table"
                TablePanel.table.setModel(dataModelSearch);
                ControlPanel.buttonRetrieveData.setVisible(true);
                //ControlPanel.searchTxtField.setText("");
                //ControlPanel.buttonSearch.setEnabled(false);
                break;
            case "retrieve":
                DefaultTableModel dataModelRetrieve = DataAccess.getInstance().getTableModel(TablePanel.selectAllQuery);
                TablePanel.table.setModel(dataModelRetrieve);
                ControlPanel.searchTxtField.setText("");
                ControlPanel.buttonSearch.setEnabled(false);
                ControlPanel.buttonRetrieveData.setVisible(false);
                break;
            default:
                break;
        }
    }

}
