package com.epfl.dedis.crypto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class QRStampSerializer implements JsonSerializer<QRStamp> {

    @Override
    public JsonElement serialize(QRStamp src, Type typeOfSrc,
                                 JsonSerializationContext context)
    {

        JsonObject obj = new JsonObject();
        obj.addProperty("ID", src.getId());
        obj.addProperty("Host", src.getHost());
        obj.addProperty("Port", src.getPort());

        return obj;
    }
}
