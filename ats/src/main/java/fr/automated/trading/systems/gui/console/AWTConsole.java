package fr.automated.trading.systems.gui.console;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class AWTConsole extends WindowAdapter implements WindowListener, ActionListener, Runnable
{
	private final Frame frame;
	private final TextArea textArea;
	private final Thread reader;
	private final Thread reader2;
	private boolean quit;
					
	private final PipedInputStream pin=new PipedInputStream(); 
	private final PipedInputStream pin2=new PipedInputStream();
	
	public AWTConsole()
	{
		// create all components and add them
		frame=new Frame("Java Console");
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize=new Dimension(screenSize.width/2, screenSize.height/2);
		int x= frameSize.width/2;
		int y= frameSize.height/2;
		frame.setBounds(x,y,frameSize.width,frameSize.height);
		
		textArea=new TextArea();
		textArea.setEditable(false);
		Button button=new Button("clear");
		
		Panel panel=new Panel();
		panel.setLayout(new BorderLayout());
		panel.add(textArea,BorderLayout.CENTER);
		panel.add(button,BorderLayout.SOUTH);
		frame.add(panel);
		
		frame.setVisible(true);		
		
		frame.addWindowListener(this);		
		button.addActionListener(this);
		
		try
		{
			PipedOutputStream pout=new PipedOutputStream(this.pin);
			System.setOut(new PrintStream(pout,true)); 
		} 
		catch (IOException | SecurityException io)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n"+io.getMessage());
		}

        try
		{
			PipedOutputStream pout2=new PipedOutputStream(this.pin2);
			System.setErr(new PrintStream(pout2,true));
		} 
		catch (IOException | SecurityException io)
		{
			textArea.append("Couldn't redirect STDERR to this console\n"+io.getMessage());
		}

		quit=false; // signals the Threads that they should exit
				
		// Starting two seperate threads to read from the PipedInputStreams				
		//
		reader=new Thread(this);
		reader.setDaemon(true);	
		reader.start();	
		//
		reader2=new Thread(this);	
		reader2.setDaemon(true);	
		reader2.start();

	}
	
	public synchronized void windowClosed(WindowEvent evt)
	{
		quit=true;
		this.notifyAll(); // stop all threads
		try { reader.join(1000);pin.close();   } catch (Exception e){ e.printStackTrace();}
		try { reader2.join(1000);pin2.close(); } catch (Exception e){ e.printStackTrace();}
		System.exit(0);
	}		
		
	public synchronized void windowClosing(WindowEvent evt)
	{
		frame.setVisible(false); // default behaviour of JFrame	
		frame.dispose();
	}
	
	public synchronized void actionPerformed(ActionEvent evt)
	{
		textArea.setText("");
	}

	public synchronized void run()
	{
		try
		{			
			while (Thread.currentThread()==reader)
			{
				try { this.wait(100);}catch(InterruptedException ie) { ie.printStackTrace();}
				if (pin.available()!=0)
				{
					String input=this.readLine(pin);
					textArea.append(input);
				}
				if (quit) return;
			}
		
			while (Thread.currentThread()==reader2)
			{
				try { this.wait(100);}catch(InterruptedException ie) { ie.printStackTrace();}
				if (pin2.available()!=0)
				{
					String input=this.readLine(pin2);
					textArea.append(input);
				}
				if (quit) return;
			}			
		} catch (Exception e)
		{
			textArea.append("\nConsole reports an Internal error.");
			textArea.append("The error is: "+e);			
		}

	}
	
	public synchronized String readLine(PipedInputStream in) throws IOException
	{
		String input="";
		do
		{
			int available=in.available();
			if (available==0) break;
			byte b[]=new byte[available];
			in.read(b);
			input=input+new String(b,0,b.length);														
		}while( !input.endsWith("\n") &&  !input.endsWith("\r\n") && !quit);
		return input;
	}	

}