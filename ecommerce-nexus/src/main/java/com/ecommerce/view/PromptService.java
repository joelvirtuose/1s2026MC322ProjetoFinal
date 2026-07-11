package com.ecommerce.view;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.EndOfFileException;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Serviço responsável por encapsular toda a lógica de Input/Output do terminal.
 * Protege o resto do sistema de lidar diretamente com a API complexa do JLine.
 */
public class PromptService {
    private Terminal terminal;
    private LineReader lineReader;
    // Mantemos uma lista dinâmica de completers na memória do serviço
    private final List<Completer> dynamicCompleters;

    public PromptService() {
        try {
            // Constrói o terminal capturando o console nativo do Sistema Operacional
            this.terminal = TerminalBuilder.builder().system(true).build();
            this.dynamicCompleters = new ArrayList<>();

            // Criamos o aggregate completer acoplado à nossa lista mutável
            Completer rootCompleter = (reader, line, candidates) -> {
                for (Completer c : dynamicCompleters) {
                    c.complete(reader, line, candidates);
                }
            };
            
            // O lineReader é construído APENAS UMA VEZ aqui
            this.lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(rootCompleter)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o terminal JLine", e);
        }
    }

    /**
     * Atualiza dinamicamente os completers do JLine dependendo do estado da View ativa.
     * Resolve o erro de tipagem forçando a conversão limpa para array de interfaces.
     */
    public void updateCompleters(List<String> commands) {
        this.dynamicCompleters.clear();
        if (commands != null && !commands.isEmpty()) {
            this.dynamicCompleters.add(new StringsCompleter(commands));
        }
    }

    public String readString(String promptLabel) {
        try {
            String input = lineReader.readLine(promptLabel);
            return input == null ? "" : input.trim();
        } catch (UserInterruptException | EndOfFileException e) {
            // Captura o Ctrl+C/Ctrl+D e simula uma saída vazia ou comando de fechar
            return "sair"; 
        }
    }

    public String readMenuOption(String promptLabel) {
        try {
            String input = lineReader.readLine(promptLabel);
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }
        } catch (UserInterruptException | EndOfFileException e) {
            return "sair"; // Se o usuário interromper, joga o comando "sair" para o estado atual
        }

        // Mantém o Fallback nativo caso o JLine não bloqueie o terminal
        java.util.Scanner fallbackScanner = new java.util.Scanner(System.in);
        while (true) {
            System.out.print(promptLabel);
            if (!fallbackScanner.hasNextLine()) {
                return "sair";
            }
            String input = fallbackScanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void printHeader(String title) {
        System.out.println("=================================================================");
        System.out.println(" >> " + title);
        System.out.println("=================================================================");
    }

    public void printFooter() {
        System.out.println("=================================================================");
    }

    public void printInfo(String message) { System.out.println("[INFO] " + message); }
    public void printSuccess(String message) { System.out.println("\u001B[32m[SUCESSO] " + message + "\u001B[0m"); }
    public void printWarning(String message) { System.out.println("\u001B[33m[AVISO] " + message + "\u001B[0m"); }
    public void printError(String message) { System.out.println("\u001B[31m[ERRO] " + message + "\u001B[0m"); }

    public void printNumberedList(String label, List<String> options) {
        System.out.println(label);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + options.get(i));
        }
    }
}
