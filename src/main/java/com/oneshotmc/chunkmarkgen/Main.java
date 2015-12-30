package com.oneshotmc.chunkmarkgen;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public ChunkGenerator getDefaultWorldGenerator(final String worldName, final String id) {
        return new Generator(worldName);
    }
}