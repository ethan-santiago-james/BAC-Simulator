import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class MainGUI extends JPanel {

   JLabel title,question;
   JLabel[] questions = {new JLabel("How many drinks did you have?"),new JLabel("Over how long? (i.e 1:30 would be 1 and a half hours)"),new JLabel("Average drink size(ml)?"),
   new JLabel("Avg alcohol percentage per drink"),new JLabel("Body weight(kg)?"),new JLabel("Dinner size prior? (Enter either Small, Medium, or Large)"),new JLabel("Simulation Speed (i.e 2x speed)")};
   
   JTextField[] answers = {new JTextField(10),new JTextField(10),new JTextField(10),new JTextField(10),new JTextField(10),new JTextField(10),new JTextField(10)};
   
   JButton begin,submit;
   JPanel frontScreen;
   JLabel errorMessage;
   BufferedImage background;
   
   public static int page = 1;
   public ButtonListener bL = new ButtonListener();
   
   public MainGUI() {
   
      try {
      
         background = ImageIO.read(getClass().getResourceAsStream("beer.png"));
         
      } catch(IOException e) {}
      
      errorMessage = new JLabel("");
      errorMessage.setForeground(Color.red);
     
      this.setPreferredSize(new Dimension(600,600));
   
      frontScreen = new JPanel();
      frontScreen.setLayout(new GridLayout(3,1));
      
      title = new JLabel("Welcome To Drunk Simulator");
      title.setHorizontalAlignment(SwingConstants.CENTER);
      
      begin = new JButton("Begin");
      begin.setPreferredSize(new Dimension(150,50));
      begin.setBackground(Color.cyan);
      begin.setOpaque(true);
      
      begin.setHorizontalAlignment(SwingConstants.CENTER);
      begin.addActionListener(bL);
      
      frontScreen.add(title);
      frontScreen.add(new JLabel(""));
      frontScreen.add(begin);
      
      
      add(frontScreen);
      frontScreen.setVisible(true);
   }
   
   public void paintComponent(Graphics g) {

      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      if(page == 1) {
      
         g2.drawImage(background,0,80,550,600,null);
      
      }
   
   }
   
   public static void checkDinnerEntry(String entry) throws DinnerSizeException {
   
      if(!entry.equals("Small") && !entry.equals("Medium") && !entry.equals("Large") && !entry.equals("None")) {
      
         throw new DinnerSizeException("Must enter either Small, Medium, Large, or None!");
         
      }
   
   }
   
   public void loadQuestionScreen() {
   
      page = 2;
      repaint();
      frontScreen.setVisible(false);
      JPanel questionScreen = new JPanel();
      
      questionScreen.setLayout(new GridLayout(18,1));
      
      for (JTextField t: answers) {
      
         t.setHorizontalAlignment(SwingConstants.CENTER);
      
      }
      
      for (JLabel l: questions) {
      
         l.setHorizontalAlignment(SwingConstants.CENTER);
      
      }
      
      for (int i = 0; i < questions.length; i++) {
      
         questionScreen.add(questions[i]);
         questionScreen.add(answers[i]);
      
      }
      
      submit = new JButton("Submit");
      submit.addActionListener(bL);
      submit.setBackground(Color.green);
      submit.setOpaque(true);
      submit.setHorizontalAlignment(SwingConstants.CENTER);
      
      questionScreen.add(submit);
      questionScreen.add(errorMessage);
      
      add(questionScreen);
      questionScreen.setVisible(true);
      
      
      
   }
   
   public static int timeToMinutes(String t) {
   
      int numMinutes = 0;
      
      if(t.contains(":") && !t.contains(".")) {
      
         int colonIndex = t.indexOf(":");
         
         String hours = t.substring(0,colonIndex);
         
         String minutes = t.substring(colonIndex + 1);
         
         try {
         
            int H = Integer.valueOf(hours);
            int M = Integer.valueOf(minutes);
            
            numMinutes += (60*H);
            numMinutes += M;
            return numMinutes;
         } catch(InputMismatchException e) {
         
            return -1;
         } catch(NoSuchElementException n) {
         
            return -1;
         } catch(NumberFormatException n) {
         
            return -1;
         }
         
      }
      
      return -1;
   
   }
   
   
   private class ButtonListener implements ActionListener {
   
      public void actionPerformed(ActionEvent ae) {
      
         JButton b = (JButton)ae.getSource();
         
         if(b == begin) {
         
            loadQuestionScreen();
            
         } else if(b == submit) {
         
            int numDrinks = 0;
            String time = "";
            int drinkML = 0;
            double percentAlcohol = 0;
            double bodyWeight = 0;
            String dinnerSize = "";
            double simSpeed = 0;
            
            try {
            
               numDrinks = Integer.valueOf(answers[0].getText());
               
               time = answers[1].getText();
               int timePeriod = timeToMinutes(time);
               
               if(timePeriod > -1) {
               
                  drinkML = Integer.valueOf(answers[2].getText());
                  
                  percentAlcohol = Double.valueOf(answers[3].getText());
                  
                  bodyWeight = Double.valueOf(answers[4].getText());
                  
                  dinnerSize = answers[5].getText();
                  checkDinnerEntry(dinnerSize);
                  
                  simSpeed = Double.valueOf(answers[6].getText());
                  
                  DrunkSimulator.loadSimulation(numDrinks,timePeriod,drinkML,percentAlcohol,
                  bodyWeight,dinnerSize,simSpeed);
               
               
               }
            
            } catch(InputMismatchException e) {
            
               errorMessage.setText("Invalid input somewhere");
            
            } catch(NoSuchElementException e) {
            
               errorMessage.setText("Some fields are empty");
            
            } catch(NumberFormatException n) {
            
               errorMessage.setText("Invalid number format somewhere");
            
            } catch(DinnerSizeException d) {
            
               errorMessage.setText("Either Small, Medium, Large or None must be entered for dinner");
            }
            
            
         
         }
         
      
      }
   
   }

}