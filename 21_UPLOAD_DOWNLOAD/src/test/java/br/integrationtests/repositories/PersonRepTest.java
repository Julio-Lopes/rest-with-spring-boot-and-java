package br.integrationtests.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.intTest.testContainers.AbsIntTest;
import br.model.Person;
import br.repositories.PersonRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepTest extends AbsIntTest{
    
    @Autowired
    PersonRepository repository;

    private static Person person;

    @BeforeAll
    public static void setup(){
        person = new Person();
    }

    @Test
	@Order(0)
	void testFindByName() throws JsonMappingException, JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));
		person = repository.findPersonByName("ayr", pageable).getContent().get(0);

		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertTrue(person.getEnabled());

		assertEquals(1, person.getId());

		assertEquals("Ayrton", person.getFirstName());
		assertEquals("Senna", person.getLastName());
		assertEquals("São Paulo", person.getAddress());
		assertEquals("Male", person.getGender());
	}

    @Test
	@Order(1)
	void testDisablePerson() throws JsonMappingException, JsonProcessingException {
        repository.disablePerson(person.getId());

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));
		person = repository.findPersonByName("ayr", pageable).getContent().get(0);

		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertFalse(person.getEnabled());

		assertEquals(1, person.getId());

		assertEquals("Ayrton", person.getFirstName());
		assertEquals("Senna", person.getLastName());
		assertEquals("São Paulo", person.getAddress());
		assertEquals("Male", person.getGender());
	}
}