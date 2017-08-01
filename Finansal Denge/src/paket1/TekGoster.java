package paket1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class TekGoster extends JInternalFrame implements ActionListener {
	
	private JPanel jpGoster = new JPanel();
	private JLabel etkNo, etkIsim, etkTarih, etkBakiye;
	private JTextField metNo, metIsim, metTarih, metBakiye, metKayit;
	private JButton dgmBas, dgmOnceki, dgmSonraki, dgmSon;
	
	private int kayitSayac;
	private int satir = 0;
	private int toplam;
	
	//dosyadan kayýt çaðýran string tipi dizi
	private String kayitCagirici[][] = new String[500][6];
			
	private FileInputStream fis;
	private DataInputStream  dis;
	
	TekGoster() {
		super("Müþteri Hesabý Göster", false, true, false, true);
		setSize(350, 235);
		
		jpGoster.setLayout(null);
		
		etkNo = new JLabel("Hesap No:");
		etkNo.setForeground(Color.black);
		etkNo.setBounds(15, 20, 80, 25);
		etkIsim = new JLabel("Müþteri Ýsmi:");
		etkIsim.setForeground(Color.black);
		etkIsim.setBounds(15, 55, 80, 25);
		etkTarih = new JLabel("Son Ýþlem Tarihi:");
		etkTarih.setForeground(Color.black);
		etkTarih.setBounds(15, 90, 100, 25);
		etkBakiye = new JLabel("Bakiye:");
		etkBakiye.setForeground(Color.black);
		etkBakiye.setBounds(15, 125, 80, 25);
		
		metNo = new JTextField();
		metNo.setHorizontalAlignment(JTextField.RIGHT);
		metNo.setEnabled(false);
		metNo.setBounds(125, 20, 200, 25);
		metIsim = new JTextField();
		metIsim.setEnabled(false);
		metIsim.setBounds(125, 55, 200, 25);
		metTarih = new JTextField();
		metTarih.setEnabled(false);
		metTarih.setBounds(125, 90, 200, 25);
		metBakiye = new JTextField();
		metBakiye.setHorizontalAlignment(JTextField.RIGHT);
		metBakiye.setEnabled(false);
		metBakiye.setBounds(125, 125, 200, 25);
		metKayit = new JTextField();
		metKayit.setEnabled(false);
		metKayit.setHorizontalAlignment(JTextField.CENTER);
		metKayit.setBounds(115, 165, 109, 25);
		
		//Düðmeleri hizala
		dgmBas = new JButton("<<");
		dgmBas.setBounds(15, 165, 50, 25);
		dgmBas.addActionListener(this);
		dgmOnceki = new JButton("<");
		dgmOnceki.setBounds(65, 165, 50, 25);
		dgmOnceki.addActionListener(this);
		dgmSonraki = new JButton(">");
		dgmSonraki.setBounds(225, 165, 50, 25);
		dgmSonraki.addActionListener(this);
		dgmSon = new JButton(">>");
		dgmSon.setBounds(275, 165, 50, 25);
		dgmSon.addActionListener(this);
		
		//Elemanlarý panele ekle
		jpGoster.add(etkNo);
		jpGoster.add(metNo);
		jpGoster.add(etkIsim);
		jpGoster.add(metIsim);
		jpGoster.add(etkTarih);
		jpGoster.add(metTarih);
		jpGoster.add(etkBakiye);
		jpGoster.add(metBakiye);
		jpGoster.add(dgmBas);
		jpGoster.add(dgmOnceki);
		jpGoster.add(dgmSonraki);
		jpGoster.add(dgmSon);
		jpGoster.add(metKayit);
		
		//paneli pencereye ekle
		add(jpGoster);
		
		//hafýzadaki mevcut tüm kayýtlarý yükler ve form da göster
		diziYukle();
		kayitGoster(0);
		
		//pencereyi göster
		setVisible(true);	
			}
	
	//olay güdümlü prog. parçasý
	public void actionPerformed(ActionEvent ae) {
			Object obj = ae.getSource();
				if(obj == dgmBas) {
					kayitSayac = 0;
					kayitGoster(kayitSayac);
				}
				else if(obj == dgmOnceki) {
					kayitSayac = kayitSayac - 1;
					if(kayitSayac < 0) {
						kayitSayac = 0;
						kayitGoster(kayitSayac);
	JOptionPane.showMessageDialog(this,"Ýlk müþteri kaydýna ulaþtýnýz",
						"Ýlk Kayýt", JOptionPane.PLAIN_MESSAGE);
					}
					else {
						kayitGoster(kayitSayac);
					}	
				}
				else if(obj == dgmSonraki) {
					kayitSayac = kayitSayac  + 1;
					if(kayitSayac == toplam) {
						kayitSayac = toplam - 1;
						kayitGoster(kayitSayac);
	JOptionPane.showMessageDialog(this,"Son müþteri kaydýna ulaþtýnýz",
								"Son Kayýt", JOptionPane.PLAIN_MESSAGE);
							
					}
					else {
						kayitGoster(kayitSayac);
					}
				}
				else if(obj == dgmSon) {
					kayitSayac = toplam - 1;
					kayitGoster(kayitSayac);	
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
	//dizideki kaydý form üzerinde gösteren kayitGoster() metodu
	public void kayitGoster(int kayit) {
			metNo.setText(kayitCagirici[kayit][0]);
			metIsim.setText(kayitCagirici[kayit][1]);
			metTarih.setText(kayitCagirici[kayit][2] + " " + kayitCagirici[kayit][3] + 
				" " + 	kayitCagirici[kayit][4]);
			metBakiye.setText(kayitCagirici[kayit][5]);
			if(toplam == 0) {
		metKayit.setText(kayit + "/" + toplam); // Kayýt no ve toplam kaydý göster	
			}
			else {
		metKayit.setText((kayit + 1) + "/" + toplam); // Kayýt no ve toplam kaydý göster
				}
			}
	//pencerenin tüm düðmelerini kilitleyen dugmeKilit() metodu
	void dugmeKilit() {
			dgmBas.setEnabled(false);
			dgmSon.setEnabled(false);
			dgmSonraki.setEnabled(false);
			dgmOnceki.setEnabled(false);
					}	
		}
