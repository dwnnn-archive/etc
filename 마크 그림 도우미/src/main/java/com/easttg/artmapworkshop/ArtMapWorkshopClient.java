/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
 *  net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
 *  net.minecraft.class_2561
 *  net.minecraft.class_304
 *  net.minecraft.class_310
 *  net.minecraft.class_3675$class_307
 *  net.minecraft.class_437
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.AimTracker;
import com.easttg.artmapworkshop.AutoProgress;
import com.easttg.artmapworkshop.CanvasDetector;
import com.easttg.artmapworkshop.ImageDecoders;
import com.easttg.artmapworkshop.MapColorLogger;
import com.easttg.artmapworkshop.ProjectState;
import com.easttg.artmapworkshop.ProjectionRenderer;
import com.easttg.artmapworkshop.ProjectionState;
import com.easttg.artmapworkshop.WorkshopHud;
import com.easttg.artmapworkshop.WorkshopScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_437;

@Environment(value=EnvType.CLIENT)
public final class ArtMapWorkshopClient
implements ClientModInitializer {
    private static class_304 detect;
    private static class_304 toggle;
    private static class_304 load;
    private static class_304 menu;
    private static class_304 clearTile;
    private static class_304 prevTile;
    private static class_304 nextTile;
    private static class_304 focus;
    private static class_304 focusStage;
    private static class_304 donePixel;
    private static class_304 undoPixel;
    private static class_304 captureColorId;

    private static class_304 key(String name, int glfw) {
        return KeyBindingHelper.registerKeyBinding((class_304)new class_304("key.artmapworkshop." + name, class_3675.class_307.field_1668, glfw, "key.categories.artmapworkshop"));
    }

    public void onInitializeClient() {
        ImageDecoders.register();
        detect = ArtMapWorkshopClient.key("detect", 67);
        toggle = ArtMapWorkshopClient.key("toggle3d", 295);
        load = ArtMapWorkshopClient.key("load", 297);
        menu = ArtMapWorkshopClient.key("menu", 298);
        clearTile = ArtMapWorkshopClient.key("clear_tile", 86);
        prevTile = ArtMapWorkshopClient.key("prev_tile", 91);
        nextTile = ArtMapWorkshopClient.key("next_tile", 93);
        focus = ArtMapWorkshopClient.key("focus", 80);
        focusStage = ArtMapWorkshopClient.key("focus_stage", 79);
        donePixel = ArtMapWorkshopClient.key("done_pixel", 88);
        undoPixel = ArtMapWorkshopClient.key("undo_pixel", 90);
        captureColorId = ArtMapWorkshopClient.key("capture_color_id", 296);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            AimTracker.update(client);
            AutoProgress.tick(client);
            while (detect.method_1436()) {
                if (!CanvasDetector.detect(client)) continue;
                AutoProgress.reset();
            }
            while (toggle.method_1436()) {
                ProjectionState.enabled = !ProjectionState.enabled;
                ArtMapWorkshopClient.message(client, "3D Overlay: " + (ProjectionState.enabled ? "ON" : "OFF"));
            }
            while (load.method_1436()) {
                ProjectState.loadImage(client);
            }
            while (menu.method_1436()) {
                if (client.field_1755 != null) continue;
                client.method_1507((class_437)new WorkshopScreen());
            }
            while (clearTile.method_1436()) {
                ProjectState.clearCurrentTileCompleted();
                ArtMapWorkshopClient.message(client, "\ud604\uc7ac \uce94\ubc84\uc2a4\uc758 \uc644\ub8cc \ud53d\uc140\uc744 \ubaa8\ub450 \ucde8\uc18c\ud588\uc2b5\ub2c8\ub2e4.");
            }
            while (prevTile.method_1436()) {
                ProjectState.previousTile(client);
            }
            while (nextTile.method_1436()) {
                ProjectState.nextTile(client);
            }
            while (focus.method_1436()) {
                ProjectState.cycleFocus();
            }
            while (focusStage.method_1436()) {
                ProjectState.cycleFocusStage();
            }
            while (donePixel.method_1436()) {
                ProjectState.completeAimedPixel();
            }
            while (undoPixel.method_1436()) {
                ProjectState.undoAimedPixel();
            }
            while (captureColorId.method_1436()) {
                MapColorLogger.capture(client);
            }
            ProjectState.tickProgressSave();
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ProjectState.flushPendingProgress());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ProjectState.flushPendingProgress();
            AutoProgress.reset();
        });
        WorldRenderEvents.AFTER_ENTITIES.register(ProjectionRenderer::render);
        WorkshopHud.register();
        ProjectState.loadProgress();
    }

    private static void message(class_310 client, String text) {
        if (client.field_1724 != null) {
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)text), true);
        }
    }
}

