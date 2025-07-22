package impl.util.font.glyph;

import com.google.gson.JsonObject;

public class Glyph {

    private int unicode;
    private float advance;

    private float planeLeft;
    private float planeBottom;
    private float planeRight;
    private float planeTop;

    private float atlasLeft;
    private float atlasBottom;
    private float atlasRight;
    private float atlasTop;

    public static Glyph parse(final JsonObject object) {
        final Glyph glyph = new Glyph();

        glyph.unicode = object.get("unicode").getAsInt();
        glyph.advance = object.get("advance").getAsFloat();

        if (object.has("planeBounds")) {
            glyph.planeLeft = object.get("planeBounds").getAsJsonObject().get("left").getAsFloat();
            glyph.planeBottom = object.get("planeBounds").getAsJsonObject().get("bottom").getAsFloat();
            glyph.planeRight = object.get("planeBounds").getAsJsonObject().get("right").getAsFloat();
            glyph.planeTop = object.get("planeBounds").getAsJsonObject().get("top").getAsFloat();
        }

        if (object.has("atlasBounds")) {
            glyph.atlasLeft = object.get("atlasBounds").getAsJsonObject().get("left").getAsFloat();
            glyph.atlasBottom = object.get("atlasBounds").getAsJsonObject().get("bottom").getAsFloat();
            glyph.atlasRight = object.get("atlasBounds").getAsJsonObject().get("right").getAsFloat();
            glyph.atlasTop = object.get("atlasBounds").getAsJsonObject().get("top").getAsFloat();
        }

        return glyph;
    }

    public final int getUnicode() {
        return this.unicode;
    }

    public final float getAdvance() {
        return this.advance;
    }

    public final float getPlaneLeft() {
        return this.planeLeft;
    }

    public final float getPlaneBottom() {
        return this.planeBottom;
    }

    public final float getPlaneRight() {
        return this.planeRight;
    }

    public final float getPlaneTop() {
        return this.planeTop;
    }

    public final float getAtlasLeft() {
        return this.atlasLeft;
    }

    public final float getAtlasBottom() {
        return this.atlasBottom;
    }

    public final float getAtlasRight() {
        return this.atlasRight;
    }

    public final float getAtlasTop() {
        return this.atlasTop;
    }
}
