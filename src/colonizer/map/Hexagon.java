package colonizer.map;

import colonizer.enums.HexagonType;
import colonizer.enums.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Hexagon extends Position {
    public HexagonType hexagonType;

    public boolean canBeRobbed;
    public boolean isRobbed = false;

    public int rollNumber;

    private final List<Position> neighborIntersectionPositions = new ArrayList<> ();
    private final List<Position> neighborRoadPositions = new ArrayList<> ();

    public List<Intersection> neighborIntersections = new ArrayList<> ();
    public List<Road> neighborRoads = new ArrayList<> ();
//    public List<Hexagon> neighborHexagons = new ArrayList<> ();

    public Hexagon () {
        this (HexagonType.NONE);
    }

    public Hexagon (HexagonType hexagonType) {
        super (-1, 0, 0, NodeType.HEXAGON);
        this.hexagonType = hexagonType;
        this.rollNumber = -1;
        this.canBeRobbed = false;
    }

    public Hexagon (int x, int y, HexagonType hexagonType) {
        this (x, y, hexagonType, -1);
    }

    public Hexagon (int x, int y, HexagonType hexagonType, int rollNumber) {
        super (-1, x, y, NodeType.HEXAGON);
        this.hexagonType = hexagonType;
        this.rollNumber = rollNumber;
        this.canBeRobbed = rollNumber != -1;

        canBeRobbed = hexagonType.id < 10;

        if (x % 2 == 0) {
            for (int i=0; i < 3; i++) {
                neighborIntersectionPositions.add(new Position (x, 2*y+i));
                neighborIntersectionPositions.add(new Position (x+1, 2*y+i));
            }
//            neighborIntersectionPositions.add(new Position (x, 2*y));
//            neighborIntersectionPositions.add(new Position (x+1, 2*y));
//            neighborIntersectionPositions.add(new Position (x, 2*y+1));
//            neighborIntersectionPositions.add(new Position (x+1, 2*y+1));
//            neighborIntersectionPositions.add(new Position (x, 2*y+2));
//            neighborIntersectionPositions.add(new Position (x+1, 2*y+2));

            neighborRoadPositions.add (new Position(2*x+1, 2*y));
            neighborRoadPositions.add (new Position(2*x, 2*y));
            neighborRoadPositions.add (new Position(2*x+2, 2*y));
            neighborRoadPositions.add (new Position(2*x, 2*y+1));
            neighborRoadPositions.add (new Position(2*x+2, 2*y+1));
            neighborRoadPositions.add (new Position(2*x+1, 2*y+2));


        } else {
            for (int i=0; i < 3; i++) {
                neighborIntersectionPositions.add(new Position (x, 2*y+i+1));
                neighborIntersectionPositions.add(new Position (x+1, 2*y+i+1));
            }
//            neighborIntersectionPositions.add(new Position (x, 2*y+1));
//            neighborIntersectionPositions.add(new Position (x+1, 2*y+1));
//            neighborIntersectionPositions.add(new Position (x, 2*y+2));
//            neighborIntersectionPositions.add(new Position (x+1, 2*y+2));
//            neighborIntersectionPositions.add(new Position (x, 2*y+3));
//            neighborIntersectionPositions.add(new Position (x+1, 2*y+3));

            neighborRoadPositions.add (new Position(2*x+1, 2*y+1));
            neighborRoadPositions.add (new Position(2*x, 2*y+1));
            neighborRoadPositions.add (new Position(2*x+2, 2*y+1));
            neighborRoadPositions.add (new Position(2*x, 2*y+2));
            neighborRoadPositions.add (new Position(2*x+2, 2*y+2));
            neighborRoadPositions.add (new Position(2*x+1, 2*y+3));
        }
    }

    public void setRobber (boolean isRobbed) {
        this.isRobbed = isRobbed;
    }

    public int getRollNumber () {
        return rollNumber;
    }

    public List<Position> getNeighborIntersectionPositions() {
        return neighborIntersectionPositions;
    }
    public List<Position> getNeighborRoadPositions() {
        return neighborRoadPositions;
    }

    public void addNeighborRoad(Road road) {
        neighborRoads.add(road);
    }

    public void addNeighborIntersection(Intersection intersection) {
        neighborIntersections.add(intersection);
    }

    public static Position getIntersectionPositionFromHexagons(Hexagon hex1,
                                                               Hexagon hex2,
                                                               Hexagon hex3) {

        List<Position> result = hex1.neighborIntersectionPositions.stream()
                .distinct()
                .filter(hex2.neighborIntersectionPositions::contains)
                .filter(hex3.neighborIntersectionPositions::contains)
                .collect(Collectors.toList());
        return result.isEmpty() ? null : result.get(0);
    }
    public static Position getRoadPositionFromHexagons(Hexagon hex1,
                                                       Hexagon hex2) {

        List<Position> result = hex1.neighborRoadPositions.stream()
                .distinct()
                .filter(hex2.neighborRoadPositions::contains)
                .collect(Collectors.toList());
        return result.isEmpty() ? null : result.get(0);
    }
}
