package com.hbm.blocks.generic;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockGirderBracket extends BlockGirder {
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	public BlockGirderBracket(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	public int getRenderType() { return renderID; }

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
		int meta = world.getBlockMetadata(x, y, z);
		int i = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if(i == 0) world.setBlockMetadataWithNotify(x, y, z, 1 + meta, 2);
		if(i == 1) world.setBlockMetadataWithNotify(x, y, z, 2 + meta, 2);
		if(i == 2) world.setBlockMetadataWithNotify(x, y, z, 3 + meta, 2);
		if(i == 3) world.setBlockMetadataWithNotify(x, y, z, 4 + meta, 2);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		float f = 0.0625F;

		if(meta == 1 || meta == 6 || meta == 3 || meta == 8) {
			this.setBlockBounds(6f * f, 0.0F, 0.0F, 10F * f, 1.0F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 6f * f, 1.0F, 1.0F, 10F * f);
		}
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity collider) {
		int meta = world.getBlockMetadata(x, y, z);
		List<AxisAlignedBB> bbs = new ArrayList<>();

		double f = 0.0625D;
		double minY = (meta > 5) ? f * 12D : 0D;
		double maxY = (meta > 5) ? 1D : f * 4D;

		if(meta == 1 || meta == 6) {
			bbs.add(AxisAlignedBB.getBoundingBox(x + 6D * f, y + minY, z + 0D, x + 10D * f, y + maxY, z + 12D * f));
			bbs.add(AxisAlignedBB.getBoundingBox(x + 6D * f, y + 0D, z + 12D * f, x + 10D * f, y + 1D, z + 1D));
		} else if(meta == 2 || meta == 7) {
			bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + minY, z + 6D * f, x + 12D * f, y + maxY, z + 10D * f));
			bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 6D * f, x + 4D * f, y + 1D, z + 10D * f));
		} else if(meta == 3 || meta == 8) {
			bbs.add(AxisAlignedBB.getBoundingBox(x + 6D * f, y + minY, z + 0D, x + 10D * f, y + maxY, z + 1D));
			bbs.add(AxisAlignedBB.getBoundingBox(x + 6D * f, y + 0D, z + 0D, x + 10D * f, y + 1D, z + 4D * f));
		} else if(meta == 4 || meta == 9) {
			bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + minY, z + 6D * f, x + 12D * f, y + maxY, z + 10D * f));
			bbs.add(AxisAlignedBB.getBoundingBox(x + 12D * f, y + 0D, z + 6D * f, x + 1D, y + 1D, z + 10D * f));
		}

		for(AxisAlignedBB bb : bbs) {
			if(aabb.intersectsWith(bb)) {
				list.add(bb);
			}
		}
	}

	public boolean canConnect(IBlockAccess world, int x, int y, int z, ForgeDirection dir)
	{
		int meta = world.getBlockMetadata(x, y, z);

		switch (dir) {
			case NORTH:
				if(meta == 1 || meta == 6) return true;
				break;
			case WEST:
				if(meta == 4 || meta == 9) return true;
				break;
			case SOUTH:
				if(meta == 3 || meta == 8) return true;
				break;
			case EAST:
				if(meta == 2 || meta == 7) return true;
				break;
		}

		return false;
	}
}
