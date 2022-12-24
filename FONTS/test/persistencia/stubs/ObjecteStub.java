package test.persistencia.stubs;

import java.io.Serializable;
import java.util.Objects;

public class ObjecteStub implements Serializable {
    int i;

    public ObjecteStub(int i){
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjecteStub that = (ObjecteStub) o;
        return i == that.i;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i);
    }
}
