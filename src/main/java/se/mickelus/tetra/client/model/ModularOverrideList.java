package se.mickelus.tetra.client.model;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ItemLayerModel;
import se.mickelus.tetra.NBTHelper;
import se.mickelus.tetra.items.ItemModular;

public class ModularOverrideList extends ItemOverrideList {

    private Cache<CacheKey, IBakedModel> bakedModelCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    static ModularOverrideList INSTANCE = new ModularOverrideList();

    protected ModularOverrideList() {
        super(ImmutableList.of());
    }

    @Nonnull
    @Override
    public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, final ItemStack stack, final World world, final EntityLivingBase entity) {
        NBTTagCompound baseTag = NBTHelper.getTag(stack);
        IBakedModel result = originalModel;
        if(!baseTag.hasNoTags()) {
            CacheKey key = getCacheKey(stack, originalModel);

            try {
                result = bakedModelCache.get(key, () -> getOverrideModel(stack, world, entity, originalModel));
            } catch(ExecutionException e) {
                // do nothing, return original model
                e.printStackTrace();
            }
        }
        return result;
    }

    protected CacheKey getCacheKey(ItemStack stack, IBakedModel original) {
        return new CacheKey(original, stack);
    }

    protected IBakedModel getOverrideModel(ItemStack itemStack, World world, EntityLivingBase entity, IBakedModel original) {
        ItemModular item  = (ItemModular) itemStack.getItem();

        BakedWrapper wrapper = (BakedWrapper) original;
        ImmutableList<ResourceLocation> textures = item.getTextures(itemStack);

        return new ItemLayerModel(textures).bake(
                wrapper.getOriginalState(),
                wrapper.getOriginalFormat(),
                wrapper.getBakedTextureGetter());
    }

    protected static class CacheKey {

        final IBakedModel parent;
        final String data;

        protected CacheKey(IBakedModel parent, ItemStack stack) {
            this.parent = parent;
            this.data = getDataFromStack(stack);
        }

        protected String getDataFromStack(ItemStack stack) {
            return NBTHelper.getTag(stack).toString();
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }

            CacheKey cacheKey = (CacheKey) o;

            if(parent != null ? parent != cacheKey.parent : cacheKey.parent != null) {
                return false;
            }
            return data != null ? data.equals(cacheKey.data) : cacheKey.data == null;

        }

        @Override
        public int hashCode() {
            int result = parent != null ? parent.hashCode() : 0;
            result = 31 * result + (data != null ? data.hashCode() : 0);
            return result;
        }
    }
}
