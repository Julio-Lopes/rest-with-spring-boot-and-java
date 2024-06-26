package br.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.data.vo.v1.PersonVO;
import br.data.vo.v2.PersonVOV2;
import br.exceptions.NotfoundException;
import br.mapper.DozerMapper;
import br.mapper.custom.PersonMapper;
import br.model.Person;
import br.repositories.PersonRepository;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    public PersonVO findById(Long id){
        logger.info("Finding one person");
        var entity = repository.findById(id).orElseThrow(() -> new NotfoundException("No records found for this ID"));
        return DozerMapper.parseObject(entity, PersonVO.class);

    }

    public List<PersonVO> findAll(){
        logger.info("Finding all people");
        return DozerMapper.parseListObject(repository.findAll(), PersonVO.class);
    }

    public PersonVO create(PersonVO person){
        logger.info("Creating one person");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    public PersonVO update(PersonVO person){
        logger.info("updating one person");
        var entity = repository.findById(person.getId()).orElseThrow(() -> new NotfoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    public void delete(Long id){
        logger.info("Deleting one person");
        var entity = repository.findById(id).orElseThrow(() -> new NotfoundException("No records found for this ID"));
        repository.delete(entity);
    }

    //v2

    public PersonVOV2 createV2(PersonVOV2 person){
        logger.info("Creating one person with V2");
        var entity = mapper.convertVoTOEntity(person);
        var vo = mapper.convertEntityToVo(repository.save(entity));
        return vo;
    }
}
