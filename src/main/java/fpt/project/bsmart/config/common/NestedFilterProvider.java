package fpt.project.bsmart.config.common;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class NestedFilterProvider extends SimpleFilterProvider {

    private static final long serialVersionUID = 1L;

    /**
     * Get List Custom filter name
     * @return
     */
    public String getListKeyFilter() {
        return _filtersById.keySet().toString();
    }

}
