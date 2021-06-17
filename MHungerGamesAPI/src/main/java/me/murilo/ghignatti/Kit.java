package me.murilo.ghignatti;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Kit implements Listener{
    
    private HashSet<UUID> players;

    private String kitName;

    /**
     * The constructor is used to create the list of players using this kit
     */
    public Kit(String kitName){
        this.players = new HashSet<>();
        this.kitName = kitName;
    }

    /**
     * 
     * @param player the player to give the kit
     * @return true if the player received the kit
     */
    public boolean givePlayerKit(Player player){
        return players.add(player.getUniqueId());
    }

    /**
     * 
     * @param player player to check
     * @return true if the player has this kit
     */
    public boolean playerHasKit(Player player){
        return players.contains(player.getUniqueId());
    }

    /**
     * 
     * @return true if this kit is beeing used by someone
     */
    public boolean checkUse(){
        return players != null && players.size() > 0;
    }

    /**
     * Generate the default config for your kit, this is called by MHungerGames
     */
    public void generateConfig(Class<? extends Kit> kitClass){
        try {
            Kit currentKit = kitClass.getDeclaredConstructor().newInstance();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(new StringBuilder(Bukkit.getPluginManager().getPlugin("MHungerGames").getDataFolder().getAbsolutePath()).append("/").append("kits").append("/").append(this.kitName).append(".json").toString()), currentKit);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getKitName() {
        return kitName;
    }
}
