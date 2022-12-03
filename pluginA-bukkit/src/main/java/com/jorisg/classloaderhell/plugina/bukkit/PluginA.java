package com.jorisg.classloaderhell.plugina.bukkit;

import com.jorisg.classloaderhell.library.LibraryObject;
import com.jorisg.classloaderhell.plugina.api.PluginAManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class PluginA extends JavaPlugin implements PluginAManager {

    @Override
    public void soSomething(LibraryObject object) {
        getLogger().log(Level.INFO, "Received value: {}", object.value());
    }

}
