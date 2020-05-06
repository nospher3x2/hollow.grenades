package net.redehollow.grenades.util.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.redehollow.grenades.HollowGrenades;
import net.redehollow.grenades.util.inventory.item.Action;
import net.redehollow.grenades.util.inventory.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author SrGutyerrez
 **/
public class InventoryBuilder implements Listener {

    private Inventory inventory;
    private Inventory back;
    private Inventory outside;
    private HashMap<Integer, ItemBuilder> items = Maps.newHashMap();
    private Boolean cancel = false;
    private Boolean closed = false;
    private Boolean returnable = false;
    private HashMap<Integer, Character> pattern = Maps.newHashMap();
    private Consumer<InventoryCloseEvent> consumer;

    public InventoryBuilder() {
    }

    public InventoryBuilder(Integer rows, String name) {
        this.inventory = Bukkit.createInventory(null, rows * 9, name);
        Bukkit.getPluginManager().registerEvents(this, HollowGrenades.getInstance());
    }

    public InventoryBuilder setItem(Integer slot, ItemBuilder item) {
        this.items.put(slot, item);
        this.inventory.setItem(slot, item.build());
        return this;
    }

    public Integer getItemsSize() {
        return items.size();
    }

    public HashMap<Integer, ItemBuilder> getItems() {
        return items;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private Integer getNextSlot() {
        Integer current = this.items.size();
        return current;
    }

    public InventoryBuilder addItem(ItemBuilder item) {
        Integer slot = getNextSlot();
        this.items.put(slot, item);
        this.inventory.setItem(slot, item.build());
        return this;
    }

    public void setDesign(String... design) {
        Integer slot = 0;
        for (String current : design) {
            char[] charArray;
            for (int length = (charArray = current.toCharArray()).length, i = 0; i < length; ++i) {
                char letter = charArray[i];
                pattern.put(slot, letter);
                slot++;
            }
        }
    }

    public Integer getDesignLength(String... design) {
        Integer slot = 0;
        for (String current : design) {
            char[] charArray;
            for (int length = (charArray = current.toCharArray()).length, i = 0; i < length; ++i) {
                char letter = charArray[i];
                if (letter != 'X') slot++;
            }
        }
        return slot;
    }

    public void organize() {
        if (pattern.isEmpty()) return;
        HashMap<Integer, ItemBuilder> cloned = Maps.newHashMap();
        cloned.putAll(items);
        List<ItemBuilder> items1 = Lists.newArrayList();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i > pattern.size()) break;
            ItemBuilder item = cloned.get(i);
            if (item != null && item.isEditable()) {
                inventory.setItem(i, null);
                items.remove(i);
                items1.add(item);
            }
        }
        int slot = 0;
        for (Integer integer : pattern.keySet()) {
            char character = pattern.get(integer);
            if (character != 'X') {
                if (slot >= items1.size()) break;
                ItemBuilder item = items1.get(slot);
                inventory.setItem(integer, item.build());
                items.put(integer, item);
                slot++;
            }
        }
    }

    public void open(Player player) {
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        if (this.returnable && openInventory.getType() == InventoryType.CHEST) this.back = openInventory;
        if (this.inventory == null) return;
        organize();
        if (back != null && player.getOpenInventory().getTopInventory().getType().equals(InventoryType.CHEST))
            this.setItem(this.inventory.getSize() - 5, new ItemBuilder(Material.ARROW).name("§aVoltar").setAction(Action.OPEN, back));
        player.openInventory(inventory);
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public void setReturnable(Boolean returnable) {
        this.returnable = returnable;
    }

    public void setOutside(Inventory outside) {
        this.outside = outside;
    }

    public Inventory getOutside() {
        return outside;
    }

    public Consumer<InventoryCloseEvent> getConsumer() {
        return consumer;
    }

    public InventoryBuilder onClick(Consumer<InventoryCloseEvent> consumer) {
        return this.setConsumer(consumer);
    }

    public InventoryBuilder setConsumer(Consumer<InventoryCloseEvent> consumer) {
        this.consumer = consumer;
        return this;
    }

    @EventHandler
    void onClick(InventoryClickEvent event) {
        if (this.inventory == null) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().equals(inventory)) {
            if (event.getClickedInventory() == null && outside != null) {
                player.openInventory(outside);
                return;
            }
            if (event.getClick() == ClickType.NUMBER_KEY) {
                event.setCancelled(true);
                return;
            }
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
            if (this.cancel) event.setCancelled(true);
            ItemBuilder item = this.items.get(event.getRawSlot());
            if (item == null) return;
            if (item.isCancellable()) event.setCancelled(true);
            item.getAction(player);
            if (item.getConsumer() != null) item.getConsumer().accept(event);
            if (event.isRightClick()) item.getRAction(player);
            else if (event.isLeftClick()) item.getLAction(player);
            if (event.isShiftClick()) if (event.isRightClick()) item.getSRAction(player);
            else if (event.isLeftClick()) item.getSLAction(player);
            if (item.isCloseable()) player.closeInventory();
            if (this.closed) player.closeInventory();
            if (back != null && event.getSlot() == inventory.getSize() - 5) player.openInventory(back);
            return;
        }
    }

    @EventHandler
    void onClose(InventoryCloseEvent event){
        if (this.inventory == null) return;
        Player player = (Player) event.getPlayer();
        if (event.getInventory().equals(inventory)) {
            if (this.consumer != null) this.consumer.accept(event);
        }
    }
}
