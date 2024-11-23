package tylypahka;


import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author olive
 * @version 27.2.2024
 */
public class Tylypahka {

	private Asukkaat asukkaat = new Asukkaat();
	
	private static Elokuvat elokuvat = new Elokuvat();

	/**
	 * Palauttaa listalta asukkaan annetulla indeksillä.
	 * @param i Halutun asukkaan indeksi
	 * @return Palauttaa asukkaan, joka on listalla annetulla indeksillä
	 * @throws IndexOutOfBoundsException Jos annettu indeksi ei ole listan rajojen sisällä
	 */
	public Asukas annaAsukas(int i) throws IndexOutOfBoundsException{
		return asukkaat.anna(i);
	}
	
	
	/**
	 * Aliohjelma, joka lisää asukkaan, joka on tehty jo luokassa Asukkaat
	 * @param asukas joka lisätään
	 * @throws SailoException jos lisäystä ei voida tehdä
	 */
	public void lisaa(Asukas asukas) throws SailoException {
		asukkaat.lisaa(asukas);
	}


	/**
	 * Metodi, jolla lisätään tiedot
	 * @param tiedot mitä lisätään
	 */
	public void lisaa(Elokuva tiedot){
		elokuvat.lisaa(tiedot);
	}


	/**
	 * Korvaa asukkaan tietorakenteessa.
	 * Etsii samalla tunnusnumerolla olevan asukkaan. Jos ei ole niin lisää uuden asukkaan suoraan.
	 * @param asukas lisätään asukkaan viite.
	 * @throws SailoException jos tietorakenne täynnä.
	 */
	public void korvaaTaiLisaa(Asukas asukas) throws SailoException {
		asukkaat.korvaaTaiLisaa(asukas);
		
	}

	/**
	 * Korvaa elokuviin liittyvät tiedot tietorakenteessa.
	 * Jos samalla tunnusnumerolla ei löydy tietoja asukkaasta, niin lisää uudet tiedot suoraan.
	 * @param elokuva lisättävien tietojen viite.
	 * @throws SailoException jos tietorakenteessa ei ole tilaa, toki tässä tapauksessa ei ole.
	 */
	public void korvaaTaiLisaa(Elokuva elokuva) throws SailoException {
		elokuvat.korvaaTaiLisaa(elokuva);

	}


	/**
	 * Hakee asukkaiden lukumäärän.
	 * @return palauttaa asukkaiden lukumäärän Asukkaat-luokasta
	 */
	public int getAsukkaita() {
		return asukkaat.getLkm();
	}


	/**
	 * Hakee kaikki elokuviin liittyvät tiedot, jotka asukkaalla on.
	 * @param asukas kenen tietoja haetaan.
	 * @return tietorakenne jossa viitteet elokuvien tietoihin.
	 */
	public List<Elokuva> annaElokuvanTiedot(Asukas asukas) {
		return elokuvat.annaElokuvanTiedot(asukas.getId());
	}


	/**
	 * Lukee asukkaiden tiedot tiedostosta.
	 * @param hakemisto josta tiedot luetaan.
	 * @throws SailoException jos lukeminen ei onnistu.
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException{
		File dir = new File(hakemisto);
		dir.mkdir();

		asukkaat = new Asukkaat();
		elokuvat = new Elokuvat();		
		
		asukkaat.lueTiedostosta(hakemisto);
		elokuvat.lueTiedostosta(hakemisto);
	}

	/**
	 * Tallentaa asukkaiden tiedot tiedostoon.
	 * @param hakemisto jonne tallennetaan.
	 * @throws SailoException jos tallentamisessa tulee ongelmia.
	 */
	public void tallenna(String hakemisto) throws SailoException{
		String virhe = "";
		try {
			asukkaat.tallenna(hakemisto);
			
		} catch (SailoException e) {
			virhe = e.getMessage();
		}
		
		try {
			elokuvat.tallenna(hakemisto);
		} catch (SailoException e) {
			virhe = e.getMessage();
		}
		if (!"".equals(virhe)) throw new SailoException(virhe);
	}


	/**
	 * Palauttaa "taulukossa" hakuehtoon vastaavien asukkaiden viitteet.
	 * @param hakuehto hakuehto jota käytetään haussa.
	 * @param k etsittävän kentän indeksi siis asukkaan henkilötiedoissa.
	 * @return tietorakenteen löytyvistä asukkaista.
	 * @throws SailoException jos haku epäonnistuu.
	 */
	public Collection<Asukas> etsi(String hakuehto, int k) throws SailoException{
		return asukkaat.etsi(hakuehto, k);
	}

	/**
	 * Asukkaan tietojen poistamista varten. Poistaa myös asukkaan elokuvien tiedot.
	 * @param asukas joka poistetaan.
	 * @return montako asukasta poistettiin.
	 */
	public int poista(Asukas asukas) {
		if (asukas == null) return 0;
		int poistettava = asukkaat.poista(asukas.getId());
		elokuvat.poistaElokuvienTiedot(asukas.getId());
		return poistettava;
	}

	/**
	 * Elokuviin liittyvien tietojen poistamista varten.
	 * @param tiedot poistettavat tiedot, jotka ovat tässä tapauksessa
	 * kaikki asukkaan tiedot.
	 */
	public void poistaElokuvienTiedot(Elokuva tiedot) {
		elokuvat.poistaPelkatElokuvienTiedot(tiedot);
	}


	/**
	 * Testiohjelma kerhosta.
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Tylypahka tylypahka = new Tylypahka();
		
		
		try {
		Asukas asukas1 = new Asukas();
		Asukas asukas2 = new Asukas();
		
		asukas1.rekisteroi();
		asukas1.taytaTylypahkanAsukas();
		
		asukas2.rekisteroi();
		asukas2.taytaTylypahkanAsukas();
		
		tylypahka.lisaa(asukas1);
		tylypahka.lisaa(asukas2);

		int id1 = asukas1.getId();
		int id2 = asukas2.getId();
		Elokuva elokuva1 = new Elokuva(id1); elokuva1.taytaTiedot(id1); tylypahka.lisaa(elokuva1);
		Elokuva elokuva2 = new Elokuva(id2); elokuva2.taytaTiedot(id2); tylypahka.lisaa(elokuva2);
		
		System.out.println("==========TESTI==============");
		
		for (int i = 0; i < tylypahka.getAsukkaita(); i++) {
			Asukas asukas = tylypahka.annaAsukas(i);
			System.out.println("Asukas paikassa: " + i);
			asukas.tulosta(System.out);
			List<Elokuva> olevat = tylypahka.annaElokuvanTiedot(asukas);
			for (Elokuva tiedot : olevat){
				tiedot.tulosta(System.out);
			}
		}
		
		} catch (SailoException ex) {
			System.out.println(ex.getMessage());
		}
		
		
	}

}
