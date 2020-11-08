/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author J
 */
public class KassapaateTest {
    
    Kassapaate kp;

    @Before
    public void setUp() {
        kp = new Kassapaate();
    }
    
    // luodun kassapäätteen rahamäärä ja myytyjen lounaiden määrä on oikea (rahaa 1000, lounaita myyty 0)
    @Test
    public void luodunKassapaatteenRahamaaraOikea() {
        assertEquals(100000, kp.kassassaRahaa());
    }
    
    @Test
    public void luodunKassapaatteenMyytyjenEdullistenLounaidenMaaraOnOikea() {
        assertEquals(0, kp.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void luodunKassapaatteenMyytyjenMaukkaidenLounaidenMaaraOnOikea() {
        assertEquals(0, kp.maukkaitaLounaitaMyyty());
    }
    
    // käteisosto toimii sekä edullisten että maukkaiden lounaiden osalta
        // jos maksu riittävä: kassassa oleva rahamäärä kasvaa lounaan hinnalla
    @Test
    public void josEdullisenKateismaksuRiittäväKassaKasvaaEdullisenVerran() {
        kp.syoEdullisesti(240);
        assertEquals(100240, kp.kassassaRahaa());
    }
    
    @Test
    public void josMaukkaanKateismaksuRiittäväKassaKasvaaMaukkaanVerran() {
        kp.syoMaukkaasti(400);
        assertEquals(100400, kp.kassassaRahaa());
    }
    
        // jos maksu riittävä: vaihtorahan suuruus on oikea
    @Test
    public void josEdullisenKateismaksuRiittäväOnVaihtorahaOikea() {
        assertEquals(60, kp.syoEdullisesti(300));        
    }
    
    @Test
    public void josMaukkaanKateismaksuRiittäväOnVaihtorahaOikea() {
        assertEquals(100, kp.syoMaukkaasti(500));   
    }
    
        // jos maksu on riittävä: myytyjen lounaiden määrä kasvaa
    @Test
    public void josEdullisenKateismaksuRiittäväKasvaaMyydytEdulliset() {
        kp.syoEdullisesti(240);
        assertEquals(1, kp.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void josMaukkaanKateismaksuRiittäväKasvaaMyydytMaukkaat() {
        kp.syoMaukkaasti(400);
        assertEquals(1, kp.maukkaitaLounaitaMyyty());
    }
    
        // jos maksu ei ole riittävä: kassassa oleva rahamäärä ei muutu
    @Test
    public void edullinenKateisostoToimiiEliJosMaksuEiOleRiittavaNiinKassaEiMuutu() {
        kp.syoEdullisesti(200);
        assertEquals(100000, kp.kassassaRahaa());
    }
    
    @Test
    public void maukasKateisostoToimiiEliJosMaksuEiOleRiittavaNiinKassaEiMuutu() {
        kp.syoMaukkaasti(200);
        assertEquals(100000, kp.kassassaRahaa());
    }
    
        // jos maksu ei ole riittävä: kaikki rahat palautetaan vaihtorahana
    @Test
    public void edullinenKateisostoToimiiEliJosMaksuEiOleRiittavaNiinRahatPalautetaan () {
        assertEquals(200, kp.syoEdullisesti(200));
    }
    
    @Test
    public void maukasKateisostoToimiiEliJosMaksuEiOleRiittavaNiinRahatPalautetaan () {
        assertEquals(200, kp.syoMaukkaasti(200));
    }
    
        // jos maksu ei ole riittävä: myytyjen lounaiden määrässä ei muutosta
    @Test
    public void edullinenKateisostoToimiiEliJosMaksuEiOleRiittavaNiinMyytyjenMaaraEiMuutu () {
        kp.syoEdullisesti(200);
        assertEquals(0, kp.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukasKateisostoToimiiEliJosMaksuEiOleRiittavaNiinMyytyjenMaaraEiMuutu () {
        kp.syoMaukkaasti(200);
        assertEquals(0, kp.maukkaitaLounaitaMyyty());
    }

    // korttiosto toimii sekä edullisten että maukkaiden lounaiden osalta 
        // jos kortilla on tarpeeksi rahaa: veloitetaan summa kortilta ja palautetaan true
    @Test
    public void edullinenKorttiostoToimiiJosKortillaOnTarpeeksiRahaaEdulliseen() {
        Maksukortti kortti = new Maksukortti(10000);
        assertEquals(true, kp.syoEdullisesti(kortti));
    }
    
    @Test
    public void maukasKorttiostoToimiiJosKortillaOnTarpeeksiRahaa() {
        Maksukortti kortti = new Maksukortti(10000);
        assertEquals(true, kp.syoMaukkaasti(kortti));
    }
    
        // jos kortilla on tarpeeksi rahaa: myytyjen lounaiden määrä kasvaa
    @Test
    public void edullinenKorttiostoToimiiEliJosKortillaOnTarpeeksiRahaaNiinLounaidenMaaraKasvaa() {
        Maksukortti kortti = new Maksukortti(10000);
        kp.syoEdullisesti(kortti);
        assertEquals(1, kp.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukasKorttiostoToimiiEliJosKortillaOnTarpeeksiRahaaNiinLounaidenMaaraKasvaa() {
        Maksukortti kortti = new Maksukortti(10000);
        kp.syoMaukkaasti(kortti);
        assertEquals(1, kp.maukkaitaLounaitaMyyty());
    }
    
        // jos kortilla ei ole tarpeeksi rahaa: myytyjen lounaiden määrä muuttumaton
    @Test
    public void myyntisaldoEiKasvaJosEiOleVaraaEdulliseen(){
        Maksukortti kortti = new Maksukortti(10);
        kp.syoEdullisesti(kortti);
        assertEquals(0, kp.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myyntisaldoEiKasvaJosEiOleVaraaMaukkaaseen() {
        Maksukortti kortti = new Maksukortti(10);
        kp.syoMaukkaasti(kortti);
        assertEquals(0, kp.maukkaitaLounaitaMyyty());
    }
        
        // jos kortilla ei ole tarpeeksi rahaa: kortin rahamäärä ei muutu
    @Test
    public void kortinSaldoEiMuutuJosEiOleVaraaEdulliseen() {
        Maksukortti kortti = new Maksukortti(10);
        assertEquals(false, kp.syoEdullisesti(kortti));
    }
    
    @Test
    public void kortinSaldoEiMuutuJosEiOleVaraaMaukkaaseen() {
        Maksukortti kortti = new Maksukortti(10);
        assertEquals(false, kp.syoMaukkaasti(kortti));
    }
    
        // kassassa oleva rahamäärä ei muutu kortilla ostettaessa
    @Test
    public void kassaEiMuutuKortillaOstettaessaEdullista() {
        Maksukortti kortti = new Maksukortti(10000);
        kp.syoEdullisesti(kortti);
        assertEquals(100000, kp.kassassaRahaa());
    }
    
    @Test
    public void kassaEiMuutuKortillaOstettaessaMaukasta() {
        Maksukortti kortti = new Maksukortti(10000);
        kp.syoMaukkaasti(kortti);
        assertEquals(100000, kp.kassassaRahaa());
    }    
    
    // kortille rahaa ladattaessa kassassa oleva rahamäärä kasvaa ladatulla summalla
    @Test
    public void korttiaLadattaessaSaldoMuuttuuJaKassaKasvaaLadatullaSummalla() {
        Maksukortti kortti = new Maksukortti(0);
        kp.lataaRahaaKortille(kortti, 1000);
        assertEquals(101000, kp.kassassaRahaa());
    }
    
    @Test
    public void korttiaLadattaessaNegatiivisellaMaarallaEiKasvaKassa() {
        Maksukortti kortti = new Maksukortti(0);
        kp.lataaRahaaKortille(kortti, -2);
        assertEquals(100000, kp.kassassaRahaa());
    }
}
