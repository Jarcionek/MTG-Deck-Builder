package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.Filter;

import java.util.List;

public class FilterToUrlConverter {

    private static final String PREFIX = "http://api.mtgdb.info/search/?q=";

    public String convert(List<Filter> filters) {
        StringBuilder stringBuilder = new StringBuilder(PREFIX + convert(filters.get(0)));

        for (int i = 1; i < filters.size(); i++) {
            stringBuilder.append(" and ");
            stringBuilder.append(convert(filters.get(i)));
        }

        return stringBuilder.toString();
    }

    private static String convert(Filter filter) {
        return filter.getField().name() + " " + filter.getFunction().name() + " '" + filter.getArgument() + "'";
    }

}
