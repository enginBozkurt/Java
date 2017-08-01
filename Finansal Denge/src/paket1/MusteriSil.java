package paket1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class MusteriSil extends JInternalFrame implements ActionListener {
	
	private JPanel jpSil = new JPanel();
	private JLabel etkNo, etkIsim, etkTarih, etkBakiye;
	private JTextField metNo, metIsim, metTarih, metBakiye;
	private JButton dgmSil, dgmIptal;
	
	private int kayitSayac;
	private int satir = 0;
	private int toplam;
	
	//dosyadan kayýt çaðýran string tipi dizi
	private String kayitCagirici[][] = new String[500][6];
		
	private FileInputStream fis;
	private DataInputStream  dis;
	
	MusteriSil() {
		super("Müþteri Hesabý Sil", false, true, false, true);
		setSize(350, 235);
		
		jpSil.setLayout(null);
		
		etkNo = new JLabel("Hesap No:");
		etkNo.setForeground(Color.black);
		etkNo.setBounds(15, 20, 80, 25);
		etkIsim = new JLabel("Müþteri Ýsmi:");
		etkIsim.setForeground(Color.black);
		etkIsim.setBounds(15, 55, 90, 25);
		etkTarih = new JLabel("Son Ýþlem Tarihi:");
		etkTarih.setForeground(Color.black);
		etkTarih.setBounds(15, 90, 100, 25);
		etkBakiye = new JLabel("Bakiye:");
		etkBakiye.setForeground(Color.black);
		etkBakiye.setBounds(15, 125, 80, 25);
		
		metNo = new JTextField();
		metNo.setHorizontalAlignment(JTextField.RIGHT);
		metNo.setBounds(125, 20, 200, 25);
		metIsim = new JTextField();
		metIsim.setHorizontalAlignment(JTextField.RIGHT);
		metIsim.setBounds(125, 55, 200, 25);
		metTarih = new JTextField();
		metTarih.setHorizontalAlignment(JTextField.RIGHT);
		metTarih.setBounds(125, 90, 200, 25);
		metBakiye= new JTextField();
		metBakiye.setHorizontalAlignment(JTextField.RIGHT);
		metBakiye.setBounds(125, 125, 200, 25);
		
		//Düðmeleri hizala
	    dgmSil = new JButton("Sil");
	    dgmSil.setBounds(20, 165, 120, 25);
	    dgmSil.addActionListener(this);
		dgmIptal = new JButton("Ýptal");
		dgmIptal.setBounds(200, 165, 120, 25);
		dgmIptal.addActionListener(this);
		
		//Elemanlarý panele ekle
		jpSil.add(etkNo);
		jpSil.add(metNo);
		jpSil.add(etkIsim);
		jpSil.add(metIsim);
		jpSil.add(etkTarih);
		jpSil.add(metTarih);
		jpSil.add(etkBakiye);
		jpSil.add(metBakiye);
		jpSil.add(dgmSil);
		jpSil.add(dgmIptal);
		
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
		
		//paneli pencereye ekle
		add(jpSil);
				
		//hafýzadaki mevcut tüm kayýtlarý yükler
		diziYukle();
				
		//pencereyi göster
		setVisible(true);	
		}
	
		//olay güdümlü prog. parçasý
		public void actionPerformed(ActionEvent ae) {
				Object obj = ae.getSource();
					if(obj == dgmSil) {
						if(metNo.getText().equals("")) {
		JOptionPane.showMessageDialog(this,"Lütfen Hesap no giriniz",
						"Boþ Alan", JOptionPane.PLAIN_MESSAGE);
					metNo.requestFocus();	
						}
						else {
							silmeOnay();
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
			metTarih.setText(kayitCagirici[kayit][2] + " " + kayitCagirici[kayit][3] + 
				" " + kayitCagirici[kayit][4]);
			metBakiye.setText(kayitCagirici[kayit][5]);
			kayitSayac = kayit;
			}
			
		//silme iþlemi onayý alan silmeOnay() metodu
		void silmeOnay() {
			try {
				int onay = JOptionPane.showConfirmDialog(this,
			metIsim.getText() + " adlý müþteriyi silmek istiyor musunuz?" ,
			"Silme Ýþlemi", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				
				if(onay == JOptionPane.YES_OPTION) {
					kayitSil();
				}
				else if(onay == JOptionPane.NO_OPTION) { }
		}
			catch(Exception ex) { }
		}
		
		//diziden bir elemaný silen kayitSil() metodu
		void kayitSil() {
			try {
				if(kayitCagirici != null) {
					for(int i = kayitSayac; i < toplam; i++) {
						for(int r = 0; r < 6; r++) {
							kayitCagirici[i][r] = kayitCagirici[i+1][r];
							if( kayitCagirici[i][r] == null)
								break;
						}
					}
					toplam = toplam -1;
					dosyaSil();
				}	
			}
			catch (ArrayIndexOutOfBoundsException ex) { }
		}
		
		//silinen kayýttan sonra yeni dosyayý düzenleyen dosyaSil() metodu
		void dosyaSil() {
			try {
				FileOutputStream fos = new FileOutputStream("Banka.dat");
				DataOutputStream dos = new DataOutputStream(fos);
				if(kayitCagirici != null) {
					for(int i = 0; i < toplam; i++) {
						for(int r = 0; r < 6; r++) {
							dos.writeUTF(kayitCagirici[i][r]);
							if( kayitCagirici[i][r] == null) 
								break;
							}
						}
			JOptionPane.showMessageDialog(this, "Müþteri baþarýyla silindi",
			" Kayýt Silme Ýþlemi", JOptionPane.PLAIN_MESSAGE);
				metinSil();
					}
					else { }
					dos.close();
					fos.close();		
			}
			
			catch(IOException ioe) {
				JOptionPane.showMessageDialog(this, "Dosyayla ilgili bir hata oluþtu",
					"Hata", JOptionPane.PLAIN_MESSAGE);
				}	
			}
			
		//metin alanlarýný boþaltmak için kullanýlan metod
		void metinSil() {
					metNo.setText("");
					metIsim.setText("");
					metTarih.setText("");
					metBakiye.setText("");
					metNo.requestFocus();
			}	
		
		//pencerenin tüm düðmelerini kilitleyen dugmeKilit() metodu
		void dugmeKilit() {
					metNo.setEnabled(false);
					dgmSil.setEnabled(false);
				}	
	}
