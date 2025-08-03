import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Random;
import java.util.Set;
import java.util.random.*;
import java.time.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PNLE {

    JPanel questionPanel;
    PNLE()
    {
        ActionListener ad = null;
        //Create objects and paths
        String path = new File("").getAbsolutePath();
        File questionsFolder = new File(path+"/questions");
        File answersFolder = new File(path+"/answers");
        FileSystem fs = FileSystems.getDefault();
        RandomGenerator rand = new Random(ZonedDateTime.of(LocalDateTime.now(),ZoneId.systemDefault()).toInstant().toEpochMilli());
        JButton start = new JButton("SELECT CLASS ABOVE");
        start.setActionCommand("Banana");
        start.setVisible(true);
        start.setEnabled(false);
        //Create arrays of files inside questions and answers
        File[] questions = questionsFolder.listFiles();
        File[] answers = answersFolder.listFiles();

        //Determine maximum number of files and generate chosen random number
        int numOfFiles = questions.length;

        System.out.println(questions[0].getName());
        System.out.println(ZonedDateTime.of(LocalDateTime.now(),ZoneId.systemDefault()).toInstant().toEpochMilli());

        JFrame frame = new JFrame("PNLE Exam");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new DimensionUIResource(1270, 720));
        frame.setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#ece7d5"));

        questionPanel = new JPanel();
        questionPanel.setBackground(Color.white);
        //Action Listener
        ad = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(e.getActionCommand()=="Banana")
                {
                    System.out.println("ababa");
                    start.setVisible(false);

                    CardLayout c = (CardLayout)(questionPanel.getLayout());
                    c.show(questionPanel, "card1");

                    questionPanel.setVisible(true);
                    questionPanel.setEnabled(true);
                    questionPanel.repaint();


                }
                try
                {
                    // TODO Auto-generated method stub
                    System.out.println(e.getActionCommand());
                    String[] params = e.getActionCommand().split("\\*");
                    File[] questions = new File(params[2]).listFiles();
                    File[] answers = new File(params[3]).listFiles();
                    System.out.println(params[0]);
                
                    if(params[0].compareTo("SECTION")==0)
                    {
                        
                        System.out.println("dwaghueros");
                        questionPanel=loadContents(questionPanel, params[1],questions,answers);
                        questionPanel.repaint();
                        start.setText("START " + params[1]);
                        start.setVisible(true);
                        start.setEnabled(true);
                        start.repaint();
                    }
                } catch (ArrayIndexOutOfBoundsException x){}
            }
            
        };
        start.addActionListener(ad);
        //add ribbon options
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx=1.0;
        c.weighty=0.1;
        for(int x = 0; x < questions.length; x++)
        {
            if(x+1==questions.length)
                c.gridwidth=GridBagConstraints.REMAINDER;
            JButton b = new JButton(questions[x].getName());
            b.setActionCommand("SECTION*"+ b.getText() + "*" + questions[x].getAbsolutePath() + "*" + answers[x].getAbsolutePath());
            b.addActionListener(ad);
            mainPanel.add(b,c);
        }
        /*
        c.weighty=1;
        JPanel questionPanel = new JPanel();
        questionPanel.setBackground(Color.decode("#ece7d5"));
        CardLayout card = new CardLayout();
        questionPanel.setLayout(card);
        JButton start = new JButton("START");
        start.setPreferredSize(new Dimension(300, 300));
        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==start)
                {
                    System.out.println("START");
                    card.next(questionPanel);
                    start.setVisible(false);
                }
                
            }
            
        });
        */
        c.weighty=1;
        mainPanel.add(start,c);
        mainPanel.add(questionPanel,c);
        c.weighty=0.05;
        JPanel currentStatusPanel = new JPanel();
        currentStatusPanel.add(new JLabel("Current Status: "));
        mainPanel.add(currentStatusPanel,c);

        frame.add(mainPanel);
        mainPanel.setVisible(true);
        frame.setVisible(true);

    }
    public static void main(String[] args)
    {
        PNLE app = new PNLE();

    }

    JPanel loadContents(JPanel questionPanel, String section, File[] questions, File[] answers)
    {
        CardLayout c = new CardLayout();
        JPanel simPanel = new JPanel();
        simPanel.setBackground(Color.decode("#ece7d5"));
        GridBagConstraints gbc = new GridBagConstraints();
        simPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx=0;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        questionPanel.setLayout(c);
        System.out.println(questions[0]);
        int counter = 0;
        int card = 1;

        for(int x = 0; x<questions.length;x++)
        {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(questions[0]), StandardCharsets.UTF_8))) 
            {
                String line;
                
                while ((line = br.readLine()) != null)
                {
                    if(line.compareTo("")==0)
                        continue;
                    System.out.println(line);
                    JLabel label = new JLabel();
                    switch (counter) {
                        case 0:
                            label.setText(line);
                            break;
                        case 1:
                            label.setText("A: "+line);
                            break;
                        case 2:
                            label.setText("B: "+line);
                            break;
                        case 3:
                            label.setText("C: "+line);
                            break;
                        case 4:
                            label.setText("D: "+line);
                            
                            break;
                    }
                    gbc.gridy=counter;
                    simPanel.add(label,gbc);
                    if(counter==4)
                    {
                        JButton prev = new JButton("Previous");
                        JButton next = new JButton("Next");
                        JButton check = new JButton("check");
                        gbc.gridy=5;
                        gbc.gridwidth=1;
                        gbc.gridx=0;
                        prev.setVisible(true);
                        prev.setEnabled(true);
                        gbc.weightx=1;
                        simPanel.add(prev,gbc);
                        gbc.gridx=1;
                        simPanel.add(check,gbc);
                        gbc.gridx=2;
                        simPanel.add(next,gbc);
                        gbc.gridx=0;
                        simPanel.revalidate();
                        simPanel.repaint();
                    }
                    counter++;
                    if(counter==5)
                    {
                        questionPanel.add(simPanel,"card"+card);
                        simPanel = new JPanel(new GridBagLayout());
                        counter=0;
                        card++;
                    }
                        
                }
            }
            catch(Exception e){System.out.println(e.getLocalizedMessage());}
        }

        return questionPanel;
    }
}
