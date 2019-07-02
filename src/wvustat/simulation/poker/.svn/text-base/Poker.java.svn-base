/*
* Poker.java
*/

package wvustat.simulation.poker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Poker extends JPanel implements ActionListener{

//Constants
double typeProb[]={1.0,0.50157,0.422579,0.047529,0.021138,0.003532,0.001967,0.001441,0.00024,0.000012,0.000002};
String typeName[]={"","Nothing","One pair","Two pairs","Three of a kind      ","Straight","Flush","Full house","Four of a kind","Straight flush","Royal flush"};

//Controlling variable
boolean shouldWait=false,needCheck=true,shouldPlay=false;
int handTotal=100,handCur=0,typeCur=1,typeNumber[]=new int[11];
Robot robot=null;

//GUIs
ButtonGroup bSpeed;
ImageIcon icon,icons[][]=new ImageIcon[5][14];
JButton jbPlay,jbReset;
JLabel jlTitle,jlLine,jlInput,jlCards[]=new JLabel[5],jlLines[][]=new JLabel[11][5];
JPanel jpCards,jpControl[]=new JPanel[3];
JRadioButton jrSpeed[]=new JRadioButton[2];
JTextField jtInput;

public Poker(){
    init();
}

public void init(){

	ClassLoader cl=this.getClass().getClassLoader();
	//Load Images
	icon=new ImageIcon(cl.getResource("images/000.jpg"));
	for(int i=1;i<5;i++)for(int j=1;j<14;j++) icons[i][j]=new ImageIcon(cl.getResource("images/"+(i*100+j)+".jpg"));

	//Layout
	GridBagLayout layout=new GridBagLayout();
	GridBagConstraints c=new GridBagConstraints();
	c.fill=GridBagConstraints.HORIZONTAL;

	//Top Container
	Container pane=this;
	pane.setLayout(layout);
	

	//Title
	jlTitle=new JLabel("Poker Cards");
	jlTitle.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
	c.gridx=0;
	c.gridy=0;
	c.gridwidth=5;
	layout.setConstraints(jlTitle,c);
	pane.add(jlTitle);

	//Cards
	jpCards=new JPanel();
	for(int i=0;i<5;i++){
		jlCards[i]=new JLabel(icon);
		jlCards[i].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),BorderFactory.createEmptyBorder(2,2,2,2)));
		jpCards.add(jlCards[i]);
	}
	c.gridx=0;
	c.gridy=1;
	layout.setConstraints(jpCards,c);
	pane.add(jpCards);

	//Info Line
	jlLine=new JLabel();
	jlLine.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
	c.gridx=0;
	c.gridy=2;
	layout.setConstraints(jlLine,c);
	pane.add(jlLine);

	//Statistics Lines
	jlLines[0][0]=new JLabel("");
	jlLines[0][1]=new JLabel("    Observed#   ");
	jlLines[0][2]=new JLabel("    Expected#   ");
	jlLines[0][3]=new JLabel("    Observed%   ");
	jlLines[0][4]=new JLabel("    Expected%   ");
	
	
	for(int i=1;i<11;i++){
		jlLines[i][0]=new JLabel(typeName[i]);
		for(int j=1;j<4;j++)jlLines[i][j]=new JLabel("");
		jlLines[i][4]=new JLabel("    "+printDouble(typeProb[i]*100.0));
	}
	c.gridwidth=1;
	for(int i=0;i<11;i++){
		c.gridy=i+3;
		for(int j=0;j<5;j++){
			c.gridx=j;
			layout.setConstraints(jlLines[i][j],c);
			pane.add(jlLines[i][j]);
		}
	}

	//Control Panel
	for(int i=0;i<3;i++){
		jpControl[i]=new JPanel();
		jpControl[i].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20,0,20,0),BorderFactory.createEtchedBorder()));
	}
	jpControl[1].setLayout(new BoxLayout(jpControl[1],BoxLayout.Y_AXIS));

	//Controlling buttons
	jbPlay=new JButton("Play");//,new ImageIcon("gif/play-start.gif"));
	jbPlay.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,0,0,0),jbPlay.getBorder()));
	jbPlay.setMnemonic(KeyEvent.VK_P);
	jbPlay.setActionCommand("play");
	jbPlay.addActionListener(this);
	jbReset=new JButton("Reset");//,new ImageIcon("gif/play-stop.gif"));
	jbReset.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,0,0,0),jbReset.getBorder()));
	jbReset.setMnemonic(KeyEvent.VK_R);
	jbReset.setActionCommand("reset");
	jbReset.addActionListener(this);
	jbReset.setEnabled(false);
	jpControl[2].add(jbPlay);
	jpControl[2].add(jbReset);

	//Speed Selector
	jrSpeed[0]=new JRadioButton("Fast");
	jrSpeed[0].setMnemonic(KeyEvent.VK_F);
	jrSpeed[0].setActionCommand("fast");
	jrSpeed[0].setSelected(true);
	jrSpeed[1]=new JRadioButton("Slow");
	jrSpeed[1].setMnemonic(KeyEvent.VK_S);
	jrSpeed[1].setActionCommand("slow");
	bSpeed=new ButtonGroup();
	for(int i=0;i<2;i++){
		bSpeed.add(jrSpeed[i]);
		jrSpeed[i].setActionCommand("speed"+i);
		jrSpeed[i].addActionListener(this);
		jpControl[1].add(jrSpeed[i]);
	}

	//Text Input
	jlInput=new JLabel("Number of hands:");
	jlInput.setForeground(Color.black);
	jlInput.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
	jtInput=new JTextField(6);
	jtInput.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,0,0,0),jtInput.getBorder()));
	jtInput.setOpaque(false);
	jtInput.setText("100");
	jtInput.setActionCommand("text");
	jtInput.addActionListener(this);
	jpControl[0].add(jlInput);
	jpControl[0].add(jtInput);

	c.gridy=14;
	c.fill=GridBagConstraints.BOTH;
	c.gridx=0;
	c.gridwidth=2;
	layout.setConstraints(jpControl[0],c);
	pane.add(jpControl[0]);
	c.gridx=3;
	layout.setConstraints(jpControl[2],c);
	pane.add(jpControl[2]);
	c.gridx=2;
	c.gridwidth=1;
	layout.setConstraints(jpControl[1],c);
	pane.add(jpControl[1]);

	//Dispatching job
	robot=new Robot(this);
	robot.start();
}
		
