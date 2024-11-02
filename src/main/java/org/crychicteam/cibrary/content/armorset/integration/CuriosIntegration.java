package org.crychicteam.cibrary.content.armorset.integration;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.events.server.ArmorSetHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;
import java.util.stream.Collectors;

public class CuriosIntegration {
    public static boolean isCuriosLoaded = ModList.get().isLoaded("curios");

    public static List<ItemStack> getAllItems(LivingEntity entity) {
        if (!isCuriosLoaded) return Collections.emptyList();
        return CuriosApi.getCuriosInventory(entity).resolve()
                .map(handler -> handler.getCurios().values().stream()
                        .flatMap(CuriosIntegration::streamStacks)
                        .filter(stack -> !stack.isEmpty())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public static boolean matchesCurioRequirements(LivingEntity entity, Map<Item, Integer> requiredCurios) {
        if (!isCuriosLoaded) return true;
        List<ItemStack> equippedCurios = getAllItems(entity);
        Map<Item, Integer> equippedCuriosCount = new HashMap<>();
        for (ItemStack stack : equippedCurios) {
            equippedCuriosCount.merge(stack.getItem(), 1, Integer::sum);
        }
        for (Map.Entry<Item, Integer> entry : requiredCurios.entrySet()) {
            Item requiredItem = entry.getKey();
            int requiredCount = entry.getValue();
            int equippedCount = equippedCuriosCount.getOrDefault(requiredItem, 0);
            if (equippedCount < requiredCount) {
                return false;
            }
        }
        return true;
    }

    @SubscribeEvent
    public void onCurioChangeEvent(CurioChangeEvent event) {
        if (!isCuriosLoaded) return;

        if (event.getEntity() instanceof ServerPlayer player) {
            if (Cibrary.ARMOR_SET_MANAGER != null) {
                Cibrary.ARMOR_SET_MANAGER.updateEntityISetEffect(player);
                ArmorSetHandler.syncArmorSet(player);
            } else {
                System.err.println("ArmorSetManager is null in CuriosIntegration.onCurioChangeEvent");
            }
        }
    }
    private static java.util.stream.Stream<ItemStack> streamStacks(ICurioStacksHandler stacksHandler) {
        IDynamicStackHandler stacks = stacksHandler.getStacks();
        int slots = stacks.getSlots();
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i < slots; i++) {
            itemStacks.add(stacks.getStackInSlot(i));
        }
        return itemStacks.stream();
    }

    public static int getSlot(LivingEntity entity, Item item) {
        if (!isCuriosLoaded) return -1;
        return CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(handler -> handler.findFirstCurio(item))
                .map(slotResult -> slotResult.slotContext().index())
                .orElse(-1);
    }

    public static boolean isPresent(LivingEntity entity, Item item) {
        return getSlot(entity, item) != -1;
    }

    public static int getSlotByIdentifier(LivingEntity entity, Item item, String identifier) {
        if (!isCuriosLoaded) return -1;
        return CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(handler -> Optional.ofNullable(handler.getCurios().get(identifier)))
                .map(stacksHandler -> {
                    IDynamicStackHandler stacks = stacksHandler.getStacks();
                    for (int i = 0; i < stacks.getSlots(); i++) {
                        if (stacks.getStackInSlot(i).getItem() == item) {
                            return i;
                        }
                    }
                    return -1;
                })
                .orElse(-1);
    }

    public static int getEmptySlotByIdentifier(LivingEntity entity, String identifier) {
        return getSlotByIdentifier(entity, Item.byId(0), identifier);
    }

    public static int getTotalSlotsByIdentifier(LivingEntity entity, String identifier) {
        if (!isCuriosLoaded) return 0;
        return CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(handler -> Optional.ofNullable(handler.getCurios().get(identifier)))
                .map(stacksHandler -> stacksHandler.getStacks().getSlots())
                .orElse(0);
    }

    public static ItemStack getStackInSlotByIdentifier(LivingEntity entity, String identifier, int slot) {
        if (!isCuriosLoaded) return ItemStack.EMPTY;
        return CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(handler -> Optional.ofNullable(handler.getCurios().get(identifier)))
                .map(stacksHandler -> stacksHandler.getStacks().getStackInSlot(slot))
                .orElse(ItemStack.EMPTY);
    }

    public static void setStackInSlotByIdentifier(LivingEntity entity, String identifier, int slot, ItemStack itemStack) {
        if (!isCuriosLoaded) return;
        CuriosApi.getCuriosInventory(entity).resolve()
                .ifPresent(handler -> handler.setEquippedCurio(identifier, slot, itemStack));
    }

    public static Map<String, Object> getCurioInfo(LivingEntity entity, String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("hasItem", false);
        result.put("count", 0);
        result.put("slots", new ArrayList<Integer>());

        if (!isCuriosLoaded) return result;

        List<ItemStack> curiosItems = getAllItems(entity);
        for (int i = 0; i < curiosItems.size(); i++) {
            ItemStack stack = curiosItems.get(i);
            if (!stack.isEmpty() && stack.getItem().toString().equals(id)) {
                result.put("hasItem", true);
                result.put("count", (int) result.get("count") + stack.getCount());
                ((List<Integer>) result.get("slots")).add(i);
            }
        }

        return result;
    }

    public static List<Map<String, Object>> getUniqueCuriosItems(LivingEntity entity) {
        if (!isCuriosLoaded) return Collections.emptyList();

        List<ItemStack> curiosItems = getAllItems(entity);
        Map<String, Map<String, Object>> uniqueItems = new HashMap<>();

        for (ItemStack stack : curiosItems) {
            if (!stack.isEmpty()) {
                String id = stack.getItem().toString();
                uniqueItems.computeIfAbsent(id, k -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", id);
                    item.put("count", 0);
                    item.put("name", stack.getHoverName().getString());
                    return item;
                });
                uniqueItems.get(id).put("count", (int) uniqueItems.get(id).get("count") + stack.getCount());
            }
        }

        return new ArrayList<>(uniqueItems.values());
    }

    public static boolean isWearing(LivingEntity entity, Object item) {
        if (!isCuriosLoaded) return false;
        if (item instanceof String) {
            return (boolean) getCurioInfo(entity, (String) item).get("hasItem");
        } else if (item instanceof Item) {
            return isPresent(entity, (Item) item);
        }
        return false;
    }

    public static int getCurioItemCount(LivingEntity entity, String itemId) {
        return (int) getCurioInfo(entity, itemId).get("count");
    }

    public static Map<String, Object> getIdentifierInfo(LivingEntity entity, String identifier) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalSlots", getTotalSlotsByIdentifier(entity, identifier));
        result.put("emptySlot", getEmptySlotByIdentifier(entity, identifier));
        return result;
    }

    public static Map<String, Boolean> areItemsPresent(LivingEntity entity, List<String> itemIds) {
        Map<String, Boolean> result = new HashMap<>();
        for (String id : itemIds) {
            result.put(id, isWearing(entity, id));
        }
        return result;
    }

    public static ItemStack safeGetCurioStack(LivingEntity entity, String identifier, int slot) {
        try {
            return getStackInSlotByIdentifier(entity, identifier, slot);
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }
}