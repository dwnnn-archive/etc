/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1935
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 */
package com.easttg.artmapworkshop;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_310;
import net.minecraft.class_332;

@Environment(value=EnvType.CLIENT)
public final class PaintItems {
    private static final Map<String, class_1792> ITEMS = Map.ofEntries(Map.entry("D001", class_1802.field_8794), Map.entry("D002", class_1802.field_8330), Map.entry("D003", class_1802.field_8145), Map.entry("D004", class_1802.field_17503), Map.entry("D005", class_1802.field_8264), Map.entry("D006", class_1802.field_8131), Map.entry("D007", class_1802.field_8054), Map.entry("D008", class_1802.field_8749), Map.entry("D009", class_1802.field_8408), Map.entry("D010", class_1802.field_8192), Map.entry("D011", class_1802.field_8790), Map.entry("D012", class_1802.field_8055), Map.entry("D013", class_1802.field_8116), Map.entry("D014", class_1802.field_8273), Map.entry("D015", class_1802.field_8434), Map.entry("D016", class_1802.field_8652), Map.entry("D017", class_1802.field_8759), Map.entry("D018", class_1802.field_8669), Map.entry("D019", class_1802.field_8602), Map.entry("D020", class_1802.field_8809), Map.entry("D021", class_1802.field_8296), Map.entry("D022", class_1802.field_8492), Map.entry("D023", class_1802.field_8397), Map.entry("D024", class_1802.field_8135), Map.entry("D025", class_1802.field_8632), Map.entry("D026", class_1802.field_8324), Map.entry("D027", class_1802.field_8382), Map.entry("D028", class_1802.field_8851), Map.entry("D029", class_1802.field_46249), Map.entry("D030", class_1802.field_8786), Map.entry("D031", class_1802.field_8186), Map.entry("D032", class_1802.field_8298), Map.entry("D033", class_1802.field_46250), Map.entry("D034", class_1802.field_8426), Map.entry("D035", class_1802.field_8610), Map.entry("D036", class_1802.field_8067), Map.entry("D037", class_1802.field_21981), Map.entry("D038", class_1802.field_8680), Map.entry("D039", class_1802.field_22015), Map.entry("D040", class_1802.field_17516), Map.entry("D041", class_1802.field_21982), Map.entry("D042", class_1802.field_20393), Map.entry("D043", class_1802.field_8803), Map.entry("D044", class_1802.field_8675), Map.entry("D045", class_1802.field_8201), Map.entry("D046", class_1802.field_8601), Map.entry("D047", class_1802.field_28409), Map.entry("D048", class_1802.field_8635), Map.entry("D049", class_1802.field_22013), Map.entry("N001", class_1802.field_8233), Map.entry("N002", class_1802.field_22490), Map.entry("N003", class_1802.field_22008), Map.entry("N004", class_1802.field_8777), Map.entry("N005", class_1802.field_8687), Map.entry("N006", class_1802.field_8665), Map.entry("N007", class_1802.field_8279), Map.entry("N008", class_1802.field_8621), Map.entry("N009", class_1802.field_22489), Map.entry("N010", class_1802.field_29025), Map.entry("N011", class_1802.field_8070), Map.entry("001", class_1802.field_8794), Map.entry("002", class_1802.field_8264), Map.entry("003", class_1802.field_8408), Map.entry("004", class_1802.field_8116), Map.entry("005", class_1802.field_8759), Map.entry("006", class_1802.field_8296), Map.entry("007", class_1802.field_8632), Map.entry("008", class_1802.field_8851), Map.entry("009", class_1802.field_8298), Map.entry("010", class_1802.field_8330), Map.entry("011", class_1802.field_8131), Map.entry("012", class_1802.field_8192), Map.entry("013", class_1802.field_8273), Map.entry("014", class_1802.field_8669), Map.entry("015", class_1802.field_8492), Map.entry("016", class_1802.field_8324), Map.entry("017", class_1802.field_46249), Map.entry("018", class_1802.field_46250), Map.entry("019", class_1802.field_8145), Map.entry("020", class_1802.field_8054), Map.entry("021", class_1802.field_8790), Map.entry("022", class_1802.field_8434), Map.entry("023", class_1802.field_8602), Map.entry("024", class_1802.field_8397), Map.entry("026", class_1802.field_8786), Map.entry("027", class_1802.field_8426), Map.entry("028", class_1802.field_17503), Map.entry("029", class_1802.field_8055), Map.entry("030", class_1802.field_8652), Map.entry("031", class_1802.field_8809), Map.entry("032", class_1802.field_8135), Map.entry("033", class_1802.field_8382), Map.entry("034", class_1802.field_8186), Map.entry("035", class_1802.field_8610), Map.entry("036", class_1802.field_8067), Map.entry("037", class_1802.field_8680), Map.entry("038", class_1802.field_17516), Map.entry("039", class_1802.field_20393), Map.entry("040", class_1802.field_8675), Map.entry("041", class_1802.field_8601), Map.entry("042", class_1802.field_28409), Map.entry("043", class_1802.field_8635), Map.entry("044", class_1802.field_22013), Map.entry("045", class_1802.field_21981), Map.entry("046", class_1802.field_22015), Map.entry("047", class_1802.field_21982), Map.entry("048", class_1802.field_8803), Map.entry("049", class_1802.field_8201));
    private static final Map<String, class_1799> STACKS = new HashMap<String, class_1799>();
    private static final class_1799 FEATHER = new class_1799((class_1935)class_1802.field_8153);
    private static final class_1799 COAL = new class_1799((class_1935)class_1802.field_8713);

