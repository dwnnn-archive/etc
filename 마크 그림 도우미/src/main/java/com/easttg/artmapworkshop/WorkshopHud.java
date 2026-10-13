/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.AimTracker;
import com.easttg.artmapworkshop.ArtImage;
import com.easttg.artmapworkshop.PaintItems;
import com.easttg.artmapworkshop.ProjectState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.class_310;
import net.minecraft.class_332;

@Environment(value=EnvType.CLIENT)
public final class WorkshopHud {
    private WorkshopHud() {
    }

    public static void register() {
        HudRenderCallback.EVENT.register((g, tickCounter) -> {
            Object targetLine;
            Object aimLine;
            if (!ProjectState.showHud) {
                return;
            }
            class_310 mc = class_310.method_1551();
            if (mc.field_1724 == null) {
                return;
            }
            int panelX = 8;
            int panelW = 258;
            int panelH = 104;
            int panelY = Math.max(8, (g.method_51443() - 104) / 2);
            int textX = 18;
            int line1Y = panelY + 8;
            int lineGap = 13;
            int itemRowHeight = 22;
            int textMaxW = 238;
            g.method_25294(8, panelY, 266, panelY + 104, -1072556772);
            g.method_25294(8, panelY, 11, panelY + 104, -9852929);
            String focusText = WorkshopHud.clean(ProjectState.focusDisplay());
            WorkshopHud.drawTrimmed(g, mc, "\ud604\uc7ac \uc791\uc5c5 \u00b7 " + focusText, 18, line1Y, 238, -158);
            PaintItems.draw(g, mc, 18, line1Y + 12, 238, ProjectState.image != null ? ProjectState.focusKey : "", ProjectState.focusAction);
            if (!AimTracker.valid) {
                aimLine = "\uc870\uc900 \u00b7 \uc624\ubc84\ub808\uc774 \ud53d\uc140 \uc704\uc5d0 \uc870\uc900\ud558\uc138\uc694";
                targetLine = "\ubaa9\ud45c \u00b7 -";
            } else {
                int px = AimTracker.x + 1;
                int py = AimTracker.y + 1;
                ArtImage.Cell cell = ProjectState.cursorCell();
                aimLine = "\uc870\uc900 \ud53d\uc140 \u00b7 " + px + ", " + py;
                if (cell.emptyPixel()) {
                    targetLine = "\ubaa9\ud45c \u00b7 \ube48 \ud53d\uc140";
                } else {
                    targetLine = "\ubaa9\ud45c \u00b7 " + WorkshopHud.clean(cell.color().display());
                    if (ProjectState.isCompleted(AimTracker.x, AimTracker.y)) {
                        targetLine = (String)targetLine + " \u00b7 \uc644\ub8cc";
                    }
                }
            }
            WorkshopHud.drawTrimmed(g, mc, (String)aimLine, 18, line1Y + 13 + 22, 238, -1644049);
            WorkshopHud.drawTrimmed(g, mc, (String)targetLine, 18, line1Y + 26 + 22, 238, -1);
            Object canvasLine = ProjectState.image == null ? "Canvas \u00b7 \uc774\ubbf8\uc9c0 \uc5c6\uc74c" : "Canvas " + (ProjectState.tile + 1) + "/" + ProjectState.image.tileCount() + " \u00b7 \ub0a8\uc74c " + ProjectState.remainingFocusPixels() + " \u00b7 " + String.format("%.0f%%", ProjectState.progress() * 100.0);
            WorkshopHud.drawTrimmed(g, mc, (String)canvasLine, 18, line1Y + 39 + 22, 238, -4931892);
            int dividerY = line1Y + 52 + 22 - 2;
            g.method_25294(18, dividerY, 256, dividerY + 1, 860773230);
            WorkshopHud.drawTrimmed(g, mc, "X \uc644\ub8cc \u00b7 Z \ucde8\uc18c \u00b7 P \uc0c9\uc0c1 \u00b7 O \ub2e8\uacc4", 18, line1Y + 52 + 22 + 4, 238, -7102286);
        });
    }

    private static String clean(String text) {
        if (text == null || text.isBlank()) {
            return "-";
        }
        return text.replace(" / ", " \u00b7 ");
    }

    private static String fit(class_310 mc, String text, int maxWidth) {
        if (mc.field_1772.method_1727(text) <= maxWidth) {
            return text;
        }
        String ellipsis = "...";
        int allowed = Math.max(0, maxWidth - mc.field_1772.method_1727("..."));
        String result = text;
        while (!result.isEmpty() && mc.field_1772.method_1727(result) > allowed) {
            result = result.substring(0, result.length() - 1);
        }
        return result + "...";
    }

    private static void drawTrimmed(class_332 g, class_310 mc, String text, int x, int y, int maxWidth, int color) {
        g.method_51433(mc.field_1772, WorkshopHud.fit(mc, text, maxWidth), x, y, color, false);
    }
}

