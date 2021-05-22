package massbalancer.Unit;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Defines edu entries that will be considered in calculating mass.
 * Also defines which edu entry values will be considered as well
 */
public enum UnitAttributeMapper {
    PRIMARY_ARMOR("stat_pri_armour", (unit, eduEntry)-> {
        setEntryInOrder(unit, eduEntry, UnitAttribute.ARMOR, UnitAttribute.DEFENCE_SKILL, UnitAttribute.SHIELD);
    }),
    MENTAL("stat_mental",(unit, eduEntry)-> {
        setEntryInOrder(unit, eduEntry, UnitAttribute.MORALE, UnitAttribute.DISCIPLINE, UnitAttribute.TRAINING);
    }),
    SPECIAL("attributes",(unit, eduEntry)-> {
        setEntryIfExist(unit, eduEntry, UnitAttribute.COMMAND, UnitAttribute.CANNOT_SKIRMISH);
    }),
    TYPE("type",(unit, eduEntry)-> {
        unit.setDataFor(UnitAttribute.NAME, String.join(" ", eduEntry.getData()));
    }),
    PRIMARY_WEAPON("stat_pri",(unit, eduEntry)-> {

    }),
    SECONDARY_WEAPON("stat_sec", (unit, eduEntry)-> {

    }),
    FORMATION("formation",(unit, eduEntry)-> {

    }),
    CLASS("class",  (unit, eduEntry)-> {

    });

    private final String eduAttribute;

    private final BiConsumer<Unit, UnitEntry> processor;

    UnitAttributeMapper(
        final String eduAttribute,
        final BiConsumer<Unit, UnitEntry> processor
    ){
        this.eduAttribute = eduAttribute;
        this.processor = processor;
    }

    public String getEduAttribute() {
        return eduAttribute;
    }

    public BiConsumer<Unit, UnitEntry> getProcessor() {
        return processor;
    }

    private static void setEntryInOrder(final Unit unit,final  UnitEntry entry, final UnitAttribute... attributes) {
        final int numAttributes = attributes.length;
        final List<String> data = entry.getData();
        try {
            for (int i = 0; i < numAttributes; i++) {
                unit.setDataFor(attributes[i], data.get(i));
            }
        }catch (final IndexOutOfBoundsException e){
            System.err.println(String.format("While there were %d expected attribute data, only %d were found for entry %s", numAttributes, data.size(), entry.getEntry()));
        }
    }

    private static void setEntryIfExist(final Unit unit, final  UnitEntry entry, final UnitAttribute... attributes){
        final Map<String, UnitAttribute> eduNameToAttributes =
            Arrays.stream(attributes).collect(Collectors.toMap(UnitAttribute::getEduName, Function.identity()));
        entry.getData().stream().forEach(data -> {
            final UnitAttribute attribute = eduNameToAttributes.get(data);
            if(attribute != null){
                unit.setDataFor(attribute, data);
            }
        });
    }
}
