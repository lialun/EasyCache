package li.allan.easycache.remote.serializer;

import li.allan.easycache.remote.serializer.exception.SerializationException;
import org.junit.Test;

/**
 * @author lialun
 */
public class Jackson2SerializerTest extends AbstractSerializerTest {
    @Override
    Serializer serializer() {
        return new Jackson2Serializer();
    }

    @Test(expected = SerializationException.class)
    public void notImplementsSerializable() {
        serializeAndDeserializeCheck(new TestBean(), TestBean.class);
    }

    private class TestBean {

    }
}