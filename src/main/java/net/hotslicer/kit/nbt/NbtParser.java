package net.hotslicer.kit.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.experimental.UtilityClass;
import org.jglrxavpok.hephaistos.nbt.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jenya705
 */
@UtilityClass
public class NbtParser {

    public JsonObject toJson(NBT nbt) {
        JsonObject result = new JsonObject();
        NBTType<?> type = nbt.getID();
        result.addProperty("type", type.getOrdinal());
        if (type == NBTType.TAG_List) {
            NBTList<?> nbtList = (NBTList<?>) nbt;
            result.addProperty("subtype", nbtList.getSubtagType().getOrdinal());
        }
        result.add("value", parseValue(nbt));
        return result;
    }

    private JsonElement parseValue(NBT nbt) {
        NBTType<?> type = nbt.getID();
        if (nbt instanceof NBTNumber) {
            return new JsonPrimitive(((NBTNumber<?>) nbt).getValue().longValue());
        }
        else if (nbt instanceof NBTString) {
            return new JsonPrimitive(((NBTString) nbt).getValue());
        }
        else if (nbt instanceof NBTCompound compound) {
            JsonObject obj = new JsonObject();
            compound.asMapView().forEach((name, newNbt) ->
                    obj.add(name, toJson(newNbt))
            );
            return obj;
        }
        else if (type == NBTType.TAG_Int_Array) {
            JsonArray array = new JsonArray();
            ((NBTIntArray) nbt).forEach(array::add);
            return array;
        }
        else if (type == NBTType.TAG_Long_Array) {
            JsonArray array = new JsonArray();
            ((NBTLongArray) nbt).forEach(array::add);
            return array;
        }
        else if (type == NBTType.TAG_Byte_Array) {
            JsonArray array = new JsonArray();
            ((NBTByteArray) nbt).forEach(array::add);
            return array;
        }
        else if (type == NBTType.TAG_List) {
            JsonArray array = new JsonArray();
            ((NBTList<?>) nbt).forEach(it -> {
                JsonElement element = parseValue(nbt);
                if (element == null) return;
                array.add(element);
            });
            return array;
        }
        else {
            return null;
        }
    }

    private NBTByteArray byteArrayNbt(JsonArray array) {
        byte[] bytes = new byte[array.size()];
        Iterator<JsonElement> iterator = array.iterator();
        int i = 0;
        while (iterator.hasNext()) bytes[i++] = iterator.next().getAsByte();
        return new NBTByteArray(bytes);
    }

    private NBTIntArray intArrayNbt(JsonArray array) {
        int[] numbers = new int[array.size()];
        Iterator<JsonElement> iterator = array.iterator();
        int i = 0;
        while (iterator.hasNext()) numbers[i++] = iterator.next().getAsInt();
        return new NBTIntArray(numbers);
    }

    private NBTLongArray longArrayNbt(JsonArray array) {
        long[] numbers = new long[array.size()];
        Iterator<JsonElement> iterator = array.iterator();
        int i = 0;
        while (iterator.hasNext()) numbers[i++] = iterator.next().getAsLong();
        return new NBTLongArray(numbers);
    }

    @SuppressWarnings("unchecked")
    private <T extends NBT> NBTList<?> listNbt(JsonArray array, int type) {
        NBTType<T> nbtType = (NBTType<T>) NBTType.byIndex(type);
        List<T> list = new ArrayList<>();
        for (JsonElement jsonElement : array) list.add((T) fromJson(jsonElement.getAsJsonObject(), type));
        return new NBTList<>(nbtType, list);
    }

    public NBT fromJson(JsonObject json) {
        int type = json.get("type").getAsInt();
        return fromJson(json, type);
    }

    private NBT fromJson(JsonObject json, int type) {
        JsonElement value = json.get("value");
        return switch (type) {
            case 0 -> NBTEnd.INSTANCE;
            case 1 -> new NBTByte(value.getAsByte());
            case 2 -> new NBTShort(value.getAsShort());
            case 3 -> new NBTInt(value.getAsInt());
            case 4 -> new NBTLong(value.getAsInt());
            case 5 -> new NBTFloat(value.getAsFloat());
            case 6 -> new NBTDouble(value.getAsDouble());
            case 7 -> byteArrayNbt(value.getAsJsonArray());
            case 8 -> new NBTString(value.getAsString());
            case 9 -> listNbt(value.getAsJsonArray(), json.get("subtype").getAsInt());
            case 10 -> nbtCompound(value.getAsJsonObject());
            case 11 -> intArrayNbt(value.getAsJsonArray());
            case 12 -> longArrayNbt(value.getAsJsonArray());
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    private NBT nbtCompound(JsonObject json) {
        return new NBTCompound().modify(mutableNBTCompound ->
                json.entrySet().forEach(stringJsonElementEntry ->
                        mutableNBTCompound.put(
                                stringJsonElementEntry.getKey(),
                                fromJson(stringJsonElementEntry.getValue().getAsJsonObject())
                        )
                )
        );
    }

}
