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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tylypahka.Elokuva;

import java.net.URL;
import java.util.ResourceBundle;

public class ElokuvaController implements ModalControllerInterface<Elokuva>, Initializable {

    @FXML
    private GridPane gridTietue;
    @FXML
    private Label labelVirhe;
    private Elokuva tietueKohdalla;
    private TextField[] edits;
    private int kentta = 0;
    private static final Elokuva aputieto = new Elokuva();


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }


    @FXML
    void handleTallenna() {
        if (tietueKohdalla != null && tietueKohdalla.anna(tietueKohdalla.ekaKentta()).trim().isEmpty()) {
            naytaVirhe("Ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }


    /**
     * Näyttää virheen jos tarvetta.
     * @param virhe teksti joka näytetään virheilmoituksessa.
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
        } else {
            labelVirhe.setText(virhe);
            labelVirhe.getStyleClass().add("virhe");
        }
    }


    @FXML
    void handleLopeta() {
        tietueKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }


    /**
     * Tekee tarvittavat alustukset StringGridiin.
     */
    protected void alusta() {
        edits = luoKentat(gridTietue);

        for (TextField edit : edits) {
            if (edit != null) {
                edit.setOnKeyReleased(event -> muutosElokuvaan(edit));
            }
        }
    }


    /**
     * Käsitellään asukkaan elokuviin liittyviin tietoihin tullut muutos.
     * @param edit muuttunut tekstikenttä.
     */
    private void muutosElokuvaan(TextField edit) {
        if (tietueKohdalla == null) return;
        int k = getFieldId(edit, aputieto.ekaKentta());
        String s = edit.getText();
        String virhe = tietueKohdalla.aseta(k, s);
        if (virhe == null || virhe.isEmpty()) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }

    /**
     * Luodaan GridPaneen elokuvien tiedot kentät.
     * @param gridTietue mihin tiedot luodaan.
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridTietue) {
        gridTietue.getChildren().clear();
        TextField[] edits = new TextField[aputieto.getKenttia()];

        for (int i = 0, k = aputieto.ekaKentta(); k < aputieto.getKenttia(); k++, i++) {
            Label label = new Label(aputieto.getKysymys(k));
            gridTietue.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e" + k);
            gridTietue.add(edit, 1, i);
        }
        return edits;
    }


    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna
     */
    public static int getFieldId(Object obj, int oletus) {
        if (!(obj instanceof Node)) return oletus;
        Node node = (Node) obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }

    @Override
    public Elokuva getResult() {
        return tietueKohdalla;
    }

    @Override
    public void setDefault(Elokuva oletus) {
        tietueKohdalla = oletus;
        alusta();
        naytaTietue(edits, tietueKohdalla);
    }

    @Override
    public void handleShown() {
        kentta = Math.max(tietueKohdalla.ekaKentta(), Math.min(kentta, tietueKohdalla.getKenttia() - 1));
        edits[kentta].requestFocus();
    }


    /**
     * Näytetään asukkaan elokuviin liittyvät tiedot TextField komponentteihin.
     * @param edits taulukko TextFieldeistä joihin näytetään.
     * @param tietue näytettävät tiedot.
     */
    public static void naytaTietue(TextField[] edits, Elokuva tietue) {
        if (tietue == null) return;
        for (int k = tietue.ekaKentta(); k < tietue.getKenttia(); k++) {
            edits[k].setText(tietue.anna(k));
        }
    }


    /**
     * Luodaan asukkaan elokuviin liittyvien tietojen muokkausdialogi.
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param clone mitä dataan näytetään, eli halutun asukkaan tiedot tässä tapauksessa.
     * @param kentta mikä kenttä saa fokuksen kun näytetään.
     * @return null jos painetaan Cancel, muuten täytetyt tiedot
     */
    public static Elokuva kysyTietue(Stage modalityStage, Elokuva clone, int kentta) {
        return ModalController.<Elokuva, ElokuvaController>showModal(ElokuvaController.class.getResource(
                "ElokuvaController.fxml"), "Muokkaa Elokuvan tietoja", modalityStage, clone, ctrl -> ctrl.setKentta(kentta)
        );
    }

    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
}
