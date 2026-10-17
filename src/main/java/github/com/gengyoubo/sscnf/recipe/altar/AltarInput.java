package github.com.gengyoubo.sscnf.recipe.altar;
public record AltarInput(net.minecraft.world.Container inventory) implements net.minecraft.world.item.crafting.RecipeInput {
    @Override public net.minecraft.world.item.ItemStack getItem(int index) { return inventory.getItem(index); }
    @Override public int size() { return inventory.getContainerSize(); }
}
