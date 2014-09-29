package mtgdeckbuilder.data;

public class CardImageInfo {

    private final int id;
    private final String name;

    public CardImageInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
