package split_text_file;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Frame extends javax.swing.JFrame {
	File selectedFile;

	public Frame() {
		init();
	}

	public void init()  {
		setTitle("split text file - developed by Mayyas Qasem");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(700, 350));
		pack();
		setResizable(false);
		// -------------------------------------------
		JTextField text = new JTextField(40);
		text.setEnabled(false);
		text.setFont(new Font("TimesRoman", Font.BOLD, 13));
		JLabel numLabel = new JLabel("                          # of rows : ");
		numLabel.setFont(new Font("TimesRoman", Font.BOLD, 13));
		JTextField num = new JTextField(15);
		num.setFont(new Font("TimesRoman", Font.BOLD, 13));
		JLabel label = new JLabel();
		label.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		// --------------------------------------------
		JButton b1 = new JButton("select file");
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String userDir = System.getProperty("user.home");
				JFileChooser jfc = new JFileChooser(userDir + "/Desktop");
				jfc.setFileFilter(new FileNameExtensionFilter("text file", "txt"));
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = jfc.getSelectedFile();
					text.setText(selectedFile.getAbsolutePath());
					label.setText("");

				}
			}
		});
		// --------------------------------------------
		JPanel p1 = new JPanel(new GridLayout(7, 1));
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT ));
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT ));
		JCheckBox c1 = new JCheckBox("keep the first line in all splitted files"); 
		c1.setSelected(true);
		JButton b2 = new JButton("start");
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<?,?> worker = new SwingWorker<Void, String>() {
				    @Override
				    protected Void doInBackground() throws Exception {
				    	label.setText("Splitting....");
				    	b2.setEnabled(false);
				    	c1.setEnabled(false);
				    	File wr ;
						FileWriter writer=null;;
						FileReader fr=null;
						BufferedReader br=null;
						try {
							int n=Integer.parseInt(num.getText());  
							if(n<1) throw new Exception();
							int c=0;
							int count =1;
						//-----------------------------------------------------------
						 fr = new FileReader(selectedFile);
						 br = new BufferedReader(fr);
						String line;
						String firstLine="";
						 wr = new File(selectedFile.getPath()+"_"+count+".txt");
						wr.createNewFile(); 
						 writer = new FileWriter(wr.getPath());
						 int fl=0;
						 
						while((line = br.readLine()) != null){	
							if(fl==0&&c1.isSelected())
							{
								 firstLine=line;
								 fl=1;
								 c--;
							}
							
							writer.write(line);	
						    c++;
						    if(c==n)
							    {
						        writer.close();
						        c=0;
						    	count++;
						        wr = new File(selectedFile.getPath()+"_"+count+".txt");
						        wr.createNewFile();
						        writer = new FileWriter(wr.getPath());
						        if(c1.isSelected())
						        {
						        	writer.write(firstLine);
						        	 writer.write(System.lineSeparator());
						        }
							    }
						    else writer.write(System.lineSeparator());	
						}
						writer.close();
						br.close();
						fr.close();
						//--------------------------
						//---------------------------
						Path path = Paths.get(selectedFile.getPath()+"_"+count+".txt");
						long bytes = Files.size(path);
						File file=null; 
						if(bytes==firstLine.getBytes().length+2||bytes==0)
						{
							File f = new File(selectedFile.getPath()+"_"+count+".txt");
							f.delete();
						}
						//-----------------------------------------------------------
						else {						
					    file = new File(selectedFile.getPath()+"_"+count+".txt");
					    FileChannel fileChannel = new FileOutputStream(file, true).getChannel();
					    fileChannel.truncate(fileChannel.size() - 2); //Removes last character
					    fileChannel.close();}
						
						
						}
						catch(Exception ex)
						{
							label.setText("Wrong!");
							ex.printStackTrace();
							
						}
						if (br != null) {
					        try {
					            br.close();
					        } catch (Exception exp) {
					           
					        }
					    }
						if (writer != null) {
					        try {
					        	writer.close();
					        } catch (Exception exp) {
					           
					        }
					    }
					
				    	return null;
				    }

				    @Override
				    protected void done() {
				    	if(!label.getText().contains("Wrong"))
				    	label.setText("Done!");
				    	b2.setEnabled(true);
				    	c1.setEnabled(true);
				    	repaint();
				    }
				};
				worker.execute(); //Execute the worker
		}
		});
		// ----------------------------------------------
		
		b2.setPreferredSize(new Dimension(300, 30));
		Border padding = BorderFactory.createEmptyBorder(30, 10, 10, 10);
		p1.setBorder(padding);
		// ---------------------------------------------
		p2.add(b1);
		p2.add(text);
		p1.add(p2);
		
		p3.add(numLabel);
		p3.add(num);
		p1.add(p3);
		
		p6.add(new JLabel("                      "));
		p6.add(c1);
		p1.add(p6);
		
		p4.add(b2);
		p1.add(p4);

		p5.add(label);
		p1.add(p5);
		

		this.add(p1);
		setVisible(true);
		setLocationRelativeTo(null);
	}

}