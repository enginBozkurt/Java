package paket1;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;

public class HesapBul extends JInternalFrame implements ActionListener {
	private JPanel jpBul = new JPanel();
	private JLabel etkNo, etkIsim, etkTarih, etkBakiye;
	private JTextField metNo, metIsim, metTarih, metBakiye;
	private JButton dgmBul, dgmIptal;
	
	private int satir = 0;
	private int toplam = 0;
	
	//dosyadan kay�t �a��ran string tipi dizi
	private String kayitCagirici[][] = new String[500][6];
	
	private FileInputStream fis;
	private DataInputStream  dis;
	
	HesapBul() {
		super("Hesap no ile M��teri Bul", false, true, false, true);
		setSize(350, 235);
		
		jpBul.setLayout(null);
		
		etkNo = new JLabel("Hesap No:");
		etkNo.setForeground(Color.black);
		etkNo.setBounds(15, 20, 80, 25);
		etkIsim = new JLabel("M��teri �smi:");
		etkIsim.setForeground(Color.black);
		etkIsim.setBounds(15, 55, 80, 25);
		etkTarih = new JLabel("Son ��lem Tarihi:");
		etkTarih.setForeground(Color.black);
		etkTarih.setBounds(15, 90, 100, 25);
		etkBakiye = new JLabel("Bakiye:");
		etkBakiye.setForeground(Color.black);
		etkBakiye.setBounds(15, 125, 80, 25);
		
		metNo = new JTextField();
		metNo.setHorizontalAlignment(JTextField.RIGHT);
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
		metBakiye.setBounds(125, 125, 205, 25);
		
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
		
		//D��meleri hizala
	    dgmBul = new JButton("Bul");
	    dgmBul .setBounds(20, 165, 120, 25);
	    dgmBul.addActionListener(this);
		dgmIptal = new JButton("�ptal");
		dgmIptal.setBounds(200, 165, 120, 25);
		dgmIptal.addActionListener(this);
		
		//Elemanlar� panele ekle
		jpBul.add(etkNo);
		jpBul.add(metNo);
		jpBul.add(etkIsim);
		jpBul.add(metIsim);
		jpBul.add(etkTarih);
		jpBul.add(metTarih);
		jpBul.add(etkBakiye);
		jpBul.add(metBakiye);
		jpBul.add(dgmBul);
		jpBul.add(dgmIptal);
				
		//paneli pencereye ekle
		add(jpBul);
								
		//haf�zadaki mevcut t�m kay�tlar� y�kler
		diziYukle();
								
		//pencereyi g�ster
		setVisible(true);
	  }
	
	//olay g�d�ml� prog. par�as�
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
			if(obj == dgmBul) {
				if(metNo.getText().equals("")) {
		JOptionPane.showMessageDialog(this,"L�tfen Hesap no giriniz",
							"Bo� Alan", JOptionPane.PLAIN_MESSAGE);
					metNo.requestFocus();	
						}
				else {
					satir = 0;
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
			metTarih.setText(kayitCagirici[kayit][2] + " " +kayitCagirici[kayit][3] + 
					" " +kayitCagirici[kayit][4]);
			metBakiye.setText(kayitCagirici[kayit][5]);
					}
	
	//metin alanlar�n� bo�altmak i�in kullan�lan metod
		void metinSil() {
					metNo.setText("");
					metIsim.setText("");
					metTarih.setText("");
					metBakiye.setText("");
					metNo.requestFocus();
				}
		
	//pencerenin t�m d��melerini kilitleyen dugmeKilit() metodu
	void dugmeKilit() {
			metNo.setEnabled(false);
			dgmBul.setEnabled(false);
				}	
	}
