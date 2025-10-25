package com.hbm.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.lib.Library;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.network.TileEntityCableBaseNT;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGirder extends Block {
	public static int renderIDGirder = RenderingRegistry.getNextAvailableRenderId();
	public static int renderIDBracket = RenderingRegistry.getNextAvailableRenderId();


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

	@Override
	public int getRenderType() {
		if(this == ModBlocks.steel_girder) {
			return renderIDGirder;
		} else {
			return renderIDBracket;
		}
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
		if(this != ModBlocks.steel_girder_bracket) return;

		int meta = world.getBlockMetadata(x, y, z);
		int i = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if(i == 0) world.setBlockMetadataWithNotify(x, y, z, 1 + meta, 2);
		if(i == 1) world.setBlockMetadataWithNotify(x, y, z, 2 + meta, 2);
		if(i == 2) world.setBlockMetadataWithNotify(x, y, z, 3 + meta, 2);
		if(i == 3) world.setBlockMetadataWithNotify(x, y, z, 4 + meta, 2);

		System.out.println(world.getBlockMetadata(x, y, z));
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

		boolean posX = Library.canConnect(world, x + 1, y, z, Library.POS_X);
		boolean negX = Library.canConnect(world, x - 1, y, z, Library.NEG_X);
		boolean posZ = Library.canConnect(world, x, y, z + 1, Library.POS_Z);
		boolean negZ = Library.canConnect(world, x, y, z - 1, Library.NEG_Z);

		setBlockBounds(posX, negX, posZ, negZ, world.getBlockMetadata(x, y, z));
	}

	public boolean canConnect(IBlockAccess world, int x, int y, int z, ForgeDirection dir)
	{
		return world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == world.getBlockMetadata(x, y, z);
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
