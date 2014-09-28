package mtgdeckbuilder;

public enum Function {
    m("contains"),
    eq("equal to"),
    not("not equal"),
    gt("greater than"),
    gte("greater than or equal to"),
    lt("less than"),
    lte("less than or equal to");

    private final String name;

    private Function(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
