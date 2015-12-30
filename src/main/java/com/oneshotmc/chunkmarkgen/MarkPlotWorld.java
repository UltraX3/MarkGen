package com.oneshotmc.chunkmarkgen;

import com.intellectualcrafters.plot.config.ConfigurationNode;
import com.intellectualcrafters.plot.generator.ClassicPlotWorld;

public class MarkPlotWorld extends ClassicPlotWorld{

	public MarkPlotWorld(String worldname) {
		super(worldname);
		this.PLOT_WIDTH=16*32;
		this.ROAD_WIDTH=16*16;
		this.ALLOW_SIGNS=false;
		this.PLOT_HEIGHT=16;
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public ConfigurationNode[] getSettingNodes() {
        return new ConfigurationNode[]{};
    }

}
