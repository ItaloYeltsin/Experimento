package br.uece.goes.iga.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.uece.goes.iga.core.InteractiveEntity;

public class InteractionGUI extends JDialog implements InteractiveEntity{
	JPanel contentPanel = new JPanel();
	JTable content;
	JSlider slider;
	
	String [] description; 
	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	Object [][] data = null;
	RowRenderer renderer = new RowRenderer();
	private double solutionSHE;
 	InteractionGUI(){
		setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setContentPane(contentPanel);
		setTitle("Interactive Genetic Algorithm - Solution");
		contentPanel.setLayout(new BorderLayout());
		JPanel p = new JPanel();
		contentPanel.add(p, BorderLayout.SOUTH);
		p.setBorder(new EmptyBorder(5, 5, 5, 5));
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.9;
		c.fill = c.HORIZONTAL;
		manageSlider();
		p.add(slider, c);
		JButton evaluate = new JButton("Evaluate");
		c.gridx = 1;
		c.weightx = 0.1;
		c.fill = c.NONE;
		p.add(evaluate, c);
		
		evaluate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solutionSHE = slider.getValue()/(double)slider.getMaximum();
				setVisible(false);
			}
		});
 	}
	
	public void setRequirements(String [] description, int [] solution) {
		String [] headers = {"#ID", "Description"};
		
		data = new Object[description.length][2];
		for (int i = 0; i < description.length; i++) {
			data[i][1] = description[i];
			data[i][0] = i;
 		}
		renderer.setSolution(solution);
			
		JTable content = new JTable(data, headers);
		content.setEnabled(false);
		
		TableColumnAdjuster tca = new TableColumnAdjuster(content);
		tca.adjustColumns();	
		//content.setBorder(BorderFactory.createLineBorder(Color.black)); 
		
		
		content.setDefaultRenderer(Object.class, renderer);
		JScrollPane panel = new JScrollPane(content);
		//panel.setBorder();
		contentPanel.add(panel, BorderLayout.CENTER);
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		
	}
	
	public void manageSlider() {
		slider = new JSlider(SwingConstants.HORIZONTAL, 0, 1000, 500);
		
		//slider.setPaintTicks(true);
		slider.setMajorTickSpacing(250);
		slider.setMinorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		Hashtable intervals = new Hashtable();
		intervals.put(new Integer(0), new JLabel("Péssimo"));
		intervals.put(new Integer(250), new JLabel("Ruim"));
		intervals.put(new Integer(500), new JLabel("Indiferente"));
		intervals.put(new Integer(750), new JLabel("Bom"));
		intervals.put(new Integer(1000), new JLabel("Excelente"));
		slider.setLabelTable(intervals);		
	}

	@Override
	public double evaluate(int[] solution) {
		if(solution == null)
			try {
				throw new Exception("Solution cannot be null");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		setRequirements(description, solution);
		setVisible(true);
		return solutionSHE;
	}
	
}
