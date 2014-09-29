package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.Field;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Function;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FilterToUrlConverterTest {

    private FilterToUrlConverter filterToUrlConverter = new FilterToUrlConverter();

    @Test
    public void convertsSingleFilter() {
        String url = filterToUrlConverter.convert(newArrayList(new Filter(Field.color, Function.eq, "black")));

        assertThat(url, is(equalTo("http://api.mtgdb.info/search/?q=color eq 'black'")));
    }

    @Test
    public void convertsMultipleFilters() {
        String url = filterToUrlConverter.convert(
                newArrayList(
                        new Filter(Field.convertedmanacost, Function.lte, "5"),
                        new Filter(Field.name, Function.m, "Act of"),
                        new Filter(Field.description, Function.m, "until end of turn")
                )
        );

        assertThat(url, is(equalTo("http://api.mtgdb.info/search/?q=convertedmanacost lte '5' and name m 'Act of' and description m 'until end of turn'")));
    }

}
