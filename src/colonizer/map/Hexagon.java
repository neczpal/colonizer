package colonizer.map;

import colonizer.enums.HexagonType;
import colonizer.enums.NodeType;

import java.util.ArrayList;
import java.util.List;

public class Hexagon extends Position {
    public HexagonType hexagonType;

    public boolean canBeRobbed;
    public boolean isRobbed = false;

    public int rollNumber;

    private final List<Position> neighborIntersectionPositions = new ArrayList<> ();

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
            neighborIntersectionPositions.add(new Position (x, 2*y));
            neighborIntersectionPositions.add(new Position (x+1, 2*y));
            neighborIntersectionPositions.add(new Position (x, 2*y+1));
            neighborIntersectionPositions.add(new Position (x+1, 2*y+1));
            neighborIntersectionPositions.add(new Position (x, 2*y+2));
            neighborIntersectionPositions.add(new Position (x+1, 2*y+2));
        } else {
            neighborIntersectionPositions.add(new Position (x, 2*y+1));
            neighborIntersectionPositions.add(new Position (x+1, 2*y+1));
            neighborIntersectionPositions.add(new Position (x, 2*y+2));
            neighborIntersectionPositions.add(new Position (x+1, 2*y+2));
            neighborIntersectionPositions.add(new Position (x, 2*y+3));
            neighborIntersectionPositions.add(new Position (x+1, 2*y+3));
        }
    }

    public void setRobber (boolean isRobbed) {
        this.isRobbed = isRobbed;
    }

    public int getRollNumber () {
        return rollNumber;
    }

    public List<Position> getNeighborIntersections() {
        return neighborIntersectionPositions;
    }
}
