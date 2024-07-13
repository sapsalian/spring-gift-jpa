package gift.resolver;

import gift.annotation.PageInfo;
import gift.annotation.TokenEmail;
import gift.dto.member.PageInfoDTO;
import gift.exception.InvalidPageRequestException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageInfoResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PageInfo.class);
    }

    @Override
    public PageInfoDTO resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageInfoDTO pageInfoDTO;

        try {
            pageInfoDTO = new PageInfoDTO(
                    extractPageNum(webRequest),
                    extractPageSize(webRequest),
                    extractSortType(webRequest),
                    extractSortOrder(webRequest)
            );
        } catch (Exception e) {
            throw new InvalidPageRequestException();
        }

        return pageInfoDTO;
    }

    private int extractPageNum(NativeWebRequest webRequest) {
        String pageParam = webRequest.getParameter("page");

        if (pageParam == null) {
            return 0;
        }

        return Integer.parseInt(pageParam);
    }

    private int extractPageSize(NativeWebRequest webRequest) {
        String pageSizeParam = webRequest.getParameter("size");

        if (pageSizeParam == null) {
            return 10;
        }

        return Integer.parseInt(pageSizeParam);
    }

    private Boolean extractSortOrder(NativeWebRequest webRequest) {
        String ascParam = webRequest.getParameter("asc");

        if (ascParam == null) {
            return true;
        }

        return Boolean.parseBoolean(ascParam);
    }

    private String extractSortType(NativeWebRequest webRequest) {
        String sortParam = webRequest.getParameter("sort");

        if (sortParam == null) {
            return "id";
        }

        //Todo sortType이 들어온 enum중에 존재하는지 확인

        return sortParam;
    }
}
