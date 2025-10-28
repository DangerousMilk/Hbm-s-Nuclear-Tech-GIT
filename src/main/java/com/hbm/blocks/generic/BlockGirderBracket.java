package com.hbm.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.lib.Library;
import com.hbm.lib.RefStrings;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGirderBracket extends BlockGirder {
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	public BlockGirderBracket(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	public int getRenderType() { return renderID; }

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

	public boolean canConnect(IBlockAccess world, int x, int y, int z, ForgeDirection dir)
	{
		return true;
		//return world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == world.getBlockMetadata(x, y, z);
	}
}
