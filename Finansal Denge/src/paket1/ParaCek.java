package paket1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class ParaCek extends JInternalFrame implements ActionListener {
	private JPanel jpCek = new JPanel();
	private JLabel etkIsim, etkNo, etkTarih, etkParaCek;
	private JTextField metIsim, metNo, metParaCek;
	private JComboBox<String> cboAy, cboGun, cboYil;
	private JButton dgmKaydet, dgmIptal;
	
	private int kayitSayac = 0;
	private int satir = 0;
	private int toplam = 0;
	private int guncelBakiye;
	private int cekilenPara ;
	
	//dosyadan kay�t �a��ran string tipi dizi
	private String kayitCagirici[][] = new String[500][6];
	
	private FileInputStream fis;
	private DataInputStream  dis;
	
	ParaCek() {
		super("Para �ek", false, true, false, true);
		setSize(360, 235);
		
		jpCek.setLayout(null);
		
		etkNo = new JLabel("Hesap No:");
		etkNo.setForeground(Color.black);
		etkNo.setBounds(15, 20, 80, 25);
		etkIsim = new JLabel("M��teri �smi:");
		etkIsim.setForeground(Color.black);
		etkIsim.setBounds(15, 55, 80, 25);
		etkTarih = new JLabel("Para �ekme Tarihi:");
		etkTarih.setForeground(Color.black);
		etkTarih.setBounds(15, 90, 120, 25);
		etkParaCek = new JLabel("�ekilen Para Miktar�:");
		etkParaCek.setForeground(Color.black);
		etkParaCek.setBounds(15, 125, 120, 25);
		
		metNo = new JTextField();
		metNo.setHorizontalAlignment(JTextField.RIGHT);
		//metin alan�na kullan�c� son odakland. yap�lan hesap no kontrol�
		metNo.addFocusListener(new FocusListener() {
		 public void focusGained(FocusEvent e) { }
			public void focusLost(FocusEvent fe) {
			  if(metNo.getText().equals("")) { }
				 else {
				   satir = 0;
				   diziYukle(); //haf�zadaki mevcut t�m kay�tlar� y�kler
				   kayitBul(); //hesap no nun sist. varl���n� kontrol eder
					}
				}
			});
		metNo.setBounds(130, 20, 205, 25);
		metIsim = new JTextField();
		metIsim.setHorizontalAlignment(JTextField.RIGHT);
		metIsim.setBounds(130, 55, 205, 25);
		metParaCek= new JTextField();
		metParaCek.setHorizontalAlignment(JTextField.RIGHT);
		metParaCek.setBounds(130, 125, 205, 25);
		
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
		metParaCek.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
				if(!((Character.isDigit(c)|| (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep();
					ke.consume();
			          }
				   }		
			    });	
		
		//D��meleri hizala
	    dgmKaydet = new JButton("Kaydet");
	    dgmKaydet.setBounds(20, 165, 120, 25);
		dgmKaydet.addActionListener(this);
		dgmIptal = new JButton("�ptal");
		dgmIptal.setBounds(185, 165, 120, 25);
		dgmIptal.addActionListener(this);
		
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
		
		//Elemanlar� panele ekle
		jpCek.add(etkNo);
		jpCek.add(metNo);
		jpCek.add(etkIsim);
		jpCek.add(metIsim);
		jpCek.add(etkTarih);
		jpCek.add(cboGun);
		jpCek.add(cboAy);
		jpCek.add(cboYil);
		jpCek.add(etkParaCek);
		jpCek.add(metParaCek);
		jpCek.add(dgmKaydet);
		jpCek.add(dgmIptal);
				
		//paneli pencereye ekle
		add(jpCek);
				
		//haf�zadaki mevcut t�m kay�tlar� y�kler
		diziYukle();
				
		//pencereyi g�ster
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
				else if(metParaCek.getText().equals("")) {
		JOptionPane.showMessageDialog(this,"L�tfen �ekilecek miktar� giriniz",
							"Bo� Alan", JOptionPane.PLAIN_MESSAGE);
					metParaCek.requestFocus();	
				}
				else {
					cekilenPara =Integer.parseInt(metParaCek.getText());
					if(guncelBakiye == 0) {
		JOptionPane.showMessageDialog(this, metIsim.getText() +
		"hesab�nda para bulunmamaktad�r","Bo� Hesap", JOptionPane.PLAIN_MESSAGE);
					metinSil();			
							}
					else if(cekilenPara > guncelBakiye) {
		JOptionPane.showMessageDialog(this, "�ekilmek istenen miktar hesap limitini a��yor",
			"Fazla Miktar", JOptionPane.PLAIN_MESSAGE);
					metParaCek.setText("");
					metParaCek.requestFocus();	
					}
					else {
						kayitDuzenle();
					}
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
					if(toplam == 0) {
		JOptionPane.showMessageDialog(this,"Kay�t dosyas� bo� ",
						"Bo� Dosya", JOptionPane.PLAIN_MESSAGE);
				dugmeKilit();
							}
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
		* dizisiyle uyumunu denetleyen kayitBul() metodu*/
		void kayitBul() {
			boolean bulundu = false;
				for(int x =0; x < toplam; x++) {
					if(kayitCagirici[x][0].equals(metNo.getText())) {
						bulundu = true;
						kayitGoster(x);
						break;
						}
					}
			if(bulundu == false) {
			String met = metNo.getText();
			metinSil();
			JOptionPane.showMessageDialog(this,"Hesap numaras� " + met +
			" olan m��teri yok", "Hatal� hesap no", JOptionPane.PLAIN_MESSAGE);
						}
					}
		
		//dizideki kayd� form �zerinde g�steren kayitGoster() metodu
		public void kayitGoster(int kayit) {
			metNo.setText(kayitCagirici[kayit][0]);
			metIsim.setText(kayitCagirici[kayit][1]);
			guncelBakiye = Integer.parseInt(kayitCagirici[kayit][5]);
			kayitSayac = kayit;
			}
		
		//metin alanlar�n� bo�altmak i�in kullan�lan metod
		void metinSil() {
			metNo.setText("");
			metIsim.setText("");
			metParaCek.setText("");
			metNo.requestFocus();
			}	
				
		//dizinin elemanlar�n� de�i�tiren kayitDuzenle() metodu
		public void kayitDuzenle() {
			cekilenPara = Integer.parseInt(metParaCek.getText());
			kayitCagirici[kayitSayac][0] = metNo.getText();
			kayitCagirici[kayitSayac][1] = metIsim.getText();
			kayitCagirici[kayitSayac][2] = "" + cboGun.getSelectedItem();
			kayitCagirici[kayitSayac][3] = "" + cboAy.getSelectedItem();
			kayitCagirici[kayitSayac][4] = "" + cboYil.getSelectedItem();
			kayitCagirici[kayitSayac][5] = "" + (guncelBakiye - cekilenPara);
		dosyaDuzenle(); //bu diziyi dosyaya kaydet		
			}
		
		//kullan�c�n�n yapt��� kay�t de�i�ikli�ini dosyaya aktaran metod
		public void dosyaDuzenle() {
			try {
				FileOutputStream fos = new FileOutputStream("Banka.dat");
				DataOutputStream dos = new DataOutputStream(fos);
					if(kayitCagirici != null) {
						for(int i = 0; i < toplam; i++) {
							for(int c = 0; c < 6; c++) {
								dos.writeUTF(kayitCagirici[i][c]);
								if(kayitCagirici[i][c] == null)
									break;
							}
						}
			JOptionPane.showMessageDialog(this,"Kay�t ba�ar�yla g�ncellendi ",
					"Kay�t Ger�ekle�ti", JOptionPane.PLAIN_MESSAGE);
				metinSil();
				dos.close();
				fos.close();
					}
				 }
			catch(IOException ioe) {
			 JOptionPane.showMessageDialog(this,"Dosya ile ilgili bir hata olu�tu ",
				"hata", JOptionPane.PLAIN_MESSAGE);
					}		
				}
		
		//pencerenin t�m d��melerini kilitleyen dugmeKilit() metodu
		void dugmeKilit() {
			metNo.setEnabled(false);
			cboAy.setEnabled(false);
			cboGun.setEnabled(false);
			cboYil.setEnabled(false);
			metParaCek.setEnabled(false);
			dgmKaydet.setEnabled(false);
				}	
	}
