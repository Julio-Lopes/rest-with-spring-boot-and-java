package br.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.controllers.PersonController;
import br.data.vo.v1.PersonVO;
import br.exceptions.NotfoundException;
import br.exceptions.ObjectNullException;
import br.mapper.DozerMapper;
import br.model.Person;
import br.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service
@SuppressWarnings("null")
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public PersonVO findById(Long id) throws Exception{
        logger.info("Finding one person");
        var entity = repository.findById(id).orElseThrow(() -> new NotfoundException("No records found for this ID"));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public List<PersonVO> findAll(){
        logger.info("Finding all people");
        var persons = DozerMapper.parseListObject(repository.findAll(), PersonVO.class);
        persons
            .stream()
            .forEach(p -> {
                try {
                    p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        return persons;
    }

    public PersonVO create(PersonVO person) throws Exception{
        if (person == null) throw new ObjectNullException();
        logger.info("Creating one person");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) throws Exception{
        if (person == null) throw new ObjectNullException();
        logger.info("updating one person");
        var entity = repository.findById(person.getKey()).orElseThrow(() -> new NotfoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) throws Exception{
        logger.info("Disabling one person");
        repository.disablePerson(id);

        var entity = repository.findById(id).orElseThrow(() -> new NotfoundException("No records found for this ID"));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id){
        logger.info("Deleting one person");
        var entity = repository.findById(id).orElseThrow(() -> new NotfoundException("No records found for this ID"));
        repository.delete(entity);
    }
}
