package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Bst.BST;
import Bst.NodeBst;
import linkedlist.LinkedList;
import linkedlist.Nodelist;
import trie.TrieNode;
import trie.TrieTree;
import tst.TST;
import tst.TSTNode;





public class Panel extends JPanel implements MouseListener,KeyListener{

	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	JLabel j1,j0;
	JTextField t1;
	JButton browse;
	JTextArea display;
	JLabel j2;
	JRadioButton bst;
	JRadioButton tst;
	JRadioButton trie;
	JTextField t2,bsttext,tsttext,trietext;
	JButton build;
	JButton reset;
	JButton help;
	JButton exit;
	JFileChooser filechooser;
	FileInputStream input;
	File file;
	Vector<Word> stopwords1;
	Vector<String> stopwords = new Vector<>();
	Vector<Document> files ;
	
	BST bsttree = null;
	TST tsttree = null;
	TrieTree trietree = null;
	
	//for working like cmd
	stack.Stack stack = new stack.Stack();
	int index = 0;
	String precommand = "";
	
	//itaretor for inorder func
	int k=1;
	//for tst traverse
	char buffer[] = new char[200];
	
	
	
	
	public Panel(){
		
		File stopfile = new File("./StopWords.txt");
		stopwords1=split(readTxtFile(stopfile.getAbsolutePath()));
		for(Word w : stopwords1){
			stopwords.addElement(w.word.toLowerCase());
		}
		
		
		
		filechooser=new JFileChooser("C:\\Users\\asus\\Documents\\third term fall\\DS\\New folder\\docs");
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		filechooser.setAcceptAllFileFilterUsed(false);
		
		
		//------------------------------GUI-----------------------------------------
		
		GridLayout layoutthispanel = new GridLayout(2, 1);
		this.setLayout(layoutthispanel);
		
		JPanel p0 = new JPanel();
		GridLayout layoutthisuppanel = new GridLayout(6, 1);
		p0.setLayout(layoutthisuppanel);
		
		
		j0=new JLabel ("Invreted Index", SwingConstants.CENTER);	
		j0.setFont(new Font("SansSerif", Font.ITALIC,60));
		p0.add(j0);
		
		j1=new JLabel ("please enter a folder addres to use browse button", SwingConstants.CENTER);	
		j1.setFont(new Font("SansSerif", Font.ITALIC, 20));
		j1.setBackground(Color.getHSBColor(154, 254, 25));
		p0.add(j1);
		
		
		
		JPanel p1=new JPanel();
		FlowLayout layout1 = new FlowLayout();
		p1.setLayout(layout1);		
		t1=new JTextField();
		t1.setColumns(60);
		p1.add(t1);
		browse = new JButton("Browse");
		browse.addMouseListener(this);
		p1.add(browse);
		p1.setVisible(true);
		p0.add(p1);
		
		
	
	
		
		JPanel p2=new JPanel();
		GridLayout layout2 = new GridLayout(1,6);
		p2.setLayout(layout2);
		bst=new JRadioButton("BST");
		p2.add(bst);
		bsttext = new JTextField();
		bsttext.setFont(new Font("SansSerif", Font.ITALIC,25));
		p2.add(bsttext);
		bst.setSelected(true);
		tst=new JRadioButton("TST");
		p2.add(tst);
		tsttext = new JTextField();
		tsttext.setFont(new Font("SansSerif", Font.ITALIC,25));
		p2.add(tsttext);
		trie=new JRadioButton("TRIE");
		p2.add(trie);
		trietext = new JTextField();
		trietext.setFont(new Font("SansSerif", Font.ITALIC,25));
		p2.add(trietext);
		ButtonGroup bg=new ButtonGroup();
		bg.add(bst);
		bg.add(tst);
		bg.add(trie);
		p1.setVisible(true);
		p0.add(p2);
		
		
		t2=new JTextField();
		t2.setText("please enter your command");
		t2.setFont(new Font("SansSerif", Font.ITALIC,40));
		p0.add(t2);
		
		JPanel p3=new JPanel();
		FlowLayout layout3 = new FlowLayout();
		p3.setLayout(layout3);
		build =new JButton("build");
		Dimension d1 = new Dimension(d.width/15, d.height/30);
		build.setPreferredSize(d1);
		build.addMouseListener(this);
		p3.add(build);
		reset=new JButton("reset");
		reset.setPreferredSize(d1);
		reset.addMouseListener(this);
		reset.addKeyListener(this);;
		p3.add(reset);
		exit=new JButton("exit");
		exit.setPreferredSize(d1);
		exit.addMouseListener(this);
		p3.add(exit);
		help=new JButton("help");
		help.setPreferredSize(d1);
		help.addMouseListener(this);
		p3.add(help);
		p3.setVisible(true);
		p0.add(p3);
		
		this.add(p0);
		
		display=new JTextArea();
		display.setFont(new Font("SansSerif", Font.ITALIC,30));
        JScrollPane scroll = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setMinimumSize(new Dimension(160, 200));
        scroll.setPreferredSize(new Dimension(160, 200));
        this.add(scroll);


		
		this.setBackground(Color.getHSBColor(154, 254, 25));
		p0.addKeyListener(this);
		
		//----------------------------end GUI------------------------------------

	}

	
	public Vector<Document> readFromFile(File file){
		//can read files recursivly
		Vector<Document> documents =new Vector<Document>();
		for (File f : file.listFiles()) {
			if (!f.isDirectory()) {//if it is txt or pdf
				// read the text file
				Document d = new Document(f.getAbsolutePath(),f.getName());
				//first find lines and then split lines put words in words vector
				d.words = split(readTxtFile(f.getAbsolutePath()));
				documents.addElement(d);
				
			} else {
				// recursively read all the files in the directory
				readFromFile(f);
			}

		}
		return documents;
	}
	//--------------------------------------------------------------------------------
	public String readTxtFile(String filepath) {
		BufferedReader br=null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line = null;
		
		StringBuilder sb = new StringBuilder();
		while (true) {
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (line != null) {
				sb.append(" ");
				sb.append(line);
			} else {
				line=sb.toString();
				break;
			}
		}
		return line;
	}
	//--------------split------------------------------------------------------------------
	
