package ch.epfl.dedis.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class QRStampSerializerTest {

    @Test
    public void serializationEnforcesCorrectOrder() {
        QRStamp qrStamp = new QRStamp("device", "host", "port");
        String json = Utils.toJson(qrStamp);
        assertEquals("{\"ID\":\"device\",\"Host\":\"host\",\"Port\":\"port\"}", json);
    }
}
