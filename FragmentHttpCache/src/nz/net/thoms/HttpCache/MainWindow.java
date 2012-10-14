package nz.net.thoms.HttpCache;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JList;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.ListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	JFrame frame;
	private JTextArea textArea;
	/**
	 * Create the application.
	 */
	public MainWindow(ListModel model) {
		initialize(model);
	}

	public void postMessage(String message) {
    	textArea.setText(HttpMain.window.textArea.getText() + message + "\n");	        	
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ListModel model) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JList list = new JList();
		list.setModel(model);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 0);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		frame.getContentPane().add(list, gbc_list);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HttpMain.cache.clear();
			}
		});
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 2;
		frame.getContentPane().add(btnClear, gbc_btnClear);
	}

}
