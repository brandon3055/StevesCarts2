package vswe.stevescarts.client.models.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelFarmer extends ModelCartbase
{
    public ModelFarmer(final ResourceLocation resource)
    {
        super(getTexturedModelData().bakeRoot(), resource);
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition mainAnchor = modelPartData.addOrReplaceChild("mainAnchor", CubeListBuilder.create(),
                PartPose.offset(18.0f, 4.0f, 0.0f));

        for (int i = -1; i <= 1; i += 2)
        {
            mainAnchor.addOrReplaceChild("smallarm" + i + i, CubeListBuilder.create().texOffs(26, 23)
                    .addBox(-1.0f, -1.0f, -1.0f, 8, 2, 2),
                    PartPose.offset(0.0f, 0.0f, i * 17));
        }

        mainAnchor.addOrReplaceChild("mainarm", CubeListBuilder.create().texOffs(0, 37)
                .addBox(-30.0f, -2.0f, -2.0f, 60f, 4f, 4f),
                PartPose.offsetAndRotation(8.0f, 0.0f, 0.0f, 0f, 1.5707964f, 0f));


        for (int j = -1; j <= 1; j += 2)
        {
            mainAnchor.addOrReplaceChild("extra" + j, CubeListBuilder.create().texOffs(26, 27)
                            .addBox(-2.5f, -2.5f, -1.0f, 5, 5, 2),
                    PartPose.offset(8.0f, 0.0f, j * 30));

            mainAnchor.addOrReplaceChild("bigarm" + j, CubeListBuilder.create().texOffs( 26, 17)
                            .addBox(-1.0f, -2.0f, -1.0f, 16, 4, 2),
                    PartPose.offset(8.0f, 0.0f, j * 32));
        }

        PartDefinition anchor = mainAnchor.addOrReplaceChild("anchor", CubeListBuilder.create(),
                PartPose.offset(22.0f, 0.0f, 0.0f));


        final float start = -1.5f;
        final float end = 1.5f;
        for (float k = -1.5f; k <= 1.5f; ++k)
        {
            for (int l = 0; l < 6; ++l)
            {
                anchor.addOrReplaceChild("side" + k + l, CubeListBuilder.create().texOffs( 0, 0)
                        .addBox(-5.0f, -8.8f, -1.0f, 10, 4, 2),
                        PartPose.offsetAndRotation(0.0f, 0.0f, k * 20.0f + l % 2 * 0.005f, 0, 0, l * 6.2831855f / 6.0f));
            }

            if (k == start || k == end)
            {
                anchor.addOrReplaceChild("sidecenter" + k, CubeListBuilder.create().texOffs( 0, 12)
                                .addBox(-6.0f, -6.0f, -0.5f, 12, 12, 1),
                        PartPose.offset(0.0f, 0.0f, k * 20.0f));
            }
            else
            {
                for (int l = 0; l < 3; ++l)
                {
                    anchor.addOrReplaceChild("sidecenter2" + k, CubeListBuilder.create().texOffs(26, 12)
                                    .addBox(-1.0f, -2.0f, -0.5f, 8, 4, 1),
                            PartPose.offsetAndRotation(0.0f, 0.0f, k * 20.0f, 0, 0, (l + 0.25f) * 6.2831855f / 3.0f));
                }
            }
        }

        for (int m = 0; m < 6; ++m)
        {
            PartDefinition middle = anchor.addOrReplaceChild("middle" + m, CubeListBuilder.create().texOffs( 0, 6)
                    .addBox(-30.0f, -1.7f, -1.0f, 60, 2, 2),
                    PartPose.offsetAndRotation(0.0f, 0.0f, m % 2 * 0.005f, m * 6.2831855f / 6.0f, 1.5707964f, 0));

            PartDefinition nailAnchor = anchor.addOrReplaceChild("nailAnchor" + m, CubeListBuilder.create(),
                    PartPose.offsetAndRotation(0, 0, 0, nailRot(m), 1.5707964f, 0));


            PartDefinition outer = nailAnchor.addOrReplaceChild("outer" + m, CubeListBuilder.create().texOffs(0, 10)
                    .addBox(-30.0f, -0.5f, -0.5f, 60, 1, 1),
                    PartPose.offsetAndRotation(0.0f, -8.8f, 0.0f, 3.1415927f, 0, 0));

            for (int j2 = -13; j2 <= 13; ++j2)
            {
                if (Math.abs(j2) > 6 || Math.abs(j2) < 4)
                {
                    outer.addOrReplaceChild("nail" + m + j2, CubeListBuilder.create().texOffs(44, 13)
                                    .addBox(-0.5f, -1.5f, -0.5f, 1, 3, 1),
                            PartPose.offset(j2 * 2, -2.0f, 0.0f));
                }
            }
        }
        return LayerDefinition.create(modelData, 128, 128);
    }

    private static float nailRot(final int i)
    {
        return (i + 0.5f) * 6.2831855f / 6.0f;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha)
    {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Quaternion.fromXYZ(0, 15.7f, 0));
        super.renderToBuffer(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}
