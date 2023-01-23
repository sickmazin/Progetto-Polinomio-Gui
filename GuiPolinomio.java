import java.io.BufferedReader;import java.io.BufferedWriter;import java.io.File;import java.io.FileNotFoundException;import java.io.FileReader;import java.io.FileWriter;import java.io.IOException;import java.util.ArrayList;import java.util.Iterator;import java.awt.Font;import java.awt.event.*;import javax.swing.JButton;import javax.swing.JCheckBox;import javax.swing.JFileChooser;import javax.swing.JFrame;import javax.swing.JLabel;import javax.swing.JMenu;import javax.swing.JMenuBar;import javax.swing.JMenuItem;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JTextField;import javax.swing.UIManager;import javax.swing.UnsupportedLookAndFeelException;import javax.swing.filechooser.FileFilter;import javax.swing.plaf.ColorUIResource;import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.BorderLayout;import java.awt.Color;import java.awt.GridLayout;

class Gui extends JFrame implements ActionListener{
    static String POLINOMIO = "\\-?\\d*((x\\^){1}\\d+|x)?(([\\-\\+]{1})\\d*((x\\^){1}\\d+|x)?)*";
    private ArrayList<PolinomioSet> polinomi= new ArrayList<>();
    private JMenuBar barramenu;
    private JMenu file,operazioni,help;
    private JMenuItem salva,apri, valore, somma, moltipl, derivata, rimuovi,info,differenza;
    private JTextField inserimento;
	private JPanel contenitorePolinomi;
	private JButton inserisci;
	private File fileTesto;
    Font font= new Font("Cambria Math", Font.PLAIN, 12);
    public Gui() {
        setTitle("Calcolatore Polinomi");
        setSize(750, 550);
		setLocation(350,200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e) {if(uscita()) System.exit(0);}
        private boolean uscita() {
            int scelta=JOptionPane.showConfirmDialog( null, "Vuoi uscire ?", "I dati non salvati andranno persi!", JOptionPane.YES_NO_OPTION);
		   return scelta==JOptionPane.YES_OPTION;
        }});

        //INIZIALIZZAZIONE BARRA DEI MENU E RIEMPIMENTO DI ESSA
        barramenu= new JMenuBar();
        //MENU FILE
        file= new JMenu("File");               file.setFont(font);
            salva= new JMenuItem("Salva su file");      salva.addActionListener(this);  salva.setFont(font);    file.add(salva);
            apri= new JMenuItem("Apri da file");       apri.addActionListener(this);    apri.setFont(font);     file.add(apri);
        //BARRA OPERAZIONI
        operazioni= new JMenu("Operazioni");         operazioni.setFont(font);
            somma= new JMenuItem("Addizziona polinomi selezionati");         somma.addActionListener(this);             somma.setFont(font);             operazioni.add(somma);
            differenza= new JMenuItem("Differenza polinomi selezionati");    differenza.addActionListener(this);        differenza.setFont(font);        operazioni.add(differenza);
            moltipl= new JMenuItem("Moltiplica polinomi selezionati");       moltipl.addActionListener(this);           moltipl.setFont(font);           operazioni.add(moltipl);
            derivata= new JMenuItem("Derivata del polinomio selezionato");   derivata.addActionListener(this);          derivata.setFont(font);          operazioni.add(derivata);
            valore= new JMenuItem("Funzione da polinomio");                  valore.addActionListener(this);            valore.setFont(font);            operazioni.add(valore);            
            rimuovi= new JMenuItem("Rimuovi polinomi selezionati");          rimuovi.addActionListener(this);           rimuovi.setFont(font);           operazioni.add(rimuovi);
        //BARRA INFORMAZIONI
        help= new JMenu("Help");               help.setFont(font);
            info= new JMenuItem("Informazioni Studente");       info.addActionListener(this);   info.setFont(font);              help.add(info);  
        //AGGIUNGO I MENU ALLA BARRA
        barramenu.add(file); barramenu.add(operazioni); barramenu.add(help);
        setJMenuBar(barramenu);

        //SCRITTURE SU FINESTRA PRINCIPALE
        JLabel intro= new JLabel("Inserisci dei polinomi nel campo apposito ed esegui delle operazioni selezionandoli",JLabel.CENTER);    intro.setFont(new Font("Cambria Math", Font.BOLD, 16));    intro.setForeground(new ColorUIResource(255, 82, 3));
        JLabel richiestaInserimento = new JLabel("Inserisci un polinomio ->");       richiestaInserimento.setFont(new Font("Cambria Math", Font.PLAIN, 16));            richiestaInserimento.setForeground(new ColorUIResource(255, 82, 3));

        //CASELLA DOVE VERRANNO INSERITI DALL'UTENTE I POLINOMI
        inserimento= new JTextField(20);        inserimento.addActionListener(this);

		inserisci = new JButton("Inserisci"); inserisci.setFont(new Font("Cambria Math", Font.PLAIN, 18));
		inserisci.addActionListener(this);

        this.setJMenuBar(barramenu);

        //PANNELLI DOVE APPARIRANNO TESTI E CASELLA TESTO
        JPanel barraSopra= new JPanel();        barraSopra.add(intro,BorderLayout.CENTER);      
        JPanel barraInserimento= new JPanel();  barraInserimento.add(richiestaInserimento);     barraInserimento.add(inserimento);      barraInserimento.add(inserisci);
        
        //INSERIAMO LE 2 BARRE NELLA FINESTRA PRINCIPALE
        add(barraInserimento,BorderLayout.SOUTH); add(barraSopra,BorderLayout.NORTH);

        //CREIAMO IL PANNELLO CHE CONTERRA I POLINOMI
        contenitorePolinomi = new JPanel(new GridLayout(10,10,5,5)); add(contenitorePolinomi,BorderLayout.CENTER); contenitorePolinomi.setBackground(new ColorUIResource(30,30,30));
        contenitorePolinomi.setFont(font);

        refreshGui(); 
    }
    /**
     * Il metodo permettere di aggiornare la gui e i vari menu.
     * Permette anche di evidenziare i polinomi selezionati.
     */
    private void refreshGui() {
        int c=0;
        for (int i = 0; i < contenitorePolinomi.getComponentCount(); ++i) {
            if(((JCheckBox)contenitorePolinomi.getComponent(i)).isSelected()) c++;            
        }
        switch (c) {
            case 0:
                start();
                break;
            case 1:
                menuUno();
                break;
            case 2:
                menuDuePiu();  
                break;      
            default:
                menuDuePiu();
                break;
        }
        revalidate(); repaint();
    }
    
    /**
     * Metodo che permette di rendere attivi i metodi utilizzabili. 
     * Essi dipenderanno dal numero di polinomi inseriti:
     * il menu inziale avrà
     * @implNote 0 polinomi
     * dunque possiamo solo aprire un file.
     */
    private void start() {
        salva.setEnabled(false);
        apri.setEnabled(true);
        somma.setEnabled(false);
        rimuovi.setEnabled(false);
        moltipl.setEnabled(false);
        derivata.setEnabled(false);
        differenza.setEnabled(false);
        valore.setEnabled(false);
    }
    /**
     * Metodo che permette di rendere attivi i metodi utilizzabili. 
     * Essi dipenderanno dal numero di polinomi inseriti:
     * il menu inziale avrà
     * @implNote un polinomio dunque possiamo utilizzare la derivata o la funzione, oppure salvare su file.
     */
    private void menuUno() {
        salva.setEnabled(true);
        somma.setEnabled(false);
        rimuovi.setEnabled(true);
        moltipl.setEnabled(false);
        derivata.setEnabled(true);
        differenza.setEnabled(false);
        valore.setEnabled(true);
    }
    /**
     * Metodo che permette di rendere attivi i metodi utilizzabili. 
     * Essi dipenderanno dal numero di polinomi inseriti:
     * il menu inziale avrà
     * @implNote 2+ polinomi
     * dunque possiamo utilizzare tutti i metodi tranne derivata e funzione.
     */
    private void menuDuePiu() {
        salva.setEnabled(true);
        somma.setEnabled(true);
        rimuovi.setEnabled(true);
        moltipl.setEnabled(true);
        derivata.setEnabled(false);
        valore.setEnabled(false);
        differenza.setEnabled(true);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        refreshGui();
        
        for (int i = 0; i < polinomi.size(); i++) {
            if(((JCheckBox)contenitorePolinomi.getComponent(i)).isSelected())
                contenitorePolinomi.getComponent(i).setBackground(new Color(212, 255, 0,30)); 
            else
                contenitorePolinomi.getComponent(i).setBackground(new ColorUIResource(30,30,30));
        }

        if(evt.getSource()==salva) 
            sceltaFile();
        if(evt.getSource()==inserisci) 
            inserisciPolinomio(inserimento.getText());
        if(evt.getSource()==somma)
            polinomiAdd();
        if(evt.getSource()==rimuovi) 
            rimuoviPolinomio();
        if(evt.getSource()==moltipl) 
            moltiplicaPolinomi();
        if(evt.getSource()==differenza) 
            differenzaPolinomi();    
        if(evt.getSource()==derivata)
            calcolaDerivata();
        if(evt.getSource()==valore) 
            finestraValore();
        if(evt.getSource()==info) 
            InformazioniStudente();
		if (evt.getSource() == apri){
			try {
				apriDaFile();
			} catch (IOException e1) {
				JFrame f = new FinestraDErrore("Problemi con l'apertura del file. Riprova");
				f.setVisible(true);
			} 
        }
    }
    
    /**
     *  Calcola la differenza tra monomi selezionati, cioè il primo meno tutti gli altri :
     *  Risultato= p_1 - p_2-...-p_n
     */
    private void differenzaPolinomi() {
        ArrayList<PolinomioSet> al= selezionati();
        PolinomioSet daInserire= al.get(0).copy();

        for(int j=1;j<al.size();++j){			
			daInserire=(PolinomioSet)daInserire.add(al.get(j).molt(new Monomio(-1,0)));
		}
        
        //CONTROLLO SE LA DIFFERENZA HA ELIMINATO TUTTO
        if(vuoto(daInserire)){ 
            JFrame b= new FinestraDErrore("La differenza ha eliminato tutti i monomi"); 
            b.setVisible(true); 
            return;
        }
        //POLINOMIO NON VUOTO, un while per eliminare gli eventuali 0 dell'operazione.
        Iterator<Monomio> it= daInserire.iterator();
        while (it.hasNext()) {
            if(it.next().getCoeff()==0) it.remove();
        }
        JFrame frame=new FinestraFunzione("DIFFERENZA",daInserire,this); frame.setVisible(true);
        refreshGui();
    }
    /**
     * Metodo utilizzato quando l'utente vuole salvare il file, allora cliccando sul JmenuItem dedicato deve scegliere se creare un nuovo file su cui salvare i
     * polinomi selezionati, oppure salvarli su un file già esistente.
     */
    private void sceltaFile() {
        int nscelta= JOptionPane.showConfirmDialog(null, "Vuoi creare un nuovo file?", "Crea nuovo File", JOptionPane.YES_NO_OPTION);
        if(nscelta==JOptionPane.YES_OPTION){
            try {
            salvaFile(0);
        } catch (IOException e){
            JFrame finestra = new FinestraDErrore("Si è verificato un errore con il file, controlla bene e ritenta");
		    finestra.setVisible(true);}
        }else{
            try {
                salvaFile(1);
            } catch (IOException e) {
                JFrame finestra = new FinestraDErrore("Si è verificato un errore con il file, controlla bene e ritenta");
		        finestra.setVisible(true);
            }
        }
    }

    /**
     * Utilizzato per salvare i polinomi selezionati su un file che viene creato ad hoc oppure che l'utente ha selezionato da FileChooser 
     * @param i sta ad indicare se il file va salvato su un file esistente i==1 oppure no i!=1.
     * @throws IOException Nasce nel caso in cui il file non è stato trovato oppure non esiste, o un altro problema su file.
     */
    private void salvaFile(int i) throws IOException {
        JFileChooser finSceltaFile = new JFileChooser(); 
		String titolo= (i==1)? "Scegli il file su cui salvare ciò che hai selezionato": "Scegli directory su cui salvare il nuovo file e dagli un nome.txt";
        finSceltaFile.setDialogTitle(titolo);
        finSceltaFile.setFileFilter(new ControllaPercorsoFile());
		int a = finSceltaFile.showSaveDialog(this);
        try {
		    if (a == JFileChooser.APPROVE_OPTION) {
                fileTesto=finSceltaFile.getSelectedFile(); 
                BufferedWriter buffer = new BufferedWriter(new FileWriter(fileTesto));
                for (PolinomioSet polinomio : selezionati()) {
                    buffer.append(polinomio.toString());
                    buffer.newLine();
                }
                buffer.flush();
                buffer.close();
            }            
        } catch (FileNotFoundException e) {
            JFrame f = new FinestraDErrore("Il file non è stato trovato, o non esiste");
			f.setVisible(true);
        }
    }

    /**
     * Permette all'utente di aprire un file.txt presente sul suo pc, ed ottenere tutti i polinomi presenti nel file all'interno della grid della gui.
     * @throws IOException Errore sul file.
     * @throws IllegalArgumentException Se nel file sono presenti caratteri di testo che non formano un polinomio!
     * @throws IndexOutOfBoundsException Troppi polinomi presenti nel file che non entrano nella grid della gui,  bisognerà eliminarne qualcuno per aprire il file!
     * @throws FileNotFoundException Il file non è stato trovato, o non esiste!
     */
    private void apriDaFile() throws IOException {
        JFileChooser finSceltaFile = new JFileChooser(); finSceltaFile.setDialogTitle("Scegli il file che vuoi aprire");
		finSceltaFile.setFileFilter(new ControllaPercorsoFile());
		int a = finSceltaFile.showOpenDialog(this);
        try {
		    if (a == JFileChooser.APPROVE_OPTION) {
                fileTesto = finSceltaFile.getSelectedFile();
                BufferedReader buffer = new BufferedReader(new FileReader(fileTesto));
                String lineaLetta= "";
	            while (lineaLetta!=null) {
                    lineaLetta= buffer.readLine();  
                    if(lineaLetta==null) break;
                    RiconosciPolinomio.riconosciPolinomio(lineaLetta);
                    inserisciPolinomio(lineaLetta);
                }
                buffer.close();
            }             
        }catch (IllegalArgumentException e) {
            JFrame frame= new FinestraDErrore("Il file contiene testo che non è un polinomio");
            frame.setVisible(true);
        }
           catch (IndexOutOfBoundsException e) {
			JFrame frame= new FinestraDErrore("Troppi polinomi inseriti nel file, eliminane qualcuno per aprire il file!");
			frame.setVisible(true); 
		}   catch (FileNotFoundException e) {
			JFrame frame= new FinestraDErrore("Il file non è stato trovato, o non esiste");
			frame.setVisible(true);
		}
    }
    

    /**
     * Apre una nuova finestra in cui sono presenti le informazioni dello studente che ha creato questo progettino
     */
    private void InformazioniStudente() {
        JFrame inf= new FinestraInfo();
        inf.setVisible(true);
    }

    /**
     * Apre una nuova finestra di dialogo che riferisce il valore della funzione matematica con il valore inserito per il polinomio selezionato.
     */
    private void finestraValore() {
        PolinomioSet p= ottieniPolinomioSelezionato();        
        JFrame f= new FinestraValore(p); f.setVisible(true);
    }

    /**
     * Permette di ottenere il polinomio selezionato dall'utente su cui vuole eseguire qualche operazione
     * 
     */
    private PolinomioSet ottieniPolinomioSelezionato() {
        PolinomioSet p= new PolinomioSet();
        int c= contenitorePolinomi.getComponentCount();
        int i=0;
        while (i<c) {
            if(((JCheckBox)contenitorePolinomi.getComponent(i)).isSelected()) {    
                p=polinomi.get(i); break; }
            i++;
        } return p;
    }
    

    /**
     * Calcola la derivata del polinomio selezionato, mostrando su una nuova finestra il risultato che è possibile aggiungere o meno
     * Nel caso sia solo un numero esce fuori una finestra di errore.
     */
    private void calcolaDerivata() {
        PolinomioSet p= ottieniPolinomioSelezionato();
        boolean flag= false;
        for (int i = 0; i < p.toString().length(); i++) {
            if (p.toString().charAt(i)=='x') {
                flag=true;
            }
        }
        if (!flag) {
            JFrame a = new FinestraDErrore("Il polinomio selezionato" + p + " e' una costante. Dunque la derivata e' nulla");
            a.setVisible(true);
        }else{
        JFrame ap=new FinestraFunzione("DERIVATA",p.derivata(),this); ap.setVisible(true);
        refreshGui(); }
    }

    /**
     * Moltiplica tutti i polinomi selezionati, e mostra a schermo il risultato che può esser inserito insieme agli altri o meno.
     * Risultato= p_1 * p_2 *...* p_n
     */
    private void moltiplicaPolinomi() {
        ArrayList<PolinomioSet> al= selezionati();
        PolinomioSet daInserire= al.get(0).copy();

        for(int j=1;j<al.size();++j){			
			daInserire=(PolinomioSet)daInserire.molt(al.get(j));
		}
        
        JFrame mol=new FinestraFunzione("MOLTIPLICAZIONE",daInserire,this); mol.setVisible(true);
        refreshGui(); 
    }


    /**
     * Rimuove tutti i polinomi selezionati dall'utente.
     */
    private void rimuoviPolinomio() {
        int c = contenitorePolinomi.getComponentCount();
		int i = 0;
		while (i<c) {
			if(((JCheckBox)contenitorePolinomi.getComponent(i)).isSelected()) {
				polinomi.remove(i);
				c--;
				contenitorePolinomi.remove((JCheckBox)contenitorePolinomi.getComponent(i));
			}
            else
                i++;
        }
        refreshGui(); 
    }
    
    /**
     * Esegue la somma di tutti i polinomi selezionati, elimina gli 0 ottenuti e mostra a schermo il risultato che può esser inserito insieme agli altri o meno.
     * Risultato= p_1 + p_2 +...+ p_n
     */
    private void polinomiAdd() {
        ArrayList<PolinomioSet> p= selezionati();
        PolinomioSet ottenuto= p.get(0).copy();
        //SOMMO TUTTI I POLINOMI SELEZIONATI
		for(int i=1;i<p.size();++i){			
			ottenuto=(PolinomioSet)ottenuto.add(p.get(i));
		}
        //CONTROLLO SE L'ADDIZIONE HA ELIMINATO TUTTO
        if(vuoto(ottenuto)){ 
            JFrame b= new FinestraDErrore("L'addizione ha eliminato tutti i monomi"); 
            b.setVisible(true); 
            return;
        }
        //POLINOMIO NON VUOTO, un while per eliminare gli eventuali 0 dell'operazione.
        Iterator<Monomio> it= ottenuto.iterator();
        while (it.hasNext()) {
            if(it.next().getCoeff()==0) it.remove();
        }
        //FACCIO APPARIRE IL RISULTATO SU UNA NUOVA FINESTRA DI DIALOGO
        JFrame a=new FinestraFunzione("ADDIZIONE",ottenuto,this); a.setVisible(true);
        refreshGui(); 
    }

    /**
     * Utilizzato per controllare che il risultato dell'operazione non sia 0
     * @param ottenuto risultato dell'add
     * @return false se esiste un monomio con coefficiente diverso da 0;
     * true altrimenti
     */
    private boolean vuoto(PolinomioSet ottenuto) {
        for (Monomio m: ottenuto) {
            if(m.getCoeff()!=0) return false;
        }
        return true;
    }
    
    
    /**
     * @return un ArrayList di PolinomioSet che contiene tutti i polinomi selezionati.
     * @implNote Nel caso fosse selezionato un solo polinomio viene utilizzato tale metodo con il get(0).
     */
    private ArrayList<PolinomioSet> selezionati() {
        ArrayList<PolinomioSet> p=new ArrayList<>();
        int c= contenitorePolinomi.getComponentCount();
        int i=0;
        while (i<c) {
            if(((JCheckBox)contenitorePolinomi.getComponent(i)).isSelected())     
                p.add(polinomi.get(i)); 
            i++;
        }
        return p;
    }
    

    /**
     * Inserisce il polinomio all'interno dell'ArrayList dedicato e crea un nuovo JCheckBox con il polinomio.
     * @param pol Stringa passata per riconoscere se esso è un polinomio valido e nel caso inserirlo nella grid.
     */
    void inserisciPolinomio(String pol) {
        PolinomioSet p= new PolinomioSet();
        if(contenitorePolinomi.getComponentCount()>=100) throw new IndexOutOfBoundsException();
        if (pol.equals("")) {
            JFrame frame= new FinestraDErrore("Nessun polinomio inserito");
            frame.setVisible(true);
            return;
        }
        try{
            RiconosciPolinomio.riconosciPolinomio(pol.strip());
        }catch(IndexOutOfBoundsException e){
            JFrame frame= new FinestraDErrore("Troppi polinomi inseriti, eliminane qualcuno per inserirne altri!");
            frame.setVisible(true);
        }catch(IllegalArgumentException e){
            JFrame frame= new FinestraDErrore("Quello che hai scritto non è un polinomio!");
            frame.setVisible(true);
        }
        
        p = RiconosciPolinomio.riconosciPolinomio(pol.strip());        polinomi.add(p);        JCheckBox c = new JCheckBox(p.toString());        c.addActionListener(this);        contenitorePolinomi.add(c);        
        refreshGui(); 
    }
    
    
}

