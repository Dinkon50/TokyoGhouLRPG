package com.tokyoghoul.rpg.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.tokyoghoul.rpg.capability.GhoulData;
import com.tokyoghoul.rpg.entity.ModEntities;
import com.tokyoghoul.rpg.network.AbilityPacket;
import com.tokyoghoul.rpg.network.KagunePacket;
import com.tokyoghoul.rpg.network.NetworkHandler;
import com.tokyoghoul.rpg.network.RagePacket;
import com.tokyoghoul.rpg.screen.ProgressionScreen;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.lwjgl.glfw.GLFW;

public final class ClientSetup {

    public static KeyMapping KAGUNE;
    public static KeyMapping PROGRESSION;
    public static KeyMapping RAGE;
    public static KeyMapping ABILITY_ONE;
    public static KeyMapping ABILITY_TWO;
    public static KeyMapping ABILITY_THREE;

    public static void init() {
        FMLJavaModLoadingContext.get()
                .getModEventBus()
                .register(ClientSetup.class);

        MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
    }

    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(
                ModEntities.GHOUL_NPC.get(),
                ctx -> new SimpleHumanoidRenderer<>(
                        ctx,
                        new ResourceLocation(
                                "minecraft",
                                "textures/entity/player/wide/steve.png"
                        )
                )
        );

        event.registerEntityRenderer(
                ModEntities.CCG_NPC.get(),
                ctx -> new SimpleHumanoidRenderer<>(
                        ctx,
                        new ResourceLocation(
                                "minecraft",
                                "textures/entity/player/wide/alex.png"
                        )
                )
        );
    }

    @SubscribeEvent
    public static void keys(RegisterKeyMappingsEvent event) {

        KAGUNE = new KeyMapping(
                "key.tokyoghoulrpg.kagune",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.tokyoghoulrpg"
        );

        PROGRESSION = new KeyMapping(
                "key.tokyoghoulrpg.progression",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.tokyoghoulrpg"
        );

        RAGE = new KeyMapping(
                "key.tokyoghoulrpg.rage",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                "key.categories.tokyoghoulrpg"
        );

        ABILITY_ONE = new KeyMapping(
                "key.tokyoghoulrpg.ability_one",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,
                "key.categories.tokyoghoulrpg"
        );

        ABILITY_TWO = new KeyMapping(
                "key.tokyoghoulrpg.ability_two",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_E,
                "key.categories.tokyoghoulrpg"
        );

        ABILITY_THREE = new KeyMapping(
                "key.tokyoghoulrpg.ability_three",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "key.categories.tokyoghoulrpg"
        );

        event.register(KAGUNE);
        event.register(PROGRESSION);
        event.register(RAGE);
14:15
event.register(ABILITY_ONE);
        event.register(ABILITY_TWO);
        event.register(ABILITY_THREE);
    }

    public static final class ForgeEvents {

        private ForgeEvents() {
        }

        @SubscribeEvent
        public static void input(InputEvent.Key event) {

            if (event.getAction() != GLFW.GLFW_PRESS) {
                return;
            }

            if (KAGUNE != null
                    && KAGUNE.matches(event.getKey(), event.getScanCode())) {

                NetworkHandler.CHANNEL.sendToServer(
                        new KagunePacket()
                );
            }

            if (PROGRESSION != null
                    && PROGRESSION.matches(event.getKey(), event.getScanCode())) {

                Minecraft.getInstance().setScreen(
                        new ProgressionScreen()
                );
            }

            if (RAGE != null
                    && RAGE.matches(event.getKey(), event.getScanCode())) {

                NetworkHandler.CHANNEL.sendToServer(
                        new RagePacket()
                );
            }

            if (ABILITY_ONE != null
                    && ABILITY_ONE.matches(event.getKey(), event.getScanCode())) {

                NetworkHandler.CHANNEL.sendToServer(
                        new AbilityPacket(0)
                );
            }

            if (ABILITY_TWO != null
                    && ABILITY_TWO.matches(event.getKey(), event.getScanCode())) {

                NetworkHandler.CHANNEL.sendToServer(
                        new AbilityPacket(1)
                );
            }

            if (ABILITY_THREE != null
                    && ABILITY_THREE.matches(event.getKey(), event.getScanCode())) {

                NetworkHandler.CHANNEL.sendToServer(
                        new AbilityPacket(2)
                );
            }
        }

        @SubscribeEvent
        public static void overlay(RenderGuiOverlayEvent.Post event) {

            if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) {
                return;
            }

            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null) {
                return;
            }

            if (ClientGhoulData.race() == GhoulData.Race.HUMAN) {
                return;
            }

            GuiGraphics g = event.getGuiGraphics();

            int x = mc.getWindow().getGuiScaledWidth() / 2 - 105;
            int y = mc.getWindow().getGuiScaledHeight() - 58;

            int w = 210;
            int h = 12;

            g.fill(
                    x - 1,
                    y - 1,
                    x + w + 1,
                    y + h + 1,
                    0xFF111111
            );

            g.fill(
                    x,
                    y,
                    x + w,
                    y + h,
                    0xFF333333
            );

            int fill = (int) (
                    w * (ClientGhoulData.rage() / 100.0f)
            );

            if (fill > 0) {
                g.fill(
                        x,
                        y,
                        x + fill,
                        y + h,
                        ClientGhoulData.race() == GhoulData.Race.CCG
                                ? 0xFF2580C8
                                : 0xFFE51C3A
                );
            }

            String race = switch (ClientGhoulData.race()) {
                case GHOUL -> "ГУЛЬ";
                case HALF_GHOUL -> "ПОЛУГУЛЬ";
                case CCG -> "CCG";
                default -> "ЧЕЛОВЕК";
            };

            String meter;

            if (ClientGhoulData.rageActive()) {

                meter =
                        (ClientGhoulData.race() == GhoulData.Race.CCG
                                ? "БОЕВОЙ ДУХ — "
                                : "ЯРОСТЬ — ")
                        + Math.max(
                                0,
14:15
ClientGhoulData.rageTicks() / 20
                        )
                        + "с";

            } else {

                meter =
                        (ClientGhoulData.race() == GhoulData.Race.CCG
                                ? "Боевой дух: "
                                : "Ярость: ")
                        + ClientGhoulData.rage()
                        + "%";
            }

            g.drawString(
                    mc.font,
                    race + " | Уровень "
                            + ClientGhoulData.level(),
                    x,
                    y - 23,
                    0xFFFFFFFF,
                    true
            );

            g.drawString(
                    mc.font,
                    meter,
                    x,
                    y - 11,
                    0xFFFFFFFF,
                    true
            );

            g.drawString(
                    mc.font,
                    "Q: способность   E: рывок   F: восстановление",
                    x,
                    y + 15,
                    0xFFDDDDDD,
                    false
            );
        }
    }

    private ClientSetup() {
    }
}
