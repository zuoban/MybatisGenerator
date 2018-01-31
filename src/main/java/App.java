import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class App {

    private static ResourceBundle generatorConfig = ResourceBundle.getBundle("generatorConfig");
    private static String tables = generatorConfig.getString("mybatis.generator.tableName");
    private static String base = generatorConfig.getString("package.base");
    private static String dao = generatorConfig.getString("package.suffix.dao");
    private static String mapper = generatorConfig.getString("package.suffix.mapper");
    private static String entity = generatorConfig.getString("package.suffix.entity");
    private static String deletePrevious= generatorConfig.getString("delete.previous");
    private static String previousFirstName= generatorConfig.getString("previous.package.firstName");
    private static File targetFile = new File("target");


    public static void main(String[] args) throws Exception {
        deletePrevious();
        printInfo();
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        InputStream configStream = ClassLoader.getSystemClassLoader().getResourceAsStream("generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configStream);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        if (tables != null && tables.length() > 0) {
            String[] tableNames = tables.trim().replace(" ", "").split(",");
            Context context = config.getContext("test");
            List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
            for (String tableName : tableNames) {
                TableConfiguration e = new TableConfiguration(context);
                e.setTableName(tableName);
                tableConfigurations.add(e);
            }
            context.getJavaModelGeneratorConfiguration().setTargetPackage(getPackage(base, entity));
            context.getJavaClientGeneratorConfiguration().setTargetPackage(getPackage(base, dao));
            context.getSqlMapGeneratorConfiguration().setTargetPackage(getPackage(base, mapper));

        }

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(new VerboseProgressCallback());
    }

    private static void deletePrevious() {
        if (Boolean.TRUE.toString().equals(deletePrevious)) {
            try {
                deleteDirectory(new File(targetFile, previousFirstName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("表名", tables);
        map.put("目标路径", targetFile.getAbsolutePath());
        printMapInfo(map);
    }

    private static String getPackage(String base, String sub) {
        return String.format("%s.%s", base, sub);
    }

    private static void printMapInfo(Map<String, String> map) {
        System.out.println("\n\n*******************************************************\n\n");
        map.forEach((key, value) -> System.out.println(String.format("%s : %s", key, value)));
        System.out.println("\n\n*******************************************************\n\n");
    }

    public static void cleanDirectory(File directory) throws IOException {
        String message;
        if (!directory.exists()) {
            message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        } else if (!directory.isDirectory()) {
            message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        } else {
            File[] files = directory.listFiles();
            if (files == null) {
                throw new IOException("Failed to list contents of " + directory);
            } else {
                IOException exception = null;

                for(int i = 0; i < files.length; ++i) {
                    File file = files[i];

                    try {
                        forceDelete(file);
                    } catch (IOException var6) {
                        exception = var6;
                    }
                }

                if (null != exception) {
                    throw exception;
                }
            }
        }
    }
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + file);
            }

            if (!file.delete()) {
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }

    }
    public static void deleteDirectory(File directory) throws IOException {
        if (directory.exists()) {
            cleanDirectory(directory);
            if (!directory.delete()) {
                String message = "Unable to delete directory " + directory + ".";
                throw new IOException(message);
            }
        }
    }
}