class FinestraDErrore extends JFrame{
    public FinestraDErrore(String t){
        setTitle("ERRORE");
        setSize(450,150);
        setLocation(450,400);
        JLabel testo= new JLabel(t,JLabel.CENTER); testo.setFont(new Font("Cambria Math",Font.PLAIN,16)); testo.setForeground(new ColorUIResource(255,0,0));
        this.add(testo);
    }
}

class FinestraInfo extends JFrame{
    Font font= new Font("Cambria Math", Font.PLAIN, 16);
    public FinestraInfo(){
        setTitle("Informazioni Studente"); setFont(getFont());
        setSize(300,100);
        setLocation(450,400);
        JLabel titolo= new JLabel("Progetto Polinomio Gui realizzato da",JLabel.CENTER); titolo.setForeground(new ColorUIResource(255, 82, 3));titolo.setFont(font);
        add(titolo,BorderLayout.NORTH);
        JLabel nome= new JLabel("Mattia Corigliano",JLabel.CENTER); nome.setForeground(new ColorUIResource(255, 82, 3));    nome.setFont(font);
        JLabel mat= new JLabel("Matricola 231301",JLabel.CENTER);   mat.setForeground(new ColorUIResource(255, 82, 3));     mat.setFont(font);
        add(nome,BorderLayout.CENTER);  add(mat,BorderLayout.SOUTH);
    }
}

