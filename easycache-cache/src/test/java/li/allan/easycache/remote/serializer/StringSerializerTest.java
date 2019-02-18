package li.allan.easycache.remote.serializer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lialun
 */
public class StringSerializerTest {

    @Test
    public void testSerializeAndDeserialize() {
        String str = "abc123";
        StringSerializer stringSerializer = SerializerContainer.getSerializer(StringSerializer.class);
        Assert.assertEquals(str, stringSerializer.deserialize(stringSerializer.serialize(str), String.class));
    }
}