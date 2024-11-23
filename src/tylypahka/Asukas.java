package tylypahka;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.TuvanArpominen;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import static kanta.TuvanArpominen.*;

/**
 * - kopioi CRC-kortin vastuut
 * @author oliver
 */
public class Asukas implements Cloneable {

	private int 	id 					= 0;
	private String 	nimi 				= "";
	private String 	tupa 				= "";
	private String 	sukupuoli 			= "";
	private String 	hvari 				= "";
	private String 	syntypera 			= "";
	private String 	morko 				= "";
	private String 	taikasauva 			= "";
	
	private static int seuraavanId 	= 1;
	
	private static final TuvanArpominen tuvanTarkistus = new TuvanArpominen();
	
	public Asukas() {
		// parametriton muodostaja, jota ei edes tarvitsisi
	}
	
	
	/**
	 * Tulostetaan asukkaan tiedot
	 * @param os mihin tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	
	/**
	 * Tulostetaan henkilöntiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", id)+ " " + nimi + ", " + tupa);
		out.println(sukupuoli + ", " + hvari + ",");
		out.println(syntypera + ", " + morko + ", ");
		out.println(taikasauva);
	}


	/**
	 * @return palautetaan nimi
	 * @example
	 * <pre name="test">
	 * 	Asukas asukas = new Asukas();
	 * 	asukas.taytaTylypahkanAsukas();
	 * 	asukas.getNimi() =R= "Uusi Asukas";
	 * </pre>
	 */
	public String getNimi() {
		return nimi;
	}
	
	
	/**
	 * Antaa asukkaalle id-numeron.
	 * @example
	 * <pre name="test">
	 * 	Asukas harry = new Asukas();
	 * 	harry.getId() === 0;
	 * 	harry.rekisteroi();
	 * 	Asukas hermione = new Asukas();
	 * 	hermione.rekisteroi();
	 * 	int n1 = harry.getId();
	 * 	int n2 = hermione.getId();
	 * 	n1 === n2-1;
	 * </pre>
	 */
	public void rekisteroi() {
		this.id = seuraavanId;
		seuraavanId++;
	}
	
	
	/**
	 * Palauttaa asukkaan id:n. 
	 * @return asukkaan id
	 */
	public int getId() {
		return id;
	}


	/**
	 * Apumetodi, jolla saadaan täytettyä testiarvot asukkaalle.
	 * @param aputupa tupa joka tulee asukkaalle
	 */
	public void taytaTylypahkanAsukas(String aputupa) {
		nimi = "Uusi Asukas";
		tupa = aputupa;
		sukupuoli = "";
		hvari = "";
		syntypera = "";
		morko = "";
		taikasauva = "";
	}

	
	/**
	 * Apumetodi, jolla saadaan testiarvot asukkaalle.
	 * Tupa arvotaan satunnaisesti, niin kuin se oikeastikin Tylypahkassa...
	 * Kuitenkin tupaa käyttäjä pystyy myös itse muokkaamaan. 
	 */
	public void taytaTylypahkanAsukas() {
		String aputupa = arvoTupa();
		taytaTylypahkanAsukas(aputupa);
	}
	
	
	
	/**
	 * Palauttaa asukkaan tiedot merkkijonona, jonka voi tallentaa tiedostoon.
	 * @return asukas tolpalla erotettuna merkkijonona.
	 * @example
	 * <pre name="test">
	 * 	Asukas asukas = new Asukas();
	 * 	asukas.parse("1|   Uusi Asukas|Rohkelikko");
	 * 	asukas.toString().startsWith("1|Uusi Asukas|Rohkelikko|") === true;
	 * </pre>
	 */
	@Override
	public String toString() {
		return "" +
				getId() + "|" + 
				nimi + "|" + 
				tupa + "|" + 
				sukupuoli + "|" + 
				hvari + "|" + 
				syntypera + "|" + 
				morko + "|" + 
				taikasauva;
				
	}
	
	
	/**
	 * Selvittää tiedot erotellusta merkkijonosta ja että seuraava asukas saa suuremman 
	 * id:n kun edellinen.
	 * @param rivi josta asukkaan tiedot otetaan.
	 * @example
	 * <pre name="test">
	 * 	Asukas asukas = new Asukas();
	 * 	asukas.parse("1|   Uusi Asukas|Rohkelikko");
	 * 	asukas.getId() === 1;
	 * 	asukas.toString().startsWith("1|Uusi Asukas|Rohkelikko|") === true;
	 *  asukas.rekisteroi();
	 *  int n = asukas.getId();
	 *  asukas.parse(""+(n+1));
	 *  asukas.rekisteroi();
	 *  asukas.getId() === n+1+1;
	 * </pre>
	 */
	public void parse(String rivi) {
		var sb = new StringBuilder(rivi);
		setId(Mjonot.erota(sb, '|', getId()));
		nimi = Mjonot.erota(sb, '|', nimi);
		tupa = Mjonot.erota(sb, '|', tupa);
		sukupuoli = Mjonot.erota(sb, '|', sukupuoli);
		hvari = Mjonot.erota(sb, '|', hvari);
		syntypera = Mjonot.erota(sb, '|', syntypera);
		morko = Mjonot.erota(sb, '|', morko);
		taikasauva = Mjonot.erota(sb, '|', taikasauva);
	}
	
	
	/**
	 * Asettaa ID:n ja varmistaa että seuraava saa suuremman kuin edellinen.
	 * @param nro asetettava id
	 */
	private void setId(int nro) {
		id = nro;
		if(id >= seuraavanId) seuraavanId = id + 1;
		
	}


