package SpellcheckerPackage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Creates a Spellchecker Object.
 * <p>
 * The object will read in a txt file
 * and display found spelling errors.
 * Errors will be shown for editing choices
 * and will update the .txt when done.
 *
 * @author Manuel Armenta Batt
 */
public class Spellchecker extends Application {

    static Stage secondStage = new Stage();
    static Button fileButton;
    static Button skipButton;
    static Button replaceButton;
    private static String word = "";
    static int j = 0;
    static Dictionary dictionary;
    String dictionaryPath = "AllOfTheWords.txt";
    static ArrayList<String> errors = new ArrayList<String>();
    static ArrayList<Integer> places = new ArrayList<Integer>();
    static ArrayList<String> fixedWords = new ArrayList<String>();


    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SpellcheckerPackage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Spellchecker  -Manny Batt");

        //Intro Text formatting
        Text introText = new Text("Welcome to Manny Batt's Spellchecker!");
        introText.setFont(Font.font("Verdana", 24));
        introText.setFill(Color.PURPLE);

        //Directions Text formatting
        Text introText2 = new Text("Upload a .txt file to scan for spelling errors");
        introText2.setFont(Font.font("Verdana", 16));
        introText2.setFill(Color.DARKSLATEGREY);

        /* FileChooser and Buttons formatting
         ** All of the action occurs when a file is chosen from filechooser
         ** Text is automatically processed and results are displayed
         */
        String path = "";
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        fileButton = new Button("Upload");
        fileButton.setOnAction(e -> {
            File chosenFile = filechooser.showOpenDialog(stage);
            documentImport(chosenFile);

        });

        VBox layout = new VBox(10); // Vertical Box
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(introText);
        layout.getChildren().add(introText2);
        layout.getChildren().add(fileButton);

        Scene scene = new Scene(layout, 550, 200);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        initializeDictionary();

    }


    private void initializeDictionary() {
        int numWords = 0;
        dictionary = new Hashtable<String, String>();
        File allWords = new File(dictionaryPath);
        Scanner scan = null;
        try {
            scan = new Scanner(allWords);
        } catch (FileNotFoundException e) {
            System.out.println("File not Found.");
        }

        while (scan.hasNext()) {
            String word = scan.next();
            dictionary.put(word, "A");
            numWords++;
        }
        System.out.println("Dictionary of " + numWords + " words successfully loaded into memory.");
    }


    public static void documentImport(File file) {

        Dictionary documentWords = new Hashtable<String, Integer>();

        Scanner scan = null;
        int counter = 0;
        int wordCount = 0;

        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
        }

        if (scan.hasNext()) {     //Checks for Empty file
            while (scan.hasNext()) {          //Iterate document once and find all errors & places
                word = scan.next();

                if (dictionary.get(word) != null) {   //If word is found in dictionary
                    //move on...
                } else {                              //If word is NOT found in Dictionary
                    errors.add(word);
                    places.add(counter);
                }
            }
            counter++;
        }

        Iterator<String> errorIterator = errors.iterator();
        Iterator<Integer> placesIterator = places.iterator();

        int z=0;
        while(z<errors.size()){
            buttonDoer(errorIterator, placesIterator);
            z++;
        }
    }

    private static void buttonDoer(Iterator<String> errorIterator, Iterator<Integer> placesIterator) {
        word = errorIterator.next();
        placesIterator.next();

        Text seTitle = new Text("Spelling Errors");
        seTitle.setFont(Font.font("Bell MT", 24));
        seTitle.setFill(Color.PURPLE);

        Text info = new Text("The following words have been found to be misspelled.\nChose your actions bellow");
        info.setFont(Font.font("Bell MT", 18));
        info.setFill(Color.GRAY);

        Text errorText = new Text(word);
        errorText.setFont(Font.font("Bell MT", 22));
        errorText.setFill(Color.DARKRED);

        TextField updateField = new TextField(word);

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            fixedWords.add(updateField.getText());
            buttonDoer(errorIterator, placesIterator);
        });

        VBox replaceLayout = new VBox(5);
        replaceLayout.setAlignment(Pos.CENTER);
        replaceLayout.getChildren().add(seTitle);
        replaceLayout.getChildren().add(info);
        replaceLayout.getChildren().add(errorText);
        replaceLayout.getChildren().add(updateField);
        replaceLayout.getChildren().add(nextButton);

        Scene secondScene = new Scene(replaceLayout, 550, 550);
        secondStage.setScene(secondScene);
        secondStage.show();
    }

    private static void updateWord(String next) {
        word = next;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
