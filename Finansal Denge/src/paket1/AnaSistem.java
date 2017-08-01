package paket1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.awt.PrintJob;

public class AnaSistem extends JFrame implements ActionListener {
	
	private JLabel etkLogo;
	private JLabel etkYazilim;
	private JLabel hosgeldin;
	private JLabel yazar;
	private JPanel durumCubuk = new JPanel();           //hosgeldin ve prog.ad� 
	private JDesktopPane masaustu = new JDesktopPane(); //masa�st�
	private JMenuItem yHesap, yazdir, kapat;  		   //Dosya men�s� se�enekleri
	private JMenuItem paraYat, paraCek,
			silMusteri, isimdenBul, noBul;	 	      //D�zenle men�s� se�enekleri
	private JMenuItem bireyListe, tumListe;  	     //G�r�n�m men�s� se�enekleri
	private JMenuItem aktifPenKapat, tumPenKapat;   //Pencere men�s� se�enekleri
	private JMenuItem kisayol, bilgi;			   //Yard�m men�s� se�enekleri
	private JMenuBar cubuk;						  //men� �ubu�u
	private JMenu dosya, duzen, gorunum,		 // ana men�
			pencere, yardim;
	private JToolBar aracCubuk;					//ara� �ubu�u
	private JButton dgmYeni, dgmYazdir, dgmCek, //ara� �ubu�u butonlar�
	dgmYatir, dgmSil, dgmBul;
	
	//g�ncel sistem tarihini al
	private java.util.Date gunTarih = new java.util.Date();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
	private String d = sdf.format(gunTarih);
	
	//kay�tlar� okumak ve saklamak i�in kullan�lan de�i�kenler
	private int satir = 0;
	private int toplam = 0;
	
	//dosyadan kayit �a��rmak i�in kullan�lan string tipi dizi
	private String kayitCagirici [][] = new String [500][6];
	
	//kayitlari okumak i�in kullanilan de�i�kenler
	private FileInputStream fis;
	private DataInputStream dis;
	
