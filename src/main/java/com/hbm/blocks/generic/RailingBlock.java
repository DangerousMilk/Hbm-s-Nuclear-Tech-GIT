package com.hbm.blocks.generic;

import api.hbm.block.IToolable;
import com.hbm.blocks.BlockMulti;
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

public class RailingBlock extends BlockMulti implements IToolable, INBTBlockTransformable {
	protected String[] variants = new String[] {"block_steel", "scaffold_red", "scaffold_white", "scaffold_yellow"};
	@SideOnly(Side.CLIENT) protected IIcon[] icons;
	@SideOnly(Side.CLIENT) private IIcon grateIcon;

	private double thickness = 0.08D;
	private double wall_height = 1.1D;

	public RailingBlock(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon(RefStrings.MODID + ":block_steel");
		this.grateIcon = reg.registerIcon(RefStrings.MODID + ":grate_top");

		this.icons = new IIcon[variants.length];
		for(int i = 0; i < variants.length; i++) {
			this.icons[i] = reg.registerIcon(RefStrings.MODID + ":" + variants[i]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch (side) {
			case 1:
				return this.grateIcon;
			default:
				return this.icons[this.damageDropped(meta)];
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

		/*
		if(i == 0) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if(i == 1) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		if(i == 2) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if(i == 3) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		 */

		int meta = itemStack.getItemDamage();

		if(i == 0) world.setBlockMetadataWithNotify(x, y, z, meta + 4, 2);
		if(i == 1) world.setBlockMetadataWithNotify(x, y, z, meta + 8, 2);
		if(i == 2) world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		if(i == 3) world.setBlockMetadataWithNotify(x, y, z, meta + 12, 2);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int te = world.getBlockMetadata(x, y, z);
		float f = 0.0625F;

		if(this == ModBlocks.steel_railing) {
			if (te >= 12) {
				this.setBlockBounds(1.0F - (float) thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
			else if(te >= 8) {
				this.setBlockBounds(0.0F, 0.0F, 0.0F, (float)thickness, 1.0F, 1.0F);
			}
			else if(te >= 4) {
				this.setBlockBounds(0.0F, 0.0F, 1.0F - (float) thickness, 1.0F, 1.0F, 1.0F);
			}
			else { this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, (float) thickness);
			}
		}
		else if(this == ModBlocks.steel_stairs) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
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

		if(this == ModBlocks.steel_railing)
		{
			if(meta >= 12) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1.0D - thickness, y + 0.0D, z + 0.0D, x + 1.0D, y + wall_height, z + 1.0D));
			}
			else if(meta >= 8) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0.0D, y + 0.0D, z + 0.0D, x + thickness, y + wall_height, z + 1.0D));
			}
			else if(meta >= 4) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0.0D, y + 0.0D, z + 1.0D - thickness, x + 1.0D, y + wall_height, z + 1.0D));
			}
			else {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0.0D, y + 0.0D, z + 0.0D, x + 1.0D, y + wall_height, z + thickness));
			}
		}
		else if(this == ModBlocks.steel_railing_corner) {
			if(meta >= 12) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + wall_height, z + thickness));
			}
			else if(meta >= 8) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + wall_height, z + 1D));
			}
			else if(meta >= 4) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + wall_height, z + 1D));
			}
			else {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + wall_height, z + thickness));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + wall_height, z + 1D));
			}
		} else if(this == ModBlocks.steel_railing_straight) {
			if(meta >= 12) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + wall_height, z + thickness));
			}
			else if(meta >= 8) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + wall_height, z + thickness));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + wall_height, z + 1D));
			}
			else if(meta >= 4) {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + wall_height, z + 1D));
			}
			else {
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + wall_height, z + 1D));
			}
		} else if(this == ModBlocks.steel_railing_end) {
			if(meta >= 12) {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + wall_height, z + thickness));
				// Back
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + wall_height, z + 1D));
			}
			else if(meta >= 8) {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + wall_height, z + thickness));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + wall_height, z + 1D));
				// Back
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + wall_height, z + 1D));
			}
			else if(meta >= 4) {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + wall_height, z + 1D));
				// Back
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + wall_height, z + 1D));
			}
			else {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + wall_height, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + wall_height, z + 1D));
				// Back
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + wall_height, z + thickness));
			}
		} else if(this == ModBlocks.steel_stairs) {
			if(meta >= 12) {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + 2, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 2, z + thickness));

				// Steps
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 0.5D, y + 0.5D, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0.5D, y + 0.5D, z + 0D, x + 1D, y + 1D, z + 1D));
			}
			else if(meta >= 8) {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 2, z + thickness));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 1D - thickness, x + 1D, y + 2, z + 1D));

				// Steps
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0.5D, y + 0D, z + 0D, x + 1D, y + 0.5D, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0.5D, z + 0D, x + 0.5D, y + 1D, z + 1D));
			}
			else if(meta >= 4) {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + 2, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + 2, z + 1D));

				// Steps
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 0.5D, z + 0.5D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0.5D, z + 0.5D, x + 1D, y + 1D, z + 1D));
			}
			else {
				// Sides
				bbs.add(AxisAlignedBB.getBoundingBox(x + 1D - thickness, y + 0D, z + 0D, x + 1D, y + 2D, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + thickness, y + 2, z + 1D));

				// Steps
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0.5D, x + 1D, y + 0.5D, z + 1D));
				bbs.add(AxisAlignedBB.getBoundingBox(x + 0D, y + 0.5D, z + 0D, x + 1D, y + 1D, z + 0.5D));
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

	@Override
	public int getSubCount() {
		return variants.length;
	}
}
