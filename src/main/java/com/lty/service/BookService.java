package com.lty.service;

import com.lty.model.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lty
*/
public interface BookService extends IService<Book> {

    /**
     * 校验
     * @author lty
     *  13:33
     * @param book
     * @param add
     */
    void validBook(Book book,boolean add);
}
