package fxAsukkaat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tylypahka.Tylypahka;



/**
 * @author Oliver Kandén
 * @version 27.1.2024
 * Pääohjelma ohjelman käynnistämiseksi.
 */
public class AsukkaatMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("AsukkaatGUIView.fxml")); // OK
            final Pane root = ldr.load();
            final AsukkaatGUIController asukkaatCtrl = (AsukkaatGUIController)ldr.getController();


            Scene scene = new Scene(root); 
            scene.getStylesheets().add(getClass().getResource("asukkaat.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tylypahkan asukasrekisteri");
            primaryStage.show();

            Tylypahka asukkaat = new Tylypahka();
            asukkaatCtrl.setTylypahka(asukkaat);
            asukkaatCtrl.lueTiedosto();
            
            primaryStage.show();
            
            // if ( !asukkaatCtrl.avaa() ) Platform.exit(); // Avaa TODO: Mieti tarvitsetko edes...

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}