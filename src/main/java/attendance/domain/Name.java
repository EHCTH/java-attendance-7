package attendance.domain;

import java.util.Objects;

public class Name implements Comparable<Name>{
    private final String name;

    public Name(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Name name1)) return false;
        return Objects.equals(name, name1.name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public int compareTo(Name o) {
        return this.name.compareTo(o.name);
    }
}
