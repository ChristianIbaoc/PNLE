import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PNLE {

    JPanel questionPanel;
    JPanel simPanel;
    JPanel currentStatusPanel = new JPanel();
    JLabel overallScore;
    int corrects=0;
    int total = 0;
    float overall = 0;
    Font font;

    PNLE()
    {
        font = new Font("Arial", 0,20);
        ActionListener ad = null;
        //Create objects and paths
        String path = new File("").getAbsolutePath();
        File questionsFolder = new File(path+"/questions");
        File answersFolder = new File(path+"/answers");

        File[] questions = questionsFolder.listFiles();
        File[] answers = answersFolder.listFiles();

        //System.out.println(questions[0].getName());
        //System.out.println(ZonedDateTime.of(LocalDateTime.now(),ZoneId.systemDefault()).toInstant().toEpochMilli());
        JButton hideshow = new JButton("Hide");
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
                if(e.getActionCommand()=="hide")
                {
                    if(overallScore.isVisible())
                    {
                        overallScore.setVisible(false); hideshow.setText("Show");
                    }
                    else
                    {
                        overallScore.setVisible(true);  hideshow.setText("Hide");
                    }
                }
                if(e.getActionCommand()=="Banana")
                {
                    //System.out.println("ababa");
                    //start.setVisible(false);

                    CardLayout c = (CardLayout)(questionPanel.getLayout());
                    c.show(questionPanel, "card1");

                    questionPanel.setVisible(true);
                    questionPanel.setEnabled(true);
                    questionPanel.repaint();


                }
                try
                {

                    String[] params = e.getActionCommand().split("\\*");
                    File[] questions = new File(params[2]).listFiles();
                    File[] answers = new File(params[3]).listFiles();
                
                    if(params[0].compareTo("SECTION")==0)
                    {
                        
                        //System.out.println("dwaghueros");
                        questionPanel.removeAll();
                        questionPanel=loadContents(questionPanel, params[1],questions,answers);
                        questionPanel.repaint();
                        //start.setText("START " + params[1]);
                        //start.setVisible(true);
                        //start.setEnabled(true);
                        //start.repaint();
                    }
                } catch (ArrayIndexOutOfBoundsException x){}
            }
            
        };
        //start.addActionListener(ad);
        //add ribbon options
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx=1.0;
        c.weighty=0.2;

        for(int x = 0; x < questions.length; x++)
        {
            if(x+1==questions.length)
                c.gridwidth=GridBagConstraints.REMAINDER;
            JButton b = new JButton("<html><center>"+questions[x].getName()+"</center></html>");
            b.setActionCommand("SECTION*"+ b.getText() + "*" + questions[x].getAbsolutePath() + "*" + answers[x].getAbsolutePath());
            
            b.addActionListener(ad);
            mainPanel.add(b,c);
        }
        c.weighty=1;
        //mainPanel.add(start,c);
        mainPanel.add(questionPanel,c);
        c.weighty=0.05;

        hideshow.setActionCommand("hide");
        hideshow.addActionListener(ad);
        overallScore = new JLabel("Correct Answers / Total Answers: " + corrects +" / "+ total + "(" + overall + "%)");
        currentStatusPanel.add(overallScore);
        currentStatusPanel.add(hideshow);
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
        shuffleArr(questions, answers);
        CardLayout c = new CardLayout();
        JPanel simPanel = new JPanel();
        simPanel.setBackground(Color.decode("#ece7d5"));
        GridBagConstraints gbc = new GridBagConstraints();
        simPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx=1;
        gbc.gridwidth=3;
        gbc.insets = new Insets(10, 50, 10, 50);
        questionPanel.setLayout(c);
        int counter = 0;
        int card = 1;

        String[] ops = {"A","B","C","D"};

        ActionListener ad = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(e.getActionCommand().equals("prev"))
                {
                    //System.out.println("Prevcard");
                    c.previous(questionPanel);
                }
                    
                if(e.getActionCommand().equals("next"))
                {
                    //System.out.println("nextcard");
                    c.next(questionPanel);
                }
                if(e.getActionCommand().equals("next25"))
                {
                    for(int i =0;i<25;i++)
                    {
                        c.next(questionPanel);
                    }
                }
                if(e.getActionCommand().equals("prev25"))
                {
                    for(int i =0;i<25;i++)
                    {
                        c.previous(questionPanel);
                    }
                }
                    
            }
            
        };

        for(int x = 0; x<questions.length;x++)
        {
            try 
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(questions[x]), StandardCharsets.UTF_8));
                BufferedReader abr = new BufferedReader(new InputStreamReader(new FileInputStream(answers[x]), StandardCharsets.UTF_8));
                String line;
                
                while ((line = br.readLine()) != null)
                {
                    if(line.trim().isEmpty())
                        continue;
                    JLabel label = new JLabel();
                    label.setFont(font);
                    JComboBox jop = new JComboBox<>(ops);
                    jop.setActionCommand("jop");
                    jop.addActionListener(ad);
                    switch (counter) {
                        case 0:
                            label.setText("<html><body style='width: 900px'>" + line + "</body></html>");
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
                    gbc.gridx=0;
                    gbc.gridwidth=6;
                    simPanel.add(label,gbc);
                    if(counter==4)
                    {
                        String raw="";
                        String letter="";
                        String ratio="";
                        try {
                            raw = abr.readLine();
                            if (raw.startsWith("Answer:") || raw.startsWith("(")) {
                                // Format: Answer: (C) explanation...
                                int start = raw.indexOf("(") + 1;
                                int end = raw.indexOf(")");
                                letter = raw.substring(start, end);
                                ratio = raw.substring(end + 2);  // Skip ") "
                            } else {
                                // Format: D. statement...
                                letter = raw.substring(0, raw.indexOf(".")).trim();
                                ratio = raw.substring(raw.indexOf(".") + 1).trim();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            System.out.println(e.getLocalizedMessage());
                            System.out.println("X : " + x);
                            System.out.println(raw);
                            System.out.println(line);
                        } 
                        String[] ans = {letter,ratio};
                        //System.out.println(ans[0]);
                        //System.out.println(ans[1]);
                        
                        gbc.gridy=5;
                        gbc.gridx=2;
                        gbc.gridwidth=2;
                        simPanel.add(jop,gbc);

                        JButton prev = new JButton("Previous");
                        JButton prev25 = new JButton("<< 25");
                        prev25.setFont(font);
                        prev.setFont(font);
                        JButton next = new JButton("Next");
                        JButton next25 = new JButton(">> 25");
                        next.setFont(font);
                        next25.setFont(font);
                        JButton check = new JButton("Check");
                        check.setFont(font);
                        JLabel ansLabel = new JLabel("<html><body style='width: 800px'>" + ans[0]+ " : " +ans[1] + "</body></html>");
                        ansLabel.setFont(font);
                        ansLabel.setVisible(false);

                        prev.setActionCommand("prev");
                        prev25.setActionCommand("prev25");
                        next.setActionCommand("next");
                        next25.setActionCommand("next25");
                        check.setActionCommand("check");
                        
                        prev.addActionListener(ad);
                        next.addActionListener(ad);
                        prev25.addActionListener(ad);
                        next25.addActionListener(ad);
                        check.addActionListener(new ActionListener() 
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                if(e.getActionCommand().equals("check"))
                                {
                                    //System.out.println("checkansw");
                                    ((JButton)e.getSource()).setEnabled(false);
                                    //System.out.println(jop.getSelectedItem().toString());
                                    if(jop.getSelectedItem().toString().compareTo(ans[0])==0)
                                    {
                                        //System.out.println(ans[0]);
                                        //System.out.println("Correct");
                                        corrects++;
                                    }
                                    total++;
                                    overall=((float)corrects/total)*100;
                                    overallScore.setText("Correct Answers / Total Answers: " + corrects +" / "+ total + "(" + overall + "%)");
                                    overallScore.repaint();
                                    currentStatusPanel.revalidate();
                                    currentStatusPanel.repaint();
                                    jop.setEnabled(false);
                                    ansLabel.setVisible(true);
                                }
                            }
                        });

                        

                        gbc.gridy=6;
                        gbc.gridwidth=1;
                        gbc.gridx=0;
                        prev.setVisible(true);
                        prev.setEnabled(true);
                        gbc.weightx=0.8;
                        simPanel.add(prev25,gbc);
                        gbc.gridx=1;
                        simPanel.add(prev,gbc);
                        gbc.gridx=2;
                        gbc.weightx=1;
                        simPanel.add(check,gbc);
                        gbc.gridx=4;
                        gbc.weightx=0.5;
                        simPanel.add(next,gbc);
                        gbc.gridx=5;
                        simPanel.add(next25,gbc);
                        
                        gbc.gridx=0;
                        gbc.gridwidth=6;
                        gbc.gridy=8;

                        simPanel.add(ansLabel,gbc);
                        gbc.gridx=0;
                        simPanel.revalidate();
                        simPanel.repaint();
                        questionPanel.add(simPanel,"card"+card);
                        simPanel = new JPanel(new GridBagLayout());
                        simPanel.setBackground(Color.decode("#ece7d5"));
                        counter=0;
                        card++;
                    }
                    else
                        counter++;        
                }
                br.close();
                abr.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getLocalizedMessage());
                System.out.println("I was activated");
            }
        }

        return questionPanel;
    }

    void shuffleArr(File[] arr, File[] arr2)
    {
            Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            File a = arr[index];
            File b = arr2[index];
            arr[index] = arr[i];
            arr2[index] = arr2[i];
            arr[i] = a;
            arr2[i] = b;
        }
    }
}
