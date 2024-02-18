package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Parent {

    private boolean encrypted;
    private List<Item> items;

    public Parent() {
        // Default constructor
    }

    public Parent(boolean encrypted, List<Item> items) {
        this.encrypted = encrypted;
        this.items = items;
    }
    @Override
    public String toString() {
        return "items: " + items;
    }
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Parent parent = objectMapper.readValue(new File("src/test/resources/bitwarden_export_20231002161839.json"), Parent.class);
        Map<String, List<Item>> passwordUsedCount = parent.getPasswordUsedCount();
        for (String password : passwordUsedCount.keySet()) {
            List<Item> itemsWithSamePasswords = passwordUsedCount.get(password);
            if (itemsWithSamePasswords.size() > 2) {
                for (Item item : itemsWithSamePasswords) {
                    System.out.print(item.getName() + " ");
                }
                System.out.println();
            }
        }
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Map<String, List<Item>> getPasswordUsedCount() {
        final Map<String, List<Item>> map = new HashMap<>();
        for (Item item : this.items) {
            final Login login = item.getLogin();
            if (login == null) {
                continue;
            }
            String password = login.getPassword();
            map.computeIfAbsent(password, k -> new ArrayList<>()).add(item);
        }
        return map;
    }
}
