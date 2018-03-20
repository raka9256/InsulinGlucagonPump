package View;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;

public class OptionsGui extends JFrame {

	private JPanel contentPane;

	private static volatile OptionsGui instance;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OptionsGui frame = new OptionsGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OptionsGui() {
	 
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 0, 0, 0));
		
		JPanel panel_up = new JPanel();
		panel.add(panel_up);
		panel_up.setLayout(null);
		
		JButton btnBack = new JButton();
		 try {
			    Image img = ImageIO.read(getClass().getResource("/images/back-arrow.png"));
			    btnBack.setIcon(new ImageIcon(img));
			  } catch (IOException ex) {
			  }
		btnBack.setBounds(10, 11, 89, 23);
		panel_up.add(btnBack);
		
		JPanel panel_down = new JPanel();
		panel.add(panel_down);
		panel_down.setLayout(new GridLayout(4, 2, 0, 0));
		
		JButton btnNewButton_2 = new JButton("PERSONAL PROFILE");
		panel_down.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("ALERT SETTINGS");
		panel_down.add(btnNewButton_3);
		
		JButton btnNewButton_1 = new JButton("PUMP SETTINGS");
		panel_down.add(btnNewButton_1);
	}

	public static OptionsGui getInstance() {
		if(instance!=null)
		{
			//instance = new BolusGUI(bglevel);
		}
		else{
			instance = new OptionsGui();
		}
		return instance;
	}
}
