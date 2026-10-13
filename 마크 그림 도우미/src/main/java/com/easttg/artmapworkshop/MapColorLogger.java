/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1533
 *  net.minecraft.class_1806
 *  net.minecraft.class_1937
 *  net.minecraft.class_22
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_9209
 *  net.minecraft.class_9334
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.AimTracker;
import com.easttg.artmapworkshop.ArtImage;
import com.easttg.artmapworkshop.CanvasDetector;
import com.easttg.artmapworkshop.ProjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1533;
import net.minecraft.class_1806;
import net.minecraft.class_1937;
import net.minecraft.class_22;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

@Environment(value=EnvType.CLIENT)
public final class MapColorLogger {
    private MapColorLogger() {
    }

    public static void capture(class_310 mc) {
        class_22 data;
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1755 != null) {
            return;
        }
        AimTracker.update(mc);
        class_1533 frame = CanvasDetector.target(mc);
        if (frame == null || !AimTracker.valid) {
            MapColorLogger.message(mc, "C\ud0a4\ub85c \uce94\ubc84\uc2a4\ub97c \ucc3e\uace0 \uce21\uc815\ud560 \uce78\uc744 \ubc14\ub77c\ubcf8 \ub4a4 F7\uc744 \ub204\ub974\uc138\uc694.");
            return;
        }
        class_9209 mapId = (class_9209)frame.method_6940().method_57824(class_9334.field_49646);
        class_22 class_222 = data = mapId == null ? null : class_1806.method_7997((class_9209)mapId, (class_1937)mc.field_1687);
        if (data == null || data.field_122 == null || data.field_122.length < 16384) {
            MapColorLogger.message(mc, "\uc9c0\ub3c4 \ub370\uc774\ud130\uac00 \uc544\uc9c1 \uc5c6\uc2b5\ub2c8\ub2e4. \uc9c0\ub3c4\uac00 \ud45c\uc2dc\ub41c \ub4a4 \ub2e4\uc2dc \uce21\uc815\ud558\uc138\uc694.");
            return;
        }
        int x = AimTracker.x;
        int y = AimTracker.y;
        JsonArray samples = new JsonArray();
        JsonObject counts = new JsonObject();
        int first = data.field_122[y * 4 * 128 + x * 4] & 0xFF;
        boolean uniform = true;
        for (int dy = 0; dy < 4; ++dy) {
            for (int dx = 0; dx < 4; ++dx) {
                int id = data.field_122[(y * 4 + dy) * 128 + x * 4 + dx] & 0xFF;
                samples.add((Number)id);
                String key = Integer.toString(id);
                counts.addProperty(key, (Number)(counts.has(key) ? counts.get(key).getAsInt() + 1 : 1));
                uniform &= id == first;
            }
        }
        JsonObject record = new JsonObject();
        record.addProperty("schemaVersion", (Number)1);
        record.addProperty("timestampUtc", Instant.now().toString());
        record.addProperty("mapId", (Number)mapId.comp_2315());
        record.addProperty("x", (Number)x);
        record.addProperty("y", (Number)y);
        record.addProperty("paletteMode", ProjectState.paletteMode.name());
        record.addProperty("colorMatchMode", ProjectState.colorMatchMode.name());
        record.addProperty("selectedPaintCode", ProjectState.focusKey);
        record.addProperty("selectedStage", ProjectState.focusAction);
        record.addProperty("labelsVerified", Boolean.valueOf(false));
        record.addProperty("uniform4x4", Boolean.valueOf(uniform));
        record.add("packedIdsRowMajor", (JsonElement)samples);
        record.add("packedIdCounts", (JsonElement)counts);
        if (uniform) {
            record.addProperty("observedPackedId", (Number)first);
            record.addProperty("observedBaseColorId", (Number)(first >>> 2));
            record.addProperty("observedShadeId", (Number)(first & 3));
        }
        if (ProjectState.image != null) {
            record.addProperty("targetTileIndex", (Number)ProjectState.tile);
            ArtImage.Cell cell = ProjectState.image.cell(ProjectState.tile, x, y);
            if (!cell.emptyPixel()) {
                JsonObject target = new JsonObject();
                target.addProperty("code", cell.color().code());
                target.addProperty("name", cell.color().paintName());
                target.addProperty("action", cell.color().action());
                target.addProperty("rgb", (Number)cell.color().rgb());
                record.add("imageTargetNotMeasurementLabel", (JsonElement)target);
            }
        }
        Path log = ProjectState.rootDir().resolve("color-id-log.jsonl");
        try {
            Files.createDirectories(log.getParent(), new FileAttribute[0]);
            Files.writeString(log, (CharSequence)(String.valueOf(record) + System.lineSeparator()), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            MapColorLogger.message(mc, (String)(uniform ? "\uc0c9\uc0c1 ID \uae30\ub85d: " + first + " (\uae30\ubcf8 ID " + (first >>> 2) + ", \ubc1d\uae30 ID " + (first & 3) + ") \u2192 color-id-log.jsonl" : "4\u00d74\uc758 \uc0c9\uc0c1\uc774 \uc11e\uc5ec \uc788\uc2b5\ub2c8\ub2e4. 16\uac1c ID\ub97c \uae30\ub85d\ud588\uc2b5\ub2c8\ub2e4. \uc7a0\uc2dc \ud6c4 \ub2e4\uc2dc \uce21\uc815\ud558\uc138\uc694."));
        }
        catch (IOException | SecurityException error) {
            MapColorLogger.message(mc, "\uc0c9\uc0c1 ID \ub85c\uadf8 \uc800\uc7a5 \uc2e4\ud328: " + error.getMessage());
        }
    }

    private static void message(class_310 mc, String text) {
        mc.field_1724.method_7353((class_2561)class_2561.method_43470((String)text), false);
    }
}

