import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class SimulationWindow extends JPanel implements Runnable {

   private int numDrinks;
   private int mins;
   private double drinkML;
   private double alcPercent;
   private double weight;
   private int dinnerML;
   private double simSpeed;
   
   private int milliSecondUpdate;
   
   // LIVE DATA
   private int drinksConsumed;
   private int minutesPerDrink;
   private double millilitresConsumedPerMinute;
   public int minutesPassed;
   private String status = "Sober";
   BufferedImage statusImage;
   
   
   Thread gameThread;
   
   // RATES
   double beta = 0.2;
   static double alpha = 2.5;
   double gamma = 0.0044;
   
   // COMPARTMENTS
   public static double alcoholInStomach = 0;
   public static double stomachVolume = 0;
   public static double alcoholInBloodStream = 0;
   public static double BAC = 0;
   
   //PEAKS
   public static double peakBAC = 0;
   public static String peakMinutesPassed;
   public static String peakStatus;
   
   //ERROR CHECKING
   public static boolean error = false;
   

   public SimulationWindow(int numDrinks,int mins,double drinkML,
   double alcPercent,double weight,int dinnerML,double simSpeed) {
   
      this.setPreferredSize(new Dimension(600,600));
      this.numDrinks = numDrinks;
      this.mins = mins;
      this.alcPercent = alcPercent;
      this.weight = weight;
      this.dinnerML = dinnerML;
      this.simSpeed = simSpeed;
      
      this.milliSecondUpdate = (int)(1000/(this.simSpeed));
      stomachVolume += (dinnerML);
      
      if(this.numDrinks > 0) {
      
         minutesPerDrink = (int)(mins/numDrinks);
         
      } else {
      
         minutesPerDrink = 0;
         
      }
      
      drinksConsumed = 0;
      
      this.alcPercent *= 0.01;
      
      millilitresConsumedPerMinute = (this.alcPercent)*((double)numDrinks*drinkML)/(double)mins;
      
      try {
      
         statusImage = ImageIO.read(getClass().getResourceAsStream("stages/sober.png"));
         
      } catch(IOException e) {}
      
   }
   
   public void startGameThread() {
   
      gameThread = new Thread(this);
      gameThread.start();
   
   }
   
   @Override
   public void run() {
   
      while(gameThread != null) {
      
         try {

            TimeUnit.MILLISECONDS.sleep(this.milliSecondUpdate);
            repaint();
            updateReccurrence();
            updateStatus();
            minutesPassed++;
            
            if(drinksConsumed >= (this.numDrinks)) {
            
               millilitresConsumedPerMinute = 0;
            
            }
         
         } catch(InterruptedException e) {}
      
      }
   
   }

   
   public static String minsToHours(int mins) {
   
      int MINUTES = (mins%60);
      
      int HOURS = (mins - MINUTES)/60;
      
      String hourTerminology = "";
      String minuteTerminology = "";
      
      if(HOURS == 1) {
      
         hourTerminology = "hour";
         
      } else {
      
         hourTerminology = "hours";
      
      }
      
      if(MINUTES == 1) {
      
         minuteTerminology = "minute";
         
      } else {
      
         minuteTerminology = "minutes";
      
      }
      
      if(HOURS < 0) {
      
         return MINUTES + " " + minuteTerminology + ".";
      
      } else {
      
         return HOURS + " " + hourTerminology + ", and " + MINUTES + " " + minuteTerminology + ".";
      
      }
   
   }
   
   public void paintComponent(Graphics g) {
   
     if(error == false) {
     
        g.setColor(Color.white);
        g.fillRect(0,0,600,600);
        
        String time = minsToHours(minutesPassed);
        
        g.setColor(Color.black);
        g.drawString("Time: " + time,5,200);
        g.drawString("BAC: " + BAC + " grams of alcohol per 100ml of blood",5,250);
        g.drawString("Status: " + this.status,5,300);
        g.drawString("Drinks Consumed: " + drinksConsumed,5,350);
        g.drawString("Alcohol in Blood Stream: " + alcoholInBloodStream + "g",5,400);
        g.drawString("Alcohol in Stomach: " + alcoholInStomach + "g",5,450);
        g.drawString("Stomach Volume: " + stomachVolume + "g",5,500);
        g.drawString("Peak BAC, and Status: " + peakBAC + ", " + peakStatus + ") at " + peakMinutesPassed,5,550);
        
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawImage(this.statusImage,400,0,200,200,null);
     
     } else {
     
        drawError(g);
     }
   
   }
   
   public void drawError(Graphics g) {
   
      g.setColor(Color.red);
      g.setFont(new Font("TimesRoman",Font.BOLD,30));
      
      g.drawString("Too many drinks entered for the given timeframe!",0,300);
   
   }
   
   public void updateStatus() {
   
      if(this.status.equals("Likely Dead")) {
      
         this.status = "Likely Dead";
         
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/dead.jpg"));
            
         } catch(IOException e) {}
      
      } else if(BAC < 0.01) {
      
         this.status = "Sober";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/sober.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.02) {
      
         this.status = "Buzzed";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/buzzed.png"));
            
         } catch(IOException e) {}
      
      } else if(BAC < 0.04) {
      
         this.status = "Tipsy";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/tipsy.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.06) {
      
         this.status = "Drunk";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/drunk.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.08) {
      
         this.status = "Legal Limit";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/legallimit.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.1) {
      
         this.status = "Vomity";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/vomity.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.12) {
      
         this.status = "Legless";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/legless.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.18) {
      
         this.status = "Potential Memory Loss";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/memoryloss.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.3) {
      
         this.status = "Severe confusion, motor control, and emotional behaviour";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/emotional.png"));
            
         } catch(IOException e) {}
         
      } else if(BAC < 0.4) {
      
         this.status = "Unconscious";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/unconscious.jpg"));
            
         } catch(IOException e) {}
         
      } else {
      
         this.status = "Likely Dead";
         try {
         
            statusImage = ImageIO.read(getClass().getResourceAsStream("stages/dead.jpg"));
            
         } catch(IOException e) {}
      
      }
      
   }
   
   
   public void updateReccurrence() {
      
         double currAlcInS = alcoholInStomach;
         double currSV = stomachVolume;
         double currAIBS = alcoholInBloodStream;
         BAC = currAIBS/(10*(0.65*(this.weight)));
         double initBAC = BAC;
         
         if(currAlcInS < 0) {
         
            currAlcInS = 0;
         } else if(currSV < 0) {
         
            currSV = 0;
            
         } else if(currAIBS < 0) {
         
            currAIBS = 0;
         }
         
         alcoholInStomach += (millilitresConsumedPerMinute - (alpha*(currAlcInS/(currSV + 0.08))));
         alcoholInBloodStream += ((alpha*(currAlcInS/(currSV + 0.08)) - Math.min(currAIBS,beta + gamma*(currAIBS))));
         stomachVolume += (millilitresConsumedPerMinute - Math.min(currSV,alpha));
         BAC = alcoholInBloodStream/(10*(0.65*(this.weight)));
         
         if(BAC <= 0 && BAC < initBAC) {
         
            gameThread = null;
            
         } else if(BAC > peakBAC) {
            
            peakBAC = BAC;
            peakMinutesPassed = minsToHours(minutesPassed);
            peakStatus = this.status;
         
         }
         
         try {
         
            if(minutesPassed == minutesPerDrink) {
            
               drinksConsumed = 1;
            
            } else if(minutesPassed%minutesPerDrink == 0 && (drinksConsumed < (this.numDrinks) && (drinksConsumed > 0))) {
            
               drinksConsumed++;
            
            }
            
         } catch(ArithmeticException e) {
         
            error = true;
         }
      }

}
