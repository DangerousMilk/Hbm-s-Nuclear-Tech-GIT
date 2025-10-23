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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGirder extends Block {
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	public BlockGirder(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	/*
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntity();
	}
	 */

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
		return renderID;
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
			return 0;
		else if(side == 1)
			// Top
			return 1;
		else if(hY > 0.5F)
			return 0;
		else if(hY < 0.5F)
			return 1;

		return  1;
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
		float minY = (meta == 1) ? 0F : pixel * 12F;
		float maxY = (meta == 1) ? pixel * 4F : 1F;
		float minZ = negZ ? 0F : min;
		float maxZ = posZ ? 1F : max;

		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
