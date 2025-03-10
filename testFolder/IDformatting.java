public class IDformatting {
    public static void main(String[] args) {
        String userId = "" + 'C';
        int count = 11;
        int FORMAT_DIGIT = 1000;
        FORMAT_DIGIT += count;
        System.out.println(Math.log10(FORMAT_DIGIT - count));
        userId += String.valueOf(FORMAT_DIGIT).substring(1, (int)Math.log10((double)(FORMAT_DIGIT - count)) + 1);
        System.out.println(userId);
    }
}
