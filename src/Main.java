import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Main {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите режим: (1) Шифрование (2) Расшифровка");
        int mode = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите путь к входному файлу: ");
        String inputPath = scanner.nextLine();
        if (!Files.exists(Paths.get(inputPath))) {
            System.out.println("Ошибка: Входной файл не существует.");
            return;
        }

        System.out.print("Введите путь к выходному файлу: ");
        String outputPath = scanner.nextLine();

        System.out.print("Введите ключ (неотрицательное целое число): ");
        int key;
        try {
            key = Integer.parseInt(scanner.nextLine());
            if (key < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Недопустимый ключ. Нужно неотрицательное целое число.");
            return;
        }

        try {
            if (mode == 1) {
                processFile(inputPath, outputPath, key, true);
                System.out.println("Шифрование завершено.");
            } else if (mode == 2) {
                processFile(inputPath, outputPath, key, false);
                System.out.println("Расшифровка завершена.");
            } else {
                System.out.println("Выбран недопустимый режим.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    private static void processFile(String inputPath, String outputPath, int key, boolean encrypt) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = encrypt ? encryptLine(line, key) : decryptLine(line, key);
                writer.write(processedLine);
                writer.newLine();
            }
        }
    }

    private static String encryptLine(String line, int key) {
        StringBuilder result = new StringBuilder();
        for (char ch : line.toCharArray()) {
            int index = ALPHABET.indexOf(ch);
            if (index != -1) {
                int newIndex = (index + key) % ALPHABET.length();
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private static String decryptLine(String line, int key) {
        StringBuilder result = new StringBuilder();
        for (char ch : line.toCharArray()) {
            int index = ALPHABET.indexOf(ch);
            if (index != -1) {
                int newIndex = (index - key + ALPHABET.length()) % ALPHABET.length();
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}