package attendance.domain;

import java.util.Comparator;
import java.util.Objects;

public class Name implements Comparable<Name>{
    private final String name;

    public Name(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }

    @Override
    public int compareTo(Name o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Name)) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
