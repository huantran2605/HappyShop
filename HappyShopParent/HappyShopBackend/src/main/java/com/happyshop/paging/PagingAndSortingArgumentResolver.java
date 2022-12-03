package com.happyshop.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethodAnnotation(PagingAndSortingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
            NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        
//        String sortDir = request.getParameter("sortDir");
//        String sortField =  request.getParameter("sortField");
//        String keyWord = request.getParameter("keyWord");
        System.out.println("dang xu li-----------");
//        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
//        model.addAttribute("reserveDir", reserveDir);
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDir", sortDir);
//        model.addAttribute("keyWord", keyWord);

        return new PagingAndSortingHelper();
    }

}
