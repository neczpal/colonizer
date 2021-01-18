package colonizer;

import colonizer.enums.ActionCardType;
import colonizer.enums.HexagonType;
import colonizer.enums.ResourceCardType;
import colonizer.map.Hexagon;
import colonizer.map.Intersection;
import colonizer.map.Position;
import colonizer.map.Road;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main (String[] args) {
        GameTable t = new GameTable ();
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
                    t.getCurrentPlayer().pass();
                } else if (command.startsWith ("pick") && t.isAvailableCommand ("pick")) {
                    Hexagon hex1 = t.parseHexagonString (splittedCommand[1]);
                    Hexagon hex2 = t.parseHexagonString (splittedCommand[2]);
                    Hexagon hex3 = t.parseHexagonString (splittedCommand[3]);


                    Position position = Hexagon.getIntersectionPositionFromHexagons(hex1, hex2, hex3);
                    Intersection intersection = t.parseIntersectionPosition(position);

                    t.getCurrentPlayer ().pickStartingVillage (intersection);
                    t.nextPickTurn ();
                } else if (command.startsWith ("build") && t.isAvailableCommand ("build")) {

                    if (splittedCommand[1].equals ("village")) {
                        Hexagon hex1 = t.parseHexagonString (splittedCommand[2]);
                        Hexagon hex2 = t.parseHexagonString (splittedCommand[3]);
                        Hexagon hex3 = t.parseHexagonString (splittedCommand[4]);


                        Position position = Hexagon.getIntersectionPositionFromHexagons(hex1, hex2, hex3);
                        Intersection intersection = t.parseIntersectionPosition(position);
                        t.getCurrentPlayer ().buildVillage (intersection);
                    } else if (splittedCommand[1].equals ("city")) {
                        Hexagon hex1 = t.parseHexagonString (splittedCommand[2]);
                        Hexagon hex2 = t.parseHexagonString (splittedCommand[3]);
                        Hexagon hex3 = t.parseHexagonString (splittedCommand[4]);


                        Position position = Hexagon.getIntersectionPositionFromHexagons(hex1, hex2, hex3);
                        Intersection intersection = t.parseIntersectionPosition(position);
                        t.getCurrentPlayer ().buildCity (intersection);
                    } else if (splittedCommand[1].equals ("road")) {
                        Hexagon hex1 = t.parseHexagonString (splittedCommand[2]);
                        Hexagon hex2 = t.parseHexagonString (splittedCommand[3]);

                        Position position = Hexagon.getRoadPositionFromHexagons(hex1, hex2);
                        Road road = t.parseRoadPosition(position);
                        t.getCurrentPlayer ().buildRoad (road);
                    }
                } else if (command.startsWith("buy") && t.isAvailableCommand("buy")) {
                    t.getCurrentPlayer().buyActionCard();
                } else if (command.startsWith("trade") && t.isAvailableCommand("trade")) {
                    if (splittedCommand[1].equals ("4-1")) {
                        t.getCurrentPlayer()
                                .trade4for1(ResourceCardType.values()[Integer.parseInt(splittedCommand[2])],
                                            ResourceCardType.values()[Integer.parseInt(splittedCommand[3])]);
                    } else if (splittedCommand[1].equals ("3-1")) {
                        t.getCurrentPlayer()
                                .trade3for1(ResourceCardType.values()[Integer.parseInt(splittedCommand[2])],
                                        ResourceCardType.values()[Integer.parseInt(splittedCommand[3])]);
                    } else if (splittedCommand[1].equals ("2-1")) {
                        t.getCurrentPlayer()
                                .trade2for1(ResourceCardType.values()[Integer.parseInt(splittedCommand[2])],
                                        ResourceCardType.values()[Integer.parseInt(splittedCommand[3])]);
                    }
                } else if (command.startsWith("rob") && t.isAvailableCommand("rob")) {
                    Hexagon hexagon = t.parseHexagonString (splittedCommand[1]);
                    t.getCurrentPlayer().choseRobberPlace(hexagon);
                } else if (command.startsWith("play") && t.isAvailableCommand("play")) {
                    ActionCardType actionCard = t.parseActionCardString (splittedCommand[1]);
                    switch (actionCard) {
                        case KNIGHT: {
                            Hexagon hexagon = t.parseHexagonString (splittedCommand[2]);

                            t.getCurrentPlayer().playKnight(hexagon);
                            break;
                        }
                        case YEAR_OF_PLENTY: {
                            t.getCurrentPlayer()
                                    .playYearOfPlenty(ResourceCardType.values()[Integer.parseInt(splittedCommand[2])],
                                                      ResourceCardType.values()[Integer.parseInt(splittedCommand[3])]);
                            break;
                        }
                        case ROUTE_BUILDING: {// #TODO
//                            t.getCurrentPlayer()
//                                    .playRoadBuilding()
                            break;
                        }
                        case MONOPOLY: {
                            t.getCurrentPlayer()
                                    .playMonopoly(ResourceCardType.values()[Integer.parseInt(splittedCommand[2])]);
                            break;
                        }
                    }
                } else if (command.startsWith ("info")) {
                    System.out.print ("n\\r\t|");
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
                } else if (command.startsWith("map")) {
                    if (splittedCommand.length == 1 || splittedCommand[1].startsWith("coords")) {
                        t.draw();
                    } else
                    if (splittedCommand[1].startsWith("prod")) {
                        t.drawMine(t.getCurrentPlayer(), true);
                    } else
                    if (splittedCommand[1].startsWith("gain")) {
                        t.drawMine(t.getCurrentPlayer(), false);
                    }
                } else if (command.startsWith("cards")) {
                    for (int i=0; i < ActionCardType.values().length; i++) {
                        System.out.print(ActionCardType.values()[i].name + ": " +t.getCurrentPlayer().actionCards[i]+"\t");
                    }
                    System.out.println();
                } else if (command.startsWith("opts")){
                    System.out.print("4-1 : (Any)\t");
                    if (t.getCurrentPlayer().canTrade3for1) {
                        System.out.print("3-1 : (Any)\t");
                    }
                    for (int i=0; i < ResourceCardType.values().length; i++) {
                        if (t.getCurrentPlayer().canTrade2for1[i]) {
                            System.out.print("2-1 : (" + ResourceCardType.values()[i].name() + ")\t");
                        }
                    }
                    System.out.println();
                } else if (command.startsWith("points")){

                    System.out.println("Points: " + t.getCurrentPlayer().getPoints());
                } else if (command.startsWith ("help")) {
                    List<String> allCommands = new ArrayList<>();

                    allCommands.addAll(t.getGameStance().getAvailableCommands());

                    allCommands.add("cards");
                    allCommands.add("opts");
                    allCommands.add("points");
                    allCommands.add("map");
                    allCommands.add("res");
                    allCommands.add("gain");
                    allCommands.add("info");
                    allCommands.add("help");

                    System.out.print("Available commands: ");
                    for(String cmd : allCommands) {
                        System.out.print(cmd+" ");
                    }
                    System.out.println();

                }

            } catch (Exception ex) {
                ex.printStackTrace ();
            }
        }

    }
}
