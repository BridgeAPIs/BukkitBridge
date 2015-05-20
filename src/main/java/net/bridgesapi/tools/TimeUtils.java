package net.bridgesapi.tools;

public class TimeUtils
{
    public static String formatTime(String baseMessage, int time)
    {
        int hours = (int) time / 3600;
        int remainder = (int) time - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        String timeStr = null;
        
        if (hours > 1)
        {
            if (secs == 0)
            {
                if (mins == 30 || mins == 0)
                {
                    timeStr = hours + "h" + mins;
                }
            }
        }
        else
        {
            if ((mins == 45 || mins == 30 || mins == 20 || mins == 10 || mins == 5 || mins == 3 || mins == 2 || mins == 1) && secs == 0)
            {
                timeStr = mins + " minutes";
            }
            
            if (mins == 1 && secs == 30)
            {
                timeStr = mins + " minutes et " + secs + " secondes";
            }
            
            if (mins == 0)
            {
                if (secs == 30 || secs == 20 || secs == 10 || (secs <= 5 && secs > 0))
                {
                    timeStr = secs + " secondes";
                }
            }
        }
        
        if (timeStr != null)
        {
            return baseMessage.replace("${TIME}", timeStr);
        }
        
        return "";
    }
}
