import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

File serviceFile = new File(basedir, "target/classes/META-INF/services/javax.annotation.processing.Processor");

if (!serviceFile.exists()) {
    throw new Exception("Service file not generated at: " + serviceFile.getAbsolutePath());
}

BufferedReader reader = new BufferedReader(new FileReader(serviceFile));
String line;
boolean found = false;
while ((line = reader.readLine()) != null) {
    if (line.trim().equals("com.example.ExampleProcessor")) {
        found = true;
        break;
    }
}
reader.close();

if (!found) {
    throw new Exception("Processor com.example.ExampleProcessor not found in service file");
}

System.out.println("Integration test PASSED: Service file correctly generated");