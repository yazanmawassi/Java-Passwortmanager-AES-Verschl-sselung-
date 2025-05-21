import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;
import org.json.JSONObject;

public class PasswordManager {

    private static final String KEY_FILE = "key.key";
    private static final String PASSWORD_FILE = "passwords.json";

    public static void generateKey() throws Exception {
        if (!Files.exists(Paths.get(KEY_FILE))) {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            Files.write(Paths.get(KEY_FILE), keyBytes);
        }
    }

    public static SecretKey loadKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(KEY_FILE));
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    public static void savePassword(String service, String username, String password) throws Exception {
        SecretKey key = loadKey();
        String encryptedPassword = encrypt(password, key);

        JSONObject data = new JSONObject();
        if (Files.exists(Paths.get(PASSWORD_FILE))) {
            String content = new String(Files.readAllBytes(Paths.get(PASSWORD_FILE)));
            if (!content.isEmpty()) {
                data = new JSONObject(content);
            }
        }

        JSONObject entry = new JSONObject();
        entry.put("username", username);
        entry.put("password", encryptedPassword);
        data.put(service, entry);

        Files.write(Paths.get(PASSWORD_FILE), data.toString(4).getBytes());
    }

    public static void getPassword(String service) throws Exception {
        if (!Files.exists(Paths.get(PASSWORD_FILE))) {
            System.out.println("No stored passwords found.");
            return;
        }

        String content = new String(Files.readAllBytes(Paths.get(PASSWORD_FILE)));
        JSONObject data = new JSONObject(content);
        if (data.has(service)) {
            JSONObject entry = data.getJSONObject(service);
            String username = entry.getString("username");
            String encrypted = entry.getString("password");
            String password = decrypt(encrypted, loadKey());
            System.out.printf("Service: %s\nUsername: %s\nPassword: %s\n", service, username, password);
        } else {
            System.out.println("Service not found.");
        }
    }

    public static void main(String[] args) {
        try {
            generateKey();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n1. Save Password\n2. Retrieve Password\n3. Exit");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    System.out.print("Service: ");
                    String service = scanner.nextLine();
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    savePassword(service, username, password);
                } else if (choice.equals("2")) {
                    System.out.print("Service to search: ");
                    String service = scanner.nextLine();
                    getPassword(service);
                } else if (choice.equals("3")) {
                    break;
                } else {
                    System.out.println("Invalid selection.");
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