	//kurucu metod
	public AnaSistem() {
		//program ba�l���
		super("Pars Finansal Denge");
		
		//men� �ubu�u olu�tur
		cubuk = new JMenuBar();
		
		//logo olu�tur
		etkLogo = new JLabel();
		etkYazilim = new JLabel();
		
		//ana pencer ayarlar�
		setSize(1079, 591);
		setJMenuBar(cubuk);
		
		//ana pencereyi kapatma
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				uygulamaCik();
			}
		}
		);
		
		//uygulaman�n ekrandaki konumu
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
		
		//men� �ubu�u elemenlar� olu�tur ve �ubu�a ekle
		dosya = new JMenu("Dosya");
		dosya.setMnemonic('D');
		cubuk.add(dosya);
		duzen = new JMenu("D�zenle");
		duzen.setMnemonic('z');
		cubuk.add(duzen);
		gorunum = new JMenu("G�r�n�m");
		gorunum.setMnemonic('G');
		cubuk.add(gorunum);
		pencere = new JMenu("Pencere");
		pencere.setMnemonic('P');
		cubuk.add(pencere);
		yardim = new JMenu("Yard�m");
		yardim.setMnemonic('Y');
		cubuk.add(yardim);
		
		//Dosya men�s�n�n elemanlar�	
		yHesap = new JMenuItem("Yeni Hesap");
		yHesap.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		yHesap.addActionListener(this);
		yazdir= new JMenuItem("Hesap Ekstresi Yazd�r");
		yazdir.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		yazdir.addActionListener(this);
		kapat = new JMenuItem("��k��");
		kapat.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		kapat.addActionListener(this);
		
		//D�zenle men�s�n�n elemanlar�	
		paraYat = new JMenuItem("Hesaba Para Yat�r");
		paraYat.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		paraYat.addActionListener(this);
		paraCek = new JMenuItem("Hesaptan Para �ek");
		paraCek.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		paraCek.addActionListener(this);
		silMusteri = new JMenuItem("M��teri Kayd� Sil");
		silMusteri.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		silMusteri.addActionListener(this);
		isimdenBul = new JMenuItem("�simden M��teri Bul");
		isimdenBul.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		isimdenBul.addActionListener(this);
		noBul = new JMenuItem("Hesap no ile M��teri Bul");
		noBul.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		noBul.addActionListener(this);
		
		//G�r�n�m men�s�n�n elemanlar�
		bireyListe = new JMenuItem("Tek M��teri D�k�m�");
		bireyListe. setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		bireyListe.addActionListener(this);
		tumListe = new JMenuItem("Mevcut M��terilerin D�k�m�");
		tumListe.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		tumListe.addActionListener(this);
		
		//Pencere men�s�n�n elemanlar�
		aktifPenKapat = new JMenuItem("Aktif Pencereyi Kapat");
		aktifPenKapat.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		aktifPenKapat.addActionListener(this);
		tumPenKapat = new JMenuItem("T�m Pencereleri Kapat");
		tumPenKapat.setAccelerator(
		KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
		tumPenKapat.addActionListener(this);
		
		//Yard�m men�s�n� elemanlar�
		kisayol = new JMenuItem("K�sayollar");
		kisayol.addActionListener(this);
		bilgi = new JMenuItem("Finansal Denge hakk�nda");
		bilgi.addActionListener(this);
	
		//ilgili elemanlar� Dosya men�s�ne ekle
		dosya.add(yHesap);
		dosya.addSeparator();
		dosya.add(yazdir);
		dosya.addSeparator();
		dosya.add(kapat);
		
		//ilgili elemanlar� D�zenle men�s�ne ekle
		duzen.add(paraYat);
		duzen.add(paraCek);
		duzen.addSeparator();
		duzen.add(silMusteri);
		duzen.addSeparator();
		duzen.add(isimdenBul);
		duzen.add(noBul);
		
		//ilgili elemanlar� G�r�n�m men�s�ne ekle
		gorunum.add(bireyListe);
		gorunum.add(tumListe);
		
		//ilgili elemanlar� Pencere men�s�ne ekle
		pencere.add(aktifPenKapat);
		pencere.add(tumPenKapat);
		
		//ilgili elemanlar� Yard�m men�s�ne ekle
		yardim.add(kisayol);
		yardim.add(bilgi);
		
		//ara� �ubu�u butonlar� olu�tur
		dgmYeni = new JButton(new ImageIcon("C:/resim/admin.png"));
		dgmYeni.setToolTipText("Yeni Hesap");
		dgmYeni.addActionListener(this);
		dgmYazdir = new JButton(new ImageIcon("C:/resim/icon.png"));
	    dgmYazdir.setToolTipText("Yazd�r");
	    dgmYazdir.addActionListener(this);
	    dgmCek = new JButton(new ImageIcon("C:/resim/atm.png"));
	    dgmCek.setToolTipText("Para �ek");
	    dgmCek.addActionListener(this);
	    dgmYatir = new JButton(new ImageIcon("C:/resim/payment.png"));
	    dgmYatir.setToolTipText("Para Yat�r");
	    dgmYatir.addActionListener(this);
	    dgmSil = new JButton(new ImageIcon("C:/resim/user.png"));
	    dgmSil.setToolTipText("M��teri Sil");
	    dgmSil.addActionListener(this);
	    dgmBul = new JButton(new ImageIcon("C:/resim/search.png"));
	    dgmBul.setToolTipText("M��teri Bul");
	    dgmBul.addActionListener(this);
	    etkYazilim.setIcon(new ImageIcon("C:/resim/yazilim.png"));
	    etkLogo.setIcon(new ImageIcon("C:/resim/logo.png"));
	   
		
		//ara� �ubu�u olu�tur
	    aracCubuk = new JToolBar();
	    aracCubuk.setFloatable(true);
	    aracCubuk.add(dgmYeni);
	    aracCubuk.add(dgmYazdir);
	    aracCubuk.add(dgmCek);
	    aracCubuk.add(dgmYatir);
	    aracCubuk.add(dgmSil);
	    aracCubuk.add(dgmBul);
	    aracCubuk.add(etkYazilim);
	    aracCubuk.add(etkLogo);
	    
	    //durum �ubu�u
	    yazar = new JLabel("Enki ve Engin Yaz�l�m", Label.LEFT);
	    yazar.setForeground(Color.black);
	    hosgeldin = new JLabel("Ho�geldiniz!!  Bug�n " + d +" ", JLabel.RIGHT);
	    hosgeldin.setForeground(Color.black);
	    durumCubuk.setLayout(new BorderLayout());
	    durumCubuk.add(yazar, BorderLayout.WEST);
	    durumCubuk.add(hosgeldin, BorderLayout.EAST);
	    
	    //grafik elemanlar�n� yerle�tir
	    add(aracCubuk, BorderLayout.NORTH);
	    add(masaustu, BorderLayout.CENTER);
	    add(durumCubuk, BorderLayout.SOUTH);
	    
	    //ana formu g�ster
	    setVisible(true);
	}

	//prog. men�lerini y�r�ten olay tipleri ve dinleyici aray�z metodlar�
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		if(obj == yHesap || obj == dgmYeni) {
			boolean b = yavruPenAc("Yeni Hesap A�");
			if(b == false) {
				YeniHesap ynHsp = new YeniHesap();
				masaustu.add(ynHsp);
				ynHsp.setVisible(true);
			}
		}
		else if(obj == dgmYazdir || obj == yazdir) {
			getHesapNo();
		}
		else if( obj == kapat) {
			uygulamaCik();
		}
		else if(obj == paraYat || obj == dgmYatir) {
			boolean b = yavruPenAc("Hesaba Para Yat�r");
			if(b == false) {
			ParaYatir prYtr = new ParaYatir();
			masaustu.add(prYtr);
			prYtr.setVisible(true);	
		 }
		}
		else if(obj == paraCek || obj == dgmCek) {
			boolean b = yavruPenAc("Hesaptan Para �ek");
			if(b == false) {
			ParaCek prCk = new ParaCek();
			masaustu.add(prCk);
			prCk.setVisible(true);	
		 }
		}
		else if(obj == silMusteri || obj == dgmSil) {
			boolean b = yavruPenAc("M��teri Kayd� Sil");
			if(b == false) {
			MusteriSil mSil = new MusteriSil();
			masaustu.add(mSil);
			mSil.setVisible(true);	
		 }
		}
		else if(obj == noBul || obj == dgmBul) {
			boolean b = yavruPenAc("Hesap no ile M��teri Bul");
			if(b == false) {
			HesapBul hBul = new HesapBul();
			masaustu.add(hBul);
			hBul.setVisible(true);	
		 }
		}
		else if(obj == isimdenBul) {
			boolean b = yavruPenAc("�simden M��teri Bul");
			if(b == false) {
			IsimBul iBul = new IsimBul();
			masaustu.add(iBul);
			iBul.setVisible(true);	
		 }
		}
		else if(obj == bireyListe) {
			boolean b = yavruPenAc("Tek M��teri D�k�m�");
			if(b == false) {
			TekGoster tGoster = new TekGoster();
			masaustu.add(tGoster);
			tGoster.setVisible(true);	
		 }
		}
		else if(obj == tumListe) {
			boolean b = yavruPenAc("Mevcut M��terilerin D�k�m�");
			if(b == false) {
			MusterileriGoster mGoster = new MusterileriGoster();
			masaustu.add(mGoster);
			mGoster.setVisible(true);	
		 }
		}
		else if(obj == aktifPenKapat) {
			try {
				masaustu.getSelectedFrame().setClosed(true);
			}
			catch (Exception KapatExc) {
			}
		}
		else if(obj == tumPenKapat) {
	   JInternalFrame cerceveler[]= masaustu.getAllFrames(); 
	   for(int i =0; i< cerceveler.length; i++) {
		   try {
			   cerceveler[i].setClosed(true);
		   }
		   catch (Exception KapatExc) { }
	      }
		}
		else if(obj == bilgi) {
	String mesaj = "Pars Finansal Denge.\n\n" + "Created & Designed By:\n" +
		"Engin Bozkurt\n\n" + "E-mail :\nengin.bozkurt034@gmail.com";
	JOptionPane.showMessageDialog(this, mesaj, "Pars Finansal Denge  hakk�nda",
			JOptionPane.PLAIN_MESSAGE);
		}
	}

	//prog.dan ��kmak i�in kullan�lan uygulamaCik() metodu
	public void uygulamaCik() {
		try {
		int onay = JOptionPane.showConfirmDialog(this,
				"Programdan ��kmak istiyor musunuz ?",
		"Program Kapat�l�yor", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (onay == JOptionPane.YES_NO_OPTION) {
			setVisible(false);
			dispose();  //sistem kaynaklar�n� sebest b�rak
	System.out.println("Finansal Dengeyi kulland���n�z i�in te�ekk�rler");
	System.exit(0);	
		}
		else if(onay == JOptionPane.NO_OPTION) {
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}
	catch (Exception e) { }
   }
	
	//a��k pencereler aras�nda ge�i� yapan d�ng�
	public boolean yavruPenAc(String baslik) {
		JInternalFrame[] cocuklar = masaustu.getAllFrames();
		for(int i =0; i < cocuklar.length; i++) {
			if(cocuklar[i].getTitle().equalsIgnoreCase(baslik)) {
				cocuklar[i].setVisible(true);
				return true;
			}	
		}
		return false;
	} 
	
	//hesap ekstresi alma ve yazd�rma i�lemini ger�ekle�tiren HesapNo() metodu
	void getHesapNo() {
		String yazma;
		satir = 0;
		boolean b = diziYukle();
		if(b == false) { }
		 else
		 {
			 try {
	yazma = JOptionPane.showInputDialog(this,
	"Hesap ekstresini yazd�rmak i�in hesap no giriniz.\n", JOptionPane.PLAIN_MESSAGE);
	if(yazma == null) { }
	if (yazma.equals("")) {
		JOptionPane.showInputDialog(this, "Yazd�rma i�lemi i�in hesap no giriniz",
				JOptionPane.PLAIN_MESSAGE);
		getHesapNo();
			}
	   else  {
		   kayitBul(yazma);
	       }
		 }
			catch(Exception e) { }
	     }
	}
	
	//dosyadan t�m kay�tlar� �a��ran diziYukle() metodu
	boolean diziYukle() {
		boolean b = false;
		try {
			fis = new FileInputStream("Banka.dat");
			dis = new DataInputStream(fis);
			
			while(true) {
				for(int i=0; i < 6; i++) {
					kayitCagirici[satir][i] = dis.readUTF();
				}
				satir++ ;
			}
		}
		catch(Exception ex) {
			toplam = satir;
			if (toplam == 0) {
	JOptionPane.showMessageDialog(null,
	"Dosyada kay�tl� veri yok", "Bo� Dosya", JOptionPane.PLAIN_MESSAGE);
	 b = false;
		}
			else {
				b = true;
			}
			try {
				dis.close();
				fis.close();
			}
			
			catch(Exception exp) { }
		}
	 return b;
    }
	
	//kayitBul() metodu
	void kayitBul(String kayit) {
		boolean bulundu = false;
		for (int x = 0; x < toplam; x++) {	
		if(kayitCagirici[x][0].equals(kayit)) {
			bulundu = true;
			kayitYazdir(ciktiOlustur(x));
			break;
		}	
	}
		if (bulundu == false) {
	JOptionPane.showMessageDialog(this, kayit + 
	"Hesap numarali kayit yok", "Yanl�� hesap no",JOptionPane.PLAIN_MESSAGE);
		getHesapNo();
		}
	}
	
	//g�ncel kayd� yazd�rma i�lemine haz�r hale getrime
	String ciktiOlustur(int kayit) {
		String bilgi;
		String bilgi0 = "               Enki ve Engin Yaz�l�m               \n";
		String bilgi1 = "               M��teri Hesap D�k�m�             \n";
		String bilgi2 = "  Hesap No.:       " + kayitCagirici[kayit][0] + "\n";
		String bilgi3 = "  M��teri �smi:     " + kayitCagirici[kayit][1] + "\n";
		String bilgi4 = "  Son ��lem Tarihi:  " + kayitCagirici[kayit][2] + " " + kayitCagirici[kayit][3] + " " + kayitCagirici[kayit][4] + "\n";
		String bilgi5 = "  G�ncel Bakiye:   " + kayitCagirici[kayit][5] + "\n\n";
		String bilgi6 = "          Copyright � 2013 Engin Bozkurt.\n";	
		String sep0 = " -----------------------------------------------------------\n";
		String sep1 = " -----------------------------------------------------------\n";
		String sep2 = " -----------------------------------------------------------\n";
		String sep3 = " -----------------------------------------------------------\n";
		String sep4 = " -----------------------------------------------------------\n\n";

		bilgi = bilgi0 + sep0 + bilgi1 + bilgi2 + sep1 + bilgi3 + sep2 + bilgi4 + sep3 + bilgi5 + sep4 + bilgi6;
		return bilgi;	
	}
	
	//yazd�rma i�lemi
	void kayitYazdir(String kayit) {
		StringReader sr = new StringReader(kayit);
		LineNumberReader satirNo = new LineNumberReader(sr);
		Font yaziYuzu = new Font("Arial", Font.PLAIN, 12);
		Properties p = new Properties();
		PrintJob pJob = getToolkit().getPrintJob(this, 
		"M��teri Hesap Ekstresi Yazd�r", p);
		
		if(pJob != null) {
			Graphics gr = pJob.getGraphics();
			if(gr != null) {
				FontMetrics fm = gr.getFontMetrics();
				int kenarBos = 20;
				int sayfaYuk = pJob.getPageDimension().height - kenarBos;
				int fontYuk = fm.getHeight();
				int fontKuyruk = fm.getDescent();
				int guncelYuk = kenarBos;
				String sonrakiSatir;
				gr.setFont(yaziYuzu);
				
			try {
				do {
					sonrakiSatir = satirNo.readLine();
					if(sonrakiSatir != null) {
						if((guncelYuk + fontYuk) > sayfaYuk ) { //yeni sayfa
							gr.dispose();
							gr = pJob.getGraphics();
							guncelYuk = kenarBos;
							}
						guncelYuk += fontYuk;
							if(gr != null) {
								gr.setFont(yaziYuzu);
					gr.drawString(sonrakiSatir, kenarBos, guncelYuk - fontKuyruk);
							}
					
						}	
					}
					while(sonrakiSatir != null);
						}
							catch(EOFException eof) { }
							catch(Throwable t) { }
						}
						gr.dispose();		
				}
			if(pJob != null) 
				pJob.end();
		}	
	}
