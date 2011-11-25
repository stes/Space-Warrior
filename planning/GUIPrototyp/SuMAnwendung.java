
import sum.komponenten.*;
import sum.werkzeuge.*;
import sum.ereignis.*;

/**
* Die Klasse SuMAnwendung wurde automatisch erstellt: 
* 
* @author 
* @version 13.10.2011
*/
public class SuMAnwendung extends EBAnwendung
{
    // Objekte
    private Textfeld _txtIPAdresse;
    private Textfeld _txtName;
    private Textfeld _txtChatnachricht;
    private Knopf _btnVerbinden;
    private Knopf _btnChat;
    private Etikett _lblIPAdresse;
    private Etikett _lblName;
    private Zeilenbereich _lstChatverlauf;
    
    // Attribute
    
    /**
    * Konstruktor
    */
    public SuMAnwendung()
    {
        //Initialisierung der Oberklasse
        super(1467, 954);
        this.initKomponenten();

    }
    
    private void initKomponenten()
    {
        _txtIPAdresse = new Textfeld(189, 53, 400, 25, "");
        // Ausrichtung
        _txtIPAdresse.setzeAusrichtung(Ausrichtung.LINKS);
        _txtName = new Textfeld(189, 95, 400, 25, "");
        // Ausrichtung
        _txtName.setzeAusrichtung(Ausrichtung.LINKS);
        _txtChatnachricht = new Textfeld(54, 815, 530, 25, "");
        // Ausrichtung
        _txtChatnachricht.setzeAusrichtung(Ausrichtung.LINKS);
        _txtChatnachricht.setzeBearbeiterEingabeBestaetigt("_txtChatnachrichtEingabeBestaetigt");
        _btnVerbinden = new Knopf(611, 53, 100, 25, "Verbinden");
        _btnVerbinden.setzeBearbeiterGeklickt("_btnVerbindenGeklickt");
        _btnChat = new Knopf(602, 815, 100, 25, "Chat");
        _btnChat.setzeBearbeiterGeklickt("_btnChatGeklickt");
        _lblIPAdresse = new Etikett(55, 54, 100, 25, "IP-Adresse");
        // Ausrichtung
        _lblIPAdresse.setzeAusrichtung(Ausrichtung.LINKS);
        _lblName = new Etikett(55, 95, 100, 25, "Name");
        // Ausrichtung
        _lblName.setzeAusrichtung(Ausrichtung.LINKS);
        _lstChatverlauf = new Zeilenbereich(55, 700, 645, 90, "");
    }
    
    /**
    * Vorher: Ereignis EingabeBestaetigtvon_txtChatnachricht fand statt.
    * Nachher: (schreiben Sie, was in dieser Methode ausgefuehrt wird)
    */
    public void _txtChatnachrichtEingabeBestaetigt()
    {
        //    Schreiben Sie hier den Text ihres Dienstes
    }
    
    /**
    * Vorher: Ereignis Geklicktvon_btnVerbinden fand statt.
    * Nachher: (schreiben Sie, was in dieser Methode ausgefuehrt wird)
    */
    public void _btnVerbindenGeklickt()
    {
        //    Schreiben Sie hier den Text ihres Dienstes
    }
    
    /**
    * Vorher: Ereignis Geklicktvon_btnChat fand statt.
    * Nachher: (schreiben Sie, was in dieser Methode ausgefuehrt wird)
    */
    public void _btnChatGeklickt()
    {
        //    Schreiben Sie hier den Text ihres Dienstes
    }

}
