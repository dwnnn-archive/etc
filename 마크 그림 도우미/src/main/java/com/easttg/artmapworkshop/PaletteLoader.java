/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.AddedPaints;
import com.easttg.artmapworkshop.PaletteColor;
import com.easttg.artmapworkshop.PaletteMode;
import com.easttg.artmapworkshop.ProjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class PaletteLoader {
    private PaletteLoader() {
    }

    public static List<PaletteColor> load() throws IOException {
        return PaletteLoader.load(ProjectState.paletteMode);
    }

    public static List<PaletteColor> load(PaletteMode mode) throws IOException {
        String resource = "/assets/artmapworkshop/" + mode.resourceName();
        InputStream in = PaletteLoader.class.getResourceAsStream(resource);
        if (in == null) {
            throw new FileNotFoundException(mode.resourceName() + " not found");
        }
        ArrayList<PaletteColor> result = new ArrayList<PaletteColor>();
        try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);){
            JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
            for (JsonElement paintElement : root.getAsJsonArray("paints")) {
                JsonObject paint = paintElement.getAsJsonObject();
                if (paint.has("special") && !paint.get("special").isJsonNull() && "transparent".equals(paint.get("special").getAsString())) continue;
                String code = paint.get("code").getAsString();
                String name = paint.get("name").getAsString();
                for (JsonElement variantElement : paint.getAsJsonArray("variants")) {
                    JsonObject variant = variantElement.getAsJsonObject();
                    if (!variant.has("rgb") || variant.get("rgb").isJsonNull()) continue;
                    JsonArray rgb = variant.getAsJsonArray("rgb");
                    result.add(new PaletteColor(code, name, variant.get("action").getAsString(), rgb.get(0).getAsInt(), rgb.get(1).getAsInt(), rgb.get(2).getAsInt()));
                }
            }
        }
        result.addAll(AddedPaints.colors());
        return result;
    }
}

