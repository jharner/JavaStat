/*
* Testcorrcoeff.java
*
*/
package wvustat.simulation.correlation;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Testcorrcoeff 
        extends javax.swing.JPanel
        implements ActionListener,MouseListener{
        
        JButton resetButton,submitButton,newtrialButton;
        JLabel titleLabel,bkLabel1,bkLabel2,bkLabel3,bkLabel4;
        JPanel titlePanel,mainPanel,actionPanel,resultPanel,bottomPanel,plotpanel;
        JPanel[] plotPanel=new JPanel[4];
        JPanel panel1,panel2,panel3,panel4;
        InfoPanel infoPanel;
        JComboBox choice1,choice2,choice3,choice4;
        Border etched;
        
       
   	Pos pos;
        Posone posone;
        Nega nega;
        Negaone negaone;
        Zero zero;
        CorrePanel mPanel,plotpanel1,plotpanel2,plotpanel3,plotpanel4;
    	Color[] resultcolor=new Color[4];
        public static String plotname;
        int MAX_NUMBER=100;
        public int[][] plotx=new int[4][MAX_NUMBER];
        public int[][] ploty=new int[4][MAX_NUMBER];
        public int[] x=new int[MAX_NUMBER];
        public int[] y=new int[MAX_NUMBER];
        public int[] randomplot={0,1,2,3,4};
        public int a0,a1,a2,a3;
        public double[] plotr=new double[4];
        public String[] corr=new String[4];
        public String[][] choiceitem = new String[4][5];
        public boolean isvisible;
        public boolean[] itemExist= new boolean[5],selitemExist=new boolean[4];
        public boolean[] isChosen = new boolean[4];
        public String[] stAnswer = new String[4];
        public int totalScore,trialCount,stScore;
        public int choicecount;
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        
        public Testcorrcoeff(){
	    init();
        }
        
        
        public void init(){
        
              
                //init data
                trialCount=0;
                totalScore=0;
                stScore=0;
                
                for(int i=0;i<4;i++){
                	stAnswer[i]=null;
                };
                
                //titlePanel
                this.setLayout(new BorderLayout());
                
                
                
                titleLabel = new JLabel("Correlation Coefficient");
                titleLabel.setFont(new Font("title",Font.BOLD,13));
                titlePanel = new JPanel();
                titlePanel.add(titleLabel);
                add("North",titlePanel);
                
                // plot init
                plotinit();
                
                //add mainPanel
                etched=BorderFactory.createEtchedBorder();
                
                plotname="plot 1";
                
                mPanel=new CorrePanel(340,340,340,plotx[0],ploty[0],8,true,plotname);
                plotpanel1 = new CorrePanel(85,85,85,plotx[0],ploty[0],2,true,"plot 1");
        	plotpanel1.addMouseListener(this);
        	  
                //    plot 2
        	plotpanel2 = new CorrePanel(85,85,85,plotx[1],ploty[1],2,false,"plot 2");
                plotpanel2.addMouseListener(this);
        	
        	//    plot 3
        	plotpanel3 = new CorrePanel(85,85,85,plotx[2],ploty[2],2,false,"plot 3");
                plotpanel3.addMouseListener(this);
        	
        	//    plot 4
        	plotpanel4 = new CorrePanel(85,85,85,plotx[3],ploty[3],2,false,"plot 4");
                plotpanel4.addMouseListener(this);
                
                //choice ComboBox
                for(int i=0;i<5;i++){
	        	itemExist[i]=true;
		    
		         
		        };
		
	        for(int j=0;j<4;j++){
	        	isChosen[j]=false;
	        	selitemExist[j]=true;
	        };
	        
        	choicecount=0;
                correlation();
                
                for(int i=0;i<4;i++){
                	choiceitem[i][0]="    select ";
                	choiceitem[i][1]=" "+corr[0];
                	choiceitem[i][2]=" "+corr[1];
                	choiceitem[i][3]=" "+corr[2];
                	choiceitem[i][4]=" "+corr[3];
                	
                }
                
                choice1 = new JComboBox();
                choice2 = new JComboBox();
                choice3 = new JComboBox();
                choice4 = new JComboBox();
                
                choice1.setPreferredSize(new Dimension(80,25));
                choice2.setPreferredSize(new Dimension(80,25));
                choice3.setPreferredSize(new Dimension(80,25));
                choice4.setPreferredSize(new Dimension(80,25));
               
                for(int i=0;i<5;i++){
                	choice1.addItem(choiceitem[0][i]);
                	choice2.addItem(choiceitem[1][i]);
                	choice3.addItem(choiceitem[2][i]);
                	choice4.addItem(choiceitem[3][i]);
                	
                };
                
                
                choice1.addActionListener(this);
                choice2.addActionListener(this);
                choice3.addActionListener(this);
                choice4.addActionListener(this);
                
                panel1=new JPanel();
                panel2=new JPanel();
                panel3=new JPanel();
                panel4=new JPanel();
                
                panel1.setLayout(new BorderLayout());
                panel2.setLayout(new BorderLayout());
                panel3.setLayout(new BorderLayout());
                panel4.setLayout(new BorderLayout());
                
                panel1.add("Center",plotpanel1);
                panel1.add("South",choice1);
                panel2.add("Center",plotpanel2);
                panel2.add("South",choice2);
                panel3.add("Center",plotpanel3);
                panel3.add("South",choice3);
                panel4.add("Center",plotpanel4);
                panel4.add("South",choice4);
                
                plotpanel=new JPanel();
                plotpanel.setLayout(gbl);
                gbc.insets=new Insets(2,2,2,2);
                
                panel1.setBorder(etched);
                panel2.setBorder(etched);
                panel3.setBorder(etched);
                panel4.setBorder(etched);
                
                gbc.weightx=0.2;
                gbc.weighty=1;
                gbc.fill=GridBagConstraints.BOTH;
                gbc.anchor=GridBagConstraints.WEST;
                gbc.gridx=0;
                gbc.gridy=0;
                addComponent(plotpanel,gbl,gbc,panel1);
                
                gbc.gridx=0;
                gbc.gridy=1;
                bkLabel1=new JLabel("");
                addComponent(plotpanel,gbl,gbc,bkLabel1);
                
                gbc.gridx=0;
                gbc.gridy=2;
                addComponent(plotpanel,gbl,gbc,panel2);
                
                gbc.gridx=0;
                gbc.gridy=3;
                bkLabel2=new JLabel("");
                addComponent(plotpanel,gbl,gbc,bkLabel2);
                
                gbc.anchor=GridBagConstraints.EAST;
                gbc.weightx=0.2;
                gbc.weighty=1;
              	gbc.gridx=1;
                gbc.gridy=0;
                addComponent(plotpanel,gbl,gbc,panel3);
                
                gbc.gridx=1;
                gbc.gridy=1;
                bkLabel3=new JLabel("");
                addComponent(plotpanel,gbl,gbc,bkLabel3);
                
                gbc.gridx=1;
                gbc.gridy=2;
                addComponent(plotpanel,gbl,gbc,panel4);
                
                gbc.gridx=1;
                gbc.gridy=3;
                bkLabel4=new JLabel("");
                addComponent(plotpanel,gbl,gbc,bkLabel4);
                
                mainPanel= new JPanel();
                mainPanel.setLayout(gbl);
                gbc.insets=new Insets(0,0,0,0);
                
                gbc.fill=GridBagConstraints.BOTH;
                gbc.gridx=0;
                gbc.gridy=0;
                gbc.weightx=0.7;
                gbc.weighty=0.1;
                addComponent(mainPanel,gbl,gbc,mPanel);
                gbc.gridx=1;
                gbc.gridy=0;
                gbc.weightx=0.15;
                gbc.weighty=1;
                addComponent(mainPanel,gbl,gbc,plotpanel);
                
                mainPanel.setBorder(etched);
                add("Center",mainPanel);
                
                //add actionPanel
                
                bottomPanel = new JPanel();
                bottomPanel.setLayout(new BorderLayout());
                actionPanel = new JPanel();
                actionPanel.setLayout(new GridLayout(3,1));
                actionPanel.setFont(new Font("bottom",Font.PLAIN,12));
                resetButton = new JButton(" reset ");
                resetButton.addActionListener(this);
                submitButton = new JButton(" submit ");
                submitButton.addActionListener(this);
                newtrialButton = new JButton("new trial");
                newtrialButton.addActionListener(this);
                newtrialButton.setEnabled(false);
                actionPanel.add(submitButton);
                actionPanel.add(resetButton);
                actionPanel.add(newtrialButton);
                
               
                resultPanel = new JPanel();
                resultPanel.setLayout(new BorderLayout());
                for(int i=0;i<4;i++){
        		resultcolor[i]=Color.magenta;
        	    };
        	    
        	isvisible=false;
                infoPanel = new InfoPanel(isvisible,getValue(plotr[0]),getValue(plotr[1]),
                getValue(plotr[2]),getValue(plotr[3]),resultcolor[0],resultcolor[1],
                resultcolor[2],resultcolor[3],totalScore,stScore);
                resultPanel.add("Center",infoPanel);
               	
               	bottomPanel.add("Center",resultPanel);
		bottomPanel.add("East",actionPanel);
		add("South",bottomPanel);
                       
                //System.out.print(check);
        }// end of init
        
        
        public void addComponent(Container c,GridBagLayout gl, GridBagConstraints gc, Component com){
        
         	gl.setConstraints(com,gc);
         	c.add(com);
 	}// end of addGB
 	
public int[] randomChoose(){
	  int[] randomchoose = new int[4];
	  int[] index = new int[4];
	  boolean check=true;
	  int a;
	  
	  for (int i=0;i<4;i++){
	  	index[i]=5; //anything except 0-4;
	  }
	  	
	  for (int i=0;i<4;i++){
	  	a=(int)(Math.random()*5);
	  	for(int j=0;j<i;j++){
	  		if(a==index[j])
	  		check=false;
	  	}
	  	if(check==true){
	  		randomchoose[i]=a;
	  		index[i]=a;
	  	}
	  	else{
	  	i=i-1;
	  	check=true;
	        };
	}
	return randomchoose;
}// end of randomChoose
	  		
public String getValue(double x){
	
	String double_value = Double.toString(x);
	return (double_value.substring(0, double_value.indexOf(".") + Math.min(3, double_value.length()-1)));
	
	/*(int a;
	int b;
	int c;
	int d;
	int sign;
	float getvalue;
	
	if(x<0)
	sign=-1;
	else sign=1;
	
	x=Math.abs(x);
	a=(int)(x*1000)-(int)(x*100)*10;
	b=(int)(x*100)-(int)(x*10)*10;
	d=(int)(x);
	c=(int)(x*10)-d*10;
	
	getvalue=sign*(float)(d+c*0.1+b*0.01+a*0.001);
	return Float.toString(getvalue);*/
}//end of getValue;

public int[] choiceitem(){
	
	int[] choiceitem = new int[4];
	int mytemp;
	boolean check;
	boolean[] c = new boolean[4];
	
	mytemp=0;
	choiceitem[0]=(int)(Math.random()*4);
	for(int i=1;i<4;i++){
		check=true;
		
		while(check==true)
		{mytemp=(int)(Math.random()*4);
		for(int k=0;k<4;k++){
		c[k]=false;
	        };
		 for(int j=0;j<i;j++){
		 	if(choiceitem[j]==mytemp)
		 	c[j]=true;
		 	
		        //end of if
		      };//end of for
		 if((c[0]==false)&&(c[1]==false)&&(c[2]==false)&&(c[3]==false))
		 check=false;
		 else check=true;
	        };//end of while
	        choiceitem[i]=mytemp;
	
	};//end of for
	return choiceitem;
}//end of choiceitem		

public String[] correlation(){
	int[] choiceindex = new int[4];
	
	
	choiceindex=choiceitem();
	a0=choiceindex[0];
	a1=choiceindex[1];
	a2=choiceindex[2];
	a3=choiceindex[3];
	
	if (plotr[0]<0) corr[a0]=getValue(plotr[0]); else corr[a0]=" "+getValue(plotr[0]);
	if (plotr[1]<0) corr[a1]=getValue(plotr[1]); else corr[a1]=" "+getValue(plotr[1]);
	if (plotr[2]<0) corr[a2]=getValue(plotr[2]); else corr[a2]=" "+getValue(plotr[2]);
	if (plotr[3]<0) corr[a3]=getValue(plotr[3]); else corr[a3]=" "+getValue(plotr[3]);
	
	return corr;
}//end of Correlation

public void plotinit(){
	randomplot=randomChoose();
                for(int i=0;i<4;i++){
                	
                	switch(randomplot[i]){
                	case 0:
                	pos = new Pos();
                	plotx[i]=pos.getPlotx();
                	ploty[i]=pos.getPloty();
                	plotr[i]=pos.getPlotr();
        	        break;
        	        
        	        case 1:
        	        posone = new Posone();
        	        plotx[i]=posone.getPlotx();
                	ploty[i]=posone.getPloty();
                	plotr[i]=posone.getPlotr();       	          	  
        	        break;
        	        
        	        case 2:
        	        nega = new Nega();
        	        plotx[i]=nega.getPlotx();
                	ploty[i]=nega.getPloty();
                	plotr[i]=nega.getPlotr();       	       
        	        break;
        	        
        	        case 3:
        	        negaone = new Negaone();
        	        plotx[i]=negaone.getPlotx();
                	ploty[i]=negaone.getPloty();
                	plotr[i]=negaone.getPlotr(); 
        	        break;
        	        
        	        case 4:
        	        default:
        	        zero = new Zero();
        	        plotx[i]=zero.getPlotx();
                	ploty[i]=zero.getPloty();
                	plotr[i]=zero.getPlotr(); 
        	        break;
        	}//end of switch
        }// end of for
}//end of plotinit


public void mouseClicked(MouseEvent e){
}

public void mouseReleased(MouseEvent e){

	
}

public void mouseEntered(MouseEvent e){
}

public void mouseExited(MouseEvent e){
}

public void mousePressed(MouseEvent e){
	Object plotclick=e.getSource();
	
	if(plotclick==plotpanel1){
        	        		
        	        		mPanel.x=plotx[0];
        	        		mPanel.y=ploty[0];
        	        		mPanel.plotname="plot 1";
        	        		mPanel.repaint();
        	        		plotpanel1.border=true;
        	        		plotpanel1.repaint();
        	        		plotpanel2.border=false;
        	        		plotpanel2.repaint();
        	        		plotpanel3.border=false;
        	        		plotpanel3.repaint();
        	        		plotpanel4.border=false;
        	        		plotpanel4.repaint();
        	        		
        	        	}
       else if(plotclick==plotpanel2){
       	
        	        	
        	        	
        	        		mPanel.x=plotx[1];
        	        		mPanel.y=ploty[1];
        	        		mPanel.plotname="plot 2";
        	        		mPanel.repaint();
        	        		plotpanel1.border=false;
        	        		plotpanel1.repaint();
        	        		plotpanel2.border=true;
        	        		plotpanel2.repaint();
        	        		plotpanel3.border=false;
        	        		plotpanel3.repaint();
        	        		plotpanel4.border=false;
        	        		plotpanel4.repaint();
        	        		
        	        	}
       else if(plotclick==plotpanel3){
       
        	        		mPanel.x=plotx[2];
        	        		mPanel.y=ploty[2];
        	        		mPanel.plotname="plot 3";
        	        		mPanel.repaint();
        	        		plotpanel1.border=false;
        	        		plotpanel1.repaint();
        	        		plotpanel2.border=false;
        	        		plotpanel2.repaint();
        	        		plotpanel3.border=true;
        	        		plotpanel3.repaint();
        	        		plotpanel4.border=false;
        	        		plotpanel4.repaint();
        	        		
        	        	}
        else if(plotclick==plotpanel4){
        		     
        	        		mPanel.x=plotx[3];
        	        		mPanel.y=ploty[3];
        	        		mPanel.plotname="plot 4";
        	        		mPanel.repaint();
        	        		plotpanel1.border=false;
        	        		plotpanel1.repaint();
        	        		plotpanel2.border=false;
        	        		plotpanel2.repaint();
        	        		plotpanel3.border=false;
        	        		plotpanel3.repaint();
        	        		plotpanel4.border=true;
        	        		plotpanel4.repaint();
        	        		
        	        	}
}//end of mousePressed


public void actionPerformed(ActionEvent e)
{
	
	
	if((e.getSource()==choice1)&&(choice1.getSelectedItem()==choiceitem[0][1])&&(itemExist[1]==true)){
	      	        
			if(selitemExist[0]==true)choice1.removeItem(choiceitem[0][0]);
			if(itemExist[2]==true)choice1.removeItem(choiceitem[0][2]);
			if(itemExist[3]==true)choice1.removeItem(choiceitem[0][3]);
			if(itemExist[4]==true)choice1.removeItem(choiceitem[0][4]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][1]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][1]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][1]);
		        selitemExist[0]=false;
		        itemExist[1]=false;
		        //choice1.removeItemListener(this);
		        isChosen[0]=true;
		        choicecount++;
		        stAnswer[0]=choiceitem[0][1];  	
		}
		
		else if((e.getSource()==choice1)&&(choice1.getSelectedItem()==choiceitem[0][2])&&(itemExist[2]==true)){
			if(selitemExist[0]==true)choice1.removeItem(choiceitem[0][0]);
			if(itemExist[1]==true)choice1.removeItem(choiceitem[0][1]);
			if(itemExist[3]==true)choice1.removeItem(choiceitem[0][3]);
			if(itemExist[4]==true)choice1.removeItem(choiceitem[0][4]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][2]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][2]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][2]);
		        //choice1.removeItemListener(this);
		        selitemExist[0]=false;
		        itemExist[2]=false;
		        isChosen[0]=true;
		        choicecount++;
		        stAnswer[0]=choiceitem[0][2];
		}
		
		else if((e.getSource()==choice1)&&(choice1.getSelectedItem()==choiceitem[0][3])&&(itemExist[3]==true)){
			if(selitemExist[0]==true)choice1.removeItem(choiceitem[0][0]);
			if(itemExist[1]==true)choice1.removeItem(choiceitem[0][1]);
			if(itemExist[2]==true)choice1.removeItem(choiceitem[0][2]);
			if(itemExist[4]==true)choice1.removeItem(choiceitem[0][4]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][3]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][3]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][3]);
		       // choice1.removeItemListener(this);
		        selitemExist[0]=false;
		        itemExist[3]=false;
		        isChosen[0]=true;
		        choicecount++;
		        stAnswer[0]=choiceitem[0][3];
		}
		
		else if((e.getSource()==choice1)&&(choice1.getSelectedItem()==choiceitem[0][4])&&(itemExist[4]==true)){
			if(selitemExist[0]==true)choice1.removeItem(choiceitem[0][0]);
			if(itemExist[1]==true)choice1.removeItem(choiceitem[0][1]);
			if(itemExist[2]==true)choice1.removeItem(choiceitem[0][2]);
			if(itemExist[3]==true)choice1.removeItem(choiceitem[0][3]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][4]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][4]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][4]);
		       // choice1.removeItemListener(this);
		        selitemExist[0]=false;
		        itemExist[4]=false;
		        isChosen[0]=true;
		        choicecount++;
		        stAnswer[0]=choiceitem[0][4];
		}
		
		else if((e.getSource()==choice2)&&(choice2.getSelectedItem()==choiceitem[1][1])&&(itemExist[1]==true)){
			if(selitemExist[1]==true)choice2.removeItem(choiceitem[1][0]);
			if(itemExist[2]==true)choice2.removeItem(choiceitem[1][2]);
			if(itemExist[3]==true)choice2.removeItem(choiceitem[1][3]);
			if(itemExist[4]==true)choice2.removeItem(choiceitem[1][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][1]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][1]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][1]);
		       // choice2.removeItemListener(this);
		        selitemExist[1]=false;
		        itemExist[1]=false;
		        isChosen[1]=true;
		        choicecount++;
		        stAnswer[1]=choiceitem[1][1];
		}
		
		else if((e.getSource()==choice2)&&(choice2.getSelectedItem()==choiceitem[1][2])&&(itemExist[2]==true)){
			if(selitemExist[1]==true)choice2.removeItem(choiceitem[1][0]);
			if(itemExist[1]==true)choice2.removeItem(choiceitem[1][1]);
			if(itemExist[3]==true)choice2.removeItem(choiceitem[1][3]);
			if(itemExist[4]==true)choice2.removeItem(choiceitem[1][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][2]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][2]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][2]);
		      //  choice2.removeItemListener(this);
		        selitemExist[1]=false;
		        itemExist[2]=false;
		        isChosen[1]=true;
		        choicecount++;
		        stAnswer[1]=choiceitem[1][2];
		}
		
		else if((e.getSource()==choice2)&&(choice2.getSelectedItem()==choiceitem[1][3])&&(itemExist[3]==true)){
			if(selitemExist[1]==true)choice2.removeItem(choiceitem[1][0]);
			if(itemExist[1]==true)choice2.removeItem(choiceitem[1][1]);
			if(itemExist[2]==true)choice2.removeItem(choiceitem[1][2]);
			if(itemExist[4]==true)choice2.removeItem(choiceitem[1][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][3]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][3]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][3]);
		      //  choice2.removeItemListener(this);
		        selitemExist[1]=false;
		        itemExist[3]=false;
		        isChosen[1]=true;
		        choicecount++;
		        stAnswer[1]=choiceitem[1][3];
		}
		
		else if((e.getSource()==choice2)&&(choice2.getSelectedItem()==choiceitem[1][4])&&(itemExist[4]==true)){
			if(selitemExist[1]==true)choice2.removeItem(choiceitem[1][0]);
			if(itemExist[1]==true)choice2.removeItem(choiceitem[1][1]);
			if(itemExist[2]==true)choice2.removeItem(choiceitem[1][2]);
			if(itemExist[3]==true)choice2.removeItem(choiceitem[1][3]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][4]);
			if(isChosen[2]==false)choice3.removeItem(choiceitem[2][4]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][4]);
		      //  choice2.removeItemListener(this);
		        selitemExist[1]=false;
		        itemExist[4]=false;
		        isChosen[1]=true;
		        choicecount++;
		        stAnswer[1]=choiceitem[1][4];
		}
		 
		else if((e.getSource()==choice3)&&(choice3.getSelectedItem()==choiceitem[2][1])&&(itemExist[1]==true)){
			if(selitemExist[2]==true)choice3.removeItem(choiceitem[2][0]);
			if(itemExist[2]==true)choice3.removeItem(choiceitem[2][2]);
			if(itemExist[3]==true)choice3.removeItem(choiceitem[2][3]);
			if(itemExist[4]==true)choice3.removeItem(choiceitem[2][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][1]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][1]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][1]);
		        //choice3.removeItemListener(this);
		        selitemExist[2]=false;
		        itemExist[1]=false;
		        isChosen[2]=true;
		        choicecount++;
		        stAnswer[2]=choiceitem[2][1];
		}
		
		else if((e.getSource()==choice3)&&(choice3.getSelectedItem()==choiceitem[2][2])&&(itemExist[2]==true)){
			if(selitemExist[2]==true)choice3.removeItem(choiceitem[2][0]);
			if(itemExist[1]==true)choice3.removeItem(choiceitem[2][1]);
			if(itemExist[3]==true)choice3.removeItem(choiceitem[2][3]);
			if(itemExist[4]==true)choice3.removeItem(choiceitem[2][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][2]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][2]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][2]);
		       // choice3.removeItemListener(this);
		        selitemExist[2]=false;
		        itemExist[2]=false;
		        isChosen[2]=true;
		        choicecount++;
		        stAnswer[2]=choiceitem[2][2];
		}
		
		else if((e.getSource()==choice3)&&(choice3.getSelectedItem()==choiceitem[2][3])&&(itemExist[3]==true)){
			if(selitemExist[2]==true)choice3.removeItem(choiceitem[2][0]);
			if(itemExist[1]==true)choice3.removeItem(choiceitem[2][1]);
			if(itemExist[2]==true)choice3.removeItem(choiceitem[2][2]);
			if(itemExist[4]==true)choice3.removeItem(choiceitem[2][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][3]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][3]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][3]);
		       // choice3.removeItemListener(this);
		        selitemExist[2]=false;
		        itemExist[3]=false;
		        isChosen[2]=true;
		        choicecount++;
		        stAnswer[2]=choiceitem[2][3];
		}
		
		else if((e.getSource()==choice3)&&(choice3.getSelectedItem()==choiceitem[2][4])&&(itemExist[4]==true)){
			if(selitemExist[2]==true)choice3.removeItem(choiceitem[2][0]);
			if(itemExist[1]==true)choice3.removeItem(choiceitem[2][1]);
			if(itemExist[2]==true)choice3.removeItem(choiceitem[2][2]);
			if(itemExist[3]==true)choice3.removeItem(choiceitem[2][3]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][4]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][4]);
		        if(isChosen[3]==false)choice4.removeItem(choiceitem[3][4]);
		       // choice3.removeItemListener(this);
		        selitemExist[2]=false;
		        itemExist[4]=false;
		        isChosen[2]=true;
		        choicecount++;
		        stAnswer[2]=choiceitem[2][4];
		}
                
                else if((e.getSource()==choice4)&&(choice4.getSelectedItem()==choiceitem[3][1])&&(itemExist[1]==true)){
			if(selitemExist[3]==true)choice4.removeItem(choiceitem[3][0]);
			if(itemExist[2]==true)choice4.removeItem(choiceitem[3][2]);
			if(itemExist[3]==true)choice4.removeItem(choiceitem[3][3]);
			if(itemExist[4]==true)choice4.removeItem(choiceitem[3][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][1]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][1]);
		        if(isChosen[2]==false)choice3.removeItem(choiceitem[2][1]);
		     //   choice4.removeItemListener(this);
		        selitemExist[3]=false;
		        itemExist[1]=false;
		        isChosen[3]=true;
		        choicecount++;
		        stAnswer[3]=choiceitem[3][1];
		}
		
		else if((e.getSource()==choice4)&&(choice4.getSelectedItem()==choiceitem[3][2])&&(itemExist[2]==true)){
			if(selitemExist[3]==true)choice4.removeItem(choiceitem[3][0]);
			if(itemExist[1]==true)choice4.removeItem(choiceitem[3][1]);
			if(itemExist[3]==true)choice4.removeItem(choiceitem[3][3]);
			if(itemExist[4]==true)choice4.removeItem(choiceitem[3][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][2]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][2]);
		        if(isChosen[2]==false)choice3.removeItem(choiceitem[2][2]);
		      //  choice4.removeItemListener(this);
		        selitemExist[3]=false;
		        itemExist[2]=false;
		        isChosen[3]=true;
		        choicecount++;
		        stAnswer[3]=choiceitem[3][2];
		}
		
		else if((e.getSource()==choice4)&&(choice4.getSelectedItem()==choiceitem[3][3])&&(itemExist[3]==true)){
			if(selitemExist[3]==true)choice4.removeItem(choiceitem[3][0]);
			if(itemExist[1]==true)choice4.removeItem(choiceitem[3][1]);
			if(itemExist[2]==true)choice4.removeItem(choiceitem[3][2]);
			if(itemExist[4]==true)choice4.removeItem(choiceitem[3][4]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][3]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][3]);
		        if(isChosen[2]==false)choice3.removeItem(choiceitem[2][3]);
		      //  choice4.removeItemListener(this);
		        selitemExist[3]=false;
		        itemExist[3]=false;
		        isChosen[3]=true;
		        choicecount++;
		        stAnswer[3]=choiceitem[3][3];
		}
		
		else if((e.getSource()==choice4)&&(choice4.getSelectedItem()==choiceitem[3][4])&&(itemExist[4]==true)){
			if(selitemExist[3]==true)choice4.removeItem(choiceitem[3][0]);
			if(itemExist[1]==true)choice4.removeItem(choiceitem[3][1]);
			if(itemExist[2]==true)choice4.removeItem(choiceitem[3][2]);
			if(itemExist[3]==true)choice4.removeItem(choiceitem[3][3]);
			if(isChosen[0]==false)choice1.removeItem(choiceitem[0][4]);
			if(isChosen[1]==false)choice2.removeItem(choiceitem[1][4]);
		        if(isChosen[2]==false)choice3.removeItem(choiceitem[2][4]);
		      //  choice4.removeItemListener(this);
		        selitemExist[3]=false;
		        itemExist[4]=false;
		        isChosen[3]=true;
		        choicecount++;  
		        stAnswer[3]=choiceitem[3][4];
		}
	
	else if(e.getSource()==resetButton)
	{       
		
		choice1.removeAllItems();
		choice2.removeAllItems();
		choice3.removeAllItems();
		choice4.removeAllItems(); 
		
		
		
		for(int i=0;i<5;i++){
			choice1.addItem(choiceitem[0][i]);
			choice2.addItem(choiceitem[1][i]);
			choice3.addItem(choiceitem[2][i]);
			choice4.addItem(choiceitem[3][i]);
		};
		
		for(int i=0;i<4;i++){
			stAnswer[i]=null;
			isChosen[i]=false;
			selitemExist[i]=true;
		};
		
		for(int i=0;i<5;i++){
			itemExist[i]=true;
		};
		
		choice1.addActionListener(this);
		choice2.addActionListener(this);
		choice3.addActionListener(this);
		choice4.addActionListener(this);
        	
        }
        
        else if(e.getSource()==submitButton){
        	
		if((stAnswer[0]!=stAnswer[1])&&(stAnswer[0]!=stAnswer[2])&&(stAnswer[0]!=stAnswer[3])
		&&(stAnswer[1]!=stAnswer[2])&&(stAnswer[1]!=stAnswer[3])&&(stAnswer[2]!=stAnswer[3]))
		{isvisible=true;
		for(int i=0;i<4;i++){
			if ((stAnswer[i].compareTo(getValue(plotr[i]))==0)|stAnswer[i].compareTo(" "+getValue(plotr[i]))==0|stAnswer[i].compareTo("  "+getValue(plotr[i]))==0)
			resultcolor[i]=Color.blue;
			else resultcolor[i]=Color.magenta;
		}
		isvisible=true;
		
		
		trialCount=trialCount+1;
		totalScore=trialCount*100;
		for(int i=0;i<4;i++){
			
			if((stAnswer[i].compareTo(getValue(plotr[i]))==0)|stAnswer[i].compareTo(" "+getValue(plotr[i]))==0|stAnswer[i].compareTo("  "+getValue(plotr[i]))==0)
			{
			stScore=stScore+25;
			
		        }
		};
		infoPanel.show=isvisible;
		infoPanel.stScore=stScore;
        infoPanel.totalScore=totalScore;
        infoPanel.rsColor1=resultcolor[0];
        infoPanel.rsColor2=resultcolor[1];
        infoPanel.rsColor3=resultcolor[2];
        infoPanel.rsColor4=resultcolor[3];
        infoPanel.repaint();
		
		
		submitButton.setEnabled(false);
		resetButton.setEnabled(false);
		newtrialButton.setEnabled(true);
               }
               
        }
        else if(e.getSource()==newtrialButton){
        	resetButton.setEnabled(true);
        	submitButton.setEnabled(true);
        	newtrialButton.setEnabled(false);
        	plotinit();
        	mPanel.x=plotx[0];
        	mPanel.y=ploty[0];
        	mPanel.plotname="plot 1";
        	mPanel.repaint();
        	plotpanel1.x=plotx[0];
        	plotpanel1.y=ploty[0];
        	plotpanel1.border=true;
        	plotpanel1.repaint();
        	plotpanel2.x=plotx[1];
        	plotpanel2.y=ploty[1];
        	plotpanel2.border=false;
        	plotpanel2.repaint();
        	plotpanel3.x=plotx[2];
        	plotpanel3.y=ploty[2];
        	plotpanel3.border=false;
        	plotpanel3.repaint();
        	plotpanel4.x=plotx[3];
        	plotpanel4.y=ploty[3];
        	plotpanel4.border=false;
        	plotpanel4.repaint();
        	
        	for(int i=0;i<4;i++){
			stAnswer[i]=null;
			isChosen[i]=false;
			selitemExist[i]=true;
		};
		
		for(int i=0;i<5;i++){
			itemExist[i]=true;
		};
		
		correlation();
		for(int i=0;i<4;i++){
                	choiceitem[i][0]="    select ";
                	choiceitem[i][1]=" "+corr[0];
                	choiceitem[i][2]=" "+corr[1];
                	choiceitem[i][3]=" "+corr[2];
                	choiceitem[i][4]=" "+corr[3];
                	
                };
                
		choice1.removeAllItems();
		choice2.removeAllItems();
		choice3.removeAllItems();
		choice4.removeAllItems();
		
                
                
                for(int i=0;i<5;i++){
                	choice1.addItem(choiceitem[0][i]);
                	choice2.addItem(choiceitem[1][i]);
                	choice3.addItem(choiceitem[2][i]);
                	choice4.addItem(choiceitem[3][i]);
                	
                }; 
        	
        	choice1.addActionListener(this);
        	choice2.addActionListener(this);
        	choice3.addActionListener(this);
        	choice4.addActionListener(this);
        	
        	isvisible=false;
		infoPanel.show=isvisible;
		infoPanel.stScore=stScore;
                infoPanel.totalScore=totalScore;
                infoPanel.plotr1=getValue(plotr[0]);
                infoPanel.plotr2=getValue(plotr[1]);
                infoPanel.plotr3=getValue(plotr[2]);
                infoPanel.plotr4=getValue(plotr[3]);
                infoPanel.rsColor1=resultcolor[0];
                infoPanel.rsColor2=resultcolor[1];
                infoPanel.rsColor3=resultcolor[2];
                infoPanel.rsColor4=resultcolor[3];
                infoPanel.repaint();
        };
  
}//end of actionPerformed
        	      	
			      
}	//end of class
        	


