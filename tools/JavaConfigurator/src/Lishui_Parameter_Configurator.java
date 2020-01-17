/*
This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software Foundation,
Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

/**
 *
 * @author stancecoke
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.ListModel;

public class Lishui_Parameter_Configurator extends javax.swing.JFrame {

    /**
     * Creates new form TSDZ2_Configurator
     */
    
    private File experimentalSettingsDir;
    private File lastSettingsFile = null;
    
    DefaultListModel provenSettingsFilesModel = new DefaultListModel();
    DefaultListModel experimentalSettingsFilesModel = new DefaultListModel();
    JList experimentalSettingsList = new JList(experimentalSettingsFilesModel);
    
    	public class FileContainer {

		public FileContainer(File file) {
			this.file = file;
		}
		public File file;

		@Override
		public String toString() {
			return file.getName();
		}
	}

    
    
 
public void loadSettings(File f) throws IOException {
    
     		BufferedReader in = new BufferedReader(new FileReader(f));
		Parameter1.setText(in.readLine());
                Parameter3.setText(in.readLine());
                RB_JLCD.setSelected(Boolean.parseBoolean(in.readLine()));
                RB_KM5S.setSelected(Boolean.parseBoolean(in.readLine()));
                RB_KUNTENG.setSelected(Boolean.parseBoolean(in.readLine()));
                RB_BAFANG.setSelected(Boolean.parseBoolean(in.readLine()));
                RB_DEBUG.setSelected(Boolean.parseBoolean(in.readLine()));

		in.close();
	}   

