import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.*;

public class QuizletPlus {
    
    private static final String FILE_NAME  ="flashcards.json"; // File to store flashcards
    private static String folderPath = "";  // Used to store selected folder path
    private static final String CONFIG_FILE = "config.txt"; //Stores folder path
    private static final String DEFUALT_FILENAME = "flashcards.json";
    private static ArrayList<FlashCard> flashcards = new ArrayList<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        
        loadFolderPath();
        
        if (folderPath.isEmpty())
        {
            selectedFolder();
        }
        
        loadFlashcards(); // Load on start
        System.out.println("Flashcards loaded succesfully!");
        
        // Initializing Frames
        JFrame menuFrame = new JFrame("QuizletPlus");
        JFrame createFrame = new JFrame("Create Set");
        JFrame reviewFrame = new JFrame("Review Set");
          
        menuFrame.setLayout(null);    
        createFrame.setLayout(null);
        reviewFrame.setLayout(null);

        // Setting frame properties
        menuFrame.setSize(900, 600);
        menuFrame.getContentPane().setBackground(new Color(97, 23, 145));
        createFrame.setSize(900, 600);
        createFrame.getContentPane().setBackground(new Color(97, 23, 145));
        reviewFrame.setSize(900, 600);
        reviewFrame.getContentPane().setBackground(new Color(97, 23, 145));

        menuFrame.setResizable(false);
        createFrame.setResizable(false);
        reviewFrame.setResizable(false);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome to QuizletPlus!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 50));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(150, 50, 900, 100);
         
        // Labels
        JLabel createLabel = new JLabel("Create a new set");
        createLabel.setFont(new Font("Arial", Font.BOLD, 30));
        createLabel.setForeground(Color.WHITE);
        createLabel.setBounds(500, 250, 300, 50);
        
        JLabel reviewLabel = new JLabel("Review a set");
        reviewLabel.setFont(new Font("Arial", Font.BOLD, 30));
        reviewLabel.setForeground(Color.WHITE);
        reviewLabel.setBounds(100, 250, 300, 50);
        
        // Buttons
        JButton reviewButton = new JButton("Review");
        reviewButton.setFont(new Font("Arial", Font.BOLD, 30));
        reviewButton.setBounds(120, 300, 150, 50);
        
        JButton createButton = new JButton("Create");
        createButton.setFont(new Font("Arial", Font.BOLD, 30));
        createButton.setBounds(550, 300, 150, 50);
        
        JButton backButton = new JButton("Return");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(30, 50, 100, 50);

        // Back Button Action
        backButton.addActionListener(e -> {
            createFrame.setVisible(false);
            reviewFrame.setVisible(false);
            menuFrame.setVisible(true);
        });

        // Create Button Action
        createButton.addActionListener(e -> {
            menuFrame.setVisible(false);
            createFrame.setVisible(true);
        });

        // Review Button Action
        reviewButton.addActionListener(e -> {
            menuFrame.setVisible(false);
            reviewFrame.setVisible(true);
        });

        // Adding components to menuFrame
        menuFrame.add(welcomeLabel);
        menuFrame.add(reviewLabel);
        menuFrame.add(createLabel);
        menuFrame.add(reviewButton);
        menuFrame.add(createButton);

        // Close Window Handlers
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        reviewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuFrame.setVisible(true);

        /**
         * Flashcard creation UI
         */
        JLabel questionLabel = new JLabel("Enter the question:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 25));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBounds(150, 150, 250, 50);

        JLabel answerLabel = new JLabel("Enter the answer:");
        answerLabel.setFont(new Font("Arial", Font.BOLD, 25));
        answerLabel.setForeground(Color.WHITE);
        answerLabel.setBounds(550, 150, 250, 50);

        // JTextAreas with Wrapping
        JTextArea questionText = new JTextArea(5, 20);
        questionText.setFont(new Font("Arial", Font.PLAIN, 20));
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);

        JScrollPane questionScroll = new JScrollPane(questionText);
        questionScroll.setBounds(135, 200, 250, 100);

        JTextArea answerText = new JTextArea(5, 20);
        answerText.setFont(new Font("Arial", Font.PLAIN, 20));
        answerText.setLineWrap(true);
        answerText.setWrapStyleWord(true);

        JScrollPane answerScroll = new JScrollPane(answerText);
        answerScroll.setBounds(525, 200, 250, 100);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setBounds(350, 350, 150, 50);
        
        // Save Button Function
        saveButton.addActionListener(e -> {
            String question = questionText.getText().trim();
            String answer = answerText.getText().trim();
            
            if (!question.isEmpty() && !answer.isEmpty())
            {
               flashcards.add(new FlashCard(question, answer));
               saveFlashcards();
               JOptionPane.showMessageDialog(createFrame, "Flashcard Saved!");
               questionText.setText("");
               answerText.setText("");
            } 
            else
            { 
               JOptionPane.showMessageDialog(createFrame, "Please enter both a question and answer.");
            }
        });
         
        createFrame.add(questionLabel);
        createFrame.add(answerLabel);
        createFrame.add(questionScroll);
        createFrame.add(answerScroll);
        createFrame.add(saveButton);
        createFrame.add(backButton);
    }

    // Save Flashcard to JSON File
   private static void saveFlashcards()
   {
      File file = new File(folderPath, DEFUALT_FILENAME);
      try (FileWriter writer = new FileWriter(file))
      {
         gson.toJson(flashcards, writer);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   // Load Flashcards from JSON File
   private static void loadFlashcards()
   {
      File file = new File(folderPath, DEFUALT_FILENAME);
      if (!file.exists())
      {
         flashcards = new ArrayList<>();
         return;
      }
      
      try (FileReader reader = new FileReader(file))
      {
         flashcards = gson.fromJson(reader, new TypeToken<ArrayList<FlashCard>>() {}.getType());
         
         if (flashcards == null)
         {
            flashcards = new ArrayList<>();
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   private static void selectedFolder()
   {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Select a folder to save your flashcards");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      
      int result = fileChooser.showOpenDialog(null);
      
      if (result == JFileChooser.APPROVE_OPTION)
      {
         File selectedFolder = fileChooser.getSelectedFile();
         folderPath = selectedFolder.getAbsolutePath();
         saveFolderPath();
      }
      else
      {
         System.out.println("No folder selected. Using defualt location.");
         folderPath = System.getProperty("user.dir");
      }
   }
   
   private static void saveFolderPath()
   {
      try (FileWriter writer = new FileWriter(CONFIG_FILE))
      {
         writer.write(folderPath);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   
   private static void loadFolderPath()
   {
      File file = new File(CONFIG_FILE);
      if (file.exists())
      {
         try (BufferedReader reader = new BufferedReader(new FileReader(file)))
         {
            folderPath = reader.readLine();
         }
         catch (IOException e)
         {
            e.printStackTrace(); 
         }
      }
   }
}

// FlashCard class
class FlashCard {

    String question;
    String answer;
    
    public FlashCard(String q, String a) {
        this.question = q;
        this.answer = a;
    }
}

