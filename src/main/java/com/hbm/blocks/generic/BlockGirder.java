package com.hbm.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.lib.Library;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.network.TileEntityCableBaseNT;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGirder extends BlockContainer {
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	public BlockGirder(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntity();
	}

	@Override
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
		/*
		if(side == 0)
			return 7;

		if(side == 1)
			return 0;
		 */
		return (int)Math.floor(hY * 8D);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {

		boolean posX = Library.canConnect(world, x + 1, y, z, Library.POS_X);
		boolean negX = Library.canConnect(world, x - 1, y, z, Library.NEG_X);
		boolean posZ = Library.canConnect(world, x, y, z + 1, Library.POS_Z);
		boolean negZ = Library.canConnect(world, x, y, z - 1, Library.NEG_Z);

		setBlockBounds(posX, negX, false, false, posZ, negZ);

		return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {

		boolean posX = Library.canConnect(world, x + 1, y, z, Library.POS_X);
		boolean negX = Library.canConnect(world, x - 1, y, z, Library.NEG_X);
		boolean posZ = Library.canConnect(world, x, y, z + 1, Library.POS_Z);
		boolean negZ = Library.canConnect(world, x, y, z - 1, Library.NEG_Z);

		setBlockBounds(posX, negX, false, false, posZ, negZ);
	}

	private void setBlockBounds(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {

		float pixel = 0.0625F;
		float min = pixel * 5.5F;
		float max = pixel * 10.5F;

		float minX = negX ? 0F : min;
		float maxX = posX ? 1F : max;
		float minY = negY ? 0F : min;
		float maxY = posY ? 1F : max;
		float minZ = negZ ? 0F : min;
		float maxZ = posZ ? 1F : max;

		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
