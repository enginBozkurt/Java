package paket1;
import javax.swing.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;

public class MusterileriGoster extends JInternalFrame {
	
	private JPanel jpYansit = new JPanel();
	private DefaultTableModel dtmMusteri;
	private JTable tbMusteri;
	private JScrollPane jspTablo;
	
	private int satir = 0;
	private int toplam;
	
	//kay�tlar� dosyaya y�kleyen dizi
	private String satirBilgi[][];
	
	private FileInputStream fis;
	private DataInputStream dis;
	
	MusterileriGoster() {
		super("Mevcut T�m M��terilerin D�k�m�", false, true, false, true);
		setSize(475, 280);
		
		jpYansit.setLayout(null);
		
		diziYukle();
		
		tbMusteri = tabloYap();
		jspTablo = new JScrollPane(tbMusteri);
		jspTablo.setBounds(20, 20  ,425, 200);
		
		//tabloyu panele ekle
		jpYansit.add(jspTablo);
		
		//paneli pencereye ekle
		add(jpYansit);
		
		//pencereyi g�ster
		setVisible(true);	
					}
	
	//pencere a��kken dosyadan t�m kay�tlar� y�kleyen metod
	void diziYukle() {
		
		//t�m kay�tlar� dosyaya y�kleyen dizi
		String satirlar[][] = new String[500][6];
		
		try{
			fis = new FileInputStream("Banka.dat");
			dis = new DataInputStream(fis);	
			//dizi y�kleyen d�ng�
			while(true) {
				for(int i = 0; i<6 ; i++) {
				satirlar[satir][i] = dis.readUTF();
						}
						satir++;
							}
			}
		catch(Exception ex) {
			toplam = satir;
			satirBilgi = new String[toplam][4];
			if(toplam == 0) {
			JOptionPane.showMessageDialog(this,"Kay�t dosyas� bo� ",
						"Bo� Dosya", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				for(int i= 0; i< toplam; i++) {
					satirBilgi[i][0] = satirlar[i][0];
					satirBilgi[i][1] = satirlar[i][1];
					satirBilgi[i][2] = satirlar[i][2] + " " + satirlar[i][3] + " " + satirlar[i][4];
					satirBilgi[i][3] = satirlar[i][5];
				}
				try {
					dis.close();
					fis.close();
				}
				catch(Exception exp) { }	
				}
			}	
		}
	
	//tablo olu�turup ilgili data y� ekleyen tabloYap() metodu
	private JTable tabloYap() {
		String sutunlar[] = {"Hesap No", "M��teri �smi", "Hesap A��lma Tarihi", "Bakiye"};
		
		dtmMusteri = new DefaultTableModel(satirBilgi, sutunlar);
		tbMusteri = new JTable(dtmMusteri) {
			public boolean isCellEditable(int xSatir, int xSutun) {
						return false; //tablo i�eri�inin de�i�tirilmesini engeller
			}
		};
		
		//tablonon s�tunlar�n� boyutland�rma
		(tbMusteri.getColumnModel().getColumn(0)).setPreferredWidth(180);
		(tbMusteri.getColumnModel().getColumn(1)).setPreferredWidth(275);
		(tbMusteri.getColumnModel().getColumn(2)).setPreferredWidth(275);
		(tbMusteri.getColumnModel().getColumn(3)).setPreferredWidth(200);
		tbMusteri.setRowHeight(20);
		tbMusteri.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return tbMusteri;
		}
	}
