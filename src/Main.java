import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;

public class Main extends JFrame {
    private JPanel MainPanel;
    private JButton newNoteButton;
    private JPanel Notes = new JPanel(new GridLayout(5,1,5,5));
    public JTable table;

    public Main() {

        JFrame frame = new JFrame("My Notepad"); //Creating new JFrame and setting its properties
        frame.setContentPane(MainPanel);
        frame.setTitle("My Notepad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        newNoteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { //When the new note button is clicked, the current frame is disposed and a new frame is created
                super.mouseClicked(e);
                frame.dispose();
                new NewNote();
            }
        });

        DefaultTableModel model = new DefaultTableModel(); //Creating a new table model and adding its columns
        model.addColumn("Title");
        model.addColumn("Date Modified");
        model.addColumn("Edit");
        model.addColumn("Delete");

        table.setModel(model); //Setting the table model to the table
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null); //Disabling editing of the table
        table.setShowGrid(false); //Hiding the grid lines

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); //Creating a new table cell renderer and setting its alignment to center
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 2; i < table.getColumnCount(); i++) { //Setting the alignment of the columns to center (Just the last two columns -- Edit and Delete)
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        String docpath = System.getProperty("user.home") + File.separator + "Documents"; //Getting the path of the documents folder
        File path = new File(docpath + File.separator + "My Notepad"); //Creating a new file object with the path of the My Notepad folder
        File[] files = path.listFiles(); //Getting all the files in the My Notepad folder
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"); //Creating a new date format

        if (files != null) {
            for (File file : files) { //Files in the files array are added to the table with the loop
                if (file.isFile()) {
                    DefaultTableModel model1 = (DefaultTableModel) table.getModel();
                    model1.addRow(new Object[]{file.getName().replace(".txt", ""), sdf.format(file.lastModified()), "Edit", "Delete"});
                }
            }
        }


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { //When a cell is clicked, the row and column of the cell is retrieved
                super.mouseClicked(e);
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (col == 2 || col == 0 || col == 1) { //If the column is  Title, Date Modified or Edit, the title of the note is retrieved and the frame is disposed and a new frame is created with the title of the note
                    String title = table.getValueAt(row, 0).toString() + ".txt";
                    String docpath = System.getProperty("user.home") + File.separator + "Documents";
                    File path = new File(docpath + File.separator + "My Notepad");
                    File[] files = path.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {
                                if (file.getName().equals(title)) {
                                    frame.dispose();
                                    new NewNote(title);
                                }
                            }
                        }
                    }
                } else if (col == 3) { //If the column is Delete, the title of the note is retrieved and a confirmation dialog is shown. If the user clicks yes, the note is deleted and the frame is disposed and a new frame is created
                    String title = table.getValueAt(row, 0).toString() + ".txt";
                    String docpath = System.getProperty("user.home") + File.separator + "Documents";
                    File path = new File(docpath + File.separator + "My Notepad");
                    File[] files = path.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {
                                if (file.getName().equals(title)) {
                                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this note?", "Delete", JOptionPane.YES_NO_OPTION);
                                    if (result == JOptionPane.YES_OPTION) {
                                        file.delete();
                                        dispose();
                                        refresh();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }

    public void refresh() { //This method is used to refresh the table
        String docpath = System.getProperty("user.home") + File.separator + "Documents";
        File path = new File(docpath + File.separator + "My Notepad");
        File[] files = path.listFiles();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        DefaultTableModel model1 = (DefaultTableModel) table.getModel();


        while (table.getRowCount() > 0) {
            model1.removeRow(0);
        }

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    model1.addRow(new Object[]{file.getName().replace(".txt", ""), sdf.format(file.lastModified()), "Edit", "Delete"});
                }
            }
        }
    }
}
