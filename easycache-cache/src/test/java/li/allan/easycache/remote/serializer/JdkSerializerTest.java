package li.allan.easycache.remote.serializer;

import li.allan.easycache.remote.serializer.exception.SerializationException;
import org.junit.Test;

/**
 * @author lialun
 */
public class JdkSerializerTest extends AbstractSerializerTest {
    @Override
    Serializer serializer() {
        return new JdkSerializer();
    }

    @Test(expected = SerializationException.class)
    public void notImplementsSerializable() {
        serializeAndDeserializeCheck(new TestBean(), TestBean.class);
    }

    private class TestBean {

    }
}