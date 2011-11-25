package sw.client;

import sw.shared.Spielkonstanten;
import sw.shared.Nachrichtentyp;
import sw.shared.SpielerDaten;
import sw.shared.SpielerListe;
import sw.shared.SpielerEingabe;
import sw.shared.Schuss;
import sw.shared.Paket;
import sw.shared.Punkt;

import sum.multimedia.Bild;

import java.awt.event.AWTEventListener;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */ 
public class SpielController implements AWTEventListener, ClientListener
{
    // Bezugsobjekte
    private SpielerListe _spielerListe;
    private SpielerDaten _lokalerSpieler;
    private IClient _client;
    private SpielerEingabe _status;
    private SpielerEingabe _alterStatus;
    
    // Attribute
    private boolean _istInitialisiert;
    
    // Konstruktor
    /**
     * Erzeugt einen neuen SpielController
     */
    public SpielController(IClient client)
    {
        _status = new SpielerEingabe();
        _alterStatus = new SpielerEingabe();
        _spielerListe = new SpielerListe(Spielkonstanten.MAX_SPIELERZAHL);
        _client = client;
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
        _istInitialisiert = true;
    }
        
    // Dienste 
    /**
     * @return die aktuelle Spielerliste
     */
    public SpielerListe spielerListe()
    {
        return _spielerListe;
    }

    /**
     * Sendet einen Snapshot an jeden Spieler
     */
    public void bearbeiteSnapshot(Paket snapshot)
    {
        _spielerListe.update(snapshot);
        for (int i = 0; i < _spielerListe.laenge(); i++)
        {
            SpielerDaten d = _spielerListe.elementAn(i);
            if (d != null && d.lokal())
            {
                _lokalerSpieler = d;
            }
        }
    }
    
    @Override
    public void bearbeiteSchuss(Paket paket)
    {
        Schuss s = Schuss.hole(paket);
        SchussPool.fuegeSchussHinzu(s);
    }
    @Override
    public void bearbeiteChatNachricht(String name, String text) {}
    @Override
    public void bearbeiteTrennung(String grund) {}
    @Override
    public void eventDispatched(AWTEvent e)
    {
        if (e.getID() == KeyEvent.KEY_TYPED)
        {
            KeyEvent keyEvent = (KeyEvent)e;
            char eingabe = ((KeyEvent)e).getKeyChar();
            switch (eingabe)
            {
                case 'w':
                {
                    // vorwärts
                    _status.setzeBewegung(1);
                    break;
                }
                case 's':
                {
                    // rückwärts
                    _status.setzeBewegung(-1);
                    break;
                }
                case 'a':
                {
                    // links
                    _status.setzeDrehung(1);
                    break;
                }
                case 'd':
                {
                    // rechts
                    _status.setzeDrehung(-1);
                    break;
                }
                case 'n':
                {
                    // normaler Schuss
                    _status.setzeSchuss(1);
                    break;
                }
                case 'm':
                {
                    // Masterschuss
                    _status.setzeSchuss(2);
                    break;
                }
            }
        }
        else if (e.getID() == KeyEvent.KEY_RELEASED)
        {
            KeyEvent keyEvent = (KeyEvent)e;
            char eingabe = ((KeyEvent)e).getKeyChar();
            switch (eingabe)
            {
                case 'w':
                {
                    // vorwärts
                    _status.setzeBewegung(-1);
                    break;
                }
                case 's':
                {
                    // rückwärts
                    _status.setzeBewegung(0);
                    break;
                }
                case 'a':
                {
                    // links
                    _status.setzeDrehung(0);
                    break;
                }
                case 'd':
                {
                    // rechts
                    _status.setzeDrehung(0);
                    break;
                }
                case 'm': case 'n':
                {
                    _status.setzeSchuss(0);
                    break;
                }
            }
        }
        if (!_alterStatus.equals(_status))
        {
            _alterStatus = new SpielerEingabe(_status);
            Paket p = _status.pack();
            _client.sendeNachricht(p);
        }
    }
}
