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
		Location bottom = MainUtil.getPlotBottomLoc(plotworld.worldname, plot.getId());
		Location top = MainUtil.getPlotTopLoc(plotworld.worldname, plot.getId());
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
		int lower;
		//Splitting road in half
		if ((mpw.ROAD_WIDTH % 2) == 0) {
            lower = (mpw.ROAD_WIDTH / 2) - 1;
        } else {
            lower = (mpw.ROAD_WIDTH / 2);
        }
		int size = mpw.PLOT_WIDTH+mpw.ROAD_WIDTH;
		int px= x / size;
		int pz =z /size;
		//Since don't start at 0
		if(px>=0){
			px++;
		}
		if(pz>=0){
			pz++;
		}
		
		int rx = px % size;
		int rz = pz % size;
		if(rx < 0){
			rx+=size;
		}
		if(rz < 0){
			rz+=size;
		}
		//If is north/south of plot (on road/wall)
        final boolean northSouth = (rz <= lower) || (rz > (mpw.PLOT_WIDTH + lower));
		//If is east/west of plot (on road/wall)
        final boolean eastWest = (rx <= lower) || (rx > (mpw.PLOT_WIDTH + lower));
		
		//This stuff pretty much involves merging, since the walls/road no longer exist. ID will be null if not merged and not in plot
        if (northSouth && eastWest) {
            // This means you are in the intersection
            final Location loc = new Location(pw.worldname, x + 5, 0, z + 5);
            final Plot plot = MainUtil.getPlot(loc);
            if (plot == null) {
                return null;
            }
            if ((plot.getMerged(0) && plot.getMerged(3))) {
                return MainUtil.getBottomPlot(plot).id;
            }
            return null;
        }
        if (northSouth) {
            // You are on a road running West to East (yeah, I named the var poorly)
            final Location loc = new Location(pw.worldname, x, 0, z + 5);
            final Plot plot = MainUtil.getPlot(loc);
            if (plot == null) {
                return null;
            }
            if (plot.getMerged(0)) {
                return MainUtil.getBottomPlot(plot).id;
            }
            return null;
        }
        if (eastWest) {
            // This is the road separating an Eastern and Western plot
            final Location loc = new Location(pw.worldname, x + 5, 0, z);
            final Plot plot = MainUtil.getPlot(loc);
            if (plot == null) {
                return null;
            }
            if (plot.getMerged(3)) {
                return MainUtil.getBottomPlot(plot).id;
            }
            return null;
        }
      //Since we already said X and Z are coord/size (they are int too)
        final PlotId id = new PlotId(px, pz);
		//TODO: Why not just stop here? I'm confused
        final Plot plot = PS.get().getPlots(pw.worldname).get(id);
        if (plot == null) {
            return id;
        }
        return MainUtil.getBottomPlot(plot).id;
	}

	@Override
	public PlotId getPlotIdAbs(PlotWorld pw, int x, int y, int z) {
		final MarkPlotWorld mpw = (MarkPlotWorld) pw;
		//lower is half of the road width or where R is road and p is plot R/2 - P - R/2 it is R/2
        int lower;
        if ((mpw.ROAD_WIDTH % 2) == 0) {
            lower = (mpw.ROAD_WIDTH / 2) - 1;
        } else {
            lower = (mpw.ROAD_WIDTH / 2);
        }
        final int size = (mpw.ROAD_WIDTH+mpw.PLOT_WIDTH);
        int X = (x / size);
        int Z = (z / size);
        if (x >= 0) {
            X++;
        }
        if (z >= 0) {
            Z++;
        }
		//TODO: Confused... why not just use size here?
        int xx = x % size;
        int zz = z % size;
        if (xx < 0) {
            xx += size;
        }
        if (zz < 0) {
            zz += size;
        }
        if ((xx > (mpw.PLOT_WIDTH + lower)) || (zz > (mpw.PLOT_WIDTH + lower)) || (xx <= lower) || (zz <= lower)) {
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
	public Location getSignLoc(PlotWorld arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return null;
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

}
