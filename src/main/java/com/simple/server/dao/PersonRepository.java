package com.simple.server.dao;

import com.simple.server.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {

	@Autowired
	HibernateOperations hibernate;

	public List<Person> getAll() {
		return hibernate.loadAll(Person.class);
	}

	public Person add(Person person) {
		hibernate.save(person);
		return person;
	}

	public void delete(Long id) {
		Person person = hibernate.get(Person.class, id);
		if (person != null) {
			hibernate.delete(person);
		}
	}

	public void update(Person person) {
		hibernate.update(person);
	}
}
