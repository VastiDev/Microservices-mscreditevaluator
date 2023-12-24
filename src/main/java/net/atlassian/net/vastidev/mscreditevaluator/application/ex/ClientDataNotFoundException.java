package net.atlassian.net.vastidev.mscreditevaluator.application.ex;

public class ClientDataNotFoundException extends Exception{
    public ClientDataNotFoundException(String s) {
        super("Dados do cliente não encontrados para o CPF informado");
    }
}
