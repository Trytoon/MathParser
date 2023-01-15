package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JList;
import java.awt.Font;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class Menu {

	private JFrame frmMathApplication;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frmMathApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		String ressourcesFolder = System.getProperty("user.dir");
		ressourcesFolder += "\\ressources";
		String iconsFolder = ressourcesFolder += "\\icons";
		
		frmMathApplication = new JFrame();
		frmMathApplication.setTitle("Math Application");
		
		
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        
        
		frmMathApplication.setBounds(0, 0, size.width - 50, size.height - 50);
		frmMathApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frmMathApplication.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		frmMathApplication.setJMenuBar(menuBar);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		JMenuItem saveButton = new JMenuItem("Save");
		saveButton.setIcon(new ImageIcon(iconsFolder + "\\saveButton.png"));
		file.add(saveButton);
		
		JMenuItem loadButton = new JMenuItem("Load");
		loadButton.setIcon(new ImageIcon(iconsFolder + "\\loadButton.png"));
		file.add(loadButton);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frmMathApplication.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		JPanel panel_1 = new JPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_1, panel);
		
		JLabel lblNewLabel = new JLabel("                       ");
		panel_1.add(lblNewLabel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel funcLabel = new JLabel("Enter function:");
		funcLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		GridBagConstraints gbc_funcLabel = new GridBagConstraints();
		gbc_funcLabel.insets = new Insets(0, 0, 0, 5);
		gbc_funcLabel.anchor = GridBagConstraints.EAST;
		gbc_funcLabel.gridx = 0;
		gbc_funcLabel.gridy = 7;
		panel.add(funcLabel, gbc_funcLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 23));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 7;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		frmMathApplication.getContentPane().add(splitPane, gbc_splitPane);
	}
}
