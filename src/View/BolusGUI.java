package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Model.AnimatedLineChart;
import Model.Battery;
import Model.BolusCalculation;
import Model.InsulinReservoir;
import Model.PatientData;

public class BolusGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField carbsTxt;
	private JTextField bgTxt;
	private static volatile BolusGUI instance;
/*	private InsulinReservoir insulin;
	private Battery battery;*/
	private int insulinUnits;

	/**
	 * Create the frame.
	 * @param pdata 
	 * @param insulin 
	 * @param battery 
	 */
	private BolusGUI(int bgLevel, JFrame gui, AnimatedLineChart chart, PatientData pdata, Battery battery, InsulinReservoir insulin) {
		
		setBounds(100, 100, 370, 246);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);

		carbsTxt = new JTextField();
		carbsTxt.setHorizontalAlignment(SwingConstants.CENTER);
		carbsTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		carbsTxt.setColumns(10);

		bgTxt = new JTextField();
		bgTxt.setHorizontalAlignment(SwingConstants.CENTER);
		bgTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bgTxt.setEditable(false);
		bgTxt.setText("" + bgLevel);
		bgTxt.setColumns(10);

		JLabel lblCarbs = new JLabel("Carbs");
		lblCarbs.setForeground(new Color(102, 153, 153));
		lblCarbs.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblBg = new JLabel("Bg");
		lblBg.setForeground(new Color(51, 153, 153));
		lblBg.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnCalculate = new JButton("CALCULATE");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BolusCalculation bc = new BolusCalculation();

				if (validateCarbs(carbsTxt.getText()) && !carbsTxt.getText().equals("")) {
					if (!carbsTxt.getText().equals("0")) {
						insulinUnits = bc.calculateBolusCarbs(Integer.parseInt(carbsTxt.getText()),
								Integer.parseInt(bgTxt.getText()),pdata.getPatient("Patient1"));
						textInsulin.setText("" + insulinUnits);
					} else if (carbsTxt.getText().equals("0")) {
						insulinUnits = bc.calculateBolus(Integer.parseInt(bgTxt.getText()),pdata.getPatient("Patient1"));
						textInsulin.setText("" + insulinUnits);
					}
				} else {
					System.out.println("Write a number");
				}
			}
		});

		JLabel lblGram = new JLabel("gram");
		lblGram.setFont(new Font("Tahoma", Font.PLAIN, 10));

		JLabel lblMgdl = new JLabel("mg/dL");
		lblMgdl.setFont(new Font("Tahoma", Font.PLAIN, 10));

		textInsulin = new JTextField();
		textInsulin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textInsulin.setEditable(false);

		JButton btnOk = new JButton("NEXT");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(insulinUnits > insulin.getAvailable()){
					JOptionPane.showMessageDialog(BolusGUI.this, "Please refill the insulin reservoir!", "Error amount",JOptionPane.ERROR_MESSAGE);
				} else {
					if (!textInsulin.getText().equals("")) {
						battery.getPowerAmout(1);    //level of battery is reduced by 1 each transaction
						insulin.getInsulinAmount(Integer.parseInt(textInsulin.getText()));
						((GUI) gui).progressBar_Battery.setValue(battery.getAvailable());
						((GUI) gui).progressBar_Insulin.setValue(insulin.getAvailable());
						chart.setInsulinInjected(true);
						chart.setInjectedTime(System.currentTimeMillis());
						dispose();
					}
				}
			}
		});

		JLabel lblInsulin = new JLabel("Insulin");
		lblInsulin.setForeground(new Color(102, 153, 153));
		lblInsulin.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblUnit = new JLabel("unit");
		lblUnit.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		textInsulin = new JTextField();
		textInsulin.setEditable(false);
		textInsulin.setHorizontalAlignment(SwingConstants.CENTER);
		textInsulin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textInsulin.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblBg)
								.addComponent(lblCarbs))
							.addGap(10)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(carbsTxt, 0, 0, Short.MAX_VALUE)
								.addComponent(bgTxt, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
							.addGap(4)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblGram)
								.addComponent(lblMgdl))
							.addGap(12))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnCalculate, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(30)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblInsulin)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textInsulin, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(lblUnit)))
					.addGap(34))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addContainerGap()
									.addComponent(carbsTxt, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(21)
									.addComponent(lblCarbs)))
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(18)
									.addComponent(bgTxt, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(8)
									.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblInsulin)
										.addComponent(textInsulin, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(28)
									.addComponent(lblBg))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(23)
							.addComponent(lblGram)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(33)
									.addComponent(lblUnit))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(42)
									.addComponent(lblMgdl)))))
					.addPreferredGap(ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnOk, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnCalculate, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
	}

	public static BolusGUI getInstance(int bglevel, JFrame gui, AnimatedLineChart chart, PatientData pdata, Battery battery, InsulinReservoir insulin) {
		//TODO: why should be this a singleton? - fix me!
		if (instance != null) {
			 instance.dispose();
		}
		return instance = new BolusGUI(bglevel, gui, chart,pdata,battery,insulin);
	}

	private static Pattern datepattern = Pattern.compile(("^[0-9]*$"));
	private JTextField textInsulin;


	boolean validateCarbs(String carbs) {
		Matcher mtch = datepattern.matcher(carbs);
		if (mtch.matches()) {
			return true;
		}
		return false;
	}
}
