package me.blayyke.vgmgroups;

import org.spongepowered.api.text.Text;

public class TextUtils {
    public static Text createNewLineText() {
        return Text.of("\n");
    }

    public static Text createTextSpanLines(String... lines) {
        if (lines == null) return null;
        if (lines.length < 1) return null;

        Text.Builder builder = Text.builder();

        for (String line : lines) {
            builder.append(Text.of(line)).append(createNewLineText());
        }

        return builder.build();
    }
}