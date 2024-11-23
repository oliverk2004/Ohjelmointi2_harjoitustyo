package fxAsukkaat;

import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import tylypahka.Asukas;
import tylypahka.Elokuva;
import tylypahka.SailoException;
import tylypahka.Tylypahka;

import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static fxAsukkaat.MuokkaaTietojaController.getFieldId;

/**
 * @author Oliver Kandén
 * @version 27.1.2024
 */
public class AsukkaatGUIController implements Initializable{

    @FXML private ListChooser<Asukas> chooserAsukkaat;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private RadioButtonChooser<String> tuvanValinta;
    @FXML private BorderPane panelAsukas;
    @FXML private TextField editHvari;
    @FXML private TextField editMorko;
    @FXML private TextField editNimi;
    @FXML private TextField editSukupuoli;
    @FXML private TextField editSyntypera;
    @FXML private TextField editTaikasauva;
    @FXML private TextField editTupa;
    @FXML private TextField hakuehto;
    @FXML private StringGrid<Elokuva> tableElokuvanTiedot;
    @FXML private GridPane gridAsukas;
    @FXML private Label labelValittu;


    @FXML void handleTallenna() {
        tallenna();
    } // OK
    @FXML void handleLopeta() {
        tallenna();
        lopeta();
    } // OK
    @FXML void handleOhje(){ ohje(); } // OK, kaikki toimii.
    @FXML void handleHakuehto(){
        hae(0);
    }
    @FXML void handleValitseTupa(){
        haeTupa();
    }



    @FXML
    void handleTulosta() {
        TulostaController tulostusCtrl = TulostaController.tulosta(null);
        tulostaValitut(tulostusCtrl.getTextArea());
    }


    @FXML
    void LuoAsukas() {
        tallenna();
        // luoAsukas();
        uusiAsukas();
    }


    @FXML
    void MuokkaaTietoja() throws SailoException{
        // tallenna();
        muokkaaTietoja(0);
    }


    @FXML
    void PoistaAsukas(){
        tallenna();
        poistaAsukas();
    }


    @FXML
    void PoistaElokuvienTiedot(){
        poistaElokuvienTiedot();
    }


