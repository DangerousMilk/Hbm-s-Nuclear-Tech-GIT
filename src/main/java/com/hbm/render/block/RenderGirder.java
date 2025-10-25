package com.hbm.render.block;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockGirder;
import com.hbm.blocks.network.BlockCable;
import com.hbm.lib.Library;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.ObjUtil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class RenderGirder implements ISimpleBlockRenderingHandler {
	private int renderID;

	public RenderGirder(int renderType) {
		renderID = renderType;
	}
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = block.getIcon(0, 0);

		WavefrontObject model = (block == ModBlocks.steel_girder) ? (WavefrontObject) ResourceManager.steel_girder : (WavefrontObject) ResourceManager.steel_girder_bracket;

		tessellator.setColorOpaque_F(1, 1, 1);

		if(renderer.hasOverrideBlockTexture()) {
			iicon = renderer.overrideBlockTexture;
		}

		GL11.glRotated(180, 0, 1, 0);
		GL11.glScaled(1.25D, 1.25D, 1.25D);

		tessellator.startDrawingQuads();
		if(block == ModBlocks.steel_girder) {
			ObjUtil.renderPartWithIcon(model, "Core", iicon, tessellator, 0, false);
			ObjUtil.renderPartWithIcon(model, "Top", iicon, tessellator, 0, false);
			ObjUtil.renderPartWithIcon(model, "Bottom", iicon, tessellator, 0, false);
			ObjUtil.renderPartWithIcon(model, "Left", iicon, tessellator, 0, false);
			ObjUtil.renderPartWithIcon(model, "Right", iicon, tessellator, 0, false);
		} else {
			ObjUtil.renderWithIcon(model, iicon, tessellator, 0, false);
		}
		tessellator.draw();

		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

		Tessellator tessellator = Tessellator.instance;

		IIcon iicon = block.getIcon(0, 0);
		WavefrontObject model = (block == ModBlocks.steel_girder) ? (WavefrontObject) ResourceManager.steel_girder : (WavefrontObject) ResourceManager.steel_girder_bracket;

		tessellator.setColorOpaque_F(1, 1, 1);

		if(renderer.hasOverrideBlockTexture()) {
			iicon = renderer.overrideBlockTexture;
		}

		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		tessellator.setColorOpaque_F(1, 1, 1);

		int meta = world.getBlockMetadata(x, y, z);
		boolean topSide = (meta >= 5);
		boolean pX = Library.canConnect(world, x + 1, y, z, Library.POS_X);
		boolean nX = Library.canConnect(world, x - 1, y, z, Library.NEG_X);
		boolean pZ = Library.canConnect(world, x, y, z + 1, Library.POS_Z);
		boolean nZ = Library.canConnect(world, x, y, z - 1, Library.NEG_Z);
		float offset = (1F / 16F) * 6F;

		if(block == ModBlocks.steel_girder) {
			tessellator.addTranslation(x + 0.5F, y + (topSide ? offset : - offset), z + 0.5F);

			ObjUtil.renderPartWithIcon(model, "Core", iicon, tessellator, 0, true);
			if (nZ) {
				ObjUtil.renderPartWithIcon(model, "Top", iicon, tessellator, 0, true);
			}
			if (pZ) {
				ObjUtil.renderPartWithIcon(model, "Bottom", iicon, tessellator, 0, true);
			}
			if (nX) {
				ObjUtil.renderPartWithIcon(model, "Left", iicon, tessellator, 0, true);
			}
			if (pX) {
				ObjUtil.renderPartWithIcon(model, "Right", iicon, tessellator, 0, true);
			}

			tessellator.addTranslation(-x - 0.5F, -y - (topSide ? offset : - offset), -z - 0.5F);
		} else {
			float rot = 0;

			if(meta == 4 || meta == 9) {
				rot = 90F / 180F * (float) Math.PI;
			} else if(meta == 2 || meta == 7) {
				rot = 270F / 180F * (float) Math.PI;
			} else if(meta == 3 || meta == 8) {
				rot = 180F / 180F * (float) Math.PI;
			}

			tessellator.addTranslation(x + 0.5F, y + (topSide ? 0 : 1F), z + 0.5F);
			ObjUtil.renderWithIcon(model, iicon, tessellator, rot, (meta >= 5) ? 0F : (float) Math.PI, true);
			tessellator.addTranslation(-x - 0.5F, -y - (topSide ? 0 : 1F), -z - 0.5F);
		}

		return true;
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
