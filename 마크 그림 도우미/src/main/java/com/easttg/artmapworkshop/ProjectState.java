/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.AccessibilityMode;
import com.easttg.artmapworkshop.AimTracker;
import com.easttg.artmapworkshop.ArtImage;
import com.easttg.artmapworkshop.AutoProgress;
import com.easttg.artmapworkshop.ColorMatchMode;
import com.easttg.artmapworkshop.DitherMode;
import com.easttg.artmapworkshop.DitherProcessor;
import com.easttg.artmapworkshop.Image32Processor;
import com.easttg.artmapworkshop.ImageStyle;
import com.easttg.artmapworkshop.PaletteColor;
import com.easttg.artmapworkshop.PaletteLoader;
import com.easttg.artmapworkshop.PaletteMode;
import com.easttg.artmapworkshop.PixelMarkerMode;
import com.easttg.artmapworkshop.ProjectionRenderer;
import com.easttg.artmapworkshop.ProjectionState;
import com.easttg.artmapworkshop.Resize32Mode;
import com.easttg.artmapworkshop.WorkMode;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2561;
import net.minecraft.class_310;

@Environment(value=EnvType.CLIENT)
public final class ProjectState {
    public static ArtImage image;
    public static int tile;
    public static int row;
    public static int cursorX;
    public static int cursorY;
    public static WorkMode workMode;
    public static AccessibilityMode accessibility;
    public static PixelMarkerMode pixelMarkerMode;
    public static boolean grayscale;
    public static boolean overlayEnabled;
    public static boolean fullPreviewOverlayEnabled;
    public static boolean colorOverlayEnabled;
    public static boolean grid;
    public static boolean showHud;
    public static boolean showCompleted;
    public static boolean showTutorial;
    public static boolean autoProgressEnabled;
    public static int overlayAlpha;
    public static String focusKey;
    public static String focusAction;
    public static String status;
    public static Resize32Mode resize32Mode;
    public static DitherMode ditherMode;
    public static PaletteMode paletteMode;
    public static ColorMatchMode colorMatchMode;
    public static ImageStyle imageStyle;
    public static Path preparedImageFile;
    public static int targetImageWidth;
    public static int targetImageHeight;
    private static String targetSizeSourceName;
    private static String currentWorkId;
    private static boolean legacyProgressMigratedThisSession;
    public static final BitSet completedTiles;
    public static final Map<Integer, BitSet> completedCells;
    public static final List<Path> imageFiles;
    public static int selectedImageIndex;
    private static boolean progressDirty;
    private static long progressSaveDue;

    private ProjectState() {
    }

    public static Path rootDir() {
        return FabricLoader.getInstance().getConfigDir().resolve("artmap_workshop");
    }

    public static Path imageDir() {
        return ProjectState.rootDir().resolve("images");
    }

    public static Path progressFile() {
        return ProjectState.rootDir().resolve("progress.json");
    }

    public static void refreshImageList() {
        try {
            Files.createDirectories(ProjectState.imageDir(), new FileAttribute[0]);
            imageFiles.clear();
            try (Stream<Path> stream = Files.list(ProjectState.imageDir());){
                stream.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(ProjectState::isSupportedImage).sorted(Comparator.comparing(p -> p.getFileName().toString().toLowerCase(Locale.ROOT))).forEach(imageFiles::add);
            }
            selectedImageIndex = imageFiles.isEmpty() ? 0 : Math.max(0, Math.min(selectedImageIndex, imageFiles.size() - 1));
        }
        catch (Exception e) {
            imageFiles.clear();
            selectedImageIndex = 0;
            e.printStackTrace();
        }
    }

