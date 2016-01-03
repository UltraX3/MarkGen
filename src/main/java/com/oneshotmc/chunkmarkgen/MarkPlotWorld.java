package com.oneshotmc.chunkmarkgen;

import com.intellectualcrafters.configuration.ConfigurationSection;
import com.intellectualcrafters.plot.config.ConfigurationNode;
import com.intellectualcrafters.plot.generator.ClassicPlotWorld;
import com.intellectualcrafters.plot.generator.GridPlotWorld;

public class MarkPlotWorld extends GridPlotWorld{
	public int ROAD_WIDTH;
    public int PLOT_WIDTH;
    public int PLOT_HEIGHT;
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
        return new ConfigurationNode[0];
    }
	
	@Override
    public void loadConfiguration(final ConfigurationSection arg0) {
        // Nothing is configurable :P
    }

}
