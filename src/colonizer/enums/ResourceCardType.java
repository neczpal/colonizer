package colonizer.enums;

public enum ResourceCardType {
    RES_WOOD(0),
    RES_CLAY(1),
    RES_WOOL(2),
    RES_WHEAT(3),
    RES_STONE(4);

    public int id;

    ResourceCardType(int id) {
        this.id = id;
    }
};