class FinestraFunzione extends JFrame implements ActionListener{
    JButton aggiungi= new JButton("Aggiungi"), grazie= new JButton("Grazie");
    JLabel titolo= new JLabel();
    JPanel BS= new JPanel(), BD= new JPanel(); 
    Gui princ= new Gui();
    String polinomio;
    Font font=new Font("Cambria Math", Font.BOLD, 12);
    public FinestraFunzione(String op,Polinomio daInserire, Gui princ) {
        setTitle("Operazione richiesta");   setFont(font);
        setSize(600,350);            setLocation(450,400);
        JLabel pol= new JLabel(daInserire.toString(),JLabel.CENTER);    pol.setFont(new Font("Cambria Math", Font.PLAIN, 25));   
        switch (op) {
            case "ADDIZIONE" ->       titolo.setText("Il risultato dell'addizione che hai richiesto è");
            case "MOLTIPLICAZIONE" -> titolo.setText("Il risultato della moltiplicazione che hai richiesto è");
            case "DIFFERENZA" ->      titolo.setText("Il risultato della differenza che hai richiesto è");
            case "DERIVATA" ->        titolo.setText("Il risultato della derivata che hai richiesto è");
        }
        polinomio=pol.getText();
        this.princ=princ;

        titolo.setForeground(new ColorUIResource(255, 82, 3)); titolo.setFont(new Font("Cambria Math", Font.PLAIN, 20));
        BS.add(titolo,BorderLayout.CENTER);
        add(BS,BorderLayout.NORTH);

        add(pol,BorderLayout.CENTER);
        
        aggiungi.addActionListener(this);   grazie.addActionListener(this); 
        aggiungi.setFont(font);             grazie.setFont(font);
        BD.add(aggiungi);  BD.add(grazie);        add(BD,BorderLayout.SOUTH);
    }
    
