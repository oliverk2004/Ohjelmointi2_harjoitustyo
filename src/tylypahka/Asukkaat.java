package tylypahka;

import fi.jyu.mit.ohj2.WildChars;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * 
 */
public class Asukkaat {

	private static final int MAX_ASUKKAITA = 50; // Poista tulevaisuudessa.
	int lkm = 0;
	private final Asukas[] alkiot;
	private String tiedostonNimi;
    private boolean muutettu = false;  


	/**
	 * Luodaan alustava taulukko.
	 */
	public Asukkaat() {
		this.alkiot = new Asukas[MAX_ASUKKAITA];
	}
	
	
	/**
	 * Palauttaa viitteen i:teen asukkaaseen. 
	 * @param i monenteeko asukkaaseen halutaan viitatata
	 * @return viite asukkaaseen jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella 
	 */
    public Asukas anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Ei ole olemassa kyseistä indeksiä!: " + i);
        return alkiot[i];
    }


	/**
	 * Lisää yhden uuden asukkaan tietorakenteeseen.
	 * @param asukas lisättävään asukkaaseen viite. 
	 * @throws SailoException jos tietorakenne on jo täynnä. 
	 */
	public void lisaa(Asukas asukas) throws SailoException {
		if (lkm >= alkiot.length) throw new SailoException("Liikaa asukkaita!");
		this.alkiot[this.lkm] = asukas;
		this.lkm++;
		muutettu = true;
	}
	
	/**
	 * Korvaa asukkaan tietorakenteessa.
	 * @param asukas lisätään asukkaan viite.
	 * @throws SailoException jos tietorakenne täynnä.
	 */
	public void korvaaTaiLisaa(Asukas asukas) throws SailoException {
		int id = asukas.getId();
		for(int i = 0; i < lkm; i++) {
			if(alkiot[i].getId() == id) {
				alkiot[i] = asukas;
				muutettu = true;
				return;
			}
		}
		lisaa(asukas);
	}
	
	
	/**
	 * Lukee asukkaat tiedostosta.
	 * @param hakemisto tiedoston hakemisto.
	 * @throws SailoException jos epäonnistuu.
	 */
	public void lueTiedostosta(String hakemisto)throws SailoException {
		tiedostonNimi = hakemisto + "/tiedot.dat";
		File tiedosto = new File(tiedostonNimi);
		
		try (Scanner fi = new Scanner(new FileInputStream(tiedosto))){
			while(fi.hasNext()) {
				String s = fi.nextLine();
				if (s == null || "".equals(s) || s.charAt(0) == ';') continue;
				Asukas asukas = new Asukas();
				asukas.parse(s);
				lisaa(asukas);
			}
			muutettu = false;
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedoston luku epäonnistui");
		}
	}
	

	/**
	 * Tallentaa asukkaat tiedostoon.
	 * @param hakemisto mihin hakemistoon tallennetaan.
	 * @throws SailoException jos tallennus ei onnistu.
	 */
	public void tallenna(String hakemisto) throws SailoException {
		if (!muutettu) return;
		
		File tiedosto = new File(hakemisto + "/tiedot.dat");
		try (PrintStream fout = new PrintStream(new FileOutputStream(tiedosto))){
			for(int i = 0; i < this.getLkm(); i++) {
				Asukas asukas = this.anna(i);
				fout.println(asukas.toString());
			}
			
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto" + tiedosto.getAbsolutePath());
		}
		
		muutettu = false;
	}
	
	
	/**
	 * Metodi, että saadaan asukkaiden lukumäärä.
	 * @return lkm
	 */
	public int getLkm() {
		return lkm;
	}


	/**
	 * Palauttaa "taulukossa" hakuehtoon vastaavien asukkaiden viitteet.
	 * @param hakuehto hakuehto
	 * @param k etsittävän kentän indeksi
	 * @return tietorakenteen löytyneistä asukkaista.
	 */
	public Collection<Asukas> etsi(String hakuehto, int k){
		String ehto = "*";
		if (hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto;
		int hk = k;
		if (hk < 0 ) hk = 1;
		List<Asukas> loytyneet = new ArrayList<Asukas>();
		for (int i = 0; i < lkm; i++) {
			Asukas asukas = alkiot[i];
			if(asukas.getNimi().contains(hakuehto)) loytyneet.add(asukas);
			if (WildChars.onkoSamat(asukas.anna(hk), ehto)) loytyneet.add(asukas);
		}
		loytyneet.sort(new Asukas.Lajittelija(hk));

		return loytyneet;
	}


	/**
	 * Etsii asukkaan id:n perusteella
	 * @param id tunnusnumero, jonka mukaan etsitään
	 * @return löytyneen asukkaan indeksi tai -1 jos ei löydy
	*/
	public int etsiId(int id){
		for (int i = 0; i < lkm; i++){
			if (id == alkiot[i].getId()) return i;
		}
		return -1;
	}


	/**
	 * Poistaa asukkaan jolla on valittu id.
	 * @param id poistettavan asukkaan id.
	 * @return 1 jos poistettiin, 0 jos ei löydy
	 */
	public int poista(int id) {
		int poistettavaAsukas = etsiId(id);
		lkm--;
		for(int i = poistettavaAsukas; i < lkm; i++){
			alkiot[i] = alkiot[i+1];
		}
		alkiot[lkm] = null;
		muutettu = true;
		return 1;
	}


	/**
	 * @param args ei käytössä
	 * @throws SailoException  jos vikaa
	 */
	public static void main(String[] args) throws SailoException {
		Asukkaat asukkaat = new Asukkaat();
		
		try {
			asukkaat.lueTiedostosta("data");
		} catch (SailoException ex) {
			System.err.println(ex.getMessage());
		}
		Asukas asukas1 = new Asukas(), asukas2 = new Asukas();
        asukas1.rekisteroi();
        asukas1.taytaTylypahkanAsukas();
        asukas2.rekisteroi();
        asukas2.taytaTylypahkanAsukas();

        try {
            asukkaat.lisaa(asukas1);
            asukkaat.lisaa(asukas2);
		} catch (SailoException e) {
			// TODO: handle exception
			System.err.println(e.getMessage());
		}
        
        System.out.println("==================TESTI========================");
        
        // Testausta
        for(int i = 0; i < asukkaat.getLkm(); i++) {
    		Asukas asukas = asukkaat.anna(i);
    		asukas.tulosta(System.out);
    		System.out.println();
        }
        
        try {

			asukkaat.tallenna("data");
		} catch (SailoException e) {
			System.err.println(e.getMessage());
		}



	}



}
