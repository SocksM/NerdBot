package net.hypixel.nerdbot.generator;

import net.hypixel.nerdbot.generator.exception.GeneratorException;
import net.hypixel.nerdbot.generator.util.Item;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final List<Generator> generators;

    public ItemBuilder() {
        this.generators = new ArrayList<>();
    }

    public ItemBuilder addGenerator(Generator generator) {
        this.generators.add(generator);
        return this;
    }

    public ItemBuilder addGenerator(int position, Generator generator) {
        this.generators.add(position, generator);
        return this;
    }

    public Item build() {
        int totalWidth = 0;
        int maxHeight = 0;

        for (Generator generator : generators) {
            Item generatedItem = generator.generate();
            BufferedImage generatedImage = generatedItem.getImage();

            if (generatedImage == null) {
                throw new GeneratorException("Could not generate that image!");
            }

            totalWidth += generatedImage.getWidth();
            maxHeight = Math.max(maxHeight, generatedImage.getHeight());
        }

        BufferedImage finalImage = new BufferedImage(totalWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = finalImage.createGraphics();

        int x = 0;

        for (Generator generator : generators) {
            Item generatedItem = generator.generate();
            BufferedImage generatedImage = generatedItem.getImage();

            if (generatedImage == null) {
                throw new GeneratorException("Could not generate that image!");
            }

            // Calculate the vertical offset to center the image vertically
            int yOffset = (maxHeight - generatedImage.getHeight()) / 2;

            // Draw the image with the calculated vertical offset
            graphics.drawImage(generatedImage, x, yOffset, null);

            // Adjust the x position for the next generator
            x += generatedImage.getWidth();
        }

        graphics.dispose();

        return new Item(finalImage);
    }
}