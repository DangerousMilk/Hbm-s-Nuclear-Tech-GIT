package com.hbm.render.block;

import com.hbm.blocks.generic.RailingBlock;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.ObjUtil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class RenderRailing implements ISimpleBlockRenderingHandler {

	private int renderID;
	private IModelCustom model;

	public RenderRailing(int renderType, IModelCustom IModelCustom) {
		renderID = renderType;
		model = IModelCustom;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.instance;

		IIcon steelIcon = block.getIcon(0, 0);
		IIcon grateIcon = block.getIcon(1, 0);
		tessellator.setColorOpaque_F(1, 1, 1);

		GL11.glTranslated(0, -0.5D, 0);
		GL11.glScaled(1D, 1D, 1D);
		tessellator.startDrawingQuads();
		if(this.renderID == RailingBlock.renderIDStairs) {
			ObjUtil.renderPartWithIcon((WavefrontObject) model, "RailingStairs", steelIcon, tessellator, 0, false);
			ObjUtil.renderPartWithIcon((WavefrontObject) model, "RailingSteps", grateIcon, tessellator, 0, false);
		} else {
			ObjUtil.renderWithIcon((WavefrontObject) model, steelIcon, tessellator, 0, false);
		}
		tessellator.draw();
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		int meta = world.getBlockMetadata(x, y, z);
		IIcon steelIcon = block.getIcon(0, 0);
		IIcon grateIcon = block.getIcon(1, 0);
		tessellator.setColorOpaque_F(1, 1, 1);

		/*
		if(renderer.hasOverrideBlockTexture()) {
			steel = renderer.overrideBlockTexture;
		}
		 */

		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		tessellator.setColorOpaque_F(1, 1, 1);

		float rotation = 0;

		switch(meta) {
			case 2:
				rotation = 90F / 180F * (float) Math.PI;
				break;
			case 3:
				rotation = 270F / 180F * (float) Math.PI;
				break;
			case 4:
				rotation = 180F / 180F * (float) Math.PI;
				break;
		}

		tessellator.addTranslation(x + 0.5F, y, z + 0.5F);
		if(this.renderID == RailingBlock.renderIDStairs) {
			ObjUtil.renderPartWithIcon((WavefrontObject) model, "RailingStairs", steelIcon, tessellator, rotation, true);
			ObjUtil.renderPartWithIcon((WavefrontObject) model, "RailingSteps", grateIcon, tessellator, rotation, true);
		} else {
			ObjUtil.renderWithIcon((WavefrontObject) model, steelIcon, tessellator, rotation, true);
		}
		tessellator.addTranslation(-x - 0.5F, -y, -z - 0.5F);

		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return this.renderID;
	}

}
