/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_342
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.ArtImage;
import com.easttg.artmapworkshop.AutoProgress;
import com.easttg.artmapworkshop.CanvasDetector;
import com.easttg.artmapworkshop.ColorMatchMode;
import com.easttg.artmapworkshop.Image32Processor;
import com.easttg.artmapworkshop.ImageStyle;
import com.easttg.artmapworkshop.PaletteMode;
import com.easttg.artmapworkshop.ProjectState;
import com.easttg.artmapworkshop.ProjectionRenderer;
import com.easttg.artmapworkshop.ProjectionState;
import com.easttg.artmapworkshop.WorkshopTab;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;

@Environment(value=EnvType.CLIENT)
public final class WorkshopScreen
extends class_437 {
    private WorkshopTab tab = WorkshopTab.IMAGE;
    private static final int W = 620;
    private static final int H = 390;
    private BufferedImage previewImage;
    private String previewKey = "";
    private boolean previewRequested = false;
    private ArtImage appliedStylePreview;

    public WorkshopScreen() {
        super((class_2561)class_2561.method_43470((String)"ArtMap Workshop"));
    }

    protected void method_25426() {
        ProjectState.refreshImageList();
        int px = (this.field_22789 - 620) / 2;
        int py = (this.field_22790 - 390) / 2;
        this.sidebar(px, py);
        switch (this.tab) {
            case IMAGE: {
                this.image(px, py);
                break;
            }
            case PALETTE: {
                this.palette(px, py);
                break;
            }
            case WORK: {
                this.work(px, py);
                break;
            }
            case ACCESSIBILITY: {
                this.access(px, py);
                break;
            }
            case SETTINGS: {
                this.settings(px, py);
                break;
            }
            case TUTORIAL: {
                this.tutorial(px, py);
            }
        }
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ub2eb\uae30"), b -> this.method_25419()).method_46434(px + 620 - 68, py + 390 - 28, 56, 18).method_46431());
    }

    private void sidebar(int px, int py) {
        int x = px + 12;
        int y = py + 52;
        this.tab("\uc774\ubbf8\uc9c0 \uc900\ube44", WorkshopTab.IMAGE, x, y);
        this.tab("\uc0c9\uac10 \uc120\ud0dd", WorkshopTab.PALETTE, x, y + 28);
        this.tab("\uadf8\ub9bc \uc791\uc5c5", WorkshopTab.WORK, x, y + 56);
        this.tab("\ubcf4\uae30 \ud3b8\ud558\uac8c", WorkshopTab.ACCESSIBILITY, x, y + 84);
        this.tab("\ud654\uba74 \uc124\uc815", WorkshopTab.SETTINGS, x, y + 112);
        if (ProjectState.showTutorial) {
            this.tab("\uc0ac\uc6a9\ubc95", WorkshopTab.TUTORIAL, x, y + 140);
        }
    }

    private void tab(String text, WorkshopTab target, int x, int y) {
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)((this.tab == target ? "\u25b6 " : "") + text)), b -> {
            this.tab = target;
            this.method_41843();
        }).method_46434(x, y, 96, 20).method_46431());
    }

    private void image(int px, int py) {
        int contentX;
        int previewX = contentX = px + 126;
        int previewY = py + 70;
        int controlsX = contentX + 220;
        this.addStyleButton("\ub85c\uace0/\ub3c4\ud2b8", ImageStyle.LOGO, px + 134, py + 272);
        this.addStyleButton("\uc77c\ub7ec\uc2a4\ud2b8/\uc0ac\uc9c4", ImageStyle.ILLUSTRATION, px + 232, py + 272);
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \ud3f4\ub354 \uc5f4\uae30"), b -> ProjectState.openImageFolder(class_310.method_1551())).method_46434(controlsX, previewY, 125, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \ubaa9\ub85d \uc0c8\ub85c\uace0\uce68"), b -> {
            ProjectState.refreshImageList();
            this.resetPreviewReadiness();
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(controlsX + 135, previewY, 125, 20).method_46431());
        int y = previewY + 30;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\u25c0"), b -> {
            ProjectState.previousImage(class_310.method_1551());
            this.resetPreviewReadiness();
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(controlsX, y, 38, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uc120\ud0dd\ud55c \uadf8\ub9bc \ubd88\ub7ec\uc624\uae30"), b -> {
            ProjectState.makeSelectedImage128Multiple(class_310.method_1551());
            this.previewRequested = true;
            this.clearPreviewCache();
        }).method_46434(controlsX + 48, y, 164, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\u25b6"), b -> {
            ProjectState.nextImage(class_310.method_1551());
            this.resetPreviewReadiness();
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(controlsX + 222, y, 38, 20).method_46431());
        this.addSizeInputRow(controlsX, y += 38, true);
        this.addSizeInputRow(controlsX, y += 30, false);
        y += 30;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \ub9de\ucda4: \uaf49 \ucc44\uc6b0\uae30"), b -> {}).method_46434(controlsX, y += 4, 125, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uc0c9 \uc11e\uae30: " + ProjectState.ditherModeLabel())), b -> {
            ProjectState.cycleDitherMode();
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(controlsX + 135, y, 125, 20).method_46431());
        class_4185 startButton = class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ubbf8\ub9ac\ubcf4\uae30 \ud655\uc778"), b -> {
            this.previewRequested = true;
            this.clearPreviewCache();
        }).method_46434(controlsX, y += 36, 260, 24).method_46431();
        this.method_37063((class_364)startButton);
    }

    private void addStyleButton(String label, ImageStyle style, int x, int y) {
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)((ProjectState.imageStyle == style ? "\u25b6 " : "") + label)), b -> {
            ProjectState.applyImageStyle(class_310.method_1551(), style);
            this.previewRequested = true;
            this.clearPreviewCache();
            this.appliedStylePreview = ProjectState.image;
            this.method_41843();
        }).method_46434(x, y, 92, 20).method_46431());
    }

    private void palette(int px, int py) {
        int x = px + 160;
        int y = py + 78;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)((ProjectState.paletteMode == PaletteMode.MEASURED ? "\u25b6 " : "") + "\uae30\ubcf8 \uc0c9\uc0c1\ud45c")), b -> {
            ProjectState.setPaletteMode(class_310.method_1551(), PaletteMode.MEASURED);
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(x, y, 310, 22).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)((ProjectState.paletteMode == PaletteMode.DOSE ? "\u25b6 " : "") + "\ub3c4\uc2a4 \uc0c9\uc0c1\ud45c")), b -> {
            ProjectState.setPaletteMode(class_310.method_1551(), PaletteMode.DOSE);
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(x, y += 34, 310, 22).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)((ProjectState.colorMatchMode == ColorMatchMode.CLASSIC ? "\u25b6 " : "") + "\uc6d0\ubcf8 \uc0c9\uac10 (\uae30\ubcf8)")), b -> {
            ProjectState.setColorMatchMode(class_310.method_1551(), ColorMatchMode.CLASSIC);
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(x, y += 46, 310, 22).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)((ProjectState.colorMatchMode == ColorMatchMode.SOFT_TONE ? "\u25b6 " : "") + "\uade0\ud615 \uc7a1\ud78c \uc0c9\uac10 (V1)")), b -> {
            ProjectState.setColorMatchMode(class_310.method_1551(), ColorMatchMode.SOFT_TONE);
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(x, y += 30, 310, 22).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)((ProjectState.colorMatchMode == ColorMatchMode.ADAPTIVE_TONE_V2 ? "\u25b6 " : "") + "\ubc1d\uace0 \ubd80\ub4dc\ub7ec\uc6b4 \uc0c9\uac10 (V2)")), b -> {
            ProjectState.setColorMatchMode(class_310.method_1551(), ColorMatchMode.ADAPTIVE_TONE_V2);
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(x, y += 30, 310, 22).method_46431());
    }

    private void work(int px, int py) {
        int x = px + 160;
        int y = py + 75;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ubc14\ub77c\ubcf4\ub294 \uce94\ubc84\uc2a4 \ucc3e\uae30 [C]"), b -> CanvasDetector.detect(class_310.method_1551())).method_46434(x, y, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uadf8\ub9bc \ud45c\uc2dc \ubc29\uc2dd: " + ProjectState.workMode.label())), b -> {
            ProjectState.workMode = ProjectState.workMode.next();
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x + 160, y, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ub2e4\uc74c \uc791\uc5c5 \uc0c9 [P]"), b -> {
            ProjectState.cycleFocus();
            this.method_41843();
        }).method_46434(x, y += 36, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ub2e4\uc74c \ub2e8\uacc4 [O]"), b -> {
            ProjectState.cycleFocusStage();
            this.method_41843();
        }).method_46434(x + 160, y, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ubc14\ub77c\ubcf4\ub294 \uce78 \uc644\ub8cc [X]"), b -> ProjectState.completeAimedPixel()).method_46434(x, y += 36, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ubc14\ub77c\ubcf4\ub294 \uce78 \ub418\ub3cc\ub9ac\uae30 [Z]"), b -> ProjectState.undoAimedPixel()).method_46434(x + 160, y, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\u25c0 \uc774\uc804 \uce94\ubc84\uc2a4"), b -> ProjectState.previousTile(class_310.method_1551())).method_46434(x, y += 36, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ub2e4\uc74c \uce94\ubc84\uc2a4 \u25b6"), b -> ProjectState.nextTile(class_310.method_1551())).method_46434(x + 160, y, 150, 20).method_46431());
    }

    private void access(int px, int py) {
        int x = px + 160;
        int y = py + 75;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uc0c9 \uad6c\ubd84 \ub3c4\uc6c0: " + ProjectState.accessibility.label())), b -> {
            ProjectState.accessibility = ProjectState.accessibility.next();
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y, 310, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\ud604\uc7ac \uce78 \ud45c\uc2dc: " + ProjectState.pixelMarkerMode.label())), b -> {
            ProjectState.pixelMarkerMode = ProjectState.pixelMarkerMode.next();
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y += 38, 310, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uadf8\ub9bc \uc0c9\uc0c1 \ud45c\uc2dc " + (ProjectState.colorOverlayEnabled ? "ON" : "OFF") + " (\ub9c8\ucee4 \uc720\uc9c0)")), b -> {
            ProjectState.colorOverlayEnabled = !ProjectState.colorOverlayEnabled;
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y += 38, 310, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uc790\ub3d9\uc73c\ub85c \uc644\ub8cc \uccb4\ud06c " + (ProjectState.autoProgressEnabled ? "ON" : "OFF"))), b -> {
            ProjectState.autoProgressEnabled = !ProjectState.autoProgressEnabled;
            AutoProgress.reset();
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y += 38, 310, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ud751\ubc31\uc73c\ub85c \ubcf4\uae30"), b -> {
            ProjectState.toggleGrayscale(class_310.method_1551());
            this.clearPreviewCache();
            this.method_41843();
        }).method_46434(x, y += 38, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uce78 \uad6c\ubd84\uc120 " + (ProjectState.grid ? "ON" : "OFF"))), b -> {
            ProjectState.grid = !ProjectState.grid;
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x + 160, y, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uc644\ub8cc\ud55c \uce78 " + (ProjectState.showCompleted ? "\ud45c\uc2dc" : "\uc228\uae40"))), b -> {
            ProjectState.showCompleted = !ProjectState.showCompleted;
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y += 36, 310, 20).method_46431());
    }

    private void tutorial(int px, int py) {
    }

    private void settings(int px, int py) {
        int x = px + 160;
        int y = py + 75;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \uc5f0\ud558\uac8c"), b -> {
            ProjectState.overlayAlpha = Math.max(25, ProjectState.overlayAlpha - 10);
            ProjectState.saveProgress();
        }).method_46434(x, y, 72, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \uc9c4\ud558\uac8c"), b -> {
            ProjectState.overlayAlpha = Math.min(230, ProjectState.overlayAlpha + 10);
            ProjectState.saveProgress();
        }).method_46434(x + 78, y, 72, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \ub4a4\ub85c"), b -> ProjectionState.backward()).method_46434(x + 160, y, 72, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \uc55e\uc73c\ub85c"), b -> ProjectionState.forward()).method_46434(x + 238, y, 72, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"\uadf8\ub9bc \uc704\uce58\ub97c \uce94\ubc84\uc2a4\uc5d0 \ub9de\ucd94\uae30"), b -> ProjectionState.snapToSurface()).method_46434(x, y += 36, 310, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uc791\uc5c5 \uc815\ubcf4\ucc3d " + (ProjectState.showHud ? "ON" : "OFF"))), b -> {
            ProjectState.showHud = !ProjectState.showHud;
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y += 36, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uce94\ubc84\uc2a4 \uadf8\ub9bc \ud45c\uc2dc " + (ProjectState.overlayEnabled ? "ON" : "OFF"))), b -> {
            ProjectState.overlayEnabled = !ProjectState.overlayEnabled;
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x + 160, y, 150, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uce94\ubc84\uc2a4 \uc606 \uc804\uccb4 \ubbf8\ub9ac\ubcf4\uae30 " + (ProjectState.fullPreviewOverlayEnabled ? "ON" : "OFF"))), b -> {
            ProjectState.fullPreviewOverlayEnabled = !ProjectState.fullPreviewOverlayEnabled;
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y += 36, 310, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)("\uc0ac\uc6a9\ubc95 \ud45c\uc2dc " + (ProjectState.showTutorial ? "ON" : "OFF"))), b -> {
            ProjectState.showTutorial = !ProjectState.showTutorial;
            ProjectState.saveProgress();
            this.method_41843();
        }).method_46434(x, y += 36, 310, 20).method_46431());
    }

    public void method_25394(class_332 g, int mouseX, int mouseY, float partialTick) {
        int ty;
        int tx;
        g.method_25294(0, 0, this.field_22789, this.field_22790, 0x33000000);
        int px = (this.field_22789 - 620) / 2;
        int py = (this.field_22790 - 390) / 2;
        g.method_25294(px, py, px + 620, py + 390, -434759648);
        g.method_25294(px, py, px + 620, py + 3, -9524993);
        g.method_25294(px + 8, py + 42, px + 116, py + 390 - 38, -1441984489);
        g.method_25294(px + 120, py + 42, px + 620 - 8, py + 390 - 38, -1978591970);
        g.method_51433(this.field_22793, "ArtMap Workshop", px + 14, py + 14, -1, false);
        g.method_51433(this.field_22793, "Fabric 1.21.4 \u00b7 32\u00d732 Canvas", px + 14, py + 27, -5326906, false);
        String title = switch (this.tab) {
            default -> throw new MatchException(null, null);
            case WorkshopTab.IMAGE -> "\uc774\ubbf8\uc9c0 \uc900\ube44";
            case WorkshopTab.PALETTE -> "\uc0c9\uac10 \uc120\ud0dd";
            case WorkshopTab.WORK -> "\uadf8\ub9bc \uc791\uc5c5";
            case WorkshopTab.ACCESSIBILITY -> "\ubcf4\uae30 \ud3b8\ud558\uac8c";
            case WorkshopTab.SETTINGS -> "\ud654\uba74 \uc124\uc815";
            case WorkshopTab.TUTORIAL -> "\ucc98\uc74c \uc0ac\uc6a9\ud558\ub294 \ubc29\ubc95";
        };
        g.method_51433(this.field_22793, title, px + 130, py + 48, -6306305, false);
        if (this.tab == WorkshopTab.IMAGE) {
            this.renderImagePreview(g, px + 134, py + 76, 190, 190);
            int infoY = py + 302;
            g.method_51433(this.field_22793, this.trim("\uc120\ud0dd \u00b7 " + ProjectState.selectedImageName(), 185), px + 134, infoY, -1, false);
            g.method_51433(this.field_22793, this.previewReady() ? "\ubbf8\ub9ac\ubcf4\uae30 \ud45c\uc2dc \uc911" : "\ud604\uc7ac \ud06c\uae30\ub85c \ubc14\ub85c \ubd88\ub7ec\uc624\uae30 \uac00\ub2a5", px + 134, infoY + 13, -4602926, false);
            g.method_51433(this.field_22793, "\ucd9c\ub825 " + ProjectState.targetImageWidth + "\u00d7" + ProjectState.targetImageHeight + " \u00b7 " + ProjectState.targetImageWidth / 32 + "\u00d7" + ProjectState.targetImageHeight / 32 + " \uce94\ubc84\uc2a4", px + 134, infoY + 26, -142, false);
            g.method_51433(this.field_22793, "\uc544\ub798 \uc21c\uc11c\ub294 \ucd94\ucc9c \uc21c\uc11c\uc774\uba70 \ub2e4\ub978 \uba54\ub274\ub3c4 \uc5b8\uc81c\ub4e0 \uc124\uc815\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.", px + 134, infoY + 43, -7366488, false);
        }
        if (this.tab == WorkshopTab.PALETTE) {
            tx = px + 160;
            ty = py + 264;
            g.method_51433(this.field_22793, "\uc0ac\uc6a9 \uc911\uc778 \uc0c9\uc0c1\ud45c: " + ProjectState.paletteModeLabel(), tx, ty, -142, false);
            g.method_51433(this.field_22793, "\uc120\ud0dd\ud55c \uc0c9\uac10: " + ProjectState.colorMatchModeLabel(), tx, ty + 15, -142, false);
            g.method_51433(this.field_22793, "\uae30\ubcf8 = \uc6d0\ubcf8\uc5d0 \ucda9\uc2e4 \u00b7 V1 = \uade0\ud615 \u00b7 V2 = \ubc1d\uace0 \ubd80\ub4dc\ub7fd\uac8c", tx, ty + 34, -4537391, false);
            g.method_51433(this.field_22793, "\uc0c9\uac10\uc744 \ubc14\uafb8\uba74 \ubbf8\ub9ac\ubcf4\uae30\uc640 \uc2e4\uc81c \uc791\uc5c5 \uc0c9\uc5d0\ub3c4 \uc801\uc6a9\ub429\ub2c8\ub2e4", tx, ty + 49, -7366488, false);
        }
        if (this.tab == WorkshopTab.WORK) {
            int iy = py + 390 - 64;
            g.method_51433(this.field_22793, "\ud604\uc7ac \uc0c9\uc0c1: " + ProjectState.focusDisplay(), px + 160, iy, -120, false);
            g.method_51433(this.field_22793, "\ub0a8\uc740 \ud53d\uc140: " + ProjectState.remainingFocusPixels() + " \u00b7 \uc870\uc900 \ud6c4 X\ub85c \uc644\ub8cc", px + 160, iy + 13, -4537391, false);
        }
        if (this.tab == WorkshopTab.TUTORIAL) {
            tx = px + 142;
            ty = py + 78;
            g.method_51433(this.field_22793, "1. [\uc774\ubbf8\uc9c0 \uc900\ube44]\uc5d0\uc11c \uadf8\ub9bc\uc744 \uc120\ud0dd\ud558\uc138\uc694.", tx, ty, -1, false);
            g.method_51433(this.field_22793, "2. \ud544\uc694\ud558\uba74 \uac00\ub85c\u00b7\uc138\ub85c \uce94\ubc84\uc2a4 \uc218\ub97c \ubc14\uafb8\uc138\uc694 (1~64).", tx, ty + 25, -4537391, false);
            g.method_51433(this.field_22793, "   1\uce78\uc740 32\u00d732 \ud53d\uc140\uc785\ub2c8\ub2e4. \uc608: 3\u00d73 = 96\u00d796 \ud53d\uc140", tx, ty + 40, -7366488, false);
            g.method_51433(this.field_22793, "3. [\uc0c9\uac10 \uc120\ud0dd]\uc5d0\uc11c \uc6d0\ud558\ub294 \uc0c9\uac10\uacfc \uc0c9\uc0c1\ud45c\ub97c \uace0\ub974\uc138\uc694.", tx, ty + 65, -4537391, false);
            g.method_51433(this.field_22793, "4. [\uc120\ud0dd\ud55c \uadf8\ub9bc \ubd88\ub7ec\uc624\uae30]\ub85c \ubc14\ub85c \uc791\uc5c5\ud558\uc138\uc694.", tx, ty + 90, -4537391, false);
            g.method_51433(this.field_22793, "5. \uac8c\uc784\uc5d0\uc11c \uce94\ubc84\uc2a4\ub97c \ubc14\ub77c\ubcf4\uace0 C\ub97c \ub20c\ub7ec \ucc3e\uc73c\uc138\uc694.", tx, ty + 115, -4537391, false);
            g.method_51433(this.field_22793, "6. \ud45c\uc2dc\ub41c \uc0c9\uc744 \ub530\ub77c \uce60\ud558\uba74 \uc790\ub3d9 \uc644\ub8cc \uccb4\ud06c\uac00 \ub3c4\uc640\uc90d\ub2c8\ub2e4.", tx, ty + 140, -4537391, false);
            g.method_51433(this.field_22793, "\uc790\uc8fc \uc4f0\ub294 \ud0a4", tx, ty + 177, -142, false);
            g.method_51433(this.field_22793, "C \uce94\ubc84\uc2a4 \ucc3e\uae30 \u00b7 P \ub2e4\uc74c \uc0c9 \u00b7 O \ub2e4\uc74c \ub2e8\uacc4", tx, ty + 195, -1, false);
            g.method_51433(this.field_22793, "X \uc9c1\uc811 \uc644\ub8cc \u00b7 Z \ub418\ub3cc\ub9ac\uae30 \u00b7 [ ] \uce94\ubc84\uc2a4 \uc774\ub3d9", tx, ty + 212, -1, false);
            g.method_51433(this.field_22793, "F6 \uadf8\ub9bc \uc804\uccb4 \uc228\uae30\uae30 \u00b7 F9 \uc774 \uba54\ub274 \uc5f4\uae30", tx, ty + 229, -1, false);
            g.method_51433(this.field_22793, "\ucc98\uc74c\uc5d0\ub294 \uae30\ubcf8 \uc124\uc815 \uadf8\ub300\ub85c \uc2dc\uc791\ud574\ub3c4 \uad1c\ucc2e\uc2b5\ub2c8\ub2e4.", tx, ty + 260, -6306305, false);
        }
        super.method_25394(g, mouseX, mouseY, partialTick);
    }

    private void renderImagePreview(class_332 g, int x, int y, int boxW, int boxH) {
        g.method_25294(x - 2, y - 2, x + boxW + 2, y + boxH + 2, -13617339);
        g.method_25294(x, y, x + boxW, y + boxH, -15592165);
        if (!this.previewReady()) {
            g.method_25300(this.field_22793, "\uadf8\ub9bc\uc744 \ubd88\ub7ec\uc624\uac70\ub098", x + boxW / 2, y + boxH / 2 - 11, -4602926);
            g.method_25300(this.field_22793, "\ubbf8\ub9ac\ubcf4\uae30 \ud655\uc778\uc744 \ub20c\ub7ec\uc8fc\uc138\uc694", x + boxW / 2, y + boxH / 2 + 3, -7366488);
            return;
        }
        this.ensurePreviewImage();
        if (this.previewImage == null) {
            g.method_25300(this.field_22793, "\uc774\ubbf8\uc9c0 \uc5c6\uc74c", x + boxW / 2, y + boxH / 2 - 4, -7366488);
            return;
        }
        int targetW = Math.max(32, this.previewImage.getWidth());
        int targetH = Math.max(32, this.previewImage.getHeight());
        double displayScale = Math.min((double)boxW / (double)targetW, (double)boxH / (double)targetH);
        int outW = Math.max(1, (int)Math.round((double)targetW * displayScale));
        int outH = Math.max(1, (int)Math.round((double)targetH * displayScale));
        int ox = x + (boxW - outW) / 2;
        int oy = y + (boxH - outH) / 2;
        g.method_25294(ox, oy, ox + outW, oy + outH, -14341581);
        for (int dy = 0; dy < outH; ++dy) {
            for (int dx = 0; dx < outW; ++dx) {
                int sy;
                int sx = Math.min(targetW - 1, (int)Math.floor((double)dx / (double)outW * (double)targetW));
                int argb = this.previewImage.getRGB(sx, sy = Math.min(targetH - 1, (int)Math.floor((double)dy / (double)outH * (double)targetH)));
                int alpha = argb >>> 24 & 0xFF;
                if (alpha == 0) continue;
                int rgb = argb & 0xFFFFFF;
                g.method_25294(ox + dx, oy + dy, ox + dx + 1, oy + dy + 1, 0xFF000000 | rgb);
            }
        }
        int cols = Math.max(1, ProjectState.targetImageWidth / 32);
        int rows = Math.max(1, ProjectState.targetImageHeight / 32);
        for (int c = 1; c < cols; ++c) {
            int gx = ox + (int)Math.round((double)outW * ((double)c / (double)cols));
            g.method_25294(gx, oy, gx + 1, oy + outH, -1426063361);
        }
        for (int r = 1; r < rows; ++r) {
            int gy = oy + (int)Math.round((double)outH * ((double)r / (double)rows));
            g.method_25294(ox, gy, ox + outW, gy + 1, -1426063361);
        }
    }

    private void ensurePreviewImage() {
        String key;
        Path selected = ProjectState.selectedImageFile();
        String string = key = selected == null ? "" : String.valueOf(selected.toAbsolutePath()) + "|" + ProjectState.targetImageWidth + "x" + ProjectState.targetImageHeight + "|" + ProjectState.ditherMode.name() + "|" + ProjectState.grayscale + "|" + ProjectState.paletteMode.name() + "|" + ProjectState.colorMatchMode.name() + "|" + ProjectState.imageStyle.name();
        if (key.equals(this.previewKey)) {
            return;
        }
        this.previewKey = key;
        this.previewImage = null;
        if (selected == null) {
            return;
        }
        if (this.appliedStylePreview != null && this.appliedStylePreview == ProjectState.image) {
            this.previewImage = Image32Processor.previewOfWork(this.appliedStylePreview);
            return;
        }
        this.previewImage = Image32Processor.buildPreviewImage(selected, ProjectState.targetImageWidth, ProjectState.targetImageHeight, ProjectState.ditherMode, ProjectState.grayscale, ProjectState.colorMatchMode);
    }

    private void clearPreviewCache() {
        this.appliedStylePreview = null;
        ProjectionRenderer.clearLivePreview();
        this.previewKey = "";
        this.previewImage = null;
    }

    private boolean previewReady() {
        return this.previewRequested;
    }

    private void resetPreviewReadiness() {
        this.appliedStylePreview = null;
        ProjectionRenderer.clearLivePreview();
        this.previewRequested = false;
        this.previewImage = null;
        this.previewKey = "";
    }

    private String trim(String text, int widthLimit) {
        if (this.field_22793.method_1727(text) <= widthLimit) {
            return text;
        }
        String suffix = "...";
        String work = text;
        while (!work.isEmpty() && this.field_22793.method_1727(work + suffix) > widthLimit) {
            work = work.substring(0, work.length() - 1);
        }
        return work + suffix;
    }

    public boolean method_25421() {
        return false;
    }

    private void addSizeInputRow(int x, int y, boolean widthInput) {
        int currentPixels = widthInput ? ProjectState.targetImageWidth : ProjectState.targetImageHeight;
        int currentCanvases = Math.max(1, currentPixels / 32);
        class_342 input = new class_342(this.field_22793, x, y, 196, 20, (class_2561)class_2561.method_43470((String)(widthInput ? "\uac00\ub85c \uce94\ubc84\uc2a4 \uc218" : "\uc138\ub85c \uce94\ubc84\uc2a4 \uc218")));
        input.method_1880(2);
        input.method_1890(value -> value.isEmpty() || value.matches("[0-9]{1,2}") && Integer.parseInt(value) >= 1 && Integer.parseInt(value) <= 64);
        input.method_1852(Integer.toString(currentCanvases));
        input.method_47404((class_2561)class_2561.method_43470((String)((widthInput ? "\uac00\ub85c" : "\uc138\ub85c") + " \uce78 \uc218 (1\uce78=32px)")));
        input.method_1863(value -> this.applySizeInput(input, widthInput));
        this.method_37063((class_364)input);
        class_4185 label = class_4185.method_46430((class_2561)class_2561.method_43470((String)(widthInput ? "\uac00\ub85c" : "\uc138\ub85c")), b -> {}).method_46434(x + 204, y, 56, 20).method_46431();
        label.field_22763 = false;
        this.method_37063((class_364)label);
    }

    private void applySizeInput(class_342 input, boolean widthInput) {
        try {
            int requestedCanvases = Integer.parseInt(input.method_1882());
            int canvases = Math.max(1, Math.min(64, requestedCanvases));
            int pixels = canvases * 32;
            if (widthInput) {
                ProjectState.setTargetImageWidth(pixels);
            } else {
                ProjectState.setTargetImageHeight(pixels);
            }
            this.previewRequested = false;
            this.clearPreviewCache();
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }
}

