package br.unittests.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.data.vo.v1.BookVO;
import br.exceptions.ObjectNullException;
import br.model.Book;
import br.repositories.BookRepository;
import br.services.BookServices;
import br.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookTest {
    MockBook input;

    @InjectMocks
    private BookServices service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUpMock() throws Exception{
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() throws Exception {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
    }

    @Test
    void testFindAll(){
        List<Book> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        var book = service.findAll();
        assertNotNull(book);
        assertEquals(14, book.size());

        var bookOne = book.get(1);
        assertNotNull(bookOne);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getLinks());        
        assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", bookOne.getAuthor());
        assertNotNull(bookOne.getLaunchDate());
        assertEquals("Some Title1", bookOne.getTitle());
        assertEquals(25D, bookOne.getPrice());

        var bookFour = book.get(4);
        assertNotNull(bookFour);
        assertNotNull(bookFour.getKey());
        assertNotNull(bookFour.getLinks());        
        assertTrue(bookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
        assertEquals("Some Author4", bookFour.getAuthor());
        assertNotNull(bookFour.getLaunchDate());
        assertEquals("Some Title4", bookFour.getTitle());
        assertEquals(25D, bookFour.getPrice());

        var bookSeven = book.get(7);
        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getKey());
        assertNotNull(bookSeven.getLinks());        
        assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Some Author7", bookSeven.getAuthor());
        assertNotNull(bookSeven.getLaunchDate());
        assertEquals("Some Title7", bookSeven.getTitle());
        assertEquals(25D, bookSeven.getPrice());
    }

    @SuppressWarnings("null")
    @Test
    void testCreate() throws Exception {
        Book entity = input.mockEntity(1);
        Book persisted = entity;
        persisted.setId(1L);
        
        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());        
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
    }

    @Test
    void testUpdate() throws Exception {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1L);
        
        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());        
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
    }

    @Test
    void testDelete() throws Exception {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }

    @Test
    void testCreateWithNullBook() throws Exception {
        Exception exception = assertThrows(ObjectNullException.class, () -> service.create(null));

        String expectedMessage = "It is allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateWithNullBook() throws Exception {
        Exception exception = assertThrows(ObjectNullException.class, () -> service.update(null));

        String expectedMessage = "It is allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
