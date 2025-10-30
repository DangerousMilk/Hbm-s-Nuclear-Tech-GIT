package com.hbm.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.lib.Library;
import com.hbm.lib.RefStrings;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockGirder extends Block {
	public BlockGirder(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon(RefStrings.MODID + ":scaffold_steel");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_)
	{
		return this.blockIcon;
	}

	public static int girderRenderID = RenderingRegistry.getNextAvailableRenderId();
	public static int girderBracketRenderID = RenderingRegistry.getNextAvailableRenderId();
	public static int girderWallRenderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public int getRenderType() {
		if(this == ModBlocks.steel_girder) return girderRenderID;
		if(this == ModBlocks.steel_girder_bracket) return girderBracketRenderID;
		if(this == ModBlocks.steel_girder_wall) return girderWallRenderID;
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hX, float hY, float hZ, int meta) {
		if(this != ModBlocks.steel_girder || this != ModBlocks.steel_girder_bracket) return 0;
		if(side == 0)
			// Bottom
			return 5;
		else if(side == 1)
			// Top
			return 0;
		else if(hY > 0.5F)
			return 5;
		else if(hY < 0.5F)
			return 0;

		return  0;
	}

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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);

		boolean posX = Library.canConnect(world, x + 1, y, z, Library.POS_X);
		boolean negX = Library.canConnect(world, x - 1, y, z, Library.NEG_X);
		boolean posZ = Library.canConnect(world, x, y, z + 1, Library.POS_Z);
		boolean negZ = Library.canConnect(world, x, y, z - 1, Library.NEG_Z);

		setBlockBounds(posX, negX, posZ, negZ, meta);
		return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if(this == ModBlocks.steel_girder_bracket) {
			int meta = world.getBlockMetadata(x, y, z);
			float f = 0.0625F;

			if(meta == 1 || meta == 6 || meta == 3 || meta == 8) {
				this.setBlockBounds(6f * f, 0.0F, 0.0F, 10F * f, 1.0F, 1.0F);
			} else {
				this.setBlockBounds(0.0F, 0.0F, 6f * f, 1.0F, 1.0F, 10F * f);
			}
		} else {
			boolean posX = Library.canConnect(world, x + 1, y, z, Library.POS_X);
			boolean negX = Library.canConnect(world, x - 1, y, z, Library.NEG_X);
			boolean posZ = Library.canConnect(world, x, y, z + 1, Library.POS_Z);
			boolean negZ = Library.canConnect(world, x, y, z - 1, Library.NEG_Z);

			setBlockBounds(posX, negX, posZ, negZ, world.getBlockMetadata(x, y, z));
		}
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity collider) {
		if(this != ModBlocks.steel_girder_bracket) return;

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
		if(this == ModBlocks.steel_girder) {
			return world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == world.getBlockMetadata(x, y, z);
		} else if(this == ModBlocks.steel_girder_bracket) {
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

		return false;
	}

	private void setBlockBounds(boolean posX, boolean negX, boolean posZ, boolean negZ, int meta) {

		float pixel = 0.0625F;
		float min = pixel * 6F;
		float max = pixel * 10F;

		float minX = negX ? 0F : min;
		float maxX = posX ? 1F : max;
		float minY = (meta >= 5) ? pixel * 12F : 0F;
		float maxY = (meta >= 5) ? 1F : pixel * 4F;
		float minZ = negZ ? 0F : min;
		float maxZ = posZ ? 1F : max;

		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
