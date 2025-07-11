package br.com.biblios.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

// A anotação @WebFilter("/*") faz com que este filtro seja aplicado a TODAS as requisições da aplicação.
@WebFilter("/*")
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Define a codificação para a requisição (dados que vêm do formulário)
        request.setCharacterEncoding("UTF-8");
        
        // Define a codificação para a resposta (dados que vão para o navegador)
        response.setCharacterEncoding("UTF-8");
        
        // Continua o fluxo da requisição
        chain.doFilter(request, response);
    }

    // Métodos init e destroy podem ser deixados vazios para nosso caso.
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}