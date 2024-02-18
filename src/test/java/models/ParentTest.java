package models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ParentTest {

    @Test
    void objectToJsonTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    void jsonToObject() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Parent parent = objectMapper.readValue(new File("src/test/resources/bitwarden_export_20231002161839.json"), Parent.class);

        for (Item item : parent.getItems()) {
            System.out.println(item);
        }
    }

    @Test
    void jsonArrayToJava() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Parent parent = objectMapper.readValue(new File("src/test/resources/bitwarden_export_20231025111602.json"), Parent.class);
//        Parent parent = objectMapper.readValue(new File("src/test/resources/bitwarden_export_20231002161839.json"), Parent.class);
        Map<String, List<Item>> passwordUsedCount = parent.getPasswordUsedCount();
        for (String password : passwordUsedCount.keySet()) {
            List<Item> itemsWithSamePasswords = passwordUsedCount.get(password);
            if (itemsWithSamePasswords.size() > 1) {
                System.out.println();
                System.out.println("password: " + password + " count: " + itemsWithSamePasswords.size());
                for (Item item : itemsWithSamePasswords) {
                    System.out.println(item);
                }
                System.out.println("=======");
            }
        }
    }

}