//Formatting double print
private String printDouble(double x){
	if(x>0.001) return((x+"0000000").substring(0,6)+"%");
	else if(x>0.0001) return(("0.000"+(x+0.00005)).substring(0,6)+"%");
	else return("0.0000%");
}

//Read in text
private void readHand(){
	int old=handTotal;
	String s=jtInput.getText();
	try{
		Integer i=new Integer(s);
		handTotal=i.intValue();
	}catch(NumberFormatException ex){
		handTotal=old;
	}
	if(handTotal<=1||handTotal>=10000000)handTotal=old;
	jtInput.setText(handTotal+"");
	updateLine();	
}
	
//Actions performer
public void actionPerformed(ActionEvent e){
	String action=e.getActionCommand();
	if(action.equals("play")){
		if(needCheck){
			readHand();
			jtInput.setEnabled(false);
			needCheck=false;
		}
		jbPlay.setText("Pause");
		jbPlay.setActionCommand("pause");
		jbReset.setEnabled(false);
		shouldPlay=true;
	}else if(action.equals("pause")){
		jbPlay.setText("Play");
		jbPlay.setActionCommand("play");
		jbReset.setEnabled(true);
		shouldPlay=false;
	}else if(action.equals("reset")){
		jtInput.setEnabled(true);
		jbPlay.setText("Play");
		jbPlay.setActionCommand("play");
		jbPlay.setEnabled(true);
		jbReset.setEnabled(false);
		needCheck=true;
		handCur=0;
		typeCur=1;
		for(int i=0;i<5;i++) jlCards[i].setIcon(icon);
		for(int i=1;i<11;i++) typeNumber[i]=0;
		updateLines();
	}else if(action.equals("speed0")) shouldWait=false;
	else if(action.equals("speed1")) shouldWait=true;
	else if(action.equals("text")){
		readHand();
	}
}
	
