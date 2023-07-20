package com.finalmelontoken.fmtauthserver.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalmelontoken.fmtauthserver.exception.BaseException;
import com.finalmelontoken.fmtauthserver.exception.ExceptionResponseDto;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        }catch (BaseException e){
            setErrorResponse(response, e);
        }
    }
    private void setErrorResponse(
            HttpServletResponse response,
            BaseException exception
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(exception.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ExceptionResponseDto errorResponse = ExceptionResponseDto.builder()
                .message(exception.getMessage())
                .status(exception.getHttpStatus().value())
                .error(exception.getHttpStatus())
                .build();
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}