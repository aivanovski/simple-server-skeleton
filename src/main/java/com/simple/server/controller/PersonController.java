package com.simple.server.controller;

import com.simple.server.dao.PersonRepository;
import com.simple.server.model.Person;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("person")
public class PersonController extends SpringAwareResource {

	@Autowired
	PersonRepository repository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Person> getAllPersons() {
		return repository.getAll();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Person add(Person person) {
		return repository.add(person);
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") long id) {
		repository.delete(id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(@PathParam("id") long id, Person person) {
		person.setId(id);
		repository.update(person);
	}

}
