/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.server;

import sum.strukturen.Tabelle;

import sw.shared.Spielkonstanten;
import sw.shared.SpielerDaten;
import sw.shared.SpielerListe;
import sw.shared.SpielerEingabe;
import sw.shared.Schuss;
import sw.shared.Paket;
import sw.shared.Nachrichtentyp;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */ 

public class SpielController
{
    // Bezugsobjekte
    private SpielerListe _verbundeneSpieler;
    private SpielerListe _aktiveSpieler;
    private IServer _server;
    
    // Attribute
    
    // Konstruktor
    /**
     * Erzeugt einen neuen SpielController
     * 
     * @param server IServer
     */
    public SpielController(IServer server)
    {
        _verbundeneSpieler = new SpielerListe(Spielkonstanten.MAX_SPIELERZAHL);
        _aktiveSpieler = new SpielerListe(Spielkonstanten.MAX_SPIELERZAHL);
        _server = server;
    }
        
    // Dienste
    /**
     * startet ein neues Spiel und fügt alle verbundenen Spieler
     * zu den aktiven Spielern hinzu und gibt dem Client eine
     * Nachricht aus
     */
    public void starteSpiel()
    {
        // Liste der aktiven Spieler leeren
        _aktiveSpieler.leere();
        // alle verbundenen Spieler in das Spiel einfügen
        for(int i = 0; i < _verbundeneSpieler.laenge(); i++)
        {
            SpielerDaten daten = _verbundeneSpieler.elementAn(i);
            if (daten != null)
            {
                daten.init();
                _aktiveSpieler.fuegeEin(daten, null);
            }
        }
        Paket info = new Paket(Nachrichtentyp.SV_CHAT_NACHRICHT);
        info.fuegeStringAn("Server");
        info.fuegeStringAn("Neue Runde");
        _server.sendeRundnachricht(info);
        System.out.println("Neue Runde");
    }
    
    /**
     * Ein neuer Spieler ist beigetreten
     * 
     * @param name Name des Spielers
     */
    public void bearbeiteNeuenSpieler(String name)
    {
        SpielerDaten neu = new SpielerDaten(name, true);
        neu.init();
        _verbundeneSpieler.fuegeEin(neu, null);
    }
    /**
     * Ein Spieler hat das Spiel verlassen
     * 
     * @param name Name des Spielers
     */
    public void bearbeiteSpielerVerlaesst(String name)
    {
        SpielerDaten suchObjekt = new SpielerDaten(name, true);
        _verbundeneSpieler.versucheEntfernen(name);
        _aktiveSpieler.versucheEntfernen(name);
    }
    /**
     * Bearbeitet eine Eingabe vom Server
     * 
     * @param name Der Name des betreffenden Spielers
     * @param eingabe Die Eingabe des Spielers
     */
    public void bearbeiteEingabe(String name, SpielerEingabe eingabe)
    {
        _aktiveSpieler.versucheSetzeEingabe(name, eingabe);
    }
    /**
     * Sendet einen Snapshot an jeden Spieler
     */
    public void bearbeiteSnapshot()
    {
        for (int i = 0; i < _verbundeneSpieler.laenge(); i++)
        {
            SpielerDaten daten = _verbundeneSpieler.elementAn(i);
            if (daten != null)
            {
                Paket snapshot = _aktiveSpieler.erstelleSnapshot(daten.name());
                _server.sendeNachricht(daten.name(), snapshot);
            }
        }
    }
    
    /**
     * Der getroffen Spieler kriegt schaden und somit Leben abgezogen
     * Wenn ein Spieler kein Leben mehr hat kriegt der Angreifer einen
     * Punkt
     * 
     * @param angreifer Daten des angreifenden Spielers
     * @param schuss abgegebener Schuss
     */
    private void fuegeSchadenZu(SpielerDaten angreifer, Schuss schuss)
    {
        for (int i = 0; i < _aktiveSpieler.laenge(); i++)
        {
            SpielerDaten daten = _aktiveSpieler.elementAn(i);
            if (daten != null && !daten.name().equals(angreifer.name()))
            {
                if(schuss.abstandZu(daten.position()) < Spielkonstanten.SPIELERGROESSE/2)
                {
                    daten.setzeLeben(daten.leben() - schuss.schaden());
                    if(daten.leben() <= 0)
                    {
                        _aktiveSpieler.versucheEntfernen(daten.name());
                        angreifer.setzePunkte(angreifer.punkte() + 1);
                    }
                }
            }
        }
    }
    
    public void bearbeiteLeerlauf()
    {
        this.pruefeRunde();
        this.aktualisiereDaten();
    }
    
    public void pruefeRunde()
    {
        if((_aktiveSpieler.zaehle() == 1 && _verbundeneSpieler.zaehle() > 1) ||
            (_aktiveSpieler.zaehle() == 0 && _verbundeneSpieler.zaehle() == 1))
        {
            if(_aktiveSpieler.zaehle() == 1)
            {
                for (int i = 0; i < _aktiveSpieler.laenge(); i++)
                {
                    SpielerDaten daten = _aktiveSpieler.elementAn(i);
                    if (daten != null)
                    {
                        Paket info = new Paket(Nachrichtentyp.SV_CHAT_NACHRICHT);
                        info.fuegeStringAn("Server");
                        info.fuegeStringAn(daten.name() + " hat die Runde gewonnen!");
                        _server.sendeRundnachricht(info);
                        break;
                    }
                }
            }
            this.starteSpiel();
        }
    }
    
    private void aktualisiereDaten()
    {
        for(int i = 0; i < _aktiveSpieler.laenge(); i++)
        {
            SpielerDaten daten = _aktiveSpieler.elementAn(i);
            SpielerEingabe eingabe = _aktiveSpieler.eingabeAn(i);
            if (daten != null)
            {
                if (eingabe.schuss() > 0)
                {
                    Schuss s = daten.schiesse(eingabe.schuss() == 2);
                    if (s != null)
                    {
                        this.fuegeSchadenZu(daten, s);
                        Paket p = s.pack();
                        _server.sendeRundnachricht(p);
                    }
                }
                daten.beschleunige(Spielkonstanten.BESCHLEUNIGUNG * eingabe.bewegung());
                daten.dreheUm(Spielkonstanten.DREHWINKEL * Math.signum(eingabe.drehung()));
                daten.ladeNach();
                daten.bewege();
            }
        }
    }
}
