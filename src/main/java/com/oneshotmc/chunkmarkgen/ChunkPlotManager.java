package com.oneshotmc.chunkmarkgen;

import java.util.ArrayList;

import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.generator.GridPlotManager;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotBlock;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotWorld;
import com.intellectualcrafters.plot.util.MainUtil;
import com.intellectualcrafters.plot.util.TaskManager;

public class ChunkPlotManager extends GridPlotManager{
	
	@Override
	public boolean claimPlot(PlotWorld arg0, Plot arg1) {
		return true;
	}
	
	private int chunkLoc(Integer val){
		return val >> 4;
	}
	@Override
	public boolean clearPlot(final PlotWorld plotworld, final Plot plot, final Runnable whenDone) {
		Location bottom = MainUtil.getPlotBottomLocAbs(plotworld.worldname, plot.getId());
		Location top = MainUtil.getPlotTopLocAbs(plotworld.worldname, plot.getId());
		final ArrayList<ChunkLoc> chunks = new ArrayList<>();
		for(int x=chunkLoc(bottom.getX());x<=chunkLoc(top.getX());x++){
			for(int z=chunkLoc(bottom.getZ());z<=chunkLoc(top.getZ());z++){
				chunks.add(new ChunkLoc(x,z));
			}
		}
		final World world = Bukkit.getWorld(plotworld.worldname);
		final MutableInt id = new MutableInt(0);
        id.setValue(TaskManager.runTaskRepeat(new Runnable() {
            @Override
            public void run() {
                if (chunks.size() == 0) {
                    Bukkit.getScheduler().cancelTask(id.intValue());
                    MainUtil.update(plot);
                    whenDone.run();
                    return;
                }
                final long start = System.currentTimeMillis();
                while (((System.currentTimeMillis() - start) < 25) && (chunks.size() > 0)) {
                    final ChunkLoc loc = chunks.remove(0);
                    if (world.loadChunk(loc.x, loc.z, false)) {
                        world.regenerateChunk(loc.x, loc.z);
                    }
                }
            }
        }, 1));
        return true;
	}
	
	@Override
	public boolean createRoadEast(PlotWorld arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean createRoadSouth(PlotWorld arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean createRoadSouthEast(PlotWorld arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean finishPlotMerge(PlotWorld arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean finishPlotUnlink(PlotWorld arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Location getPlotBottomLocAbs(PlotWorld pw, PlotId id) {
		final MarkPlotWorld mpw = (MarkPlotWorld) pw;
		return getPlotTopLocAbs(pw,id).add(-mpw.PLOT_WIDTH, 1, -mpw.PLOT_WIDTH);
	}

	@Override
	public String[] getPlotComponents(PlotWorld arg0, PlotId arg1) {
		// TODO Auto-generated method stub
		return new String[] { "floor", "wall", "border" };
	}

	@Override
	public PlotId getPlotId(PlotWorld pw, int x, int y, int z) {
		MarkPlotWorld mpw = (MarkPlotWorld)pw;
		int size = mpw.PLOT_WIDTH+mpw.ROAD_WIDTH;
		int X= x / size;
		int Z =z /size;
		if (x >= 0) {
            X++;
        }
        if (z >= 0) {
            Z++;
        }
		int px = x % size;
		int pz = z % size;
		//Since don't start at 0
		if(px>=0){
			px++;
		}
		if(pz>=0){
			pz++;
		}
		int lower;
		//Splitting road in half
		if ((mpw.ROAD_WIDTH % 2) == 0) {
            lower = (mpw.ROAD_WIDTH / 2) - 1;
        } else {
            lower = (mpw.ROAD_WIDTH / 2);
        }
		
		if(px < lower || px > mpw.PLOT_WIDTH+lower){
			return null;
		}
		//Since we know it MUST be in plot boundries
        return new PlotId(X, Z);
	}

	@Override
	public Location getPlotTopLocAbs(PlotWorld pw, PlotId id) {
		int px = id.x;
		//Should be id.z :p
		int pz = id.y;
		final MarkPlotWorld mpw = (MarkPlotWorld) pw;
		final int x = (px * (mpw.ROAD_WIDTH + mpw.PLOT_WIDTH)) - ((int) Math.floor(mpw.ROAD_WIDTH / 2)) - 1;
        final int z = (pz * (mpw.ROAD_WIDTH + mpw.PLOT_WIDTH)) - ((int) Math.floor(mpw.ROAD_WIDTH / 2)) - 1;
        return new Location(pw.worldname,x,256,z);
	}

	@Override
	public Location getSignLoc(PlotWorld pw, Plot plot) {
		MarkPlotWorld mpw = (MarkPlotWorld) pw;
		final Location bot = MainUtil.getPlotBottomLocAbs(pw.worldname, plot.id);
        return new com.intellectualcrafters.plot.object.Location(pw.worldname, bot.getX(), mpw.PLOT_HEIGHT + 1, bot.getZ() - 1);
	}

	@Override
	public boolean removeRoadEast(PlotWorld arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRoadSouth(PlotWorld arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRoadSouthEast(PlotWorld arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setComponent(PlotWorld arg0, PlotId arg1, String arg2, PlotBlock[] arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startPlotMerge(PlotWorld arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean startPlotUnlink(PlotWorld arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean unclaimPlot(PlotWorld arg0, Plot arg1, Runnable arg2) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public PlotId getPlotIdAbs(PlotWorld pw, int x, int y, int z) {
		MarkPlotWorld mpw = (MarkPlotWorld) pw;
		int size = mpw.PLOT_WIDTH+mpw.ROAD_WIDTH;
		int X= x / size;
		int Z =z /size;
		if (x >= 0) {
            X++;
        }
        if (z >= 0) {
            Z++;
        }
		int px = x % size;
		int pz = z % size;
		//Since don't start at 0
		if(px>=0){
			px++;
		}
		if(pz>=0){
			pz++;
		}
		return new PlotId(X,Z);
	}

}