    public FinestraFunzione(String valore,PolinomioSet p) {
        setTitle("Operazione richiesta");   setFont(font);
        setSize(600,350);           setLocation(450,400);
        String d=""+p.valore(Integer.parseInt(valore));
        JLabel pol= new JLabel(d,JLabel.CENTER);    pol.setFont(new Font("Cambria Math", Font.PLAIN, 25));    pol.setForeground(new ColorUIResource(255, 82, 3));
        
        titolo.setText("Il risultato della funzione che hai richiesto è");
        titolo.setForeground(new ColorUIResource(95,45,233)); titolo.setFont(new Font("Cambria Math", Font.PLAIN, 20));
        
        BS.add(titolo,BorderLayout.CENTER);        add(BS,BorderLayout.NORTH);        add(pol,BorderLayout.CENTER);
        grazie.addActionListener(this);            grazie.setFont(font);
        BD.add(grazie,BorderLayout.CENTER);        add(BD,BorderLayout.SOUTH);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==aggiungi){
            princ.inserisciPolinomio(polinomio); setVisible(false);}
        if(e.getSource()==grazie)
            setVisible(false);
    }
}

class FinestraValore extends JFrame implements ActionListener{
    JTextField input;
    JButton ok= new JButton("OK");
    JLabel titolo;
    PolinomioSet p;
    Font font= new Font("Cambria Math", Font.PLAIN, 14);
    public FinestraValore(PolinomioSet p){
        setTitle("Polinomio Funzione");  setFont(font);
        setSize(350,100);        setLocation(550,450);
        this.p=p;
        JLabel testo=new JLabel("Inserisci valore che vuoi dare alla variabile x:",JLabel.CENTER); testo.setFont(font);testo.setForeground(new ColorUIResource(255, 82, 3));
        add(testo,BorderLayout.NORTH);
        JPanel barrasotto= new JPanel();
        input= new JTextField(10);      ok.addActionListener(this);
        barrasotto.add(input);                  barrasotto.add(ok);
        add(barrasotto,BorderLayout.SOUTH);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==ok){
            if(input.getText().matches("\\d+")){
                JFrame a=new FinestraFunzione(input.getText(),p); a.setVisible(true);
                setVisible(false);
            }else{
                JFrame frame=new FinestraDErrore("Quello che hai inserito non è un valore, re-inseriscilo!"); frame.setVisible(true);
            }
        }                    
    }

}

final class ControllaPercorsoFile extends FileFilter{
    public boolean accept(File file) {
        if (file.isDirectory()) return true;
        String fname = file.getName().toLowerCase();
        return fname.endsWith(".txt");
      }
      public String getDescription() {
        return "File di testo";
      }
}

public class GuiPolinomio {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            
        } catch (Exception e) {
            System.out.println("ciao");
        }
        JFrame finestra = new Gui();
        finestra.setVisible(true);
    }
    
}