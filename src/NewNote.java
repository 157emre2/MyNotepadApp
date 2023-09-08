import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class NewNote{

    private JPanel NewNotePanel;
    private JButton saveButton;
    private JTextField titleTextField;
    private JTextArea yourNotesHereTextArea;

    public NewNote() { //This constructor is used when the user wants to create a new note
        JFrame frame = new JFrame("New Note"); //Creating new JFrame and setting its properties
        frame.setContentPane(NewNotePanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { //When the window is closed, the current frame is disposed and a new frame is created
                super.windowClosing(e);
                if (titleTextField.getText().equals("Title..") && yourNotesHereTextArea.getText().equals("Your Notes Here..")) { //If the title and notes are the default values, the frame is disposed and a new frame is created
                    frame.dispose();
                    new Main();
                } else { //If the title and notes are not the default values, a confirmation dialog is shown
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit without saving?", "Exit", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        frame.dispose();
                        new Main();
                    }
                }
            }
        });
        titleTextField.addFocusListener(new FocusAdapter() { //When the title text field is focused, the default value is removed
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (titleTextField.getText().equals("Title..")) {
                    titleTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) { //When the title text field is unfocused, the default value is added if the text field is empty
                super.focusLost(e);
                if (titleTextField.getText().isEmpty()) {
                    titleTextField.setText("Title..");
                }
            }
        });
        yourNotesHereTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { //When the notes text area is focused, the default value is removed
                super.focusGained(e);
                if (yourNotesHereTextArea.getText().equals("Your Notes Here..")) {
                    yourNotesHereTextArea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) { //When the notes text area is unfocused, the default value is added if the text area is empty
                super.focusLost(e);
                if (yourNotesHereTextArea.getText().isEmpty()) {
                    yourNotesHereTextArea.setText("Your Notes Here..");
                }
            }
        });
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { //When the save button is clicked, the title and notes are retrieved and saved to a file
                super.mouseClicked(e);
                String title = titleTextField.getText();
                String notes = yourNotesHereTextArea.getText();
                String docpath = System.getProperty("user.home") + File.separator + "Documents";
                File path = new File(docpath + File.separator + "My Notepad");
                try {
                    if (!path.exists()) { //If the path does not exist, it is created
                        path.mkdir();
                    }
                    File file = new File(path +"\\" +title + ".txt");
                    if (!file.exists()) { //If the file does not exist, it is created
                        file.createNewFile();
                    }else { //If the file exists, a confirmation dialog is shown to confirm if the user wants to overwrite the file
                        int result = JOptionPane.showConfirmDialog(null, "File already exists. Do you want to overwrite?", "File Exists", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    FileWriter fileWriter = new FileWriter(file); //The notes are written to the file
                    fileWriter.write(notes);
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Note Saved!");

                } catch (Exception ioException) {
                    System.out.println("Error "  + ioException);
                }

                frame.dispose();
                new Main();
            }
        });
    }

    public NewNote(String title) { //This constructor is used when the user wants to edit a note
        JFrame frame = new JFrame("Edit Note"); //Creating new JFrame and setting its properties
        frame.setContentPane(NewNotePanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        String docpath = System.getProperty("user.home") + File.separator + "Documents";
        File path = new File(docpath + File.separator + "My Notepad");
        File file = new File(path + "\\" + title);


        titleTextField.setText(title.replace(".txt", ""));
        yourNotesHereTextArea.setText(""); //Clearing the text area so that the notes can be added to it from the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file)); //Reading the file and adding the notes to the text area
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                yourNotesHereTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String note = yourNotesHereTextArea.getText();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (titleTextField.getText().equals(title) && yourNotesHereTextArea.getText().equals(note)) { //If the title and notes are the same as the original, the frame is disposed and a new frame is created
                    frame.dispose();
                    new Main();
                } else { //If the title and notes are not the same as the original, a confirmation dialog is shown
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit without saving?", "Exit", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        frame.dispose();
                        new Main();                     }
                }
            }
        });

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { //When the save button is clicked, the title and notes are retrieved and saved to a file
                super.mouseClicked(e);
                String title = titleTextField.getText();
                String notes = yourNotesHereTextArea.getText();

                try {
                    if (!path.exists()) { //If the path does not exist, it is created
                        path.mkdir();
                    }
                    File file = new File(path +"\\" +title + ".txt"); //If the file does not exist, it is created
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file); //The notes are written to the file
                    fileWriter.write(notes);
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Note Saved!");

                } catch (Exception ioException) {
                    System.out.println("Error "  + ioException);
                }

                frame.dispose();
                new Main();
            }
        });
    }
}