class CorrePanel extends JPanel{
	
	
	final int MAX_NUMBER = 100;
        public int width;
        public int height;
        public int DIMENSION_MAX;
        public int[] x;
        public int[] y;
        public int size;
        public boolean border;
        public String plotname;
        public CorrePanel(int width, int height,int DIMENSION_MAX,
        int[] x,int[] y,int size, boolean border,String plotname)
{       this.width=width;
        this.height=height;
        this.DIMENSION_MAX=DIMENSION_MAX;
        this.x=x;
        this.y=y;
        this.size=size;
        this.border=border;  
        this.plotname=plotname;
}


public void paintComponent(Graphics g){
	super.paintComponent(g);
	
        if (border==true){
		g.setColor(Color.black);
		g.fillRect(0,0,width+12,height+12);
		
	}
	else{
	    g.setColor(Color.lightGray);
	    g.fillRect(0,0,width+12,height+12);
	};
	
	
	
	g.setColor(Color.white);
	g.fillRect(2,2,	width+8,height+8);
	g.setColor(Color.black);
	g.drawString(plotname,width/2-10,15);
	
	
	for (int i=0;i<MAX_NUMBER;i++){
		g.drawOval(x[i]*(DIMENSION_MAX/85),y[i]*(DIMENSION_MAX/85),size,size);
        }
      
}// end of paintComponent
	
}//end of Class corrPanel


