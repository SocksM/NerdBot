package net.hypixel.nerdbot.generator.impl.tooltip.skyblock.powerstone;

import lombok.extern.log4j.Log4j2;
import net.hypixel.nerdbot.generator.builder.ClassBuilder;
import net.hypixel.nerdbot.generator.data.PowerStrength;
import net.hypixel.nerdbot.generator.image.MinecraftTooltip;
import net.hypixel.nerdbot.generator.impl.tooltip.MinecraftTooltipGenerator;
import net.hypixel.nerdbot.generator.item.GeneratedObject;
import net.hypixel.nerdbot.generator.powerstone.PowerstoneStat;
import net.hypixel.nerdbot.generator.powerstone.PowerstoneUtil;
import net.hypixel.nerdbot.generator.powerstone.ScalingPowerstoneStat;
import net.hypixel.nerdbot.generator.text.segment.LineSegment;
import net.hypixel.nerdbot.generator.text.wrapper.TextWrapper;
import net.hypixel.nerdbot.util.Range;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class SkyblockPowerGenerator extends MinecraftTooltipGenerator<SkyblockPowerSettings> {

    private final PowerStrength strength;
    private final List<ScalingPowerstoneStat> scalingStats;
    private final List<PowerstoneStat> staticStats;
    private final boolean selected;
    private final int magicalPower;
    private final boolean stonePower;

    protected SkyblockPowerGenerator(String name, boolean renderBorder, int maxLineLength, int padding, int alpha, boolean centeredText, PowerStrength strength, List<ScalingPowerstoneStat> scalingStats, List<PowerstoneStat> staticStats, boolean selected, int magicalPower, boolean stonePower) {
        super(name, renderBorder, maxLineLength, padding, alpha, "", centeredText);
        this.strength = strength;
        this.scalingStats = scalingStats;
        this.staticStats = staticStats;
        this.selected = selected;
        this.magicalPower = magicalPower;
        this.stonePower = stonePower;
    }

    @Override
    public GeneratedObject generate() {
        SkyblockPowerSettings settings = new SkyblockPowerSettings(
            itemLore,
            name,
            alpha,
            padding,
            maxLineLength,
            renderBorder,
            strength,
            scalingStats,
            staticStats,
            selected,
            magicalPower,
            stonePower
        );

        return new GeneratedObject(buildItem(settings));
    }

    @Override
    public MinecraftTooltip parseLore(SkyblockPowerSettings settings) {
        log.debug("Parsing lore for item: {} with SkyblockPowerSettings: {}", settings.getName(), settings);

        MinecraftTooltip.Builder builder = MinecraftTooltip.builder()
            .withPadding(settings.getPadding())
            .setRenderBorder(settings.isRenderBorder())
            .withAlpha(Range.between(0, 255).fit(settings.getAlpha()));

        if (settings.getName() != null && !settings.getName().isEmpty()) {
            String name = settings.getName();

            builder.withLines(LineSegment.fromLegacy(TextWrapper.parseLine(name), '&'));
        }

        builder.withLines(LineSegment.fromLegacy(TextWrapper.parseLine(
            new StringBuilder("&r&8")
                .append(strength.getName())
                .append(stonePower ? "Stone" : "")
                .append("Power")
                .toString()),
            '&'
        ));

        if (settings.getScalingStats() != null && !settings.getScalingStats().isEmpty()) {
            builder.withLines(LineSegment.fromLegacy(TextWrapper.parseLine(
                new StringBuilder("\\n\\n")
                    .append(PowerstoneUtil.SCALING_STATS_HEADER_POWER)
                    .append(PowerstoneUtil.parseScalingStatsToString(settings.getScalingStats()))
                    .toString()),
                '&'
            ));
        }

        if (settings.getStaticStats() != null && !settings.getStaticStats().isEmpty()) {
            builder.withLines(LineSegment.fromLegacy(TextWrapper.parseLine(
                new StringBuilder("\\n\\n")
                    .append(PowerstoneUtil.STATIC_STATS_HEADER)
                    .append(PowerstoneUtil.parseStaticStatsToString(settings.getStaticStats()))
                    .toString()),
                '&'
            ));
        }

        builder.withLines(LineSegment.fromLegacy(TextWrapper.parseLine(
            new StringBuilder("\\n\\n")
                .append(PowerstoneUtil.MAGICAL_POWER_FORMAT.formatted(settings.getMagicalPower()))
                .toString()),
            '&'
        ));

        builder.withLines(LineSegment.fromLegacy(TextWrapper.parseLine(
            selected ? PowerstoneUtil.POWER_SELECTED_TRUE : PowerstoneUtil.POWER_SELECTED_FALSE),
            '&'
        ));

        return builder.build();
    }


    public static class Builder implements ClassBuilder<SkyblockPowerGenerator> {
        private String name;
        private boolean renderBorder = true;
        private int maxLineLength = DEFAULT_MAX_LINE_LENGTH;
        private int padding;
        private int alpha;
        private PowerStrength strength;
        private List<ScalingPowerstoneStat> scalingStats = new ArrayList<>();
        private List<PowerstoneStat> staticStats = new ArrayList<>();
        private boolean selected = true;
        private int magicalPower = 1000;
        private boolean stonePower = true;

        public SkyblockPowerGenerator.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public SkyblockPowerGenerator.Builder withRenderBorder(boolean renderBorder) {
            this.renderBorder = renderBorder;
            return this;
        }

        public SkyblockPowerGenerator.Builder withMaxLineLength(int maxLineLength) {
            this.maxLineLength = maxLineLength;
            return this;
        }

        public SkyblockPowerGenerator.Builder withPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public SkyblockPowerGenerator.Builder withAlpha(int alpha) {
            this.alpha = alpha;
            return this;
        }

        public SkyblockPowerGenerator.Builder withStrength(PowerStrength strength) {
            this.strength = strength;
            return this;
        }

        public SkyblockPowerGenerator.Builder withScalingStats(List<ScalingPowerstoneStat> stats) {
            this.scalingStats.addAll(stats);
            return this;
        }

        public SkyblockPowerGenerator.Builder withScalingStat(ScalingPowerstoneStat stat) {
            this.scalingStats.add(stat);
            return this;
        }

        public SkyblockPowerGenerator.Builder withStaticStats(List<PowerstoneStat> stats) {
            this.staticStats.addAll(stats);
            return this;
        }

        public SkyblockPowerGenerator.Builder withStaticStat(PowerstoneStat stat) {
            this.staticStats.add(stat);
            return this;
        }

        public SkyblockPowerGenerator.Builder withSelected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public SkyblockPowerGenerator.Builder withMagicalPower(int magicalPower) {
            this.magicalPower = magicalPower;
            return this;
        }

        public SkyblockPowerGenerator.Builder withStonePower(boolean stonePower) {
            this.stonePower = stonePower;
            return this;
        }

        @Override
        public SkyblockPowerGenerator build() {
            return new SkyblockPowerGenerator(name, renderBorder, maxLineLength, padding, alpha, false, strength, scalingStats, staticStats, selected, magicalPower, stonePower);
        }
    }
}