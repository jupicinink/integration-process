package br.edu.unijui.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class CSVGenerator {

    private static final String OUTPUT_FILE = "./messages.csv";
    private static final int TOTAL_MESSAGES = 1000;

    private static final String[] EVENT_CONTENT = {
        "Início de viagem",
        "Fim de viagem",
        "Cliente recusou produto",
        "Veículo apresentou falha mecânica",
        "Entrega realizada com sucesso",
        "Atraso na entrega",
        "Rota alterada",
        "Parada não programada",
        "Documento fiscal emitido",
        "Carga danificada"
    };

    private static String generateEventContent(int i) {
        return EVENT_CONTENT[i % EVENT_CONTENT.length];
    }

    public static void main(String[] args) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE));

        writer.println("id,priority,creationDate,expirationDate,targetPort,sequence,content");

        Instant baseCreation = Instant.now();

        for (int i = 0; i < TOTAL_MESSAGES; i++) {

            String id = UUID.randomUUID().toString();
            String targetPort = UUID.randomUUID().toString();

            int priority = i % 5;

            Instant creationDate = baseCreation.plus(i, ChronoUnit.MINUTES);
            Instant expirationDate = creationDate.plus(1, ChronoUnit.DAYS);

            int sequence = i + 1;
            String content = generateEventContent(i);

            writer.printf("%s,%d,%s,%s,%s,%d,%s%n",
                    id,
                    priority,
                    creationDate.toString(),
                    expirationDate.toString(),
                    targetPort,
                    sequence,
                    content
            );
        }
        
        writer.close();
    }
}
