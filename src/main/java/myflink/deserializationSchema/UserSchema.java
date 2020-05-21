package myflink.deserializationSchema;

import myflink.model.User;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

import java.io.IOException;

public class UserSchema implements DeserializationSchema<User> {
    @Override
    public User deserialize(byte[] message) throws IOException {
        return User.parseJson(new String(message));
    }

    @Override
    public boolean isEndOfStream(User nextElement) {
        return false;
    }

    @Override
    public TypeInformation<User> getProducedType() {
        return TypeExtractor.getForClass(User.class);
    }
}
