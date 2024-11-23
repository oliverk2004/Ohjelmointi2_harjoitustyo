package tylypahka;

import fi.jyu.mit.ohj2.Mjonot;

import java.io.OutputStream;
import java.io.PrintStream;

import static kanta.TuvanArpominen.arvoNro;

// TODO: Siten, että kysymys näkyy ("Onko Harryn kaveri?")

public class Elokuva implements Cloneable {
	
	private int id;
	private String nayttelija = "";
	private String kysymys = ""; // Eli siis se, että "Onko Harryn kaveri?"
	private int esiintymiset;
	private static int seuraavanId = 1;


	/**
	 * Muodostaja
	 */
	public Elokuva() {
		// Vielä ei tarvita mitään.
	}
	
	
	/**
	 * Alustetaan tietyn asukkaan elokuviin liittyvät tiedot.
	 * @param asukasNro viitenumero
	 */
	public Elokuva(int asukasNro) {
		this.id = asukasNro;
	}


	/**
	 * Täyttää elokuvaan liittyvät tiedot asukkaalle.
	 * @param tag kenen asukkaan tiedot täytetään.
	 */
	public void taytaTiedot(int tag){
		id = tag;
		nayttelija = "Etunimi Sukunimi";
		kysymys = "Kyllä/Ei";
		esiintymiset = arvoNro();
	}


	/**
	 * Antaa harrastukselle seuraavan rekisterinumeron.
	 */
	public int rekisteroi() {
		id = seuraavanId;
		seuraavanId++;
		return id;
	}


	/**
	 * Tulostetaan elokuvaan liittyvät tiedot.
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out){
		out.println(id + " " + nayttelija + " " + kysymys + " " + esiintymiset);
	}


	/**
	 * Tulostetaan asukkaan tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}


	/**
	 * Palautetaan mille asukkaalle kyseiset elokuvan tiedot kuuluvat.
	 * @return id
	 */
	public int getId(){
		return id;
	}


	/**
	 * Asettaa tunnusnumeron ja samalla varmistaa että
	 * seuraava numero on aina suurempi kuin tähän mennessä suurin.
	 * @param nro asetettava tunnusnumero
	 */
	private void setId(int nro) {
		id = nro;
		if(id >= seuraavanId) seuraavanId = id + 1;
		
	}

	/**
	 * Palauttaa elokuvien tiedot merkkijonona, joka tallenetaan tiedostoon.
	 * @return muoto jossa ne palautetaan tiedostoon.
	 */
	@Override
	public String toString() {
		return "" + 
				getId() + '|' + 
				nayttelija + '|' + 
				kysymys + '|' + 
				esiintymiset;
	}
	
	/**
	 * Selvittää tiedot | erotellusta merkkijonosta.
	 * @param rivi josta tiedot otetaan.
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		setId(Mjonot.erota(sb, '|', getId()));
		nayttelija = Mjonot.erota(sb, '|', nayttelija);
		kysymys = Mjonot.erota(sb, '|', kysymys);
		esiintymiset = Mjonot.erota(sb, '|', esiintymiset);
		
	}
	
	
	/**
	 * Asukkaalle elokuvaan liittyvät tiedot.
	 * @param args ei käytössä
	 */
	public static void main(String [] args) {
		Elokuva tiedot = new Elokuva();
		tiedot.taytaTiedot(1);
		tiedot.tulosta(System.out);
		
	}


	public int ekaKentta() {
		return 1;
	}

	public int getKenttia(){
		return 4;
	}


	/**
	 * @param k minkä kentän kysymys halutaan
	 * @return valitun kentän kysymysteksti
	 */
	public String getKysymys(int k) {
		switch (k){
			case 0 : return "id";
			case 1 : return "näyttelijä";
			case 2 : return "Harryn kaveri?";
			case 3 : return "esiintymiset";
			default: return "?";
		}
	}


	/**
	 * @param k Minkä kentän sisältö halutaan.
	 * @return valitun kentän sisältö
	 */
	public String anna(int k) {
		switch (k){
			case 0 : return "" + id;
			case 1 : return "" + nayttelija;
			case 2 : return "" + kysymys;
			case 3 : return "" + esiintymiset;
			default: return "?";
		}
	}


	/**
	 * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
	 * palautetaan null, muutoin virheteksti.
	 * @param k minkä kentän sisältö asetetaan
	 * @param s asetettava sisältö merkkijonona
	 * @return null jos ok, muuten virheteksti
	 */
	public String aseta(int k, String s) {
		String st = s.trim();
		StringBuffer sb = new StringBuffer(st);
		switch (k){
			case 0 : setId(Mjonot.erota(sb, '§', getId()));
			return null;
			case 1 : nayttelija = st;
			return null;
			case 2 : kysymys = st;
			return null;
			case 3 : try{
				esiintymiset = Mjonot.erotaEx(sb, '§', esiintymiset);
			} catch (NumberFormatException ex){
				return "Ei kokonaisluku ("+st+")";
			}
			return null;
			default: return "Ei ole olemassa kyseistä kenttää.";
		}
	}


	/**
	 * Tehdään klooni asukkaan elokuviin liittyvistä tiedoista.
	 * @return kloonatut tiedot
	 */
	@Override
	public Elokuva clone() throws CloneNotSupportedException {
		Elokuva uusi;
		uusi = (Elokuva) super.clone();
		return uusi;
	}
}
