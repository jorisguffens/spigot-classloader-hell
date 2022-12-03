package com.jorisg.classloaderhell.pluginb.bukkit;

import com.jorisg.classloaderhell.library.LibraryObject;
import com.jorisg.classloaderhell.plugina.api.PluginAManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class PluginB extends JavaPlugin {

    @Override
    public void onEnable() {

        LibraryObject value = new LibraryObject(7);

        PluginManager pm = getServer().getPluginManager();
        if ( pm.isPluginEnabled("PluginA") ) {
            PluginAManager m = (PluginAManager) pm.getPlugin("PluginA");
            Objects.requireNonNull(m);
            m.soSomething(value);
        } else {
            fallback(value);
        }

    }

    public void fallback(LibraryObject object) {
        getLogger().log(Level.INFO,"PluginA is not enabled, falling back to default behaviour");
        getLogger().log(Level.INFO, "Received value: {0}", object.value());
    }
}
