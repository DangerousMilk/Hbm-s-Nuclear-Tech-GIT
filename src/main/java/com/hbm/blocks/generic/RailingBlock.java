package com.hbm.blocks.generic;

import api.hbm.block.IToolable;
import com.hbm.blocks.ModBlocks;
import com.hbm.lib.RefStrings;
import com.hbm.world.gen.nbt.INBTBlockTransformable;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RailingBlock extends Block implements IToolable, INBTBlockTransformable {
	@SideOnly(Side.CLIENT)
	private IIcon steelIcon;
	@SideOnly(Side.CLIENT)
	private IIcon grateIcon;

	public RailingBlock(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.steelIcon = reg.registerIcon(RefStrings.MODID + ":block_steel");
		this.grateIcon = reg.registerIcon(RefStrings.MODID + ":grate_top");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		switch (side) {
			case 1:
				return this.grateIcon;
			default:
				return this.steelIcon;
		}
	}

	@Override
	public boolean onScrew(World world, EntityPlayer player, int x, int y, int z, int side, float fX, float fY, float fZ, ToolType tool) {
		if(tool != ToolType.SCREWDRIVER) return false;
		if (this != ModBlocks.steel_railing_end
			&& this != ModBlocks.steel_stairs
			&& this != ModBlocks.steel_railing_corner
			&& this != ModBlocks.steel_railing_straight
			&& this != ModBlocks.steel_railing) return false;

		int meta = world.getBlockMetadata(x, y, z);

		if(!player.isSneaking()) {
			if(meta == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 3);
			else if(meta == 4) world.setBlockMetadataWithNotify(x, y, z, 2, 3);
			else if(meta == 2) world.setBlockMetadataWithNotify(x, y, z, 5, 3);
			else if(meta == 5) world.setBlockMetadataWithNotify(x, y, z, 3, 3);
		} else {
			if(meta == 3) world.setBlockMetadataWithNotify(x, y, z, 5, 3);
			else if(meta == 4) world.setBlockMetadataWithNotify(x, y, z, 3, 3);
			else if(meta == 2) world.setBlockMetadataWithNotify(x, y, z, 4, 3);
			else if(meta == 5) world.setBlockMetadataWithNotify(x, y, z, 2, 3);
		}

		return true;
	}

	@Override @SideOnly(Side.CLIENT) public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) { return true; }

	public static int renderIDRailing = RenderingRegistry.getNextAvailableRenderId();
	public static int renderIDRailingCorner = RenderingRegistry.getNextAvailableRenderId();
	public static int renderIDRailingStraight = RenderingRegistry.getNextAvailableRenderId();
	public static int renderIDRailingEnd = RenderingRegistry.getNextAvailableRenderId();
	public static int renderIDStairs = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public int getRenderType(){
		if(this == ModBlocks.steel_railing) return renderIDRailing;
		else if(this == ModBlocks.steel_railing_corner) return renderIDRailingCorner;
		else if(this == ModBlocks.steel_railing_straight) return renderIDRailingStraight;
		else if(this == ModBlocks.steel_railing_end) return renderIDRailingEnd;
		else if(this == ModBlocks.steel_stairs) return renderIDStairs;
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
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
		int i = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if(i == 0) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if(i == 1) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		if(i == 2) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if(i == 3) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int te = world.getBlockMetadata(x, y, z);
		float f = 0.0625F;

		if(this == ModBlocks.steel_railing) {
			switch(te) {
				case 2: this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2 * f); break;
				case 3: this.setBlockBounds(0.0F, 0.0F, 14 * f, 1.0F, 1.0F, 1.0F); break;
				case 4: this.setBlockBounds(0.0F, 0.0F, 0.0F, 2 * f, 1.0F, 1.0F); break;
				case 5: this.setBlockBounds(14 * f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F); break;
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity collider) {
		int meta = world.getBlockMetadata(x, y, z);
		List<AxisAlignedBB> bbs = new ArrayList<>();

		if(this == ModBlocks.steel_railing_corner) {
			switch(meta) {
				case 2:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 0.125D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.125D, y + 1D, z + 1D));
					break;
				case 3:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.875D, x + 1D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.875D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D));
					break;
				case 4:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.125D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.875D, x + 1D, y + 1D, z + 1D));
					break;
				case 5:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.875D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 0.125D));
					break;
			}
		} else if(this == ModBlocks.steel_railing_straight) {
			switch(meta) {
				case 2:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.875D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.125D, y + 1D, z + 1D));
					break;
				case 3:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.125D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.875D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D));
					break;
				case 4:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 0.125D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.875D, x + 1D, y + 1D, z + 1D));
					break;
				case 5:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.875D, x + 1D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 0.125D));
					break;
			}
		} else if(this == ModBlocks.steel_railing_end) {
			switch(meta) {
				case 2:
					// Sides
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.875D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.125D, y + 1D, z + 1D));
					// Back
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 0.125D));
					break;
				case 3:
					// Sides
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.125D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.875D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D));
					// Back
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.875D, x + 1D, y + 1D, z + 1D));
					break;
				case 4:
					// Sides
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 0.125D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.875D, x + 1D, y + 1D, z + 1D));
					// Back
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.125D, y + 1D, z + 1D));
					break;
				case 5:
					// Sides
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.875D, x + 1D, y + 1D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 0.125D));
					// Back
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.875D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D));
					break;
			}
		} else if(this == ModBlocks.steel_stairs) {
			switch(meta) {
				case 2:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.5D, x + 1D, y + 0.5D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0.5D, z + 0D, x + 1D, y + 1D, z + 0.5D));
					break;
				case 3:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 0.5D, z + 0.5D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0.5D, z + 0.5D, x + 1D, y + 1D, z + 1D));
					break;
				case 4:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.5D, y + 0D, z + 0D, x + 1D, y + 0.5D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0.5D, z + 0D, x + 0.5D, y + 1D, z + 1D));
					break;
				case 5:
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.5D, y + 0.5D, z + 1D));
					bbs.add(AxisAlignedBB.getBoundingBox(x + 0.5D, y + 0.5D, z + 0D, x + 1D, y + 1D, z + 1D));
					break;
			}
		}

		if(bbs.isEmpty()) {
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, collider);
		} else {
			for(AxisAlignedBB bb : bbs) {
				if(aabb.intersectsWith(bb)) {
					list.add(bb);
				}
			}
		}
	}

	@Override
	public int transformMeta(int meta, int coordBaseMode) {
		return INBTBlockTransformable.transformMetaDeco(meta, coordBaseMode);
	}
}
