package com.gylderia.gyldadvancements.WorldGuard;

import com.gylderia.gyldadvancements.WorldGuard.Handler.AdvancementFlagHandler;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import com.sk89q.worldguard.session.handler.EntryFlag;

public class AdvancementFlag {
    public static StringFlag advancementFlag;

    FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
    public void registerFlag() {
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StringFlag flag = new StringFlag("region-advancement");
            registry.register(flag);
            advancementFlag = flag;


        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("region-advancement");
            if (existing instanceof StringFlag) {
                advancementFlag = (StringFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
    }


}
