package net.redehollow.grenades.util.inventory.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.redehollow.grenades.util.inventory.InventoryBuilder;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * @author SrGutyerrez
 **/
public class ItemBuilder implements Listener {

    private ItemStack itemstack;
    private Consumer<InventoryClickEvent> eventConsumer;
    private HashMap<Action, Object> actions = new HashMap<>();
    private HashMap<Action, Object> rAction = new HashMap<>();
    private HashMap<Action, Object> lAction = new HashMap<>();
    private HashMap<Action, Object> SRAction = new HashMap<>();
    private HashMap<Action, Object> SLAction = new HashMap<>();
    private Boolean closeable = false;
    private Boolean editable = true;
    private Boolean cancellable = false;
    private Consumer<PlayerInteractEvent> interactEventConsumer;

    public ItemBuilder(){}

    public ItemBuilder(Material material){
        this.itemstack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack item){
        this.itemstack = item;
    }

    public ItemBuilder setType(Material material){
        if (itemstack == null) itemstack = new ItemStack(material); else itemstack.setType(material);
        return this;
    }

    public Boolean isCloseable(){
        return closeable;
    }

    public Boolean isEditable(){
        return editable;
    }

    public ItemBuilder setCloseable(Boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public ItemBuilder setCancel(Boolean b){
        this.cancellable = b;
        return this;
    }

    public Boolean isCancellable(){
        return cancellable;
    }

    public ItemBuilder setEditable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    public ItemBuilder setConsumer(Consumer<InventoryClickEvent> eventConsumer){
        this.eventConsumer = eventConsumer;
        return this;
    }

    public Consumer<InventoryClickEvent> getConsumer() {
        return eventConsumer;
    }

    public ItemBuilder setAction(Action action, Object value){
        this.actions.put(action, value);
        return this;
    }

    public ItemBuilder setRAction(Action action, Object value){
        this.lAction.put(action, value);
        return this;
    }

    public ItemBuilder setLAction(Action action, Object value){
        this.rAction.put(action, value);
        return this;
    }

    public ItemBuilder setSRAction(Action action, Object value){
        this.SLAction.put(action, value);
        return this;
    }

    public ItemBuilder setSLAction(Action action, Object value){
        this.SRAction.put(action, value);
        return this;
    }

    public ItemBuilder durability(Integer durability){
        itemstack.setDurability(Short.parseShort(durability.toString()));
        return this;
    }

    public ItemBuilder name(String name){
        ItemMeta meta = itemstack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemstack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... line){
        ItemMeta meta = itemstack.getItemMeta();
        List<String> lore = Arrays.asList(line);
        meta.setLore(lore);
        itemstack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<String> lore){
        ItemMeta meta = itemstack.getItemMeta();
        meta.setLore(lore);
        itemstack.setItemMeta(meta);
        return this;
    }

    public void getAction(Player player){
        if (actions.isEmpty()) return;
        for (Map.Entry<Action, ?> entry : actions.entrySet()){
            executeAction(entry.getKey(), player, entry.getValue());
        }
    }

    public void getRAction(Player player){
        if (rAction.isEmpty()) return;
        for (Map.Entry<Action, Object> entry : rAction.entrySet()){
            executeAction(entry.getKey(), player, entry.getValue());
        }
    }

    public void getLAction(Player player){
        if (lAction.isEmpty()) return;
        for (Map.Entry<Action, Object> entry : lAction.entrySet()){
            executeAction(entry.getKey(), player, entry.getValue());
        }
    }

    public void getSRAction(Player player){
        if (rAction.isEmpty()) return;
        for (Map.Entry<Action, Object> entry : SRAction.entrySet()){
            executeAction(entry.getKey(), player, entry.getValue());
        }
    }

    public void getSLAction(Player player){
        if (lAction.isEmpty()) return;
        for (Map.Entry<Action, Object> entry : SLAction.entrySet()){
            executeAction(entry.getKey(), player, entry.getValue());
        }
    }

    private void executeAction(Action action, Player player, Object object){
        switch (action){
            case EXECUTE: {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ((String) object).replaceAll("player", player.getName()));
                return;
            }
            case OPEN: {
                if (object instanceof Inventory) {
                    Inventory inventory = (Inventory) this.actions.get(action);
                    player.openInventory(inventory);
                } else if (object instanceof InventoryBuilder) {
                    InventoryBuilder inventoryBuilder = (InventoryBuilder) object;

                    inventoryBuilder.organize();

                    inventoryBuilder.open(player);
                }
                return;
            }
            case RUN: {
                player.chat(((String) object));
                return;
            }
            case CLOSE: {
                player.closeInventory();
                return;
            }
        }
    }

