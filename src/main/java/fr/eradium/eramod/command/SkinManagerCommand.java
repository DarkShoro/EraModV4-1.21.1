package fr.eradium.eramod.command;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.arguments.StringArgumentType;

import fr.eradium.eramod.util.SkinCacheManager;

@EventBusSubscriber
public class SkinManagerCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("eraskin")
			.then(Commands.literal("reload")
				.requires(source -> source.hasPermission(0)) // Allow everyone including console
				.executes(context -> {
					// Console can only use reload with arguments, not without
					if (!context.getSource().isPlayer()) {
						context.getSource().sendFailure(Component.translatable("skinapi.failed.console"));
						return 0;
					}
					return reloadOwnSkin(context);
				})
				.then(Commands.argument("player", EntityArgument.player())
					.executes(context -> reloadPlayerSkin(context))
				)
			)
			.then(Commands.literal("ignore")
				.requires(source -> source.isPlayer()) // Only players can use ignore
				.then(Commands.argument("player", EntityArgument.player())
					.executes(context -> ignorePlayer(context))
					.then(Commands.argument("type", StringArgumentType.word())
						.executes(context -> ignorePlayerSelective(context))
					)
				)
			)
			.then(Commands.literal("unignore")
				.requires(source -> source.isPlayer()) // Only players can use unignore
				.then(Commands.argument("player", EntityArgument.player())
					.executes(context -> unignorePlayer(context))
					.then(Commands.argument("type", StringArgumentType.word())
						.executes(context -> unignorePlayerSelective(context))
					)
				)
			)
		);
	}

	private static int reloadOwnSkin(CommandContext<CommandSourceStack> context) {
		try {
			// This method should only be called by players (checked in command registration)
			ServerPlayer player = context.getSource().getPlayerOrException();
			String playerName = player.getGameProfile().getName();
			SkinCacheManager.clearPlayerCache(playerName);
			context.getSource().sendSuccess(() -> Component.translatable("skinapi.refresh.self"), false);
			return 1;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.self", e.getMessage()));
			return 0;
		}
	}

	private static int reloadPlayerSkin(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
			String playerName = targetPlayer.getGameProfile().getName();
			SkinCacheManager.clearPlayerCache(playerName);
			context.getSource().sendSuccess(() -> Component.translatable("skinapi.refresh.other", playerName), false);
			return 1;
		} catch (CommandSyntaxException e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.player"));
			return 0;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.other", e.getMessage()));
			return 0;
		}
	}

	private static int ignorePlayer(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
			String playerName = targetPlayer.getGameProfile().getName();
			
			if (SkinCacheManager.isPlayerIgnored(playerName)) {
				context.getSource().sendFailure(Component.translatable("skinapi.failed.already.ignored", playerName));
				return 0;
			}
			
			SkinCacheManager.ignorePlayer(playerName);
			context.getSource().sendSuccess(() -> Component.translatable("skinapi.ignored", playerName), false);
			return 1;
		} catch (CommandSyntaxException e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.player"));
			return 0;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.ignore", e.getMessage()));
			return 0;
		}
	}

	private static int ignorePlayerSelective(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
			String playerName = targetPlayer.getGameProfile().getName();
			String type = StringArgumentType.getString(context, "type").toLowerCase();
			
			// Validate type argument
			if (!type.equals("skin") && !type.equals("cape") && !type.equals("both")) {
				context.getSource().sendFailure(Component.translatable("skinapi.failed.ignore.args"));
				return 0;
			}
			
			// Check if already ignored
			boolean alreadyIgnored = false;
			switch (type) {
				case "skin":
					alreadyIgnored = SkinCacheManager.isPlayerSkinIgnored(playerName);
					break;
				case "cape":
					alreadyIgnored = SkinCacheManager.isPlayerCapeIgnored(playerName);
					break;
				case "both":
					alreadyIgnored = SkinCacheManager.isPlayerIgnored(playerName);
					break;
			}
			
			if (alreadyIgnored) {
				context.getSource().sendFailure(Component.translatable("skinapi.failed.inlist", playerName, type));
				return 0;
			}
			
			SkinCacheManager.ignorePlayer(playerName, type);
			Component message = type.equals("both") ? 
				Component.translatable("skinapi.ignored", playerName) :
				Component.translatable("skinapi.ignored.specific", type, playerName);
			context.getSource().sendSuccess(() -> message, false);
			return 1;
		} catch (CommandSyntaxException e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.player"));
			return 0;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.ignore", e.getMessage()));
			return 0;
		}
	}

	private static int unignorePlayer(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
			String playerName = targetPlayer.getGameProfile().getName();
			
			if (!SkinCacheManager.isPlayerIgnored(playerName) && 
				!SkinCacheManager.isPlayerSkinIgnored(playerName) && 
				!SkinCacheManager.isPlayerCapeIgnored(playerName)) {
				context.getSource().sendFailure(Component.translatable("skinapi.failed.notignored", playerName));
				return 0;
			}
			
			SkinCacheManager.unignorePlayer(playerName);
			context.getSource().sendSuccess(() -> Component.translatable("skinapi.unignored", playerName), false);
			return 1;
		} catch (CommandSyntaxException e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.player"));
			return 0;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.unignore", e.getMessage()));
			return 0;
		}
	}

	private static int unignorePlayerSelective(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
			String playerName = targetPlayer.getGameProfile().getName();
			String type = StringArgumentType.getString(context, "type").toLowerCase();
			
			// Validate type argument
			if (!type.equals("skin") && !type.equals("cape") && !type.equals("both")) {
				context.getSource().sendFailure(Component.translatable("skinapi.failed.ignore.args"));
				return 0;
			}
			
			// Check if currently ignored
			boolean isIgnored = false;
			switch (type) {
				case "skin":
					isIgnored = SkinCacheManager.isPlayerSkinIgnored(playerName);
					break;
				case "cape":
					isIgnored = SkinCacheManager.isPlayerCapeIgnored(playerName);
					break;
				case "both":
					isIgnored = SkinCacheManager.isPlayerIgnored(playerName) || 
							   SkinCacheManager.isPlayerSkinIgnored(playerName) || 
							   SkinCacheManager.isPlayerCapeIgnored(playerName);
					break;
			}
			
			if (!isIgnored) {
				context.getSource().sendFailure(Component.translatable("skinapi.failed.notinlist", playerName, type));
				return 0;
			}
			
			SkinCacheManager.unignorePlayer(playerName, type);
			Component message = type.equals("both") ? 
				Component.translatable("skinapi.unignored", playerName) :
				Component.translatable("skinapi.unignored.specific", type, playerName);
			context.getSource().sendSuccess(() -> message, false);
			return 1;
		} catch (CommandSyntaxException e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.player"));
			return 0;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.translatable("skinapi.failed.unignore", e.getMessage()));
			return 0;
		}
	}
}