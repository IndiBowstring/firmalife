package com.eerussianguy.firmalife.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.init.KnappingFL;
import com.eerussianguy.firmalife.init.PlantsFL;
import com.eerussianguy.firmalife.recipe.KnappingRecipeFood;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipe;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipeSimple;
import net.dries007.tfc.api.recipes.knapping.KnappingType;
import net.dries007.tfc.api.recipes.quern.QuernRecipe;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.registries.TFCRegistryEvent;
import net.dries007.tfc.api.types.Ore;
import net.dries007.tfc.api.types.Plant;
import net.dries007.tfc.objects.Powder;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.objects.items.ItemPowder;

import static com.eerussianguy.firmalife.FirmaLife.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class TFCRegistry
{
    private static final String TFC_ID = TerraFirmaCraft.MOD_ID;
    public static final ResourceLocation HALITE = new ResourceLocation(TFC_ID, "halite");

    @SubscribeEvent
    public static void onPreRegisterOre(TFCRegistryEvent.RegisterPreBlock<Ore> event)
    {
        IForgeRegistry<Ore> r = event.getRegistry();
        r.registerAll(
            new Ore(HALITE)
        );
    }

    @SubscribeEvent
    public static void onPreRegisterPlant(TFCRegistryEvent.RegisterPreBlock<Plant> event)
    {
        event.getRegistry().registerAll(
            PlantsFL.VANILLA_PLANT
        );
    }

    @SubscribeEvent
    public static void onRegisterQuernRecipeEvent(RegistryEvent.Register<QuernRecipe> event)
    {
        IForgeRegistry<QuernRecipe> r = event.getRegistry();

        r.register(new QuernRecipe(IIngredient.of("gemHalite"), new ItemStack(ItemPowder.get(Powder.SALT), 2)).setRegistryName("halite"));
        r.register(new QuernRecipe(IIngredient.of(ItemsFL.CINNAMON), new ItemStack(ItemsFL.GROUND_CINNAMON, 2)).setRegistryName("cinnamon"));
    }

    @SubscribeEvent
    public static void onRegisterKnappingRecipeEvent(RegistryEvent.Register<KnappingRecipe> event)
    {
        event.getRegistry().registerAll(
            new KnappingRecipeSimple(KnappingType.CLAY, true, new ItemStack(BlocksFL.OVEN), "XXXXX", "X   X", "X   X", "X   X", "XXXXX").setRegistryName("clay_oven"),
            new KnappingRecipeSimple(KnappingType.CLAY, true, new ItemStack(BlocksFL.OVEN_CHIMNEY), "XX XX", "X   X", "X   X", "X   X", "X   X").setRegistryName("clay_oven_chimney"),
            new KnappingRecipeSimple(KnappingType.CLAY, true, new ItemStack(BlocksFL.OVEN_WALL), "    X", "   XX", "   XX", "  XXX", "  XXX").setRegistryName("clay_oven_wall"),
            new KnappingRecipeSimple(KnappingType.STONE, true, new ItemStack(ItemsFL.NUT_HAMMER_HEAD), "     ", "XXXXX", "XXX X", "     ", "     ").setRegistryName("nut_hammer"),

            new KnappingRecipeFood(KnappingFL.PUMPKIN, true, new ItemStack(ItemsFL.PUMPKIN_SCOOPED), "XXXXX", "X   X", "X   X", "X   X", "XXXXX").setRegistryName("pumpkin_scoop"),
            new KnappingRecipeFood(KnappingFL.PUMPKIN, true, new ItemStack(ItemsFL.PUMPKIN_CHUNKS), "XX XX", "XX XX", "     ", "XX XX", "XX XX").setRegistryName("pumpkin_chunk")
        );

        event.getRegistry().registerAll(BlocksFL.getAllJackOLanterns().stream().map(j -> new KnappingRecipeSimple(KnappingFL.PUMPKIN, true, new ItemStack(Item.getItemFromBlock(j)), j.getCarving().getCraftPattern()).setRegistryName("pumpkin_carve_"+j.getCarving().getName())).toArray(KnappingRecipe[]::new));
    }

    @SubscribeEvent
    public static void onRegisterHeatRecipeEvent(RegistryEvent.Register<HeatRecipe> event)
    {
        IForgeRegistry<HeatRecipe> r = event.getRegistry();

        //Remove recipes
        IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) TFCRegistries.HEAT;
        String[] regNames = {"barley_bread", "cornbread", "oat_bread", "rice_bread", "rye_bread", "wheat_bread"};
        for (String name : regNames)
        {
            HeatRecipe recipe = TFCRegistries.HEAT.getValue(new ResourceLocation("tfc", name));
            if (recipe != null)
            {
                modRegistry.remove(recipe.getRegistryName());
                FirmaLife.logger.info("Removed heating recipe tfc:{}", name);
            }

        }
    }
}
