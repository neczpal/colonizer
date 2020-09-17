package colonizer.map;

import colonizer.enums.NodeType;

import static colonizer.enums.Direction.*;

public class Road extends Position {
    public Intersection from;
    public Intersection to;

    //FROM , where the x or the y coordinate is lower
    private Position fromIntersectionPosition;
    //TO , where the x or the y coordinate is higher
    private Position toIntersectionPosition;

    int owned = -1;

//    public Road (Intersection from, Intersection to) {
//        this.from = from;
//        this.to = to;
//    }

    public Road (int x, int y) {
        super (-1, x, y, NodeType.ROAD);

        if (x % 2 == 0) {
            fromIntersectionPosition = new Position (x / 2, y);
            toIntersectionPosition = new Position (x / 2, y + 1);
        } else {
            fromIntersectionPosition = new Position ((x - 1) / 2, y);
            toIntersectionPosition = new Position ((x - 1) / 2 + 1, y);
        }

    }

    public Position getFromIntersectionPosition () {
        return fromIntersectionPosition;
    }

    public Position getToIntersectionPosition () {
        return toIntersectionPosition;
    }

    public boolean isValid () {
        return false;
//#TODO
//        int x1 = from.x;
//        int y1 = from.y;
//        int x2 = to.x;
//        int y2 = to.y;
//
//        Position pos1 = new Position (x1, y1);
//        Position pos2 = new Position (x2, y2);
//
//        if ((x1 + y1) % 2 == 0) {
//            return pos1.moveToTriangle (BOT_LEFT).equals (pos2) ||
//                    pos1.moveToTriangle (BOT_RIGHT).equals (pos2) ||
//                    pos1.moveToTriangle (TOP).equals (pos2);
//        } else {
//            return pos1.moveToTriangle (BOT).equals (pos2) ||
//                    pos1.moveToTriangle (TOP_LEFT).equals (pos2) ||
//                    pos1.moveToTriangle (TOP_RIGHT).equals (pos2);
//        }
    }
}
