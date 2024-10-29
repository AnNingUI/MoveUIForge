package org.crychicteam.cibrary.content.armorset.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import org.crychicteam.cibrary.content.armorset.ISetEffect;
import org.crychicteam.cibrary.content.event.ItemHurtEffectResult;
import org.crychicteam.cibrary.content.event.StandOnFluidEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class KubeJSSetEffect implements ISetEffect {
    private static String identifier;
    private Consumer<LivingEntity> applyEffectConsumer = entity -> {};
    private Consumer<LivingEntity> removeEffectConsumer = entity -> {};
    private Consumer<LivingEntity> skillEffectConsumer = entity -> {};
    private Consumer<LivingEntity> releaseEffectConsumer = entity -> {};
    private Function<LivingEntity, ItemHurtEffectResult> itemHurtEffectFunction = entity -> ItemHurtEffectResult.unmodified();
    private Consumer<LivingEntity> startSprintingEffectConsumer = entity -> {};
    private Consumer<LivingEntity> stopSprintingEffectConsumer = entity -> {};
    private Consumer<LivingEntity> sprintingEffectConsumer = entity -> {};
    private Consumer<LivingEntity> normalTickingEffectEffectConsumer = entity -> {};
    private Consumer<LivingEntity> jumpEffectConsumer = entity -> {};
    private QuadConsumer<LivingEntity, Double, BlockState, BlockPos> landEffectConsumer = (entity, distance, landingBlock, pos) -> {};
    private Consumer<LivingEntity> sprintingJumpEffectConsumer = entity -> {};
    private BiConsumer<LivingEntity, LivingChangeTargetEvent> onTargetedEffectConsumer = (entity, changer) -> {};
    private BiConsumer<LivingEntity, StandOnFluidEvent> onStandOnFluidEffect = (entity, event) -> {};
    private BiConsumer<LivingEntity, ItemStackedOnOtherEvent> stackedOnOther = (entity, event) -> {};


    public void setApplyEffectConsumer(Consumer<LivingEntity> consumer) {
        this.applyEffectConsumer = consumer;
    }

    public void setRemoveEffectConsumer(Consumer<LivingEntity> consumer) {
        this.removeEffectConsumer = consumer;
    }

    public void setSkillEffectConsumer(Consumer<LivingEntity> consumer) {
        this.skillEffectConsumer = consumer;
    }

    public void setReleaseEffectConsumer(Consumer<LivingEntity> consumer) {
        this.releaseEffectConsumer = consumer;
    }

    public void setItemHurtEffectFunction(Function<LivingEntity, ItemHurtEffectResult> function) {
        this.itemHurtEffectFunction = function;
    }

    public void setStartSprintingEffectConsumer(Consumer<LivingEntity> consumer) {
        this.startSprintingEffectConsumer = consumer;
    }

    public void setStopSprintingEffectConsumer(Consumer<LivingEntity> consumer) {
        this.stopSprintingEffectConsumer = consumer;
    }

    public void setSprintingEffectConsumer(Consumer<LivingEntity> consumer) {
        this.sprintingEffectConsumer = consumer;
    }

    public void setNormalTickingEffectEffectConsumerEffectConsumer(Consumer<LivingEntity> consumer) {
        this.normalTickingEffectEffectConsumer = consumer;
    }

    public void setJumpEffectConsumer(Consumer<LivingEntity> consumer) {
        this.jumpEffectConsumer = consumer;
    }

    public void setLandEffectConsumer(QuadConsumer<LivingEntity, Double, BlockState, BlockPos> consumer) {
        this.landEffectConsumer = consumer;
    }

    public void setSprintingJumpEffectConsumer(Consumer<LivingEntity> consumer) {
        this.sprintingJumpEffectConsumer = consumer;
    }

    public void setOnTargetedEffectConsumer(BiConsumer<LivingEntity, LivingChangeTargetEvent> consumer) {
        this.onTargetedEffectConsumer = consumer;
    }

    public void setOnStandOnFluidEffect(BiConsumer<LivingEntity, StandOnFluidEvent> consumer) {
        this.onStandOnFluidEffect = consumer;
    }

    public void setStackedOnOther(BiConsumer<LivingEntity, ItemStackedOnOtherEvent> consumer) {
        this.stackedOnOther = consumer;
    }

    public void setIdentifier(String identifier) {
        KubeJSSetEffect.identifier = identifier;
    }

    @Override
    public void applyEffect(LivingEntity entity) {
        applyEffectConsumer.accept(entity);
    }

    @Override
    public void removeEffect(LivingEntity entity) {
        removeEffectConsumer.accept(entity);
    }

    @Override
    public void skillEffect(LivingEntity entity) {
        skillEffectConsumer.accept(entity);
    }

    @Override
    public void releaseEffect(LivingEntity entity) {
        releaseEffectConsumer.accept(entity);
    }

    @Override
    public ItemHurtEffectResult itemHurtEffect(LivingEntity entity, ItemStack item, int originalDamage) {
        return itemHurtEffectFunction.apply(entity);
    }

    @Override
    public void startSprintingEffect(LivingEntity entity) {
        startSprintingEffectConsumer.accept(entity);
    }

    @Override
    public void stopSprintingEffect(LivingEntity entity) {
        stopSprintingEffectConsumer.accept(entity);
    }

    @Override
    public void sprintingEffect(LivingEntity entity) {
        sprintingEffectConsumer.accept(entity);
    }

    @Override
    public void normalTickingEffect(LivingEntity entity) {
        normalTickingEffectEffectConsumer.accept(entity);
    }

    @Override
    public void jumpEffect(LivingEntity entity) {
        jumpEffectConsumer.accept(entity);
    }

    @Override
    public void landEffect(LivingEntity entity, double distance, BlockState landingBlock, BlockPos pos) {
        landEffectConsumer.accept(entity, distance, landingBlock, pos);
    }

    @Override
    public void sprintingJumpEffect(LivingEntity entity) {
        sprintingJumpEffectConsumer.accept(entity);
    }

    @Override
    public void onTargetedEffect(LivingEntity entity, LivingChangeTargetEvent changer) {
        onTargetedEffectConsumer.accept(entity, changer);
    }

    @Override
    public void onStandOnFluidEffect(LivingEntity entity, StandOnFluidEvent event) {
        onStandOnFluidEffect.accept(entity, event);
    }

    @Override
    public void stackedOnOther(LivingEntity entity, ItemStackedOnOtherEvent event) {
        stackedOnOther.accept(entity, event);
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @FunctionalInterface
    public interface QuadConsumer<A, B, C, D> {
        void accept(A a, B b, C c, D d);
    }
}
