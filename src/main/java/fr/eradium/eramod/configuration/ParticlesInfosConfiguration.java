package fr.eradium.eramod.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ParticlesInfosConfiguration {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;

	public static final ModConfigSpec.ConfigValue<String> LIGHTATTAKEFFECT;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTATTAKYNUDGE;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTATTAKFWNUDGE;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTATTAKSCALE;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTATTAKYAW;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTATTAKPITCH;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTATTAKROLL;
	public static final ModConfigSpec.ConfigValue<String> LIGHTNINGEFFECT;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTNINGYNUDGE;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTNINGFWNUDGE;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTNINGSCALE;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTNINGYAW;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTNINGPITCH;
	public static final ModConfigSpec.ConfigValue<Double> LIGHTNINGROLL;
	static {
		BUILDER.push("particles");
		BUILDER.push("lightattack");
		LIGHTATTAKEFFECT = BUILDER.comment("The efkefc file with the particle inside").define("effect", "lightattak");
		LIGHTATTAKYNUDGE = BUILDER.comment("How much to move the particle downward").define("yNudge", (double) 0.5);
		LIGHTATTAKFWNUDGE = BUILDER.comment("How much to move the particle forward").define("fwNudge", (double) 2);
		LIGHTATTAKSCALE = BUILDER.comment("The scale of the particle").define("Scale", (double) 0.5);
		BUILDER.push("rotation");
		LIGHTATTAKYAW = BUILDER.comment("Yaw adjustement of the particle").define("yaw", (double) -90);
		LIGHTATTAKPITCH = BUILDER.comment("Pitch adjustement of the particle").define("pitch", (double) 0);
		LIGHTATTAKROLL = BUILDER.comment("Roll adjustement of the particle").define("roll", (double) 0);
		BUILDER.pop();
		BUILDER.pop();
		BUILDER.push("lightning");
		LIGHTNINGEFFECT = BUILDER.comment("The efkefc file with the particle inside").define("effect", "lightning");
		LIGHTNINGYNUDGE = BUILDER.comment("How much to move the particle downward").define("yNudge", (double) 0.1);
		LIGHTNINGFWNUDGE = BUILDER.comment("How much to move the particle forward").define("fwNudge", (double) 1);
		LIGHTNINGSCALE = BUILDER.comment("The scale of the particle").define("Scale", (double) 0.5);
		BUILDER.push("rotation");
		LIGHTNINGYAW = BUILDER.comment("Yaw adjustement of the particle").define("yaw", (double) -90);
		LIGHTNINGPITCH = BUILDER.comment("Pitch adjustement of the particle").define("pitch", (double) 0);
		LIGHTNINGROLL = BUILDER.comment("Roll adjustement of the particle").define("roll", (double) 0);
		BUILDER.pop();
		BUILDER.pop();
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}