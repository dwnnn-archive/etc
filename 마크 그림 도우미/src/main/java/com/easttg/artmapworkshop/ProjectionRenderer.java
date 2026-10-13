/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
 *  net.minecraft.class_1011
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_1533
 *  net.minecraft.class_1921
 *  net.minecraft.class_2350
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  org.joml.Matrix4f
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.AccessibilityMode;
import com.easttg.artmapworkshop.ArtImage;
import com.easttg.artmapworkshop.CanvasDetector;
import com.easttg.artmapworkshop.PaletteColor;
import com.easttg.artmapworkshop.PixelMarkerMode;
import com.easttg.artmapworkshop.ProjectState;
import com.easttg.artmapworkshop.ProjectionState;
import com.easttg.artmapworkshop.WorkMode;
import java.awt.image.BufferedImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_1533;
import net.minecraft.class_1921;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import org.joml.Matrix4f;

@Environment(value=EnvType.CLIENT)
public final class ProjectionRenderer {
    private static final double FULL_PREVIEW_SIZE_PER_CANVAS = 0.5;
    private static final double FULL_PREVIEW_GAP_CANVASES = 1.0;
    private static final double FULL_PREVIEW_BRIGHTNESS_GAMMA = 0.82;
    private static final int FULL_PREVIEW_MAX_TEXTURE_SIZE = 2048;
    private static final class_2960 FULL_PREVIEW_TEXTURE = class_2960.method_60655((String)"artmapworkshop", (String)"full_preview");
    private static Object cachedPreviewImage;
    private static Object failedPreviewImage;
    private static BufferedImage livePreview;
    private static ArtImage livePreviewWork;
    private static int liveColumns;
    private static int liveRows;
    private static class_1043 cachedPreviewTexture;

    public static void setLivePreview(BufferedImage preview, int columns, int rows) {
        livePreview = preview;
        livePreviewWork = ProjectState.image;
        liveColumns = Math.max(1, columns);
        liveRows = Math.max(1, rows);
    }

    public static void clearLivePreview() {
        livePreview = null;
        livePreviewWork = null;
    }

    private static boolean hasLivePreview(ArtImage image) {
        if (livePreviewWork != image) {
            ProjectionRenderer.clearLivePreview();
        }
        return livePreview != null;
    }

    private ProjectionRenderer() {
    }

