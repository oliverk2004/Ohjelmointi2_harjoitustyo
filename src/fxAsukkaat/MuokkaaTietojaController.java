package fxAsukkaat;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tylypahka.Asukas;
import tylypahka.SailoException;
import tylypahka.Tylypahka;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MuokkaaTietojaController implements ModalControllerInterface<Asukas>, Initializable {



    @FXML
    private TextField editHvari;

    @FXML
    private TextField editMorko;

    @FXML
    private TextField editNimi;

    @FXML
    private TextField editSukupuoli;


    @FXML
    private TextField editSyntypera;

    @FXML
    private TextField editTaikasauva;

    @FXML
    private TextField editTupa;

    @FXML
    private Label labelVirhe;

    @FXML
    private GridPane gridAsukas;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();

    }

    @Override
    public Asukas getResult() {
        return asukasKohdalla;
    }

    @Override
    public void setDefault(Asukas oletus) {
        asukasKohdalla = oletus;
        naytaAsukas(edits, asukasKohdalla);
    }

    @Override
    public void handleShown() {
        kentta = Math.max(apuasukas.ekaKentta(), Math.min(kentta, apuasukas.getKenttia()-1));
        edits[kentta].requestFocus();
    }

    @FXML void handleTallenna(){
        if(asukasKohdalla != null && asukasKohdalla.getNimi().trim().equals("")){
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    @FXML void handleLopeta() {
        asukasKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    @FXML void handleTulosta(){
        // tallenna();
        tulosta();
    }
    @FXML void handleOhje(){ ohje();}


    // ======================================================================================================
    private Asukas asukasKohdalla;
    private Tylypahka tylypahka;
    private static final Asukas apuasukas = new Asukas(); // Voidaan kysyä tietoja.
    private TextField[] edits;
    private int kentta = 0;


    /**
     * Luodaan GridPaneen asukkaiden tiedot kentät.
     * @param gridAsukas mihin tiedot luodaan.
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridAsukas){
        gridAsukas.getChildren().clear();
        TextField[] edits = new TextField[apuasukas.getKenttia()];

        for (int i = 0, k = apuasukas.ekaKentta(); k < apuasukas.getKenttia(); k++, i++){
            Label label = new Label(apuasukas.getKysymys(k));
            gridAsukas.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridAsukas.add(edit, 1, i);
        }
        return edits;
    }


    /**
     * Tekee tarvittavat alustukset tekstikenttiin.
     */
    protected void alusta() {
        edits = luoKentat(gridAsukas);

        for (TextField edit : edits){
            if (edit != null){
                edit.setOnKeyReleased(event -> muutosAsukkaaseen((TextField)(event.getSource())));
            }
        }
    }


    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }


    /**
     * Käsitellään asukkaaseen tullut muutos.
     * @param edit muuttunut tekstikenttä.
     */
    private void muutosAsukkaaseen(TextField edit) {
        if(asukasKohdalla == null) return;
        int k = getFieldId(edit, apuasukas.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = asukasKohdalla.aseta(k, s);
        if (virhe == null){
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        }
        else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }


    /**
     * Näyttää virheen jos tarvetta.
     * @param virhe teksti joka näytetään virheilmoituksessa.
     */
    private void naytaVirhe(String virhe){
        if(virhe == null || virhe.isEmpty()){
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }

        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");

    }


    /**
     * Näytetään asukkaan tiedot TextField komponentteihin.
     * @param edits taulukko TextFieldeistä joihin näytetään.
     * @param asukas näytettävä asukas.
     */
    public static void naytaAsukas(TextField[] edits, Asukas asukas) {
        if(asukas == null) return;
        for (int k = asukas.ekaKentta(); k < asukas.getKenttia(); k++){
            edits[k].setText(asukas.anna(k));
        }
    }





    /**
     * Luodaan asukkaan tietojen muokkausdialogi.
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param clone mitä dataan näytetään, eli halutun asukkaan tiedot tässä tapauksessa.
     * @param kentta mikä kenttä saa fokuksen kun näytetään.
     * @return null jos painetaan Cancel, muuten täytetyt tiedot
     */
    public static Asukas kysyAsukas(Stage modalityStage, Asukas clone, int kentta) {
        return ModalController.<Asukas, MuokkaaTietojaController>showModal(MuokkaaTietojaController.class.getResource(
                "MuokkaaTietoja.fxml"), "Muokkaa Tietoja", modalityStage, clone, ctrl -> ctrl.setKentta(kentta));
    }


    private void setKentta(int kentta) {
        this.kentta = kentta;
    }


    /**
     * Tallentaa kyseisen session. Näyttää siis tällä hetkellä dialogin.
     */
    private void tallenna() {
        try {
            tylypahka.tallenna("data");
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
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
        if ( result.get() == buttonTypeYes ) ModalController.closeStage(editNimi);
    }


    /**
     * Jos joskus haluaisit tehdä sellaisen dialogin, josta pystyisi tulostamaan...
     */
    private void tulosta(){
        Dialogs.showMessageDialog("Vielä ei osata tulostaa!");
    }


    /**
     * Ohjeen avaamiseen tarkoitettu aliohjelma.
     */
    private void ohje(){
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2024k/ht/olieemka");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }


}


