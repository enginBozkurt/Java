package paket1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class ParaYatir extends JInternalFrame implements ActionListener {
	private JPanel jpYat = new JPanel();
	private JLabel etkIsim, etkNo, etkTarih, etkParaYatir;
	private JTextField metIsim, metNo, metParaYatir;
	private JComboBox<String> cboAy, cboGun, cboYil;
	private JButton dgmKaydet, dgmIptal;
	
	private int kayitSayac = 0;
	private int satir = 0;
	private int toplam = 0;
	private int guncel;
	private int yatanPara ;
	
	//dosyadan kayýt çaðýran string tipi dizi
	private String kayitCagirici[][] = new String[500][6];
	
	private FileInputStream fis;
	private DataInputStream  dis;
	
	ParaYatir() {
		super("Para Yatýr", false, true, false, true);
		setSize(360, 235);
		
		jpYat.setLayout(null);
		
		etkNo = new JLabel("Hesap No:");
		etkNo.setForeground(Color.black);
		etkNo.setBounds(15, 20, 80, 25);
		etkIsim = new JLabel("Müþteri Ýsmi:");
		etkIsim.setForeground(Color.black);
		etkIsim.setBounds(15, 55, 80, 25);
		etkTarih = new JLabel("Para Yatýrma Tarihi:");
		etkTarih.setForeground(Color.black);
		etkTarih.setBounds(15, 90, 120, 25);
		etkParaYatir = new JLabel("Yatýrýlan Para Miktarý:");
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
		
		//Tarih seçenekleri
		String aylar[] = {"Ocak", "Þubat", "Mart", "Nisan", "Mayýs",
		"Haziran", "Temmuz", "Aðustos", "Eylül", "Ekim", "Kasým", "Aralýk"};
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
				
		//sayýsal metin kutularýna sadece sayý giriþi yapýlmasý
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
		
	    //Düðmeleri hizala
	    dgmKaydet = new JButton("Kaydet");
	    dgmKaydet.setBounds(20, 165, 120, 25);
		dgmKaydet.addActionListener(this);
		dgmIptal = new JButton("Ýptal");
		dgmIptal.setBounds(185, 165, 120, 25);
		dgmIptal.addActionListener(this);
		
	   //metin alanýna kullanýcý son odaklandýðýnda yapýlan hesap no kontrolü
		metNo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) { }
			public void focusLost(FocusEvent fe) {
				if(metNo.getText().equals("")) { }
				else {
					satir = 0;
				    diziYukle();  //hafýzadaki mevcut tüm kayýtlarý yükler
				    kayitBul();   //hesap no nun sistemdeki varlýðýný kontrol eder
			}
		}
	});
		
		//Elemanlarý panele ekle
		jpYat.add(etkNo);
		jpYat.add(metNo);
		jpYat.add(etkIsim);
		jpYat.add(metIsim);
		jpYat.add(etkTarih);
		jpYat.add(cboGun);
		jpYat.add(cboAy);
		jpYat.add(cboYil);
		jpYat.add(etkParaYatir);
		jpYat.add(metParaYatir);
		jpYat.add(dgmKaydet);
		jpYat.add(dgmIptal);
		
		//paneli pencereye ekle
		add(jpYat);
		
		//hafýzadaki mevcut tüm kayýtlarý yükler
		diziYukle();
		
		//pencereyi göster
		setVisible(true);
}

	   //olay güdümlü prog. parçasý
		public void actionPerformed(ActionEvent ae) {
				Object obj = ae.getSource();
				if(obj == dgmKaydet) {
					if(metNo.getText().equals("")) {
		JOptionPane.showMessageDialog(this,"Lütfen Hesap no giriniz",
						"Boþ Alan", JOptionPane.PLAIN_MESSAGE);
				metNo.requestFocus();	
					}
					else if(metParaYatir.getText().equals("")) {
		JOptionPane.showMessageDialog(this,"Lütfen yatýrýlacak miktarý giriniz",
						"Boþ Alan", JOptionPane.PLAIN_MESSAGE);
				metParaYatir.requestFocus();	
			}
					else {
						kayitDuzenle();
					}
				}
			if(obj == dgmIptal) {
				metinSil();
				setVisible(false);
				dispose();
			 }
			}
		
		//bütün kayýtlarý yükleyen diziYukle() metodu
		void diziYukle() {
			try {
				fis = new FileInputStream("Banka.dat");
				dis = new DataInputStream(fis);	
		//dizi yükleyen döngü
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
				JOptionPane.showMessageDialog(this,"Kayýt dosyasý boþ ",
				"Boþ Dosya", JOptionPane.PLAIN_MESSAGE);
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
				
	   /**hesap no text alanýna girilen deðerin kayitCagirici[][]
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
			JOptionPane.showMessageDialog(this,"Hesap numarasý " + met +
			" olan müþteri yok", "Hatalý hesap no", JOptionPane.PLAIN_MESSAGE);
							 }
						}		
		
		//dizideki kaydý form üzerinde gösteren kayitGoster() metodu
		public void kayitGoster(int kayit) {
			metNo.setText(kayitCagirici[kayit][0]);
			metIsim.setText(kayitCagirici[kayit][1]);
			guncel = Integer.parseInt(kayitCagirici[kayit][5]);
			kayitSayac = kayit;
		}
		
		//metin alanlarýný boþaltmak için kullanýlan metod
		void metinSil() {
					metNo.setText("");
					metIsim.setText("");
					metParaYatir.setText("");
					metNo.requestFocus();
		}	
		
		//dizinin elemanlarýný deðiþtiren kayitDuzenle() metodu
		public void kayitDuzenle() {
			yatanPara = Integer.parseInt(metParaYatir.getText());
			kayitCagirici[kayitSayac][0] = metNo.getText();
			kayitCagirici[kayitSayac][1] = metIsim.getText();
			kayitCagirici[kayitSayac][2] = "" + cboGun.getSelectedItem();
			kayitCagirici[kayitSayac][3] = "" + cboAy.getSelectedItem();
			kayitCagirici[kayitSayac][4] = "" + cboYil.getSelectedItem();
			kayitCagirici[kayitSayac][5] = "" + (guncel + yatanPara);
			dosyaDuzenle(); //bu diziyi dosyaya kaydet		
		}
		
		//kullanýcýnýn yaptýðý kayýt deðiþikliðini dosyaya aktaran metod
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
		JOptionPane.showMessageDialog(this,"Kayýt baþarýyla güncellendi ",
			"Kayýt Gerçekleþti", JOptionPane.PLAIN_MESSAGE);
		metinSil();
		dos.close();
		fos.close();
			}
		 }
			catch(IOException ioe) {
	  JOptionPane.showMessageDialog(this,"Dosya ile ilgili bir hata oluþtu ",
		"hata", JOptionPane.PLAIN_MESSAGE);
			}		
		}	
		
		//pencerenin tüm düðmelerini kilitleyen dugmeKilit() metodu
		void dugmeKilit() {
			metNo.setEnabled(false);
			cboAy.setEnabled(false);
			cboGun.setEnabled(false);
			cboYil.setEnabled(false);
			metParaYatir.setEnabled(false);
			dgmKaydet.setEnabled(false);
		}	
}
