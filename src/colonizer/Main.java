package colonizer;

import colonizer.enums.HexagonType;
import colonizer.map.Intersection;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main (String[] args) {
        GameTable t = new GameTable ();
        t.generateTestMap ();
        t.draw ();
        t.startGame ();

        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        String command = "habzsibabzsi";

        while (!command.equals ("x")) {
            try {
                command = br.readLine ();
                String[] splittedCommand = command.split (" ");

                if (command.startsWith ("roll") && t.isAvailableCommand ("roll")) {
                    int dice1 = (int) (Math.random () * 6) + 1;
                    int dice2 = (int) (Math.random () * 6) + 1;
                    t.rollDice (dice1 + dice2);

                    System.out.println (t.getDiceString (dice1, dice2));
                } else if (command.startsWith ("end") && t.isAvailableCommand ("end")) {
                    t.nextTurn ();
                } else if (command.startsWith ("pick") && t.isAvailableCommand ("pick")) {
                        Intersection intersection = t.parseIntersectionString (splittedCommand[1]);
                        t.getCurrentPlayer ().pickStartingVillage (intersection);
                        t.nextPickTurn ();
                } else if (command.startsWith ("build") && t.isAvailableCommand ("build")) {
                    if (splittedCommand[1].equals ("village")) {
                        Intersection intersection = t.parseIntersectionString (splittedCommand[2]);
                        t.getCurrentPlayer ().buildVillage (intersection);
                    } else if (splittedCommand[1].equals ("city")) {
                        Intersection intersection = t.parseIntersectionString (splittedCommand[2]);
                        t.getCurrentPlayer ().buildCity (intersection);
                    } else if (splittedCommand[1].equals ("road")) {

                    }
                } else if (command.startsWith ("info")) {
                    System.out.print ("n/r\t|");
                    for (int i = 0; i < 5; i++) {
                        System.out.print ("| " + HexagonType.values ()[i].toString ().substring (0, 2) + " |\t");
                    }
                    System.out.print ("|");
                    System.out.println ();

                    for (int j = 2; j < 13; j++) {

                        System.out.print (j + "\t|");
                        for (int i = 0; i < 5; i++) {
                            System.out.printf ("| %2d |\t", t.getCurrentPlayer ().resourceGainingNumbers[j][i]);
                        }
                        System.out.print ("|");
                        System.out.println ();
                    }

                } else if (command.startsWith ("res")) {
                    for (int i = 0; i < 5; i++) {
                        System.out.println (HexagonType.values ()[i].toString () + ":" + t.getCurrentPlayer ().resources[i]);
                    }
                } else if (command.startsWith ("gain")) {
                    for (int i = 0; i < 5; i++) {
                        System.out.println (HexagonType.values ()[i].toString () + " GAIN: " + t.getCurrentPlayer ().resourceGain[i]);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace ();
            }
        }

    }
}
