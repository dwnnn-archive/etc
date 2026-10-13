/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.AddedPaints;
import com.easttg.artmapworkshop.PaletteColor;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class MeasuredMapColors {
    private static final Map<String, int[]> IDS = Map.ofEntries(Map.entry("001", new int[]{117, 118, 116, 119}), Map.entry("002", new int[]{17, 18, 16, 19}), Map.entry("003", new int[]{109, 110, 108, 111}), Map.entry("004", new int[]{105, 106, 104, 107}), Map.entry("005", new int[]{101, 102, 100, 103}), Map.entry("006", new int[]{97, 98, 96, 99}), Map.entry("007", new int[]{93, 94, 92, 95}), Map.entry("008", new int[]{25, 26, 24, 27}), Map.entry("009", new int[]{37, 38, 36, 39}), Map.entry("010", new int[]{81, 82, 80, 83}), Map.entry("011", new int[]{77, 78, 76, 79}), Map.entry("012", new int[]{73, 74, 72, 75}), Map.entry("013", new int[]{69, 70, 68, 71}), Map.entry("014", new int[]{65, 66, 64, 67}), Map.entry("015", new int[]{61, 62, 60, 63}), Map.entry("016", new int[]{57, 58, 56, 59}), Map.entry("017", new int[]{9, 10, 8, 11}), Map.entry("018", new int[]{41, 42, 40, 43}), Map.entry("019", new int[]{85, 86, 84, 87}), Map.entry("020", new int[]{89, 90, 88, 91}), Map.entry("021", new int[]{141, 142, 140, 143}), Map.entry("022", new int[]{125, 126, 124, 127}), Map.entry("023", new int[]{5, 6, 4, 7}), Map.entry("024", new int[]{121, 122, 120, 123}), Map.entry("026", new int[]{13, 14, 12, 15}), Map.entry("027", new int[]{21, 22, 20, 23}), Map.entry("028", new int[]{29, 30, 28, 31}), Map.entry("029", new int[]{49, 50, 48, 51}), Map.entry("030", new int[]{53, 54, 52, 55}), Map.entry("031", new int[]{129, 130, 128, 131}), Map.entry("032", new int[]{149, 150, 148, 151}), Map.entry("033", new int[]{193, 194, 192, 195}), Map.entry("034", new int[]{153, 154, 152, 155}), Map.entry("035", new int[]{157, 158, 156, 159}), Map.entry("036", new int[]{173, 174, 172, 175}), Map.entry("037", new int[]{169, 170, 168, 171}), Map.entry("038", new int[]{177, 178, 176, 179}), Map.entry("039", new int[]{189, 190, 188, 191}), Map.entry("040", new int[]{181, 182, 180, 183}), Map.entry("041", new int[]{161, 162, 160, 163}), Map.entry("042", new int[]{245, 246, 244, 247}), Map.entry("043", new int[]{197, 198, 196, 199}), Map.entry("044", new int[]{209, 210, 208, 211}), Map.entry("045", new int[]{213, 214, 212, 215}), Map.entry("046", new int[]{221, 222, 220, 223}), Map.entry("047", new int[]{225, 226, 224, 227}), Map.entry("048", new int[]{145, 146, 144, 147}), Map.entry("049", new int[]{137, 138, 136, 139}), Map.entry("D001", new int[]{117, 118, 116, 119}), Map.entry("D002", new int[]{81, 82, 80, 83}), Map.entry("D003", new int[]{85, 86, 84, 87}), Map.entry("D004", new int[]{29, 30, 28, 31}), Map.entry("D005", new int[]{17, 18, 16, 19}), Map.entry("D006", new int[]{77, 78, 76, 79}), Map.entry("D007", new int[]{89, 90, 88, 91}), Map.entry("D008", new int[]{33, 34, 32, 35}), Map.entry("D009", new int[]{109, 110, 108, 111}), Map.entry("D010", new int[]{73, 74, 72, 75}), Map.entry("D011", new int[]{141, 142, 140, 143}), Map.entry("D012", new int[]{49, 50, 48, 51}), Map.entry("D013", new int[]{105, 106, 104, 107}), Map.entry("D014", new int[]{69, 70, 68, 71}), Map.entry("D015", new int[]{125, 126, 124, 127}), Map.entry("D016", new int[]{53, 54, 52, 55}), Map.entry("D017", new int[]{101, 102, 100, 103}), Map.entry("D018", new int[]{65, 66, 64, 67}), Map.entry("D019", new int[]{5, 6, 4, 7}), Map.entry("D020", new int[]{129, 130, 128, 131}), Map.entry("D021", new int[]{97, 98, 96, 99}), Map.entry("D022", new int[]{61, 62, 60, 63}), Map.entry("D023", new int[]{121, 122, 120, 123}), Map.entry("D024", new int[]{149, 150, 148, 151}), Map.entry("D025", new int[]{93, 94, 92, 95}), Map.entry("D026", new int[]{57, 58, 56, 59}), Map.entry("D027", new int[]{193, 194, 192, 195}), Map.entry("D028", new int[]{25, 26, 24, 27}), Map.entry("D029", new int[]{9, 10, 8, 11}), Map.entry("D030", new int[]{13, 14, 12, 15}), Map.entry("D031", new int[]{153, 154, 152, 155}), Map.entry("D032", new int[]{37, 38, 36, 39}), Map.entry("D033", new int[]{41, 42, 40, 43}), Map.entry("D034", new int[]{21, 22, 20, 23}), Map.entry("D035", new int[]{157, 158, 156, 159}), Map.entry("D036", new int[]{173, 174, 172, 175}), Map.entry("D037", new int[]{213, 214, 212, 215}), Map.entry("D038", new int[]{169, 170, 168, 171}), Map.entry("D039", new int[]{221, 222, 220, 223}), Map.entry("D040", new int[]{177, 178, 176, 179}), Map.entry("D041", new int[]{225, 226, 224, 227}), Map.entry("D042", new int[]{189, 190, 188, 191}), Map.entry("D043", new int[]{145, 146, 144, 147}), Map.entry("D044", new int[]{181, 182, 180, 183}), Map.entry("D045", new int[]{137, 138, 136, 139}), Map.entry("D046", new int[]{161, 162, 160, 163}), Map.entry("D047", new int[]{245, 246, 244, 247}), Map.entry("D048", new int[]{197, 198, 196, 199}), Map.entry("D049", new int[]{209, 210, 208, 211}));

    private MeasuredMapColors() {
    }

    public static int packedId(PaletteColor color) {
        if (color == null) {
            return -1;
        }
        int[] ids = IDS.get(color.code());
        if (ids == null) {
            return AddedPaints.packedId(color);
        }
        int stage = switch (color.action()) {
            case "\uae30\ubcf8", "\uae30\ubcf8(\uce94\ubc84\uc2a4)" -> 0;
            case "\uae43\ud138 1\ud68c" -> 1;
            case "\uc11d\ud0c4 1\ud68c" -> 2;
            case "\uc11d\ud0c4 2\ud68c" -> 3;
            default -> -1;
        };
        return stage < 0 ? -1 : ids[stage];
    }
}

