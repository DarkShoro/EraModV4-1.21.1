// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


public class dynamite extends EntityModel<Entity> {
	private final ModelRenderer dynamite;

	public dynamite() {
		textureWidth = 32;
		textureHeight = 32;

		dynamite = new ModelRenderer(this);
		dynamite.setRotationPoint(0.0F, 24.0F, 0.0F);
		dynamite.setTextureOffset(0, 0).addBox(-1.5F, -16.0F, -1.5F, 3.0F, 16.0F, 3.0F, 0.0F, false);
		dynamite.setTextureOffset(12, 3).addBox(-0.5F, -18.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		dynamite.setTextureOffset(12, 3).addBox(-1.5F, -19.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		dynamite.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}