	public Vector<Word>  split(String lines){
		String[] word;
		Vector<Word> words=new Vector<>();
		
			word = lines.split("[^A-Za-z]+");
			int i=1;
			for(String w : word){
				Word wo = new Word(w.toLowerCase(),i++);
				words.addElement(wo);
			}
			
		
		return words;
	}

	//--------------------------------------------------------------------------------

	@Override
	public void mouseClicked(MouseEvent e) {
		//-----------browse-------------------------------------------------------------------------------------
		if(e.getSource()==browse){// open
			filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = filechooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = filechooser.getSelectedFile();
				t1.setText(file.getAbsolutePath());
				
			} 

		}else if(e.getSource()==build){//------------build-------------------------------------------------------------
			if(file==null){
				display.setText("NO DIRECTORY!");
			}else{
					//read files
				//all files with words
				 files = readFromFile(file);
				
				
				if(bst.isSelected()){
				//if is bst---------------------------------------------
				double start = System.currentTimeMillis();
				 bsttree = new BST();
				 int k=0;
				for(Document doc : files){
					Vector<Word> words = doc.words;
					for(Word wo: words){
						String s = wo.word;
						if(!s.equals("") && !stopwords.contains(s)){
						      bsttree.add(s, doc);
							}
					}
				}
				double end = System.currentTimeMillis();
				display.setText(display.getText()+"\n"+files.size()+"file exist \n "+bsttree.number(bsttree.root)+"node is in binarysearchtree");
				bsttext.setText("Height is "+bsttree.height(bsttree.root)+" \n \\ time of make it ="+(end-start)+"ms");
				
				
				}else if(tst.isSelected()){
				//if is tst---------------------------------------------
					double start = System.currentTimeMillis();
					tsttree = new TST();
					for(Document doc : files){
						Vector<Word> words = doc.words;
						for(Word wo: words){
							String s = wo.word;
							if(!s.equals("") && !stopwords.contains(s)){
							  tsttree.add(s, doc);}
						}
					}
					double end = System.currentTimeMillis();
					display.setText(display.getText()+"\n"+files.size()+"file exist \n "+tsttree.number(tsttree.root)+"node is in tree");
					tsttext.setText("Height is "+tsttree.height(tsttree.root)+"\n \\ time of make it ="+(end-start)+"ms");
					
					
				}else if(trie.isSelected())
				{//if is trie---------------------------------------------
					
					double start = System.currentTimeMillis();
					trietree = new TrieTree();
					for(Document doc : files){
						Vector<Word> words = doc.words;
						for(Word wo: words){
							String s = wo.word;
							if(!s.equals("") && !stopwords.contains(s)){
							  trietree.add(s, doc);}
						}
					}
					double end = System.currentTimeMillis();
					display.setText(display.getText()+"\n"+files.size()+"file exist \n "+trietree.number(trietree.root)+"node is in tree");
					trietext.setText("Height is "+(trietree.height(trietree.root)+1)+"\n \\ time of make it ="+(end-start)+"ms");
					
				}
			
			
			}
		}else if(e.getSource() == reset){//---------reset-------------------------------------------------------------------------
			//i wan to use regx but i couldn't
			//command.matches("[\\s]*add[\\s]*\\((.+)\\)[\\s]*")
			String command1 = t2.getText();
			reset(command1);

		}else if(e.getSource()==exit){//---------exit-------------------------------------------------------------------------
			System.exit(0);
		}else if(e.getSource()==help){//---------help-------------------------------------------------------------------------
			display.setText(display.getText() + "\n" + "for make a tree use build button after define your folder");
			
			
		}
		
	}
