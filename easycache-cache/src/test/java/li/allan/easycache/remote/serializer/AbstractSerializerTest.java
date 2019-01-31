package li.allan.easycache.remote.serializer;

import li.allan.easycache.ValueWrapper;
import org.junit.Test;

import java.io.Serializable;

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

        serializeAndDeserializeCheck(new boolean[]{Boolean.TRUE, Boolean.FALSE}, boolean[].class);
        serializeAndDeserializeCheck(new byte[]{Byte.MIN_VALUE, Byte.MAX_VALUE}, byte[].class);
        serializeAndDeserializeCheck(new char[]{Character.MIN_VALUE, Character.MAX_VALUE}, char[].class);
        serializeAndDeserializeCheck(new double[]{Double.MIN_VALUE, Double.MAX_VALUE}, double[].class);
        serializeAndDeserializeCheck(new float[]{Float.MIN_VALUE, Float.MAX_VALUE}, float[].class);
        serializeAndDeserializeCheck(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, int[].class);
        serializeAndDeserializeCheck(new long[]{Long.MIN_VALUE, Long.MAX_VALUE}, long[].class);
        serializeAndDeserializeCheck(new short[]{Short.MIN_VALUE, Short.MAX_VALUE}, short[].class);

        serializeAndDeserializeCheck(new Boolean[]{Boolean.TRUE, Boolean.FALSE}, Boolean[].class);
        serializeAndDeserializeCheck(new Byte[]{Byte.MIN_VALUE, Byte.MAX_VALUE}, Byte[].class);
        serializeAndDeserializeCheck(new Character[]{Character.MIN_VALUE, Character.MAX_VALUE}, Character[].class);
        serializeAndDeserializeCheck(new Double[]{Double.MIN_VALUE, Double.MAX_VALUE}, Double[].class);
        serializeAndDeserializeCheck(new Float[]{Float.MIN_VALUE, Float.MAX_VALUE}, Float[].class);
        serializeAndDeserializeCheck(new Integer[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, Integer[].class);
        serializeAndDeserializeCheck(new Long[]{Long.MIN_VALUE, Long.MAX_VALUE}, Long[].class);
        serializeAndDeserializeCheck(new Short[]{Short.MIN_VALUE, Short.MAX_VALUE}, Short[].class);
    }

    @Test
    public void serializeBean() {
        DemoBean demoBean = new DemoBean(Integer.MAX_VALUE, "str",
                new SubClass(new String[]{"s", "u", "b"}, Double.MIN_VALUE));

        serializeAndDeserializeCheck(demoBean, DemoBean.class);
        serializeAndDeserializeCheck(new ValueWrapper<>(demoBean), ValueWrapper.class);
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
}