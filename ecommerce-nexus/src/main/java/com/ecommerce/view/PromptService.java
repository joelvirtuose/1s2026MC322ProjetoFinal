package com.ecommerce.view;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável por encapsular toda a lógica de Input/Output do terminal.
 * Protege o resto do sistema de lidar diretamente com a API complexa do JLine.
 */
public class PromptService {
    private Terminal terminal;
    private LineReader lineReader;

    // Lista dinâmica mutável associada ao leitor persistente do terminal
    private final List<StringsCompleter> dynamicCompleters = new ArrayList<>();

    // Códigos ANSI para cores no terminal
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    public PromptService() {
        try {
            // Constrói o terminal capturando o console nativo do Sistema Operacional
            this.terminal = TerminalBuilder.builder().system(true).build();
            
            // Cria um agregador que observa referências mutáveis sem precisar recriar o LineReader
            this.lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new AggregateCompleter(dynamicCompleters))
                    .build();
        } catch (IOException e) {
            System.err.println("Erro crítico ao inicializar o terminal interativo: " + e.getMessage());
            System.exit(1);
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void printHeader(String title) {
        System.out.println(CYAN + "========================================" + RESET);
        System.out.println(CYAN + "  " + title + RESET);
        System.out.println(CYAN + "========================================" + RESET);
    }

    public void printFooter() {
        System.out.println(CYAN + "========================================\n" + RESET);
    }

    public void printInfo(String message) {
        System.out.println(message);
    }

    public void printSuccess(String message) {
        System.out.println(GREEN + "[SUCESSO] " + message + RESET);
    }

    public void printWarning(String message) {
        System.out.println(YELLOW + "[AVISO] " + message + RESET);
    }

    public void printError(String message) {
        System.out.println(RED + "[ERRO] " + message + RESET);
    }

    public void printNumberedList(String title, List<String> items) {
        System.out.println(title);
        for (int i = 0; i < items.size(); i++) {
            System.out.println(CYAN + " [" + (i + 1) + "] " + RESET + items.get(i));
        }
    }

    public String readString(String prompt) {
        return lineReader.readLine(prompt).trim();
    }

    /**
     * ATUALIZAÇÃO SEGURA: Altera o comportamento do autocompletar sem quebrar o estado interno da thread do JLine
     */
    public void setActiveCompleter(StringsCompleter completer) {
        this.dynamicCompleters.clear();
        this.dynamicCompleters.add(completer);
    }
}
