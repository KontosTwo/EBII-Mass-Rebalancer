package massbalancer.Unit;

import java.util.List;

public final class UnitEntry {
    private final String entry;
    private final List<String> data;

    public UnitEntry(final String entry, final List<String> data) {
        this.entry = entry;
        this.data = data;
    }

    public String getEntry() {
        return entry;
    }

    public List<String> getData() {
        return data;
    }
}