public void AddListItem(File newFile) {
        
        experimentalSettingsFilesModel.add(0, new FileContainer(newFile));
    
       // ListModel<String> Liste = expSet.getModel();
        expSet.repaint();
        JOptionPane.showMessageDialog(null,experimentalSettingsFilesModel.toString(),"Titel", JOptionPane.PLAIN_MESSAGE);
}
    
    public Lishui_Parameter_Configurator() {
        initComponents();

        // update lists
        
                        experimentalSettingsDir = new File(Paths.get(".").toAbsolutePath().normalize().toString());
		while (!Arrays.asList(experimentalSettingsDir.list()).contains("experimental settings")) {
			experimentalSettingsDir = experimentalSettingsDir.getParentFile();
		}
		File provenSettingsDir = new File(experimentalSettingsDir.getAbsolutePath() + File.separator + "proven settings");
		experimentalSettingsDir = new File(experimentalSettingsDir.getAbsolutePath() + File.separator + "experimental settings");



		for (File file : provenSettingsDir.listFiles()) {
			provenSettingsFilesModel.addElement(new Lishui_Parameter_Configurator.FileContainer(file));

			if (lastSettingsFile == null) {
				lastSettingsFile = file;
			} else {
				if(file.lastModified()>lastSettingsFile.lastModified()){
					lastSettingsFile = file;
				}
			}
		}
 		

                for (File file : experimentalSettingsDir.listFiles()) {
            experimentalSettingsFilesModel.addElement(new Lishui_Parameter_Configurator.FileContainer(file));
	}
        	experimentalSettingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		experimentalSettingsList.setLayoutOrientation(JList.VERTICAL);
		experimentalSettingsList.setVisibleRowCount(-1); 
                
                expSet.setModel(experimentalSettingsFilesModel);
        
		JList provenSettingsList = new JList(provenSettingsFilesModel);
		provenSettingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		provenSettingsList.setLayoutOrientation(JList.VERTICAL);
		provenSettingsList.setVisibleRowCount(-1);
        
        provSet.setModel(provenSettingsFilesModel);
        jScrollPane2.setViewportView(provSet);
        

        expSet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
                            	try {
                                int selectedIndex = expSet.getSelectedIndex();
                                experimentalSettingsList.setSelectedIndex(selectedIndex);
					loadSettings(((FileContainer) experimentalSettingsList.getSelectedValue()).file);
					experimentalSettingsList.clearSelection();
				} catch (IOException ex) {
					Logger.getLogger(Lishui_Parameter_Configurator.class.getName()).log(Level.SEVERE, null, ex);
				}
				experimentalSettingsList.clearSelection();
                                
				//updateDependiencies(false);
			}
		});
        
         provSet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
                            	try {
                                int selectedIndex = provSet.getSelectedIndex();
                                provenSettingsList.setSelectedIndex(selectedIndex);
					loadSettings(((FileContainer) provenSettingsList.getSelectedValue()).file);
					provenSettingsList.clearSelection();
				} catch (IOException ex) {
					Logger.getLogger(Lishui_Parameter_Configurator.class.getName()).log(Level.SEVERE, null, ex);
				}
				provenSettingsList.clearSelection();
				//updateDependiencies(false);
			}
		});
         
         
         jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				{

					int n = JOptionPane.showConfirmDialog(
							null,
							"If you run this function with a brand new controller, the original firmware will be erased. This can't be undone. Are you sure?",
							"",
							JOptionPane.YES_NO_OPTION);

					if (n == JOptionPane.YES_OPTION) {
						try {
							Process process = Runtime.getRuntime().exec("cmd /c start WriteOptionBytes");
						} catch (IOException e1) {
							Parameter1.setText("Error");
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "Goodbye");
					}

					// Saving code here
				}
			}
		});
         
                 
         
          jButton1.addActionListener(new ActionListener() {
          
          public void actionPerformed(ActionEvent arg0) {
          				PrintWriter iWriter = null;
                                PrintWriter pWriter = null;
				try {
					//FileWriter fw = new FileWriter("settings.ini");
					//BufferedWriter bw = new BufferedWriter(fw);
                                        
                                        
					File newFile = new File(experimentalSettingsDir + File.separator + new SimpleDateFormat("yyyyMMdd-HHmmssz").format(new Date()) + ".ini");
					//TSDZ2_Configurator ConfiguratorObject = new TSDZ2_Configurator();
                                        //ConfiguratorObject.AddListItem(newFile);
                                        experimentalSettingsFilesModel.add(0, new FileContainer(newFile)); //hier wird nur die neue Datei in die Liste geschrieben...

					iWriter = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("config.h")));
					pWriter.println("/*\r\n"
							+ " * config.h\r\n"
							+ " *\r\n"
							+ " *  Automatically created by OSEC Parameter Configurator\r\n"
							+ " *  Author: stancecoke\r\n"
							+ " */\r\n"
							+ "\r\n"
							+ "#ifndef CONFIG_H_\r\n"
							+ "#define CONFIG_H_\r\n"
                                                        + "#include \"stdint.h\"\r\n"
                                                        + "#define DISPLAY_TYPE_KINGMETER_618U (1<<4)                  // King-Meter 618U protocol (KM5s, EBS-LCD2, J-LCD, SW-LCD)\r\n"
                                                        + "#define DISPLAY_TYPE_KINGMETER_901U (1<<8)                  // King-Meter 901U protocol (KM5s)\r\n"
                                                        + "#define DISPLAY_TYPE_KINGMETER      (DISPLAY_TYPE_KINGMETER_618U|DISPLAY_TYPE_KINGMETER_901U)\r\n"
                                                        + "#define DISPLAY_TYPE_BAFANG (1<<2)							// For 'Blaupunkt' Display of Prophete Entdecker\r\n"
                                                        + "#define DISPLAY_TYPE_KUNTENG (1<<1)							// For ASCII-Output in Debug mode\r\n"
                                                        + "#define DISPLAY_TYPE_DEBUG (1<<0)							// For ASCII-Output in Debug mode);\r\n"
                                        );
                                        
                                        
                                        String text_to_save = "#define NEXT_PARAMETER " + Parameter3.getText();
				
                                        iWriter.println(Parameter1.getText());
					pWriter.println(text_to_save); 
                                        
                                        text_to_save = "#define NUMBER_OF_PAS_MAGS " + Parameter3.getText();
				
                                        iWriter.println(Parameter3.getText());
					pWriter.println(text_to_save); 
                                        
                                        if (RB_JLCD.isSelected()) {
						text_to_save = "#define DISPLAY_TYPE DISPLAY_TYPE_KINGMETER_618U \\J-LCD";
						pWriter.println(text_to_save);
					}
					iWriter.println(RB_JLCD.isSelected());
                                        
                                        if (RB_KM5S.isSelected()) {
						text_to_save = "#define DISPLAY_TYPE DISPLAY_TYPE_KINGMETER_901U \\KM5S";
						pWriter.println(text_to_save);
					}
					iWriter.println(RB_KM5S.isSelected());
                                        
                                        if (RB_KUNTENG.isSelected()) {
						text_to_save = "#define DISPLAY_TYPE DISPLAY_TYPE_KUNTENG \\Kunteng LCD3/5 etc.";
						pWriter.println(text_to_save);
					}
					iWriter.println(RB_KUNTENG.isSelected());                                        

                                        if (RB_BAFANG.isSelected()) {
						text_to_save = "#define DISPLAY_TYPE DISPLAY_TYPE_BAFANG \\Bafang Displays, including 'Blaupunkt' ";
						pWriter.println(text_to_save);
					}
					iWriter.println(RB_BAFANG.isSelected());
                                        
                                        if (RB_DEBUG.isSelected()) {
						text_to_save = "#define DISPLAY_TYPE DISPLAY_TYPE_DEBUG \\ASCII Printout for debugging";
						pWriter.println(text_to_save);
					}
					iWriter.println(RB_DEBUG.isSelected());                                        

                                        
                                        pWriter.println("\r\n#endif /* CONFIG_H_ */");

					iWriter.close();
 				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					if (pWriter != null) {
						pWriter.flush();
						pWriter.close();

					}
				}  
                                try {
					Process process = Runtime.getRuntime().exec("cmd /c start Start_Compiling");
				} catch (IOException e1) {
					Parameter1.setText("Error");
					e1.printStackTrace();
				}
          
          }
          
          
          });
                  
         		if (lastSettingsFile != null) {
			try {
				loadSettings(lastSettingsFile);
			} catch (Exception ex) {

			}
			provenSettingsList.clearSelection();
			experimentalSettingsList.clearSelection();
			//updateDependiencies(false);
		}
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BG_DISPLAYS = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        TAB1 = new javax.swing.JPanel();
        Parameter1 = new javax.swing.JTextField();
        Label_Parameter1 = new javax.swing.JLabel();
        RB_JLCD = new javax.swing.JRadioButton();
        RB_KM5S = new javax.swing.JRadioButton();
        RB_BAFANG = new javax.swing.JRadioButton();
        RB_KUNTENG = new javax.swing.JRadioButton();
        RB_DEBUG = new javax.swing.JRadioButton();
        TAB2 = new javax.swing.JPanel();
        Parameter3 = new javax.swing.JTextField();
        Label_Param3 = new javax.swing.JLabel();
        label1 = new java.awt.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        expSet = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        provSet = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Parameter1.setText("100");

        Label_Parameter1.setLabelFor(Parameter1);
        Label_Parameter1.setText("Parameter 1");

        BG_DISPLAYS.add(RB_JLCD);
        RB_JLCD.setText("J-LCD");
        RB_JLCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RB_JLCDActionPerformed(evt);
            }
        });

        BG_DISPLAYS.add(RB_KM5S);
        RB_KM5S.setText("KM5S");

        BG_DISPLAYS.add(RB_BAFANG);
        RB_BAFANG.setText("Bafang");

        BG_DISPLAYS.add(RB_KUNTENG);
        RB_KUNTENG.setText("Kunteng");

        BG_DISPLAYS.add(RB_DEBUG);
        RB_DEBUG.setText("Debug");

        javax.swing.GroupLayout TAB1Layout = new javax.swing.GroupLayout(TAB1);
        TAB1.setLayout(TAB1Layout);
        TAB1Layout.setHorizontalGroup(
            TAB1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TAB1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(TAB1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RB_DEBUG)
                    .addComponent(RB_KUNTENG)
                    .addComponent(RB_BAFANG)
                    .addComponent(RB_KM5S)
                    .addComponent(RB_JLCD)
                    .addGroup(TAB1Layout.createSequentialGroup()
                        .addComponent(Label_Parameter1)
                        .addGap(41, 41, 41)
                        .addComponent(Parameter1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(657, Short.MAX_VALUE))
        );
        TAB1Layout.setVerticalGroup(
            TAB1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TAB1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(TAB1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Parameter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label_Parameter1))
                .addGap(38, 38, 38)
                .addComponent(RB_JLCD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RB_KM5S)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RB_BAFANG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RB_KUNTENG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RB_DEBUG)
                .addContainerGap(175, Short.MAX_VALUE))
        );

        RB_JLCD.getAccessibleContext().setAccessibleName("RB_J-LCD");

        jTabbedPane1.addTab("ParameterTab 1", TAB1);

        Parameter3.setText("357");

        Label_Param3.setText("Parameter 3");

        javax.swing.GroupLayout TAB2Layout = new javax.swing.GroupLayout(TAB2);
        TAB2.setLayout(TAB2Layout);
        TAB2Layout.setHorizontalGroup(
            TAB2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TAB2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(Label_Param3)
                .addGap(37, 37, 37)
                .addComponent(Parameter3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(663, Short.MAX_VALUE))
        );
        TAB2Layout.setVerticalGroup(
            TAB2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TAB2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(TAB2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Parameter3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label_Param3))
                .addContainerGap(319, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("ParameterTab 2", TAB2);

        label1.setFont(new java.awt.Font("Ebrima", 0, 24)); // NOI18N
        label1.setText("E-Bike Parameter Configurator");

        expSet.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(expSet);

        jLabel1.setText("Experimental Settings");

        provSet.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(provSet);

        jLabel2.setText("Proven Settings");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("Compile & Flash");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setText("Unlock controller");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(3, 3, 3)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(84, 84, 84))))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("MotorConfiguration");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void RB_JLCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RB_JLCDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RB_JLCDActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lishui_Parameter_Configurator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lishui_Parameter_Configurator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lishui_Parameter_Configurator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lishui_Parameter_Configurator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Lishui_Parameter_Configurator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BG_DISPLAYS;
    private javax.swing.JLabel Label_Param3;
    private javax.swing.JLabel Label_Parameter1;
    private javax.swing.JTextField Parameter1;
    private javax.swing.JTextField Parameter3;
    private javax.swing.JRadioButton RB_BAFANG;
    private javax.swing.JRadioButton RB_DEBUG;
    private javax.swing.JRadioButton RB_JLCD;
    private javax.swing.JRadioButton RB_KM5S;
    private javax.swing.JRadioButton RB_KUNTENG;
    private javax.swing.JPanel TAB1;
    private javax.swing.JPanel TAB2;
    private javax.swing.JList<String> expSet;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private java.awt.Label label1;
    private javax.swing.JList<String> provSet;
    // End of variables declaration//GEN-END:variables
}
