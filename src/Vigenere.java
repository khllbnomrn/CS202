public class Vigenere {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String encrypt(String message, String key) {
        StringBuilder encryptedMessage = new StringBuilder();
        int keyIndex = 0;

        for (char ch : message.toCharArray()) {
            if (Character.isLetter(ch)) {
                int shift = ALPHABET.indexOf(key.charAt(keyIndex));
                int encryptedCharIndex = (ALPHABET.indexOf(ch) + shift) % ALPHABET.length();
                encryptedMessage.append(ALPHABET.charAt(encryptedCharIndex));
                keyIndex = (keyIndex + 1) % key.length();
            } else {
                encryptedMessage.append(ch);
            }
        }

        return encryptedMessage.toString();
    }

    public String decrypt(String encryptedMessage, String key) {
        StringBuilder decryptedMessage = new StringBuilder();
        int keyIndex = 0;

        for (char ch : encryptedMessage.toCharArray()) {
            if (Character.isLetter(ch)) {
                int shift = ALPHABET.indexOf(key.charAt(keyIndex));
                int decryptedCharIndex = (ALPHABET.indexOf(ch) - shift + ALPHABET.length()) % ALPHABET.length();
                decryptedMessage.append(ALPHABET.charAt(decryptedCharIndex));
                keyIndex = (keyIndex + 1) % key.length();
            } else {
                decryptedMessage.append(ch);
            }
        }

        return decryptedMessage.toString();
    }
}
