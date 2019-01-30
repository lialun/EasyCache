package li.allan.easycache.remote.serializer;

import li.allan.easycache.ValueWrapper;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * @author lialun
 */
public abstract class AbstractSerializerTest implements Serializable {
    abstract Serializer serializer();

    @Test
    public void serializeNull() {
        serializeAndDeserializeCheck(null, Object.class);
    }

    @Test
    public void serializePrimitive() {
        serializeAndDeserializeCheck(Boolean.TRUE, Boolean.class);
        serializeAndDeserializeCheck(Byte.MAX_VALUE, Byte.class);
        serializeAndDeserializeCheck(Character.MAX_VALUE, Character.class);
        serializeAndDeserializeCheck(Double.MAX_VALUE, Double.class);
        serializeAndDeserializeCheck(Float.MAX_VALUE, Float.class);
        serializeAndDeserializeCheck(Integer.MAX_VALUE, Integer.class);
        serializeAndDeserializeCheck(Long.MAX_VALUE, Long.class);
        serializeAndDeserializeCheck(Short.MAX_VALUE, Short.class);
        serializeAndDeserializeCheck(Void.TYPE, Void.class);

        serializeAndDeserializeCheck(new boolean[]{Boolean.TRUE, Boolean.FALSE}, Boolean.class);
        serializeAndDeserializeCheck(new byte[]{Byte.MIN_VALUE, Byte.MAX_VALUE}, Byte.class);
        serializeAndDeserializeCheck(new char[]{Character.MAX_VALUE}, Character.class);
        serializeAndDeserializeCheck(new double[]{Double.MIN_VALUE, Double.MAX_VALUE}, Character.class);
    }

    @Test
    public void serializeBean() {
        serializeAndDeserializeCheck(new Demo(), Demo.class);
        serializeAndDeserializeCheck(new ValueWrapper<>(new Demo()), ValueWrapper.class);
        serializeAndDeserializeCheck(new ValueWrapper<>("abc"), ValueWrapper.class);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    <T> void serializeAndDeserializeCheck(Object obj, Class<T> type) {
        byte[] bytes = serializer().serialize(obj);
        Object tmp = serializer().deserialize(bytes, type);
        if (obj != null && obj.getClass().isArray()) {
            tmp.equals(obj);
        } else {
            assertEquals(obj, tmp);
        }
    }

    private class Demo implements Serializable {
        private int i = Integer.MAX_VALUE;
        private String str = "abc";
        private SubClass subClass = new SubClass();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Demo demo = (Demo) o;
            return i == demo.i &&
                    Objects.equals(str, demo.str) &&
                    Objects.equals(subClass, demo.subClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, str, subClass);
        }
    }

    private class SubClass implements Serializable {
        private String[] sub = new String[]{"s", "u", "b"};
        private double d = Double.MIN_VALUE;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SubClass subClass = (SubClass) o;
            return Double.compare(subClass.d, d) == 0 &&
                    Arrays.equals(sub, subClass.sub);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(d);
            result = 31 * result + Arrays.hashCode(sub);
            return result;
        }
    }
}