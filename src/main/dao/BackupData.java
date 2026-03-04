package main.dao;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupData {


    private static final String DB_NAME = "stock_management_system";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";
    private static final String PG_RESTORE_PATH = "C:\\Program Files\\PostgreSQL\\18\\bin\\pg_restore.exe";

    public static final String BACKUP_FOLDER =
            System.getProperty("user.dir") + File.separator + "database_backup";

    private static final String VERSION_FILE =
            BACKUP_FOLDER + File.separator + "version.txt";

    static {
        File folder = new File(BACKUP_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File versionFile = new File(VERSION_FILE);
        if (!versionFile.exists()) {
            try (FileWriter fw = new FileWriter(versionFile)) {
                fw.write("0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean backup() {
        String version = getNextVersion();
        String filePath = BACKUP_FOLDER + File.separator + "backup_v" + version + ".backup";

        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\18\\bin\\pg_dump.exe",
                "-U", USER,
                "-F", "c",
                "-f", filePath,
                DB_NAME
        );

        pb.environment().put("PGPASSWORD", PASSWORD);
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("PG_DUMP: " + line);
            }

            int result = process.waitFor();
            System.out.println("Exit Code: " + result);

            return result == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean restore(String version) {
        String filePath = BACKUP_FOLDER + File.separator + "backup_" + version + ".backup";

        ProcessBuilder pb = new ProcessBuilder(
                PG_RESTORE_PATH,
                "-U", USER,
                "-d", DB_NAME,
                "-c",
                filePath
        );

        pb.environment().put("PGPASSWORD", PASSWORD);
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("PG_RESTORE: " + line);
            }

            int result = process.waitFor();

            if (result == 0) {
                System.out.println("Restore successful.");
                return true;
            } else {
                System.out.println("Restore failed. Exit Code: " + result);
                return false;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static synchronized String getNextVersion() {
        int currentVersion = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(VERSION_FILE))) {
            currentVersion = Integer.parseInt(br.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        int nextVersion = currentVersion + 1;
        try (FileWriter fw = new FileWriter(VERSION_FILE)) {
            fw.write(String.valueOf(nextVersion));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(nextVersion);
    }
}