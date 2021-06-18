package me.murilo.ghignatti;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Kit implements Listener{
    
    private HashSet<UUID> players;

    private String kitName;

    private Material kitIcon;

    /**
     * The constructor is used to create the list of players using this kit
     */
    public Kit(String kitName){
        this.players = new HashSet<>();
        this.kitName = kitName;
        this.kitIcon = Material.AIR;
        this.loadConfig();
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
     * This is called to load the configuration of your plugin, all your variables that the user can change have to be here
     * @param kit your kit to copy
     */
    public abstract void copyFrom(Kit kit);

    /**
     * Generate the default config for your kit, this is called by MHungerGames
     */
    public void loadConfig(){
        try {
            File kitFolder = new File(new StringBuilder(Bukkit.getPluginManager().getPlugin("MHungerGames").getDataFolder().getAbsolutePath()).append("/").append("kits").append("/").append(this.kitName).toString());
            if(!kitFolder.exists()){
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(new File(new StringBuilder(Bukkit.getPluginManager().getPlugin("MHungerGames").getDataFolder().getAbsolutePath()).append("/").append("kits").append("/").append(this.kitName).append("/").append(this.kitName).append(".json").toString()), this);
            }
            else{
                File configFile = new File(new StringBuilder(Bukkit.getPluginManager().getPlugin("MHungerGames").getDataFolder().getAbsolutePath()).append("/").append("kits").append("/").append(this.kitName).append("/").append(this.kitName).append(".json").toString());
                ObjectMapper mapper = new ObjectMapper();
                if(!configFile.exists()){
                    mapper.writeValue(new File(new StringBuilder(Bukkit.getPluginManager().getPlugin("MHungerGames").getDataFolder().getAbsolutePath()).append("/").append("kits").append("/").append(this.kitName).append("/").append(this.kitName).append(".json").toString()), this);
                }
                this.copyFrom(mapper.readValue(configFile, getClass()));

            }
        } catch (IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        } catch (JsonGenerationException | JsonMappingException e) {
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

    public Material getKitIcon() {
        return kitIcon;
    }
}
