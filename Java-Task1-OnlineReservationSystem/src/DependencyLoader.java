import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * A pure Java implementation to replace pom.xml by automatically 
 * downloading required dependencies at runtime.
 */
public class DependencyLoader {

    private static final String MSSQL_URL = "https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/12.4.2.jre11/mssql-jdbc-12.4.2.jre11.jar";
    private static final String MSSQL_DLL_URL = "https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc_auth/12.4.2.x64/mssql-jdbc_auth-12.4.2.x64.dll";
    private static final String LIB_DIR = "lib";
    private static final String MSSQL_JAR = "mssql-jdbc.jar";
    private static final String MSSQL_DLL = "mssql-jdbc_auth-12.4.2.x64.dll";

    public static void loadDependencies() {
        try {
            Path libPath = Paths.get(LIB_DIR);
            if (!Files.exists(libPath)) {
                Files.createDirectories(libPath);
            }

            Path jarPath = libPath.resolve(MSSQL_JAR);
            if (!Files.exists(jarPath)) {
                System.out.println("Downloading Microsoft SQL Server JDBC driver...");
                try (InputStream in = new URL(MSSQL_URL).openStream()) {
                    Files.copy(in, jarPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            Path dllPath = libPath.resolve(MSSQL_DLL);
            if (!Files.exists(dllPath)) {
                System.out.println("Downloading SQL Server Windows Authentication DLL...");
                try (InputStream in = new URL(MSSQL_DLL_URL).openStream()) {
                    Files.copy(in, dllPath, StandardCopyOption.REPLACE_EXISTING);
                }
                System.out.println("Dependencies downloaded to 'lib' folder!");
            }
        } catch (Exception e) {
            System.err.println("Failed to download dependencies: " + e.getMessage());
        }
    }
}
