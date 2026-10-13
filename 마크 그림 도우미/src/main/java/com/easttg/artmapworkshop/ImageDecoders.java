/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import javax.imageio.spi.IIORegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ImageDecoders {
    private ImageDecoders() {
    }

    public static void register() {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        if (registry.getServiceProviderByClass(WebPImageReaderSpi.class) == null) {
            registry.registerServiceProvider(new WebPImageReaderSpi());
        }
    }

    public static void requireStaticImage(Path path) throws IOException {
        byte[] h;
        try (InputStream stream = Files.newInputStream(path, new OpenOption[0]);){
            h = stream.readNBytes(21);
        }
        if (h.length == 21 && h[0] == 82 && h[1] == 73 && h[2] == 70 && h[3] == 70 && h[8] == 87 && h[9] == 69 && h[10] == 66 && h[11] == 80 && h[12] == 86 && h[13] == 80 && h[14] == 56 && h[15] == 88 && (h[20] & 2) != 0) {
            throw new IOException("\uc6c0\uc9c1\uc774\ub294 WebP\ub294 \uc815\uc9c0 PNG \ub610\ub294 \uc815\uc9c0 WebP\ub85c \ubcc0\ud658\ud574\uc8fc\uc138\uc694.");
        }
    }
}

