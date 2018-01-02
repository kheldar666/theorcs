package org.libermundi.theorcs.util;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UtilTest {



    @Test
    public void testUrlMatcher() {
        String format = "/secure/chronicle/{id}/**";
        String actualUrl = "/secure/chronicle/1/coteries";
        AntPathMatcher pathMatcher = new AntPathMatcher();
        Map<String, String> variables = pathMatcher.extractUriTemplateVariables(format, actualUrl);
        assertThat(variables.get("id"), is("1"));
    }


    @Test
    public void testString() {
        String searchFor = "/secure/chronicle";
        String actualUrl = "/secure/chronicle/1/coteries";

        assertThat(actualUrl.contains(searchFor), is(Boolean.TRUE));

    }
}
