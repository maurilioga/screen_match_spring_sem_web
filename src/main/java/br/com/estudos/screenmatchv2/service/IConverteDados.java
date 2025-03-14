package br.com.estudos.screenmatchv2.service;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe);
}
