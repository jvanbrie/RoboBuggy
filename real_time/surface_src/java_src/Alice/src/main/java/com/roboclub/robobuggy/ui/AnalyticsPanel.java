package com.roboclub.robobuggy.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class AnalyticsPanel extends JPanel {
	private static final long serialVersionUID = 7017667286491619492L;

	private DataPanel dataPanel;
	private GraphPanel graphPanel;
	
	public AnalyticsPanel() {
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.DARK_GRAY);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		dataPanel = new DataPanel();
		graphPanel = new GraphPanel();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		this.add(dataPanel, gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0;
		this.add(graphPanel, gbc);
	    System.out.println("H");
	}
	
	public String valuesFromData()
	{
	  return dataPanel.getValues();	
	}
	
}
