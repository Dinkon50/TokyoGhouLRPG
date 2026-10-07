package com.tokyoghoul.rpg.screen;

import com.tokyoghoul.rpg.network.NetworkHandler;
import com.tokyoghoul.rpg.network.OriginPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OriginScreen extends Screen {
    public OriginScreen(){
        super(Component.literal("Выбор пути").withStyle(s->s.withBold(true)));
    }

    @Override protected void init(){
        int cx=width/2, cy=height/2;
        addRenderableWidget(Button.builder(Component.literal("§cСтать гулем"),
            b->choose(1)).bounds(cx-170,cy-35,160,30).build());
        addRenderableWidget(Button.builder(Component.literal("§fОстаться человеком"),
            b->choose(0)).bounds(cx+10,cy-35,160,30).build());
        addRenderableWidget(Button.builder(Component.literal("§dСтать полугулем"),
            b->choose(2)).bounds(cx-170,cy+5,160,30).build());
        addRenderableWidget(Button.builder(Component.literal("§bВступить в CCG"),
            b->choose(3)).bounds(cx+10,cy+5,160,30).build());
    }

    private void choose(int r){
        NetworkHandler.CHANNEL.sendToServer(new OriginPacket(r));
        onClose();
    }

    @Override public void render(GuiGraphics g,int x,int y,float pt){
        renderBackground(g);
        g.drawCenteredString(font,title,width/2,45,0xFFFFFF);
        g.drawCenteredString(font,Component.literal("Выбор определит твою прокачку и отношение NPC."),width/2,70,0xAAAAAA);
        g.drawCenteredString(font,Component.literal("Гуль: ярость и кагуне  |  CCG: квинке и боевой дух"),width/2,95,0xBBBBBB);
        g.drawCenteredString(font,Component.literal("Полугуль: путь гуля, но CCG не атакует тебя автоматически."),width/2,110,0xBBBBBB);
        super.render(g,x,y,pt);
    }
}