    private PaintItems() {
    }

    public static class_1799 paint(String code) {
        if (code == null) {
            return class_1799.field_8037;
        }
        class_1792 item = ITEMS.get(code);
        if (item == null) {
            return class_1799.field_8037;
        }
        return STACKS.computeIfAbsent(code, key -> new class_1799((class_1935)item));
    }

    public static class_1799 tool(String action) {
        if ("\uae43\ud138 1\ud68c".equals(action)) {
            return FEATHER;
        }
        if ("\uc11d\ud0c4 1\ud68c".equals(action) || "\uc11d\ud0c4 2\ud68c".equals(action)) {
            return COAL;
        }
        return class_1799.field_8037;
    }

    public static int toolCount(String action) {
        return "\uc11d\ud0c4 2\ud68c".equals(action) ? 2 : (PaintItems.tool(action).method_7960() ? 0 : 1);
    }

    public static void draw(class_332 g, class_310 mc, int x, int y, int width, String code, String action) {
        if (width < 150) {
            return;
        }
        g.method_51433(mc.field_1772, "\uc0ac\uc6a9 \uc544\uc774\ud15c", x, y + 4, -4931892, false);
        int ix = x + mc.field_1772.method_1727("\uc0ac\uc6a9 \uc544\uc774\ud15c") + 6;
        if (code == null || code.isEmpty()) {
            g.method_51433(mc.field_1772, "-", ix, y + 4, -4931892, false);
            return;
        }
        class_1799 paint = PaintItems.paint(code);
        if (!paint.method_7960()) {
            g.method_51427(paint, ix, y);
            ix += 21;
        } else if ("050".equals(code)) {
            g.method_51433(mc.field_1772, "\ube48 \uce94\ubc84\uc2a4", ix, y + 4, -4931892, false);
            ix += mc.field_1772.method_1727("\ube48 \uce94\ubc84\uc2a4") + 5;
        } else {
            g.method_51433(mc.field_1772, "\ubbf8\ub4f1\ub85d", ix, y + 4, -4931892, false);
            return;
        }
        class_1799 tool = PaintItems.tool(action);
        if (!tool.method_7960()) {
            g.method_51433(mc.field_1772, "\u2192", ix, y + 4, -4931892, false);
            g.method_51427(tool, ix += 13, y);
            g.method_51433(mc.field_1772, "\u00d7" + PaintItems.toolCount(action), ix + 18, y + 4, -1, false);
        }
    }
}

