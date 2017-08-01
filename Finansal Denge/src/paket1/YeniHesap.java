package paket1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class YeniHesap extends JInternalFrame implements ActionListener {
	private JPanel jpBilgi = new JPanel();
	private JLabel etkNo, etkIsim, etkTarih, etkParaYatir;
	private JTextField metNo, metIsim, metParaYatir;
	private JComboBox<String> cboAy, cboGun, cboYil;
	private JButton dgmKaydet, dgmIptal;
	private int sayac = 0;
	private int satir = 0;
	private int toplam = 0;
	private String kayitCagirici[][] = new String[500][6];
	private String kayitlar[][] = new String[500][6];
	private FileInputStream fis;
	private DataInputStream dis;
	
	YeniHesap () {
		super("Yeni Hesap Olu�tur", false, true, false, true);
		setSize(360, 235);
		jpBilgi.setBounds(0, 0, 500, 115);
		jpBilgi.setLayout(null);
		
		etkNo = new JLabel("Hesap No:");
		etkNo.setForeground(Color.black);
		etkNo.setBounds(15, 20, 80, 25);
		etkIsim = new JLabel("M��teri �smi:");
		etkIsim.setForeground(Color.black);
		etkIsim.setBounds(15, 55, 80, 25);
		etkTarih = new JLabel("Hesap A�ma Tarihi:");
		etkTarih.setForeground(Color.black);
		etkTarih.setBounds(15, 90, 120, 25);
		etkParaYatir = new JLabel("Yat�r�lan Para Miktar�:");
		etkParaYatir.setForeground(Color.black);
		etkParaYatir.setBounds(15, 125, 120, 25);
		
		metNo = new JTextField();
		metNo.setHorizontalAlignment(JTextField.RIGHT);
		metNo.setBounds(130, 20, 205, 25);
		metIsim = new JTextField();
		metIsim.setHorizontalAlignment(JTextField.RIGHT);
		metIsim.setBounds(130, 55, 205, 25);
		metParaYatir= new JTextField();
		metParaYatir.setHorizontalAlignment(JTextField.RIGHT);
		metParaYatir.setBounds(130, 125, 205, 25);
		
		//say�sal metin kutular�na sadece say� giri�i yap�lmas�
		metNo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();		
		if(!((Character.isDigit(c)|| (c == KeyEvent.VK_BACK_SPACE)))) {
			getToolkit().beep();
			ke.consume();
	          }
		   }		
	    });
		metParaYatir.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
		if(!((Character.isDigit(c)|| (c == KeyEvent.VK_BACK_SPACE)))) {
			getToolkit().beep();
			ke.consume();
	          }
		   }		
	    });	
		
		//Tarih se�enekleri
		String aylar[] = {"Ocak", "�ubat", "Mart", "Nisan", "May�s",
		"Haziran", "Temmuz", "A�ustos", "Eyl�l", "Ekim", "Kas�m", "Aral�k"};
		cboAy = new JComboBox<String>(aylar);
		cboGun = new JComboBox<String>();
		cboYil = new JComboBox<String>();
		for(int i =1; i<=31; i++) {
			String gunler = "" + i;
			cboGun.addItem(gunler);
		}
		for(int i =2013; i<=2030; i++) {
			String yillar = "" + i;
			cboYil.addItem(yillar);
		}
		
		//Tarih combobox hizalama
		cboGun.setBounds(130, 90, 43, 25);
		cboAy.setBounds(178, 90, 92, 25);
		cboYil.setBounds(275, 90, 60, 25);
		
		//D��meleri hizala
		dgmKaydet = new JButton("Kaydet");
		dgmKaydet.setBounds(20, 165, 120, 25);
		dgmKaydet.addActionListener(this);
		dgmIptal = new JButton("�ptal");
		dgmIptal.setBounds(185, 165, 120, 25);
		dgmIptal.addActionListener(this);
		
		//Elemanlar� panele ekle
		jpBilgi.add(etkNo);
		jpBilgi.add(metNo);
		jpBilgi.add(etkIsim);
		jpBilgi.add(metIsim);
		jpBilgi.add(etkTarih);
		jpBilgi.add(cboGun);
		jpBilgi.add(cboAy);
		jpBilgi.add(cboYil);
		jpBilgi.add(etkParaYatir);
		jpBilgi.add(metParaYatir);
		jpBilgi.add(dgmKaydet);
		jpBilgi.add(dgmIptal);
		
		//paneli pencereye ekle
		add(jpBilgi);
		
		//Yeni hesap penceresi g�ster
		setVisible(true);
	}
		//olay g�d�ml� prog. par�as�
		public void actionPerformed(ActionEvent ae) {
			Object obj = ae.getSource();
			if(obj == dgmKaydet) {
				if(metNo.getText().equals("")) {
	JOptionPane.showMessageDialog(this,"L�tfen Hesap no giriniz",
					"Bo� Alan", JOptionPane.PLAIN_MESSAGE);
			metNo.requestFocus();	
				}
				else if(metIsim.getText().equals("")) {
	JOptionPane.showMessageDialog(this,"L�tfen M��teri �smi giriniz",
							"Bo� Alan", JOptionPane.PLAIN_MESSAGE);
			metIsim.requestFocus();				
			}
				else if(metParaYatir.getText().equals("")) {
	JOptionPane.showMessageDialog(this,"L�tfen yat�r�lacak miktar� giriniz",
					"Bo� Alan", JOptionPane.PLAIN_MESSAGE);
			metParaYatir.requestFocus();	
		}
				else {
					diziYukle();
					kayitBul();
				}
			}
		if(obj == dgmIptal) {
			metinSil();
			setVisible(false);
			dispose();
		 }
		}
		
		//b�t�n kay�tlar� y�kleyen diziYukle() metodu
		void diziYukle() {
			try {
				fis = new FileInputStream("Banka.dat");
				dis = new DataInputStream(fis);	
		//dizi y�kleyen d�ng�
				while(true) {
					for(int i = 0; i<6 ; i++) {
						kayitCagirici[satir][i] = dis.readUTF();
					}
					satir++;
					
				}
			}
		catch(Exception ex) {
			toplam = satir;
			if(toplam == 0) { }
			else {
				try {
				dis.close();
				fis.close();
			}
		catch(Exception exp) { }
		  }
		 }
		}
		
	 /**hesap no text alan�na girilen de�erin kayitCagirici[][]
	  * dizisiyle uyumunu denetleyen kayitBul() metodu
	  */
		void kayitBul() {
			boolean bulundu = false;
			for(int x =0; x < toplam; x++) {
				if(kayitCagirici[x][0].equals(metNo.getText())) {
					bulundu = true;
  JOptionPane.showMessageDialog(this,"Hesap numaras� " + metNo.getText() +
  "olan m��teri mevcut kay�tl� m��teriler aras�ndad�r", 
  "Hatal� No", JOptionPane.PLAIN_MESSAGE);
  		metinSil();
  		break;
				}
			}
	if(bulundu == false) {
		kayitEkle();
		 }
	}
	
		//diziye yeni eleman ekleyen kayitEkle() metodu
		void kayitEkle() {
			kayitlar[sayac][0] = metNo.getText();
			kayitlar[sayac][1] = metIsim.getText();
			kayitlar[sayac][2] = "" + cboGun.getSelectedItem();
			kayitlar[sayac][3] = "" + cboAy.getSelectedItem();
			kayitlar[sayac][4] = "" + cboYil.getSelectedItem();
			kayitlar[sayac][5] = metParaYatir.getText();
			
			dosyaKaydet();
			sayac ++;
		}
	
		//Dosyaya yeni kay�t ekleyen dosyaKaydet() metodu
		void dosyaKaydet() {
		try {
		FileOutputStream fos = new FileOutputStream("Banka.dat", true);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeUTF(kayitlar[sayac][0]);
		dos.writeUTF(kayitlar[sayac][1]);
		dos.writeUTF(kayitlar[sayac][2]);
		dos.writeUTF(kayitlar[sayac][3]);
		dos.writeUTF(kayitlar[sayac][4]);
		dos.writeUTF(kayitlar[sayac][5]);
		JOptionPane.showMessageDialog(this,"Hesap ba�ar�yla kaydedildi ",
	"Kaydetme i�lemi ger�ekle�ti", JOptionPane.PLAIN_MESSAGE);
		metinSil();
		dos.close();
		fos.close();
		}
		catch(IOException ioe) {
		JOptionPane.showMessageDialog(this,"Hatal� dosya ",
					"Hata", JOptionPane.PLAIN_MESSAGE);
		 }
		}

		//metin alanlar�n� bo�altmak i�in kullan�lan metod
		void metinSil() {
			metNo.setText("");
			metIsim.setText("");
			metParaYatir.setText("");
			metNo.requestFocus();
		}		
	}	
