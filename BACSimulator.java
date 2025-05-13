import javax.swing.*;
import java.awt.*;

public class BACSimulator {

   static JFrame frame;
   
   public static void main(String[] args) {
   
      frame = new JFrame("BAC Simulator");
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      
      MainGUI gui = new MainGUI();
      
      frame.getContentPane().add(gui);
      
      frame.pack();
      frame.setVisible(true);
   
   }
   
   public static void loadSimulation(int numDrinks,int mins,double drinkML,
   double alcPercent,double weight,String dinnerSize,double simSpeed) {
   
      int dinnerML = 0;
      
      switch(dinnerSize) {
      
         case("Small"):
            
            dinnerML = 400;
            break;
            
         case("Medium"):
         
            dinnerML = 650;
            break;
            
         case("Large"):
         
            dinnerML = 1000;
            break;
            
         default:
         
            dinnerML = 0;
            break;
            
      
      }
      
      frame.setVisible(false);
      
      JFrame frameTwo = new JFrame("BAC Simulation");
      frameTwo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      
      
      SimulationWindow s = new SimulationWindow(numDrinks,mins,drinkML,alcPercent,weight,
      dinnerML,simSpeed);
      
      frameTwo.getContentPane().add(s);
      
      frameTwo.pack();
      frameTwo.setVisible(true);
      
      s.startGameThread();
   
   }

}
