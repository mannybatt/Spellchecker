package SpellcheckerPackage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    boolean doneWithFile = false;
    static Dictionary dictionary;
    String dictionaryPath = "C:\\Users\\Manny\\IdeaProjects\\Spellchecker\\src\\SpellcheckerPackage\\AllOfTheWords.txt";


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


    private static void documentImport(File file) {

        Dictionary documentWords = new Hashtable<String, Integer>();
        ArrayList<String> errors = new ArrayList<String>();
        ArrayList<Integer> places = new ArrayList<Integer>();
        ArrayList<String> fixedWords = new ArrayList<String>();
        Scanner scan = null;
        int counter = 0;
        int wordCount = 0;

        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
        }

        //Scene spellingErrorScene;

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

        word = errorIterator.next();
        placesIterator.next();
        //fixedWords.add(wordExamination(word));

        Text seTitle = new Text("Spelling Errors");
        seTitle.setFont(Font.font("Bell MT", 24));
        seTitle.setFill(Color.PURPLE);

        Text info = new Text("The following words have been found to be misspelled.\nChose your actions bellow");
        info.setFont(Font.font("Bell MT", 18));
        info.setFill(Color.GRAY);

        Text errorText = new Text(word);
        errorText.setFont(Font.font("Bell MT", 22));
        errorText.setFill(Color.DARKRED);

        TextField updateField = new TextField("Type a replacement");

        VBox replaceLayout = new VBox(5);
        replaceLayout.setAlignment(Pos.CENTER);
        replaceLayout.getChildren().add(seTitle);
        replaceLayout.getChildren().add(info);
        replaceLayout.getChildren().add(errorText);
        replaceLayout.getChildren().add(updateField);
        //replaceLayout.getChildren().add(replaceButton);
        //replaceLayout.getChildren().add(skipButton);

        Scene secondScene = new Scene(replaceLayout, 550, 550);




        replaceButton = new Button("Replace");
        replaceButton.setOnAction(e -> {
            fixedWords.add(updateField.getText());

            updateWord(errorIterator.next());

            placesIterator.next();
            //fixedWords.add(wordExamination(word));

            Text sTitle = new Text("Spelling Errors");
            seTitle.setFont(Font.font("Bell MT", 24));
            seTitle.setFill(Color.PURPLE);

            Text nfo = new Text("The following words have been found to be misspelled.\nChose your actions bellow");
            info.setFont(Font.font("Bell MT", 18));
            info.setFill(Color.GRAY);

            Text eText = new Text(word);
            errorText.setFont(Font.font("Bell MT", 22));
            errorText.setFill(Color.DARKRED);

            TextField uField = new TextField("Type a replacement");

            VBox rLayout = new VBox(5);
            rLayout.setAlignment(Pos.CENTER);
            rLayout.getChildren().add(sTitle);
            rLayout.getChildren().add(nfo);
            rLayout.getChildren().add(eText);
            rLayout.getChildren().add(uField);
            //rLayout.getChildren().add(replaceButton);
            //rLayout.getChildren().add(skipButton);

            secondStage.setScene(secondScene);
            //secondStage.show();
        });

        skipButton = new Button("Skip");
        skipButton.setOnAction(e -> {
            fixedWords.add(word);

            updateWord(errorIterator.next());
            placesIterator.next();

            Text sTitle = new Text("Spelling Errors");
            seTitle.setFont(Font.font("Bell MT", 24));
            seTitle.setFill(Color.PURPLE);

            Text nfo = new Text("The following words have been found to be misspelled.\nChose your actions bellow");
            info.setFont(Font.font("Bell MT", 18));
            info.setFill(Color.GRAY);

            Text eText = new Text(word);
            errorText.setFont(Font.font("Bell MT", 22));
            errorText.setFill(Color.DARKRED);

            TextField uField = new TextField("Type a replacement");

            VBox rLayout = new VBox(5);
            rLayout.setAlignment(Pos.CENTER);
            rLayout.getChildren().add(sTitle);
            rLayout.getChildren().add(nfo);
            rLayout.getChildren().add(eText);
            rLayout.getChildren().add(uField);
            //rLayout.getChildren().add(replaceButton);
            //rLayout.getChildren().add(skipButton);

            secondStage.setScene(secondScene);
            secondStage.show();
        });
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