    public ItemBuilder setFireworkColor(Color color) {
        if (!itemstack.getType().equals(Material.FIREWORK_CHARGE))
            return this;
        FireworkEffectMeta meta = (FireworkEffectMeta) itemstack.getItemMeta();
        FireworkEffect newEffect = FireworkEffect.builder().withColor(color).build();
        meta.setEffect(newEffect);
        itemstack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(Integer amount){
        itemstack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, Integer level){
        itemstack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder hideAttributes(){
        ItemMeta meta = itemstack.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        itemstack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder color(Color color) {
        if (!itemstack.getType().name().contains("LEATHER_")) return this;
        LeatherArmorMeta meta = (LeatherArmorMeta) itemstack.getItemMeta();
        meta.setColor(color);
        itemstack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder banner(String letter, DyeColor baseColor, DyeColor letterColor){
        ItemStack banner = itemstack;
        if (!banner.getType().equals(Material.BANNER)) return this;
        letter = ChatColor.stripColor(letter.toUpperCase()).substring(0, 1);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.setBaseColor(baseColor);
        switch (letter) {
            case "A":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "B":
            case "8":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "C":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "D":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "E":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "F":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "G":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "H":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "I":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "J":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "K":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "L":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "M":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.TRIANGLE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "N":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "O":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "P":
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Q":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_RIGHT));
                break;
            case "R":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "S":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                break;
            case "T":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "U":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "V":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "W":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.TRIANGLE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "X":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Y":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Z":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "1":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_TOP_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "2":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "3":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "4":
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "5":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "6":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "7":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "9":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "0":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
        }
        banner.setItemMeta(bannerMeta);
        return this;
    }

    public ItemBuilder owner(String owner) {
        SkullMeta im = (SkullMeta) itemstack.getItemMeta();
        im.setOwner(owner);
        itemstack.setItemMeta(im);
        return this;
    }

    public ItemBuilder getSkull(String texture) {
        GameProfile profile = createGameProfile(texture, UUID.randomUUID());
        itemstack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = itemstack.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        if (!RefSet(headMetaClass, headMeta, "profile", profile)) return this;
        else
            itemstack.setItemMeta(headMeta);
            SkullMeta skullMeta = (SkullMeta) itemstack.getItemMeta();
            itemstack.setItemMeta(skullMeta);
        return this;
    }

    public GameProfile createGameProfile(String texture, UUID id) {
        GameProfile profile = new GameProfile(id, (String) null);
        PropertyMap propertyMap = profile.getProperties();
        propertyMap.put("textures", new Property("textures", texture));
        return profile;
    }

    public boolean RefSet(Class<?> sourceClass, Object instance, String fieldName, Object value) {
        try {
            Field field = sourceClass.getDeclaredField(fieldName);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            int modifiers = modifiersField.getModifiers();
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            if ((modifiers & 16) == 16) {
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, modifiers & -17);
            }

            try {
                field.set(instance, value);
            } finally {
                if ((modifiers & 16) == 16) {
                    modifiersField.setInt(field, modifiers | 16);
                }

                if (!field.isAccessible()) {
                    field.setAccessible(false);
                }

            }

            return true;
        } catch (Exception var11) {
            Bukkit.getLogger().log(Level.WARNING, "Unable to inject Gameprofile", var11);
            return false;
        }
    }

    public ItemStack build(){
        return itemstack;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;
        if (item.isSimilar(itemstack)){
            if (interactEventConsumer != null) interactEventConsumer.accept(event);
        }
    }
}
