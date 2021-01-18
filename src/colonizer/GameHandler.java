package colonizer;

public class GameHandler {



    public void startGame() {

    }

//    public void gameCommand(String command) {
//        try {
//            String[] splittedCommand = command.split (" ");
//
//            if (command.startsWith ("roll")) {
//                int dice1 = (int) (Math.random () * 6) + 1;
//                int dice2 = (int) (Math.random () * 6) + 1;
//                t.rollDice (dice1 + dice2);
//
//                System.out.println (t.getDiceString (dice1, dice2));
//            } else if (command.startsWith ("end")) {
//                t.nextTurn ();
//            } else if (command.startsWith ("pick")) {
//                Intersection intersection = t.parseIntersectionString (splittedCommand[1]);
//                t.getCurrentPlayer ().pickStartingVillage (intersection);
//                t.nextPickTurn ();
//            } else if (command.startsWith ("build")) {
//                if (splittedCommand[1].equals ("village")) {
//                    Intersection intersection = t.parseIntersectionString (splittedCommand[2]);
//                    t.getCurrentPlayer ().buildVillage (intersection);
//                } else if (splittedCommand[1].equals ("city")) {
//                    Intersection intersection = t.parseIntersectionString (splittedCommand[2]);
//                    t.getCurrentPlayer ().buildCity (intersection);
//                } else if (splittedCommand[1].equals ("road")) {
//
//                }
//            } else if (command.startsWith ("info")) {
//                System.out.print ("n/r\t|");
//                for (int i = 0; i < 5; i++) {
//                    System.out.print ("| " + HexagonType.values ()[i].toString ().substring (0, 2) + " |\t");
//                }
//                System.out.print ("|");
//                System.out.println ();
//
//                for (int j = 2; j < 13; j++) {
//
//                    System.out.print (j + "\t|");
//                    for (int i = 0; i < 5; i++) {
//                        System.out.printf ("| %2d |\t", t.getCurrentPlayer ().resourceGainingNumbers[j][i]);
//                    }
//                    System.out.print ("|");
//                    System.out.println ();
//                }
//
//            } else if (command.startsWith ("res")) {
//                for (int i = 0; i < 5; i++) {
//                    System.out.println (HexagonType.values ()[i].toString () + ":" + t.getCurrentPlayer ().resources[i]);
//                }
//            } else if (command.startsWith ("gain")) {
//                for (int i = 0; i < 5; i++) {
//                    System.out.println (HexagonType.values ()[i].toString () + " GAIN: " + t.getCurrentPlayer ().resourceGain[i]);
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace ();
//        }
//    }
}
