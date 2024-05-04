package br.com.alura.desafio;

import br.com.alura.desafio.connection.api.ExchangerateApi;
import br.com.alura.desafio.connection.file.ConversionHistoryFile;
import br.com.alura.desafio.model.CurrencyType;
import br.com.alura.desafio.service.Converter;
import br.com.alura.desafio.service.JsonConverter;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Bem-vindo ao conversor de moedas!");
        System.out.println("Digite a moeda que deseja fazer a conversão:");
        CurrencyType baseCurrency = menu(input);

        System.out.println("Digite para qual moeda deseja realizar a conversão:");
        CurrencyType finalCurrency = menu(input);

        HttpRequest request = ExchangerateApi.createRequestToGetCurrencyRate(baseCurrency);
        HttpResponse<String> response = ExchangerateApi.getResponse(request);

        var rate = JsonConverter.getConversionRate(response.body(), finalCurrency);

        double baseValue = 0.0;
        System.out.println("Quantos " + baseCurrency.getDescription() + " gostaria de converter para " + finalCurrency.getDescription() + "?");
        try {
            baseValue = input.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Valor inválido. Finalizando programa...");
            System.exit(0);
        }

        double convertedValue = Converter.covert(baseValue, rate);

        System.out.printf("Esse valor corresponde a %.4f %s\n\n", convertedValue, finalCurrency.getDescription());

        ConversionHistoryFile.save(baseValue, baseCurrency.name(), convertedValue, finalCurrency.name());

    }

    private static CurrencyType menu(Scanner input) {
        while (true) {
            System.out.println("*********************************************");
            System.out.println("""
                    1 -> United States Dollar
                    2 -> Argentine Peso
                    3 -> Brazilian Real
                    4 -> Colombian Peso
                    + -> Mostrar mais opções
                    0 -> Sair
                    """);
            System.out.println("*********************************************");

            String op = input.nextLine();
            switch (op) {
                case "1":
                    return CurrencyType.fromDescription("United States Dollar");
                case "2":
                    return CurrencyType.fromDescription("Argentine Peso");
                case "3":
                    return CurrencyType.fromDescription("Brazilian Real");
                case "4":
                    return CurrencyType.fromDescription("Colombian Peso");
                case "+":
                    return extendedMenu(input);
                case "0":
                    System.out.println("Finalizando programa...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private static CurrencyType extendedMenu(Scanner input) {
        int initialOption = 1;
        int increment = 10;

        while (initialOption <= CurrencyType.lastItem()) {
            System.out.println("*********************************************");
            for (int i = initialOption - 1; i < (initialOption + increment - 1); i++) {
                if ((i + i) > CurrencyType.lastItem()) {
                    break;
                }
                System.out.println((i + 1) + " -> " + CurrencyType.fromID(i + 1).getDescription());
            }
            System.out.println("+ -> Mostrar mais opções");
            System.out.println("0 -> Sair");
            System.out.println("*********************************************");

            try {
                String escolha = input.nextLine();
                if (!escolha.equals("+") && (Integer.parseInt(escolha) >= initialOption && Integer.parseInt(escolha) <= (initialOption + increment - 1))) {
                    return CurrencyType.fromID(Integer.parseInt(escolha));
                } else if (escolha.equals("+")) {
                    initialOption += increment;
                } else if (escolha.equals("0")) {
                    System.out.println("Finalizando programa");
                    System.exit(0);
                } else {
                    System.out.println("Opção inválida.");
                }
            }  catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                System.out.println("Opção inválida.");
            }
        }
        System.out.println("Saindo do menu");
        return null;
    }
}
