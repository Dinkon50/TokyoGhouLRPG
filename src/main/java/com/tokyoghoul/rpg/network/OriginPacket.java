package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OriginPacket(int race){
    public static void encode(OriginPacket p,FriendlyByteBuf b){b.writeInt(p.race);}
    public static OriginPacket decode(FriendlyByteBuf b){return new OriginPacket(b.readInt());}

    public static void handle(OriginPacket p,Supplier<NetworkEvent.Context> c){
        c.get().enqueueWork(() -> {
            var s=c.get().getSender();
            if(s==null) return;
            GhoulData.get(s).ifPresent(d -> {
                GhoulData.Race r = switch(p.race){
                    case 1 -> GhoulData.Race.GHOUL;
                    case 2 -> GhoulData.Race.HALF_GHOUL;
                    case 3 -> GhoulData.Race.CCG;
                    default -> GhoulData.Race.HUMAN;
                };
                d.choose(r);
                d.sync(s);
                s.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    switch(r){
                        case GHOUL -> "§cТы выбрал путь гуля.";
                        case HALF_GHOUL -> "§dТы выбрал путь полугулю.";
                        case CCG -> "§bТы вступил в CCG. Твоя сила — квинке и тактика.";
                        default -> "§fТы остался человеком.";
                    }), false);
            });
        });
        c.get().setPacketHandled(true);
    }
}
