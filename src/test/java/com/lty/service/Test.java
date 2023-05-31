package com.lty.service;

import com.lty.model.entity.Book;
import com.lty.utils.GsonUtil;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author lty
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Test {

    @org.junit.Test
    public void gsonTest() {
        // 对象转json字符串
        Book book = new Book();
        book.setId(1L).setBookName("name1").setAuthor("lty");
        String s = GsonUtil.objectToGson(book);
        System.out.println(s);

        // json字符串转对象
        String str = "{\"id\":1,\"bookName\":\"name1\",\"author\":\"lty\"}";
        Book book1 = GsonUtil.gsonToObject(str, Book.class);
        System.out.println(book1);
    }

    @org.junit.Test
    public void gsonTest1() {
        // json字符串转List<对象>
        String str = "[{\"id\":1,\"bookName\":\"name1\",\"author\":\"lty\"},{\"id\":2,\"bookName\":\"name2\",\"author\":\"liang\"}]";
        List<Book> bookList = GsonUtil.gsonToList(str, Book.class);
        System.out.println(bookList);
    }

    @org.junit.Test
    public void gsonTest2() {
        // List对象转json字符串
        Book book = new Book();
        Book book1 = new Book();
        Book book2 = new Book();
        book.setId(1L).setBookName("name1").setAuthor("liang");
        book1.setId(2L).setBookName("name2").setAuthor("tian");
        book2.setId(3L).setBookName("name3").setAuthor("yu");
        List<Book> list = Arrays.asList(book, book1, book2);
        String s = GsonUtil.listToGson(list);
        System.out.println(s);
    }
}