package javamysqlpersistency;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author: Dejan Smiljić; e-mail: dej4n.s@gmail.com
 *
 */
public class TablePanel extends JPanel {

    private DefaultTableModel tableModel;
    protected static JTable table;
    private JScrollPane scrollPane;
    private Border border;
    private DataAccess dataAccess;
    protected static String selectAllQuery;

    public TablePanel() {

        initComponents();

    }

    private void initComponents() {

        this.setPreferredSize(new Dimension(550, 250));
        this.setLayout(new BorderLayout());
        border = BorderFactory.createEmptyBorder(2, 2, 1, 2);
        //border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY);
        this.setBorder(border);
        
        selectAllQuery = "SELECT * FROM staff";
        setTableModel();

        scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int colCount = table.getColumnCount();
                for (int i = 0; i < colCount; i++) {
                    EntryPanel.txtFieldList.get(i).setText(table.getValueAt(table.getSelectedRow(), i).toString());
                }
            }

        });

    }//initComponents() END
    
    protected void setTableModel(){
        
        //get instance of DataAccess class
        dataAccess = DataAccess.getInstance();
        //open connection to DB, this is where unique Connection object is created
        dataAccess.setDataBaseConnection();

        //get jtable model
        tableModel = dataAccess.getTableModel(selectAllQuery);
        //set table model
        table = new JTable(tableModel) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                //disables table cell editing 
                return false;
            }
        };
        
    }// setTableModel() END
    
}
