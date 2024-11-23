package tylypahka;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Elokuvat implements Iterable <Elokuva> {

	private boolean muutettu = false;
	private String tiedostonNimi = "";

	/** Taulukko harrastuksista */
	private final List<Elokuva> alkiot = new ArrayList<Elokuva>();

	
	/**
	 * Muodostaja
	 */
	public Elokuvat(){
		// toistaiseksi ei tarvita vielä mihinkään.
	}


	/**
	 * Lisää uudet tiedot tietorakenteeseen.
	 * @param tiedot lisättävät tiedot
	 */
	public void lisaa(Elokuva tiedot){
		alkiot.add(tiedot);
	}


	/**
	 * Lukee elokuviin liittyvästä tiedostosta.
	 * TODO Kesken.
	 * @param hakemisto tiedoston hakemisto
	 * @throws SailoException jos lukeminen epäonnistuu
	 */
	public void lueTiedostosta(String hakemisto)throws SailoException {
		tiedostonNimi = hakemisto + "/elokuvat.dat";
		File tiedosto = new File(tiedostonNimi);
		
		try (Scanner fi = new Scanner(new FileInputStream(tiedosto))){
			while(fi.hasNext()) {
				String s = fi.nextLine();
				if (s == null || "".equals(s) || s.charAt(0) == ';') continue;
				Elokuva tiedot = new Elokuva();
				tiedot.parse(s);
				lisaa(tiedot);
			}
			
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedoston luku epäonnistui");
		}
	}


	/**
	 * Tallentaa tiedot tiedostoon.
	 * @param hakemisto hakemisto
	 * @throws SailoException jos epäonnistuu
	 */
	public void tallenna(String hakemisto) throws SailoException {
		File tiedosto = new File(hakemisto + "/elokuvat.dat");
		try (PrintStream fout = new PrintStream(new FileOutputStream(tiedosto))){
			for(Elokuva tiedot : this) {
				fout.println(tiedot.toString());
			}
			
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto" + tiedosto.getAbsolutePath());
		}
	}
	
	
	/**
	 * Iteraattori kaikkien elokuviin liittyvien tietojen läpikäymiseksi.
	 * @return palauttaa elokuvaiteraattorin.
	 */
	@Override
	public Iterator<Elokuva> iterator() {
		return alkiot.iterator();
	}

	
	/**
	 * Haetaan kaikki asukkaan elokuviin liittyvät tiedot.
	 * @param id asukkaan tunnusnumero jolle tietoja haetaan.
	 * @return palauttaa tietorakenteen, jossa viitteet löydettyihin tietoihin.
	 */
	public List<Elokuva> annaElokuvanTiedot(int id) {
		List<Elokuva> loydetyt = new ArrayList<Elokuva>();
		for (Elokuva elo : alkiot)
			if (elo.getId() == id) loydetyt.add(elo);
		return loydetyt;

	}


	/**
	 * Korvaa tiedot tietorakenteessa.
	 * @param elokuva lisättävän elokuviin liittyvien tietojen viite.
	 */
	public void korvaaTaiLisaa(Elokuva elokuva) {
		int id = elokuva.getId();
		for (int i = 0; i < getLkm(); i++){
			if (alkiot.get(i).getId() == id){
				alkiot.set(i, elokuva);
				muutettu = true;
				return;
			}
		}
		lisaa(elokuva);
	}


	/**
	 * Palauttaa elokuviin liittyvien tietojen lukumäärän.
	 * @return tietojen lkm.
	 */
	public int getLkm() {
		return alkiot.size();
	}


	/**
	 * Poistaa kaikki tietyn asukkaan elokuviin liittyvät tiedot.
	 * @param id viite siihen, mihin asukkaaseen liittyvät tiedot poistetaan.
	 * @return montako poistettiin
	 */
	public int poistaElokuvienTiedot(int id) {
		int n = 0;
		for (Iterator<Elokuva> iteraattori = alkiot.iterator(); iteraattori.hasNext();){
			Elokuva tieto = iteraattori.next();
			if (tieto.getId() == id){
				iteraattori.remove();
				n++;
			}
		}
		if (n > 0) muutettu = true;
		return n;
	}


	/**
	 * Poistaa valitun tiedon
	 * @param tiedot poistettava
	 * @return tosi jos löytyi poistettavia tietoja.
	 */
	public boolean poistaPelkatElokuvienTiedot(Elokuva tiedot) {
		boolean poistettava = alkiot.remove(tiedot);
		if (poistettava) muutettu = true;
		return poistettava;
	}


	/**
	 * Testiohjelma.
	 * @param args ei käytössä
	 * @throws SailoException jos epäonnistuu
	 */
	public static void main(String[] args) throws SailoException {
		Elokuvat tiedot = new Elokuvat();
		
		tiedot.lueTiedostosta("data");
		
		Elokuva asukas1 = new Elokuva();
		asukas1.taytaTiedot(1);
		asukas1.rekisteroi();
		Elokuva asukas2 = new Elokuva();
		asukas2.taytaTiedot(2);
		asukas2.rekisteroi();
		Elokuva asukas3 = new Elokuva();
		asukas3.taytaTiedot(3);
		asukas3.rekisteroi(); // TODO: Muokkaa sellaiseksi, että jos halutaan vain tietylle asukkaalle lisätä enemmän näyttelijöitä.

		tiedot.lisaa(asukas1);
		tiedot.lisaa(asukas2);
		tiedot.lisaa(asukas3);
		
		System.out.println("============= Elokuvat testi =================");

		List<Elokuva> elokuvat = tiedot.annaElokuvanTiedot(5);

		for (Elokuva elo : elokuvat){
			System.out.println(elo.getId() + " ");
			elo.tulosta(System.out);
		}
		
		try {
			tiedot.tallenna("data");
		}catch (SailoException e) {
			e.printStackTrace();
		}
		
	}



}
