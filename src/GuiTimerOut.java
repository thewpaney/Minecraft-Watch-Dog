// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, World, WorldInfo, GuiButton, 
//            StatCollector, ISaveFormat, ISaveHandler, GuiMainMenu, 
//            EntityPlayerSP

public class GuiTimerOut extends GuiScreen
{

    public GuiTimerOut()
    {
    }

    public void initGui()
    {
        controlList.clear();
	controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, "Save and Quit"));
    if(mc.session == null)
	{
	    ((GuiButton)controlList.get(1)).enabled = false;
	}
    }

    protected void keyTyped(char c, int i)
    {
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id != 0);
        if(guibutton.id == 1)
        {
	    mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
            if(mc.isMultiplayerWorld())
		{
		    mc.theWorld.sendQuittingDisconnectingPacket();
		}
            mc.changeWorld1(null);
	    mc.shutdown();
	}
    }
    public void drawScreen(int i, int j, float f)
    {
        drawGradientRect(0, 0, width, height, 0x60000050, 0xa0030380);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        drawCenteredString(fontRenderer, "You're Out of Time!", width / 2 / 2, 30, 0xffffff);
        GL11.glPopMatrix();
	drawCenteredString(fontRenderer, "But that's ok, right?", width / 2, 144, 0xffffff);
        super.drawScreen(i, j, f);
    }

    public boolean doesGuiPauseGame()
    {
        return true;
    }
}