// Update the information line
public void updateLine(){
	jlLine.setText("Hand number:    "+handCur+"/"+handTotal+" -------- "+typeName[typeCur]);
}

// Update statistical information
public void updateLines(){
	updateLine();
	if(handCur==0) for(int i=1;i<11;i++)for(int j=1;j<4;j++)jlLines[i][j].setText("");
	else for(int i=1;i<11;i++){
		jlLines[i][1].setText("    "+typeNumber[i]+"");
		jlLines[i][2].setText("    "+new Integer((int)(typeProb[i]*handCur+0.5)).toString());
		jlLines[i][3].setText("    "+printDouble((double)typeNumber[i]/(double)handCur*100.0));
	}	
}

};//End of Poker

//Construct a robot
class Robot extends Thread{

Poker game;

public Robot(Poker p){
	game=p;
}

public void run(){	
	while(true){
		try{sleep(5);}catch(Exception e){}
		
		if(game.shouldPlay){
			int color[]=new int[5],number[]=new int[5],cards[]=new int[5];
			// Draw 5 different cards
			int mark[]=new int[52];	// Set no marks
			for(int i=0;i<5;i++){
				cards[i]=(int)(Math.random()*(52.0-i));	// Initial draw
				int k=-1;
				for(int j=0;j<=cards[i];j++){
					k++;
					while(mark[k]!=0)k++;	// Is the card marked?
				}
				cards[i]=k;
				mark[k]=1;	// Mark it to prevent next draw
			}
			// Translate into colors and numbers
			for(int i=0;i<5;i++){
				color[i]=cards[i]/13+1;
				number[i]=cards[i]%13+1;
				game.jlCards[i].setIcon(game.icons[color[i]][number[i]]);
			}
			// Classify type
			game.typeCur=classifyHand(color,number);
			game.typeNumber[game.typeCur]++;
			game.handCur++;
			game.updateLines();
			// Should I run next time?
			if(game.handCur==game.handTotal){
				game.jbPlay.setEnabled(false);
				game.shouldPlay=false;
				game.jbReset.setEnabled(true);
			}
			// Should I wait?
			if(game.shouldWait){
				try{sleep(1000);}
				catch(Exception e){}	
			}		
		}
	}	
}

// To classify the type of a hand (5 cards)
private int classifyHand(int color[],int number[]){
	boolean isFlush=true;
	// Is it a Flush?
	for(int i=1;i<5;i++)if(color[i]!=color[0])isFlush=false;
	if(isFlush){
		if(isStraight(number)){
			for(int i=0;i<5;i++)if(number[i]==13)return 10;	//Royal flush
			return 9;	//Straight Flush
		}
		return 6;	//Flush 
	}
	// Not a Flush
	int distr[]={1,1,1,1,1};	// Initial distribution of numbers
	for(int i=1;i<5;i++)for(int j=0;j<i;j++)if(number[i]==number[j]) distr[j]++;
	int product=1;	// The product of the distribution numbers
	for(int i=0;i<5;i++)product*=distr[i];
	switch(product){
		case 1: if(isStraight(number)) return 5;	//Straight
			return 1;		// Nothing
		case 2: return 2;	// One pair	(2!=2)
		case 4: return 3;	// Two pairs (2!*2!=4)
		case 6: return 4;	// Three of a kind (3!=6)
		case 12: return 7;	// Full house (3!*2!=12)
		case 24: return 8;	// Four of a kind (4!=24)
	}
	return 0;//Though this is impossible.
}

private boolean isStraight(int number[]){
	int smallest=14,sum=0;
	for(int i=0;i<5;i++){
		if(number[i]<smallest) smallest=number[i];
		sum+=number[i];
	}
	if(sum-smallest*5==10) return true;
	return false;
}

}//End of Robot	

