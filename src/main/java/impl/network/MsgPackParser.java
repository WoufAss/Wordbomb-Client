package impl.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgPackParser {
    private static final int TIMESTAMP_EXTENSION_TYPE = -1;
    private ByteBuffer buffer;

    public final Object parse(byte[] view, int offset) {
        this.buffer = ByteBuffer.wrap(view).order(ByteOrder.BIG_ENDIAN);
        this.buffer.position(offset);

        return parse();
    }

    private Object parse() {
        if (!buffer.hasRemaining())
            throw new RuntimeException("Out of bounds");

        final int typeCode = buffer.get() & 0xFF;
        if (typeCode < 0x80) {
            return parsePositiveFixInt(typeCode);
        }
        if (typeCode >= 0xE0) {
            return parseNegativeFixInt(typeCode);
        }
        if (typeCode < 0x90) {
            return parseFixMap(typeCode);
        }
        if (typeCode < 0xA0) {
            return parseFixArray(typeCode);
        }
        if (typeCode < 0xC0) {
            return parseFixString(typeCode);
        }

        return parseExplicitType(typeCode);
    }

    //
    public Object parseExplicitType(int typeCode) {
        return switch (typeCode) {
            // bool & null
            case 0xC0 -> null;
            case 0xC2 -> false;
            case 0xC3 -> true;

            // binary
            case 0xC4 -> parseBinary8();
            case 0xC5 -> parseBinary16();
            case 0xC6 -> parseBinary32();

            // extension
            case 0xC7 -> parseExtension8();
            case 0xC8 -> parseExtension16();
            case 0xC9 -> parseExtension32();

            // floats & doubles
            case 0xCA -> buffer.getFloat();
            case 0xCB -> buffer.getDouble();

            // unsigned integers
            case 0xCC -> parseUint8();
            case 0xCD -> parseUint16();
            case 0xCE -> parseUint32();
            case 0xCF -> parseUint64();

            //signed integers
            case 0xD0 -> parseInt8();
            case 0xD1 -> parseInt16();
            case 0xD2 -> parseInt32();
            case 0xD3 -> parseInt64();

            //fixed extension
            case 0xD4 -> parseFixedExtension(1);
            case 0xD5 -> parseFixedExtension(2);
            case 0xD6 -> parseFixedExtension(4);
            case 0xD7 -> parseFixedExtension(8); // bin
            case 0xD8 -> parseFixedExtension(16);

            //Strings
            case 0xD9 -> parseString8();
            case 0xDA -> parseString16();
            case 0xDB -> parseString32();

            //Arrays
            case 0xDC -> parseArray16();
            case 0xDD -> parseArray32();

            //maps
            case 0xDE -> parseMap16();
            case 0xDF -> parseMap32();
            default -> throw new RuntimeException("Unsupported MsgPack type: 0x" + Integer.toHexString(typeCode));
        };
    }


    /* read */

    private List<Object> readArray(int length) {
        final List<Object> result = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            result.add(parse());
        }

        return result;
    }

    private Map<String, Object> readMap(int length) {
        final Map<String, Object> result = new HashMap<>(length);

        for (int i = 0; i < length; i++) {
            final Object keyRaw = parse();
            if (!(keyRaw instanceof String)) throw new RuntimeException("Non-string map key found: " + keyRaw);

            final String key = (String) keyRaw;
            final Object val = parse();
            result.put(key, val);
        }

        return result;
    }

    public int getBufferPosition() {
        return buffer.position();
    }

    /* Parsers */
    private Object parsePositiveFixInt(final int typeCode) {
        return typeCode;
    }

    private Object parseNegativeFixInt(final int typeCode) {
        return (byte) typeCode;
    }

    private Object parseFixMap(final int typeCode) {
        final int size = typeCode & 0x0F;
        return readMap(size);
    }

    private Object parseFixArray(final int typeCode) {
        final int size = typeCode & 0x0F;
        return readArray(size);
    }

    private Object parseFixString(final int typeCode) {
        final int length = typeCode & 0x1F;
        return readString(length);
    }

    private byte[] readBin(final int length) {
        final byte[] data = new byte[length];
        buffer.get(data);

        return data;
    }

    private String readString(final int length) {
        return new String(readBin(length));
    }

    private Object parseBinary8() {
        final int length = buffer.get() & 0xFF;
        return readBin(length);
    }

    private Object parseBinary16() {
        final int length = buffer.getShort() & 0xFFFF;
        return readBin(length);
    }

    private Object parseBinary32() {
        final int length = (int) (buffer.getInt() & 0xFFFFFFFFL);
        return readBin(length);
    }

    private Object parseExtension8() {
        final int length = buffer.get() & 0xFF;
        final int extType = buffer.get();
        if (extType == TIMESTAMP_EXTENSION_TYPE)
            return parseTimestamp32();

        return new Object[]{extType, readBin(length)};
    }

    private Object parseExtension16() {
        final int length = buffer.getShort() & 0xFFFF;
        final int extType = buffer.get();

        return new Object[]{extType, readBin(length)};
    }

    private Object parseExtension32() {
        final int length = (int) (buffer.getInt() & 0xFFFFFFFFL);
        final int extType = buffer.get();

        return new Object[]{extType, readBin(length)};
    }

    // Integer parsers
    private Object parseUint8() {
        return buffer.get() & 0xFF;
    }

    private Object parseUint16() {
        return buffer.getShort() & 0xFFFF;
    }

    private Object parseUint32() {
        return Integer.toUnsignedLong(buffer.getInt());
    }

    private Object parseUint64() {
        return buffer.getLong();
    }

    private Object parseInt8() {
        return (int) buffer.get();
    }

    private Object parseInt16() {
        return (int) buffer.getShort();
    }

    private Object parseInt32() {
        return buffer.getInt();
    }

    private Object parseInt64() {
        return buffer.getLong();
    }

    private Object parseFixedExtension(int length) {
        final int extType = buffer.get();
        if (extType == TIMESTAMP_EXTENSION_TYPE)
            return parseTimestampExtension(length);

        return new Object[]{extType, readBin(length)};
    }

    private Object parseTimestampExtension(int length) {
        return switch (length) {
            case 4 -> parseTimestamp32();
            case 8 -> parseTimestamp64();
            default -> new Object[]{TIMESTAMP_EXTENSION_TYPE, readBin(length) };
        };
    }

    private Object parseTimestamp32() {
        final long seconds = Integer.toUnsignedLong(buffer.getInt());
        return Instant.ofEpochSecond(seconds);
    }

    private Object parseTimestamp64() {
        final long data = buffer.getLong();
        final long seconds = data & 0x3FFFFFFFFL;
        final long nanoseconds = (data >>> 34);

        return Instant.ofEpochSecond(seconds, nanoseconds);
    }

    private Object parseString8() {
        final int length = buffer.get() & 0xFF;
        return readString(length);
    }

    private Object parseString16() {
        final int length = buffer.getShort() & 0xFFFF;
        return readString(length);
    }

    private Object parseString32() {
        final int length = (int) (buffer.getInt() & 0xFFFFFFFFL);
        return readString(length);
    }

    private Object parseArray16() {
        final int length = buffer.getShort() & 0xFFFF;
        return readArray(length);
    }

    private Object parseArray32() {
        final int length = (int) (buffer.getInt() & 0xFFFFFFFFL);
        return readArray(length);
    }

    private Object parseMap16() {
        final int length = buffer.getShort() & 0xFFFF;
        return readMap(length);
    }

    private Object parseMap32() {
        final int length = (int) (buffer.getInt() & 0xFFFFFFFFL);
        return readMap(length);
    }
}