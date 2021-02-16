package Haupt;
import java.io.*;
import SFV.*;

//import javax.swing.event.MenuEvent;
//import javax.swing.event.MenuListener;

//import org.eclipse.swt.*;
//import org.eclipse.swt.custom.*;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.SelectionListener;
//import org.eclipse.swt.graphics.*;
//import org.eclipse.swt.layout.*;
//import org.eclipse.swt.widgets.*;


/*
 * Created on 09.12.2004
 */
/**
 * @author 3dfx
 */

public final class Main {
	private static void echo(String str) { System.out.println(str); }
	public static final String VERSION = "SFVoodoo v1.1.1b";
	public static final String TITLE = "+========================+\n|    " + VERSION + "    |\n+========================+\n";
	public static final String ER_CRC = "00000000";
	public static final int BUFFER = 1024;
	
//	private static Display display = new Display ();	
/*	public static void main (String [] args) {
		Shell shell = new Shell (display);
		FillLayout fillLayout = new FillLayout ();
		fillLayout.type = SWT.VERTICAL;
		shell.setLayout (fillLayout);
		
		shell.setText(VERSION);
//		Image icon = new Image()
//		shell.setImage()
		
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem menuFileItem = new MenuItem(menuBar, SWT.CASCADE);
		menuFileItem.setText("&File");
		Menu menuFileMenu = new Menu(menuFileItem);
		menuFileItem.setMenu(menuFileMenu);
		shell.setMenuBar(menuBar);

		MenuItem menuExitItem = new MenuItem(menuFileMenu, SWT.PUSH);
		menuExitItem.setText("E&xit");
		menuExitItem.addSelectionListener(
		        new SelectionListener() {
		            public void widgetDefaultSelected(SelectionEvent event) {}
                    public void widgetSelected(SelectionEvent event) {
                        System.exit(0);
                    }
		        }
		);
		
		MenuItem menuCheckItem = new MenuItem(menuBar, SWT.CASCADE);
		menuCheckItem.setText("&Check");
		Menu menuCheckMenu = new Menu(menuCheckItem);
		menuCheckItem.setMenu(menuCheckMenu);
		
		MenuItem menuSfvItem = new MenuItem(menuCheckMenu, SWT.PUSH);
		menuSfvItem.setText("&SFV...");
//		open.setAccelerator(SWT.CONTROL | 'O');
		menuSfvItem.addSelectionListener(
		        new SelectionListener() {
		            public void widgetDefaultSelected(SelectionEvent event) {}
                    public void widgetSelected(SelectionEvent event) {
//                        System.out.println(event.getSource());
                		Shell shell = new Shell (display);
                		
                        FileDialog dialog = new FileDialog(shell.getShell());
                        dialog.setFilterExtensions(new String[] {"*.sfv"});
                        String sSFV = dialog.open();
//                        System.out.println(sSFV);
                        
                        launch(new String[] {sSFV});
                    }
		        }
		);
		
		MenuItem menuFilesItem = new MenuItem(menuCheckMenu, SWT.PUSH);
		menuFilesItem.setText("&Files...");
		
		Text tOut = new Text (shell, SWT.BORDER);
		tOut.setText ("");
		
//		tOut.setSize (tOut.computeSize (600, 300));
		
//		Group group1 = new Group (shell, SWT.NONE);
//		group1.setText ("group1");
//		group1.setSize(shell.computeSize(300, 200));


		CoolBar coolbar0 = new CoolBar (shell, SWT.NONE);
		ToolBar coolToolBar = new ToolBar (coolbar0, SWT.FLAT);
		ToolItem coolToolItem = new ToolItem (coolToolBar, SWT.NONE);
		coolToolItem.setText ("Item 1");
		coolToolItem = new ToolItem (coolToolBar, SWT.NONE);
		coolToolItem.setText ("Item 2");
		CoolItem coolItem1 = new CoolItem (coolbar0, SWT.NONE);
		coolItem1.setControl (coolToolBar);
		Point size = coolToolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);
//		coolItem1.setSize (coolItem1.computeSize (size.x, size.y));
		coolItem1.setSize (coolItem1.computeSize (300, 0));
		
		size = coolToolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);
		coolbar0.setSize (coolbar0.computeSize (SWT.DEFAULT, SWT.DEFAULT));
		coolbar0.setVisible(false);

		
//		shell.setSize(shell.computeSize(300, 200));

		shell.pack ();
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}
*/
//	public static void launch(String[] args) {
	public static void main (String[] args) {
		int res = 0;
		echo(TITLE);
		
//		for (int i = 0; i < args.length; i++)
//		    echo(args[i].toString());
		
		if (args.length == 0) {
			echo("No arguements given.....");
			echo("Correct arguements are:\n");
			echo("===================================================");
			echo("Creating an sfv file:");
			echo("\tcreate foo.sfv file1.xxx file2.yyy file3.zzz");
			echo("Or:");
			echo("\tcreate file1.xxx file2.yyy file3.zzz");
			echo("Or:");
			echo("\tfolders folder1 folder2 folder3");
            echo("===================================================");
			echo("Checking files with CRC in filename:");
			echo("\t\"file1 [1337BABE].xxx\" \"file2 (1337BABE).yyy\" \"file3 _1337BABE_.zzz\"");
            echo("===================================================");
            echo("To append the CRCs to the filename:");
            echo("\trename file1.xxx file2.yyy file3.zzz");
            echo("===================================================");
            echo("Checking an sfv file:");
            echo("\tfoo.sfv");
            echo("===================================================");
            echo("Renaming files from an sfv file:");
            echo("\tsfvin foo.sfv");
			res = -1;
		}
		else {
			if (args[0].equalsIgnoreCase("create") ||
                args[0].equalsIgnoreCase("folders") ||
		        args[0].equalsIgnoreCase("rename")) {

			    ICreate oSfv = null;
			    oSfv = SfvObjFab.get(args[0]);
			    
				if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("folders")) {
				    System.out.print("Enter Comment: ");
				    ((SFV) oSfv).set_comment(TextIO.getln());
				    System.out.println();
			    }
				else {
				    System.out.print("Press Enter to start!");
				    TextIO.getln();
				    System.out.println();
				}
			    
				if (args.length > 1) {
					String[] tmp = new String[args.length-1];
					System.arraycopy(args, 1, tmp, 0, args.length-1);
					
                    ((SFV) oSfv).set_files(tmp);
				}
				
				try {
					res = ((ICreate) oSfv).create();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // create, rename, folders
            else if (args[0].equalsIgnoreCase("sfvin")) {
                ICheck oSfv;
                
                oSfv = new RenameFromSfv(args[1]);
                ((SFV) oSfv).set_bufsize(BUFFER);
                
                try {
                    res = oSfv.check();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                int ch = '0';
                while (ch == '0') {
                    try { ch = System.in.read(); }
                    catch (IOException e) {}
                }
            } // sfvin
			else {
				boolean mode = args[0].endsWith(".sfv");
				ICheck oSfv;
				
				if (mode)
				    oSfv = new CheckSfv(args[0]);
				else
				    oSfv = new CheckFile();
				
				((SFV) oSfv).set_bufsize(BUFFER);
				
				if (!mode) {
					String[] tmp = new String[args.length];
					System.arraycopy(args, 0, tmp, 0, args.length);
					
					((SFV) oSfv).set_files(tmp);
				}
				
				try {
					res = oSfv.check();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				int ch = '0';
		        while (ch == '0') {
					try { ch = System.in.read(); }
		        	catch (IOException e) {}
		        }
			} // check
		}
		
        System.exit(res);
	}
}
