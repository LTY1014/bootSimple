package com.lty.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.common.ErrorCode;
import com.lty.exception.BusinessException;
import com.lty.mapper.BookMapper;
import com.lty.model.entity.Book;
import com.lty.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author lty
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
        implements BookService {

    @Override
    public void validBook(Book book, boolean add) {
        if (book == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 添加要验证的字段
        Long id = book.getId();
        String bookName = book.getBookName();

        if(add){
            if(StringUtils.isAnyBlank(bookName)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }
}




