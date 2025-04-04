package example;

import db.Database;
import example.Document;

public class Main {
    public static void main(String[] args) {
        Document doc = new Document("Eid Eid Eid");

        try {
            Database.add(doc);

            System.out.println("Document added");

            System.out.println("id: " + doc.id);
            System.out.println("content: " + doc.content);
            System.out.println("creation date: " + doc.getCreationDate());
            System.out.println("last modification date: " + doc.getLastModificationDate());
            System.out.println();

            Thread.sleep(30_000);

            doc.content = "This is the new content";
            Database.update(doc);

            System.out.println("Document updated");
            System.out.println("id: " + doc.id);
            System.out.println("content: " + doc.content);
            System.out.println("creation date: " + doc.getCreationDate());
            System.out.println("last modification date: " + doc.getLastModificationDate());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}