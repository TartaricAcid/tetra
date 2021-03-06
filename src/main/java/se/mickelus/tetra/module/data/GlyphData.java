package se.mickelus.tetra.module.data;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import se.mickelus.tetra.TetraMod;

import java.lang.reflect.Type;

public class GlyphData {
    public ResourceLocation textureLocation = new ResourceLocation(TetraMod.MOD_ID, "textures/gui/glyphs.png");
    public int tint = 0xffffffff;
    public int textureX = 0;
    public int textureY = 0;

    public GlyphData() {}

    public GlyphData(int textureX, int textureY) {
        this.textureX = textureX;
        this.textureY = textureY;
    }

    public GlyphData(String texture, int textureX, int textureY) {
        textureLocation = new ResourceLocation(TetraMod.MOD_ID, texture);
        this.textureX = textureX;
        this.textureY = textureY;
    }

    public static class GlyphTypeAdapter implements JsonDeserializer<GlyphData> {

        @Override
        public GlyphData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            GlyphData data = new GlyphData();

            if (jsonObject.has("textureX")) {
                data.textureX = jsonObject.get("textureX").getAsInt();
            }

            if (jsonObject.has("textureY")) {
                data.textureY = jsonObject.get("textureY").getAsInt();
            }

            if (jsonObject.has("tint")) {
                data.tint = (int) Long.parseLong(jsonObject.get("tint").getAsString(), 16);
            }

            return data;
        }
    }
}