//-----------------------------reset function--------------------------------------------------------------------------------------
	public void reset(String command1){
		if(command1!="" && command1!="please enter your command")
		{
			stack.add(command1);
			index = stack.top;
			
			String [] command = command1.split("[^A-Za-z1-9]+");
			
			if(command[0].equals("add")){//for add an file-------------------------------------------------------------
				
				File ff = new File(t1.getText()+"\\"+command[1]+".txt");
				
				BufferedReader br=null;
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(ff.getAbsolutePath())));
					Document newdoc = new Document(ff.getAbsolutePath(),ff.getName());
					
					newdoc.words = split(readTxtFile(ff.getAbsolutePath()));
					boolean found = false;
					for(Document dd : files){//understand file exists before or not
					
						if(dd.address.equals(newdoc.address)){
							display.setText(display.getText() + "\n" + "err: already exists, you may want to update");
							found = true;
							break;}
					}
					if(!found){//if file not exist before
						
							Vector<Word> words = newdoc.words;
							for(Word wo: words){
								String s = wo.word;
								if(!s.equals("") && !stopwords1.contains(s))
								   if(bst.isSelected() && bsttree!=null)
									bsttree.add(s, newdoc);
								   else if(tst.isSelected() && tsttree!=null)
									   tsttree.add(s, newdoc); 
								   else if(trie.isSelected() && trietree !=null)
									   trietree.add(s, newdoc);
							}
							files.addElement(newdoc);
							display.setText(display.getText() + "\n" +command[1]+ "successfully added to lists.");
							
						
					}
					
					
					
				} catch (FileNotFoundException e1) {
					display.setText(display.getText() + "\n" + "err : document not found");
				}
				
				
		//----------------end add an file-------------------------------------------------------------------------------	
			}else if(command[0].equals("dl")){
				//for delete-------------------------------------------------------------
				boolean found = false;
				for(Document dd : files){//understand file exists or not
					if(dd.name.equals(command[1]+".txt")){
						///we HAD file
						if(bsttree != null){
							for(Word w : dd.words){
								if(!w.word.equals(""))
								bsttree.delete(null, bsttree.root, w.word);
							}
						}else if(tsttree != null){
							for(Word w : dd.words){
								tsttree.delete(w.word);
							}
						}else if(trietree != null){
							for(Word w : dd.words){
								trietree.delete( w.word);
							}
						}
						files.removeElement(dd);
						found = true;
						display.setText(display.getText() + "\n" + "found file and delete it");
						break;
						}
				
			}
				if(!found) display.append("\n" + " not found file ");
			
		//----------------------------------end of delete a file-------------------		
		}else if(command[0].equals("update")){//----------------update an file----------------------------------------------------------------	
			
			File ff = new File(t1.getText()+"\\"+command[1]+".txt");
			
			BufferedReader br=null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(ff.getAbsolutePath())));
				Document newdoc = new Document(ff.getAbsolutePath(),ff.getName());
				newdoc.words = split(readTxtFile(ff.getAbsolutePath()));
				boolean found = false;
				for(Document dd : files){//understand file exists before or not
				
				
					if(dd.name.equals(newdoc.name)){
						found = true;
						//remove pre words
						for(Word w : dd.words){
								if(bst.isSelected() && bsttree!=null)
									bsttree.delete(null, bsttree.root, w.word);
								   else if(tst.isSelected() && tsttree!=null)
									   tsttree.delete(w.word); 
								   else if(trie.isSelected() && trietree !=null)
									   trietree.delete(w.word);
						}
						break;
						}
				}
				if(found){//if file  exist before
						Vector<Word> words = newdoc.words;
						for(Word wo : words){
							String s = wo.word;
							if(!s.equals("") && !stopwords1.contains(s))
							   if(bst.isSelected() && bsttree!=null)
								bsttree.add(s, newdoc);
							   else if(tst.isSelected() && tsttree!=null)
								   tsttree.add(s, newdoc); 
							   else if(trie.isSelected() && trietree !=null)
								   trietree.add(s, newdoc);
						}
						display.setText(display.getText() + "\n" +command[1]+ "successfully update.");
						
					
				}else {
					display.setText(display.getText() + "\n" +command[1]+ "this file dose not exist already");
				}
				
				
				
			} catch (FileNotFoundException e1) {
				display.setText(display.getText() + "\n" + "err : document not found");
			}	
			//----------------end update an file----------------------------------------------------------------	
			}else if(command[0].equals("list")){//----------list command---------------------------------------------------------------------
				
				if(command[1].equals("w")){
					   if(bst.isSelected() && bsttree!=null){
						   
						   k=1;
						   inorder(bsttree.root);
					   }else if(tst.isSelected() && tsttree!=null){
						  traverse(tsttree.root, "");
						   
					   }else if(trie.isSelected() && trietree !=null){
						   traverseTrie(trietree.root,"");
					   }
					
				}else if(command[1].equals("l")){
					display.setText(display.getText() + "\nfiles = " );
					for(Document d : files){
						display.setText(display.getText() + d.name+" , " );						  
					}
					
				}else if(command[1].equals("f")){
					display.setText(display.getText() + "\nfiles = " );
					for (File f : file.listFiles()){
						display.setText(display.getText() + f.getName() + " , " );
					}
					
				}
			//----------end list command---------------------------------------------------------------------	
			}else if(command[0].equals("search")){//----------search  command---------------------------------------------------------------------
				 if(command[1].equals("w")){//word
					 	if(bst.isSelected() && bsttree!=null){
					 		
					 		NodeBst t = bsttree.binarySearch(command[2].toLowerCase());
					 		if(t!=null){
					 			Nodelist nl = t.files.first;
					 			display.setText(display.getText() +" \n"+command[2]+" : " );
								  while(nl != null){
									  display.setText(display.getText() + nl.name );
									  nl = nl.link;
								  }
					 		}else{
					 			display.setText(display.getText() + "\n not found" );
								  
					 		}
					 		
							  
					 		}
						   else if(tst.isSelected() && tsttree!=null){
							    TSTNode t = tsttree.search( command[2].toLowerCase());
							    display.setText(display.getText() +" \n"+command[2]+" : " );
								 if(t!=null){
						 			 Nodelist nl = t.files.first;
									  while(nl != null){
										  display.setText(display.getText() + nl.name + "," );
										  nl = nl.link;
									  }	
						 		}else{
						 			display.setText(display.getText() + "\n not found" );
									  
						 		}
							   
						   }
						   else if(trie.isSelected() && trietree !=null){
							   //write it later  too
							   TrieNode t = trietree.search(command[2].toLowerCase());
						 		if(t!=null){
						 			 Nodelist nl = t.files.first;
									  while(nl != null){
										  display.setText(display.getText() + nl.name );
										  nl = nl.link;
									  }	
						 		}else{
						 			display.setText(display.getText() + "\n not found" );
									  
						 		}
						   }
						//end of search word---------------------------------------------------------------------------
					}else if(command[1].equals("s")){//sentences
						boolean notfound =false;
						LinkedList ans = new LinkedList();
						double ss = System.currentTimeMillis();
						for(int i = 2; i < command.length; i++){//for each word
							if(!stopwords.contains(command[i].toLowerCase())){
								if(bst.isSelected() && bsttree!=null){
																
									NodeBst t = bsttree.binarySearch(command[i]);
							 		 if(t!=null){
								 			LinkedList ans1 = t.files;
								 			ans = ans.participation(ans1);
								 		}else{
								 			display.setText(display.getText() + "\n not found" );	  
								 			notfound = true;
								 		}
									  
							 		}
								   else if(tst.isSelected() && tsttree!=null){
									    TSTNode t = tsttree.search(command[i]);
									    if(t!=null){
								 			LinkedList ans1 = t.files;
								 			ans = ans.participation(ans1);
								 		}else{
								 			display.setText(display.getText() + "\n not found" );	  
								 			notfound = true;
								 		}
									   
								   }
								   else if(trie.isSelected() && trietree !=null){
									   //write it later  too
									   TrieNode t = trietree.search(command[i]);
								 		if(t!=null){
								 			LinkedList ans1 = t.files;
								 			ans = ans.participation(ans1);	
								 		}else{
								 			display.setText(display.getText() + "\n not found" );	  
								 			notfound = true;
								 		}
								   } 
								
					
							}
						}
						
						display.setText(display.getText() + "\nresult : ");
						if(ans!=null && !notfound){
							Nodelist nl = ans.first;
							while(nl != null){
								display.setText(display.getText() + nl.name +"->  ");
								//from here===========================================================================
								int numberofwords = command.length - 2;
								//j contain place of each word
								int [] j  = new int[numberofwords];
								for (int i = 0; i < numberofwords; i++){
									for(Word w : nl.doc.words){
										if((w.word).equals(command[i+2])){
											j[i] = w.place-1;
											break;
										}
									}								
								}
								int i;boolean b = true;
								for( i=0;i<numberofwords-1  ;i++){
										
											display.append("(...");
											if(j[i]-1>=0)
											display.append(nl.doc.words.elementAt(j[i]-1).word + " ");
											if(j[i] < nl.doc.words.size())
											display.append(nl.doc.words.elementAt(j[i]).word + " ");
											if(j[i]+1 < nl.doc.words.size())
											display.append(nl.doc.words.elementAt(j[i]+1).word + " ");
											display.append("...)");										
										
								}
								//to here is for more implemention ==========================================================================
								
								nl = nl.link;
								display.append("\n");
							}	
						}
						double e = System.currentTimeMillis();
						System.err.println(ss-e);
						
					}//end if search sentence---------------------------------------------------------------------
			}//end if search command---------------------------------------------------------------------
	    }//end of if(command1!="" && command1!="please enter your command")-----------------------------------

	}//end of reset func-----------------------------------------------------------------
	
	//for list -w in bst--------------------------------------------------------
	//O(n)
	public void inorder(NodeBst root){
		if(root == null) return;
		Nodelist nl = root.files.first;
		display.setText(display.getText() +"\n"+(k++) +" " + root.value +" : ");
		while(nl != null){
			display.setText(display.getText() +"," +" " + nl.name );
			nl = nl.link;
		}
		  inorder(root.lc);
		  inorder(root.rc);
	}
	
	//for list -w in tst--------------------------------------------------------
	//O(n)
	int l=0;
    private void traverse(TSTNode r, String str)

    {

        if (r != null)

        {
            
            
            str = str + r.data;

            if (r.endofword){
            	  Nodelist nl = r.files.first;
            	  display.setText(display.getText() +"\n"+(k++) +" " + str +" : ");
            	  while(nl != null){
            		  display.setText(display.getText() +"," +" " + nl.name );
            		  nl = nl.link;
            	  	}
            }

            traverse(r.middle, str);
            str = str.substring(0, str.length() - 1);
            
            
            traverse(r.left, str);
            traverse(r.right, str);

        }

    }
	//for trie ----------------------------------------------------------------
    //O(N)
	public void traverseTrie(TrieNode r,String word) {
		
		if(r.isEnd){
			word += r.ch;
			Nodelist nl = r.files.first;
            display.setText(display.getText() +"\n "+word+" : ");
    		while(nl != null){
    			display.setText(display.getText() +"," +" " + nl.name );
    			nl = nl.link;
    		}
			
		}
		if(r.ch != ' ')
		word += r.ch;
		TrieNode node = r;
		Nodelist nl = node.children.first;
		while(nl != null){
			traverseTrie(nl.node,word);
			nl = nl.link;			
		}
		if(r.ch != ' ')
		word = word.substring(0, word.length()-1);

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	



	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP){
			
			if(index - 1 >= 0 && !stack.subnode(index - 1).equals("")){	
				precommand = stack.subnode(--index );
				t2.setText(precommand);
				
			}
					
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			if(index+1 <stack.top && !stack.subnode(index + 1).equals("")){
				precommand = stack.subnode(++index);
				t2.setText(precommand);
			}
					
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(!precommand.equals("")){
				reset(precommand);
			}
				
					
		}
	}



	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}



	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	

}
