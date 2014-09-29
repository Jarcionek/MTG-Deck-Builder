package mtgdeckbuilder.data;

public class Filter {

    private final Field field;
    private final Function function;
    private final String argument;

    public Filter(Field field, Function function, String argument) {
        this.field = field;
        this.function = function;
        this.argument = argument;
    }

    public Field getField() {
        return field;
    }

    public Function getFunction() {
        return function;
    }

    public String getArgument() {
        return argument;
    }

}