    @FXML
    void AvaaLisaaTietoja(){
        tallenna();
        uudetTiedot();
    }


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
        tuvanValinta.addSelectionListener((e) -> nayta());
        panelAsukas.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.DELETE){
        poistaAsukas();}
        });
    }


    /**
     * Näyttää radiobuttoneiden yläpuolella, että minkä tuvan käyttäjä on valinnut. Ehkä vähän turha.
     */
    private void nayta() {
        String s = tuvanValinta.getSelectedText();
        if ( s == null ) return;
        labelValittu.setText("Valittu: " + s);
    }

    // ===================================================================================================================================

    private Tylypahka tylypahka;
    private Asukas asukasKohdalla;
    private TextField[] edits;

    private static final Asukas apuasukas = new Asukas();
    private static final Elokuva aputieto = new Elokuva();

    private int kentta = 0;



    /**
     * Tekee tarvittavat muut alustukset. Nyt on vaihdettu GridPanen tilalle yksi iso tekstikenttä, johon voidaan
     * tulostaa asukkaan tiedot.
     * HT5 VAIHE: nyt siihen tulostuu myös elokuviin liittyvät tiedot siihen samaan.
     * "Avaa lisää tietoja".
     */
    private void alusta() {
        chooserAsukkaat.clear();
        chooserAsukkaat.addSelectionListener(e -> naytaAsukas());

        cbKentat.clear();
        for (int k = apuasukas.ekaKentta(); k < apuasukas.getKenttia(); k++)
            cbKentat.add(apuasukas.getKysymys(k), null);
        cbKentat.getSelectionModel().select(0);

        edits = MuokkaaTietojaController.luoKentat(gridAsukas);
        for (TextField edit : edits) if (edit != null){
            edit.setEditable(false);
            edit.setOnMouseClicked(e -> { if(e.getClickCount()>1) {
                try {
                    muokkaaTietoja(getFieldId(e.getSource(),0));
                } catch (SailoException ex) {
                    throw new RuntimeException(ex);
                }
            }
            });
            edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
        }

        int eka = aputieto.ekaKentta();
        int lkm = aputieto.getKenttia();
        String[] headings = new String[lkm - eka];
        for (int i = 0, k = eka; k < lkm; i++, k++) headings[i] = aputieto.getKysymys(k);
        tableElokuvanTiedot.initTable(headings);
        tableElokuvanTiedot.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableElokuvanTiedot.setEditable(false);
        tableElokuvanTiedot.setPlaceholder(new Label("Kyseinen asukas ei esiinny elokuvissa."));

        tableElokuvanTiedot.setColumnSortOrderNumber(1);
        tableElokuvanTiedot.setColumnSortOrderNumber(2);
        tableElokuvanTiedot.setColumnWidth(1,60);

        tableElokuvanTiedot.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaElokuvaa(); } );

    }


    /**
     * Näyttää listasta valitun asukkaan tiedot, tilapäisesti tekstikenttään.
     */
    protected void naytaAsukas() {
        asukasKohdalla = chooserAsukkaat.getSelectedObject();
        if(asukasKohdalla == null) return;

        MuokkaaTietojaController.naytaAsukas(edits, asukasKohdalla);
        naytaElokuvanTiedot(asukasKohdalla);


    }


    /**
     * Pystytään muokkaamaan asukkaan tietoja.
     * @throws SailoException heittää poikkeuksen
     */
    public void muokkaaTietoja(int k) throws SailoException{
        Asukas asukasKohdalla = chooserAsukkaat.getSelectedObject();

        if(asukasKohdalla == null) return;
        try {
            Asukas asukas;
            asukas = MuokkaaTietojaController.kysyAsukas(null, asukasKohdalla.clone(), k);
            if (asukas == null) return;
            tylypahka.korvaaTaiLisaa(asukas);
            hae(asukas.getId());
        } catch (CloneNotSupportedException e){
            //
        }
    }


    /**
     * Pystytään muokkaamaan asukkaan elokuviin liittyviä tietoja.
     */
    public void muokkaaElokuvaa(){
        int r = tableElokuvanTiedot.getRowNr();
        if (r < 0) return;
        Elokuva tietueKohdalla = tableElokuvanTiedot.getObject();
        if (tietueKohdalla == null) return;
        int k = tableElokuvanTiedot.getColumnNr()+tietueKohdalla.ekaKentta();
        try{
            Elokuva uusi;
            uusi = ElokuvaController.kysyTietue(null, tietueKohdalla.clone(), k);
            if (uusi == null) return;
            tylypahka.korvaaTaiLisaa(uusi);
            naytaElokuvanTiedot(asukasKohdalla);
            tableElokuvanTiedot.selectRow(r);
        } catch (SailoException | CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Tallentaa kyseisen session. Näyttää siis tällä hetkellä dialogin.
     */
    public void tallenna() {
        try {
            tylypahka.tallenna("data");
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    /**
     * Lopetus aliohjelma.
     */
    private void lopeta(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sulje");
        alert.setHeaderText(null);
        alert.setContentText("Haluatko poistua?");

        ButtonType buttonTypeYes = new ButtonType("Kyllä", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Ei", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if ( result.get() == buttonTypeYes ) Platform.exit();
    }




    /**
     * Ohje avautuu selaimeen.
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




    /**
     * Poistaa valitun asukkaan.
     */
    private void poistaAsukas(){
        Asukas asukas = asukasKohdalla;
        if (asukas == null) return;
        if (!Dialogs.showQuestionDialog("Poista asukas", "Haluatko varmasti poistaa " + asukas.getNimi()
        , "Kyllä", "En"))
            return;
        tylypahka.poista(asukas);
        int indeksi = chooserAsukkaat.getSelectedIndex();
        hae(0);
        chooserAsukkaat.setSelectedIndex(indeksi);
    }

    /**
     * Poistaa valitut elokuvien tiedot.
     */
    private void poistaElokuvienTiedot(){
        int rivi = tableElokuvanTiedot.getRowNr();
        if (rivi < 0) return;
        Elokuva tiedot = tableElokuvanTiedot.getObject();
        if (tiedot == null) return; // Eli jos asukkaalla ei ole elokuviin liittyviä tietoja olemassa.
        tylypahka.poistaElokuvienTiedot(tiedot);
        naytaElokuvanTiedot(asukasKohdalla);
        int tietoja = tableElokuvanTiedot.getItems().size();
        if (rivi >= tietoja) rivi = tietoja -1;
        tableElokuvanTiedot.getFocusModel().focus(rivi);
        tableElokuvanTiedot.getSelectionModel().select(rivi);
    }


    /**
     * Alustaa kerhon lukemalla sen valitun nimisestä tiedostosta
     */
    protected void lueTiedosto() {
        try {
            tylypahka.lueTiedostosta("data");
            hae(0);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    /**
     * Haetaan asukkaat uudelleen
     * @param id aktiivisen asukkaan id numero
     */
    public void hae(int id) {
        int asukkaanId = id; // Asukaan id joka aktivoidaan haun jälkeen.
        if(asukkaanId <= 0){
            Asukas kohdalla = asukasKohdalla;
            if (kohdalla != null) asukkaanId = kohdalla.getId();
        }

        int k = cbKentat.getSelectionModel().getSelectedIndex() + apuasukas.ekaKentta();
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
        chooserAsukkaat.clear();

        int indeksi = 0;
        try{
            Collection<Asukas> asukkaat = tylypahka.etsi(ehto, k);
            int i = 0;
            for (Asukas asukas : asukkaat){
                if (asukas.getId() == asukkaanId) indeksi = i;
                chooserAsukkaat.add(asukas.getNimi(), asukas);
                i++;
            }
        } catch (SailoException ex){
            Dialogs.showMessageDialog("Haku epäonnistui" + ex.getMessage());
        }
        chooserAsukkaat.setSelectedIndex(indeksi); // Tästä tulee muutosviesti joka näyttää kyseisen asukkaan.

    }


    /**
     * Haetaan käyttäjän valitseman Radiobuttonin perusteella eli tuvan mukaan lajitellaan ne ListChooseriin.
     */
    private void haeTupa() {
        int asukkaanId = 0;
        Asukas kohdalla = asukasKohdalla;
        if (kohdalla != null) asukkaanId = kohdalla.getId();


        if (tuvanValinta.getSelectedIndex() == 0) return;
        int k = 2; // Kaikki toimii
        String ehto = tuvanValinta.getSelectedText();
        chooserAsukkaat.clear();

        int indeksi = 0;
        try{
            Collection<Asukas> asukkaat = tylypahka.etsi(ehto, k);
            int i = 0;
            for (Asukas asukas : asukkaat){
                if (asukas.getId() == asukkaanId) indeksi = i;
                chooserAsukkaat.add(asukas.getNimi(), asukas);
                i++;
            }
        } catch (SailoException ex){
            Dialogs.showMessageDialog("Haku epäonnistui" + ex.getMessage());
        }
        chooserAsukkaat.setSelectedIndex(indeksi); // Tästä tulee muutosviesti joka näyttää kyseisen asukkaan.

    }


    /**
     * Asetetaan käytettävät asukkaat
     * @param asukkaat jota käytetään
     */
    public void setTylypahka(Tylypahka asukkaat) {
        this.tylypahka = asukkaat;
    }


    /**
     * Uusi asukas ListChooseriin aliohjelma
     */
    public void uusiAsukas() {
        Asukas uusi = new Asukas();
        uusi.rekisteroi();
        uusi.taytaTylypahkanAsukas();
        try {
            tylypahka.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa" + e.getMessage());
        }
        hae(uusi.getId());


    }


    /**
     * Lisää uudet elokuvan tiedot.
     */
    public void uudetTiedot(){
        if (asukasKohdalla == null) return;
        Elokuva uusi = new Elokuva(asukasKohdalla.getId());
        uusi.rekisteroi();
        uusi.taytaTiedot(asukasKohdalla.getId());
        tylypahka.lisaa(uusi);
        hae(asukasKohdalla.getId());
        tableElokuvanTiedot.selectRow(1000);
    }

    /**
     * Näyttää annetun asukkaan liittyvät elokuvatiedot StringGridissä..
     * @param asukas Asukas, jonka elokuvatiedot näytetään.
     */
    private void naytaElokuvanTiedot(Asukas asukas) {
        tableElokuvanTiedot.clear();
        if (asukas == null) return;
        List<Elokuva> tiedot = tylypahka.annaElokuvanTiedot(asukas);
        if (tiedot.isEmpty()) return;
        for (Elokuva tieto : tiedot)
            naytaTieto(tieto);
    }


    /**
     * Näyttää yksittäisen elokuvatiedon StringGridissä.
     * @param tieto Elokuva-olio, jonka tiedot näytetään.
     */
    private void naytaTieto(Elokuva tieto) {
        int kenttia = tieto.getKenttia();
        String[] rivi = new String[kenttia-tieto.ekaKentta()];
        for (int i = 0, k = tieto.ekaKentta(); k < kenttia; i++, k++){
            rivi[i] = tieto.anna(k);
        }
        tableElokuvanTiedot.add(tieto, rivi);
    }


    /**
     * Metodi, mikä näyttää sen miten HT5 vaiheessa asukkaan ja elokuvan tiedot näkyvät käyttäjälle.
     * @param os tietovirta johon tulostetaan
     * @param asukas tulostettava asukas
     */
    public void tulosta(PrintStream os, final Asukas asukas){
        os.println("----------ASUKKAAN TIEDOT----------");
        // os.println("-----------------------------------");
        asukas.tulosta(os);
        os.println();
        os.println("----ELOKUVIIN LIITTYVÄY TIEDOT----");
        //os.println("----------------------------------");
        List<Elokuva> tiedot = tylypahka.annaElokuvanTiedot(asukas);
        for (Elokuva elokuva : tiedot){
            elokuva.tulosta(os);
        }
    }


    /**
     * Tulostaa ListChooserissa kaikki jäsenet tekstikenttään.
     * @param text alue johon tulostetaan.
     */
    public void tulostaValitut(TextArea text){
        try (PrintStream stream = TextAreaOutputStream.getTextPrintStream(text)){
            stream.println(" ");
            for (Asukas asukas : chooserAsukkaat.getObjects()){
                tulosta(stream, asukas);
                stream.println("\n\n");
            }

        }
    }


}
