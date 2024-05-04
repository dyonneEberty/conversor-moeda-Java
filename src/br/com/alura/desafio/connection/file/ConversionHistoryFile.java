package br.com.alura.desafio.connection.file;


import br.com.alura.desafio.model.ConversionHistoryItem;

import java.io.FileWriter;
import java.io.IOException;

public class ConversionHistoryFile {

    public static void save(double baseValue, String baseCurrency, double convertedValue, String finalCurrency) {
        String nomeArquivo = "ConversionHistory.txt";
        ConversionHistoryItem item = new ConversionHistoryItem(baseValue, baseCurrency, convertedValue, finalCurrency);

        FileWriter escrita = null;
        try {
            escrita = new FileWriter(nomeArquivo, true);
            String itemToSave = item.toString();

            escrita.write(itemToSave);
            escrita.write("\n");
            System.out.println("Dados da conversão foram salvos com sucesso, ao arquivo " + nomeArquivo + "!");
        } catch (IOException e){
            System.out.println("Falha ao salvar dados da conversão, ao arquivo " + nomeArquivo + "!");
        } finally {
            if (escrita != null) {
                try {
                    escrita.close();
                } catch (IOException e) {
                    System.out.println("FileWriter pode ser finalizado.");
                }
            }
        }
    }
}
