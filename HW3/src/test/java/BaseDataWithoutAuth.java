public abstract class BaseDataWithoutAuth {
    public static String getImageHash() {
        return ImageHash;
    }

    public static void setImageHash(String imageHash) {
        ImageHash = imageHash;
    }

    public static String getDeleteImageHash() {
        return DeleteImageHash;
    }

    public static void setDeleteImageHash(String deleteImageHash) {
        DeleteImageHash = deleteImageHash;
    }

    static String ImageHash,DeleteImageHash;
}
