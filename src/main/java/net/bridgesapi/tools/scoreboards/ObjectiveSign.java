package net.bridgesapi.tools.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

/**
 * Created by Geekpower14 on 02/01/2015.
 */

// TODO : Rewrite this class (Not owned)
public class ObjectiveSign extends VObjective{

    public HashMap<Integer, String> lines = new HashMap<>();

    public ObjectiveSign(String name, String displayName) {
        super(name, displayName);

        for(int i = 0; i < 19; i++)
        {
            lines.put(i, null);
        }
    }

    public void setLine(int nb, String line)
    {
        VScore remove = getScore(lines.get(nb));
        scores.remove(remove);
        VScore add = getScore(line);
        add.setScore(nb);
        lines.put(nb, line);

        // replaceScore(remove, add);*/
    }

    public void updateLines()
    {
        String old = toggleName();
        for(OfflinePlayer op : receivers)
        {
            if(op.isOnline())
            {
                create(op.getPlayer());
                updateScore(op.getPlayer(), true);
                displayToSidebar(op.getPlayer());
                RawObjective.removeObjective(op.getPlayer(), old);
                //remove(op.getPlayer());
            }
        }
    }

    protected void replaceScore(VScore remove, VScore add)
    {
        scores.remove(remove);
        for(OfflinePlayer op : receivers)
        {
            if(op.isOnline())
            {
                RawObjective.updateScoreObjective(op.getPlayer(), this, add);
                RawObjective.removeScoreObjective(op.getPlayer(), this, remove);
            }
        }
    }


}