class InfoPanel extends JPanel{
	
        public int stScore;
        public int totalScore;
        public String plotr1,plotr2,plotr3,plotr4;
        Color rsColor1,rsColor2,rsColor3,rsColor4;
        public boolean show;
        
        public InfoPanel(boolean show,String plotr1,String plotr2,String plotr3,
        String plotr4, Color rsColor1, Color rsColor2, Color rsColor3, Color rsColor4,
        int totalScore, int stScore)
{         
        this.stScore=stScore;
        this.totalScore=totalScore;
        this.plotr1=plotr1;
        this.plotr2=plotr2;
        this.plotr3=plotr3;
        this.plotr4=plotr4;
        this.rsColor1=rsColor1;
        this.rsColor2=rsColor2;
        this.rsColor3=rsColor3;
        this.rsColor4=rsColor4;
        this.show=show;
        
        
}


public void paintComponent(Graphics g){
        this.setSize(210,75);
        super.paintComponent(g);
        g.setFont(new Font("info",Font.BOLD,11));
        g.setColor(Color.lightGray);
        g.fillRect(0,0,309,74);
        if(show==false){
        g.setColor(Color.black);
        g.drawString("Please choose one  r  for",10,10);
        g.drawString("each plot from the ' select '",10,30);
        g.drawString("menu.",10,50);
        }
        else if(show==true){
        g.setColor(rsColor1);
        g.drawString("plot 1:    "+plotr1,10,10);
        g.setColor(rsColor3);
        g.drawString("plot 3:    "+plotr3,120,10);
        g.setColor(rsColor2);
        g.drawString("plot 2:    "+plotr2,10,30);
        g.setColor(rsColor4);
        g.drawString("plot 4:    "+plotr4,120,30);
        g.setColor(Color.black);
        g.drawString("Total score:   "+totalScore,10,50);
        g.drawString("Your Score:   "+stScore,120,50); 
        }; 
        
        
	
}

}



        	
                
