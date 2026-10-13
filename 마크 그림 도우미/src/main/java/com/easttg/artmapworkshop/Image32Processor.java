/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.ArtImage;
import com.easttg.artmapworkshop.ColorMatchMode;
import com.easttg.artmapworkshop.DitherMode;
import com.easttg.artmapworkshop.DitherProcessor;
import com.easttg.artmapworkshop.ImageDecoders;
import com.easttg.artmapworkshop.ImageStyle;
import com.easttg.artmapworkshop.PaletteColor;
import com.easttg.artmapworkshop.PaletteLoader;
import com.easttg.artmapworkshop.ProjectState;
import com.easttg.artmapworkshop.Resize32Mode;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_310;

@Environment(value=EnvType.CLIENT)
public final class Image32Processor {
    private static final int UI_PREVIEW_MAX_SIZE = 512;

    private Image32Processor() {
    }

    public static Path processedDir() {
        return ProjectState.rootDir().resolve("processed");
    }

    public static Path createSized(class_310 mc, Path input, Resize32Mode mode, int requestedWidth, int requestedHeight) {
        if (input == null) {
            Image32Processor.message(mc, "ArtMap: \uc120\ud0dd\ub41c \uc774\ubbf8\uc9c0\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.");
            return null;
        }
        try {
            Files.createDirectories(Image32Processor.processedDir(), new FileAttribute[0]);
            int targetW = Image32Processor.snap32(requestedWidth);
            int targetH = Image32Processor.snap32(requestedHeight);
            BufferedImage source = Image32Processor.readSubsampled(input, targetW, targetH);
            if (source == null) {
                Image32Processor.message(mc, "ArtMap: \uc774\ubbf8\uc9c0\ub97c \uc77d\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                return null;
            }
            BufferedImage output = ProjectState.imageStyle.resize(source, targetW, targetH);
            int srcW = source.getWidth();
            int srcH = source.getHeight();
            String original = input.getFileName().toString();
            int dot = original.lastIndexOf(46);
            String stem = dot > 0 ? original.substring(0, dot) : original;
            Path outputFile = Image32Processor.processedDir().resolve(stem + "_" + targetW + "x" + targetH + "_" + ProjectState.imageStyle.name().toLowerCase(Locale.ROOT) + "_fill.png");
            ImageIO.write((RenderedImage)output, "PNG", outputFile.toFile());
            Image32Processor.message(mc, "\uc791\uc5c5 \uc774\ubbf8\uc9c0 \uc0dd\uc131: " + srcW + "x" + srcH + " \u2192 " + targetW + "x" + targetH + " | \uc804\uccb4 \ucc44\uc6b0\uae30");
            return outputFile;
        }
        catch (Exception | OutOfMemoryError e) {
            Image32Processor.message(mc, "\uc774\ubbf8\uc9c0 \ubcc0\ud658 \uc2e4\ud328: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage buildPreviewImage(Path input, int requestedWidth, int requestedHeight, DitherMode ditherMode, boolean grayscale, ColorMatchMode matchMode) {
        if (input == null) {
            return null;
        }
        try {
            int targetW = Image32Processor.snap32(requestedWidth);
            int targetH = Image32Processor.snap32(requestedHeight);
            double previewScale = Math.min(1.0, Math.min(512.0 / (double)targetW, 512.0 / (double)targetH));
            int previewW = Math.max(32, (int)Math.floor((double)targetW * previewScale / 32.0) * 32);
            int previewH = Math.max(32, (int)Math.floor((double)targetH * previewScale / 32.0) * 32);
            BufferedImage source = Image32Processor.readSubsampled(input, previewW, previewH);
            if (source == null) {
                return null;
            }
            BufferedImage scaled = ProjectState.imageStyle.resize(source, previewW, previewH);
            List<PaletteColor> palette = PaletteLoader.load();
            BufferedImage dithered = DitherProcessor.apply(scaled, palette, ditherMode, matchMode);
            ArtImage artImage = ArtImage.convert(dithered, palette, grayscale, matchMode, ProjectState.imageStyle == ImageStyle.LOGO && ditherMode == DitherMode.OFF);
            BufferedImage preview = new BufferedImage(previewW, previewH, 2);
            int tilesX = artImage.tilesX();
            for (int y = 0; y < previewH; ++y) {
                for (int x = 0; x < previewW; ++x) {
                    int tileY = y / 32;
                    int tileX = x / 32;
                    int tileIndex = tileY * tilesX + tileX;
                    ArtImage.Cell cell = artImage.cell(tileIndex, x % 32, y % 32);
                    if (cell.emptyPixel()) {
                        preview.setRGB(x, y, 0);
                        continue;
                    }
                    PaletteColor color = cell.color();
                    preview.setRGB(x, y, cell.alpha() << 24 | color.r() << 16 | color.g() << 8 | color.b());
                }
            }
            return preview;
        }
        catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage readSubsampled(Path input, int requestedWidth, int requestedHeight) throws IOException {
        return Image32Processor.readSubsampled(input, requestedWidth, requestedHeight, ProjectState.imageStyle);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static BufferedImage readSubsampled(Path input, int requestedWidth, int requestedHeight, ImageStyle style) throws IOException {
        ImageDecoders.register();
        ImageDecoders.requireStaticImage(input);
        try (ImageInputStream stream = ImageIO.createImageInputStream(input.toFile());){
            if (stream == null) {
                BufferedImage bufferedImage = null;
                return bufferedImage;
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (!readers.hasNext()) {
                BufferedImage bufferedImage = null;
                return bufferedImage;
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(stream, true, true);
                int sourceWidth = reader.getWidth(0);
                int sourceHeight = reader.getHeight(0);
                int oversample = style == ImageStyle.ILLUSTRATION ? 2 : 1;
                int decodeWidth = Math.min(4096, Math.max(1, requestedWidth) * oversample);
                int decodeHeight = Math.min(4096, Math.max(1, requestedHeight) * oversample);
                int sampleX = Math.max(1, (int)Math.ceil((double)sourceWidth / (double)decodeWidth));
                int sampleY = Math.max(1, (int)Math.ceil((double)sourceHeight / (double)decodeHeight));
                int sample = Math.max(sampleX, sampleY);
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceSubsampling(sample, sample, 0, 0);
                BufferedImage bufferedImage = reader.read(0, param);
                reader.dispose();
                return bufferedImage;
            }
            catch (Throwable throwable) {
                reader.dispose();
                throw throwable;
            }
        }
    }

    public static int snap32(int value) {
        int clamped = Math.max(32, value);
        int steps = (int)Math.round((double)clamped / 32.0);
        return Math.max(32, steps * 32);
    }

    public static BufferedImage previewOfWork(ArtImage image) {
        int sourceW = image.tilesX() * 32;
        int sourceH = image.tilesY() * 32;
        double scale = Math.min(1.0, 512.0 / (double)Math.max(sourceW, sourceH));
        int width = Math.max(32, (int)((double)sourceW * scale));
        int height = Math.max(32, (int)((double)sourceH * scale));
        BufferedImage result = new BufferedImage(width, height, 2);
        for (int y = 0; y < height; ++y) {
            int sy = Math.min(sourceH - 1, (int)(((double)y + 0.5) * (double)sourceH / (double)height));
            for (int x = 0; x < width; ++x) {
                int sx = Math.min(sourceW - 1, (int)(((double)x + 0.5) * (double)sourceW / (double)width));
                ArtImage.Cell cell = image.cell(sy / 32 * image.tilesX() + sx / 32, sx % 32, sy % 32);
                if (cell.emptyPixel()) continue;
                result.setRGB(x, y, cell.alpha() << 24 | cell.color().rgb());
            }
        }
        return result;
    }

    private static void message(class_310 mc, String text) {
        if (mc.field_1724 != null) {
            mc.field_1724.method_7353((class_2561)class_2561.method_43470((String)text), true);
        }
    }
}

