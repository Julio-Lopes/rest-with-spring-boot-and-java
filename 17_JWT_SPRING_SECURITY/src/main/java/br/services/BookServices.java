package br.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.controllers.BookController;
import br.data.vo.v1.BookVO;
import br.exceptions.NotfoundException;
import br.exceptions.ObjectNullException;
import br.mapper.DozerMapper;
import br.model.Book;
import br.repositories.BookRepository;

@Service
@SuppressWarnings("null")
public class BookServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    BookRepository repository;

    public BookVO findById(Long id) throws Exception{
        logger.info("Finding one Book");
        var entity = repository.findById(id).orElseThrow(() -> new NotfoundException("No records found for this ID"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public List<BookVO> findAll(){
        logger.info("Finding all Books");
        var books = DozerMapper.parseListObject(repository.findAll(), BookVO.class);
        books
            .stream()
            .forEach(p -> {
                try {
                    p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        return books;
    }

    public BookVO create(BookVO vo2) throws Exception{
        if (vo2 == null) throw new ObjectNullException();
        logger.info("Creating one Book");
        var entity = DozerMapper.parseObject(vo2, Book.class);
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) throws Exception{
        if (book == null) throw new ObjectNullException();
        logger.info("updating one Book");
        var entity = repository.findById(book.getKey()).orElseThrow(() -> new NotfoundException("No records found for this ID"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id){
        logger.info("Deleting one Book");
        var entity = repository.findById(id).orElseThrow(() -> new NotfoundException("No records found for this ID"));
        repository.delete(entity);
    }
}
