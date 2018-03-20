package View;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import Model.AnimatedLineChart;
import Model.Battery;
import Model.Constants;
import Model.DailyEvents;
import Model.EatingEvent;
import Model.InsulinReservoir;
import Model.MailUtil;
import Model.Patient;
import Model.PatientData;
import Model.SimulatorUtility;
import javafx.embed.swing.JFXPanel;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JProgressBar progressBar_Battery;
	public JProgressBar progressBar_Insulin;
	private Timer updateTimeTimer;
	private JLabel timeLbl;
	private long millis ;
	private AnimatedLineChart chart;
	private Date now ;
    private JTextArea messageLbl;
	
	Battery battery = new Battery();
	InsulinReservoir insulin = new InsulinReservoir();
	
	PatientData pdata= new PatientData();
	Patient p1 = new Patient();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
					frame.setTitle("Insulin Pump");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//syntheticaBlackMoon
	/**
	 * Create the frame.
	 */
	public GUI() {
		
		//
		createPatient();
		//
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 600);
		setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnRefill = new JMenu("REFILL");
		mnRefill.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		menuBar.add(mnRefill);
		
		JMenuItem mntmInsulin = new JMenuItem("INSULIN");
		mntmInsulin.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		mnRefill.add(mntmInsulin);
		JMenuItem mntmBattery = new JMenuItem("BATTERY");
		mntmBattery.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		mnRefill.add(mntmBattery);
		
		mntmInsulin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insulin.setInsulin(100);
				progressBar_Insulin.setValue(insulin.getAvailable());
			}
		});
		
		mntmBattery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				battery.setPower(100);
				progressBar_Battery.setValue(battery.getAvailable());
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setDefaultLookAndFeelDecorated(true);
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Panel.background"));
		contentPane.add(panel, BorderLayout.NORTH);

		chart = new AnimatedLineChart(false,false,GUI.this,battery,insulin,pdata);

		progressBar_Insulin = new JProgressBar();
		progressBar_Insulin.setStringPainted(true);
		progressBar_Insulin.setValue(insulin.getAvailable());
		progressBar_Insulin.setForeground(new Color(0, 128, 0));

		progressBar_Battery = new JProgressBar();
		progressBar_Battery.setStringPainted(true);
		progressBar_Battery.setBackground(new Color(250, 240, 240));
		progressBar_Battery.setForeground(new Color(30, 144, 255));
		progressBar_Battery.setValue(battery.getAvailable());

		JLabel lblInsulin = new JLabel("Insulin");
		lblInsulin.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblBattery = new JLabel("Battery");
		lblBattery.setFont(new Font("Tahoma", Font.PLAIN, 20));

		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
		millis= System.currentTimeMillis();
		now = new Date(millis);
		timeLbl = new JLabel(sdfTime.format(now));
		timeLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		// Create a timer.
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				millis = System.currentTimeMillis();
				now = new Date(millis);
				timeLbl.setText(sdfTime.format(now));
				checkBatteryAndInsulinLevels();
			}

		};
		updateTimeTimer = new Timer(1000, action);
		updateTimeTimer.setInitialDelay(1000);
		updateTimeTimer.start();
		
		JFXPanel panel_graph = chart.createPanel();
		//JPanel panel_graph = new JPanel();
		panel_graph.setBackground(new Color(255, 255, 255));
		//panel_graph.setSize(250, 130);

		JButton btnEmergency = new JButton("EMERGENCY");
		Color red= Color.decode("#ee2211");
		btnEmergency.setBackground(red);
		btnEmergency.setForeground(Color.white);
		btnEmergency.addActionListener(new ActionListener() {
			//send email
			public void actionPerformed(ActionEvent e) {
				 String[] recipients = new String[]{"jagruti.gunjal92@gmail.com"};
			        String[] bccRecipients = new String[]{"raka9256@gmail.com"};
			        String subject = "Hi this is test Mail";
			        String messageBody = "Test Mail for insulin pump";
			        new MailUtil().sendMail(recipients, bccRecipients, subject, messageBody);
			}
		});
		try {
			Image img = ImageIO.read(getClass().getResource("/images/emergency.png"));
			btnEmergency.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		
				messageLbl = new JTextArea("");
				messageLbl.setBackground(UIManager.getColor("Panel.background"));
				// messageLbl.setHorizontalAlignment(SwingConstants.CENTER);
				messageLbl.setFont(new Font("Serif", Font.PLAIN, 14));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(36)
							.addComponent(lblInsulin, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_graph, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(progressBar_Insulin, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
									.addGap(163)
									.addComponent(timeLbl)))))
					.addGap(27)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addComponent(progressBar_Battery, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
									.addComponent(messageLbl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnEmergency, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblBattery)
							.addGap(34))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(progressBar_Insulin, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addComponent(progressBar_Battery, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblBattery)
								.addComponent(lblInsulin)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(20)
							.addComponent(timeLbl)))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnEmergency, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
							.addGap(117)
							.addComponent(messageLbl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_graph, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.linkSize(SwingConstants.HORIZONTAL, new Component[] {progressBar_Insulin, progressBar_Battery});
		panel.setLayout(gl_panel);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(UIManager.getColor("Panel.background"));
		contentPane.add(buttonsPanel, BorderLayout.CENTER);

		JButton btnBolus = new JButton("        BOLUS       ");
		btnBolus.setBackground(new Color(220, 220, 220));
		try {
			Image img = ImageIO.read(getClass().getResource("/images/bolus.png"));
			btnBolus.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		btnBolus.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnBolus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BolusGUI.getInstance(chart.getActualGL(), GUI.this, chart,pdata,battery,insulin).setVisible(true);
			}
		});

		JButton btnOptions = new JButton("OPTIONS");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new OptionsGui().setVisible(true);
			}
		});
		btnOptions.setBackground(new Color(220, 220, 220));
		try {
			Image img = ImageIO.read(getClass().getResource("/images/settings.png"));
			btnOptions.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		btnOptions.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		
		
		
		///
		JPanel buttonsPanel2 = new JPanel();
		buttonsPanel2.setBackground(new Color(173, 216, 230));
		
		        JButton btnLowBtr = new JButton("Low Battery");
		        btnLowBtr.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				battery.setPower(19);
				progressBar_Battery.setValue(battery.getAvailable());
				
		        // checkBatteryAndInsulinLevels();
			}
		});
		        // JButton btnLowSimulation = new JButton("Tired Time");

		         JButton btnLowIns = new JButton("Low Insulin");
		         btnLowIns.setAlignmentY(6.0f);
		         btnLowIns.setAlignmentX(3.0f);
		         btnLowIns.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				insulin.setInsulin(19);
				progressBar_Insulin.setValue(insulin.getAvailable());
		       // progressBar_Battery.setValue(battery.getAvailable());
		         //checkBatteryAndInsulinLevels();
			}
		});
		         
		                 JLabel lblTestSimulator = new JLabel("Test Simulator");
		                 lblTestSimulator.setAlignmentY(Component.TOP_ALIGNMENT);
		                 lblTestSimulator.setFont(new Font("Tahoma", Font.ITALIC, 20));
		                 
		                
		GroupLayout gl_buttonsPanel = new GroupLayout(buttonsPanel);
		gl_buttonsPanel.setHorizontalGroup(
			gl_buttonsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonsPanel.createSequentialGroup()
					.addContainerGap(128, Short.MAX_VALUE)
					.addComponent(btnBolus, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
					.addGap(66)
					.addComponent(btnOptions, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addGap(126))
				.addComponent(buttonsPanel2, GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
		);
		gl_buttonsPanel.setVerticalGroup(
			gl_buttonsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonsPanel.createSequentialGroup()
					.addGroup(gl_buttonsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBolus, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOptions, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(buttonsPanel2, GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_buttonsPanel.linkSize(SwingConstants.VERTICAL, new Component[] {btnBolus, btnOptions});
		gl_buttonsPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {btnBolus, btnOptions});
		GroupLayout gl_buttonsPanel2 = new GroupLayout(buttonsPanel2);
		gl_buttonsPanel2.setHorizontalGroup(
			gl_buttonsPanel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonsPanel2.createSequentialGroup()
					.addGroup(gl_buttonsPanel2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_buttonsPanel2.createSequentialGroup()
							.addGap(63)
							.addGroup(gl_buttonsPanel2.createParallelGroup(Alignment.LEADING)
								))
						.addComponent(lblTestSimulator, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
					.addComponent(btnLowBtr, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
					.addGap(64)
					.addComponent(btnLowIns, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addGap(147))
		);
		gl_buttonsPanel2.setVerticalGroup(
			gl_buttonsPanel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonsPanel2.createSequentialGroup()
					.addGroup(gl_buttonsPanel2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_buttonsPanel2.createSequentialGroup()
							.addComponent(lblTestSimulator, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_buttonsPanel2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_buttonsPanel2.createSequentialGroup()
									.addGap(11))
								.addGroup(gl_buttonsPanel2.createSequentialGroup()
									.addGap(11))
								.addGroup(gl_buttonsPanel2.createSequentialGroup()
									.addGap(11))))
						.addGroup(gl_buttonsPanel2.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_buttonsPanel2.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnLowBtr, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLowIns, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		buttonsPanel2.setLayout(gl_buttonsPanel2);
		buttonsPanel.setLayout(gl_buttonsPanel);
	}

	private void setMessageFont(Font font, String insulinLowLevelMessage) {
		messageLbl.setForeground(Color.red);
		messageLbl.setFont(font);
		messageLbl.setText(insulinLowLevelMessage);
	}

	private void checkBatteryAndInsulinLevels() {
		if(battery.getAvailable()<20 && insulin.getAvailable()<20){
			if(!messageLbl.getText().contains(Constants.batteryLowLevelMessage+Constants.insulinLowLevelMessage)){
				setMessageFont(new Font(Font.SERIF, Font.PLAIN, 13),
						Constants.batteryLowLevelMessage + "\n" + Constants.insulinLowLevelMessage);
				progressBar_Battery.setForeground(Color.red);
				progressBar_Insulin.setForeground(Color.red);
			}
		} else if(battery.getAvailable()<20){
			if (!messageLbl.getText().equals(Constants.batteryLowLevelMessage)) {
				setMessageFont(new Font(Font.MONOSPACED, Font.BOLD, 16), Constants.batteryLowLevelMessage);
				progressBar_Battery.setForeground(Color.red);
			}
		} else if(insulin.getAvailable()<20){
			if(!messageLbl.getText().equals(Constants.batteryLowLevelMessage)) {
				setMessageFont(new Font(Font.SERIF, Font.PLAIN, 13), Constants.insulinLowLevelMessage);
				progressBar_Insulin.setForeground(Color.red);
			}
		} else{
			messageLbl.setText("");
			progressBar_Battery.setForeground(new Color(30, 144, 255));
			progressBar_Insulin.setForeground(new Color(0, 128, 0));
		}
	}
	
	
	//create patient data
	private void createPatient() { //create a patient
        p1.setName("Patient1");
    	p1.setNormal_bg_level(100);
    	p1.setBasal_prescribed_rate(0.7);
    	p1.setDaily_basal_prescribed(17);
    	p1.setDaily_bolus_prescribed(27);
    	p1.setPrescribed_glucagon(1);

    	pdata.savePatient(p1);
    	
    	DailyEvents e1 = new EatingEvent("Breakfast", 30,
    			SimulatorUtility.formatTimeInMinutes("9:00"),
    			p1.getNormal_bg_level());
    	DailyEvents e2 = new EatingEvent("Lunch", 10,
    			SimulatorUtility.formatTimeInMinutes("13:00"),
    			p1.getNormal_bg_level());
    	DailyEvents e3 = new EatingEvent("Dinner", 80,
    			SimulatorUtility.formatTimeInMinutes("20:00"),
    			p1.getNormal_bg_level());
    	pdata.addPatientEvent(e1, p1.getName());
    	pdata.addPatientEvent(e2, p1.getName());
    	pdata.addPatientEvent(e3, p1.getName());}
}

