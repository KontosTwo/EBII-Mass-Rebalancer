package massbalancer.Unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class UnitReader {

    private static final String START_DELIMITER = "^type.*";
    private static final String END_DELIMITER = "^ownership.*";

    public static Map<String, Unit> read(BufferedReader reader) throws IOException {
        final Map<String, Unit> eduNameToUnit = new HashMap<>();
        final Map<String, UnitAttributeMapper> eduEntryNameToMapper =
            Arrays.stream(UnitAttributeMapper.values())
                    .collect(Collectors.toMap(UnitAttributeMapper::getEduAttribute, Function.identity()));
        final Set<String> validEDUEntries = eduEntryNameToMapper.keySet();

        String currentLine = null;
        Unit currentUnit = null;

        while((currentLine = reader.readLine()) != null){
            if(currentLine.startsWith(";") || currentLine.matches("\\s+")){
                continue;
            }
            if(currentLine.matches(START_DELIMITER)){
                currentUnit = new Unit();
            }

            if(currentLine.matches(END_DELIMITER)){
                eduNameToUnit.put(currentUnit.getDataFrom(UnitAttribute.NAME), currentUnit);
            }

            final String[] splits = currentLine.split("\\s+",2);
            final String entryName = splits[0];

            if(!isRelevant(entryName, validEDUEntries)){
                continue;
            }

            final List<String> entryData = Arrays.asList(splits[1].split(", "));
            final UnitEntry entry = new UnitEntry(entryName, entryData);
            final UnitAttributeMapper mapper = eduEntryNameToMapper.get(entryName);
            mapper.getProcessor().accept(currentUnit,entry);

        }

        return eduNameToUnit;
    }

    private static boolean isRelevant(String eduEntry, Set<String> eduEntries){
        return eduEntries.stream().anyMatch(eduEntry::matches);
    }
}
