package tylypahka.test;
// Generated by ComTest BEGIN

import org.junit.*;
import tylypahka.*;

import static org.junit.Assert.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2024.04.12 11:03:42 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class AsukasTest {



  // Generated by ComTest BEGIN
  /** testGetNimi60 */
  @Test
  public void testGetNimi60() {    // Asukas: 60
    Asukas asukas = new Asukas(); 
    asukas.taytaTylypahkanAsukas(); 
    { String _l_=asukas.getNimi(),_r_="Uusi Asukas"; if ( !_l_.matches(_r_) ) fail("From: Asukas line: 63" + " does not match: ["+ _l_ + "] != [" + _r_ + "]");}; 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testRekisteroi74 */
  @Test
  public void testRekisteroi74() {    // Asukas: 74
    Asukas harry = new Asukas(); 
    assertEquals("From: Asukas line: 76", 0, harry.getId()); 
    harry.rekisteroi(); 
    Asukas hermione = new Asukas(); 
    hermione.rekisteroi(); 
    int n1 = harry.getId(); 
    int n2 = hermione.getId(); 
    assertEquals("From: Asukas line: 82", n2-1, n1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testToString131 */
  @Test
  public void testToString131() {    // Asukas: 131
    Asukas asukas = new Asukas(); 
    asukas.parse("1|   Uusi Asukas|Rohkelikko"); 
    assertEquals("From: Asukas line: 134", true, asukas.toString().startsWith("1|Uusi Asukas|Rohkelikko|")); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testParse157 */
  @Test
  public void testParse157() {    // Asukas: 157
    Asukas asukas = new Asukas(); 
    asukas.parse("1|   Uusi Asukas|Rohkelikko"); 
    assertEquals("From: Asukas line: 160", 1, asukas.getId()); 
    assertEquals("From: Asukas line: 161", true, asukas.toString().startsWith("1|Uusi Asukas|Rohkelikko|")); 
    asukas.rekisteroi(); 
    int n = asukas.getId(); 
    asukas.parse(""+(n+1)); 
    asukas.rekisteroi(); 
    assertEquals("From: Asukas line: 166", n+1+1, asukas.getId()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAseta254 */
  @Test
  public void testAseta254() {    // Asukas: 254
    Asukas asukas = new Asukas(); 
    assertEquals("From: Asukas line: 256", null, asukas.aseta(1, "Harry Potter")); 
    { String _l_=asukas.aseta(2, "Hepuli"),_r_="Väärä tupa"; if ( !_l_.matches(_r_) ) fail("From: Asukas line: 257" + " does not match: ["+ _l_ + "] != [" + _r_ + "]");}; 
    { String _l_=asukas.aseta(2, "Rooohkelikko"),_r_="Väärä tupa"; if ( !_l_.matches(_r_) ) fail("From: Asukas line: 258" + " does not match: ["+ _l_ + "] != [" + _r_ + "]");}; 
    assertEquals("From: Asukas line: 259", null, asukas.aseta(2, "Rohkelikko")); 
  } // Generated by ComTest END
}