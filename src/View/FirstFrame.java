package View;
import java.awt.GridLayout;

import javax.swing.*;

public class FirstFrame {
	
	public FirstFrame(){
	JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("GridLayout Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(5, 4));
    frame.add(new JLabel("Name : "));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Surname : "));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Height : "));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Weight : "));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Age :"));
    frame.add(new JTextField(""));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Normal BG : "));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Basal rate  : "));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Basal prescribed :"));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Bolus prescribed :"));
    frame.add(new JTextField(""));
    frame.add(new JLabel("Glucagon prescribed :"));
    frame.add(new JTextField(""));
       
    
    
    frame.setSize(600,200);
    frame.setVisible(true);
	}

	public static void main(String []args){
		new FirstFrame();
	}
}
