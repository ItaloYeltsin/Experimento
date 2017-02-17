package br.uece.goes.iga.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import br.uece.goes.iga.core.InteractiveEntity;

public class InstanceFileDialogWindow extends JDialog{
	/*
	 * Instance File 
	 */
	File instanceFile;
	/*
	 * Description File
	 */
	File descriptionFile;
	
	
	public File getInstance() {
		return instanceFile;
	}

	public void setInstance(File instance) {
		this.instanceFile = instance;
	}

	public File getDescription() {
		return descriptionFile;
	}

	public void setDescription(File description) {
		this.descriptionFile = description;
	}

	public InstanceFileDialogWindow() {
		setSize(500, 250);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setTitle("Interactive Genetic Algorithm - Instance Dialog");
		setResizable(false);
		setModalityType(DEFAULT_MODALITY_TYPE);
		
		//Panels
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;
		c.gridy = 0;
		//Instance Panel
		JPanel instance = new JPanel();
		add(instance, c);
		instance.setBorder(new TitledBorder("Instance File"));
		instance.setLayout(new GridBagLayout());
		final JLabel instanceF = new JLabel();
		JButton instanceB = new JButton("Choose a File...");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 4;
		c.gridy = 0;
		instance.add(instanceF, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 0.1;
		c.gridy = 0;
		instance.add(instanceB, c);
		//Description Panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;
		c.gridy = 1;
		JPanel description = new JPanel();
		add(description, c);
		description.setBorder(new TitledBorder("Description File"));
		description.setLayout(new GridBagLayout());
		final JLabel descriptionF = new JLabel();
		JButton descriptionB = new JButton("Choose a File...");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 4;
		c.gridy = 0;
		description.add(descriptionF, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 0.1;
		c.gridy = 0;
		description.add(descriptionB, c);
		//Apply Button
		JButton apply = new JButton("Apply");
		c.gridx = 0;
		c.fill = c.NONE;
		c.gridy = 2;
		c.anchor = c.LAST_LINE_END;
		add(apply, c);

		//Cancel Button
		JButton cancel = new JButton("Cancel");
		c.gridx = 0;
		c.fill = c.NONE;
		c.gridy = 2;
		c.anchor = c.LAST_LINE_START;
		add(cancel, c);
		
		/**
		 * Buttons Actions
		 */
		
		//instance Button
		instanceB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser ch = new JFileChooser();
				ch.setCurrentDirectory(new File("."));
				int result = ch.showOpenDialog(instanceF);
				if(result == ch.APPROVE_OPTION) {
					instanceFile = ch.getSelectedFile();
					instanceF.setText(instanceFile.getAbsolutePath());
				}
			}
		});
		
		//instance Button
				descriptionB.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser ch = new JFileChooser();
						ch.setCurrentDirectory(new File("."));
						int result = ch.showOpenDialog(descriptionF);
						if(result == ch.APPROVE_OPTION) {
							descriptionFile = ch.getSelectedFile();
							descriptionF.setText(descriptionFile.getAbsolutePath());
						}
					}
				});
				
		//apply Button
				apply.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
		//cancel Button
				cancel.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(ABORT);
					}
				});
	}
	
	String [] getDescriptions() throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(descriptionFile));
		ArrayList<String> array = new ArrayList<String>();
		while(reader.ready()) 
			array.add(reader.readLine());
		String [] sArray = new String[array.size()];
		int count = 0;
		for (String string : array) {
			sArray[count++] = string;
		}
		return sArray;
	}
		
}
