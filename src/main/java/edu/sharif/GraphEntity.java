package edu.sharif;

import java.util.List;
import java.util.Map;

public interface GraphEntity {

    public String getLabel();
    public String getUniqueKey();
    public Object getUniqueValue();

    public String[] getProperties();
    public List<Object> getValues();
    public Map<String, Object> getMap();
}