    private static boolean isSupportedImage(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".webp");
    }

    public static Path selectedImageFile() {
        ProjectState.refreshImageList();
        if (imageFiles.isEmpty()) {
            return null;
        }
        return imageFiles.get(selectedImageIndex);
    }

    public static String selectedImageName() {
        Path p = ProjectState.selectedImageFile();
        return p == null ? "\uc774\ubbf8\uc9c0 \uc5c6\uc74c" : p.getFileName().toString();
    }

    public static void nextImage(class_310 mc) {
        ProjectState.refreshImageList();
        if (imageFiles.isEmpty()) {
            ProjectState.message(mc, "ArtMap: images \ud3f4\ub354\uc5d0 \uc774\ubbf8\uc9c0\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.");
            return;
        }
        selectedImageIndex = (selectedImageIndex + 1) % imageFiles.size();
        targetSizeSourceName = "";
        ProjectState.message(mc, "\uc120\ud0dd \uc774\ubbf8\uc9c0: " + ProjectState.selectedImageName());
    }

    public static void previousImage(class_310 mc) {
        ProjectState.refreshImageList();
        if (imageFiles.isEmpty()) {
            ProjectState.message(mc, "ArtMap: images \ud3f4\ub354\uc5d0 \uc774\ubbf8\uc9c0\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.");
            return;
        }
        selectedImageIndex = (selectedImageIndex - 1 + imageFiles.size()) % imageFiles.size();
        targetSizeSourceName = "";
        ProjectState.message(mc, "\uc120\ud0dd \uc774\ubbf8\uc9c0: " + ProjectState.selectedImageName());
    }

    public static void openImageFolder(class_310 mc) {
        Path dir = ProjectState.imageDir().toAbsolutePath().normalize();
        try {
            Files.createDirectories(dir, new FileAttribute[0]);
        }
        catch (Exception e) {
            ProjectState.message(mc, "\uc774\ubbf8\uc9c0 \ud3f4\ub354 \uc0dd\uc131 \uc2e4\ud328: " + String.valueOf(dir));
            e.printStackTrace();
            return;
        }
        try {
            String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
            if (os.contains("win")) {
                new ProcessBuilder("explorer.exe", dir.toString()).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", dir.toString()).start();
            } else {
                new ProcessBuilder("xdg-open", dir.toString()).start();
            }
            ProjectState.message(mc, "ArtMap \uc774\ubbf8\uc9c0 \ud3f4\ub354\ub97c \uc5f4\uc5c8\uc2b5\ub2c8\ub2e4: " + String.valueOf(dir));
            return;
        }
        catch (Exception processError) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    Desktop.getDesktop().open(dir.toFile());
                    ProjectState.message(mc, "ArtMap \uc774\ubbf8\uc9c0 \ud3f4\ub354\ub97c \uc5f4\uc5c8\uc2b5\ub2c8\ub2e4: " + String.valueOf(dir));
                    return;
                }
            }
            catch (Exception desktopError) {
                desktopError.addSuppressed(processError);
                desktopError.printStackTrace();
            }
            processError.printStackTrace();
            ProjectState.message(mc, "\ud3f4\ub354\ub97c \uc790\ub3d9\uc73c\ub85c \uc5f4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. \uc9c1\uc811 \uc5f4\uc5b4\uc8fc\uc138\uc694: " + String.valueOf(dir));
            return;
        }
    }

    public static void syncTargetSizeToSelectedImage() {
        Path selected = ProjectState.selectedImageFile();
        if (selected == null) {
            targetImageWidth = 32;
            targetImageHeight = 32;
            targetSizeSourceName = "";
            return;
        }
        String sourceName = selected.toAbsolutePath().toString();
        if (sourceName.equals(targetSizeSourceName)) {
            return;
        }
        try {
            BufferedImage source = ImageIO.read(selected.toFile());
            if (source != null) {
                targetImageWidth = Image32Processor.snap32(source.getWidth());
                targetImageHeight = Image32Processor.snap32(source.getHeight());
                targetSizeSourceName = sourceName;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static int selectedImageWidth() {
        Path p = ProjectState.selectedImageFile();
        if (p == null) {
            return 0;
        }
        try {
            BufferedImage source = ImageIO.read(p.toFile());
            return source == null ? 0 : source.getWidth();
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static int selectedImageHeight() {
        Path p = ProjectState.selectedImageFile();
        if (p == null) {
            return 0;
        }
        try {
            BufferedImage source = ImageIO.read(p.toFile());
            return source == null ? 0 : source.getHeight();
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static void setTargetImageWidth(int value) {
        targetImageWidth = Image32Processor.snap32(value);
    }

    public static void setTargetImageHeight(int value) {
        targetImageHeight = Image32Processor.snap32(value);
    }

    public static void resetTargetSizeToImage() {
        targetSizeSourceName = "";
        ProjectState.syncTargetSizeToSelectedImage();
    }

    public static void setPaletteMode(class_310 mc, PaletteMode mode) {
        if (mode == null || paletteMode == mode) {
            return;
        }
        paletteMode = mode;
        ProjectState.saveProgress();
        if (image != null) {
            if (preparedImageFile != null && Files.exists(preparedImageFile, new LinkOption[0])) {
                ProjectState.loadPreparedImage(mc);
            } else {
                ProjectState.loadImage(mc);
            }
        }
    }

    public static String paletteModeLabel() {
        return paletteMode.label();
    }

    public static void setColorMatchMode(class_310 mc, ColorMatchMode mode) {
        if (mode == null || colorMatchMode == mode) {
            return;
        }
        colorMatchMode = mode;
        ProjectState.saveProgress();
        if (image != null) {
            if (preparedImageFile != null && Files.exists(preparedImageFile, new LinkOption[0])) {
                ProjectState.loadPreparedImage(mc);
            } else {
                ProjectState.loadImage(mc);
            }
        }
    }

    public static String colorMatchModeLabel() {
        return colorMatchMode.label();
    }

    public static void cycleDitherMode() {
        ditherMode = ditherMode.next();
        ProjectState.saveProgress();
    }

    public static String ditherModeLabel() {
        return ditherMode.label();
    }

    public static void cycleResize32Mode() {
        resize32Mode = resize32Mode.next();
        ProjectState.saveProgress();
    }

    public static String resize32ModeLabel() {
        return resize32Mode.label();
    }

    public static void makeSelectedImage128Multiple(class_310 mc) {
        Path selected = ProjectState.selectedImageFile();
        int outputW = Math.max(32, targetImageWidth / 32 * 32);
        int outputH = Math.max(32, targetImageHeight / 32 * 32);
        targetImageWidth = outputW;
        targetImageHeight = outputH;
        resize32Mode = Resize32Mode.STRETCH;
        Path result = Image32Processor.createSized(mc, selected, Resize32Mode.STRETCH, outputW, outputH);
        if (result != null) {
            preparedImageFile = result;
            ProjectState.loadPreparedImage(mc);
        }
    }

    public static void applyImageStyle(class_310 mc, ImageStyle style) {
        ProjectState.flushPendingProgress();
        ProjectState.saveProgress();
        imageStyle = style;
        ProjectionRenderer.clearLivePreview();
        ProjectState.makeSelectedImage128Multiple(mc);
        AutoProgress.reset();
        ProjectState.saveProgress();
    }

    public static void loadPreparedImage(class_310 mc) {
        if (preparedImageFile == null || !Files.exists(preparedImageFile, new LinkOption[0])) {
            ProjectState.message(mc, "ArtMap: \uc0dd\uc131\ub41c 32\ubc30\uc218 \uc774\ubbf8\uc9c0\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.");
            return;
        }
        ProjectState.loadImageFromPath(mc, preparedImageFile);
    }

    private static void loadImageFromPath(class_310 mc, Path selected) {
        ProjectState.flushPendingProgress();
        try {
            BufferedImage source = ImageIO.read(selected.toFile());
            if (source == null) {
                image = null;
                status = "\uc774\ubbf8\uc9c0 \uc77d\uae30 \uc2e4\ud328: " + String.valueOf(selected.getFileName());
                ProjectState.message(mc, "ArtMap: " + status);
                return;
            }
            List<PaletteColor> palette = PaletteLoader.load();
            BufferedImage prepared = DitherProcessor.apply(source, palette, ditherMode, colorMatchMode);
            ArtImage converted = ArtImage.convert(prepared, palette, grayscale, colorMatchMode, imageStyle == ImageStyle.LOGO && ditherMode == DitherMode.OFF);
            currentWorkId = "COLORS11:STYLE1:" + imageStyle.name() + ":" + paletteMode.name() + ":" + colorMatchMode.name() + ":" + ditherMode.name() + ":" + grayscale + ":" + ProjectState.buildWorkId(prepared);
            image = converted;
            tile = 0;
            row = 0;
            cursorX = 0;
            cursorY = 0;
            targetImageWidth = image.tilesX() * 32;
            targetImageHeight = image.tilesY() * 32;
            ProjectState.ensureFocus();
            ProjectState.loadWorkProgress();
            status = String.valueOf(selected.getFileName()) + " | " + source.getWidth() + "x" + source.getHeight() + " | " + image.tilesX() + "x" + image.tilesY() + " | " + image.tileCount() + "\uc7a5 | " + paletteMode.label() + " | " + colorMatchMode.label() + " | " + imageStyle.label() + (grayscale ? " | \ud751\ubc31" : "");
            ProjectState.message(mc, "ArtMap \uc774\ubbf8\uc9c0 \ub85c\ub4dc \uc131\uacf5 | " + status);
        }
        catch (Exception e) {
            image = null;
            currentWorkId = "";
            status = "\ub85c\ub4dc \uc2e4\ud328: " + e.getMessage();
            ProjectState.message(mc, "ArtMap: " + status);
            e.printStackTrace();
        }
    }

    public static void loadImage(class_310 mc) {
        ProjectState.flushPendingProgress();
        ProjectState.makeSelectedImage128Multiple(mc);
    }

    public static void toggleGrayscale(class_310 mc) {
        grayscale = !grayscale;
        ProjectState.loadImage(mc);
        ProjectState.saveProgress();
    }

    public static void nextTile(class_310 mc) {
        if (image == null) {
            return;
        }
        tile = Math.min(image.tileCount() - 1, tile + 1);
        row = 0;
        cursorX = 0;
        cursorY = 0;
        ProjectState.ensureFocus();
        ProjectState.saveProgress();
        ProjectState.message(mc, ProjectState.tileMessage());
    }

    public static void previousTile(class_310 mc) {
        if (image == null) {
            return;
        }
        tile = Math.max(0, tile - 1);
        row = 0;
        cursorX = 0;
        cursorY = 0;
        ProjectState.ensureFocus();
        ProjectState.saveProgress();
        ProjectState.message(mc, ProjectState.tileMessage());
    }

    public static void nextRow() {
        if (row < 31) {
            ++row;
        } else if (image != null && tile < image.tileCount() - 1) {
            ++tile;
            row = 0;
            ProjectState.ensureFocus();
        }
        cursorY = row;
        ProjectState.saveProgress();
    }

    public static void previousRow() {
        if (row > 0) {
            --row;
        } else if (image != null && tile > 0) {
            --tile;
            row = 31;
            ProjectState.ensureFocus();
        }
        cursorY = row;
        ProjectState.saveProgress();
    }

    public static void moveCursor(int dx, int dy) {
        cursorX = Math.max(0, Math.min(31, cursorX + dx));
        row = cursorY = Math.max(0, Math.min(31, cursorY + dy));
        ProjectState.saveProgress();
    }

    public static void cycleFocus() {
        if (image == null) {
            return;
        }
        List<String> keys = image.paintCodesInTile(tile);
        if (keys.isEmpty()) {
            focusKey = "";
            focusAction = "\uae30\ubcf8";
            return;
        }
        int index = keys.indexOf(focusKey);
        focusKey = keys.get((index + 1 + keys.size()) % keys.size());
        focusAction = "\uae30\ubcf8";
        ProjectState.saveProgress();
    }

    public static void cycleFocusStage() {
        if (image == null || focusKey == null || focusKey.isEmpty()) {
            return;
        }
        List<String> stages = List.of("\uae30\ubcf8", "\uae43\ud138 1\ud68c", "\uc11d\ud0c4 1\ud68c", "\uc11d\ud0c4 2\ud68c");
        int index = stages.indexOf(focusAction);
        focusAction = stages.get((index + 1 + stages.size()) % stages.size());
        ProjectState.saveProgress();
    }

    public static void ensureFocus() {
        if (image == null) {
            return;
        }
        List<String> keys = image.paintCodesInTile(tile);
        if (focusKey != null && focusKey.contains("|")) {
            int split = focusKey.indexOf(124);
            String oldAction = focusKey.substring(split + 1);
            focusKey = focusKey.substring(0, split);
            if (oldAction.startsWith("\uae30\ubcf8")) {
                focusAction = "\uae30\ubcf8";
            } else if (!oldAction.isBlank()) {
                focusAction = oldAction;
            }
        }
        if (!keys.contains(focusKey)) {
            focusKey = keys.isEmpty() ? "" : keys.get(0);
            focusAction = "\uae30\ubcf8";
        }
        if (!List.of("\uae30\ubcf8", "\uae43\ud138 1\ud68c", "\uc11d\ud0c4 1\ud68c", "\uc11d\ud0c4 2\ud68c").contains(focusAction)) {
            focusAction = "\uae30\ubcf8";
        }
    }

    public static boolean focusMatches(PaletteColor color) {
        return color != null && color.matchesPaint(focusKey) && color.matchesStage(focusAction);
    }

    public static boolean isCompleted(int x, int y) {
        BitSet bits = completedCells.get(tile);
        return bits != null && bits.get(y * 32 + x);
    }

    public static void toggleCursorCompleted() {
        BitSet bits = completedCells.computeIfAbsent(tile, k -> new BitSet(1024));
        bits.flip(cursorY * 32 + cursorX);
        ProjectState.requestProgressSave();
    }

    public static void toggleTileCompleted() {
        completedTiles.flip(tile);
        ProjectState.saveProgress();
    }

    public static void clearCurrentTileCompleted() {
        completedTiles.clear(tile);
        completedCells.remove(tile);
        ProjectState.saveProgress();
    }

    public static double progress() {
        BitSet bits = completedCells.get(tile);
        int done = bits == null ? 0 : bits.cardinality();
        return (double)done / 1024.0;
    }

    public static boolean visible(ArtImage.Cell cell, int x, int y) {
        if (cell.emptyPixel()) {
            return false;
        }
        boolean completed = ProjectState.isCompleted(x, y);
        if (!showCompleted && completed) {
            return false;
        }
        return switch (workMode) {
            default -> throw new MatchException(null, null);
            case WorkMode.FULL -> true;
            case WorkMode.ROW -> {
                if (y == row) {
                    yield true;
                }
                yield false;
            }
            case WorkMode.FOCUS -> ProjectState.focusMatches(cell.color());
            case WorkMode.INCOMPLETE -> {
                if (!completed) {
                    yield true;
                }
                yield false;
            }
            case WorkMode.CURSOR -> x == cursorX && y == cursorY;
        };
    }

    public static ArtImage.Cell cursorCell() {
        if (image == null) {
            return ArtImage.Cell.empty();
        }
        return image.cell(tile, cursorX, cursorY);
    }

    public static PaletteColor focusColor() {
        if (image == null || focusKey == null || focusKey.isEmpty()) {
            return null;
        }
        for (int y = 0; y < 32; ++y) {
            for (int x = 0; x < 32; ++x) {
                ArtImage.Cell c = image.cell(tile, x, y);
                if (c.emptyPixel() || !ProjectState.focusMatches(c.color())) continue;
                return c.color();
            }
        }
        return null;
    }

    public static String focusDisplay() {
        if (image == null || focusKey == null || focusKey.isEmpty()) {
            return "\uc120\ud0dd \uc0c9\uc0c1 \uc5c6\uc74c";
        }
        for (int y = 0; y < 32; ++y) {
            for (int x = 0; x < 32; ++x) {
                ArtImage.Cell c = image.cell(tile, x, y);
                if (c.emptyPixel() || !c.color().matchesPaint(focusKey)) continue;
                PaletteColor p = c.color();
                return p.code() + " " + p.paintName() + " : " + focusAction;
            }
        }
        return "\uc120\ud0dd \uc0c9\uc0c1 \uc5c6\uc74c";
    }

    public static int remainingFocusPixels() {
        if (image == null || focusKey == null || focusKey.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (int y = 0; y < 32; ++y) {
            for (int x = 0; x < 32; ++x) {
                ArtImage.Cell c = image.cell(tile, x, y);
                if (c.emptyPixel() || !ProjectState.focusMatches(c.color()) || ProjectState.isCompleted(x, y)) continue;
                ++count;
            }
        }
        return count;
    }

    public static boolean completePixelAutomatically(int x, int y) {
        return ProjectState.setPixelCompletedAutomatically(x, y, true);
    }

    public static boolean setPixelCompletedAutomatically(int x, int y, boolean completed) {
        if (image == null || x < 0 || y < 0 || x >= 32 || y >= 32) {
            return false;
        }
        ArtImage.Cell c = image.cell(tile, x, y);
        if (c.emptyPixel()) {
            return false;
        }
        int index = y * 32 + x;
        if (ProjectState.isCompleted(x, y) == completed) {
            return false;
        }
        BitSet bits = completedCells.computeIfAbsent(tile, k -> new BitSet(1024));
        bits.set(index, completed);
        if (!completed && bits.isEmpty()) {
            completedCells.remove(tile);
        }
        ProjectState.requestProgressSave();
        return true;
    }

    public static void completeAimedPixel() {
        if (!AimTracker.valid || image == null) {
            return;
        }
        cursorX = AimTracker.x;
        row = cursorY = AimTracker.y;
        ArtImage.Cell c = ProjectState.cursorCell();
        if (c.emptyPixel()) {
            return;
        }
        if (ProjectState.isCompleted(cursorX, cursorY)) {
            return;
        }
        BitSet bits = completedCells.computeIfAbsent(tile, k -> new BitSet(1024));
        bits.set(cursorY * 32 + cursorX);
        ProjectState.requestProgressSave();
    }

    public static void undoAimedPixel() {
        if (!AimTracker.valid || image == null) {
            return;
        }
        if (!ProjectState.isCompleted(AimTracker.x, AimTracker.y)) {
            return;
        }
        BitSet bits = completedCells.computeIfAbsent(tile, k -> new BitSet(1024));
        bits.clear(AimTracker.y * 32 + AimTracker.x);
        ProjectState.requestProgressSave();
    }

    public static String tileMessage() {
        if (image == null) {
            return "\uc774\ubbf8\uc9c0 \uc5c6\uc74c";
        }
        return "ArtMap \uce94\ubc84\uc2a4 " + (tile + 1) + "/" + image.tileCount();
    }

    private static void requestProgressSave() {
        if (!progressDirty) {
            progressSaveDue = System.nanoTime() + 1000000000L;
        }
        progressDirty = true;
    }

    public static void tickProgressSave() {
        if (progressDirty && System.nanoTime() - progressSaveDue >= 0L) {
            ProjectState.saveProgress();
        }
    }

    public static void flushPendingProgress() {
        if (progressDirty) {
            ProjectState.saveProgress();
        }
    }

    public static void saveProgress() {
        try {
            Files.createDirectories(ProjectState.rootDir(), new FileAttribute[0]);
            JsonObject root = new JsonObject();
            root.addProperty("tile", (Number)tile);
            root.addProperty("row", (Number)row);
            root.addProperty("cursorX", (Number)cursorX);
            root.addProperty("cursorY", (Number)cursorY);
            root.addProperty("workMode", workMode.name());
            root.addProperty("accessibility", accessibility.name());
            root.addProperty("pixelMarkerMode", pixelMarkerMode.name());
            root.addProperty("grayscale", Boolean.valueOf(grayscale));
            root.addProperty("overlayEnabled", Boolean.valueOf(overlayEnabled));
            root.addProperty("fullPreviewOverlayEnabled", Boolean.valueOf(fullPreviewOverlayEnabled));
            root.addProperty("autoProgressEnabled", Boolean.valueOf(autoProgressEnabled));
            root.addProperty("colorOverlayEnabled", Boolean.valueOf(colorOverlayEnabled));
            root.addProperty("grid", Boolean.valueOf(grid));
            root.addProperty("showHud", Boolean.valueOf(showHud));
            root.addProperty("showCompleted", Boolean.valueOf(showCompleted));
            root.addProperty("showTutorial", Boolean.valueOf(showTutorial));
            root.addProperty("overlayAlpha", (Number)overlayAlpha);
            root.addProperty("focusKey", focusKey);
            root.addProperty("focusAction", focusAction);
            root.addProperty("resize32Mode", resize32Mode.name());
            root.addProperty("ditherMode", ditherMode.name());
            root.addProperty("paletteMode", paletteMode.name());
            root.addProperty("colorMatchMode", colorMatchMode.name());
            root.addProperty("imageStyle", imageStyle.name());
            root.addProperty("workGridSize", (Number)32);
            root.addProperty("targetImageWidth", (Number)targetImageWidth);
            root.addProperty("targetImageHeight", (Number)targetImageHeight);
            root.addProperty("surfaceOffset", (Number)ProjectionState.surfaceOffset);
            root.addProperty("planeSize", (Number)ProjectionState.planeSize);
            root.addProperty("inset", (Number)ProjectionState.inset);
            JsonObject workspaces = new JsonObject();
            JsonObject oldRoot = ProjectState.readProgressRoot();
            if (oldRoot != null && oldRoot.has("workspaces") && oldRoot.get("workspaces").isJsonObject()) {
                workspaces = oldRoot.getAsJsonObject("workspaces").deepCopy();
            }
            if (image != null && currentWorkId != null && !currentWorkId.isEmpty()) {
                workspaces.add(currentWorkId, (JsonElement)ProjectState.buildCurrentWorkState());
            }
            root.add("workspaces", (JsonElement)workspaces);
            Path temporary = ProjectState.progressFile().resolveSibling("progress.json.tmp");
            Files.writeString(temporary, (CharSequence)new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)root), StandardCharsets.UTF_8, new OpenOption[0]);
            try {
                Files.move(temporary, ProjectState.progressFile(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
            catch (AtomicMoveNotSupportedException unsupported) {
                Files.move(temporary, ProjectState.progressFile(), StandardCopyOption.REPLACE_EXISTING);
            }
            progressDirty = false;
        }
        catch (Exception e) {
            progressDirty = true;
            progressSaveDue = System.nanoTime() + 5000000000L;
            e.printStackTrace();
        }
    }

    public static void loadProgress() {
        JsonObject root = ProjectState.readProgressRoot();
        if (root == null) {
            return;
        }
        try {
            ProjectState.loadSavedSettings(root);
            if (image != null && currentWorkId != null && !currentWorkId.isEmpty()) {
                ProjectState.loadSavedWorkProgress(root);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadWorkProgress() {
        JsonObject root = ProjectState.readProgressRoot();
        if (root == null) {
            return;
        }
        try {
            ProjectState.loadSavedWorkProgress(root);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JsonObject readProgressRoot() {
        if (!Files.exists(ProjectState.progressFile(), new LinkOption[0])) {
            return null;
        }
        try {
            return JsonParser.parseString((String)Files.readString(ProjectState.progressFile(), StandardCharsets.UTF_8)).getAsJsonObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void loadSavedSettings(JsonObject root) {
        if (root.has("workMode")) {
            try {
                workMode = WorkMode.valueOf(root.get("workMode").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("accessibility")) {
            try {
                accessibility = AccessibilityMode.valueOf(root.get("accessibility").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("pixelMarkerMode")) {
            try {
                pixelMarkerMode = PixelMarkerMode.valueOf(root.get("pixelMarkerMode").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("grayscale")) {
            grayscale = root.get("grayscale").getAsBoolean();
        }
        if (root.has("overlayEnabled")) {
            overlayEnabled = root.get("overlayEnabled").getAsBoolean();
        }
        if (root.has("fullPreviewOverlayEnabled")) {
            fullPreviewOverlayEnabled = root.get("fullPreviewOverlayEnabled").getAsBoolean();
        }
        if (root.has("autoProgressEnabled")) {
            autoProgressEnabled = root.get("autoProgressEnabled").getAsBoolean();
        }
        if (root.has("colorOverlayEnabled")) {
            colorOverlayEnabled = root.get("colorOverlayEnabled").getAsBoolean();
        }
        if (root.has("grid")) {
            grid = root.get("grid").getAsBoolean();
        }
        if (root.has("showHud")) {
            showHud = root.get("showHud").getAsBoolean();
        }
        if (root.has("showCompleted")) {
            showCompleted = root.get("showCompleted").getAsBoolean();
        }
        if (root.has("showTutorial")) {
            showTutorial = root.get("showTutorial").getAsBoolean();
        }
        if (root.has("overlayAlpha")) {
            overlayAlpha = root.get("overlayAlpha").getAsInt();
        }
        if (root.has("resize32Mode")) {
            try {
                resize32Mode = Resize32Mode.valueOf(root.get("resize32Mode").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("ditherMode")) {
            try {
                ditherMode = DitherMode.valueOf(root.get("ditherMode").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("paletteMode")) {
            try {
                paletteMode = PaletteMode.valueOf(root.get("paletteMode").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("colorMatchMode")) {
            try {
                colorMatchMode = ColorMatchMode.valueOf(root.get("colorMatchMode").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("imageStyle")) {
            try {
                imageStyle = ImageStyle.valueOf(root.get("imageStyle").getAsString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (root.has("surfaceOffset")) {
            double savedOffset = root.get("surfaceOffset").getAsDouble();
            if (savedOffset >= 0.075 && savedOffset <= 0.085) {
                ProjectionState.snapToSurface();
            } else {
                ProjectionState.surfaceOffset = savedOffset;
            }
        }
        if (root.has("planeSize")) {
            ProjectionState.planeSize = root.get("planeSize").getAsDouble();
        }
        if (root.has("inset")) {
            ProjectionState.inset = root.get("inset").getAsDouble();
        }
        if (root.has("targetImageWidth")) {
            targetImageWidth = Image32Processor.snap32(root.get("targetImageWidth").getAsInt());
        }
        if (root.has("targetImageHeight")) {
            targetImageHeight = Image32Processor.snap32(root.get("targetImageHeight").getAsInt());
        }
    }

    private static void loadSavedWorkProgress(JsonObject root) {
        JsonObject workspaces;
        JsonObject work = null;
        if (root.has("workspaces") && root.get("workspaces").isJsonObject() && currentWorkId != null && !currentWorkId.isEmpty() && (workspaces = root.getAsJsonObject("workspaces")).has(currentWorkId) && workspaces.get(currentWorkId).isJsonObject()) {
            work = workspaces.getAsJsonObject(currentWorkId);
        }
        if (work != null) {
            ProjectState.applyWorkProgress(work);
            return;
        }
        if (!(root.has("workspaces") || currentWorkId != null && currentWorkId.startsWith("COLORS11:") || legacyProgressMigratedThisSession || !ProjectState.hasLegacyWorkData(root))) {
            ProjectState.applyWorkProgress(root);
            legacyProgressMigratedThisSession = true;
            ProjectState.saveProgress();
            return;
        }
        ProjectState.resetWorkProgress();
    }

    private static boolean hasLegacyWorkData(JsonObject root) {
        return root.has("completedCells") || root.has("completedTiles") || root.has("tile") || root.has("row") || root.has("cursorX") || root.has("cursorY") || root.has("focusKey");
    }

    private static void resetWorkProgress() {
        tile = 0;
        row = 0;
        cursorX = 0;
        cursorY = 0;
        focusKey = "";
        focusAction = "\uae30\ubcf8";
        completedTiles.clear();
        completedCells.clear();
        ProjectState.ensureFocus();
    }

    private static void applyWorkProgress(JsonObject root) {
        if (root.has("tile")) {
            tile = root.get("tile").getAsInt();
        }
        if (root.has("row")) {
            row = root.get("row").getAsInt();
        }
        if (root.has("cursorX")) {
            cursorX = root.get("cursorX").getAsInt();
        }
        if (root.has("cursorY")) {
            cursorY = root.get("cursorY").getAsInt();
        }
        if (root.has("focusKey")) {
            focusKey = root.get("focusKey").getAsString();
        }
        if (root.has("focusAction")) {
            focusAction = root.get("focusAction").getAsString();
        }
        if (focusKey != null && focusKey.contains("|")) {
            int split = focusKey.indexOf(124);
            String oldAction = focusKey.substring(split + 1);
            focusKey = focusKey.substring(0, split);
            if (oldAction.startsWith("\uae30\ubcf8")) {
                focusAction = "\uae30\ubcf8";
            } else if (!oldAction.isBlank()) {
                focusAction = oldAction;
            }
        }
        int savedGridSize = root.has("workGridSize") ? root.get("workGridSize").getAsInt() : 32;
        completedTiles.clear();
        if (root.has("completedTiles")) {
            for (Object e : root.getAsJsonArray("completedTiles")) {
                completedTiles.set(e.getAsInt());
            }
        }
        completedCells.clear();
        if (savedGridSize == 32 && root.has("completedCells")) {
            for (Object e : root.getAsJsonObject("completedCells").entrySet()) {
                BitSet bits = new BitSet(1024);
                for (JsonElement v : ((JsonElement)e.getValue()).getAsJsonArray()) {
                    bits.set(v.getAsInt());
                }
                completedCells.put(Integer.parseInt((String)e.getKey()), bits);
            }
        }
        if (image != null) {
            tile = Math.max(0, Math.min(tile, image.tileCount() - 1));
        }
        row = Math.max(0, Math.min(row, 31));
        cursorX = Math.max(0, Math.min(cursorX, 31));
        cursorY = Math.max(0, Math.min(cursorY, 31));
        ProjectState.ensureFocus();
    }

    private static JsonObject buildCurrentWorkState() {
        JsonObject work = new JsonObject();
        work.addProperty("tile", (Number)tile);
        work.addProperty("row", (Number)row);
        work.addProperty("cursorX", (Number)cursorX);
        work.addProperty("cursorY", (Number)cursorY);
        work.addProperty("focusKey", focusKey);
        work.addProperty("focusAction", focusAction);
        work.addProperty("workGridSize", (Number)32);
        JsonArray tiles = new JsonArray();
        int i = completedTiles.nextSetBit(0);
        while (i >= 0) {
            tiles.add((Number)i);
            i = completedTiles.nextSetBit(i + 1);
        }
        work.add("completedTiles", (JsonElement)tiles);
        JsonObject cells = new JsonObject();
        for (Map.Entry<Integer, BitSet> e : completedCells.entrySet()) {
            JsonArray arr = new JsonArray();
            int i2 = e.getValue().nextSetBit(0);
            while (i2 >= 0) {
                arr.add((Number)i2);
                i2 = e.getValue().nextSetBit(i2 + 1);
            }
            cells.add(String.valueOf(e.getKey()), (JsonElement)arr);
        }
        work.add("completedCells", (JsonElement)cells);
        return work;
    }

    private static String buildWorkId(BufferedImage image) {
        CRC32 crc = new CRC32();
        ProjectState.updateCrc(crc, image.getWidth());
        ProjectState.updateCrc(crc, image.getHeight());
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                ProjectState.updateCrc(crc, image.getRGB(x, y));
            }
        }
        return Long.toHexString(crc.getValue());
    }

    private static void updateCrc(CRC32 crc, int value) {
        crc.update(value >>> 24 & 0xFF);
        crc.update(value >>> 16 & 0xFF);
        crc.update(value >>> 8 & 0xFF);
        crc.update(value & 0xFF);
    }

    private static void message(class_310 mc, String text) {
        if (mc.field_1724 != null) {
            mc.field_1724.method_7353((class_2561)class_2561.method_43470((String)text), true);
        }
    }

    static {
        tile = 0;
        row = 0;
        cursorX = 0;
        cursorY = 0;
        workMode = WorkMode.FULL;
        accessibility = AccessibilityMode.NORMAL;
        pixelMarkerMode = PixelMarkerMode.OFF;
        grayscale = false;
        overlayEnabled = true;
        fullPreviewOverlayEnabled = false;
        colorOverlayEnabled = true;
        grid = false;
        showHud = true;
        showCompleted = true;
        showTutorial = true;
        autoProgressEnabled = true;
        overlayAlpha = 105;
        focusKey = "";
        focusAction = "\uae30\ubcf8";
        status = "\uc774\ubbf8\uc9c0 \ubbf8\ub85c\ub4dc";
        resize32Mode = Resize32Mode.PAD;
        ditherMode = DitherMode.OFF;
        paletteMode = PaletteMode.MEASURED;
        colorMatchMode = ColorMatchMode.CLASSIC;
        imageStyle = ImageStyle.ILLUSTRATION;
        preparedImageFile = null;
        targetImageWidth = 32;
        targetImageHeight = 32;
        targetSizeSourceName = "";
        currentWorkId = "";
        legacyProgressMigratedThisSession = false;
        completedTiles = new BitSet();
        completedCells = new HashMap<Integer, BitSet>();
        imageFiles = new ArrayList<Path>();
        selectedImageIndex = 0;
    }
}

