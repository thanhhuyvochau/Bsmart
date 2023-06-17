package fpt.project.bsmart.config.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CustomFilter {


    private static CustomFilter instances;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CustomFilter.class);
    private static NestedFilterProvider nestedFilters = new NestedFilterProvider();


    public CustomFilter addFilterOutAllExcept(String strFilter, Set<String> properties) {
        nestedFilters.addFilter(strFilter,
                SimpleBeanPropertyFilter.filterOutAllExcept(properties));
        return this;
    }


    public CustomFilter addFilterOutAllExcept(String strFilter, String... propertyArray) {
        HashSet<String> properties = new HashSet<>(propertyArray.length);
        Collections.addAll(properties, propertyArray);
        nestedFilters.addFilter(strFilter,
                SimpleBeanPropertyFilter.filterOutAllExcept(properties));

        return this;
    }

    public CustomFilter addFilterSerializeAll(String strFilter) {

        nestedFilters.addFilter(strFilter,
                SimpleBeanPropertyFilter.serializeAll());
        return this;
    }


    public static synchronized CustomFilter getInstance() {
        if (instances == null) {
            instances = new CustomFilter();
        }

        return instances;
    }

    public String setFilterProvider(Object objectFilter) throws JsonProcessingException {
        String jsonRes = "{}";
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setFilterProvider(nestedFilters);
            jsonRes = mapper.writeValueAsString(objectFilter);

        } catch (Exception e) {

        }

        return jsonRes;
    }


}
