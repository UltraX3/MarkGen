package com.oneshotmc.chunkmarkgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.ChunkLoc;
import com.intellectualcrafters.plot.object.PlotManager;
import com.intellectualcrafters.plot.object.PlotWorld;
import com.intellectualcrafters.plot.object.PseudoRandom;
import com.intellectualcrafters.plot.object.RegionWrapper;
import com.intellectualcrafters.plot.util.MainUtil;
import com.plotsquared.bukkit.generator.AugmentedPopulator;
import com.plotsquared.bukkit.generator.BukkitPlotGenerator;
import com.plotsquared.bukkit.generator.BukkitPlotPopulator;

public class Generator extends BukkitPlotGenerator {

	public Generator(String world) {
		super(world);
		MainUtil.initCache();
		// TODO Auto-generated constructor stub
	}
	
	public final static short HEIGHT = 64;
	public final static short MAIN_BLOCK = 155; // Plot main filling (stone)
	public final static short OTHER_BLOCK = 173;
	public final static short FLOOR_BLOCK = 2; // Plot top floor (grass)
	public final static short BOTTOM_BLOCK = 7; // Bottom bedrock

	private static PlotManager manager = new ChunkPlotManager();

	@Override
	public short[][] generateExtBlockSections(final World world, final Random r, final int X, final int Z,
			final BiomeGrid biomes) {
		final boolean other = (X+Z)%2==0;
		int CX = X >> 4;
		int CZ = Z >> 4;
		final int blockID;
		final MarkPlotWorld pw = (MarkPlotWorld) PS.get().getPlotWorld(world.getName());
		final short[][] result = new short[16][];
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y <= HEIGHT; y++) {
					if(other)
						setBlock(result, x, y, z, OTHER_BLOCK);
					else
						setBlock(result,x,y,z, MAIN_BLOCK);
				}
			}
		}
		return result;

	}

	@Override
	public void generateChunk(final World world, final RegionWrapper region, final PseudoRandom random, final int X,
			final int Z, final BiomeGrid grid) {
		return;
		// MegaPlotWorld pw = (MegaPlotWorld)
		// PlotSquared.getPlotWorld(world.getName());
		// if (pw.BASE_WORLD == null) {
		// System.out.print("WORLD NOT LOADED: " + X + "," + Z);
		// for (int x = 0; x < 16; x++) {
		// for (int z = 0; z < 16; z++) {
		// for (int y = 0; y < 64; y++) {
		// setBlock(x, y, z, (short) 155);
		// }
		// }
		// }
		// return;
		// }
		// int cx = X % (pw.MCA_WIDTH * 32);
		// int cz = Z % (pw.MCA_WIDTH * 32);
		// if (cx < 0) {
		// cx += (pw.MCA_WIDTH * 32);
		// }
		// if (cz < 0) {
		// cz += (pw.MCA_WIDTH * 32);
		// }
		// cx += pw.mx * 32;
		// cz += pw.mx * 32;
		// Chunk chunk = pw.BASE_WORLD.getChunkAt(cx, cz);
		// chunk.load(false);
		// if (!chunk.isLoaded()) {
		// System.out.print("CHUNK NOT LOADED!!!!!!!!!!!!!");
		// }
		// int xx = X << 4;
		// int zz = Z << 4;
		// for (int x = 0; x < 16; x++) {
		// for (int z = 0; z < 16; z++) {
		// for (int y = 0; y < 256; y++) {
		// Block block = chunk.getBlock(x, y, z);
		// int id = block.getTypeId();
		// if (id != 0) {
		// setBlock(x, y, z, (short) id);
		// byte data = block.getData();
		// if (data != 0) {
		// SetBlockQueue.setData(pw.worldname, xx + x, y, zz + z, data);
		// }
		// }
		// }
		// }
		// }
	}

	@Override
	public PlotWorld getNewPlotWorld(final String world) {
		return new MarkPlotWorld(world);
	}

	@Override
	public PlotManager getPlotManager() {
		return manager;
	}

	private List<BukkitPlotPopulator> pop;

	public void setBlock(final short[][] result, final int x, final int y, final int z, final short blkid) {
		if (result[y >> 4] == null) {
			result[y >> 4] = new short[4096];
		}
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
	}

	@Override
	public void init(final PlotWorld world) {
	}

	//TODO: confused...
	@Override
	public List<BukkitPlotPopulator> getPopulators(String arg0) {
		return new ArrayList<BukkitPlotPopulator>();
	}

}