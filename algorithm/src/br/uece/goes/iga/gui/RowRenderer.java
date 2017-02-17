package br.uece.goes.iga.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

class RowRenderer implements TableCellRenderer {

  public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
  	int [] solution;
  public void setSolution(int [] solution) {
	  this.solution = solution;
  }
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
        table, value, isSelected, hasFocus, row, column);
    ((JLabel) renderer).setOpaque(true);
    Color foreground, background;
    
      if (solution[row] == 1) {
        foreground = Color.black;
        background = new Color(0x49bf86);
      } else {
        foreground = Color.black;
        background = Color.white;
      }
    
    renderer.setForeground(foreground);
    renderer.setBackground(background);
 
    return renderer;
  }

}