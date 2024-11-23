package fxAsukkaat;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;

import java.util.Optional;

public class TulostaController implements ModalControllerInterface <String>{

    @FXML
    TextArea tulostusAlue;

    @FXML
    void handleTulosta() {
    	// Dialogs.showMessageDialog("Ei osata vielä tulostaa");
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)){
            WebEngine webEngine = new WebEngine();
            webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
            webEngine.print(job);
            job.endJob();
        }
    }

    @FXML void handleSulje(){
        lopeta();
    }

    /*
    * Näyttää tulostusalueessa tekstin
    * @param tulostus tulostettava teskti
    */
   public static TulostaController tulosta(String tulostus) {
       TulostaController tulostusCtrl =
       ModalController.showModeless(TulostaController.class.getResource("TulostaView.fxml"),
               "Tulostus", tulostus);
       return tulostusCtrl;
   }

    @Override
    public String getResult() {
        return null;
    }

    @Override
    public void setDefault(String s) {
        tulostusAlue.setText(s);
    }

    @Override
    public void handleShown() {

    }


    /**
     * @return alue johon tulostetaan
     */
    public TextArea getTextArea() {
        return tulostusAlue;
    }


    /**
     * Luo dialogin, jossa käyttäjältä kysytään, että haluaako varmasti poistua tallentamatta.
     */
    private void lopeta(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sulje");
        alert.setHeaderText(null);
        alert.setContentText("Haluatko varmasti sulkea tallentamatta?");

        ButtonType buttonTypeYes = new ButtonType("Kyllä", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Ei", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if ( result.get() == buttonTypeYes ) ModalController.closeStage(tulostusAlue);
    }
}
