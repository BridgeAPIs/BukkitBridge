package net.bridgesapi.tools.events;

import net.bridgesapi.api.BukkitBridge;

public class EventUtils
{
    public static void setEvent(Event event)
    {
        BukkitBridge.get().getResource().set("event:name", event.getName());
        BukkitBridge.get().getResource().set("event:description", event.getDescription());
        BukkitBridge.get().getResource().set("event:server", event.getServer());
        BukkitBridge.get().getResource().set("event:currently", String.valueOf(event.isCurrently()));
    }

    public static Event getCurrentEvent()
    {
        if(!isCurrentlyEvent())
            return null;

        String name = BukkitBridge.get().getResource().get("event:name");
        String description = BukkitBridge.get().getResource().get("event:description");
        String server = BukkitBridge.get().getResource().get("event:server");

        return new Event(name, description, server, true);
    }

    public static boolean isCurrentlyEvent()
    {
        if(! BukkitBridge.get().getResource().exists("event:currently"))
            return false;

        return Boolean.valueOf(BukkitBridge.get().getResource().get("event:currently"));
    }
}
