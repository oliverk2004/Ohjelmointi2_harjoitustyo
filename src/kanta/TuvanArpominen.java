package kanta;

import java.util.Random;

public class TuvanArpominen {

	public static final String [] TUVAT = {"Rohkelikko", "Puuskupuh", "Luihuinen", "Korpinkynsi"};
	public static final int[] NUMEROT = {1,2,3,4,5,6,7,8}; // Koska elokuvia siis on yhteensä 8.
	
	public static String rand(String [] TUVAT) {
		Random rand = new Random();
		int random = rand.nextInt(TUVAT.length);
		return TUVAT[random];
		
	}
	

	public static int rand(int[] NUMEROT){
		Random rand = new Random();
		int random = rand.nextInt(NUMEROT.length);
		return NUMEROT[random];
	}


	public static String arvoTupa() {
		String aputupa = rand(TUVAT);
		return aputupa; 
		
	}


	public static int arvoNro(){
		int randNro = rand(NUMEROT);
		return randNro;
	}
	
	/**
	 * Tarkistetaan tupa.
	 * @param tupa jota tutkitaan.
	 * @return null jos oikein
	 * @example
	 * <pre name="test">
	 * 	TuvanArpominen tuvanTarkistus = new TuvanArpominen();
	 * 	tuvanTarkistus.tarkistaTupa("Rohkelikko") === null;
	 * 	tuvanTarkistus.tarkistaTupa("Rohkea Rohkelikko") === "Väärä tupa";
	 * 	tuvanTarkistus.tarkistaTupa("Luihuinen") === null;
	 * </pre>
	 */
	public String tarkistaTupa(String tupa) {
		for(String s : TUVAT) {
			if(s.equals(tupa)) {
				return null;
			}
		}
		return "Väärä tupa";
	}
}
