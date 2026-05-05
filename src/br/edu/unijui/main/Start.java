package br.edu.unijui.main;

import br.edu.unijui.Message;
import br.edu.unijui.MessageLoader;
import br.edu.unijui.MessageManagerDB;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Avaliação
 * Disciplina de Programação para Camada de Negócio
 * @author Professor Rafael Zancan Frantz
 */
public class Start {


    public static void main(String[] args) throws UnknownHostException {

        // Cria uma lista de mensagens aleatórias a partir das mensagens lidas do arquivo CSV
        String filename = "./messages.csv";
        List<Message> list = MessageLoader.loadFromCSV(filename);

        // Armazena todas as mensagens criadas no banco de dados.
        MessageManagerDB.store(list);

        // Lista mensagens com a prioridade informada.
        MessageManagerDB.printMessages(Message.Priority.NORMAL);

    }

}