    public static void render(WorldRenderContext context) {
        if (!ProjectionState.enabled) {
            return;
        }
        if (!ProjectState.overlayEnabled && !ProjectState.fullPreviewOverlayEnabled) {
            return;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        class_1533 frame = CanvasDetector.target(mc);
        if (frame == null) {
            return;
        }
        class_4587 poseStack = context.matrixStack();
        class_4597 consumers = context.consumers();
        if (poseStack == null || consumers == null) {
            return;
        }
        class_4588 out = consumers.getBuffer(class_1921.method_49042());
        class_243 camera = context.camera().method_19326();
        Basis basis = ProjectionRenderer.basis(frame);
        double usable = ProjectionState.planeSize * (1.0 - ProjectionState.inset * 2.0);
        double cellSize = usable / 32.0;
        class_243 center = frame.method_19538().method_1019(basis.normal.method_1021(ProjectionState.surfaceOffset));
        class_243 topLeft = center.method_1019(basis.right.method_1021(-usable / 2.0)).method_1019(basis.up.method_1021(usable / 2.0));
        poseStack.method_22903();
        Matrix4f matrix = poseStack.method_23760().method_23761();
        if (ProjectState.overlayEnabled) {
            for (int y = 0; y < 32; ++y) {
                for (int x = 0; x < 32; ++x) {
                    boolean cursorCell;
                    int alpha;
                    int b;
                    int g;
                    int r;
                    if (ProjectState.image == null) {
                        boolean light = (x + y & 1) == 0;
                        r = light ? 190 : 158;
                        g = light ? 193 : 164;
                        b = light ? 191 : 162;
                        alpha = 105;
                    } else {
                        ArtImage.Cell cell = ProjectState.image.cell(ProjectState.tile, x, y);
                        if (cell.emptyPixel() || !ProjectState.visible(cell, x, y)) continue;
                        PaletteColor color = cell.color();
                        r = color.r();
                        g = color.g();
                        b = color.b();
                        alpha = Math.min(ProjectState.overlayAlpha, 120);
                        if (ProjectState.isCompleted(x, y)) continue;
                        if (ProjectState.accessibility == AccessibilityMode.HIGH_CONTRAST) {
                            double lum = 0.299 * (double)r + 0.587 * (double)g + 0.114 * (double)b;
                            if (lum >= 128.0) {
                                b = 255;
                                g = 255;
                                r = 255;
                            } else {
                                b = 0;
                                g = 0;
                                r = 0;
                            }
                            alpha = Math.min(220, alpha + 45);
                        }
                        if (ProjectState.accessibility == AccessibilityMode.LOW_VISION && (x != ProjectState.cursorX || y != ProjectState.cursorY) && ProjectState.workMode == WorkMode.CURSOR) {
                            alpha = 20;
                        }
                    }
                    class_243 p0 = topLeft.method_1019(basis.right.method_1021((double)x * cellSize)).method_1019(basis.up.method_1021((double)(-y) * cellSize)).method_1020(camera);
                    class_243 p1 = p0.method_1019(basis.right.method_1021(cellSize));
                    class_243 p2 = p1.method_1019(basis.up.method_1021(-cellSize));
                    class_243 p3 = p0.method_1019(basis.up.method_1021(-cellSize));
                    if (ProjectState.colorOverlayEnabled) {
                        ProjectionRenderer.vertex(out, matrix, p0, r, g, b, alpha);
                        ProjectionRenderer.vertex(out, matrix, p1, r, g, b, alpha);
                        ProjectionRenderer.vertex(out, matrix, p2, r, g, b, alpha);
                        ProjectionRenderer.vertex(out, matrix, p3, r, g, b, alpha);
                    }
                    if (ProjectState.image != null && ProjectState.pixelMarkerMode != PixelMarkerMode.OFF) {
                        boolean focusMatch;
                        ArtImage.Cell markerCell = ProjectState.image.cell(ProjectState.tile, x, y);
                        boolean bl = focusMatch = ProjectState.focusKey != null && !ProjectState.focusKey.isEmpty() && !markerCell.emptyPixel() && ProjectState.focusMatches(markerCell.color());
                        if (focusMatch && !ProjectState.isCompleted(x, y)) {
                            ProjectionRenderer.drawPixelMarker(out, matrix, p0, basis, cellSize, markerCell.color());
                        }
                    }
                    boolean bl = cursorCell = x == ProjectState.cursorX && y == ProjectState.cursorY;
                    if (cursorCell) {
                        double e = cellSize * 0.045;
                        ProjectionRenderer.drawOutline(out, matrix, p0, basis.right, basis.up, cellSize, e, 255, 255, 255, 235);
                        continue;
                    }
                    if (!ProjectState.grid) continue;
                    double e = cellSize * 0.025;
                    ProjectionRenderer.drawOutline(out, matrix, p0, basis.right, basis.up, cellSize, e, 225, 225, 225, 24);
                }
            }
        }
        if (ProjectState.fullPreviewOverlayEnabled && ProjectState.image != null) {
            ProjectionRenderer.drawFullPreview(consumers, out, matrix, camera, center, basis, usable);
        }
        poseStack.method_22909();
    }

    private static void drawFullPreview(class_4597 consumers, class_4588 out, Matrix4f matrix, class_243 camera, class_243 canvasCenter, Basis basis, double canvasSize) {
        ArtImage image = ProjectState.image;
        boolean live = ProjectionRenderer.hasLivePreview(image);
        int columns = live ? liveColumns : image.tilesX();
        int rows = live ? liveRows : image.tilesY();
        double panelWidth = (double)columns * 0.5;
        double panelHeight = (double)rows * 0.5;
        double tileWidth = 0.5;
        double tileHeight = 0.5;
        class_243 canvasBottom = canvasCenter.method_1019(basis.up.method_1021(-canvasSize / 2.0));
        class_243 panelBottomLeft = canvasBottom.method_1019(basis.right.method_1021(canvasSize / 2.0 + canvasSize * 1.0)).method_1019(basis.normal.method_1021(0.002));
        class_243 topLeft = panelBottomLeft.method_1019(basis.up.method_1021(panelHeight)).method_1020(camera);
        class_243 bg0 = topLeft.method_1019(basis.right.method_1021(-0.04)).method_1019(basis.up.method_1021(0.04)).method_1019(basis.normal.method_1021(-0.001));
        class_243 bg1 = bg0.method_1019(basis.right.method_1021(panelWidth + 0.08));
        class_243 bg2 = bg1.method_1019(basis.up.method_1021(-(panelHeight + 0.08)));
        class_243 bg3 = bg0.method_1019(basis.up.method_1021(-(panelHeight + 0.08)));
        ProjectionRenderer.quad(out, matrix, bg0, bg1, bg2, bg3, 8, 10, 14, 210);
        if (ProjectionRenderer.ensureFullPreviewTexture(image)) {
            class_4588 textured = consumers.getBuffer(class_1921.method_23028((class_2960)FULL_PREVIEW_TEXTURE));
            class_243 p1 = topLeft.method_1019(basis.right.method_1021(panelWidth));
            class_243 p2 = p1.method_1019(basis.up.method_1021(-panelHeight));
            class_243 p3 = topLeft.method_1019(basis.up.method_1021(-panelHeight));
            boolean reverse = basis.right.method_1036(basis.up).method_1026(basis.normal) > 0.0;
            ProjectionRenderer.texturedVertex(textured, matrix, topLeft, 0.0f, 0.0f);
            ProjectionRenderer.texturedVertex(textured, matrix, reverse ? p3 : p1, reverse ? 0.0f : 1.0f, reverse ? 1.0f : 0.0f);
            ProjectionRenderer.texturedVertex(textured, matrix, p2, 1.0f, 1.0f);
            ProjectionRenderer.texturedVertex(textured, matrix, reverse ? p1 : p3, reverse ? 1.0f : 0.0f, reverse ? 0.0f : 1.0f);
        }
        out = consumers.getBuffer(class_1921.method_49042());
        double border = 0.018;
        class_243 borderTopLeft = topLeft.method_1019(basis.normal.method_1021(6.0E-4));
        ProjectionRenderer.drawRectOutline(out, matrix, borderTopLeft, basis.right, basis.up, panelWidth, panelHeight, border, 235, 235, 235, 230);
        if (columns != image.tilesX() || rows != image.tilesY()) {
            return;
        }
        int currentTileX = ProjectState.tile % image.tilesX();
        int currentTileY = ProjectState.tile / image.tilesX();
        class_243 currentTopLeft = topLeft.method_1019(basis.right.method_1021((double)currentTileX * tileWidth)).method_1019(basis.up.method_1021((double)(-currentTileY) * tileHeight)).method_1019(basis.normal.method_1021(8.0E-4));
        ProjectionRenderer.drawRectOutline(out, matrix, currentTopLeft, basis.right, basis.up, tileWidth, tileHeight, border, 255, 224, 72, 245);
    }

    private static boolean ensureFullPreviewTexture(ArtImage image) {
        Object textureSource;
        BufferedImage preview = ProjectionRenderer.hasLivePreview(image) ? livePreview : null;
        Object object = textureSource = preview == null ? image : preview;
        if (cachedPreviewImage == textureSource && cachedPreviewTexture != null) {
            return true;
        }
        if (failedPreviewImage == textureSource) {
            return false;
        }
        int sourceWidth = preview == null ? image.tilesX() * 32 : preview.getWidth();
        int sourceHeight = preview == null ? image.tilesY() * 32 : preview.getHeight();
        double scale = Math.min(1.0, Math.min(2048.0 / (double)sourceWidth, 2048.0 / (double)sourceHeight));
        int width = Math.max(1, (int)Math.floor((double)sourceWidth * scale));
        int height = Math.max(1, (int)Math.floor((double)sourceHeight * scale));
        class_1011 pixels = null;
        class_1043 nextTexture = null;
        try {
            pixels = new class_1011(width, height, true);
            for (int y = 0; y < height; ++y) {
                int sourceY = Math.min(sourceHeight - 1, (int)(((double)y + 0.5) * (double)sourceHeight / (double)height));
                for (int x = 0; x < width; ++x) {
                    int sourceX = Math.min(sourceWidth - 1, (int)(((double)x + 0.5) * (double)sourceWidth / (double)width));
                    int tileX = sourceX / 32;
                    int tileY = sourceY / 32;
                    if (preview != null) {
                        int argb = preview.getRGB(sourceX, sourceY);
                        pixels.method_61941(x, y, argb & 0xFF000000 | ProjectionRenderer.brightenPreviewChannel(argb >>> 16 & 0xFF) << 16 | ProjectionRenderer.brightenPreviewChannel(argb >>> 8 & 0xFF) << 8 | ProjectionRenderer.brightenPreviewChannel(argb & 0xFF));
                        continue;
                    }
                    ArtImage.Cell cell = image.cell(tileY * image.tilesX() + tileX, sourceX % 32, sourceY % 32);
                    if (cell.emptyPixel()) {
                        pixels.method_61941(x, y, 0);
                        continue;
                    }
                    PaletteColor color = cell.color();
                    int red = ProjectionRenderer.brightenPreviewChannel(color.r());
                    int green = ProjectionRenderer.brightenPreviewChannel(color.g());
                    int blue = ProjectionRenderer.brightenPreviewChannel(color.b());
                    int argb = Math.min(255, cell.alpha()) << 24 | red << 16 | green << 8 | blue;
                    pixels.method_61941(x, y, argb);
                }
            }
            class_310 mc = class_310.method_1551();
            if (cachedPreviewTexture != null) {
                mc.method_1531().method_4615(FULL_PREVIEW_TEXTURE);
                cachedPreviewTexture = null;
                cachedPreviewImage = null;
            }
            nextTexture = new class_1043(pixels);
            pixels = null;
            mc.method_1531().method_4616(FULL_PREVIEW_TEXTURE, (class_1044)nextTexture);
            nextTexture.method_4524();
            cachedPreviewTexture = nextTexture;
            cachedPreviewImage = textureSource;
            failedPreviewImage = null;
            return true;
        }
        catch (Throwable error) {
            if (nextTexture != null) {
                nextTexture.close();
            }
            if (pixels != null) {
                pixels.close();
            }
            failedPreviewImage = textureSource;
            System.err.println("[ArtMap Workshop] Full preview texture was disabled safely: " + error.getClass().getSimpleName() + ": " + error.getMessage());
            return false;
        }
    }

    private static int brightenPreviewChannel(int channel) {
        double normalized = (double)Math.max(0, Math.min(255, channel)) / 255.0;
        return (int)Math.round(Math.pow(normalized, 0.82) * 255.0);
    }

    private static void texturedVertex(class_4588 out, Matrix4f matrix, class_243 point, float u, float v) {
        out.method_22918(matrix, (float)point.field_1352, (float)point.field_1351, (float)point.field_1350).method_1336(255, 255, 255, 255).method_22913(u, v).method_60803(0xF000F0);
    }

    private static void drawRectOutline(class_4588 out, Matrix4f matrix, class_243 p0, class_243 right, class_243 up, double width, double height, double e, int r, int g, int b, int a) {
        class_243 top1 = p0.method_1019(right.method_1021(width));
        ProjectionRenderer.quad(out, matrix, p0, top1, top1.method_1019(up.method_1021(-e)), p0.method_1019(up.method_1021(-e)), r, g, b, a);
        class_243 bottom0 = p0.method_1019(up.method_1021(-height + e));
        class_243 bottom1 = bottom0.method_1019(right.method_1021(width));
        ProjectionRenderer.quad(out, matrix, bottom0, bottom1, bottom1.method_1019(up.method_1021(-e)), bottom0.method_1019(up.method_1021(-e)), r, g, b, a);
        class_243 left0 = p0.method_1019(up.method_1021(-e));
        ProjectionRenderer.quad(out, matrix, left0, left0.method_1019(right.method_1021(e)), left0.method_1019(right.method_1021(e)).method_1019(up.method_1021(-(height - 2.0 * e))), left0.method_1019(up.method_1021(-(height - 2.0 * e))), r, g, b, a);
        class_243 right0 = p0.method_1019(right.method_1021(width - e)).method_1019(up.method_1021(-e));
        ProjectionRenderer.quad(out, matrix, right0, right0.method_1019(right.method_1021(e)), right0.method_1019(right.method_1021(e)).method_1019(up.method_1021(-(height - 2.0 * e))), right0.method_1019(up.method_1021(-(height - 2.0 * e))), r, g, b, a);
    }

    private static void drawPixelMarker(class_4588 out, Matrix4f matrix, class_243 p0, Basis basis, double cellSize, PaletteColor targetColor) {
        double lum;
        double outerSize = cellSize * 0.42;
        double innerSize = cellSize * 0.27;
        double outerMargin = (cellSize - outerSize) / 2.0;
        double innerMargin = (cellSize - innerSize) / 2.0;
        class_243 lift = basis.normal.method_1021(4.5E-4);
        class_243 outer0 = p0.method_1019(basis.right.method_1021(outerMargin)).method_1019(basis.up.method_1021(-outerMargin)).method_1019(lift);
        class_243 outer1 = outer0.method_1019(basis.right.method_1021(outerSize));
        class_243 outer2 = outer1.method_1019(basis.up.method_1021(-outerSize));
        class_243 outer3 = outer0.method_1019(basis.up.method_1021(-outerSize));
        ProjectionRenderer.quad(out, matrix, outer0, outer1, outer2, outer3, 0, 0, 0, 235);
        int mr = 255;
        int mg = 255;
        int mb = 255;
        if (ProjectState.pixelMarkerMode == PixelMarkerMode.AUTO_CONTRAST && (lum = 0.299 * (double)targetColor.r() + 0.587 * (double)targetColor.g() + 0.114 * (double)targetColor.b()) >= 150.0) {
            mb = 0;
            mg = 0;
            mr = 0;
        }
        class_243 inner0 = p0.method_1019(basis.right.method_1021(innerMargin)).method_1019(basis.up.method_1021(-innerMargin)).method_1019(basis.normal.method_1021(6.0E-4));
        class_243 inner1 = inner0.method_1019(basis.right.method_1021(innerSize));
        class_243 inner2 = inner1.method_1019(basis.up.method_1021(-innerSize));
        class_243 inner3 = inner0.method_1019(basis.up.method_1021(-innerSize));
        ProjectionRenderer.quad(out, matrix, inner0, inner1, inner2, inner3, mr, mg, mb, 250);
    }

    private static void drawOutline(class_4588 out, Matrix4f matrix, class_243 p0, class_243 right, class_243 up, double size, double e, int r, int g, int b, int a) {
        class_243 top0 = p0;
        class_243 top1 = p0.method_1019(right.method_1021(size));
        class_243 top2 = top1.method_1019(up.method_1021(-e));
        class_243 top3 = top0.method_1019(up.method_1021(-e));
        ProjectionRenderer.quad(out, matrix, top0, top1, top2, top3, r, g, b, a);
        class_243 bottom0 = p0.method_1019(up.method_1021(-size + e));
        class_243 bottom1 = bottom0.method_1019(right.method_1021(size));
        class_243 bottom2 = bottom1.method_1019(up.method_1021(-e));
        class_243 bottom3 = bottom0.method_1019(up.method_1021(-e));
        ProjectionRenderer.quad(out, matrix, bottom0, bottom1, bottom2, bottom3, r, g, b, a);
        class_243 left0 = p0.method_1019(up.method_1021(-e));
        class_243 left1 = left0.method_1019(right.method_1021(e));
        class_243 left2 = left1.method_1019(up.method_1021(-(size - 2.0 * e)));
        class_243 left3 = left0.method_1019(up.method_1021(-(size - 2.0 * e)));
        ProjectionRenderer.quad(out, matrix, left0, left1, left2, left3, r, g, b, a);
        class_243 right0 = p0.method_1019(right.method_1021(size - e)).method_1019(up.method_1021(-e));
        class_243 right1 = right0.method_1019(right.method_1021(e));
        class_243 right2 = right1.method_1019(up.method_1021(-(size - 2.0 * e)));
        class_243 right3 = right0.method_1019(up.method_1021(-(size - 2.0 * e)));
        ProjectionRenderer.quad(out, matrix, right0, right1, right2, right3, r, g, b, a);
    }

    private static void quad(class_4588 out, Matrix4f matrix, class_243 a, class_243 b, class_243 c, class_243 d, int r, int g, int bl, int alpha) {
        ProjectionRenderer.vertex(out, matrix, a, r, g, bl, alpha);
        ProjectionRenderer.vertex(out, matrix, b, r, g, bl, alpha);
        ProjectionRenderer.vertex(out, matrix, c, r, g, bl, alpha);
        ProjectionRenderer.vertex(out, matrix, d, r, g, bl, alpha);
    }

    private static void vertex(class_4588 out, Matrix4f matrix, class_243 p, int r, int g, int b, int a) {
        out.method_22918(matrix, (float)p.field_1352, (float)p.field_1351, (float)p.field_1350).method_1336(r, g, b, a);
    }

    private static Basis basis(class_1533 frame) {
        class_243 right;
        class_243 up;
        class_2350 facing = frame.method_5735();
        class_243 normal = new class_243((double)facing.method_10148(), (double)facing.method_10164(), (double)facing.method_10165());
        if (facing.method_10166().method_10179()) {
            up = new class_243(0.0, 1.0, 0.0);
            right = up.method_1036(normal).method_1029();
        } else {
            right = new class_243(1.0, 0.0, 0.0);
            up = normal.method_1036(right).method_1029();
            if (facing == class_2350.field_11033) {
                up = up.method_1021(-1.0);
            }
        }
        return new Basis(right, up, normal);
    }

    @Environment(value=EnvType.CLIENT)
    private record Basis(class_243 right, class_243 up, class_243 normal) {
    }
}

