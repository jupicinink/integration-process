package br.edu.unijui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Avaliação
 * Disciplina de Programação para Camada de Negócio
 * @author Stéfani Gabriele Arnold de Camargo, Julia da Silva Picinini e Professor Rafael Zancan Frantz
 */

public class MessageLoader {

    public static List<Message> loadFromCSV(String filepath) {

        List<Message> messages = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {

                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");

                UUID id = UUID.fromString(parts[0]);

                int priorityIndex = Integer.parseInt(parts[1]);
                Message.Priority priority = Message.Priority.values()[priorityIndex];

                Instant creationInstant = Instant.parse(parts[2]);
                Instant expirationInstant = Instant.parse(parts[3]);

                Date creationDate = Date.from(creationInstant);
                Date expirationDate = Date.from(expirationInstant);

                UUID targetPort = UUID.fromString(parts[4]);
                int sequence = Integer.parseInt(parts[5]);
                String content = parts[6];

                Message msg = new Message(id, creationDate);
                msg.setPriority(priority);
                msg.setExpirationDate(expirationDate);
                msg.setTargetPort(targetPort);
                msg.setSequenceNumber(sequence);
                msg.setContent(content);

                messages.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }
}