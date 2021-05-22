package massbalancer.Unit;

import massbalancer.Balancer.Balancer;
import massbalancer.Balancer.BalancerDefinition;
import massbalancer.Balancer.BalancerException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static massbalancer.StringUtils.eduEntryToData;

public final class UnitWriter {
    private static final String START_DELIMITER = "^type.*";
    private static final String MASS_DELIMITER = "^soldier.*";
    private static final String CATEGORY_DELIMITER = "^category.*";

    public static void write (
        final BufferedReader reader,
        final BufferedWriter writer,
        final Map<String, Unit> unitNameToUnit,
        final List<Balancer> balancers
    ) throws IOException, BalancerException {
        String currentLine = null;
        String currentName = null;
        boolean isInfantry = false;

        while((currentLine = reader.readLine()) != null){
            boolean writeOriginalLine = true;

            if(currentLine.matches(CATEGORY_DELIMITER)){
                final String category = currentLine.split("\\s+")[1];
                isInfantry = category.equals("infantry");
            }
            else if(currentLine.matches(START_DELIMITER)){
                currentName = String.join(" ", eduEntryToData(currentLine));
            }
            else if(currentLine.matches(MASS_DELIMITER) && isInfantry){
                final List<String> soldierData = eduEntryToData(currentLine);

                final Unit unit = unitNameToUnit.get(currentName);
                final String mass = String.valueOf(BalancerDefinition.balance(unit, balancers));

                soldierData.set(3, mass);

                writer.write(String.format("soldier                 %s",String.join(", ",soldierData)));
                writer.newLine();

                writeOriginalLine = false;
            }

            if(writeOriginalLine){
                writer.write(currentLine);
                writer.newLine();
            }
        }
    }
}
