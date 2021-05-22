package massbalancer.Unit;

import java.util.HashMap;
import java.util.Map;

public final class Unit {

    private final Map<UnitAttribute, String> attributeToData;

    public Unit(){
        attributeToData = new HashMap<>();
    }

    public boolean hasDataFor(final UnitAttribute attribute){
        return attributeToData.containsKey(attribute);
    }

    public String getDataFrom(final UnitAttribute attribute){
        return attributeToData.get(attribute);
    }

    public void setDataFor(final UnitAttribute attribute, String data){
        attributeToData.put(attribute, data);
    }
}
