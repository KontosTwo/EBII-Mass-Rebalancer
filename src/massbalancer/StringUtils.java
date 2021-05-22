package massbalancer;

import java.util.Arrays;
import java.util.List;

public final class StringUtils {


    public static List<String> eduEntryToData(String entry){
        return Arrays.asList(entry.split("\\s+",2)[1].split(", "));
    }
}