	/**
	 * Asukkaat pääohjelma
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Asukas asukas1 = new Asukas();
		Asukas asukas2 = new Asukas();

		asukas1.rekisteroi();
		asukas2.rekisteroi();
		asukas1.taytaTylypahkanAsukas();
		asukas2.taytaTylypahkanAsukas();
		
		asukas1.tulosta(System.out);
		asukas2.tulosta(System.out);
		

		
		
	}

	/**
	 * @return Asukkaan tupa
	 */
	public String getTupa() {
		return tupa;
	}

	
	/**
	 * @return Asukkaan sukupuoli
	 */
	public String getSukupuoli() {
		return sukupuoli;
	}


	/**
	 * TODO: TESTIT
	 */
	public Asukas clone() throws CloneNotSupportedException {
		Asukas uusi;
		uusi = (Asukas) super.clone();
		return uusi;
	}


	public int getKenttia() {
		return 8;
	}

	public int ekaKentta() {
		return 1;
	}


	/**
	 * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon.
	 * @param k kuinka monennen kentän arvo asetetaan.
	 * @param jono jonoa joka asetetaan kentän arvoksi.
	 * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
	 * @example <pre name="test">
	 * 	Asukas asukas = new Asukas();
	 * 	asukas.aseta(1, "Harry Potter") === null;
	 * 	asukas.aseta(2, "Hepuli") =R= "Väärä tupa";
	 * 	asukas.aseta(2, "Rooohkelikko") =R= "Väärä tupa";
	 * 	asukas.aseta(2, "Rohkelikko") === null;
	 * </pre>
	 */
	public String aseta(int k, String jono) {
		String tjono = jono.trim();
		StringBuffer sb = new StringBuffer(tjono);
		switch (k){
			case 0 : setId(Mjonot.erota(sb, '§', getId()));
			return null;
			case 1 : nimi = tjono;
			return null;
			case 2 : TuvanArpominen tuvat = new TuvanArpominen();
			String virhe = tuvat.tarkistaTupa(tjono);
			if(virhe != null) return virhe;
			tupa = tjono;
			return null;
			case 3 : sukupuoli = tjono;
			return null;
			case 4 : hvari = tjono;
			return null;
			case 5 : syntypera = tjono;
			return null;
			case 6 : morko = tjono;
			return null;
			case 7 : taikasauva = tjono;
			return null;
			default:
				return "Tylypahka";
		}
	}


	/**
	 * Antaa k:n kentän sisällön merkkijonona
	 * @param k monenenko kentän sisältö palautetaan
	 * @return kentän sisältö merkkijonona
	 */
	public String anna(int k) {
		switch (k){
			case 0  : return "" + id;
			case 1  : return "" + nimi;
			case 2  : return "" + tupa;
			case 3  : return "" + sukupuoli;
			case 4  : return "" + hvari;
			case 5  : return "" + syntypera;
			case 6  : return "" + morko;
			case 7  : return "" + taikasauva;
			default: return "tylpahka";
		}
	}


	/**
	 * Palauttaa k:tta asukkaan kenttää vastaavan kysymyksen
	 * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
	 * @return k:netta kenttää vastaava kysymys
	 */

	public String getKysymys(int k){
		switch (k){
			case 0 : return "id";
			case 1 : return "nimi";
			case 2 : return "tupa";
			case 3 : return "sukupuoli";
			case 4 : return "hiusten väri";
			case 5 : return "syntyperä";
			case 6 : return "mörkö";
			case 7 : return "taikasauva";
			default: return "tylpahka";
		}
	}


	/**
	 * Asukkaiden lajittelija
	 */
	public static class Lajittelija implements Comparator<Asukas>{
		private final int k;

		public Lajittelija(int k){
			this.k = k;
		}

		@Override
		public int compare(Asukas asukas1, Asukas asukas2){
			return asukas1.anna(k).compareTo(asukas2.anna(k));
		}
	}
}
