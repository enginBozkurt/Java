package paket1;
import javax.swing.JWindow;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Baslat extends JWindow {
	Image resim = Toolkit.getDefaultToolkit().getImage("C:/resim/logo.jpg");
	ImageIcon resimIkon = new ImageIcon (resim);
	
	public Baslat() {
		try {
			setSize(358, 348);
			setLocationRelativeTo(null);
			setVisible(true);
			Thread.sleep(4000);
			dispose();
			new AnaSistem();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(resim, 0, 0, this);
		
	}

	public static void main(String[] args) {
		new Baslat();	
	}
}
