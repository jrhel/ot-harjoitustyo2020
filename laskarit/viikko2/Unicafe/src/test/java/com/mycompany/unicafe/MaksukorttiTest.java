package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoAlussaOikein() {
        assertTrue(kortti.saldo() == 10);
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(5);
        assertTrue(kortti.saldo() == 15);
    }
    
    @Test
    public void rahanOttaminenOnnistuuJosRahaaOnTarpeeksi() {
        assertEquals(true, kortti.otaRahaa(8));
    }
    
    @Test
    public void booleanRahanOttaminenEiOnnistuuJosRahaaEiOleTarpeeksi() {
        assertEquals(false, kortti.otaRahaa(12));
    }
    
    @Test
    public void toStringToimii() {
        kortti.lataaRahaa(990);
        assertEquals("saldo: 10.0" ,kortti.toString());
    }
}
